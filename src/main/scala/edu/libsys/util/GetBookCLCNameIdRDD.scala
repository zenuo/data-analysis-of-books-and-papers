package edu.libsys.util

import edu.libsys.conf.Conf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object GetBookCLCNameIdRDD {

  /**
    * 获得图书id与中图分类名的元组
    *
    * @param book_id_CLCId “book_id_CLCId”文件路径
    * @param cls_no_name   “cls_no_name”文件路径
    * @return RDD[(String, Int)]
    */
  def work(book_id_CLCId: String, cls_no_name: String, sc: SparkContext): RDD[(String, Int)] = {

    //分割符
    val delimiter01 = ","

    //bookIdCLCIdTupleList
    val bookIdCLCIdTupleList = sc.textFile(book_id_CLCId).map(line => {
      val tokens = line.split(delimiter01)
        .map(_.trim)
      //提高匹配度，暂时不使用中图法分类名解析方法，否则匹配量太多，导致质量下降
      //结果类似(H152.5/34,1)
      tokens(1) -> tokens(0).toInt
    })

    //bookCLCIdCLCNameTupleList
    val bookCLCIdCLCNameTupleList = sc.textFile(cls_no_name).map(line => {
      val tokens = line.split(delimiter01)
        .map(_.trim)
      //类似(S325,品种的整理与保存)
      tokens(0) -> tokens(1)
    })

    //返回RDD
    bookCLCIdCLCNameTupleList
      .join(bookIdCLCIdTupleList, Conf.numTasks)
      .map(tuple => {
        //tuple._2类似(品种的整理与保存,122)
        tuple._2
      })
  }
}
