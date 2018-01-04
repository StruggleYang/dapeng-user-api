package com.today.repository.user

import java.util.Date
import javax.annotation.Resource
import javax.sql.DataSource

import com.today.api.user.enums.{IntegralSourceEnum, IntegralTypeEnum, UserStatusEnum}
import com.today.api.user.enums.UserStatusEnum.{findByValue, _}
import com.today.api.user.request.{LoginUserRequest, ModifyUserRequest, RegisterUserRequest}
import com.today.api.user.response.{LoginUserResponse, ModifyUserResponse, RegisterUserResponse}
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
      dataSource.executeUpdate(
        sql"""INSERT INTO user
             (user_name,password,telephone,integral,user_status,is_deleted,created_at,updated_at,created_by,updated_by)
             VALUES (${request.userName},${request.passWord},${request.telephone},0,0,0,${new Date},${new Date},111,111)""")
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
    * 根据用户id查找用户
    *
    * @param userId
    * @return
    */
  def queryUserById(userId: String): Option[Users] = {
    val data = dataSource.row[Users](sql"SELECT * FROM user WHERE id  = ${userId}")
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
      .rows[Users](sql"SELECT * FROM user WHERE user_name  = ${content}")
      .isEmpty
  }

  /**
    * 修改用户账号状态
    *
    * @param status
    */
  def updateUserStatus(status: UserStatusEnum): Unit = {
    dataSource
      .executeUpdate(sql"update user set user_status where id = ${status.id}")
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
      case _ => Some(LoginUserResponse(
        data.get.user_name,
        data.get.telephone,
        findByValue(data.get.user_status),
        data.get.integral,
        data.get.created_at.getTime,
        data.get.updated_at.getTime,
        data.get.email,
        data.get.qq))
    }
  }

  /**
    * 更新用户资料
    *
    * @param request
    * @return
    */
  def updateUserProfile(request: ModifyUserRequest): Option[ModifyUserResponse] = {
    /**
      * 更新
      */
    val data = dataSource.executeUpdate(sql"update user set email = ${request.email} , qq = ${request.qq},updated_at = ${new Date},updated_by = ${request.userId} where id = ${request.userId}")

    if (data != 0) {
      val profile_integral:Int = 5 // 修改资料的积分，暂时写死，应当定义为枚举

      val Int_log = dataSource.executeUpdate(
        sql"UPDATE user SET integral = integral+${profile_integral}, updated_at = ${new Date} ,updated_by = ${request.userId} WHERE  id = ${request.userId} ")
      if (Int_log != 0) {
        // 修改积分及流水,返回修改状态
        val changeStatus:Boolean = ChangeUserIntegral(request.userId,profile_integral,IntegralTypeEnum.ADD,IntegralSourceEnum.PREFECT_INFORMATION)

        val userInfo = queryUserById(request.userId)
        userInfo match {
          case None => None
          case _ => Some(
            ModifyUserResponse(
              userInfo.get.user_name,
              userInfo.get.telephone,
              UserStatusEnum.findByValue(userInfo.get.user_status),
              userInfo.get.updated_at.getTime,
              userInfo.get.email,
              userInfo.get.qq)
          )
        }

      } else {
        throw new SoaException("777", "更新资料失败")
      }
    } else {
      throw new SoaException("777", "更新资料失败")
    }
  }

  /**
    *  增加积分
    * @param userId 用户id
    * @param increment 增加的积分值
    * @param integralType 积分流水类型
    * @param integralSource 积分来源
    */
  def ChangeUserIntegral(
                          userId:String,
                         increment:Int,
                         integralType:IntegralTypeEnum,
                         integralSource:IntegralSourceEnum):Boolean ={
    // 当前的积分
    val curr_Integral = queryUserById(userId).get.integral
    // 插入一条积分流水
    val journal_res = dataSource.executeUpdate(
      sql"""INSERT INTO integral_journal (user_id, integral_type, integral_price, integral_source, integral, created_at, created_by, updated_at, updated_by, remark)
               VALUES (${userId},${integralType.id},${increment},${integralSource.id},
            ${curr_Integral},${new Date},${userId},${new Date},${userId},${integralSource.name}) """
    )
    journal_res!=0
  }
}
