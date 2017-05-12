package edu.libsys

import edu.libsys.conf.Conf
import edu.libsys.util._
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Edge, Graph, VertexId}
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
      println("1.PATH_TO_OBJECT_DATA_DIRECTORY")
      println("2.PATH_TO_RESULT_DATA_DIRECTORY")
      println("Usage:\n/usr/local/spark/bin/spark-submit --class edu.libsys.Main --master local --executor-memory 52G --total-executor-cores 6 --conf spark.executor.heartbeatInterval=10000000 --conf spark.network.timeout=10000000 /home/spark/book-stats-1.0.jar /home/spark/data/graph/obj /home/spark/result")
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
    //use the Kryo library to serialize objects
    spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    val sc: SparkContext = spark.sparkContext

    //文件路径
    //结果数据文件保存路径
    val booksResultPath: String = args(1) + "/books"
    val papersResultPath: String = args(1) + "/papers"
    val bookBookRelationshipsResultPath: String = args(1) + "/bookBookRelationships"
    val paperPaperRelationshipsResultPath: String = args(1) + "/paperPaperRelationships"
    val bookPaperRelationshipsResultPath: String = args(1) + "/bookPaperRelationships"

    //获得顶点
    val vertices: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] = sc.objectFile(args(0) + "/vertices.obj")

    //计算各个顶点的入度
    val edgeOfBookBookGraphByAuthor: RDD[Edge[Int]] = sc.objectFile(args(0) + "/edgesOfBookBookGraphByAuthor.obj")
    val bookBookGraphByAuthor: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, edgeOfBookBookGraphByAuthor)
    val inDegreesOfBookBookGraphByAuthor: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      bookBookGraphByAuthor
        .inDegrees
        .map(inDegreeTuple => {
          (inDegreeTuple._1, (inDegreeTuple._2, 0, 0, 0, 0, 0, 0, 0))
        })
        .repartition(Conf.numPartitions)

    val edgesOfBookBookGraphByCLCId: RDD[Edge[Int]] = sc.textFile("/home/spark/data/graph/txt/edgesOfBookBookGraphByCLCId.txt-new")
      .map(EdgeUtil.stringToEdge)
    val bookBookGraphByCLCId: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, edgesOfBookBookGraphByCLCId)
    val inDegreesOfBookBookGraphByCLCId: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      bookBookGraphByCLCId
        .inDegrees
        .map(inDegreeTuple => {
          (inDegreeTuple._1, (0, inDegreeTuple._2, 0, 0, 0, 0, 0, 0))
        })
        .repartition(Conf.numPartitions)

    val edgesOfPaperPaperGraphByAuthor: RDD[Edge[Int]] = sc.objectFile(args(0) + "/edgesOfPaperPaperGraphByAuthor.obj")
    val paperPaperGraphByAuthor: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, edgesOfPaperPaperGraphByAuthor)
    val inDegreesOfPaperPaperGraphByAuthor: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      paperPaperGraphByAuthor
        .inDegrees
        .map(inDegreeTuple => {
          (inDegreeTuple._1, (0, 0, inDegreeTuple._2, 0, 0, 0, 0, 0))
        })
        .repartition(Conf.numPartitions)

    val edgesOfPaperPaperGraphByIndexTerm: RDD[Edge[Int]] = sc.objectFile(args(0) + "/edgesOfPaperPaperGraphByIndexTerm.obj")
    val paperPaperGraphByIndexTerm: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, edgesOfPaperPaperGraphByIndexTerm)
    val inDegreesOfPaperPaperGraphByIndexTerm: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      paperPaperGraphByIndexTerm
        .inDegrees
        .map(inDegreeTuple => {
          (inDegreeTuple._1, (0, 0, 0, 0, inDegreeTuple._2, 0, 0, 0))
        })
        .repartition(Conf.numPartitions)

    val edgesOfBookPaperGraphByAuthor: RDD[Edge[Int]] = sc.objectFile(args(0) + "/edgesOfBookPaperGraphByAuthor.obj")
    val bookPaperGraphByAuthor: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, edgesOfBookPaperGraphByAuthor)
    val inDegreesOfPaperBookGraphByAuthor: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      bookPaperGraphByAuthor
        .inDegrees
        .map(inDegreeTuple => {
          (inDegreeTuple._1, (0, 0, 0, 0, 0, inDegreeTuple._2, 0, 0))
        })
        .repartition(Conf.numPartitions)

    val edgesOfBookPaperGraphByFieldAndCLCName: RDD[Edge[Int]] = sc.objectFile(args(0) + "/edgesOfBookPaperGraphByFieldAndCLCName.obj")
    val bookPaperGraphByFieldAndCLCName: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, edgesOfBookPaperGraphByFieldAndCLCName)
    val inDegreesOfBookPaperGraphByFieldAndCLCName: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      bookPaperGraphByFieldAndCLCName
        .inDegrees
        .map(inDegreeTuple => {
          (inDegreeTuple._1, (0, 0, 0, 0, 0, 0, inDegreeTuple._2, 0))
        })
        .repartition(Conf.numPartitions)

    val edgesOfBookPaperGraphByIndexTermAndCLCName: RDD[Edge[Int]] = sc.objectFile(args(0) + "/edgesOfBookPaperGraphByIndexTermAndCLCName.obj")
    val bookPaperGraphByIndexTermAndCLCName: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      Graph(vertices, edgesOfBookPaperGraphByIndexTermAndCLCName)
    val inDegreesOfBookPaperGraphByIndexTermAndCLCName: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      bookPaperGraphByIndexTermAndCLCName
        .inDegrees
        .map(inDegreeTuple => {
          (inDegreeTuple._1, (0, 0, 0, 0, 0, 0, 0, inDegreeTuple._2))
        })
        .repartition(Conf.numPartitions)

    //以所有顶点为顶点集合，建一个边集合为空的图
    val emptyEdges: RDD[Edge[Int]] = sc.parallelize(Seq(), 1)
    val graph: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] = Graph(vertices, emptyEdges)

    //收集顶点在“图书与图书在作者上的联系”上的入度
    val tempGraph01: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      graph.joinVertices(inDegreesOfBookBookGraphByAuthor) {
        (_, all, part) =>
          (part._1, 0, 0, 0, 0, 0, 0, 0)
      }

    val tempGraph02: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph01.joinVertices(inDegreesOfBookBookGraphByCLCId) {
        (_, part, all) =>
          (all._1, part._2, 0, 0, 0, 0, 0, 0)
      }

    val tempGraph03: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph02.joinVertices(inDegreesOfPaperPaperGraphByAuthor) {
        (_, all, part) =>
          (all._1, all._2, part._3, 0, 0, 0, 0, 0)
      }

    val tempGraph05: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph03.joinVertices(inDegreesOfPaperPaperGraphByIndexTerm) {
        (_, all, part) =>
          (all._1, all._2, all._3, all._4, part._5, 0, 0, 0)
      }
    val tempGraph06: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph05.joinVertices(inDegreesOfPaperBookGraphByAuthor) {
        (_, all, part) =>
          (all._1, all._2, all._3, all._4, all._5, part._6, 0, 0)
      }
    val tempGraph07: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph06.joinVertices(inDegreesOfBookPaperGraphByFieldAndCLCName) {
        (_, all, part) =>
          (all._1, all._2, all._3, all._4, all._5, all._6, part._7, 0)
      }
    val tempGraph08: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph07.joinVertices(inDegreesOfBookPaperGraphByIndexTermAndCLCName) {
        (_, all, part) =>
          (all._1, all._2, all._3, all._4, all._5, all._6, all._7, part._8)
      }

    //图书节点
    val books: RDD[String] = tempGraph08.vertices
      .filter(VertexUtil.getVertexType(_) == 0)
      .map(VertexUtil.vertexToString(_, 0))
      .repartition(Conf.numPartitions)
    //论文节点
    val papers: RDD[String] = tempGraph08.vertices
      .filter(VertexUtil.getVertexType(_) == 1)
      .map(VertexUtil.vertexToString(_, 1))
      .repartition(Conf.numPartitions)

    //图书与图书关联
    val bookBookRelationships: RDD[String] = bookBookGraphByAuthor.edges
      .union(bookBookGraphByCLCId.edges)
      .filter(EdgeUtil.getEdgeType(_) == 0)
      .map(EdgeUtil.edgeToString(_, 0))
      .repartition(Conf.numPartitions)
    //文与论文关联
    val paperPaperRelationships: RDD[String] = paperPaperGraphByAuthor.edges
      .union(paperPaperGraphByIndexTerm.edges)
      .filter(EdgeUtil.getEdgeType(_) == 1)
      .map(EdgeUtil.edgeToString(_, 1))
      .repartition(Conf.numPartitions)
    //图书与论文关联
    val bookPaperRelationships: RDD[String] = bookPaperGraphByAuthor.edges
      .union(bookPaperGraphByFieldAndCLCName.edges)
      .union(bookPaperGraphByIndexTermAndCLCName.edges)
      .filter(EdgeUtil.getEdgeType(_) == 2)
      .map(EdgeUtil.edgeToString(_, 2))
      .repartition(Conf.numPartitions)

    //保存到文本文件
    books.saveAsTextFile(booksResultPath)
    papers.saveAsTextFile(papersResultPath)
    bookBookRelationships.saveAsTextFile(bookBookRelationshipsResultPath)
    paperPaperRelationships.saveAsTextFile(paperPaperRelationshipsResultPath)
    bookPaperRelationships.saveAsTextFile(bookPaperRelationshipsResultPath)

    //停止
    sc.stop()
    spark.stop()
  }
}
