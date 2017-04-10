package edu.libsys.stats

import edu.libsys.conf.Conf
import org.apache.spark.SparkContext
import org.apache.spark.graphx.VertexId
import org.apache.spark.rdd.RDD

object GetPaperVertices {
  /**
    * 获得所有论文顶点
    *
    * @param paper_id_paperId “paper_id_paperId”文件路径
    * @return RDD[(VertexId, Int)]
    */
  def work(paper_id_paperId: String, sc: SparkContext): RDD[(VertexId, Int)] = {
    //分割符
    val delimiter01 = ","
    //返回RDD
    sc.textFile(paper_id_paperId).map(line => {
      val tokens = line
        .split(delimiter01)
        .map(_.trim)
      //为与图书id区别，论文id在处理过程中作加一亿处理
      //格式为(节点ID,边权重之和)，其中“边权重之和”初始值为0
      (tokens(0).toLong + Conf.paperIdOffset.toLong, 0)
    })
  }
}
