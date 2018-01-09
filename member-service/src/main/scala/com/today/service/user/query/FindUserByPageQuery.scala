package com.today.service.user.query

import com.today.api.user.request.FindUserByPageRequest
import com.today.api.user.response.FindUserByPageResponse
import com.today.service.commons.{Assert, Query}
import com.today.service.user.query.sql.UserQuerySql

class FindUserByPageQuery(request: FindUserByPageRequest) extends Query[FindUserByPageResponse] {
  override def preCheck: Unit = {
    Assert.assert(request.pageRequest.start >= 0, "100001", "开始页数不能小于0!")
  }

  override def action: FindUserByPageResponse = {
    UserQuerySql.queryUserByPage(request)
  }
}
