## 基础服务编写分层示例图
```
|-- service
|   |-- user                          
|   |   |-- action                                     事件
|   |   |   |-- sql
|   |   |   |   |-- UserActionSql.scala
|   |   |   |-- UserXxxxAction.scala               
|   |   |   |-- ....
|   |   |-- query                                      查询
|   |   |   |-- sql
|   |   |   |   |-- UserQuerySql.scala
|   |   |   |-- FindUserXxxxQuery.scala               
|   |   |   |-- ....
|   |   |-- UserServiceImpl.scala      
-------------------------------------------------------
```

 ## 基础约束
  * 服务主流程尽量简单、清晰，没有复杂的分支流程
  * 一个方法体不能超过90行，每行长度不超过120列
  * 必须规范化代码后才能提交，包括format以及优化import
  * 避免使用 var 及 mutable collections(如果一定要用可变量,那么必须申请review)
  * 新的EmptyChecking，统一对逻辑Empty的检查。
  * 服务代码中避免 null 的使用，对基本类型，使用Option[T] 替代，对集合类型，使用 List.Empty 或者类似集合替代
  * Option[T] 可以直接传递给 scala-sql 包。
  * Enum 类型可以直接传递给 scala-sql 包。
  * 新的明确语义的 Bean Copy 函数，完成对象间的转换。明确，无歧义。
  * 链式调用分行处理。