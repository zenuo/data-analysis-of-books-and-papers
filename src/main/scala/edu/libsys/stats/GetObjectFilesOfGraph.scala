package edu.libsys.stats

import edu.libsys.conf.Conf
import edu.libsys.util._
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * 获得七个图的顶点与边的对象文件
  */
object GetObjectFilesOfGraph {
  /**
    * 主方法
    *
    * @param args 控制台参数
    */
  def main(args: Array[String]): Unit = {
    //屏蔽不必要的日志输出
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

    //判断文件参数是否正确
    if (args.length != 2) {
      println("-------------------------------ERROR-------------------------------")
      println("Valid program arguments:")
      println("1.PATH_TO_SOURCE_DATA_DIRECTORY")
      println("2.PATH_TO_RESULT_DATA_DIRECTORY")
      println("Usage:\n/usr/local/spark/bin/spark-submit --class edu.libsys.stats.GetObjectFilesOfGraph --master local --executor-memory 52G --total-executor-cores 6 --conf spark.executor.heartbeatInterval=10000000 --conf spark.network.timeout=10000000 /home/spark/book-stats-1.0.jar /home/spark/data /home/spark/result")
      println("please try again, exit now.")
      println("-------------------------------------------------------------------")
      return
    }

    /**
      * spark会话
      */
    val spark: SparkSession = SparkSession
      .builder()
      .appName("GetObjectFilesOfGraph")
      .getOrCreate()
    //use the Kryo library to serialize objects
    spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sc: SparkContext = spark.sparkContext

    //文件路径
    //来源数据文件路径
    val book_id_author: String = args(0) + "/book_id_author.txt"
    val book_id_CLCId: String = args(0) + "/book_id_CLCId.txt"
    val cls_no_name: String = args(0) + "/cls_no_name.txt"
    val paper_id_paperId: String = args(0) + "/paper_id_paperId.txt"
    val paper_paperID_author: String = args(0) + "/paper_paperId_author.txt"
    val paper_paperId_field: String = args(0) + "/paper_paperId_field.txt"
    val paper_paperId_indexTerm: String = args(0) + "/paper_paperId_indexTerm.txt"

    //图书与论文在作者上的联系
    val bookAuthorIdRDD: RDD[(String, Int)] =
      GetBookAuthorIdRDD.work(book_id_author, sc).distinct()
    val paperAuthorIdRDD: RDD[(String, Int)] =
      GetPaperAuthorIdRDD.work(paper_id_paperId, paper_paperID_author, sc).distinct()
    val paperBookRelationshipByAuthor: RDD[Edge[Int]] = paperAuthorIdRDD
      .join(bookAuthorIdRDD, Conf.numTasks)
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
      })

    //图书与图书在作者上的联系
    val bookBookRelationshipByAuthor: RDD[Edge[Int]] = bookAuthorIdRDD
      .join(bookAuthorIdRDD, Conf.numTasks)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
      })
    //论文与论文在作者上的联系
    val paperPaperRelationshipByAuthor: RDD[Edge[Int]] = paperAuthorIdRDD
      .join(paperAuthorIdRDD, Conf.numTasks)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
      })

    //图书的中图法分类名与论文的关键词的联系
    val bookCLCNameIdRDD: RDD[(String, Int)] =
      GetBookCLCNameIdRDD.work(book_id_CLCId, cls_no_name, sc).distinct()
    val paperIndexTermIdRDD: RDD[(String, Int)] =
      GetPaperIndexTermIdRDD.work(paper_id_paperId, paper_paperId_indexTerm, sc).distinct()
    val paperBookRelationshipByIndexTermAndCLCName: RDD[Edge[Int]] = paperIndexTermIdRDD
      .join(bookCLCNameIdRDD, Conf.numTasks)
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
      })

    //论文与论文在关键词上的联系
    val paperPaperRelationshipByIndexTerm: RDD[Edge[Int]] = paperIndexTermIdRDD
      .join(paperIndexTermIdRDD, Conf.numTasks)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
      })

    //图书的中图法分类名与论文的领域名称的联系
    val paperFieldIdRDD: RDD[(String, Int)] =
      GetPaperFieldIdRDD.work(paper_id_paperId, paper_paperId_field, sc).distinct()
    val paperBookRelationshipByFieldAndCLCName: RDD[Edge[Int]] =
      paperFieldIdRDD
        .join(bookCLCNameIdRDD, Conf.numTasks)
        .map(tuple => {
          EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
        })

    //图书与图书在中图法分类号上的联系
    val bookCLCIdIdRDD: RDD[(String, Int)] =
      GetBookCLCIdIdRDD.work(book_id_CLCId, sc)

    val bookBookRelationshipByCLCId: RDD[Edge[Int]] = bookCLCIdIdRDD
      .join(bookCLCIdIdRDD, Conf.numTasks)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2)
      })


    //获得顶点为建图作准备
    //图书
    val bookVertices: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
    GetBookVertices.work(book_id_author, sc)
    //论文
    val paperVertices: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      GetPaperVertices.work(paper_id_paperId, sc)
    //获得顶点
    val vertices: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] = bookVertices
      .union(paperVertices)
    //vertices.saveAsObjectFile(args(1) + "/vertices")

    //建图，并按边的计数合并边

    val bookBookGraphByAuthor: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, bookBookRelationshipByAuthor)
        .groupEdges(merge = (count1, count2) => count1 + count2)
    bookBookGraphByAuthor.edges.saveAsObjectFile(args(1) + "/edgesOfBookBookGraphByAuthor")
    //Uncaches both vertices and edges of this graph.
    bookBookGraphByAuthor.unpersist()

    val bookBookGraphByCLCId: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, bookBookRelationshipByCLCId)
        .groupEdges(merge = (count1, count2) => count1 + count2)
    bookBookGraphByCLCId.edges.saveAsObjectFile(args(1) + "/edgesOfBookBookGraphByCLCId")
    bookBookGraphByCLCId.unpersist()

    val paperPaperGraphByAuthor: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, paperPaperRelationshipByAuthor)
        .groupEdges(merge = (count1, count2) => count1 + count2)
    paperPaperGraphByAuthor.edges.saveAsObjectFile(args(1) + "/edgesOfPaperPaperGraphByAuthor")
    paperPaperGraphByAuthor.unpersist()


    val paperPaperGraphByIndexTerm: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, paperPaperRelationshipByIndexTerm)
        .groupEdges(merge = (count1, count2) => count1 + count2)
    paperPaperGraphByIndexTerm.edges.saveAsObjectFile(args(1) + "/edgesOfPaperPaperGraphByIndexTerm")
    paperPaperGraphByIndexTerm.unpersist()

    val bookPaperGraphByAuthor: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, paperBookRelationshipByAuthor)
        .groupEdges(merge = (count1, count2) => count1 + count2)
    bookPaperGraphByAuthor.edges.saveAsObjectFile(args(1) + "/edgesOfBookPaperGraphByAuthor")
    paperPaperGraphByIndexTerm.unpersist()

    val bookPaperGraphByFieldAndCLCName: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, paperBookRelationshipByFieldAndCLCName)
        .groupEdges(merge = (count1, count2) => count1 + count2)
    bookPaperGraphByFieldAndCLCName.edges.saveAsObjectFile(args(1) + "/edgesOfBookPaperGraphByFieldAndCLCName")
    bookPaperGraphByFieldAndCLCName.unpersist()

    val bookPaperGraphByIndexTermAndCLCName: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, paperBookRelationshipByIndexTermAndCLCName)
        .groupEdges(merge = (count1, count2) => count1 + count2)
    bookPaperGraphByIndexTermAndCLCName.edges.saveAsObjectFile(args(1) + "/edgesOfBookPaperGraphByIndexTermAndCLCName")

    sc.stop()
    spark.stop()
  }
}
