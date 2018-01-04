package com.today.test

import com.today.api.user.UserServiceImp
import com.today.api.user.request.{LoginUserRequest, RegisterUserRequest}
import com.today.api.user.service.UserService
import org.springframework.context.support.ClassPathXmlApplicationContext

import scala.io.StdIn

/**
  * author: HuaZhe Ray
  *
  * describe: TODO
  *
  * createDate: 2018/1/3
  * createTime: 9:36
  *
  */
object Test {

  def main(args: Array[String]): Unit = {



    val resource = classOf[UserServiceImp].getClassLoader.getResource("META-INF/spring/services.xml")

    val context = new ClassPathXmlApplicationContext(resource.toString)

    context.start();

    val service:UserService = classOf[UserService].cast(context.getBean("userService"))
    // 注册
    /*registerUser(service)*/
    //val res = service.registerUser(RegisterUserRequest("张三","a12345678","1562345679"))
    // 登陆
    val res = service.login(LoginUserRequest("a12345678","1562345679"))
    msg(res)

  }

  def msg(res:Any):Unit = {
    println("=============================[响应消息]=====================================")
    println("===>"+res)
    println("==================================================================end")
  }

/*  def  registerUser(service:UserService) = {
    println("==========================注册")
    println("========用户名:")
    val name = StdIn.readLine()
    println("========密码:")
    val pwd = StdIn.readLine()
    println("========用户名:")
    val tel = StdIn.readLine()
    val res = service.registerUser(RegisterUserRequest(name,pwd,tel))
  }*/

}
