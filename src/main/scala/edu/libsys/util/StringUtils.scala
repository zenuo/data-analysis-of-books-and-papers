package edu.libsys.util

object StringUtils {
  //从“O411.1-44”中匹配“O411”
  def parseCLCId(string: String): String = {
    //正则
    val pattern = "([A-Z])\\w+".r
    pattern.findFirstIn(string).getOrElse("")
  }

  //删除一些多余字符
  def parseBookAuthor(token: String): String = {
    val word01 = "主"
    val word02 = "著"
    val word03 = "等"
    val word04 = "编"
    val word05 = "]"
    val word06 = "["
    val word07 = "作者"
    token.replaceAll(word01, "").replaceAll(word02, "").replaceAll(word03, "").replaceAll(word04, "").replace(word05, "").replace(word06, "").replace(word07, "")
  }
}
