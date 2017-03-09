package edu.libsys

import org.apache.spark.sql.SparkSession

object Main {

  //lend record
  case class histRecord(cert_id: Int, time: String, record_no: Int, call_no: String)

  //item
  case class item(prop_no: Int, record_no: Int, count: Int)

  //main method
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("BookStatistics")
      .getOrCreate()
    //load files
    val item = spark.sparkContext.textFile("/home/spark/Project/data/csv/ITEM.csv")
    val book = spark.sparkContext.textFile("/home/spark/Project/data/csv/ASORD_MARC.csv")
    //cache data
    item.cache()
    book.cache()

    spark.stop()
  }
}
