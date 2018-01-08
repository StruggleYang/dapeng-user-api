## 基础服务编写分层示例图
```
|-- service
|   |-- user                          
|   |   |-- action                            
|   |   |   |-- sql
|   |   |   |   |-- UserActionSql.scala
|   |   |   |-- UserXxxxAction.scala               
|   |   |   |-- ....
|   |   |-- query
|   |   |   |-- sql
|   |   |   |   |-- UserQuerySql.scala
|   |   |   |-- FindUserXxxxQuery.scala               
|   |   |   |-- ....
|   |   |-- UserServiceImpl.scala      
-------------------------------------------------------
```