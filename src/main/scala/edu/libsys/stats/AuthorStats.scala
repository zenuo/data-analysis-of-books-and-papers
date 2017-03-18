package edu.libsys.stats

import edu.libsys.data.dao.AuthorDao
import edu.libsys.entity.Author
import org.apache.spark.sql.SparkSession

/*数据样例
论文ID，作者
TGZG200701002,刘志军
10511-1012349726.nh,孙自俭
10511-1012349726.nh,朱英
10651-1014167683.nh,向开祥
 */

object AuthorStats {
  def main(args: Array[String]): Unit = {
    //create spark session
    val spark = SparkSession
      .builder()
      .appName("AuthorStats")
      .getOrCreate()

    //create an instances of AuthorDao
    val authorDao = new AuthorDao()

    //load file
    val filePath = "/home/spark/Project/data/txt/id_author.txt"
    val delimiter = ","

    //parse file to list of tuple
    val authorIDTupleList = spark.sparkContext.textFile(filePath).map(line => {
      val tokens = line.split(delimiter).map(_.trim)
      tokens(1) -> tokens(0)
    })
    authorIDTupleList.cache()
    println(authorIDTupleList.count())

    val authorStats = authorIDTupleList.groupByKey.collect

    //traversal list, save authors to database
    authorStats.foreach(tuple => {
      //println(tuple)
      //println(tuple._2.toList.size)
      authorDao.addAuthor(new Author(tuple._1, tuple._2.toArray.length))
    })

    //stop work
    spark.stop()
  }
}
