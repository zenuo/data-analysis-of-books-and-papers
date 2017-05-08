package edu.libsys.util

import edu.libsys.conf.Conf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object GetPaperAuthorIdRDD {

  /**
    * 获得论文作者与论文id的对应
    *
    * @param paper_id_paperId     “paper_id_paperId”文件路径
    * @param paper_paperID_author “paper_paperID_author”文件路径
    * @return RDD[(String, Int)]
    */
  def work(paper_id_paperId: String, paper_paperID_author: String, sc: SparkContext): RDD[(String, Int)] = {
    //分割符
    val delimiter01 = ","

    //paper_id_paperId
    val paperIdPaperIDTupleList = sc
      .textFile(paper_id_paperId)
      .map(line => {
        val tokens = line.split(delimiter01)
          .map(_.trim)
        //类似(10001-1011132221.nh,1000000001)
        //为与图书id区别，论文id在处理过程中作加一亿处理
        tokens(1) -> (tokens(0).toInt + Conf.paperIdOffset)
      })

    //paper_paperID_author
    val paperAuthorPaperIDTupleList = sc
      .textFile(paper_paperID_author)
      .map(line => {
        val tokens = line.split(delimiter01)
          .map(_.trim)
        //结果类似(TGZG200701002,刘志军)
        tokens(0) -> tokens(1)
      })

    //返回RDD
    paperAuthorPaperIDTupleList
      .join(paperIdPaperIDTupleList, Conf.numTasks)
      .map(tuple => {
        //tuple类似(BGDH200609003,(杨竣辉,144810))
        // tuple._2类似(杨竣辉,144810)
        tuple._2
      })
  }
}
