package com.today.api.user.request

/**
         * Autogenerated by Dapeng-Code-Generator (1.2.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated

        *

 分页查询用户接口

        **/
        case class FindUserByPageRequest(

         /**
        *

 用户 id

        **/
        
        pageRequest : com.today.api.page.TPageRequest, /**
        *

  积分数

        **/
        
        integral : Int, /**
        *

  用户类型

        **/
        
        userStatus : com.today.api.user.enums.UserStatusEnum
        )
      