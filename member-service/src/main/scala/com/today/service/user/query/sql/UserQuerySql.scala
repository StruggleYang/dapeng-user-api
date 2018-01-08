package com.today.service.user.query.sql

import com.today.api.user.dto.TUser
import com.today.service.user.DbSource
import wangzx.scala_commons.sql._

object UserQuerySql {

  /**
    * 根据用户id查找用户
    * @param userId
    * @return
    */
  def queryUserById(userId: String): Option[TUser] = {
    val data = DbSource.dataSource.row[TUser](sql"""SELECT * FROM user WHERE id  = ${userId}""")
    data match {
      case None => None
      case _ => data
    }
  }


  /**
    * 根据手机号查找用户
    * @param tel
    * @return
    */
  def queryUserByTel(tel: String): Option[TUser] = {
    val data = DbSource.dataSource.row[TUser](sql"""SELECT * FROM user WHERE telephone = ${tel}""")
    data match {
      case None => None
      case _ => data
    }
  }
}
