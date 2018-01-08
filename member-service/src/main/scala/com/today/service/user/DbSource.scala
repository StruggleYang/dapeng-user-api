package com.today.service.user

import javax.annotation.Resource
import javax.sql.DataSource

/**
  *  dataSource
  *  多数据源配置多个变量
  */
object  DbSource {
  var dataSource: DataSource = null
}

class DbSource{
  @Resource(name = "crm_dataSource")
  def setDataSource(dataSource: DataSource): Unit = {
    DbSource.dataSource = dataSource
  }
}

