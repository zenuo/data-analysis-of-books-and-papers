# 解决方案
> 思路：通过书籍的“作者、标题、中图分类”与论文的“作者、关键词、领域”进行关联后，在数据库中保存分析出的关联，再显示到客户端，达到推荐的目的。

## 一、目标与解决思路
### 目标
随着信息技术的发展，数据量越发巨大，虽然增加了知识量，但是也增加了信息检索的难度。在高校师生的学习过程中，也渐渐遇到关联信息检索难、或者信息关联度较低的问题，使得科研、学习效率降低。但是大数据技术的发展让我们发现一种解决方法，我们希望通过大数据的技术来有效解决关联数据之间的高质量关联的问题。
### 解决思路
通过开源的大数据技术分析（Hadoop与Spark）计算高校图书馆资源（图书、论文）之间的关联程度，以达到关联程度资源之间的推荐。

## 二、业务解决方案
通过Hadoop结合Spark集群对数据进行离线计算（数据离线计算部分），然后持久化到图数据库Neo4j中，通过基于Jersey框架的RESTful的API对外提供以Json为数据格式的访问服务（数据实时服务部分）。 

## 三、技术解决方案
## 1.数据离线计算部分
### 1）概述
将来自图书馆图书数据库、论文数据库的数据输出到文件后，由Spark（整合Hadoop）集群进行计算，然后将计算结果输出到文件中，示意图如下：

