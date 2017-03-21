package edu.libsys.stats

import edu.libsys.data.dao.{AuthorDao, PaperBookRelationshipDao}
import org.apache.spark.sql.SparkSession

/*数据样例
head paper_id_paperId.txt
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
paper_paperID_author.txt
TGZG200701002,刘志军
10511-1012349726.nh,孙自俭
10511-1012349726.nh,朱英
10651-1014167683.nh,向开祥
10651-1014167683.nh,赵吉林
10456-1013119679.nh,李永超
10456-1013119679.nh,郭庆
TDJJ200801005,吴克俭
TGZG200701001,刘志军
JSJW201001110320,张力行
 */

/*数据样例
book_id_author-title_callId.txt
1,作者陈世秀等,最新应用文写作,H152.3
2,主编鲍志伸，陆建平,全国著名教师评改初中生作文文库,H194.5
3,鲍志伸，陆建平主编,全国著名教师评改高中生作文文库,H194.5
4,薛毅编著,最优化原理与方法,O224
5,邓洪波编著,中国书院章程,G649.299
6,薛庆主编,计算机应用基础习题与上机指导-第2版,TP39-44
7,主编崔山田等,预防医学,R1
8,曹军生等编著,SQL Server 2000实用教程,TP311.138SQ
9,主编张玉平,电子技术实验及电子电路计算机仿真,TN01-33
10,主编冯建奇等,研究生英语教程,H31
 */

object BookPaperRelationshipByAuthor {
  //若论文与图书作者相同，则建立关系；计数
  def main(args: Array[String]): Unit = {
    //判断文件参数是否正确
    if (args.length != 3) {
      println("-------------------------------ERROR-------------------------------")
      println("Valid program arguments:")
      println("PATH_TO_paper_paperID_author.txt PATH_TO_paper_id_paperId.txt PATH_TO_book_id_author_title.txt")
      println("please try again, exit now.")
      println("-------------------------------------------------------------------")
      sys.exit()
    }

    //创建会话
    val spark = SparkSession
      .builder()
      .appName("BookPaperRelationshipByAuthor")
      .getOrCreate()

    //创建数据库访问对象
    val authorDao = new AuthorDao()
    val paperBookRelationshipDao = new PaperBookRelationshipDao()

    //文件路径
    val paperInfo01Path = args(0)
    val paperInfo02Path = args(1)
    val bookInfoPath = args(2)
    /*
    val paperInfo01Path = "/home/spark/Project/data/txt/paper_paperID_author.txt"
    val paperInfo02Path = "/home/spark/Project/data/txt/paper_id_paperId.txt"
    val bookInfoPath = "/home/spark/Project/data/txt/book_id_author_title.txt"
    */

    //分割符
    val delimiter01 = ","
    val delimiter02 = "#"

    /*
    //计数
    //作者论文计数
    val authorPaperList = spark.sparkContext.textFile(paperInfo01Path).map(line => {
      val tokens = line.split(delimiter01).map(_.trim)
      tokens(1) -> 1
    })
    //作者图书计数
    val authorBookCountList = spark.sparkContext.textFile(bookInfoPath).map(line => {
      val tokens = line.split(delimiter02).map(_.trim)
      parseBookAuthor(tokens(1)) -> 1
    })
    //聚合两个MapPartitionsRDD
    //val authorStats = authorBookCountList.union(authorPaperList)
    //val authorWorkCount = authorStats.reduceByKey(_ + _)
    //遍历，将作者信息存入数据库

    authorWorkCount.foreach(tuple => {
      authorDao.addAuthor(new Author(tuple._1, tuple._2))
        })
    */

    //建立联系
    //获得论文id与作者的元组
    //paperID_author
    val paperAuthorPaperIDTupleList = spark.sparkContext.textFile(paperInfo01Path).map(line => {
      val tokens = line.split(delimiter01).map(_.trim)
      //类似(TGZG200701002,刘志军)
      tokens(0) -> tokens(1)
    })
    //println(paperAuthorpaperIDTupleList.first().toString()) is (TGZG200701002,刘志军)

    //id_paperId
    val paperIdPaperIDTupleList = spark.sparkContext.textFile(paperInfo02Path).map(line => {
      val tokens = line.split(delimiter01).map(_.trim)
      //类似(10001-1011132221.nh,1)
      tokens(1) -> tokens(0).toInt
    })
    //println(paperIdpaperIDTupleList.first().toString()) is (10001-1011132221.nh,1)

    //join两个MapPartitionsRDD
    val paperAuthorIDTupleList = paperAuthorPaperIDTupleList.join(paperIdPaperIDTupleList).map(tuple => {
      //tuple类似于(BGDH200609003,(杨竣辉,144810))
      tuple._2
    })
    //println(paperAuthorIDTupleList.first()) is (王立莉,300221)
    //println(paperAuthorIDTupleList.count()) is 825605

    //获得图书id与作者的元组
    val bookAuthorIDTupleList = spark.sparkContext.textFile(bookInfoPath).map(line => {
      //首先需要将转义字符串“\\,”替换成“，”
      val tokens = line.split(delimiter02).map(_.trim)
      parseBookAuthor(tokens(1)) -> tokens(0).toInt
    })
    //println(bookAuthorIDTupleList.first()) is (陈世秀,1)
    //println(bookAuthorIDTupleList.count()) is 1226327

    //join两个MapPartitionsRDD
    val bookPaperRelationshipList = bookAuthorIDTupleList.join(paperAuthorIDTupleList).map(tuple => {
      tuple._2
    })

    bookPaperRelationshipList.foreach(tuple => {
      paperBookRelationshipDao.addPaperBookRelationship(tuple._2, tuple._1)
    })
    //println(bookPaperRelationshipList.count()) is 5527071
    //println(bookPaperRelationshipList.first()) is (778473,2349) bookId,paperId

    //stop work
    spark.stop()
  }

  //删除一些字符
  def parseBookAuthor(token: String): String = {
    val word01 = "主"
    val word02 = "著"
    val word03 = "等"
    val word04 = "编"
    val word05 = "]"
    val word06 = "["
    val word07 = "作者"
    token.replaceAll(word01, "").replaceAll(word02, "").replaceAll(word03, "").replaceAll(word04, "").replace(word05, "").replace(word06, "").replace(word07, "")
  }
}
