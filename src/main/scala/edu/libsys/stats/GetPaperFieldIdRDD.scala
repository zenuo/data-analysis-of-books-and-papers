package edu.libsys.stats

import edu.libsys.Main
import org.apache.spark.rdd.RDD

object GetPaperFieldIdRDD {

  //获得论文领域与论文id对应
  def work(paper_id_paperId: String, paper_paperId_field: String): RDD[(String, Int)] = {
    //分割符
    val delimiter01 = ","

    //paper_id_paperId
    val paperIdPaperIDTupleList = Main.spark.sparkContext.textFile(paper_id_paperId).map(line => {
      val tokens = line.split(delimiter01).map(_.trim)
      //类似(10001-1011132221.nh,1)
      tokens(1) -> tokens(0).toInt
    })

    //paper_paperId_field
    val paperPaperIdFieldTupleList = Main.spark.sparkContext.textFile(paper_paperId_field).map(line => {
      val tokens = line.split(delimiter01).map(_.trim)
      //类似(TGZG200701002,铁路)
      tokens(0) -> tokens(1)
    })

    paperPaperIdFieldTupleList.join(paperIdPaperIDTupleList).map(tuple => {
      //类似(铁路,112)
      tuple._2
    })
  }
}
