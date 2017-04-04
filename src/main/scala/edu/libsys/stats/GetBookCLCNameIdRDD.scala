package edu.libsys.stats

import edu.libsys.Main
import org.apache.spark.rdd.RDD

object GetBookCLCNameIdRDD {

  /**
    * 获得图书id与中图分类名的元组
    *
    * @param book_id_CLCId “book_id_CLCId”文件路径
    * @param cls_no_name   “cls_no_name”文件路径
    * @return RDD[(String, Int)]
    */
  def work(book_id_CLCId: String, cls_no_name: String): RDD[(String, Int)] = {

    //分割符
    val delimiter01 = ","

    //bookIdCLCIdTupleList
    val bookIdCLCIdTupleList = Main.spark.sparkContext
      .textFile(book_id_CLCId).map(line => {
      val tokens = line.split(delimiter01)
        .map(_.trim)
      //提高匹配度，暂时不使用中图法分类名解析方法，否则匹配量太多，导致质量下降
      //StringUtils.parseCLCId(tokens(1)) -> tokens(0).toInt
      //结果类似(H152,1)
      tokens(1) -> tokens(0).toInt
    })

    //bookCLCIdCLCNameTupleList
    val bookCLCIdCLCNameTupleList = Main.spark.sparkContext
      .textFile(cls_no_name).map(line => {
      val tokens = line.split(delimiter01)
        .map(_.trim)
      //类似(S325,品种的整理与保存)
      tokens(0) -> tokens(1)
    })

    //返回RDD
    bookCLCIdCLCNameTupleList
      .join(bookIdCLCIdTupleList)
      .map(tuple => {
        //tuple._2类似(品种的整理与保存,122)
        tuple._2
      })
  }
}
