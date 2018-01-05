package com.today.repository.user

import java.util.Date
import javax.annotation.Resource
import javax.sql.DataSource

import com.today.api.user.enums.{IntegralSourceEnum, IntegralTypeEnum, UserStatusEnum}
import com.today.api.user.enums.UserStatusEnum.{findByValue, _}
import com.today.api.user.request.{LoginUserRequest, ModifyUserRequest, RegisterUserRequest}
import com.today.api.user.response.{LoginUserResponse, ModifyUserResponse, RegisterUserResponse}
import wangzx.scala_commons.sql._

import com.isuwang.dapeng.core.SoaException
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import com.today.entity.Users

class UserRepository {
  @Resource(name = "crm_dataSource")
  var dataSource: DataSource = _

  /**
    * 注册用户
    *
    * @param request
    * @return
    */
  def insertUser(request: RegisterUserRequest): Option[RegisterUserResponse] = {
    try {
      dataSource.executeUpdate(
        sql"""INSERT INTO user SET user_name = ${request.userName}, password = ${request.passWord},telephone = ${request.telephone},integral = 0,
             user_status = 0,is_deleted = 0,created_at = now(),updated_at = now(),created_by = 111,updated_by = 111""")
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
  def queryUserByName(content: String): Option[RegisterUserResponse] = {
    val data = dataSource.row[Users](sql"""SELECT * FROM user WHERE user_name  = ${content}""")
    data match {
      case None => None
      case _ => Some(RegisterUserResponse(
        userName = data.get.user_name,
        telephone = data.get.telephone,
        status = findByValue(data.get.user_status),
        createdAt = data.get.created_at.getTime
      ))
    }
  }

  /**
    * 根据用户id查找用户
    * @param userId
    * @return
    */
  def queryUserById(userId: String): Option[Users] = {
    val data = dataSource.row[Users](sql"""SELECT * FROM user WHERE id  = ${userId}""")
    data match {
      case None => None
      case _ => data
    }
  }

  /**
    * 根据用户名查找用户是否存在
    *
    * @param content
    * @return
    */
  def checkUserByName(content: String): Boolean = {
    dataSource
      .rows[Users](sql"""SELECT * FROM user WHERE user_name  = ${content}""")
      .isEmpty
  }

  /**
    * 登录
    *
    * @param request
    * @return
    */
  def queryUserByNameAndPwd(request: LoginUserRequest): Option[LoginUserResponse] = {
    val data = dataSource.row[Users](sql"select *  from user where telephone = ${request.telephone} and password =${request.passWord}")
    data match {
      case None => None
      case Some(x) => Some(BeanBuilder.build[LoginUserResponse](x)(
        "userName" -> x.user_name,
        "status" -> findByValue(x.user_status),
        "createdAt" -> x.created_at.getTime,
        "updatedAt" -> x.updated_at.getTime
      ))
    }
  }

  /**
    * 更新用户资料,完善资料，成为权属会员
    *
    * @param request
    * @return
    */
  def updateUserProfile(request: ModifyUserRequest): Option[ModifyUserResponse] = {

    val data = dataSource.executeUpdate(
      sql"""update user set email = ${request.email} , qq = ${request.qq},user_status = ${UserStatusEnum.DATA_PERFECTED.id},
           updated_at = ${new Date},updated_by = ${request.userId} where id = ${request.userId}""")

    val userInfo = queryUserById(request.userId)
    userInfo match {
      case None => None
      case Some(x) => Some(BeanBuilder.build[ModifyUserResponse](x)(
        "userName" -> x.user_name,
        "status" -> findByValue(x.user_status),
        "updatedAt" -> x.updated_at.getTime
      ))
    }
  }

  /**
    * 改变积分
    *
    * @param userId         用户id
    * @param increment      增加的积分值
    * @param integralType   积分流水类型
    * @param integralSource 积分来源
    * @param mark           可选状态变更备注,如果不写则是状态的描述
    */
  def changeUserIntegral(
                          userId: String,
                          increment: Int,
                          integralType: IntegralTypeEnum,
                          integralSource: IntegralSourceEnum,
                          mark: String*): Boolean = {

    var _mark = integralSource.name
    if (mark.nonEmpty && mark.length == 1) {_mark = mark(0)}
    else {for (m <- mark) _mark += m}
   dataSource.executeUpdate(
      sql"""UPDATE user SET integral = integral+${increment}, updated_at = now(),updated_by = ${userId} WHERE  id = ${userId} """)
    // 当前的积分
    val curr_Integral = queryUserById(userId).get.integral

    // 插入一条积分流水
  dataSource.executeUpdate(
      sql"""INSERT INTO integral_journal SET user_id = ${userId},integral_type = ${integralType.id},integral_price = ${increment},integral_source =${integralSource.id} ,
           integral = ${curr_Integral},created_at = now(),created_by = ${userId},updated_at = now(),updated_by = ${userId},remark = ${_mark}"""
    ) != 0
  }

  /**
    * 更新用户状态
    *
    * @param userId         用户id
    * @param userStatusEnum 用户状态
    * @param mark           可选状态变更备注,如果不写则是状态的描述
    * @return
    */
  def updateUserStatus(userId: String, userStatusEnum: UserStatusEnum, mark: String*): Boolean = {

    var _mark = userStatusEnum.name
    if (mark.nonEmpty && mark.length == 1) {_mark = mark(0)}
    else {for (m <- mark) _mark += m}

    dataSource.executeUpdate(
      sql"""update user set user_status = ${userStatusEnum.id} , remark = ${_mark},updated_by = 111,updated_at = now() where id = ${userId}""") != 0
  }

}
