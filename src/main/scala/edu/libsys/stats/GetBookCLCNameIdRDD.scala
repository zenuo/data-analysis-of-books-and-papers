package edu.libsys.stats

import edu.libsys.Main
import edu.libsys.util.StringUtils
import org.apache.spark.rdd.RDD

object GetBookCLCNameIdRDD {

  //获得图书id与中图分类名的元组
  def work(book_id_CLCId: String, cls_no_name: String): RDD[(String, Int)] = {

    //分割符
    val delimiter01 = ","

    //book_id_CLCId
    val bookIdCLCIdTupleList = Main.spark.sparkContext.textFile(book_id_CLCId).map(line => {
      val tokens = line.split(delimiter01).map(_.trim)
      //类似(H152,1)
      StringUtils.parseCLCId(tokens(1)) -> tokens(0).toInt
    })

    //book_CLCId_CLCName
    val bookCLCIdCLCNameTupleList = Main.spark.sparkContext.textFile(cls_no_name).map(line => {
      val tokens = line.split(delimiter01).map(_.trim)
      //类似(S325,品种的整理与保存)
      tokens(0) -> tokens(1)
    })

    //返回RDD
    bookCLCIdCLCNameTupleList.join(bookIdCLCIdTupleList).map(tuple => {
      //类似(品种的整理与保存,122)
      tuple._2
    })
  }
}
