package com.today.api.user

import com.isuwang.dapeng.core.SoaException
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
import com.today.api.user.service.UserService
import com.today.api.user.enums.{IntegralSourceEnum, IntegralTypeEnum, UserStatusEnum}
import com.today.api.user.request._
import com.today.api.user.response._
import com.today.repository.user.UserRepository
import com.today.util.UserUtil
import org.springframework.beans.factory.annotation.Autowired

class UserServiceImp extends UserService {

  @Autowired
  var userRepository: UserRepository = _

  /**
    * 注册
    **/
  override def registerUser(request: RegisterUserRequest): RegisterUserResponse = {


    val checkUse = userRepository.checkUserByName(request.userName)
    val checkName = UserUtil.checkName(request.userName)
    val checkPwd = UserUtil.checkPwd(request.passWord)
    val checkTel = UserUtil.checkPwd(request.telephone)

    if (checkUse && checkName && checkPwd && checkTel) {
      try {
        val sc = userRepository.insertUser(request)
        sc match {
          case None => throw new SoaException("888", "注册失败,服务器错误")
          case _ => sc.get
        }
      } catch {
        case ms: MySQLIntegrityConstraintViolationException => throw new SoaException("777", "手机号已被使用")
      }
    } else {
      throw new SoaException("666", "请求参数有误，检查用户名和密码填写正确")
    }
  }

  /**
    * 登陆
    **/
  override def login(request: LoginUserRequest): LoginUserResponse = {

    val checkPwd = UserUtil.checkPwd(request.passWord)
    val checkTel = UserUtil.checkPwd(request.telephone)

    if (checkTel && checkPwd) {
      val sc = userRepository.queryUserByNameAndPwd(request)
      sc match {
        case None => throw new SoaException("888", "登录失败用户名或密码错误")
        case _ => sc.get
      }
    } else {
      throw new SoaException("888", "登录失败,登录失败用户名或密码格式错误")
    }
  }

  /**
    * 修改资料
    **/
  override def modifyUser(request: ModifyUserRequest): ModifyUserResponse = {

    val checkQQ = UserUtil.checkQQ(request.qq)
    val checkEmail = UserUtil.checkEmail(request.email)

    if (checkQQ && checkEmail) {
      val res = userRepository.updateUserProfile(request)
      // 修改资料的积分，暂时写死，应当定义为枚举
      val profile_integral: Int = 5
      // 修改积分及流水,返回修改状态
      val changeStatus: Boolean = userRepository.changeUserIntegral(request.userId, profile_integral, IntegralTypeEnum.ADD, IntegralSourceEnum.PREFECT_INFORMATION)

      res match {
        case None => throw new SoaException("777", "更新资料失败,未知错误")
        case _ => res.get
      }
    } else {
      throw new SoaException("777", "更新资料失败,邮箱或qq格式有误，请检查")
    }
  }

  /**
    * 冻结用户
    **/
  override def freezeUser(request: FreezeUserRequest): FreezeUserResponse = {

    val statusList: List[Int] = List(UserStatusEnum.BLACK.id, UserStatusEnum.DELETE.id, UserStatusEnum.FREEZED.id)
    val checkStatus = statusList.contains(userRepository.queryUserById(request.userId).get.user_status)

    if (!checkStatus) {
      userRepository.updateUserStatus(request.userId, UserStatusEnum.FREEZED, request.remark)
      val userData = userRepository.queryUserById(request.userId)
      userData match {
        case None => throw new SoaException("777", "此用户不在冻结操作范围内")
        case _ => FreezeUserResponse(userData.get.id.toString, UserStatusEnum.findByValue(userData.get.user_status), userData.get.remark)
      }
    } else {
      throw new SoaException("777", "此用户不在冻结操作范围内")
    }
  }

  /**
    * 拉黑用户
    **/
  override def blackUser(request: BlackUserRequest): BlackUserResponse = {

    val statusList: List[Int] = List(
      UserStatusEnum.BLACK.id,
      UserStatusEnum.DELETE.id,
      UserStatusEnum.FREEZED.id)

    // 检查用户状态
    val checkStatus = statusList.contains(userRepository.queryUserById(request.userId).get.user_status)
    if (!checkStatus) {
      userRepository.updateUserStatus(request.userId, UserStatusEnum.BLACK, request.remark)

      // 修改资料的积分，暂时写死，应当定义为枚举
      val profile_integral: Int = -userRepository.queryUserById(request.userId).get.integral
      // 修改积分及流水,返回修改状态
      val changeStatus: Boolean = userRepository.changeUserIntegral(request.userId, profile_integral, IntegralTypeEnum.MINUS, IntegralSourceEnum.BLACK, request.remark)
      val userData = userRepository.queryUserById(request.userId)
      userData match {
        case None => throw new SoaException("777", "此用户不在拉黑操作范围内")
        case _ => BlackUserResponse(userData.get.id.toString, UserStatusEnum.findByValue(userData.get.user_status), userData.get.remark)
      }
    } else {
      throw new SoaException("777", "此用户不在拉黑操作范围内")
    }
  }

  /**
    *   修改积分及流水,返回修改状态
    **/
  override def changeUserIntegral(request: ChangeIntegralRequest): Int = {

    val changeStatus: Boolean = userRepository.changeUserIntegral(
      request.userId,
      request.integralPrice.toInt,
      request.integralType,
      request.integralSource)

    userRepository.queryUserById(request.userId).get.integral
  }
}
