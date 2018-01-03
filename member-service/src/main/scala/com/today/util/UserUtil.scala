package com.today.util

object UserUtil {

  /**
    * 用户名验证
    */
  def checkName(name:String): Boolean ={
    name.length<16
  }

  /**
    * 验证邮箱
    */
  def checkEmail(email:String): Boolean ={
    val EMAIL_PATTERN = """(?i)[a-z0-9._-]+@[a-z0-9._-]+(\.[a-z0-9._-]+)+""".r
    EMAIL_PATTERN.findAllIn(email).nonEmpty
  }
  /**
    * 验证手机
    */
  def checkTel(tel:String): Boolean ={
    val TEL_PATTERN = """1(([3,5,8]\d{9})|(4[5,7]\d{8})|(7[0,6-8]\d{8}))""".r
    TEL_PATTERN.findFirstIn(tel).nonEmpty
  }

  /**
    * 验证密码格式
    */
  def checkPwd(pwd:String): Boolean ={
    val PWD_PATTERN = """[a-zA-Z0-9]""".r
    8<=pwd.length&&pwd.length<=16&&PWD_PATTERN.findAllIn(pwd).nonEmpty
  }
}
