package com.today.service.user

import com.today.api.user.request._
import com.today.api.user.response._
import com.today.api.user.service.UserService
import com.today.service.user.action._
import com.today.service.user.action.sql.UserActionSql
import com.today.service.user.query.FindUserByPageQuery
import com.today.service.user.query.sql.UserQuerySql

class UserServiceImpl extends UserService {
  override def registerUser(request: RegisterUserRequest): RegisterUserResponse = {
    new RegisterUserAction(request).execute
  }

  override def login(request: LoginUserRequest): LoginUserResponse = {
    new UserLoginAction(request).execute
  }

  override def modifyUser(request: ModifyUserRequest): ModifyUserResponse = {
    new UserModifyAction(request).execute
  }

  override def freezeUser(request: FreezeUserRequest): FreezeUserResponse = {
    new UserFreezeAction(request).execute
  }

  override def blackUser(request: BlackUserRequest): BlackUserResponse = {
    new UserBlackAction(request).execute
  }

  override def changeUserIntegral(request: ChangeIntegralRequest): Int = {
    UserActionSql.changeUserIntegral(
      request.userId.toString,
      request.integralPrice,
      request.integralType,
      request.integralSource)

    UserQuerySql.queryUserById(request.userId.toString).get.integral
  }

  // TODO 分页查询
  override def findUserByPage(request: FindUserByPageRequest): FindUserByPageResponse = {
    new FindUserByPageQuery(request).execute
  }
}
