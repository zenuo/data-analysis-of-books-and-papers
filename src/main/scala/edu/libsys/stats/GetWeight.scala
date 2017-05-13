package edu.libsys.stats

import edu.libsys.conf.Conf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
  * 整合入度
  */
object GetWeight {

  /**
    * 主方法
    *
    * @param args 命令行参数
    */
  def main(args: Array[String]): Unit = {

    /**
      * spark会话
      */
    val spark: SparkSession = SparkSession
      .builder()
      .appName("GetWeight")
      .getOrCreate()
    //use the Kryo library to serialize objects
    spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sc: SparkContext = spark.sparkContext

    val sourcePath: String = "/home/spark/data/graph/indegrees/"
    val resultPath: String = "/home/spark/data/result/"

    val empty: RDD[(Int, (Int, Int, Int))] =
      sc.textFile("/home/spark/data/txt/all.txt")
        .map(_.toInt -> (0, 0, 0))

    val indOfbba: RDD[(Int, Int)] = sc.textFile("indOfbba.txt").map(stringToTuple)
    val indOfbbcid: RDD[(Int, Int)] = sc.textFile("indOfbbcid.txt").map(stringToTuple)
    val indOfppa: RDD[(Int, Int)] = sc.textFile("indOfppa.txt").map(stringToTuple)
    val indOfppi: RDD[(Int, Int)] = sc.textFile("indOfppi.txt").map(stringToTuple)
    val indOfbpa: RDD[(Int, Int)] = sc.textFile("indOfbpa.txt").map(stringToTuple)
    val indOfbcnpf: RDD[(Int, Int)] = sc.textFile("indOfbcnpf.txt").map(stringToTuple)
    val indOfbcnpi: RDD[(Int, Int)] = sc.textFile("indOfbcnpi.txt").map(stringToTuple)

    //精确性权重
    val weight01Ofbba: RDD[(Int, Int)] =
      indOfbba.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight1._1
      })
    val weight01Ofbbcid: RDD[(Int, Int)] =
      indOfbbcid.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight1._2
      })
    val weight01Ofppa: RDD[(Int, Int)] =
      indOfppa.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight1._3
      })
    val weight01Ofppi: RDD[(Int, Int)] =
      indOfppi.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight1._5
      })
    val weight01Ofbpa: RDD[(Int, Int)] =
      indOfbpa.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight1._6
      })
    val weight01Ofbcnpf: RDD[(Int, Int)] =
      indOfbcnpf.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight1._7
      })
    val weight01Ofbcnpi: RDD[(Int, Int)] =
      indOfbcnpi.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight1._8
      })

    //折中权重
    val weight02Ofbba: RDD[(Int, Int)] =
      indOfbba.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight2._1
      })
    val weight02Ofbbcid: RDD[(Int, Int)] =
      indOfbbcid.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight2._2
      })
    val weight02Ofppa: RDD[(Int, Int)] =
      indOfppa.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight2._3
      })
    val weight02Ofppi: RDD[(Int, Int)] =
      indOfppi.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight2._5
      })
    val weight02Ofbpa: RDD[(Int, Int)] =
      indOfbpa.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight2._6
      })
    val weight02Ofbcnpf: RDD[(Int, Int)] =
      indOfbcnpf.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight2._7
      })
    val weight02Ofbcnpi: RDD[(Int, Int)] =
      indOfbcnpi.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight2._8
      })

    //多样性权重
    val weight03Ofbba: RDD[(Int, Int)] =
      indOfbba.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight3._1
      })
    val weight03Ofbbcid: RDD[(Int, Int)] =
      indOfbbcid.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight3._2
      })
    val weight03Ofppa: RDD[(Int, Int)] =
      indOfppa.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight3._3
      })
    val weight03Ofppi: RDD[(Int, Int)] =
      indOfppi.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight3._5
      })
    val weight03Ofbpa: RDD[(Int, Int)] =
      indOfbpa.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight3._6
      })
    val weight03Ofbcnpf: RDD[(Int, Int)] =
      indOfbcnpf.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight3._7
      })
    val weight03Ofbcnpi: RDD[(Int, Int)] =
      indOfbcnpi.map(tuple => {
        tuple._1 -> tuple._2 * Conf.weight3._8
      })

    //聚合权重1
    val weight01: RDD[(Int, Int)] = weight01Ofbba
      .union(weight01Ofbbcid)
      .union(weight01Ofppa)
      .union(weight01Ofppi)
      .union(weight01Ofbpa)
      .union(weight01Ofbcnpf)
      .union(weight01Ofbcnpi)
      .reduceByKey(_ + _)
    //聚合权重2
    val weight02: RDD[(Int, Int)] = weight02Ofbba
      .union(weight02Ofbbcid)
      .union(weight02Ofppa)
      .union(weight02Ofppi)
      .union(weight02Ofbpa)
      .union(weight02Ofbcnpf)
      .union(weight02Ofbcnpi)
      .reduceByKey(_ + _)
    //聚合权重3
    val weight03: RDD[(Int, Int)] = weight03Ofbba
      .union(weight03Ofbbcid)
      .union(weight03Ofppa)
      .union(weight03Ofppi)
      .union(weight03Ofbpa)
      .union(weight03Ofbcnpf)
      .union(weight03Ofbcnpi)
      .reduceByKey(_ + _)

    //聚合三种权重
    val tempWeight01: RDD[(Int, (Int, Int, Int))] = empty
      .join(weight01)
      .map { t =>
        t._1 -> (t._2._2, 0, 0)
      }

    val tempWeight02: RDD[(Int, (Int, Int, Int))] = tempWeight01
      .join(weight02)
      .map(t => {
        t._1 -> (t._2._1._1, t._2._2, 0)
      })

    val weight: RDD[(Int, (Int, Int, Int))] = tempWeight02
      .join(weight03)
      .map(t => {
        t._1 -> (t._2._1._1, t._2._1._1, t._2._2)
      })

    //保存到文本
    weight.filter(_._1 < Conf.paperIdOffset)
      .map(weightToString)
      .saveAsTextFile("/home/spark/data/result/books")

    weight.filter(_._1 > Conf.paperIdOffset)
      .map(weightToString)
      .saveAsTextFile("/home/spark/data/result/papers")

    sc.stop()
    spark.stop()
  }

  /**
    * 将权重元组输出字符串
    *
    * @param weight 权重元组
    * @return 转换后的字符串
    */
  def weightToString(weight: (Int, (Int, Int, Int))): String = {
    var string: String = ""
    if (weight._1 > Conf.paperIdOffset) {
      string = s"${weight._1 - Conf.paperIdOffset},${weight._2._1},${weight._2._2},${weight._2._3},P"
    } else {
      string = s"${weight._1},${weight._2._1},${weight._2._2},${weight._2._3},B"
    }
    string
  }

  /**
    * 将字符串转为二元组
    *
    * @param string 字符串
    * @return 二元组
    */
  def stringToTuple(string: String): (Int, Int) = {
    val tokens: Array[String] = string.replace("(", "").replace(")", "").split(",")
    tokens(0).toInt -> tokens(1).toInt
  }


}
