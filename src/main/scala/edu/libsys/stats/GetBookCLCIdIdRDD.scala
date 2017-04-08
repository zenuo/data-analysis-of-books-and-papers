package edu.libsys.stats

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object GetBookCLCIdIdRDD {
  /**
    * 获得图书id与中图分类号的元组
    *
    * @param book_id_CLCId “book_id_CLCId”文件路径
    * @return : RDD[(String, Int)]
    */
  def work(book_id_CLCId: String, sc: SparkContext): RDD[(String, Int)] = {

    //分割符
    val delimiter01 = ","

    //返回RDD
    sc.textFile(book_id_CLCId).map(line => {
      val tokens = line.split(delimiter01)
        .map(_.trim)
      //结果类似(H152,1)
      tokens(1) -> tokens(0).toInt
    })
  }
}
