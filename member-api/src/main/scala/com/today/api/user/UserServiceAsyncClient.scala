package com.today.api.user

import com.isuwang.dapeng.core._
        import com.isuwang.org.apache.thrift._
        import com.isuwang.dapeng.remoting.BaseCommonServiceClient
        import com.today.api.user.UserServiceCodec._
        import scala.concurrent.{Future, Promise}
        import java.util.function.BiConsumer

        /**
         * Autogenerated by Dapeng-Code-Generator (1.2.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated

        **/
        object UserServiceAsyncClient extends BaseCommonServiceClient("com.today.api.user.service.UserService", "1.0.0"){

        override def isSoaTransactionalProcess: Boolean = {

          var isSoaTransactionalProcess = false
          
          isSoaTransactionalProcess
        }

        
            /**
            * 

### 用户注册

#### 业务描述
    用户注册账户，用户密码需要加盐之后存储(加盐方案还么确定,小伙伴可以自己随意设计个简单的加解密方案)

#### 接口依赖
    无
#### 边界异常说明
    无

#### 输入
    1.user_request.RegisterUserRequest

#### 前置检查
    1. 手机号码规则验证
    2. 手机号未被使用验证
    3. 密码规则,字母数字八位混合

####  逻辑处理
    1.密码加盐处理
    2.新增一条user记录
    3.返回结果 user_response.RegisterUserResponse

#### 数据库变更
    1. insert into user() values()

####  事务处理
    无

####  输出
    1.user_response.RegisterUserResponse

            **/
            def registerUser(request:com.today.api.user.request.RegisterUserRequest , timeout: Long) : scala.concurrent.Future[com.today.api.user.response.RegisterUserResponse] = {

            initContext("registerUser");

            try {
              val _responseFuture = sendBaseAsync(registerUser_args(request), new RegisterUser_argsSerializer(), new RegisterUser_resultSerializer(), timeout).asInstanceOf[java.util.concurrent.CompletableFuture[registerUser_result]]

              val promise = Promise[com.today.api.user.response.RegisterUserResponse]()

              _responseFuture.whenComplete(new BiConsumer[registerUser_result, Throwable]{

              override def accept(r: registerUser_result, e: Throwable): Unit = {
                if(e != null)
                  promise.failure(e)
                else
                  promise.success(r.success)
                }
              })
              promise.future
            }catch{
              case e: SoaException => throw e
              case e: TException => throw new SoaException(e)
            }finally {
              destoryContext()
            }
          }
          
            /**
            * 

### 用户登录

#### 业务描述
   用户登录

#### 接口依赖
    无
#### 边界异常说明
    无

#### 输入
    1.user_request.LoginUserRequest

#### 前置检查
    1.手机号码规则验证
    2.密码规则,字母数字八位混合

####  逻辑处理
    1. 根据手机号码和密码查询用户记录
    2. 异常用户状态的用户登录返回 Exception

#### 数据库变更
    1. select *  from user where telphone = ? and password = ?

####  事务处理
    无

####  输出
    1.user_response.LoginUserResponse

            **/
            def login(request:com.today.api.user.request.LoginUserRequest , timeout: Long) : scala.concurrent.Future[com.today.api.user.response.LoginUserResponse] = {

            initContext("login");

            try {
              val _responseFuture = sendBaseAsync(login_args(request), new Login_argsSerializer(), new Login_resultSerializer(), timeout).asInstanceOf[java.util.concurrent.CompletableFuture[login_result]]

              val promise = Promise[com.today.api.user.response.LoginUserResponse]()

              _responseFuture.whenComplete(new BiConsumer[login_result, Throwable]{

              override def accept(r: login_result, e: Throwable): Unit = {
                if(e != null)
                  promise.failure(e)
                else
                  promise.success(r.success)
                }
              })
              promise.future
            }catch{
              case e: SoaException => throw e
              case e: TException => throw new SoaException(e)
            }finally {
              destoryContext()
            }
          }
          
            /**
            * 

### 用户修改个人资料

#### 业务描述
   用户再注册之后完善个人资料,完善资料增加积分5

#### 接口依赖
    无
#### 边界异常说明
    无

#### 输入
    1.user_request.ModifyUserRequest

#### 前置检查
    1. 邮箱规则验证
    2. qq 规则验证
    3. 用户状态判断只有用户状态为

####  逻辑处理
    1. 根据输入的参数计算用户积分
    2. 修改用户 email qq
    2. 修改完成之后调用积分action增加用户积分(完善资料增加积分5) ChangeUserIntegralAction

#### 数据库变更
    1. update user set email = ? , qq = ? where id = ${userId}

####  事务处理
    1. 无

####  输出
    1.user_response.ModifyUserAction

            **/
            def modifyUser(request:com.today.api.user.request.ModifyUserRequest , timeout: Long) : scala.concurrent.Future[com.today.api.user.response.ModifyUserResponse] = {

            initContext("modifyUser");

            try {
              val _responseFuture = sendBaseAsync(modifyUser_args(request), new ModifyUser_argsSerializer(), new ModifyUser_resultSerializer(), timeout).asInstanceOf[java.util.concurrent.CompletableFuture[modifyUser_result]]

              val promise = Promise[com.today.api.user.response.ModifyUserResponse]()

              _responseFuture.whenComplete(new BiConsumer[modifyUser_result, Throwable]{

              override def accept(r: modifyUser_result, e: Throwable): Unit = {
                if(e != null)
                  promise.failure(e)
                else
                  promise.success(r.success)
                }
              })
              promise.future
            }catch{
              case e: SoaException => throw e
              case e: TException => throw new SoaException(e)
            }finally {
              destoryContext()
            }
          }
          
            /**
            * 

### 冻结用户接口

#### 业务描述
   用户因为触犯一些游戏规则,后台自检程序或者管理员会冻结该用户

#### 接口依赖
    无
#### 边界异常说明
    无

#### 输入
    1.user_request.FreezeUserRequest

#### 前置检查
    1.用户状态检查(已冻结,已拉黑,已逻辑删除的用户不能冻结)

####  逻辑处理
    1. 设置用户状态为 FREEZE

#### 数据库变更
    1. update user set status = ? , remark = ? where id = ${userId}

####  事务处理
    1. 无

####  输出
    1.user_response.FreezeUserResponse

            **/
            def freezeUser(request:com.today.api.user.request.FreezeUserRequest , timeout: Long) : scala.concurrent.Future[com.today.api.user.response.FreezeUserResponse] = {

            initContext("freezeUser");

            try {
              val _responseFuture = sendBaseAsync(freezeUser_args(request), new FreezeUser_argsSerializer(), new FreezeUser_resultSerializer(), timeout).asInstanceOf[java.util.concurrent.CompletableFuture[freezeUser_result]]

              val promise = Promise[com.today.api.user.response.FreezeUserResponse]()

              _responseFuture.whenComplete(new BiConsumer[freezeUser_result, Throwable]{

              override def accept(r: freezeUser_result, e: Throwable): Unit = {
                if(e != null)
                  promise.failure(e)
                else
                  promise.success(r.success)
                }
              })
              promise.future
            }catch{
              case e: SoaException => throw e
              case e: TException => throw new SoaException(e)
            }finally {
              destoryContext()
            }
          }
          
            /**
            * 

### 拉黑用户接口

#### 业务描述
   用户因为触犯一些游戏规则,后台自检程序或者管理员会拉黑该用户,拉黑用户把用户的积分置为0

#### 接口依赖
    无
#### 边界异常说明
    无

#### 输入
    1.user_request.BlackUserRequest

#### 前置检查
    1.用户状态检查(已冻结,已拉黑,已逻辑删除的用户不能拉黑)

####  逻辑处理
    1. 设置用户状态为  BLACK
    2. 调用积分修改接口 ChangeUserIntegralAction

#### 数据库变更
    1. update user set status = ? , remark = ? where id = ${userId}

####  事务处理
    1. 无

####  输出
    1.user_response.BlackUserResponse

            **/
            def blackUser(request:com.today.api.user.request.BlackUserRequest , timeout: Long) : scala.concurrent.Future[com.today.api.user.response.BlackUserResponse] = {

            initContext("blackUser");

            try {
              val _responseFuture = sendBaseAsync(blackUser_args(request), new BlackUser_argsSerializer(), new BlackUser_resultSerializer(), timeout).asInstanceOf[java.util.concurrent.CompletableFuture[blackUser_result]]

              val promise = Promise[com.today.api.user.response.BlackUserResponse]()

              _responseFuture.whenComplete(new BiConsumer[blackUser_result, Throwable]{

              override def accept(r: blackUser_result, e: Throwable): Unit = {
                if(e != null)
                  promise.failure(e)
                else
                  promise.success(r.success)
                }
              })
              promise.future
            }catch{
              case e: SoaException => throw e
              case e: TException => throw new SoaException(e)
            }finally {
              destoryContext()
            }
          }
          
            /**
            * 

### 记录积分改变流水

#### 业务描述
   用户因为完成一些游戏规则或者触犯游戏规则导致积分减少或者增加,调用该接口修改用户积分

#### 接口依赖
    无
#### 边界异常说明
    无

#### 输入
    1.user_request.ChangeIntegralRequest

#### 前置检查
    1.用户状态检查(已冻结,已拉黑,已逻辑删除的用户不能冻结)

####  逻辑处理
    1. 设置用户状态为 FREEZE

#### 数据库变更
    1. update user set integral = ?  where id = ${userId}
    2. insert into integral_journal() values()

####  事务处理
    1. 无

####  输出
    1. i32 流水 Id

            **/
            def changeUserIntegral(request:com.today.api.user.request.ChangeIntegralRequest , timeout: Long) : scala.concurrent.Future[Int] = {

            initContext("changeUserIntegral");

            try {
              val _responseFuture = sendBaseAsync(changeUserIntegral_args(request), new ChangeUserIntegral_argsSerializer(), new ChangeUserIntegral_resultSerializer(), timeout).asInstanceOf[java.util.concurrent.CompletableFuture[changeUserIntegral_result]]

              val promise = Promise[Int]()

              _responseFuture.whenComplete(new BiConsumer[changeUserIntegral_result, Throwable]{

              override def accept(r: changeUserIntegral_result, e: Throwable): Unit = {
                if(e != null)
                  promise.failure(e)
                else
                  promise.success(r.success)
                }
              })
              promise.future
            }catch{
              case e: SoaException => throw e
              case e: TException => throw new SoaException(e)
            }finally {
              destoryContext()
            }
          }
          

      }
      