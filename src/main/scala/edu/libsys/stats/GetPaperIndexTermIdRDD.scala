package edu.libsys.stats

import edu.libsys.Main
import org.apache.spark.rdd.RDD

object GetPaperIndexTermIdRDD {

  //获得论文关键词与论文id对应
  def work(paper_id_paperId: String, paper_paperId_indexTerm: String): RDD[(String, Int)] = {
    //分割符
    val delimiter01 = ","

    //paperIdPaperIDTupleList
    val paperIdPaperIDTupleList = Main.spark.sparkContext
      .textFile(paper_id_paperId)
      .map(line => {
        val tokens = line.split(delimiter01)
          .map(_.trim)
        //类似(10001-1011132221.nh,1)
        tokens(1) -> tokens(0).toInt
      })

    //paperIdPaperIDTupleList
    val paperPaperIdIndexTermTupleList = Main.spark.sparkContext
      .textFile(paper_paperId_indexTerm)
      .map(line => {
        val tokens = line.split(delimiter01)
          .map(_.trim)
        //类似(TGZG200701002,和谐铁路)
        tokens(0) -> tokens(1)
      })

    //返回RDD
    paperPaperIdIndexTermTupleList
      .join(paperIdPaperIDTupleList)
      .map(tuple => {
        //tuple._2类似(和谐铁路,122)
        tuple._2
      })
  }
}
