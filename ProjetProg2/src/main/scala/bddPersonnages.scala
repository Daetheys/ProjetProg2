package bddPersonnages

import Personnage.{Personnage=>Personnage}
import bddCompetences.{bddCompetences=>bddComp}

object bddPersonnages {

	def create_turtle(player:Int):Personnage = {
		var turtle = new Personnage
		turtle.name = "Tortue"
		turtle.pv_max = 100
		turtle.pv_current = 100
		turtle.player = player
		turtle.actives("Move") = bddComp.create_move(turtle,1,0) //speed,type(0:ground,1:air)
		turtle.actives("AutoAttack") = bddComp.create_autoattack(turtle,1,5,2) //range,dmg,speed
		turtle.image_path = "sprite_character_turtle.png"
		return turtle
	}

	def create_bird(player:Int):Personnage = {
		var bird = new Personnage
		bird.name = "Oiseau"
		bird.pv_max = 75
		bird.pv_current = 75
		bird.player = player
		bird.actives("Move") = bddComp.create_move(bird,3,1)
		bird.actives("AutoAttack") = bddComp.create_autoattack(bird,3,3,3)
		bird.image_path = "sprite_character_bird.png"
		return bird
	}
	//... -> faire la suite	
}
