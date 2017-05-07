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
  def GetVertexType(vertex: (Long, (Int, Int, Int, Int, Int, Int, Int, Int))): Int = {
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
  def VertexToString(vertex: (Long, (Int, Int, Int, Int, Int, Int, Int, Int)), vertexType: Int): String = {
    //字符串
    var string = ""
    if (vertexType == 0) {
      //图书
      //id:ID(B-ID),:LABEL,bba:int,bbcid:int,bpa:int,bcnpf:int,bcnpi:int
      string = s"${vertex._1},${vertex._2._1},${vertex._2._2},${vertex._2._6},${vertex._2._7},${vertex._2._8},B"
    } else if (vertexType == 1) {
      //论文
      //id:ID(P-ID),:LABEL,ppa:int,ppf:int,ppi:int,bpa:int,bcnpf:int,bcnpi:int
      string = s"${vertex._1 - Conf.paperIdOffset},${vertex._2._3},${vertex._2._4},${vertex._2._5},${vertex._2._6},${vertex._2._7},${vertex._2._8},P"
    }
    //返回字符串
    string
  }
}
