package edu.libsys.stats

import edu.libsys.util.EdgeUtil
import org.apache.spark.SparkContext
import org.apache.spark.graphx.{Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * 第三步骤
  * 根据“节点、边文件”计算出“每个节点的入度”，并保存为文本文件。
  * 在更新权重后，此步骤不需要重新执行。
  */
object GetInDegrees {
  /**
    * 主方法
    *
    * @param args 命令行参数
    */
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .appName("IntegrateInDegrees")
      .getOrCreate()
    //use the Kryo library to serialize objects
    spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    val sc: SparkContext = spark.sparkContext

    val sourcePath: String = "/home/spark/data/graph/edges/"
    val resultPath: String = "/home/spark/data/result/"

    val vertices: RDD[(VertexId, Int)] =
      sc.textFile("/home/spark/data/txt/all.txt")
        .map(_.toLong -> 0)

    val bbaGraph: Graph[Int, Int] = Graph(vertices,
      sc.textFile(sourcePath + "edgesOfBookBookGraphByAuthor.txt").map(EdgeUtil.stringToEdge))
    //使用fullOuterJoin，确保无入度的节点也在结果中
    val indOfbba: RDD[(VertexId, Int)] = vertices
      .fullOuterJoin(bbaGraph.inDegrees)
      .map(tuple => {
        tuple._1 -> tuple._2._2.getOrElse(0)
      })
    indOfbba.saveAsTextFile(resultPath + "indOfbba")
    bbaGraph.unpersist()
    indOfbba.unpersist()

    val bbcidGraph: Graph[Int, Int] = Graph(vertices,
      sc.textFile(sourcePath + "edgesOfBookBookGraphByCLCId.txt-new").map(EdgeUtil.stringToEdge))
    val indOfbbcid: RDD[(VertexId, Int)] = vertices
      .fullOuterJoin(bbcidGraph.inDegrees)
      .map(tuple => {
        tuple._1 -> tuple._2._2.getOrElse(0)
      })
    indOfbbcid.saveAsTextFile(resultPath + "indOfbbcid")
    bbcidGraph.unpersist()
    indOfbbcid.unpersist()

    val ppaGraph: Graph[Int, Int] = Graph(vertices,
      sc.textFile(sourcePath + "edgesOfPaperPaperGraphByAuthor.txt").map(EdgeUtil.stringToEdge))
    val indOfppa: RDD[(VertexId, Int)] = vertices
      .fullOuterJoin(ppaGraph.inDegrees)
      .map(tuple => {
        tuple._1 -> tuple._2._2.getOrElse(0)
      })
    indOfppa.saveAsTextFile(resultPath + "indOfppa")
    ppaGraph.unpersist()
    indOfppa.unpersist()

    val ppiGraph: Graph[Int, Int] = Graph(vertices,
      sc.textFile(sourcePath + "edgesOfPaperPaperGraphByIndexTerm.txt").map(EdgeUtil.stringToEdge))
    val indOfppi: RDD[(VertexId, Int)] = vertices
      .fullOuterJoin(ppiGraph.inDegrees)
      .map(tuple => {
        tuple._1 -> tuple._2._2.getOrElse(0)
      })
    indOfppi.saveAsTextFile(resultPath + "indOfppi")
    ppiGraph.unpersist()
    indOfppi.unpersist()

    val bpaGraph: Graph[Int, Int] = Graph(vertices,
      sc.textFile(sourcePath + "edgesOfBookPaperGraphByAuthor.txt").map(EdgeUtil.stringToEdge))
    val indOfbpa: RDD[(VertexId, Int)] = vertices
      .fullOuterJoin(bpaGraph.inDegrees)
      .map(tuple => {
        tuple._1 -> tuple._2._2.getOrElse(0)
      })
    indOfbpa.saveAsTextFile(resultPath + "indOfbpa")
    bpaGraph.unpersist()
    indOfbpa.unpersist()

    val bcnpfGraph: Graph[Int, Int] = Graph(vertices,
      sc.textFile(sourcePath + "edgesOfBookPaperGraphByFieldAndCLCName.txt").map(EdgeUtil.stringToEdge))
    val indOfbcnpf: RDD[(VertexId, Int)] = vertices
      .fullOuterJoin(bcnpfGraph.inDegrees)
      .map(tuple => {
        tuple._1 -> tuple._2._2.getOrElse(0)
      })
    indOfbcnpf.saveAsTextFile(resultPath + "indOfbcnpf")
    bcnpfGraph.unpersist()
    indOfbcnpf.unpersist()

    val bcnpiGraph: Graph[Int, Int] = Graph(vertices,
      sc.textFile(sourcePath + "edgesOfBookPaperGraphByIndexTermAndCLCName.txt").map(EdgeUtil.stringToEdge))
    val indOfbcnpi: RDD[(VertexId, Int)] = vertices
      .fullOuterJoin(bcnpiGraph.inDegrees)
      .map(tuple => {
        tuple._1 -> tuple._2._2.getOrElse(0)
      })
    indOfbcnpi.saveAsTextFile(resultPath + "indOfbcnpi")
    bcnpiGraph.unpersist()
    indOfbcnpi.unpersist()

    sc.stop()
    spark.stop()
  }
}
