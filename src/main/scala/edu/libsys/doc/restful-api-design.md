# RESTful API设计
>通过以下的api存取图书（book）、论文（paper）、收藏（collection）、行为（behavior）等资源。

## 1.图书部分
|功能|资源地址|示例|
| --- | --- | --- |
|添加|POST /books|POST /books|
|删除|DELETE /books/{id}|DELETE /books/1|
|更新|PUT /books/{id}|PUT /books/1|
|获得单个|GET /books/{id}|GET /books/1|
|获得列表|GET /books?page={page}&size={size}|GET /books?page=1&size=10|
|关键词查询|GET /books?keyword={keyword}&size={size}|GET /books?keyword=马克思&size=10|
|根据论文ID获取推荐|GET /books?paper-id={paper-id}&size={size}|GET /books?paper-id=10001-1011132221.nh&size=10|

## 2.论文部分
|功能|资源地址|示例|
| --- | --- | --- |
|添加|POST /papers|POST /papers|
|删除|DELETE /papers/{id}|DELETE /papers/1|
|更新|PUT /papers/{id}|PUT /papers/1|
|获得单个|GET /papers/{id}|GET /papers/1|
|获得列表|GET /papers?page={page}&size={size}|GET /papers?page=1&size=10|
|关键词查询|GET /papers?keyword={keyword}&size={size}|GET /papers?keyword=马克思&size=10|
|根据图书ID获取推荐|GET /papers?book-id={book-id}&size={size}|GET /papers?book-id=904190&size=10|

## 3.收藏部分
|功能|资源地址|示例|
| --- | --- | --- |
|添加|PUT /collections?user-id={user-id}|PUT /collections/1|
|删除|DELETE /collections?user-id={user-id}&collection-id={collection-id}|DELETE /collections?user-id=1&collection-id=|

## 4.行为部分
|功能|资源地址|示例|
| --- | --- | --- |
|反馈|POST /behaviors|POST /behaviors|