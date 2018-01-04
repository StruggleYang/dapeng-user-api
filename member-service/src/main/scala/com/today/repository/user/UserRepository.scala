package com.today.repository.user

import java.util.Date
import javax.annotation.Resource
import javax.sql.DataSource

import com.today.api.user.enums.UserStatusEnum
import com.today.api.user.enums.UserStatusEnum.{findByValue, _}
import com.today.api.user.request.{LoginUserRequest, RegisterUserRequest}
import com.today.api.user.response.{LoginUserResponse, RegisterUserResponse}
import wangzx.scala_commons.sql._
import java.text.SimpleDateFormat

import com.isuwang.dapeng.core.SoaException
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import com.today.entity.Users
import org.springframework.stereotype.Component

@Component
class UserRepository {
  @Resource(name = "crm_dataSource")
  var dataSource: DataSource = _

  val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  /**
    * 注册用户
    *
    * @param request
    * @return
    */
  def registerUser(request: RegisterUserRequest): Option[RegisterUserResponse] = {
    try {
      dataSource.executeUpdate(sql"INSERT INTO user (user_name,password,telephone,integral,user_status,is_deleted,created_at,updated_at,created_by,updated_by) VALUES (${request.userName},${request.passWord},${request.telephone},0,0,0,${new Date},${new Date},000,000)")
        queryUserByName(request.userName)
    } catch {
      case _: MySQLIntegrityConstraintViolationException => throw new SoaException("666", "手机号已被使用")
    }
  }

  /**
    * 根据用户名查找用户
    *
    * @param content
    * @return
    */
  def queryUserByName(content: String):Option[RegisterUserResponse] = {
    val data = dataSource.row[Users](sql"SELECT * FROM user WHERE user_name  = ${content}")
    data match {
      case None => None
      case _ => Some(RegisterUserResponse(
        data.get.user_name,
        data.get.telephone,
        findByValue(data.get.user_status),
        data.get.created_at.getTime))
    }
  }

  /**
    * 根据用户名查找用户是否存在
    *
    * @param content
    * @return
    */
  def checkUserByName(content: String):Boolean= {
   dataSource
     .rows[Users](sql"SELECT * FROM user WHERE user_name  = ${content}")
     .isEmpty
  }

  /**
    * 修改用户账号状态
    */
  def updateUserStatus(status:UserStatusEnum): Unit ={
    dataSource
      .executeUpdate(sql"update user set user_status where id = ${status.id}")
  }

  /**
    *
    * 登录
    */
  def queryUserByNameAndPwd(request: LoginUserRequest):Option[LoginUserResponse] ={
    val res = dataSource.row[Users](sql"select *  from user where telephone = ${request.telephone} and password =${request.passWord}")
    res match {
      case None => None
      case _ => Some(LoginUserResponse(
      res.get.user_name,
      res.get.telephone,
      findByValue(res.get.user_status),
      res.get.integral,
      res.get.created_at.getTime,
      res.get.updated_at.getTime,
      res.get.email,
      res.get.qq))
    }
  }
}
