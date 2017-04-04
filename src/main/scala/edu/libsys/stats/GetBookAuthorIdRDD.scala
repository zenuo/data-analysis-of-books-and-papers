package edu.libsys.stats

import edu.libsys.Main
import edu.libsys.util.StringUtils
import org.apache.spark.rdd.RDD

object GetBookAuthorIdRDD {
  /**
    * 获得BookAuthorIdRDD
    *
    * @param book_id_author “book_id_author”文件路径
    * @return
    */
  def work(book_id_author: String): RDD[(String, Int)] = {
    //分割符
    val delimiter01 = "#"
    //返回RDD
    Main.spark.sparkContext
      .textFile(book_id_author)
      .map(line => {
        val tokens = line.split(delimiter01)
          .map(_.trim)
        StringUtils.parseBookAuthor(tokens(1)) -> tokens(0).toInt
      })
  }
}
