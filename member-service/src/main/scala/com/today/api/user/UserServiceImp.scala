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
    *
    * *
    * ### 用户注册
    * *
    * #### 业务描述
    * 用户注册账户，用户密码需要加盐之后存储(加盐方案还么确定,小伙伴可以自己随意设计个简单的加解密方案)
    * *
    * #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    * *
    * #### 输入
    *1.user_request.RegisterUserRequest
    * *
    * #### 前置检查
    *1. 手机号码规则验证
    *2. 手机号未被使用验证
    *3. 密码规则,字母数字八位混合
    * *
    * ####  逻辑处理
    *1.密码加盐处理
    *2.新增一条user记录
    *3.返回结果 user_response.RegisterUserResponse
    * *
    * #### 数据库变更
    *1. insert into user() values()
    * *
    * ####  事务处理
    * 无
    * *
    * ####  输出
    *1.user_response.RegisterUserResponse
    *
    **/
  override def registerUser(request: RegisterUserRequest): RegisterUserResponse = {

    val checkUse = userRepository.checkUserByName(request.userName)
    val checkName = UserUtil.checkName(request.userName)
    val checkPwd = UserUtil.checkPwd(request.passWord)
    val checkTel = UserUtil.checkPwd(request.telephone)

    if (checkUse && checkName && checkPwd && checkTel) {
      try {
        val sc = userRepository.registerUser(request)
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
    *
    * *
    * ### 用户登录
    * *
    * #### 业务描述
    * 用户登录
    * *
    * #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    * *
    * #### 输入
    *1.user_request.LoginUserRequest
    * *
    * #### 前置检查
    *1.手机号码规则验证
    *2.密码规则,字母数字八位混合
    * *
    * ####  逻辑处理
    *1. 根据手机号码和密码查询用户记录
    *2. 异常用户状态的用户登录返回 Exception
    * *
    * #### 数据库变更
    *1. select *  from user where telphone = ? and password = ?
    * *
    * ####  事务处理
    * 无
    * *
    * ####  输出
    *1.user_response.LoginUserResponse
    *
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
    *
    * *
    * ### 用户修改个人资料
    * *
    * #### 业务描述
    * 用户再注册之后完善个人资料,完善资料增加积分5
    * *
    * #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    * *
    * #### 输入
    *1.user_request.ModifyUserRequest
    * *
    * #### 前置检查
    *1. 邮箱规则验证
    *2. qq 规则验证
    *3. 用户状态判断只有用户状态为
    * *
    * ####  逻辑处理
    *1. 根据输入的参数计算用户积分
    *2. 修改用户 email qq
    *2. 修改完成之后调用积分action增加用户积分(完善资料增加积分5) ChangeUserIntegralAction
    * *
    * #### 数据库变更
    *1. update user set email = ? , qq = ? where id = ${userId}
    * *
    * ####  事务处理
    *1. 无
    * *
    * ####  输出
    *1.user_response.ModifyUserAction
    *
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
    *
    * *
    * ### 冻结用户接口
    * *
    * #### 业务描述
    * 用户因为触犯一些游戏规则,后台自检程序或者管理员会冻结该用户
    * *
    * #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    * *
    * #### 输入
    *1.user_request.FreezeUserRequest
    * *
    * #### 前置检查
    *1.用户状态检查(已冻结,已拉黑,已逻辑删除的用户不能冻结)
    * *
    * ####  逻辑处理
    *1. 设置用户状态为 FREEZE
    * *
    * #### 数据库变更
    *1. update user set status = ? , remark = ? where id = ${userId}
    * *
    * ####  事务处理
    *1. 无
    * *
    * ####  输出
    *1.user_response.FreezeUserResponse
    *
    **/
  override def freezeUser(request: FreezeUserRequest): FreezeUserResponse = {

    val statusList: List[Int] = List(UserStatusEnum.BLACK.id, UserStatusEnum.DELETE.id, UserStatusEnum.FREEZED.id)
    val checkStatus = statusList.contains(userRepository.queryUserById(request.userId).get.user_status)

    if (!checkStatus) {
      userRepository.updateUserStaus(request.userId, UserStatusEnum.FREEZED, request.remark)
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
    *
    * *
    * ### 拉黑用户接口
    * *
    * #### 业务描述
    * 用户因为触犯一些游戏规则,后台自检程序或者管理员会拉黑该用户,拉黑用户把用户的积分置为0
    * *
    * #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    * *
    * #### 输入
    *1.user_request.BlackUserRequest
    * *
    * #### 前置检查
    *1.用户状态检查(已冻结,已拉黑,已逻辑删除的用户不能拉黑)
    * *
    * ####  逻辑处理
    *1. 设置用户状态为  BLACK
    *2. 调用积分修改接口 ChangeUserIntegralAction
    * *
    * #### 数据库变更
    *1. update user set status = ? , remark = ? where id = ${userId}
    * *
    * ####  事务处理
    *1. 无
    * *
    * ####  输出
    *1.user_response.BlackUserResponse
    *
    **/
  override def blackUser(request: BlackUserRequest): BlackUserResponse = {

    val statusList: List[Int] = List(
      UserStatusEnum.BLACK.id,
      UserStatusEnum.DELETE.id,
      UserStatusEnum.FREEZED.id)

    // 检查用户状态
    val checkStatus = statusList.contains(userRepository.queryUserById(request.userId).get.user_status)
    if (!checkStatus) {
      userRepository.updateUserStaus(request.userId, UserStatusEnum.BLACK, request.remark)

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
    *
    * *
    * ### 记录积分改变流水
    * *
    * #### 业务描述
    * 用户因为完成一些游戏规则或者触犯游戏规则导致积分减少或者增加,调用该接口修改用户积分
    * *
    * #### 接口依赖
    * 无
    * #### 边界异常说明
    * 无
    * *
    * #### 输入
    *1.user_request.ChangeIntegralRequest
    * *
    * #### 前置检查
    *1.用户状态检查(已冻结,已拉黑,已逻辑删除的用户不能冻结)
    * *
    * ####  逻辑处理
    *1. 设置用户状态为 FREEZE
    * *
    * #### 数据库变更
    *1. update user set integral = ?  where id = ${userId}
    *2. insert into integral_journal() values()
    * *
    * ####  事务处理
    *1. 无
    * *
    * ####  输出
    *1. i32 流水 Id
    *
    **/
  override def changeUserIntegral(request: ChangeIntegralRequest): Int = {
    // 修改积分及流水,返回修改状态
    val changeStatus: Boolean = userRepository.changeUserIntegral(
      request.userId,
      UserUtil.strToInt(request.integralPrice),
      request.integralType,
      request.integralSource)

    userRepository.queryUserById(request.userId).get.integral
  }
}
