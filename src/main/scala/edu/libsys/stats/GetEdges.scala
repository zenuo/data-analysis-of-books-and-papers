package edu.libsys.stats

import edu.libsys.util.EdgeUtil
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * 获取入度RDD
  */
object GetEdges {
  def main(args: Array[String]): Unit = {
    //屏蔽不必要的日志输出
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

    //判断文件参数是否正确
    if (args.length != 2) {
      println("-------------------------------ERROR-------------------------------")
      println("Valid program arguments:")
      println("1.PATH_TO_OBJECT_DATA_DIRECTORY")
      println("2.PATH_TO_RESULT_DATA_DIRECTORY")
      println("Usage:\n/usr/local/spark/bin/spark-submit --class edu.libsys.stats.GetInDegreesAndRelationship --master local --executor-memory 52G --total-executor-cores 6 --conf spark.executor.heartbeatInterval=10000000 --conf spark.network.timeout=10000000 /home/spark/book-stats-1.0.jar /home/spark/data/graph/txt /home/spark/result")
      println("please try again, exit now.")
      println("-------------------------------------------------------------------")
      return
    }

    /**
      * spark会话
      */
    val spark: SparkSession = SparkSession
      .builder()
      .appName("GetInDegreesAndRelationship")
      .getOrCreate()
    //use the Kryo library to serialize objects
    spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    val sc: SparkContext = spark.sparkContext

    //获得顶点
    val vertices: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      sc.objectFile(args(0) + "/vertices.obj")
        .cache()

    //计算各个顶点的入度
    val edgeOfBookBookGraphByAuthor: RDD[Edge[Int]] =
      sc.textFile(args(0) + "/edgesOfBookBookGraphByAuthor.txt")
        .map(EdgeUtil.stringToEdge)
    val bookBookGraphByAuthor: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, edgeOfBookBookGraphByAuthor)

    val edgesOfBookBookGraphByCLCId: RDD[Edge[Int]] =
      sc.textFile(args(0) + "/edgesOfBookBookGraphByCLCId.txt-new")
        .map(EdgeUtil.stringToEdge)
    val bookBookGraphByCLCId: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, edgesOfBookBookGraphByCLCId)

    val edgesOfPaperPaperGraphByAuthor: RDD[Edge[Int]] =
      sc.textFile(args(0) + "/edgesOfPaperPaperGraphByAuthor.txt")
        .map(EdgeUtil.stringToEdge)
    val paperPaperGraphByAuthor: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, edgesOfPaperPaperGraphByAuthor)

    val edgesOfPaperPaperGraphByIndexTerm: RDD[Edge[Int]] =
      sc.textFile(args(0) + "/edgesOfPaperPaperGraphByIndexTerm.txt")
        .map(EdgeUtil.stringToEdge)
    val paperPaperGraphByIndexTerm: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, edgesOfPaperPaperGraphByIndexTerm)

    val edgesOfBookPaperGraphByAuthor: RDD[Edge[Int]] =
      sc.textFile(args(0) + "/edgesOfBookPaperGraphByAuthor.txt")
        .map(EdgeUtil.stringToEdge)
    val bookPaperGraphByAuthor: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, edgesOfBookPaperGraphByAuthor)

    val edgesOfBookPaperGraphByFieldAndCLCName: RDD[Edge[Int]] =
      sc.textFile(args(0) + "/edgesOfBookPaperGraphByFieldAndCLCName.txt")
        .map(EdgeUtil.stringToEdge)
    val bookPaperGraphByFieldAndCLCName: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, edgesOfBookPaperGraphByFieldAndCLCName)

    val edgesOfBookPaperGraphByIndexTermAndCLCName: RDD[Edge[Int]] =
      sc.textFile(args(0) + "/edgesOfBookPaperGraphByIndexTermAndCLCName.txt")
        .map(EdgeUtil.stringToEdge)
    val bookPaperGraphByIndexTermAndCLCName: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, edgesOfBookPaperGraphByIndexTermAndCLCName)

    //图书与图书关联
    val bookBookRelationships: RDD[String] = bookBookGraphByAuthor.edges
      .union(bookBookGraphByCLCId.edges)
      .filter(EdgeUtil.getEdgeType(_) == 0)
      .map(EdgeUtil.edgeToString(_, 0))
    //文与论文关联
    val paperPaperRelationships: RDD[String] = paperPaperGraphByAuthor.edges
      .union(paperPaperGraphByIndexTerm.edges)
      .filter(EdgeUtil.getEdgeType(_) == 1)
      .map(EdgeUtil.edgeToString(_, 1))
    //图书与论文关联
    val bookPaperRelationships: RDD[String] = bookPaperGraphByAuthor.edges
      .union(bookPaperGraphByFieldAndCLCName.edges)
      .union(bookPaperGraphByIndexTermAndCLCName.edges)
      .filter(EdgeUtil.getEdgeType(_) == 2)
      .map(EdgeUtil.edgeToString(_, 2))

    bookBookRelationships.saveAsTextFile(args(1) + "/bookBookRelationships")
    paperPaperRelationships.saveAsTextFile(args(1) + "/paperPaperRelationships")
    bookPaperRelationships.saveAsTextFile(args(1) + "/bookPaperRelationships")


    sc.stop()
    spark.stop()
  }

}
