package com.today.xinzhi.services.common.domain

class Currency private(val id: Int, val name: String) extends com.isuwang.dapeng.core.enums.TEnum(id,name) {}

/**
 * Autogenerated by Dapeng-Code-Generator (1.2.2)
*
* DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
*  @generated

*

鑫知结算币种

**/
object Currency {


      val XUEDIAN = new Currency(1, "心点")

      val JIAODIAN = new Currency(2, "知点")

val UNDEFINED = new Currency(-1,"UNDEFINED") // undefined enum


def findByValue(v: Int): Currency = {
  v match {
    case 1 => XUEDIAN
      case 2 => JIAODIAN

    case _ => new Currency(v,"#"+ v)
  }
}

def apply(v: Int) = findByValue(v)
def unapply(v: Currency): Option[Int] = Some(v.id)

}
    