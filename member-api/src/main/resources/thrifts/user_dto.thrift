namespace java com.today.api.user.dto
struct TUser{
   /**
   * 利用主键策略生成的唯一键
   */
 1 :  i32 id,
   /**
   * 用户名
   */
 2 :  string userName,
   /**
   * 密码
   */
 3 :  string password,
   /**
   * 手机号码
   */
 4 :  string telephone,
   /**
   * 邮箱
   */
 5 :  string email,
   /**
   *  qq
   */
 6 :  string qq,
   /**
   *  积分
   */
 7 :  i32 integral,
   /**
   * 创建时间
   */
 8 :  i64 createdAt,
   /**
   * 特指后台创建人(公司员工 id)
   */
 9 :  i32 createdBy,
   /**
   * 更新时间
   */
 10 :  i64 updatedAt,
   /**
   * 特指后台更新人(公司员工 id)
   */
 11 :  i32 updatedBy,
   /**
   * 备注
   */
 12 :  string remark,
  /**
    * 用户状态
    */
  13 :  string userStatus,
   /**
     * 数据状态,是否删除
     */
   14 :  string isDeleted,
}

struct TIntegralJournal{
   /**
   * 利用主键策略生成的唯一键
   */
 1 :  i32 id,
   /**
   * 用户名
   */
 2 :  i32 userId,
   /**
   * 流水类型,1:增加流水(add);2:减少流水(minus)
   */
 3 :  i32 integralType,
   /**
   * 当前流水涉及到的积分值
   */
 4 :  i32 integralPrice,
   /**
   * 流水来源,1:完善个人资料(prefect_information);2:拉黑(black)
   */
 5 :  i32 integralSource,
   /**
   * 用户当前的积分值
   */
 6 :  i32 integral,
   /**
   * 创建时间
   */
 7 :  i64 createdAt,
   /**
   * 特指后台创建人(公司员工 id)
   */
 8 :  i32 createdBy,
   /**
   * 更新时间
   */
 9 :  i64 updatedAt,
   /**
   * 特指后台更新人(公司员工 id)
   */
 10 :  i32 updatedBy,
   /**
   * 备注
   */
 11 :  string remark,
}
