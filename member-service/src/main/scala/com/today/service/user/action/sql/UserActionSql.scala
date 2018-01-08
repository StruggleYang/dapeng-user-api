package com.today.service.user.action.sql

import java.util.Date

import com.today.api.user.dto.TUser
import com.today.api.user.enums.{IntegralSourceEnum, IntegralTypeEnum, UserStatusEnum}
import com.today.api.user.enums.UserStatusEnum.findByValue
import wangzx.scala_commons.sql._
import com.today.api.user.request.{LoginUserRequest, ModifyUserRequest, RegisterUserRequest}
import com.today.api.user.response.{LoginUserResponse, ModifyUserResponse, RegisterUserResponse}
import com.today.service.user.DbSource
import com.today.service.user.query.sql.UserQuerySql

object UserActionSql {

  /**
    * 插入一条用户数据
    * @param request
    * @return
    */
  def insertUser(request: RegisterUserRequest): Option[RegisterUserResponse] = {

      val userId = DbSource.dataSource.generateKey[Int](
        sql"""INSERT INTO user
                SET user_name = ${request.userName}, password = ${request.passWord},telephone = ${request.telephone},integral = 0,
                user_status = 0,is_deleted = 0,created_at = now(),updated_at = now(),created_by = 111,updated_by = 111""")

      UserQuerySql.queryUserById(userId.toString) match {
        case None => None
        case Some(x) => Some(
          BeanBuilder
            .build[RegisterUserResponse](x)(
            "userName" -> x.userName,
            "status" -> findByValue(x.userStatus.toInt),
            "createdAt" -> x.createdAt
          ))
      }
  }

  /**
    * 根据用户名和密码查询
    *
    * @param request
    * @return
    */
  def queryUserByNameAndPwd(request: LoginUserRequest): Option[LoginUserResponse] = {
    val data = DbSource.dataSource.row[TUser](
      sql"""select * from user
              where telephone = ${request.telephone}
              and password =${request.passWord}""")

    data match {
      case None => None
      case Some(x) => Some(
        BeanBuilder
        .build[LoginUserResponse](x)(
        "userName" -> x.userName,
        "status" -> findByValue(x.userStatus.toInt),
        "email" -> Some(x.email),
        "qq" -> Some(x.qq),
        "createdAt" -> x.createdAt,
        "updatedAt" -> x.updatedAt
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

   DbSource.dataSource.executeUpdate(
      sql"""update
            user set email = ${request.email} , qq = ${request.qq},user_status = ${UserStatusEnum.DATA_PERFECTED.id},
           updated_at = ${new Date},updated_by = ${request.userId} where id = ${request.userId}""")

    val info = UserQuerySql.queryUserById(request.userId.toString)
    info match {
      case None => None
      case Some(x) => Some(
        BeanBuilder
        .build[ModifyUserResponse](x)(
        "userName" -> x.userName,
        "status" -> findByValue(x.userStatus.toInt),
        "updatedAt" -> x.updatedAt,
        "email" -> Some(x.email),
        "qq" -> Some(x.qq)
      ))
    }
  }

  /**
    * 修改用户积分积分
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

    DbSource.dataSource.executeUpdate(
      sql"""UPDATE
           user SET integral = integral+${increment}, updated_at = now(),updated_by = ${userId}
           WHERE  id = ${userId} """)

    val curr_Integral = UserQuerySql.queryUserById(userId).get.integral

    // 插入一条积分流水
    DbSource.dataSource.executeUpdate(
      sql"""INSERT INTO integral_journal
            SET user_id = ${userId},integral_type = ${integralType.id},integral_price = ${increment},integral_source =${integralSource.id} ,
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

    DbSource.dataSource.executeUpdate(
      sql"""update user
            set user_status = ${userStatusEnum.id} , remark = ${_mark},updated_by = 111,updated_at = now()
            where id = ${userId}""") != 0
  }

}
