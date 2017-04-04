package edu.libsys.util

import org.apache.spark.graphx.Edge

object Test {

  def main(args: Array[String]): Unit = {
    val e: Edge[Int] = Edge(2133131L, 1000000001L, 3)
    println(EdgeUtil.EdgeToString(e, 2))
  }
}
