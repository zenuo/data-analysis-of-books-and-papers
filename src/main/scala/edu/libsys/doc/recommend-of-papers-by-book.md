# 关联书籍的论文推荐
> 思路：通过书籍的“作者、标题、中图分类”与论文的“作者、关键词、领域”进行关联后，在数据库中保存分析出的关联，再显示到客户端，达到推荐的目的。

## 一、原理
### 1.数据来源
图书馆图书数据库、论文数据库；
### 2.数据预处理
为加快Spark读取数据的速度，我们将数据库中记录保存成7份文件；文件有如下两个部分：
####图书数据文件：
##### a.图书ID与作者关联文件（book_id_author.txt）
内容截图：
 
##### b.图书ID与中图法分类号关联文件（book_id_CLCId.txt）
内容截图：
 
##### c.中图法分类号与中图法分类名关联文件（cls_no_name.txt）
内容截图：
 
论文数据文件：
##### d.论文ID与论文系统唯一标识关联文件（paper_id_paperId.txt）
内容截图：
 
##### e.论文系统唯一标识与论文作者关联文件（paper_paperId_author.txt）
内容截图：
 
##### f.论文系统唯一标识与论文领域名称关联文件（paper_paperId_field.txt）
内容截图：
 
##### g.论文系统唯一标识与论文关键词关联文件（paper_paperId_indexTerm.txt）
内容截图：
 

### 3.数据处理
#### 第一阶段：
##### 目的：
将7份文件加载到内存中，生成7类RDD；

##### 实现：
图书部分：
加载book_id_author.txt到内存中，将每行数据用“#”分割，形成长度为2的数组（如{“1”, “陈世秀”}），使用spark的map函数利用刚才的数组生成格式为(图书ID,作者)的二元元组（如(1, “陈世秀”)），代码截图如下：
 
整个文件生成一个格式为(作者,图书ID)的二元元组集合，在Spark中称为RDD(Resilient Distributed Dataset，弹性分布式数据集)；
同理，根据book_id_CLCId.txt文件生成格式为(中图法分类号,图书ID)的二元元组集合、根据cls_no_name.txt文件生成格式为(中图法分类号,中图法分类名)的二元元组集合；
如图：
 

论文部分：
与上同理，根据paper_id_paperId.txt文件生成格式为(论文系统唯一标识,论文ID)的二元元组集合、根据paper_paperID_author.txt文件生成格式为(论文系统唯一标识,作者)的二元元组集合、根据paper_paperId_field.txt文件生成格式为(论文系统唯一标识,领域名称)的二元元组集合、根据paper_paperId_indexTerm.txt文件生成格式为(论文系统唯一标识,关键词)的二元元组集合；
如图：
 
至此，第一阶段完成；

#### 第二阶段：
##### 目的：
图书部分：建立作者与图书ID、中图法分类名与图书ID两种对应关系；
论文部分：建立作者与论文ID、领域名称与论文ID、关键词与论文ID三种对应关系；
##### 实现：
图书部分：
作者与图书ID：
在上阶段已完成，即bookAuthorIdRDD
中图法分类名与图书ID：
由bookCLCIdCLCNameTupleList与bookIdCLCIdTupleList做join运算，即生成格式为(中图法分类号,(中图法分类名,图书ID))的二元元组（其中一元为嵌套的元组）的集合，我们将嵌套元组提取出来生成新的元组(map运算)，达到目的，即是bookCLCNameIdRDD，代码如图：
 
示意图如下：
 

论文部分：
作者与论文ID：
由paperAuthorPaperIDTupleList与paperIdPaperIDTupleList做join运算，即生成格式为(论文系统唯一标识,(作者与论文ID))的二元元组（其中一元为嵌套的元组）的集合，我们将嵌套元组提取出来生成新的元组(map运算)，达到目的，即是paperAuthorIdRDD，代码如图：
 
示意图如下：
 
领域名称与论文ID：
由paperPaperIdFieldTupleList与paperIdPaperIDTupleList做join运算，即生成格式为(论文系统唯一标识,(领域名称与论文ID))的二元元组（其中一元为嵌套的元组）的集合，我们将嵌套元组提取出来生成新的元组(map运算)，达到目的，即是PaperFieldIdRDD，代码如图：
 
示意图如下：
 
关键词与论文ID：
由paperPaperIdFieldTupleList与paperPaperIdIndexTermTupleList做join运算，即生成格式为(论文系统唯一标识,(关键词与论文ID))的二元元组（其中一元为嵌套的元组）的集合，我们将嵌套元组提取出来生成新的元组(map运算)，达到目的，即是paperIndexTermIdRDD，代码如图：
 
示意图如下：
 
至此，本阶段目的达成，如图：
 
#### 第三阶段：
##### 目的：
图书部分：图书与图书在作者上的联系、图书与图书在中图法分类名上的联系；
论文部分：论文与论文在作者上的联系、论文与论文在领域名称上的联系、论文与论文在关键词上的联系；
图书与论文部分：
图书与论文在作者上的联系、图书的中图法分类名与论文的领域名称的联系、图书的中图法分类名与论文的关键词的联系；

