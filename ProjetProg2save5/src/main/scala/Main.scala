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

	val Human = new Player
	val IA  = new Player
	this.Human.add_unit(bddp.create_turtle(this.Human))
	this.Human.units(0).add_item(bddi.create_recharge_fire())
	this.Human.units(0).equip_weapon(bddi.create_gun_god())
	this.Human.units(0).equip_armor(bddi.create_vest_god())
	this.Human.add_unit(bddp.create_bird(this.Human))
	this.Human.full_heal()
	val Donjon = new Donjon(5)

	def initialize(){
		Donjon.start()
	}
}