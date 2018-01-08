package com.today.service.user.action

import java.util.Date

import com.today.api.user.enums.UserStatusEnum
import com.today.api.user.request.RegisterUserRequest
import com.today.api.user.response.RegisterUserResponse
import com.today.service.commons._

class RegisterUserAction(request: RegisterUserRequest) extends Action[RegisterUserResponse]{
  override def preCheck: Unit = {
    Assert.assert(request.userName.length > 0, "100001" , "用户名不容许为空")
  }

  override def action: RegisterUserResponse = {
    RegisterUserResponse(userName = "", telephone = "", status = UserStatusEnum.ACTIVATED, createdAt = new Date().getTime)
  }
}
