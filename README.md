# data-analysis-of-books-and-papers
> 此服务使用Jersey和Mybatis框架开发，查询MariaDB/Neo4j/Redis，向外部提供json格式的数据服务。

## 一、MariaDB部分
此部分通过和Mybatis编写的抽象层查询MariaDB，对外提供图书、论文的单个或多个的查询服务；

## 二、Neo4j部分
此部分通过使用Mybatis编写的抽象层查询Neo4j，对外提供图书、论文之间的关联查询服务（推荐）；

## 三、Redis部分
此部分通过使用Jedis编写的抽象层查询Redis，对外提供根据标题的图书、论文查询服务；
