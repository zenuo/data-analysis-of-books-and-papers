package edu.libsys.stats

import edu.libsys.Main
import org.apache.spark.graphx.VertexId
import org.apache.spark.rdd.RDD

object GetPaperVertices {
  def work(paper_id_paperId: String): RDD[(VertexId, Int)] = {
    //分割符
    val delimiter01 = ","
    //返回RDD
    Main.spark.sparkContext.textFile(paper_id_paperId).map(line => {
      val tokens = line
        .split(delimiter01)
        .map(_.trim)
      (tokens(0).toLong, 0)
    })
  }
}
