package edu.libsys.stats

import org.apache.spark.SparkContext
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

object GetBookVertices {
  /**
    * 获取所有图书顶点
    *
    * @param book_id_author “book_id_author”文件路径
    * @return RDD[(VertexId, Int)]
    */
  def work(book_id_author: String, sc: SparkContext): RDD[(VertexId, Int)] = {
    //分割符
    val delimiter01 = "#"
    //返回RDD
    sc.textFile(book_id_author).map(line => {
      val tokens = line.replace(",", "")
        .split(delimiter01)
        .map(_.trim)
      //格式为(节点ID,边权重之和)，其中“边权重之和”初始值为0
      (tokens(0).toLong, 0)
    })
  }
}
