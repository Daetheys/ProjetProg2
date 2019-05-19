package Inventory
import Personnage._
import Player._
import bddItems.bddItem._


class Item {

	var id : Int = 0
	var name : String = ""
	var image_path : String = ""
	var element : String = "" //to open mechanisms of the same element

}

class Weapon extends Item {

	var force : Int = 0
	var charge : Boolean = true
	var cd_attack : Double = 0D
	var range : Int = 0
	var powerA : String = "" //compétence active du tireur
	var effect : String = "" //compétence passive de la cible

}

class Armor extends Item {

	var defense : Int = 0
	var bonus_strength : Int = 0
	var bonus_vivacity : Int = 0
	var bonus_intelligence : Int = 0
	var bonus_accuracy : Int = 0
	var bonus_dodge : Int = 0
	var powerP : String = "" //compétence passive

}

class OneUse extends Item {

	var effect : String = "" //modification des stats
	var use : Personnage => Unit = null

}

class Empile (i : Item) {
	
	var it : Item = i
	var quantity : Int = 0
	def incr(n : Int) = {this.quantity += n}

}

class mainInventory(p:Player) {

	var content : Array[Empile] = {
		val c = new Array[Empile](19)		
		c(0) = new Empile (create_first_aid_kit())
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
		c	
	}
	var equipe = p.units
	var items_perso : List[Array[Option[Item]]] = List()
	def add_inventory(c : Personnage) : Unit = {
		this.items_perso +: c.inventory
	}
	def init_perso : Unit = {
		equipe.foreach(add_inventory)
	}
	def add_to_main(i : Int) : Unit = {
		this.content(i).incr(1)
	}
	def send_to(i : Int, tab : Int) = {
		if (tab == 0) {
			this.add_to_main(i)
		} else {
			val pos = this.items_perso.apply(tab-1).indexOf(None)
			if (pos > -1) {
				this.items_perso.apply(tab-1)(pos) = Some(this.content(i).it)
			}
		}
	}
	
}
