package edu.libsys.stats

import org.apache.spark.sql.SparkSession

object BookPaperFieldIndexTermStats {
  def main(args: Array[String]): Unit = {
    //create spark session
    val spark = SparkSession
      .builder()
      .appName("BookPaperFieldIndexTermStats")
      .getOrCreate()


  }
}
