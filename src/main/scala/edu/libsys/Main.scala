package edu.libsys

import org.apache.spark.sql.SparkSession

object Main {

  /*
  //lend record
  case class LendRecord(cert_id: Int, time: String, marc_rec_id: Int, call_no: String)

  //item
  case class Item(prop_id: Int, marc_rec_id: Int, count: Int)
  */

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
