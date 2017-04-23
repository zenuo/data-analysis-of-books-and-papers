package edu.libsys.util

import edu.libsys.conf.Conf

object VertexUtil {
  /**
    * 根据顶点判定顶点类型
    * 图书0
    * 论文1
    *
    * @param vertex 需要判断类型的顶点
    * @return Int
    */
  def GetVertexType(vertex: (Long, Int)): Int = {
    //类型
    var vertexType = -1
    if (vertex._1 < Conf.paperIdOffset) {
      //图书
      vertexType = 0
    } else {
      //论文
      vertexType = 1
    }
    //返回类型
    vertexType
  }


  /**
    * 根据顶点类型格式化字符串
    *
    * @param vertex     需要格式化成字符串的顶点
    * @param vertexType 需要格式化成字符串的顶点的类型
    * @return String
    */
  def VertexToString(vertex: (Long, Int), vertexType: Int): String = {
    //编号
    var vertexId = ""
    //标识
    var label = ""
    if (vertexType == 0) {
      //图书
      vertexId = vertex._1.toString
      label = "Book"
    } else {
      //论文
      vertexId = (vertex._1 - Conf.paperIdOffset).toString
      label = "Paper"
    }
    //返回字符串
    s"$vertexId,${vertex._2},$label"
  }
}
