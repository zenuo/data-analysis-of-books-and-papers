package edu.libsys.util

import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object Test {

  //创建会话
  val spark: SparkSession = SparkSession
    .builder()
    .appName("MainStats")
    .getOrCreate()

  def main(args: Array[String]): Unit = {
    val sc = spark.sparkContext
    val users: RDD[(VertexId, Int)] = sc.parallelize(Array((1L, 0), (2L, 0), (3L, 0)))
    val relationships: RDD[Edge[Int]] = sc.parallelize(Array(Edge(1L, 2L, 2), Edge(1L, 2L, 3), Edge(1L, 2L, 4), Edge(3L, 2L, 3), Edge(3L, 2L, 5)))

    val graph = Graph(users, relationships)

    graph.triplets.foreach(println)

    val vertices = graph.aggregateMessages[Int](tripletFields => {
      //tripletFields.sendToSrc(tripletFields.attr)
      tripletFields.sendToDst(tripletFields.attr)
    },
      (a, b) => a + b
    )

    vertices.foreach(println)

    spark.stop()
  }
}
