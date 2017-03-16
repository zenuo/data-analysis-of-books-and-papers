# RESTful API设计
>通过对图书（book）、论文（paper）、收藏（collection）、点击等资源与行为的分析进行推荐。

## 1.图书部分
|功能|资源地址|示例|
| --- | --- | --- |
|添加|POST /books|POST /books|
|删除|DELETE /books/{id}|DELETE /books/1|
|更新|PUT /books/{id}|PUT /books/1|
|获得列表|GET /books?page={page}&size={size}|GET /books?page=1&size=10|
|关键词查询|GET /books?keyword={keyword}&size={size}|GET /books?keyword=马克思&size=10|
|根据作者名获取推荐|GET /books?author={author}&size={size}|GET /books?author=张明&size=10|
|根据论文ID获取推荐|GET /books?paper-id={paper-id}&size={size}|GET /books?paper-id=&size=10|
