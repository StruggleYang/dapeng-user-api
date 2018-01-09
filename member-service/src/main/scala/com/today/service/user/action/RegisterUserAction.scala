package com.today.service.user.action

import com.isuwang.dapeng.core.SoaException
import com.today.api.user.request.RegisterUserRequest
import com.today.api.user.response.RegisterUserResponse
import com.today.service.commons._
import com.today.service.user.action.sql.UserActionSql
import com.today.service.user.query.sql.UserQuerySql
import com.today.util.user.UserUtil

/**
  * 注册账户
  *
  * @param request
  */
class RegisterUserAction(request: RegisterUserRequest) extends Action[RegisterUserResponse] {

  override def preCheck: Unit = {
    Assert.assert(UserQuerySql.queryUserByTel(request.telephone).isEmpty, "100001", "手机号已被使用请检查!")
    Assert.assert(UserUtil.checkName(request.userName), "100001", "用户名格式有误请检查!")
    Assert.assert(UserUtil.checkPwd(request.passWord), "100001", "密码格式有误请检查!")
    Assert.assert(UserUtil.checkTel(request.telephone), "100001", "手机号格式有误请检查!")
  }

  override def action: RegisterUserResponse = {
    UserActionSql.insertUser(request) match {
      case None => throw new SoaException("888", "注册失败,服务器错误")
      case Some(x) => x
    }
  }
}
