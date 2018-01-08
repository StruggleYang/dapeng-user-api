package com.today.service.user

import com.today.api.user.request._
import com.today.api.user.response._
import com.today.api.user.service.UserService

class UserServiceImpl extends UserService{
  override def registerUser(request: RegisterUserRequest): RegisterUserResponse = ???

  override def login(request: LoginUserRequest): LoginUserResponse = ???

  override def modifyUser(request: ModifyUserRequest): ModifyUserResponse = ???

  override def freezeUser(request: FreezeUserRequest): FreezeUserResponse = ???

  override def blackUser(request: BlackUserRequest): BlackUserResponse = ???

  override def changeUserIntegral(request: ChangeIntegralRequest): Int = ???

  override def findUserByPage(request: FindUserByPageRequest): FindUserByPageResponse = ???
}
