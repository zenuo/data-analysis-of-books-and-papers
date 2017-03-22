package edu.libsys.stats

import edu.libsys.dataclean.RemoveInvalid

object Test {
  def main(args: Array[String]): Unit = {
    /*val authorList = ArrayBuffer[Author]()
    val author = new Author("lihua")
    authorList += author
    authorList += author
    authorList.foreach(println(_))*/
    println(RemoveInvalid.isChineseChar("字"))
  }
}
