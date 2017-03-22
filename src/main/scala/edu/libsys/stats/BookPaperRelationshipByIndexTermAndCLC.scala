package edu.libsys.stats

import edu.libsys.util.StringUtils
import org.apache.spark.sql.SparkSession

/*数据样例
book_id_callId.txt
1,H152.3
2,H194.5
3,H194.5
4,O224
5,G649.299
6,TP39-44
7,R1
8,TP311.138SQ
9,TN01-33
10,H31
 */

/*数据样例
cls_no_name.txt
S325,品种的整理与保存
S325.1,室内保存
S325.2,田间保存
S326,种质资源（品种资源）的开发与利用
S329,品种志、品种目录
S330,作物遗传育种
S330.2,种子生理、生化
S330.3,种子生态
S331,优良品质的育种
S332,抗逆品种的育种
 */

/*
paper_id_paperId.txt
1,10001-1011132221.nh
2,10001-1011132230
3,10001-1011132232.nh
4,10001-1011132236.nh
5,10001-1011132307.nh
6,10001-1011284507
7,10001-1012027136.nh
8,10001-1012027140
9,10001-1012027149.nh
10,10001-1012343710.nh
 */

/*数据样例
paper_paperId_indexTerm.txt
TGZG200701002,铁路
TGZG200701002,和谐铁路
TGZG200701002,交通运输
TGZG200701002,发展
TGZG200701002,形势
10511-1012349726.nh,铁路工人
10511-1012349726.nh,国有铁路
10511-1012349726.nh,交通部
10511-1012349726.nh,铁道部
10651-1014167683.nh,罗定铁路
 */

object BookPaperRelationshipByIndexTermAndCLC {
  def main(args: Array[String]): Unit = {
    //判断文件参数是否正确
    if (args.length != 5) {
      println("-------------------------------ERROR-------------------------------")
      println("Valid program arguments:")
      println("PATH_TO_book_id_callId.txt PATH_TO_cls_no_name.txt PATH_TO_paper_id_paperId.txt PATH_TO_paper_paperId_indexTerm.txt PATH_TO_resultFile")
      println("please try again, exit now.")
      println("-------------------------------------------------------------------")
      sys.exit()
    }

    //创建会话
    val spark = SparkSession
      .builder()
      .appName("BookPaperRelationshipByIndexTermAndCLC")
      .getOrCreate()

    //文件路径
    val bookInfoPath01 = args(0)
    val bookInfoPath02 = args(1)
    val paperInfoPath01 = args(2)
    val paperInfoPath02 = args(3)
    val resultFilePath = args(4)

    //分割符
    val delimiter01 = ","

    //建立联系
    //获得图书id与中图分类名的元组
    //book_id_CLCId
    val bookIdCLCIdTupleList = spark.sparkContext.textFile(bookInfoPath01).map(line => {
      val tokens = line.split(delimiter01).map(_.trim)
      //类似(H152,1)
      StringUtils.parseCLCId(tokens(1)) -> tokens(0).toInt
    })
    //book_CLCId_CLCName
    val bookCLCIdCLCNameTupleList = spark.sparkContext.textFile(bookInfoPath02).map(line => {
      val tokens = line.split(delimiter01).map(_.trim)
      //类似(S325,品种的整理与保存)
      tokens(0) -> tokens(1)
    })
    //join两个MapPartitionsRDD
    val bookCLCNameIdTupleList = bookCLCIdCLCNameTupleList.join(bookIdCLCIdTupleList).map(tuple => {
      //类似(品种的整理与保存,122)
      tuple._2
    })
    //bookCLCNameIdTupleList.count(): 1107331
    //bookCLCNameIdTupleList.first(): (地质勘控仪器,433339)

    //获得论文id与关键词的元组
    //paper_id_paperId
    val paperIdPaperIDTupleList = spark.sparkContext.textFile(paperInfoPath01).map(line => {
      val tokens = line.split(delimiter01).map(_.trim)
      //类似(10001-1011132221.nh,1)
      tokens(1) -> tokens(0).toInt
    })
    //paper_paperId_indexTerm
    val paperPaperIdIndexTermTupleList = spark.sparkContext.textFile(paperInfoPath02).map(line => {
      val tokens = line.split(delimiter01).map(_.trim)
      //类似(TGZG200701002,和谐铁路)
      tokens(0) -> tokens(1)
    })
    //join两个MapPartitionsRDD
    val paperIndexTermIdTupleList = paperPaperIdIndexTermTupleList.join(paperIdPaperIDTupleList).map(tuple => {
      //类似(和谐铁路,122)
      tuple._2
    })
    //paperIndexTermIdTupleList.count(): 2679067
    //paperIndexTermIdTupleList.first(): (中铁快运,100397)

    //分析联系
    //join两个MapPartitionsRDD
    val paperBookRelationshipList = paperIndexTermIdTupleList.join(bookCLCNameIdTupleList).map(tuple => {
      //类似(100,122)
      tuple._2
    })
    //paperBookRelationshipList.count(): 36696695
    //paperBookRelationshipList.first(): (508464,310462)

    //保存文本文件
    paperBookRelationshipList.saveAsTextFile(resultFilePath)

    //结束
    spark.stop()
  }
}
