package edu.libsys.stats

import edu.libsys.data.dao.AuthorDao
import edu.libsys.entity.Author
import org.apache.spark.sql.SparkSession

object AuthorStats {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("AuthorStats")
      .getOrCreate()

    //create dao of author
    val authorDao = new AuthorDao()

    //load file
    val filePath = "/home/spark/Project/data/txt/id_author.txt"
    val delimiter = ","

    //parse file to list of tuple
    val authorIDTupleList = spark.sparkContext.textFile(filePath).map(line => {
      val tokens = line.split(delimiter).map(_.trim)
      (tokens(1) -> tokens(0))
    })
    authorIDTupleList.cache()
    println(authorIDTupleList.count())

    //create list of tuple(s) like authorName -> paperId
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
