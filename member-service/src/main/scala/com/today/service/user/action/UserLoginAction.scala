package com.today.service.user.action

import com.isuwang.dapeng.core.SoaException
import com.today.api.user.request.LoginUserRequest
import com.today.api.user.response.LoginUserResponse
import com.today.service.commons.{Action, Assert}
import com.today.service.user.action.sql.UserActionSql
import com.today.util.user.UserUtil

class UserLoginAction(request:LoginUserRequest) extends Action[LoginUserResponse]{
  override def preCheck: Unit = {
    Assert.assert(UserUtil.checkTel(request.telephone), "100001" , "账号格式有误!")
    Assert.assert(UserUtil.checkPwd(request.passWord), "100001" , "密码格式有误!")
  }

  override def action: LoginUserResponse = {
    UserActionSql.queryUserByNameAndPwd(request) match {
      case None => throw new SoaException("888", "登陆失败,服务器错误")
      case Some(x) => x
    }
  }
}
