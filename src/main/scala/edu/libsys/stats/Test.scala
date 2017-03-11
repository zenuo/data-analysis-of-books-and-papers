package edu.libsys.stats

import edu.libsys.entity.{Item, LendRecord}

/**
  * Created by spark on 3/11/17.
  */
object Test {
  def main(args: Array[String]) : Unit= {
    val s1 = "99000328,0000006280,0"
    val i:Item = parseItem(s1)
    println(i.toString)
    val s2 = "\"99321117\",\"2000-09-0209:54:56\",\"0000040935\",\"N49/81:1\""
    val l:LendRecord = parseLendRecord(s2)
    println(l.toString)
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
