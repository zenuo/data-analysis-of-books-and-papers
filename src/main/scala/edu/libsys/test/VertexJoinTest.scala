package edu.libsys.test

import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object VertexJoinTest {
  /**
    * 测试类主方法
    *
    * @param args 命令行参数
    */
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .appName("TestVertexJoin")
      .getOrCreate()

    val sc: SparkContext = spark.sparkContext

    val edges: RDD[Edge[Long]] = sc.parallelize(Seq(Edge(1L, 2L, 0), Edge(3L, 1L, 0), Edge(2L, 3L, 0)), 1)
    val vertices: RDD[(VertexId, (Int, Int, Int, Int, Int, Int))] = sc.parallelize(Seq((1L, (0, 0, 0, 0, 0, 0)), (2L, (0, 0, 0, 0, 0, 0)), (3L, (0, 0, 0, 0, 0, 0))), 1)
    //val vertices: RDD[(VertexId, (Int, Int, Int, Int, Int, Int))] = sc.parallelize(Seq(), 1)

    val graph: Graph[(Int, Int, Int, Int, Int, Int), Long] = Graph(vertices, edges)

    val users: RDD[(VertexId, (Int, Int, Int, Int, Int, Int))] = sc.parallelize(Seq((1L, (11, 0, 0, 0, 0, 0)), (2L, (12, 0, 0, 0, 0, 0)), (3L, (13, 0, 0, 0, 0, 0))), 1)

    val graph2 = graph.joinVertices(users) { (vid, all, part) => (part._1, 0, 0, 0, 0, 0) }

    val users2: RDD[(VertexId, (Int, Int, Int, Int, Int, Int))] = sc.parallelize(Seq((1L, (11, 0, 0, 0, 0, 0)), (2L, (12, 0, 0, 0, 0, 0)), (3L, (13, 0, 0, 0, 0, 0))), 1)

    val graph3 = graph.joinVertices(graph2.vertices) { (vid, all, part) => (part._1, part._1, 0, 0, 0, 0) }

    val graph4 = graph.joinVertices(graph3.vertices) { (vid, all, part) => (part._1, part._1, part._1, 0, 0, 0) }

    /*val edgeCount =
      graph.aggregateMessages[(Int, Int, Int, Int, Int, Int)](
        tripletFields => {
          tripletFields.sendToDst((tripletFields.attr.toInt, tripletFields.dstAttr._2, tripletFields.dstAttr._2, tripletFields.dstAttr._2, tripletFields.dstAttr._2, tripletFields.dstAttr._2))
        },
        (a, b) => (a._1 + b._1, a._2, a._3, a._4, a._5, a._6)
      )
    */
    sc.stop()
    spark.stop()

  }
}
