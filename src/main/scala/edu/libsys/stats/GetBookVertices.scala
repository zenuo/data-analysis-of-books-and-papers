package edu.libsys.stats

import edu.libsys.util.Test
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

object GetBookVertices {
  def work(book_id_author: String): RDD[(VertexId, Int)] = {
    //分割符
    val delimiter01 = "#"
    //返回RDD
    Test.spark.sparkContext.textFile(book_id_author).map(line => {
      val tokens = line.replace(",", "")
        .split(delimiter01)
        .map(_.trim)
      (tokens(0).toLong, 0)
    })
  }
}
