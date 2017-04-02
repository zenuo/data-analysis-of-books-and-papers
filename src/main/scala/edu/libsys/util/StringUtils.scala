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
    val word01 = "本书"
    val word02 = "编写"
    val word03 = "委员会"
    val word04 = "编著"
    val word05 = "]"
    val word06 = "["
    val word07 = "主编"
    val word08 = "编绘"
    val word09 = "本社"
    val word10 = "著"
    val word11 = "编委会"
    val word12 = "本书"
    val word13 = "编"
    val word14 = "编辑组"
    val word15 = "编写"
    token.replaceAll(word01, "")
      .replaceAll(word02, "")
      .replaceAll(word03, "")
      .replaceAll(word04, "")
      .replace(word05, "")
      .replace(word06, "")
      .replaceAll(word07, "")
      .replaceAll(word08, "")
      .replaceAll(word09, "")
      .replaceAll(word10, "")
      .replaceAll(word11, "")
      .replaceAll(word12, "")
      .replaceAll(word13, "")
      .replaceAll(word14, "")
      .replaceAll(word15, "")
  }

  def doubleTupleToString(tuple: (Int, Int)): String = {
    //"\"" + tuple._1.toString + "\",\"" + tuple._2.toString + "\""
    tuple._1.toString + "," + tuple._2.toString
  }
}
