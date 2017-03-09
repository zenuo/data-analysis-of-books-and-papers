package edu.libsys

import org.apache.spark.sql.SparkSession

object Main {

  //lend record
  case class HistRecord(cert_id: Int, time: String, record_no: Int, call_no: String)

  //item
  case class Item(prop_no: Int, record_no: Int, count: Int)

  //main method
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("BookStatistics")
      .getOrCreate()
    //load data
    val items = spark.sparkContext.textFile("/home/spark/Project/data/csv/item.csv")
    val histRecords = spark.sparkContext.textFile("/home/spark/Project/data/csv/lend_hist.csv")
    //cache data
    items.cache()
    histRecords.cache()
    //test


    //stop
    spark.stop()
  }

  //parse String to HistRecord
  def parseHistRecord(line: String) = {
    //replace """(comma)
    val pieces = line.replaceAll("\"", "").split(",")
    val cert_id = pieces(0).toInt
    val time = pieces(1)
    val record_no = pieces(2).toInt
    val call_no = pieces(3)
    HistRecord(cert_id, time, record_no, call_no)
  }

  //parse String to Item
  def parseItem(line: String) = {
    //replace """(comma)
    val pieces = line.replaceAll("\"", "").split(",")
    val prop_no = pieces(0).toInt
    val record_no = pieces(1).toInt
    val count = pieces(2).toInt
    Item(prop_no, record_no, count)
  }
}
