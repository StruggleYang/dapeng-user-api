package com.today.service.user.action

import com.isuwang.dapeng.core.SoaException
import com.today.api.user.enums.{IntegralSourceEnum, IntegralTypeEnum, UserStatusEnum}
import com.today.api.user.request.BlackUserRequest
import com.today.api.user.response.{BlackUserResponse, FreezeUserResponse}
import com.today.service.commons.`implicit`.Implicits
import com.today.service.commons.{Action, Assert}
import com.today.service.user.action.sql.UserActionSql
import com.today.service.user.query.sql.UserQuerySql

/**
  * 拉黑账户
  * @param request
  */
class UserBlackAction(request: BlackUserRequest) extends Action[BlackUserResponse] {
  val statusList: List[Int] = List(
    UserStatusEnum.BLACK.id,
    UserStatusEnum.DELETE.id,
    UserStatusEnum.FREEZED.id)

  override def preCheck: Unit = {

    Assert.assert(
      Implicits.CommonEx(
        UserQuerySql
          .queryUserById(request.userId.toString)
          .get
          .userStatus
          .toInt)
        .notIn(statusList), "100001", "冻结失败，此用户不在拉黑操作范围内!")
  }

  override def action: BlackUserResponse = {
    UserActionSql
      .updateUserStatus(request.userId.toString, UserStatusEnum.BLACK, request.remark)

    // 清空积分，会减去当前用户的所有积分
    val profile_integral: Int = -UserQuerySql.queryUserById(request.userId.toString).get.integral
    val _: Boolean = UserActionSql.changeUserIntegral(request.userId.toString, profile_integral, IntegralTypeEnum.MINUS, IntegralSourceEnum.BLACK, request.remark)

    val userInfo = UserQuerySql.queryUserById(request.userId.toString)
    userInfo match {
      case None => throw new SoaException("100001", "冻结失败")
      case _ => BlackUserResponse(userInfo.get.id.toString, UserStatusEnum.findByValue(userInfo.get.userStatus.toInt), userInfo.get.remark)
    }
  }
}
