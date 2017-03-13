package edu.libsys

import org.apache.spark.sql.SparkSession

object Main {
  //main method
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("BookStatistics")
      .getOrCreate()
    //load data
    val items = spark.sparkContext.textFile("/home/spark/Project/data/csv/item.csv")
    val lendRecords = spark.sparkContext.textFile("/home/spark/Project/data/csv/lend_hist.csv")
    //cache data
    items.cache()
    lendRecords.cache()
    //test


    //stop
    spark.stop()
  }

  //
  def itemCountStats(): Unit = {

  }
}
