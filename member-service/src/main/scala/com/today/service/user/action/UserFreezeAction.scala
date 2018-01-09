package com.today.service.user.action

import com.isuwang.dapeng.core.SoaException
import com.today.api.user.enums.UserStatusEnum
import com.today.api.user.request.FreezeUserRequest
import com.today.api.user.response.FreezeUserResponse
import com.today.service.commons.`implicit`.Implicits
import com.today.service.commons.{Action, Assert}
import com.today.service.user.action.sql.UserActionSql
import com.today.service.user.query.sql.UserQuerySql

/**
  * 冻结账户
  * @param request
  */
class UserFreezeAction(request: FreezeUserRequest) extends Action[FreezeUserResponse] {

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
        .notIn(statusList), "100001", "冻结失败，此用户不在冻结操作范围内!")
  }

  override def action: FreezeUserResponse = {
    UserActionSql
      .updateUserStatus(request.userId.toString, UserStatusEnum.FREEZED, request.remark)
    val userInfo = UserQuerySql.queryUserById(request.userId.toString)
    userInfo match {
      case None => throw new SoaException("100001", "冻结失败")
      case _ => FreezeUserResponse(userInfo.get.id.toString, UserStatusEnum.findByValue(userInfo.get.userStatus.toInt), userInfo.get.remark)
    }
  }
}
