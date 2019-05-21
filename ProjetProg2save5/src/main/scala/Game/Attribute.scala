package Attribute

abstract class Attribute 

abstract class PrimaryAttribute extends Attribute
case class Strength() extends PrimaryAttribute
case class Vivacity() extends PrimaryAttribute
case class Intelligence() extends PrimaryAttribute
case class Accuracy() extends PrimaryAttribute
case class Dodge() extends PrimaryAttribute

abstract class SecondaryAttribute extends Attribute
case class Health() extends SecondaryAttribute
