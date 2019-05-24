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

	val seed = 65536
	val gen_parseur = true

	val Human = new Player
	Human.nb_max_units = 6
	val IA  = new Player
	for (i<-0 to 5){ //Ressources de base
		Human.inventory.send_to(0,0)
	}
	this.Human.inventory.send_to(19,0)
	this.Human.inventory.send_to(20,0)
	val Donjon = new Donjon(5)

	def initialize(){
		Donjon.start()
	}
}