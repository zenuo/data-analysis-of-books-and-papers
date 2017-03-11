package edu.libsys

import org.apache.spark.sql.SparkSession
import edu.libsys.entity.Item;
import edu.libsys.entity.LendRecord;

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

  //parse String to LendRecord
  def parseLendRecord(line: String) = {
    //replace """(comma)
    val pieces = line.replaceAll("\"", "").split(",")
    val cert_id = pieces(0).toInt
    val time = pieces(1)
    val marc_rec_id = pieces(2).toInt
    val call_no = pieces(3)
    new LendRecord(cert_id, time, marc_rec_id, call_no)
  }

  //parse String to Item
  def parseItem(line: String) = {
    //replace """(comma)
    val pieces = line.split(",")
    val prop_id = pieces(0).toInt
    val marc_rec_id = pieces(1).toInt
    val count = pieces(2).toInt
    new Item(prop_id, marc_rec_id, count)
  }
}
