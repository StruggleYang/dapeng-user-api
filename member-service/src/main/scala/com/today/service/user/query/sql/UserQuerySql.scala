package com.today.service.user.query.sql

import com.today.api.page.{TPageRequest, TPageResponse}
import com.today.api.user.dto.TUser
import com.today.api.user.request.FindUserByPageRequest
import com.today.api.user.response.FindUserByPageResponse
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

  /**
    * 分页查找用户
    * @return
    */
  def queryUserByPage(request: FindUserByPageRequest): FindUserByPageResponse = {
    val query = s"SELECT * FROM user "
    val limit = s" LIMIT ${request.pageRequest.start} , ${request.pageRequest.limit} "

    val querySql = request.pageRequest.sortFields match {
      case None => query +limit
      case Some(x) => query+s" ORDER BY ${x.toString} "+limit
    }

    val data = DbSource.dataSource.rows[TUser](querySql)
    data match {
      case Nil => BeanBuilder
        .build[FindUserByPageResponse](request.pageRequest)(
        "pageResponse" -> TPageResponse(request.pageRequest.start,request.pageRequest.limit,data.length),
        "userList" -> Nil
      )
      case x => BeanBuilder
        .build[FindUserByPageResponse](request.pageRequest)(
        "pageResponse" -> TPageResponse(request.pageRequest.start,request.pageRequest.limit,data.length),
        "userList" -> x
      )
    }
  }
}
