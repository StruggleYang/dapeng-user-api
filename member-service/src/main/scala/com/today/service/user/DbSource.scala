package com.today.service.user

import javax.annotation.Resource
import javax.sql.DataSource

/**
  * dataSource
  * 可多数据源配置
  */
object DbSource {
  var mysqlData: DataSource = null
}

class DbSource {
  @Resource(name = "crm_dataSource")
  def setDataSource(mysqlData: DataSource): Unit = {
    DbSource.mysqlData = mysqlData
  }
}

