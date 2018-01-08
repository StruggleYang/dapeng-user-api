package com.today.service.user.query

import com.today.api.user.request.FindUserByPageRequest
import com.today.api.user.response.FindUserByPageResponse
import com.today.service.commons.Query

class FindUserByPageQuery(request: FindUserByPageRequest) extends Query[FindUserByPageResponse]{
  override def preCheck: Unit = {

  }

  override def action: FindUserByPageResponse = {
    null
  }
}
