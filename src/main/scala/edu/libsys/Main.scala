package edu.libsys

import edu.libsys.conf.Conf
import edu.libsys.stats._
import edu.libsys.util.{EdgeUtil, Filter, VertexUtil}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Edge, Graph, VertexId, VertexRDD}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object Main {
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
      println("Usage:\n/usr/local/spark/bin/spark-submit --class edu.libsys.Main --master local --executor-memory 52G --total-executor-cores 6 --conf spark.executor.heartbeatInterval=10000000 --conf spark.network.timeout=10000000 /home/spark/book-stats-1.0.jar /home/spark/data /home/spark/result")
      println("please try again, exit now.")
      println("-------------------------------------------------------------------")
      return
    }

    /**
      * spark会话
      */
    val spark: SparkSession = SparkSession
      .builder()
      .appName("MainStats")
      .getOrCreate()

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
    //结果数据文件保存路径
    val booksResultPath: String = args(1) + "/books"
    val papersResultPath: String = args(1) + "/papers"
    val bookBookRelationshipsResultPath: String = args(1) + "/bookBookRelationships"
    val paperPaperRelationshipsResultPath: String = args(1) + "/paperPaperRelationships"
    val bookPaperRelationshipsResultPath: String = args(1) + "/bookPaperRelationships"

    //图书与论文在作者上的联系
    val bookAuthorIdRDD: RDD[(String, Int)] = GetBookAuthorIdRDD.work(book_id_author, sc).distinct()
    val paperAuthorIdRDD: RDD[(String, Int)] = GetPaperAuthorIdRDD.work(paper_id_paperId, paper_paperID_author, sc).distinct()
    val paperBookRelationshipByAuthor: RDD[Edge[Int]] = paperAuthorIdRDD
      .join(bookAuthorIdRDD, Conf.numTasks)
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfPaperBookRelationshipByAuthor)
      })
    //图书与图书在作者上的联系
    val bookBookRelationshipByAuthor: RDD[Edge[Int]] = bookAuthorIdRDD
      .join(bookAuthorIdRDD, Conf.numTasks)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfBookBookRelationshipByAuthor)
      })
    //论文与论文在作者上的联系
    val paperPaperRelationshipByAuthor: RDD[Edge[Int]] = paperAuthorIdRDD
      .join(paperAuthorIdRDD, Conf.numTasks)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfPaperPaperRelationshipByAuthor)
      })

    //图书的中图法分类名与论文的关键词的联系
    val bookCLCNameIdRDD: RDD[(String, Int)] = GetBookCLCNameIdRDD.work(book_id_CLCId, cls_no_name, sc).distinct()
    val paperIndexTermIdRDD: RDD[(String, Int)] = GetPaperIndexTermIdRDD.work(paper_id_paperId, paper_paperId_indexTerm, sc).distinct()
    val paperBookRelationshipByIndexTermAndCLCName: RDD[Edge[Int]] = paperIndexTermIdRDD
      .join(bookCLCNameIdRDD, Conf.numTasks)
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfPaperBookRelationshipByIndexTermAndCLCName)
      })

    //论文与论文在关键词上的联系
    val paperPaperRelationshipByIndexTerm: RDD[Edge[Int]] = paperIndexTermIdRDD
      .join(paperIndexTermIdRDD, Conf.numTasks)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfPaperPaperRelationshipByIndexTerm)
      })

    //图书的中图法分类名与论文的领域名称的联系
    val paperFieldIdRDD: RDD[(String, Int)] = GetPaperFieldIdRDD.work(paper_id_paperId, paper_paperId_field, sc).distinct()
    val paperBookRelationshipByFieldAndCLCName: RDD[Edge[Int]] = paperFieldIdRDD
      .join(bookCLCNameIdRDD, Conf.numTasks)
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfPaperBookRelationshipByFieldAndCLCName)
      })

    //论文与论文在领域名称上的联系
    val paperPaperRelationshipByField: RDD[Edge[Int]] = paperFieldIdRDD
      .join(paperFieldIdRDD, Conf.numTasks)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfPaperPaperRelationshipByField)
      })

    //图书与图书在中图法分类号上的联系
    val bookCLCIdCLCNameTupleList: RDD[(String, Int)] = stats.GetBookCLCIdIdRDD.work(book_id_CLCId, sc)
    val bookBookRelationshipByCLCId: RDD[Edge[Int]] = bookCLCIdCLCNameTupleList
      .join(bookCLCIdCLCNameTupleList, Conf.numTasks)
      .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
      .map(tuple => {
        EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfBookBookRelationshipByCLCId)
      })
    /*
    println("*********************************************************************************")
    //图书与图书在作者上的联系
    val bookBookRelationshipByAuthorcount = bookBookRelationshipByAuthor.count()
    println("图书与图书在作者上的联系(bookBookRelationshipByAuthor)：")
    println("共计：" + bookBookRelationshipByAuthorcount + "条；")
    println("前十条：")
    bookBookRelationshipByAuthor.take(10).foreach(println)

    println("*********************************************************************************")
    //图书与图书在中图法分类号上的联系
    val bookBookRelationshipByCLCIdCount = bookBookRelationshipByCLCId.count()
    println("图书与图书在中图法分类号上的联系(bookBookRelationshipByCLCId)：")
    println("共计：" + bookBookRelationshipByCLCIdCount + "条；")
    println("前十条：")
    bookBookRelationshipByCLCId.take(10).foreach(println)

    println("*********************************************************************************")
    //论文与论文在作者上的联系
    val paperPaperRelationshipByAuthorCount = paperPaperRelationshipByAuthor.count()
    println("论文与论文在作者上的联系(paperPaperRelationshipByAuthor)：")
    println("共计：" + paperPaperRelationshipByAuthorCount + "条；")
    println("前十条：")
    paperPaperRelationshipByAuthor.take(10).foreach(println)

    println("*********************************************************************************")
    //论文与论文在领域名称上的联系
    val paperPaperRelationshipByFieldCount = paperPaperRelationshipByField.count()
    println("论文与论文在领域名称上的联系(paperPaperRelationshipByField)：")
    println("共计：" + paperPaperRelationshipByFieldCount + "条；")
    println("前十条：")
    paperPaperRelationshipByField.take(10).foreach(println)

    println("*********************************************************************************")
    //论文与论文在关键词上的联系
    val paperPaperRelationshipByIndexTermCount = paperPaperRelationshipByIndexTerm.count()
    println("论文与论文在关键词上的联系(paperPaperRelationshipByIndexTerm)：")
    println("共计：" + paperPaperRelationshipByIndexTermCount + "条；")
    println("前十条：")
    paperPaperRelationshipByIndexTerm.take(10).foreach(println)

    println("*********************************************************************************")
    //图书与论文在作者上的联系
    val paperBookRelationshipByAuthorCount = paperBookRelationshipByAuthor.count()
    println("图书与论文在作者上的联系(paperBookRelationshipByAuthor)：")
    println("共计：" + paperBookRelationshipByAuthorCount + "条；")
    println("前十条：")
    paperBookRelationshipByAuthor.take(10).foreach(println)

    println("*********************************************************************************")
    //图书的中图法分类名与论文的领域名称的联系
    val paperBookRelationshipByFieldAndCLCNameCount = paperBookRelationshipByFieldAndCLCName.count()
    println("图书的中图法分类名与论文的领域名称的联系(paperBookRelationshipByFieldAndCLCName)：")
    println("共计：" + paperBookRelationshipByFieldAndCLCNameCount + "条；")
    println("前十条：")
    paperBookRelationshipByFieldAndCLCName.take(10).foreach(println)

    println("*********************************************************************************")
    //图书的中图法分类名与论文的关键词的联系
    val paperBookRelationshipByIndexTermAndCLCNameCount = paperBookRelationshipByIndexTermAndCLCName.count()
    println("图书的中图法分类名与论文的关键词的联系(paperBookRelationshipByIndexTermAndCLCName)：")
    println("共计：" + paperBookRelationshipByIndexTermAndCLCNameCount + "条；")
    println("前十条：")
    paperBookRelationshipByIndexTermAndCLCName.take(10).foreach(println)
    */

    //println("*********************************************************************************")
    //获得所有联系并缓存
    val edges: RDD[Edge[Int]] = bookBookRelationshipByAuthor
      //.union(bookBookRelationshipByCLCId)
      .union(paperPaperRelationshipByAuthor)
      //.union(paperPaperRelationshipByField)
      .union(paperPaperRelationshipByIndexTerm)
      .union(paperBookRelationshipByAuthor)
      .union(paperBookRelationshipByFieldAndCLCName)
      .union(paperBookRelationshipByIndexTermAndCLCName)

    //val edgesCount = edges.count()
    //println("图的边(edges)：")
    //println("共计：" + edgesCount + "条；")
    //println("前十条：")
    //edges.take(10).foreach(println)

    //获得顶点为建图作准备
    //图书
    val bookVertices: RDD[(VertexId, Int)] = GetBookVertices.work(book_id_author, sc)
    //论文
    val paperVertices: RDD[(VertexId, Int)] = GetPaperVertices.work(paper_id_paperId, sc)
    //获得顶点
    val vertices: RDD[(VertexId, Int)] = bookVertices
      .union(paperVertices)

    //println("*********************************************************************************")
    val verticesCount = vertices.count()
    //println("图的顶点(vertices)：")
    //println("共计：" + verticesCount + "个；")
    //println("前十个：")
    //vertices.take(10).foreach(println)


    //建图
    val graph: Graph[Int, Int] = Graph(vertices, edges)

    //按各联系权重合并平行边
    val mergedEdgesGraph: Graph[Int, Int] = graph
      .groupEdges(merge = (edgeWeight01, edgeWeight02) => edgeWeight01 + edgeWeight02)
      .cache()

    //合并平行边之后的图
    println("*********************************************************************************")
    val mergedEdgesGraphEdgeCount = mergedEdgesGraph.edges.count()
    val mergedEdgesGraphVerticesCount = mergedEdgesGraph.vertices.count()
    println("合并平行边之后的图(mergedEdgesGraph)：")
    println("共计：" + mergedEdgesGraphEdgeCount + "条边；")
    println("共计：" + mergedEdgesGraphVerticesCount + "个顶点；")
    //println("前十条：")
    //mergedEdgesGraph.edges.take(10).foreach(println)

    //计算各个顶点的边的权重之和
    //注，本RDD不包括所有的顶点
    //RDD[(Int, Int)]
    val partOfVerticesWithWeight: VertexRDD[Int] = mergedEdgesGraph
      .aggregateMessages[Int](
      triplet => {
        //发送边的权重给目标顶点
        triplet.sendToDst(triplet.attr)
        //发送边的权重给出发顶点
        triplet.sendToSrc(triplet.attr)
      },
      //相加
      (a, b) => a + b
    )

    //获得含有权重的所有顶点
    //VertexRDD[Int]
    val verticesWithWeight: RDD[(VertexId, Int)] = vertices
      .leftOuterJoin(partOfVerticesWithWeight).map(tuple => {
      (tuple._1, tuple._2._2.getOrElse(0))
    })

    //获得含有权重的所有顶点
    println("*********************************************************************************")
    val verticesWithWeightCount = verticesWithWeight.count()
    println("共计：" + verticesCount + "个；")
    //println("前十个：")
    //vertices.take(10).foreach(println)
    println("获得含有权重的所有顶点(verticesWithWeight)：")
    println("共计：" + verticesWithWeightCount + "个；")
    //println("前十个：")
    //verticesWithWeight.take(10).foreach(println)

    var max: (Long, Int) = (0L, Int.MinValue)
    var min: (Long, Int) = (0L, Int.MaxValue)

    verticesWithWeight.foreach(vertex => {
      if (vertex._2 < min._2) {
        min = vertex
      }
      if (vertex._2 > max._2) {
        max = vertex
      }
    })
    println("权重最大节点：id: " + max._1 + ", weight: " + max._2)
    println("权重最小节点：id: " + min._1 + ", weight: " + min._2)


    //字符串RDD，准备保存
    //图书
    val books: RDD[String] = verticesWithWeight
      .filter(VertexUtil.GetVertexType(_) == 0)
      .map(VertexUtil.VertexToString(_, 0))
    //论文
    val papers: RDD[String] = verticesWithWeight
      .filter(VertexUtil.GetVertexType(_) == 1)
      .map(VertexUtil.VertexToString(_, 1))

    //图书与图书关联
    val bookBookRelationships: RDD[String] = mergedEdgesGraph.edges
      .filter(EdgeUtil.GetEdgeType(_) == 0)
      .map(EdgeUtil.EdgeToString(_, 0))
    //论文与论文关联
    val paperPaperRelationships: RDD[String] = mergedEdgesGraph.edges
      .filter(EdgeUtil.GetEdgeType(_) == 1)
      .map(EdgeUtil.EdgeToString(_, 1))
    //图书与关论文联
    val bookPaperRelationships: RDD[String] = mergedEdgesGraph.edges
      .filter(EdgeUtil.GetEdgeType(_) == 2)
      .map(EdgeUtil.EdgeToString(_, 2))

    //保存到文本文件
    books.saveAsTextFile(booksResultPath)
    papers.saveAsTextFile(papersResultPath)
    bookBookRelationships.saveAsTextFile(bookBookRelationshipsResultPath)
    paperPaperRelationships.saveAsTextFile(paperPaperRelationshipsResultPath)
    bookPaperRelationships.saveAsTextFile(bookPaperRelationshipsResultPath)

    //停止
    spark.stop()
  }
}
