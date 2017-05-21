package edu.libsys.util

import edu.libsys.conf.Conf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object GetPaperFieldIdRDD {
  /**
    * 获得论文领域与论文id对应
    *
    * @param paper_id_paperId    “paper_id_paperId”文件路径
    * @param paper_paperId_field “paper_paperId_field”文件路径
    * @return RDD[(String, Int)]
    */
  def work(paper_id_paperId: String, paper_paperId_field: String, sc: SparkContext): RDD[(String, Int)] = {
    //分割符
    val delimiter01 = ","

    //paper_id_paperId
    val paperIdPaperIDTupleList: RDD[(String, Int)] = sc
      .textFile(paper_id_paperId)
      .map(line => {
        val tokens = line.split(delimiter01)
          .map(_.trim)
        //类似(10001-1011132221.nh,1000000001)
        //为与图书id区别，论文id在处理过程中作加一亿处理
        tokens(1) -> (tokens(0).toInt + Conf.paperIdOffset)
      })

    //paper_paperId_field
    val paperPaperIdFieldTupleList: RDD[(String, String)] = sc
      .textFile(paper_paperId_field)
      .map(line => {
        val tokens = line.split(delimiter01)
          .map(_.trim)
        //类似(TGZG200701002,铁路)
        tokens(0) -> tokens(1)
      })

    //返回RDD
    paperPaperIdFieldTupleList
      .join(paperIdPaperIDTupleList, Conf.numTasks)
      .map(tuple => {
        //tuple._2类似(铁路,112)
        tuple._2
      })
  }
}
