package edu.libsys.util

import edu.libsys.conf.Conf

/**
  * 节点工具列
  */
object VertexUtil {
  /**
    * 根据节点判定节点类型
    * 图书0
    * 论文1
    *
    * @param vertex 需要判断类型的节点
    * @return 类型
    */
  def getVertexType(vertex: (Long, (Int, Int, Int, Int, Int, Int, Int, Int))): Int = {
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
    * 根据节点类型格式化字符串
    *
    * @param vertex     需要格式化成字符串的节点
    * @param vertexType 需要格式化成字符串的节点的类型
    * @return 格式化后的字符串
    */
  def vertexToString(vertex: (Long, (Int, Int, Int, Int, Int, Int, Int, Int)), vertexType: Int): String = {
    //字符串
    var string = ""
    if (vertexType == 0) {
      //图书
      //计算三个权重
      val weight1 = vertex._2._1 * Conf.weight1._1 +
        vertex._2._2 * Conf.weight1._2 +
        vertex._2._6 * Conf.weight1._6 +
        vertex._2._7 * Conf.weight1._7 +
        vertex._2._8 * Conf.weight1._8
      val weight2 = vertex._2._1 * Conf.weight2._1 +
        vertex._2._2 * Conf.weight2._2 +
        vertex._2._6 * Conf.weight2._6 +
        vertex._2._7 * Conf.weight2._7 +
        vertex._2._8 * Conf.weight2._8
      val weight3 = vertex._2._1 * Conf.weight3._1 +
        vertex._2._2 * Conf.weight3._2 +
        vertex._2._6 * Conf.weight3._6 +
        vertex._2._7 * Conf.weight3._7 +
        vertex._2._8 * Conf.weight3._8

      //id:ID(B-ID),weight1:int,weight2:int,weight3:int,:LABEL
      string = s"${vertex._1},$weight1,$weight2,$weight3,B"
    } else if (vertexType == 1) {
      //论文
      //计算三个权重
      val weight1 = vertex._2._3 * Conf.weight1._3 +
        //vertex._2._4 * Conf.weight1._4 +
        vertex._2._5 * Conf.weight1._5 +
        vertex._2._6 * Conf.weight1._6 +
        vertex._2._7 * Conf.weight1._7 +
        vertex._2._8 * Conf.weight1._8
      val weight2 = vertex._2._3 * Conf.weight2._3 +
        //vertex._2._4 * Conf.weight2._4 +
        vertex._2._5 * Conf.weight2._5 +
        vertex._2._6 * Conf.weight2._6 +
        vertex._2._7 * Conf.weight2._7 +
        vertex._2._8 * Conf.weight2._8
      val weight3 = vertex._2._3 * Conf.weight3._3 +
        //qvertex._2._4 * Conf.weight3._4 +
        vertex._2._5 * Conf.weight3._5 +
        vertex._2._6 * Conf.weight3._6 +
        vertex._2._7 * Conf.weight3._7 +
        vertex._2._8 * Conf.weight3._8

      //id:ID(P-ID),weight1:int,weight2:int,weight3:int,:LABEL
      string = s"${vertex._1 - Conf.paperIdOffset},$weight1,$weight2,$weight3,P"
    }
    //返回字符串
    string
  }
}
