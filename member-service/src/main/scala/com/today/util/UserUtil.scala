package com.today.util

object UserUtil {

  val TEL_PATTERN = """1(([3,5,8]\d{9})|(4[5,7]\d{8})|(7[0,6-8]\d{8}))""".r
  val EMAIL_PATTERN = """(?i)[a-z0-9._-]+@[a-z0-9._-]+(\.[a-z0-9._-]+)+""".r
  val PWD_PATTERN = """[a-zA-Z0-9]""".r

  /**
    * 用户名验证
    *
    * @param name
    * @return
    */
  def checkName(name: String): Boolean = {
    name.length < 16
  }

  /**
    * 验证邮箱
    *
    * @param email
    * @return
    */
  def checkEmail(email: String): Boolean = {
    EMAIL_PATTERN.findAllIn(email).nonEmpty
  }

  /**
    * qq验证
    *
    * @param qq
    * @return
    */
  def checkQQ(qq: String): Boolean = {
    4 <= qq.length && qq.length <= 11
  }

  /**
    * 验证手机
    *
    * @param tel
    * @return
    */
  def checkTel(tel: String): Boolean = {

    TEL_PATTERN.findFirstIn(tel).nonEmpty
  }

  /**
    * 验证密码格式
    *
    * @param pwd
    * @return
    */
  def checkPwd(pwd: String): Boolean = {
    8 <= pwd.length && pwd.length <= 16 && PWD_PATTERN.findAllIn(pwd).nonEmpty
  }
}
