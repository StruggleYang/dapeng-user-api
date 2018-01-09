package com.today.service.user.action

import com.isuwang.dapeng.core.SoaException
import com.today.api.user.enums.{IntegralSourceEnum, IntegralTypeEnum}
import com.today.api.user.request.ModifyUserRequest
import com.today.api.user.response.ModifyUserResponse
import com.today.service.commons.{Action, Assert}
import com.today.service.user.action.sql.UserActionSql
import com.today.util.user.UserUtil

/**
  * 修改账户资料
  * @param request
  */
class UserModifyAction(request: ModifyUserRequest) extends Action[ModifyUserResponse] {
  override def preCheck: Unit = {
    Assert.assert(UserUtil.checkEmail(request.email), "100001", "邮箱格式有误!")
    Assert.assert(UserUtil.checkQQ(request.qq), "100001", "QQ格式有误!")
  }

  override def action: ModifyUserResponse = {
    UserActionSql.updateUserProfile(request) match {
      case None => throw new SoaException("888", "资料修改失败!")
      case Some(x) => {
        val profile_integral: Int = 5
        val _: Boolean = UserActionSql.changeUserIntegral(request.userId.toString, profile_integral,
          IntegralTypeEnum.ADD, IntegralSourceEnum.PREFECT_INFORMATION)
        x
      }
    }
  }
}
