package Inventory
import Personnage._
import Player._

class Item {

	var id : Int = 0
	var name : String = ""
	var image_path : String = ""
	var element : String = "" //to open mechanisms of the same element

}

class Weapon extends Item {

	var force : Int = 0
	var charge : Boolean = true
	var powerA : String = "" //compétence active du tireur
	var effect : String = "" //compétence passive de la cible

}

class Armor extends Item {

	var defense : Int = 0
	var powerP : String = "" //compétence passive

}

class OneUse extends Item {

	var effect : String = "" //modification des stats

}

class Empile (i : Item) {
	
	var it : Item = i
	var quantity : Int = 0
	def incr(n : Int) = {this.quantity += n}

}

class mainInventory(p:Player) {

	var content : Array[Empile] = {
		var c = new Array[Empile](19)		
		c(0) = new Empile (create_first-aid-kit())
		c(1) = new Empile (create_gun_confusion())
		c(2) = new Empile (create_gun_electricity())
		c(3) = new Empile (create_gun_fire())
		c(4) = new Empile (create_gun_ice())
		c(5) = new Empile (create_gun_ink())
		c(6) = new Empile (create_gun_poison())
		c(7) = new Empile (create_recharge_confusion())
		c(8) = new Empile (create_recharge_electricity())
		c(9) = new Empile (create_recharge_fire())
		c(10) = new Empile (create_recharge_ice())
		c(11) = new Empile (create_recharge_ink())
		c(12) = new Empile (create_recharge_poison())
		c(13) = new Empile (create_vest_confusion())
		c(14) = new Empile (create_vest_electricity())
		c(15) = new Empile (create_vest_fire())
		c(16) = new Empile (create_vest_confusion())
		c(17) = new Empile (create_vest_ink())
		c(18) = new Empile (create_vest_poison())
		return c	
	}
	var equipe = p.units
	var items_perso : List[Array] = Nil()
	def add_inventory(c : Personnage) : Unit = {
		this.items_perso +: c.inventory
	}
	def init_perso : Unit = {
		equipe.foreach(add_inventory)
	}
	def add_to_main(i : Item) : Unit = {
		this.content(i.id-1).incr(1)
	}
	
}
