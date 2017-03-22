package edu.libsys.util

object StringUtils {
  //从“O411.1-44”中匹配“O411”
  def parseCLCId(string: String): String = {
    //正则
    val pattern = "([A-Z])\\w+".r
    pattern.findFirstIn(string).getOrElse("")
  }
}
