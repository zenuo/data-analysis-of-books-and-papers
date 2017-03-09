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
    val item = spark.sparkContext.textFile("/home/spark/Project/data/csv/item.csv")
    val lend_hist = spark.sparkContext.textFile("/home/spark/Project/data/csv/lend_hist.csv")
    //cache data
    item.cache()
    lend_hist.cache()
    //test
    val histRecord = parseHistRecord(lend_hist.first())

    //stop
    spark.stop()
  }

  //parse String to histRecord
  def parseHistRecord(line: String) = {
    //replace """(comma)
    val pieces = line.replaceAll("\"", "").split(",")
    println(line.replaceAll("\"", ""))
    val cert_id = pieces(0).toInt
    val time = pieces(1)
    val record_no = pieces(2).toInt
    val call_no = pieces(3)
    HistRecord(cert_id, time, record_no, call_no)
  }
}
