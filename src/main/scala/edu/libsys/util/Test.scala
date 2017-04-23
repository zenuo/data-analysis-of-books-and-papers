package edu.libsys.util

import org.apache.spark.graphx.Edge

object Test {
  /**
    * 测试类主方法
    *
    * @param args 控制台参数
    */
  def main(args: Array[String]): Unit = {
    val e: Edge[Int] = Edge(2133131L, 1000000001L, 3)
    println(EdgeUtil.EdgeToString(e, 2))
  }
}
