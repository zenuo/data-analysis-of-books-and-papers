package edu.libsys.stats

import edu.libsys.entity.{Author, Item, LendRecord}

import scala.collection.mutable.ArrayBuffer

object Test {
  def main(args: Array[String]): Unit = {
    val authorList = ArrayBuffer[Author]()
    val author = new Author("lihua")
    authorList += author
    authorList += author
    authorList.foreach(println(_))
  }

  //parse String to LendRecord
  def parseLendRecord(line: String): LendRecord = {
    val pieces = line.replaceAll("\"", "").split(",")
    val cert_id = pieces(0).toInt
    val time = pieces(1)
    val marc_rec_id = pieces(2).toInt
    new LendRecord(cert_id, time, marc_rec_id)
  }

  //parse String to Item
  def parseItem(line: String): Item = {
    val pieces = line.split(",")
    val prop_id = pieces(0).toInt
    val marc_rec_id = pieces(1).toInt
    val count = pieces(2).toInt
    new Item(prop_id, marc_rec_id, count)
  }
}
