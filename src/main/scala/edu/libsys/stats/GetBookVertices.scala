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
  def work(book_id_author: String, sc: SparkContext): RDD[(VertexId, (Int, Int, Int, Int, Int, Int, Int, Int))] = {
    //分割符
    val delimiter01 = "#"
    //返回RDD
    sc.textFile(book_id_author).map(line => {
      val tokens = line.replace(",", "")
        .split(delimiter01)
        .map(_.trim)
      //格式为(节点ID, bba, bbcid, bpa, bcnpf, bcnpi, 占位符（论文节点是六个属性）)
      /*
      bba 图书与图书在作者上的联系
      bbcid 图书与图书在中图法分类号上的联系
      bpa 图书与论文在作者上的联系
      bcnpf 图书的中图法分类名与论文的领域名称的联系
      bcnpi 图书的中图法分类名与论文的关键词的联系
       */
      (tokens(0).toLong, (0, 0, 0, 0, 0, 0, 0, 0))
    })
  }
}
