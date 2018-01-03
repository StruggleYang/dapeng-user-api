package com.today.test

import com.today.api.user.UserServiceImp
import com.today.api.user.request.{LoginUserRequest, RegisterUserRequest}
import com.today.api.user.service.UserService
import org.springframework.context.support.ClassPathXmlApplicationContext

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

    val res = service.login(LoginUserRequest("A12345678","15750174634"))

    println(res)



  }

}
