package edu.libsys.stats

import edu.libsys.Main
import org.apache.spark.rdd.RDD

object GetPaperAuthorIdRDD {

  //获得论文作者与论文id的对应
  def work(paper_id_paperId: String, paper_paperID_author: String): RDD[(String, Int)] = {
    //分割符
    val delimiter01 = ","

    //paper_id_paperId
    val paperIdPaperIDTupleList = Main.spark.sparkContext
      .textFile(paper_id_paperId)
      .map(line => {
        val tokens = line.split(delimiter01)
          .map(_.trim)
        //结果类似(10001-1011132221.nh,1)
        tokens(1) -> tokens(0).toInt
      })

    //paper_paperID_author
    val paperAuthorPaperIDTupleList = Main.spark.sparkContext
      .textFile(paper_paperID_author)
      .map(line => {
        val tokens = line.split(delimiter01)
          .map(_.trim)
        //结果类似(TGZG200701002,刘志军)
        tokens(0) -> tokens(1)
      })

    //返回RDD
    paperAuthorPaperIDTupleList
      .join(paperIdPaperIDTupleList)
      .map(tuple => {
        //tuple类似(BGDH200609003,(杨竣辉,144810))
        // tuple._2类似(杨竣辉,144810)
        tuple._2
      })
  }
}
