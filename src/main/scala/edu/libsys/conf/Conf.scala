package edu.libsys.conf

object Conf {
  /**
    * 论文编号偏移量
    */
  val paperIdOffset: Int = 1000000000

  /**
    * 图书与图书在作者上的联系的权重
    */
  val weightOfBookBookRelationshipByAuthor = 50

  /**
    * 图书与图书在中图法分类号上的联系的权重
    */
  val weightOfBookBookRelationshipByCLCId = 4

  /**
    * 论文与论文在作者上的联系的权重
    */
  val weightOfPaperPaperRelationshipByAuthor = 50

  /**
    * 论文与论文在领域名称上的联系的权重
    */
  val weightOfPaperPaperRelationshipByField = 1

  /**
    * 论文与论文在关键词上的联系的权重
    */
  val weightOfPaperPaperRelationshipByIndexTerm = 12

  /**
    * 图书与论文在作者上的联系的权重
    */
  val weightOfPaperBookRelationshipByAuthor = 90

  /**
    * 图书的中图法分类名与论文的领域名称的联系的权重
    */
  val weightOfPaperBookRelationshipByIndexTermAndCLCName = 90

  /**
    * 图书的中图法分类名与论文的关键词的联系的权重
    */
  val weightOfPaperBookRelationshipByFieldAndCLCName = 180

  /**
    * 任务数量
    */
  val numTasks: Int = 6
}
