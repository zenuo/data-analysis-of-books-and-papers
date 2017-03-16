package edu.libsys.stats

import edu.libsys.data.dao.LendRecordDao
import org.apache.spark.sql.SparkSession

object LendHistStats {
  def main(args: Array[String]): Unit = {
    //create spark session
    val spark = SparkSession
      .builder()
      .appName("AuthorStats")
      .getOrCreate()

    //create an instance of LendRecordDao
    val lendRecordDao = new LendRecordDao()

    //load file
    val filePath = "/home/spark/Project/data/txt/id_author.txt"
    val delimiter = ","

    //parse file to list of tuple
  }
}
