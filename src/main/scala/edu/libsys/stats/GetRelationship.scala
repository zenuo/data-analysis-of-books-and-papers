package edu.libsys.stats

import edu.libsys.conf.Conf
import edu.libsys.util.EdgeUtil
import org.apache.spark.SparkContext
import org.apache.spark.graphx.Edge
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * 第二步骤
  * 根据边文件导出图书、论文关联CSV文件，便于导入图数据库Neo4j中。
  * 在更新权重后，此步骤不需要重新执行。
  */
object GetRelationship {
  def main(args: Array[String]): Unit = {
    /**
      * spark会话
      */
    val spark: SparkSession = SparkSession
      .builder()
      .appName("GetRelationshipCSV")
      .getOrCreate()
    //use the Kryo library to serialize objects
    spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sc: SparkContext = spark.sparkContext

    val sourcePath: String = "/home/spark/data/graph/edges/txt/"
    val resultPath: String = "/home/spark/data/result/"

    val bba: RDD[Edge[Int]] = sc.textFile(sourcePath + "edgesOfBookBookGraphByAuthor.txt").map(EdgeUtil.stringToEdge)
    val bbcid: RDD[Edge[Int]] = sc.textFile(sourcePath + "edgesOfBookBookGraphByCLCId.txt-new").map(EdgeUtil.stringToEdge)
    val bb = bba.union(bbcid)
      .filter(t => {
        t.dstId < Conf.paperIdOffset
      })
      .map(EdgeUtil.edgeToString(_, 0))
      .saveAsTextFile(resultPath + "bookBookRelationships")
    bba.unpersist()
    bbcid.unpersist()

    val ppa: RDD[Edge[Int]] = sc.textFile(sourcePath + "edgesOfPaperPaperGraphByAuthor.txt").map(EdgeUtil.stringToEdge)
    val ppi: RDD[Edge[Int]] = sc.textFile(sourcePath + "edgesOfPaperPaperGraphByIndexTerm.txt").map(EdgeUtil.stringToEdge)
    ppa.union(ppi)
      .filter(t => {
        t.srcId > Conf.paperIdOffset
      })
      .map(EdgeUtil.edgeToString(_, 1))
      .saveAsTextFile(resultPath + "paperPaperRelationships")
    ppa.unpersist()
    ppi.unpersist()

    val bpa: RDD[Edge[Int]] = sc.textFile(sourcePath + "edgesOfBookPaperGraphByAuthor.txt").map(EdgeUtil.stringToEdge)
    val bcnpf: RDD[Edge[Int]] = sc.textFile(sourcePath + "edgesOfBookPaperGraphByFieldAndCLCName.txt").map(EdgeUtil.stringToEdge)
    val bcnpi: RDD[Edge[Int]] = sc.textFile(sourcePath + "edgesOfBookPaperGraphByIndexTermAndCLCName.txt").map(EdgeUtil.stringToEdge)
    bpa.union(bcnpf)
      .union(bcnpi)
      .filter(t => {
        t.srcId < Conf.paperIdOffset && t.dstId > Conf.paperIdOffset
      })
      .map(EdgeUtil.edgeToString(_, 2))
      .saveAsTextFile(resultPath + "bookPaperRelationships")
    bpa.unpersist()
    bcnpf.unpersist()
    bcnpi.unpersist()

    sc.stop()
    spark.stop()
  }
}