![](http://i2.muimg.com/524586/10d34dfa7bb7cd19.png)

### 2）数据预处理
为加快Spark读取数据的速度，我们将数据库中记录保存成7份文件；文件有如下两个部分：
#### a.图书数据文件：
##### 图书ID与作者关联文件（book_id_author.txt）
```
8473#王珏, 周志华, 周傲英
539627#周志华，杨强
126547#苏新宁
307285#苏新宁
143863#孙建军
153824#潘云鹤
1152846#潘云鹤
376852#钱培德
```
 
##### 图书ID与中图法分类号关联文件（book_id_CLCId.txt）
```
8473,TP181
539627,TP181
126547,G252.7
307285,TP311.13
143863,C934
153824,TP391.41
1152846,G254.31
376852,TP39
```
 
##### 中图法分类号与中图法分类名关联文件（cls_no_name.txt）
```
TP181,自动推理、机器学习
G252.7,文献检索
TP311.13,数据库理论与系统
C934,决策学
TP391.41,图像识别及其装置
G254.31,文献著录
TP39,计算机的应用
```
 
#### b.论文数据文件：
##### d.论文ID与论文系统唯一标识关联文件（paper_id_paperId.txt）
```
48368,10284-1016003545.nh
269170,JSGG200101018
322286,Periodical/jsjxb200201001
321446,Periodical/jsjgc200304017
408136,XDTQ200804021
48271,10284-1014451209.nh
332739,QBKX200406022
332945,QBLL200802031
48221,10284-1013190779.nh
125472,80090-1016003258.nh
218375,GJMW200204012
48070,10284-1011126640.nh
48075,10284-1011126665
257875,JEXK199603009
271796,JSJX200002017
57614,10335-2004076111.nh
56280,10335-1011052036
```
 
##### e.论文系统唯一标识与论文作者关联文件（paper_paperId_author.txt）
```
10284-1016003545.nh,周志华
JSGG200101018,周志华
Periodical/jsjxb200201001,周志华
Periodical/jsjgc200304017,周志华
XDTQ200804021,苏新宁
10284-1014451209.nh,苏新宁
QBKX200406022,苏新宁
QBLL200802031,苏新宁
10284-1013190779.nh,孙建军
80090-1016003258.nh,孙建军
GJMW200204012,孙建军
10284-1011126640.nh,孙建军
10284-1011126665,孙建军
JEXK199603009,潘云鹤
JSJX200002017,潘云鹤
10335-2004076111.nh,潘云鹤
10335-1011052036,潘云鹤
```
 
##### f.论文系统唯一标识与论文领域名称关联文件（paper_paperId_field.txt）
```
10284-1016003545.nh,自动化技术
JSGG200101018,自动化技术
XDTQ200804021,图书情报与数字图书馆
10284-1014451209.nh,计算机硬件技术
QBKX200406022,行政学及国家行政管理
QBKX200406022,政治学
QBLL200802031,图书情报与数字图书馆
10284-1013190779.nh,互联网技术
80090-1016003258.nh,计算机软件及计算机应用
GJMW200204012,民商法
10284-1011126640.nh,图书情报与数字图书馆
10284-1011126665,图书情报与数字图书馆
JEXK199603009,自动化技术
JSJX200002017,电信技术
10335-2004076111.nh,计算机软件及计算机应用
10335-1011052036,电信技术
10335-1011052036,互联网技术
```
 
##### g.论文系统唯一标识与论文关键词关联文件（paper_paperId_indexTerm.txt）
```
10284-1016003545.nh,机器学习
10284-1016003545.nh,凸松弛
JSGG200101018,支持向量机
Periodical/jsjxb200201001,神经网络
Periodical/jsjgc200304017,网络存储
XDTQ200804021,现代图书情报技术
XDTQ200804021,期刊评价
10284-1014451209.nh,绿色桌面
QBKX200406022,电子政务
QBLL200802031,隐马尔可夫模型
10284-1011126640.nh,网络引文
10284-1011126640.nh,引文分析
10284-1013190779.nh,中国科技论文在线
GJMW200204012,著作权
GJMW200204012,责任
10284-1011126640.nh,网络引文
10284-1011126665,引文分析
JEXK199603009,分维度量
JSJX200002017,多Agent系统
10335-2004076111.nh,面向对象逆向工程
10335-2004076111.nh,OO软件度量
10335-1011052036,移动互联网
```

### 3）数据离线计算
#### （1）第一阶段
##### 目的：
**图书部分**：建立作者与图书ID、中图法分类名与图书ID两种对应关系；

**论文部分**：建立作者与论文ID、领域名称与论文ID、关键词与论文ID三种对应关系；

##### 实现：
**图书部分**：
加载book_id_author.txt到内存中，将每行数据用“#”分割，形成长度为2的数组（如{“126547”, “苏新宁”}），使用spark的map函数利用刚才的数组生成格式为(图书ID,作者)的二元元组（如(1, “陈世秀”)），代码如下：
```
val delimiter01 = "#"
sc.textFile(book_id_author)
  .map(line => {
    val tokens = line.split(delimiter01)
      .map(_.trim)
    StringUtils.parseBookAuthor(tokens(1)) -> tokens(0).toInt
  })
```
 
整个文件生成一个格式为(作者,图书ID)的二元元组集合，命名为bookAuthorIdRDD，在Spark中称为RDD(Resilient Distributed Dataset，弹性分布式数据集)；

同理，根据book_id_CLCId.txt文件生成格式为(中图法分类号,图书ID)的二元元组集合，命名为bookIdCLCIdTupleList；

根据cls_no_name.txt文件生成格式为(中图法分类号,中图法分类名)的二元元组集合，命名为bookCLCIdCLCNameTupleList；

示意图如下（因篇幅原因，省略图中数据部分，下同）：

![](http://i1.piimg.com/567571/9e881433e8c790a7.png)

作者与图书ID：
在上阶段已完成，即bookAuthorIdRDD
中图法分类名与图书ID：
由bookCLCIdCLCNameTupleList与bookIdCLCIdTupleList做join运算，即生成格式为(中图法分类号,(中图法分类名,图书ID))的二元元组（其中一元为嵌套的元组）的集合，我们将嵌套元组提取出来生成新的元组(map运算)，明明为bookCLCNameIdRDD，代码如图：
```
bookCLCIdCLCNameTupleList
  .join(bookIdCLCIdTupleList, Conf.numTasks)
  .map(tuple => {
    tuple._2
  })
```
示意图如下：

![](http://i1.piimg.com/567571/955f88c9db51baf9.png)

此部分得到如下两个RDD：

![](http://i4.buimg.com/524586/ab23bb6e2c51912b.png)

**论文部分**：

为与图书id区别，论文id在处理过程中作加一亿处理；

与上同理，根据paper_id_paperId.txt文件生成格式为(论文系统唯一标识,论文ID)的二元元组集合，命名为paperIdPaperIDTupleList；

根据paper_paperID_author.txt文件生成格式为(论文系统唯一标识,作者)的二元元组集合，命名为paperAuthorPaperIDTupleList；

根据paper_paperId_field.txt文件生成格式为(论文系统唯一标识,领域名称)的二元元组集合，命名为paperPaperIdFieldTupleList；

根据paper_paperId_indexTerm.txt文件生成格式为(论文系统唯一标识,关键词)的二元元组集合.命名为paperPaperIdIndexTermTupleList；

示意图如下：

![](http://i4.buimg.com/567571/c2015d3bea9385e9.png)

论文部分：
作者与论文ID：
由paperAuthorPaperIDTupleList与paperIdPaperIDTupleList做join运算，即生成格式为(论文系统唯一标识,(作者与论文ID))的二元元组（其中一元为嵌套的元组）的集合，我们将嵌套元组提取出来生成新的元组(map运算)，达到目的，即是paperAuthorIdRDD，代码如图：
```
paperAuthorPaperIDTupleList
  .join(paperIdPaperIDTupleList, Conf.numTasks)
  .map(tuple => {
    tuple._2
  })
```
示意图如下：

![](http://i4.buimg.com/567571/b0f003b3a4baa1a7.png)

领域名称与论文ID：

由paperPaperIdFieldTupleList与paperIdPaperIDTupleList做join运算，即生成格式为(论文系统唯一标识,(领域名称与论文ID))的二元元组（其中一元为嵌套的元组）的集合，我们将嵌套元组提取出来生成新的元组(map运算)，达到目的，即是PaperFieldIdRDD，代码如图：
```
paperPaperIdFieldTupleList
  .join(paperIdPaperIDTupleList, Conf.numTasks)
  .map(tuple => {
    tuple._2
  })
```
示意图如下：

![](http://i4.buimg.com/567571/bf89f676746dfc1d.png)

关键词与论文ID：
由paperPaperIdIndexTermTupleList与paperIdPaperIDTupleList做join运算，即生成格式为(论文系统唯一标识,(关键词与论文ID))的二元元组（其中一元为嵌套的元组）的集合，我们将嵌套元组提取出来生成新的元组(map运算)，达到目的，即是paperIndexTermIdRDD，代码如图：
```
paperPaperIdIndexTermTupleList
  .join(paperIdPaperIDTupleList, Conf.numTasks)
  .map(tuple => {
    tuple._2
  })
```
示意图如下：

![](http://i4.buimg.com/567571/bf89f676746dfc1d.png)

至此，本阶段目的达成，如图：

![](http://i2.muimg.com/567571/53a5acb91b1e6380.png)

至此，第一阶段完成；

#### （2）第二阶段
##### 目的：
* 图书部分：根据权重，图书与图书在作者上的联系、图书与图书在中图法分类号上的联系；

* 论文部分：根据权重，论文与论文在作者上的联系、论文与论文在领域名称上的联系、论文与论文在关键词上的联系；

* 图书与论文部分：根据权重，图书与论文在作者上的联系、图书的中图法分类名与论文的领域名称的联系、图书的中图法分类名与论文的关键词的联系；

##### 实现：

**关联权重**
* 图书与图书在作者上的联系 3
* 图书与图书在中图法分类名上的联系 1
* 论文与论文在作者上的联系 3
* 论文与论文在领域名称上的联系 1
* 论文与论文在关键词上的联系 2
* 图书与论文在作者上的联系 3
* 图书的中图法分类名与论文的领域名称的联系 1
* 图书的中图法分类名与论文的关键词的联系 2
 
**图书部分**：
图书与图书在作者上的联系：
设计权重为3；
通过bookAuthorIdRDD与自身做join和map运算实现（排除自身与自身的联系），得到bookBookRelationshipByAuthorRDD，代码如图：
```
val bookBookRelationshipByAuthorRDD: RDD[Edge[Int]] = bookAuthorIdRDD
  .join(bookAuthorIdRDD, Conf.numTasks)
  .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
  .map(tuple => {
    EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfAuthor)
  })
```
示意图如下：

![](http://i2.muimg.com/567571/96e3ca463e669b98.png)

图书与图书在中图法分类号上的联系：
设计权重为1；
通过bookCLCIdCLCNameTupleList与自身做join和map运算实现（排除自身与自身的联系），得到bookBookRelationshipByCLCIdRDD，代码如图：
```
val bookBookRelationshipByCLCIdRDD: RDD[Edge[Int]] = bookCLCIdCLCNameTupleList
  .join(bookCLCIdCLCNameTupleList, Conf.numTasks)
  .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
  .map(tuple => {
    EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfCLCName)
  })
```
示意图如下：

![](http://i1.piimg.com/567571/d017b9e6cd74310f.png)

论文部分：

论文与论文在作者上的联系：

设计权重为3；
根据paperAuthorIdRDD与自身做join和map运算实现（排除自身与自身的联系），得到paperPaperRelationshipByAuthorRDD，代码如图：
```
val paperPaperRelationshipByAuthorRDD: RDD[Edge[Int]] = paperAuthorIdRDD
  .join(paperAuthorIdRDD, Conf.numTasks)
  .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
  .map(tuple => {
    EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfAuthor)
  })
```
示意图如下：

![](http://i2.muimg.com/567571/aab7f95a89c2b2b1.png)

论文与论文在领域名称上的联系：

设计权重为1；
根据PaperFieldIdRDD与自身做join和map运算实现（排除自身与自身的联系），得到paperPaperRelationshipByFieldRDD，代码如图：
```
val paperPaperRelationshipByFieldRDD: RDD[Edge[Int]] = paperFieldIdRDD
  .join(paperFieldIdRDD, Conf.numTasks)
  .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
  .map(tuple => {
    EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfField)
  })
```
示意图如下：

![](http://i1.piimg.com/524586/f168f42f1e2bdadb.png)

论文与论文在关键词上的联系：

设计权重为2；
根据paperIndexTermIdRDD与自身做join和map运算实现（排除自身与自身的联系），得到paperPaperRelationshipByIndexTermRDD，代码如图：
```
val paperPaperRelationshipByIndexTermRDD: RDD[Edge[Int]] = paperIndexTermIdRDD
  .join(paperIndexTermIdRDD, Conf.numTasks)
  .filter(tuple => !Filter.isDoubleTupleLeftEqualsRight(tuple._2))
  .map(tuple => {
    EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfIndexTerm)
  })
```
示意图如下：

![](http://i4.buimg.com/567571/ce1e4ee405ea104d.png)

图书与论文部分：
图书与论文在作者上的联系：
由paperAuthorIdRDD和bookAuthorIdRDD做join和map，得到
paperBookRelationshipByAuthorRDD，代码如图：
```
val paperBookRelationshipByAuthorRDD: RDD[Edge[Int]] = paperAuthorIdRDD
  .join(bookAuthorIdRDD, Conf.numTasks)
  .map(tuple => {
    EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfAuthor)
  })
```
示意图如下：

![](http://i4.buimg.com/524586/915f83b8a713c3c5.png)

图书的中图法分类名与论文的领域名称的联系：
由paperFieldIdRDD和bookCLCNameIdRDD做join和map，得到
paperBookRelationshipByFieldAndCLCNameRDD，代码如图：
```
val paperBookRelationshipByFieldAndCLCNameRDD: RDD[Edge[Int]] = paperFieldIdRDD
  .join(bookCLCNameIdRDD, Conf.numTasks)
  .map(tuple => {
    EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfCLCName)
  })
```
示意图如下：

![](http://i4.buimg.com/524586/2053efd99e87b0b9.png)

图书的中图法分类名与论文的关键词的联系：
由paperIndexTermIdRDD与bookCLCNameIdRDD做join和map，得到
paperBookRelationshipByIndexTermAndCLCNameRDD，代码如图：
```
val paperBookRelationshipByIndexTermAndCLCNameRDD: RDD[Edge[Int]] = paperIndexTermIdRDD
  .join(bookCLCNameIdRDD, Conf.numTasks)
  .map(tuple => {
    EdgeUtil.SortEdge(tuple._2._1, tuple._2._2, Conf.weightOfIndexTerm)
  })
```
示意图如下：

![](http://i4.buimg.com/524586/f8b9f47f192c86da.png)

至此，本阶段目的达成，如图：

![](http://i4.buimg.com/524586/a2ce7dc61ea312d9.png)


#### （3）第三阶段
##### 目的：
* 将所有图书、论文作为顶点，将上一阶段生成的所有关系作为边，生成一个图；
* 给各种边定义权重，分析指向每个节点的边的权重之和，保存到节点的属性里；

##### 实现：
从文件加载所有图书、论文，返回格式为(节点ID,边权重之和)的节点，其中每个节点的“边权重之和”初始值为0；

返回所有图书节点的RDD，命名为bookVertices；

返回所有论文节点的RDD，命名为paperVertices；代码如下：
```
//图书
sc.textFile(book_id_author).map(line => {
  val tokens = line.replace(",", "")
    .split(delimiter01)
    .map(_.trim)
  (tokens(0).toLong, 0)
})
//论文
sc.textFile(paper_id_paperId).map(line => {
  val tokens = line
    .split(delimiter01)
    .map(_.trim)
  //为与图书id区别，论文id在处理过程中作加一亿处理
  //无属性，属性定为0
  (tokens(0).toLong + Conf.paperIdOffset.toLong, 0)
})
```
将bookVertices和paperVertices聚合，生成全部节点的RDD，命名为vertices；

代码如下：
```
val vertices: RDD[(VertexId, Int)] = bookVertices
  .union(paperVertices)
  .cache()
```
示意图如下：

![](http://i1.piimg.com/524586/258596208f183c72.png)

将第二阶段生成的所有联系RDD聚合，代码如下：
```
val edges: RDD[Edge[Int]] = bookBookRelationshipByAuthorRDD
  .union(bookBookRelationshipByCLCIdRDD)
  .union(paperPaperRelationshipByAuthorRDD)
  .union(paperPaperRelationshipByFieldRDD)
  .union(paperPaperRelationshipByIndexTermRDD)
  .union(paperBookRelationshipByAuthorRDD)
  .union(paperBookRelationshipByFieldAndCLCNameRDD)
  .union(paperBookRelationshipByIndexTermAndCLCNameRDD)
  .cache()
```
示意图如下：

![](http://i1.piimg.com/524586/fa6d3201901c2509s.png)

接下来，利用spark的GraphX库，以vertices、edges生成属性图，其中顶点格式为（ID，指向本节点的权重之和），边的格式为（图书ID，论文ID，边的权重）,生成图的代码为：
```
val graph: Graph[Int, Int] = Graph(vertices, edges)
```
示意图如下：

![](https://1tpic.com/images/2017/04/10/13d1fba.png)

此时图建立完毕，我们接下来对图进行分析工作。

首先合并平行边（出发节点和结束节点相同的边），并且将它们的权值相加（目的是减少边数），代码如下：
```
val mergedEdgesGraph: Graph[Int, Int] = graph
  .groupEdges(merge = (edgeWeight01, edgeWeight02) => edgeWeight01 + edgeWeight02)
  .cache()
```
示意图如下：

![](https://1tpic.com/images/2017/04/10/14.png)

接下来计算指向节点的边的权重之和，并把和赋值给节点，代码如下：
```
val partOfVerticesWithWeight: VertexRDD[Int] = mergedEdgesGraph
  .aggregateMessages[Int](
  triplet => {
    //发送边的权重给目标顶点
    triplet.sendToDst(triplet.attr)
    //发送边的权重给出发顶点
    triplet.sendToSrc(triplet.attr)
  },
  //相加
  (a, b) => a + b
)
//获得含有权重的所有顶点
val verticesWithWeight: RDD[(VertexId, Int)] = vertices
  .leftOuterJoin(partOfVerticesWithWeight).map(tuple => {
  (tuple._1, tuple._2._2.getOrElse(0))
})
```
此阶段数据分析完成，示意图如下：

![](https://1tpic.com/images/2017/04/10/15.png)


#### （4）第四阶段
##### 目的：
* 将图书、论文节点保存到文件；
* 将图书与图书的联系、论文与论文的联系、图书与论文的联系保存到文件；

##### 实现：
从图中获得节点，将所有图书节点转为格式为“ID,权重只和,Book”的字符串RDD，命名为books；

将所有论文节点转为格式为“ID,权重之和,Paper”的字符串RDD，命名为papers；

代码如下：
```
//图书
val books: RDD[String] = verticesWithWeight
  .filter(VertexUtil.GetVertexType(_) == 0)
  .map(VertexUtil.VertexToString(_, 0))
//论文
val papers: RDD[String] = verticesWithWeight
  .filter(VertexUtil.GetVertexType(_) == 1)
  .map(VertexUtil.VertexToString(_, 1))
```
从图中获得边，将图书与图书的联系转为格式为“出发节点ID,目的节点ID,权重,0”的字符串RDD，命名为bookBookRelationships

> 注：
> 1.此处虽提及“出发节点,目的节点”，这是有向边的概念，但是本工程中的边皆为无向边，只是编程实现时用有向边表示；
> 2.第四个属性为此种关系的标识，目的是与其他三种关系区分，下同）；

将论文与论文的联系转为格式为“出发节点ID,目的节点ID,权重,1”的字符串RDD，命名为paperPaperRelationships；

将图书与论文的联系转为格式为“出发节点ID,目的节点ID,权重,2”的字符串RDD，命名为bookPaperRelationships；
```
//图书与图书关联
val bookBookRelationships: RDD[String] = mergedEdgesGraph.edges
  .filter(EdgeUtil.GetEdgeType(_) == 0)
  .map(EdgeUtil.EdgeToString(_, 0))
//论文与论文关联
val paperPaperRelationships: RDD[String] = mergedEdgesGraph.edges
  .filter(EdgeUtil.GetEdgeType(_) == 1)
  .map(EdgeUtil.EdgeToString(_, 1))
//图书与关论文联
val bookPaperRelationships: RDD[String] = mergedEdgesGraph.edges
  .filter(EdgeUtil.GetEdgeType(_) == 2)
  .map(EdgeUtil.EdgeToString(_, 2))
```

保存到文件，代码如下：
```
books.saveAsTextFile(booksResultPath)
papers.saveAsTextFile(papersResultPath)
bookBookRelationships.saveAsTextFile(bookBookRelationshipsResultPath)
paperPaperRelationships.saveAsTextFile(paperPaperRelationshipsResultPath)
bookPaperRelationships.saveAsTextFile(bookPaperRelationshipsResultPath)
```
得到的文件用于导入Neo4j图数据库中，数据离线计算完成。

### 2.数据实时服务部分
#### 1）概述
> 通过以下的api存取图书（book）、论文（paper）等资源，以及完成反馈的动作。

* Jersey：采用JAX-RS标准（Java领域中对REST式的web服务制定的实现标准）的开源框架。此处用来开发对外提供Json数据格式的资源API服务；
* MariaDB：MariaDB数据库管理系统是MySQL的一个分支。此处用来存储图书、论文资源；
* Neo4j：由Neo Technology公司开发的图形数据库管理系统。此处用来存储图书与图书、论文与论文、论文与图书的关联信息；

结构示意图如下

![](https://1tpic.com/images/2017/04/10/17898f1.png)



#### 2）具体实现
##### （1）概述


##### （2）图书与论文
通过监听“books”、“papers”路径，依据请求中的路径（获取资源的类型，papers或者books）和参数（实体编号id）到MariaDB中查询实体（图书、论文），返回请求需要的资源，示意图如下：

![](https://1tpic.com/images/2017/04/10/18.png)

此部分API列表如下：

|功能|资源地址|示例|
| --- | --- | --- |
|添加图书|POST /books|POST /books|
|删除图书|DELETE /books/{id}|DELETE /books/1|
|更新图书|PUT /books/{id}|PUT /books/1|
|获得单个图书实体|GET /books/{id}|GET /books/1|
|获得多个图书实体|GET /books/get?page={page}&size={size}|GET /books/get?page=1&size=10|
|关键词查询图书实体|GET /books/search?keyword={keyword}|GET /books/search?keyword=马克思|
|添加论文|POST /papers|POST /papers|
|删除论文|DELETE /papers/{id}|DELETE /papers/1|
|更新论文|PUT /papers/{id}|PUT /papers/1|
|获得单个论文实体|GET /papers/{id}|GET /papers/1|
|获得多个论文实体|GET /papers/get?page={page}&size={size}|GET /papers/get?page=1&size=10|
|关键词查询论文|GET /papers/search?keyword={keyword}|GET /papers/search?keyword=马克思|


##### （3）推荐
通过监听“recommend”路径，依据请求中的路径（获取资源的类型，papers或者books）和参数（实体编号id，实体类型type），先到Neo4j中查询与该实体编号id有关联的实体的实体编号id列表（依据实体的权重从大到小排序），然后根据列表到MariaDB中查询实体并返回实体列表；

示意图如下：

![](https://1tpic.com/images/2017/04/10/19.png)

此部分API列表如下：

|功能|资源地址|示例|
| --- | --- | --- |
|根据图书ID获取论文推荐|GET /recommend/papers?id={id}&type=book|GET /recommend/papers?id=1&type=book|
|根据论文ID获取论文推荐|GET /recommend/papers?id={id}&type=paper|GET /recommend/papers?id=1&type=paper|
|根据图书ID获取图书推荐|GET /recommend/books?id={id}&type=book|GET /recommend/books?id=1&type=book|
|根据论文ID获取图书推荐|GET /recommend/books?id={id}&type=paper|GET /recommend/books?id=1&type=paper|

##### （4）反馈
通过监听“feedback”路径，依据请求的路径（点赞为like）和参数（实体编号id，实体类型type）增加该实体的权重，示意图如下：

![](https://1tpic.com/images/2017/04/10/20.png)

|功能|资源地址|示例|
| --- | --- | --- |
|根据图书ID点赞图书|GET /feedback/like?id={id}&type=book|GET /feedback/like?id=1&type=book|
|根据论文ID点赞论文|GET /feedback/like?id={id}&type=paper|GET /feedback/like?id=1&type=paper|

## 四、成本分析和可行性分析


## 五、项目实施方案
### 1）硬件
|类别|详情|数量|
| --- | --- | --- |
|服务器|Intel Xeon 6核单路/32G内存/500GB硬盘|3|
|交换机|1000M * 24|1台|

### 2）软件
|类别|详情|版本|
| --- | --- | --- |
|Oracle JDK|开发套件/JVM环境|1.8.0_121|
|Apache Hadoop|集群资源管理|2.7.3|
|Apache Spark|数据计算|2.0.2|
|Scala|Spark编程语言|2.11.8|
|Jersey|RESTful API服务|2.25.1|
|Neo4j Community Edition|图数据库管理系统，存储图书、论文的关联数据|3.1.3|
|MariaDB|关系型数据库管理系统，存储图书、论文数据|10.1.22|