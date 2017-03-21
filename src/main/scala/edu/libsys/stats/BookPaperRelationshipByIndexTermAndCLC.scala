package edu.libsys.stats

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
    /*
    //判断文件参数是否正确
    if (args.length != 3){
      println("-------------------------------ERROR-------------------------------")
      println("Valid program arguments:")
      println("PATH_TO_paper_paperID_author.txt PATH_TO_paper_id_paperId.txt PATH_TO_book_id_author_title.txt")
      println("please try again, exit now.")
      println("-------------------------------------------------------------------")
      sys.exit()
    }
    */
    //创建会话
    val spark = SparkSession
      .builder()
      .appName("BookPaperRelationshipByIndexTermAndCLC")
      .getOrCreate()


  }
}
