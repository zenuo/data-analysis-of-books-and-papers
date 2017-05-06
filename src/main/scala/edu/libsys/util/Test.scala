package edu.libsys.util

import org.apache.spark.SparkContext
import org.apache.spark.graphx.Graph
import org.apache.spark.graphx.util.GraphGenerators
import org.apache.spark.sql.SparkSession

object Test {
  /**
    * 测试类主方法
    *
    * @param args 控制台参数
    */
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .appName("Test")
      .getOrCreate()

    val sc: SparkContext = spark.sparkContext

    val graph: Graph[Long, Int] =
      GraphGenerators.logNormalGraph(sc, numVertices = 100)

    graph.vertices.foreach(println)

    sc.stop()
    spark.stop()
  }
}
