package Game

import Personnage.{Jeton=>Jeton,Personnage=>Personnage}
import Environnement.{Environnement=>Environnement,_}
import Graphics2.{app=>app}
import bddPersonnages.{bddPersonnages=>bddp}
import Schematics.{Tile=>Tile,Plan=>Plan}
import Mechanisms.{Sprite_plan=>Sprite_plan}
import Display.{All_sprites=>All_sprites}
import Player._
import Donjon._

object Game {

	val Human = new Player
	val IA  = new Player
	val Donjon = new Donjon(5)

	def initialize(){
		Donjon.start()
	}
}