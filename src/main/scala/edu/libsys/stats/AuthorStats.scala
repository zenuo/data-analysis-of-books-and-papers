package edu.libsys.stats

import com.google.common.hash.BloomFilter
import edu.libsys.data.dao.AuthorDao
import edu.libsys.dataclean.StringFunnel
import edu.libsys.entity.Author
import org.apache.spark.sql.SparkSession

object AuthorStats {

  //create a bloomFilter of string for removing duplicated authors
  val expectedInsertions: Long = 830903
  val stringFunnel: StringFunnel = new StringFunnel()
  val bloomFilter: BloomFilter[String] = BloomFilter.create(stringFunnel, expectedInsertions)

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

    //parse file to instances of IdAuthor
    val idAuthorRDD = spark.sparkContext.textFile(filePath).map(line => {
      val tokens = line.split(delimiter).map(_.trim)
      IdAuthor(tokens(0), tokens(1))
    })
    idAuthorRDD.cache()

    //get names of unique author create instances of Author
    val authorRDD = idAuthorRDD.map(_.authorName).filter(isUniqueAuthor).map(name => {
      new Author(name)
    })
    authorRDD.cache()

    println(idAuthorRDD.count())
    println(authorRDD.count())

    //traversal authorNameArray, count the amount of works of each author
    authorRDD.foreach(author => {
      var workCount = 0
      idAuthorRDD.foreach(idAuthor => {
        if (idAuthor.authorName.equals(author.getName))
          workCount += workCount
      })
      author.setWorkCount(workCount)
      authorDao.addAuthor(author)
    })
    //stop work
    spark.stop()
  }

  def isUniqueAuthor(name: String): Boolean = {
    val isUnique = !bloomFilter.mightContain(name)
    if (isUnique) {
      bloomFilter.put(name)
    }
    isUnique
  }

  //case class
  case class IdAuthor(paperId: String, authorName: String)

}
