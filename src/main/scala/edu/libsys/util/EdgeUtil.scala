package edu.libsys.util

import org.apache.spark.graphx.Edge

object EdgeUtil {

  //返回srcId比dscId小的Edge
  def SortEdge(idA: Int, idB: Int, weight: Int): Edge[Int] = {
    if (idA < idB) {
      Edge(idA.toLong, idB.toLong, weight)
    } else {
      Edge(idB.toLong, idA.toLong, weight)
    }
  }
}
