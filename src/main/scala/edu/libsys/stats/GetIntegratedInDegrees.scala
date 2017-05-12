package edu.libsys.stats

import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * 整合入度
  */
object GetIntegratedInDegrees {
  def main(args: Array[String]): Unit = {
    //判断文件参数是否正确
    if (args.length != 2) {
      println("-------------------------------ERROR-------------------------------")
      println("Valid program arguments:")
      println("1.PATH_TO_OBJECT_DATA_DIRECTORY")
      println("2.PATH_TO_RESULT_DATA_DIRECTORY")
      println("Usage:\n/usr/local/spark/bin/spark-submit --class edu.libsys.stats.GetIntegratedInDegrees --master local --executor-memory 52G --total-executor-cores 6 --conf spark.executor.heartbeatInterval=10000000 --conf spark.network.timeout=10000000 /home/spark/book-stats-1.0.jar /home/spark/data/graph/inDegrees /home/spark/result")
      println("please try again, exit now.")
      println("-------------------------------------------------------------------")
      return
    }

    /**
      * spark会话
      */
    val spark: SparkSession = SparkSession
      .builder()
      .appName("GetIntegrateInDegrees")
      .getOrCreate()
    //use the Kryo library to serialize objects
    spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    val sc: SparkContext = spark.sparkContext

    //获得顶点
    val vertices: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      sc.objectFile(args(0) + "/vertices.obj")
        .cache()

    //以所有顶点为顶点集合，建一个边集合为空的图
    val emptyEdges: RDD[Edge[Int]] = sc.parallelize(Seq(), 1)
    val graph: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] = Graph(vertices, emptyEdges)

    val inDegreesOfBookBookGraphByAuthor: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      sc.objectFile(args(0) + "/inDegreesOfBookBookGraphByAuthor.obj")
    val tempGraph01: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      graph.joinVertices(inDegreesOfBookBookGraphByAuthor) {
        (_, _, indegrees) =>
          (indegrees._1, 0, 0, 0, 0, 0, 0, 0)
        //这里犯过一个逻辑错误
      }

    val inDegreesOfBookBookGraphByCLCId: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      sc.objectFile(args(0) + "/inDegreesOfBookBookGraphByCLCId.obj")
    val tempGraph02: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph01.joinVertices(inDegreesOfBookBookGraphByCLCId) {
        (_, _, indegrees) =>
          (indegrees._1, indegrees._2, 0, 0, 0, 0, 0, 0)
      }

    val inDegreesOfPaperPaperGraphByAuthor: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      sc.objectFile(args(0) + "/inDegreesOfPaperPaperGraphByAuthor.obj")
    val tempGraph03: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph02.joinVertices(inDegreesOfPaperPaperGraphByAuthor) {
        (_, _, indegrees) =>
          (indegrees._1, indegrees._2, indegrees._3, 0, 0, 0, 0, 0)
      }

    val inDegreesOfPaperPaperGraphByIndexTerm: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      sc.objectFile(args(0) + "/inDegreesOfPaperPaperGraphByIndexTerm.obj")
    val tempGraph05: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph03.joinVertices(inDegreesOfPaperPaperGraphByIndexTerm) {
        (_, _, indegrees) =>
          (indegrees._1, indegrees._2, indegrees._3, indegrees._4, indegrees._5, 0, 0, 0)
      }

    val inDegreesOfBookPaperGraphByAuthor: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      sc.objectFile(args(0) + "/inDegreesOfBookPaperGraphByAuthor.obj")
    val tempGraph06: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph05.joinVertices(inDegreesOfBookPaperGraphByAuthor) {
        (_, _, indegrees) =>
          (indegrees._1, indegrees._2, indegrees._3, indegrees._4, indegrees._5, indegrees._6, 0, 0)
      }

    val inDegreesOfBookPaperGraphByFieldAndCLCName: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      sc.objectFile(args(0) + "/inDegreesOfBookPaperGraphByFieldAndCLCName.obj")
    val tempGraph07: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph06.joinVertices(inDegreesOfBookPaperGraphByFieldAndCLCName) {
        (_, _, indegrees) =>
          (indegrees._1, indegrees._2, indegrees._3, indegrees._4, indegrees._5, indegrees._6, indegrees._7, 0)
      }

    val inDegreesOfBookPaperGraphByIndexTermAndCLCName: RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] =
      sc.objectFile(args(0) + "/inDegreesOfBookPaperGraphByIndexTermAndCLCName.obj")
    val tempGraph08: Graph[(Int, Int, Int, Int, Int, Int, Int, Int), Int] =
      tempGraph07.joinVertices(inDegreesOfBookPaperGraphByIndexTermAndCLCName) {
        (_, _, indegrees) =>
          (indegrees._1, indegrees._2, indegrees._3, indegrees._4, indegrees._5, indegrees._6, indegrees._7, indegrees._8)
      }

    tempGraph08.vertices.saveAsObjectFile(args(1) + "/obj")
    tempGraph08.vertices.saveAsTextFile(args(1) + "/txt")

    sc.stop()
    spark.stop()
  }

}