##### 实现：
图书部分：
图书与图书在作者上的联系：
通过bookAuthorIdRDD与自身做join和map运算实现（排除自身与自身的联系），得到joinedBookBookRelationshipByAuthorRDD，代码如图：
 
示意图如图：
 
图书与图书在中图法分类名上的联系：
通过bookCLCNameIdRDD与自身做join和map运算实现（排除自身与自身的联系），得到joinedBookBookRelationshipByCLCNameRDD，代码如图：
 
示意图如图：
 
论文部分：
论文与论文在作者上的联系：
根据paperAuthorIdRDD与自身做join和map运算实现（排除自身与自身的联系），得到joinedPaperPaperRelationshipByAuthorRDD，代码如图：
 
示意图如下：
 
论文与论文在领域名称上的联系：
根据PaperFieldIdRDD与自身做join和map运算实现（排除自身与自身的联系），得到joinedPaperPaperRelationshipByFieldRDD，代码如图：
 
示意图如下：
 
论文与论文在关键词上的联系：
根据paperIndexTermIdRDD与自身做join和map运算实现（排除自身与自身的联系），得到joinedPaperPaperRelationshipByIndexTermRDD，代码如图：
 
示意图如下：
 

图书与论文部分：
图书与论文在作者上的联系：
由paperAuthorIdRDD和bookAuthorIdRDD做join和map，得到
paperBookRelationshipListByAuthor，代码如图：
 
示意图如下：
 
图书的中图法分类名与论文的领域名称的联系：
由paperFieldIdRDD和bookCLCNameIdRDD做join和map，得到
joinedPaperBookRelationshipListByFieldAndCLCName，代码如图：
 
示意图如下：
 
图书的中图法分类名与论文的关键词的联系：
由paperFieldIdRDD与bookCLCNameIdRDD做join和map，得到
joinedPaperBookRelationshipListByIndexTermAndCLCName，代码如图：
 
示意图如下：
 
至此，本阶段目的达成，如图：
 

#### 第四阶段：
##### 目的：
生成图书与图书的联系、论文与论文的联系、论文与图书的联系；

##### 实现：
图书与图书的联系：
将排重（distinct方法）后的joinedBookBookRelationshipByAuthorRDD与排重后joinedBookBookRelationshipByCLCNameRDD做合并，然后排重，得到bookBookRelationshipRDD，代码如图： 
示意图如下：
 
论文与论文的联系：
将排重后的joinedPaperPaperRelationshipByAuthorRDD与排重后的joinedPaperPaperRelationshipByIndexTermRDD、排重后的joinedPaperPaperRelationshipByFieldRDD做合并，然后排重，得到paperPaperRelationshipRDD，代码如图：
 
示意图如下：
 
论文与图书的联系：
将排重后的paperBookRelationshipListByAuthor与排重后的joinedPaperBookRelationshipListByFieldAndCLCName、排重后的joinedPaperBookRelationshipListByIndexTermAndCLCName做合并，然后排重，得到paperBookRelationshipRDD，代码如图：
 
示意图如下：
 

至此，此阶段目标达成，示意图如下：
 

数据分析完成，利用得到的三类数据即可完成根据图书推荐图书、根据图书推荐论文、根据论文推荐论文、根据论文推荐图书。

#### 第四阶段：
##### 目的：
生成图书与图书的联系、论文与论文的联系、论文与图书的联系；

##### 实现：

### 2.关联权重
#### 图书与图书在作者上的联系 3
#### 图书与图书在中图法分类名上的联系 1
#### 论文与论文在作者上的联系 3
#### 论文与论文在领域名称上的联系 1
#### 论文与论文在关键词上的联系 2
#### 图书与论文在作者上的联系 3
#### 图书的中图法分类名与论文的领域名称的联系 1
#### 图书的中图法分类名与论文的关键词的联系 2

## 三、过程记录
### 1.启动hadoop集群
Master执行：
```
/usr/local/hadoop/sbin/start-dfs.sh && /usr/local/hadoop/sbin/start-yarn.sh
```
查看：
```markdown
192.168.1.124:8088
```
### 2.启动spark集群
Master执行：
```markdown
/usr/local/spark/sbin/start-all.sh
```
查看：
```markdown
192.168.1.124:8080
```
### 3.程序及数据文件路径：
程序：Master:/home/spark/spark-stats.jar
文件：hdfs:///home/spark/data/*

### 4.提交作业
```markdown
/usr/local/spark/bin/spark-submit \
    --class edu.libsys.Main \
    --master yarn \
    --deploy-mode cluster \ 
    --executor-memory 3G \
    --queue default \
    /home/spark/book-stats-1.0.jar \
    hdfs:///user/data \
    hdfs:///user/result
```