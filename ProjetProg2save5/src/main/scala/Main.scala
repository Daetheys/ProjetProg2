package Game

import Personnage.{Jeton=>Jeton,Personnage=>Personnage}
import Environnement.{Environnement=>Environnement,_}
import Graphics2.{app=>app}
import bddPersonnages.{bddPersonnages=>bddp}
import Schematics.{Tile=>Tile,Plan=>Plan}
import Mechanisms.{Sprite_plan=>Sprite_plan}
//import Display.{All_sprites=>All_sprites}
import Player._
import Donjon._
import bddItems.{bddItem=>bddi}

object Game {

	val seed = 123456

	val Human = new Player
	Human.nb_max_units = 6
	val IA  = new Player
	for (i<-0 to 5){ //Ressources de base
		Human.inventory.send_to(0,0)
	}
	this.Human.add_unit(bddp.create_turtle(this.Human))
	this.Human.units(0).add_item(bddi.create_recharge_electricity())
	this.Human.units(0).add_item(bddi.create_recharge_electricity())
	this.Human.units(0).add_item(bddi.create_recharge_electricity())
	this.Human.units(0).add_item(bddi.create_first_aid_kit())
	this.Human.units(0).add_item(bddi.create_gun_god())
	this.Human.units(0).equip_weapon(bddi.create_gun_electricity())
	this.Human.units(0).equip_armor(bddi.create_vest_poison())
	this.Human.add_unit(bddp.create_bird(this.Human))
	this.Human.full_heal()
	val Donjon = new Donjon(5)

	def initialize(){
		Donjon.start()
	}
}