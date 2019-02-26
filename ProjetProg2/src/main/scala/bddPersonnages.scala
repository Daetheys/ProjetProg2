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
	
	def create_bees(player:Int):Personnage = {
		var bees = new Personnage
		bees.name = "Abeilles"
		bees.pv_max = 50
		bees.pv_current = 50
		bees.player = player
		bees.actives("Move") = bddComp.create_move(bees,3,1)
		bees.actives("AutoAttack") = bddComp.create_autoattack(bees,3,3,3)
		bees.image_path = "sprite_character_bees.png"
		return bees
	}

	def create_cat(player:Int):Personnage = {
		var cat = new Personnage
		cat.name = "Chat"
		cat.pv_max = 90
		cat.pv_current = 90
		cat.player = player
		cat.actives("Move") = bddComp.create_move(cat,3,1)
		cat.actives("AutoAttack") = bddComp.create_autoattack(cat,3,3,3)
		cat.image_path = "sprite_character_cat.png"
		return cat
	}

	def create_monkey(player:Int):Personnage = {
		var monkey = new Personnage
		monkey.name = "Singe"
		monkey.pv_max = 110
		monkey.pv_current = 110
		monkey.player = player
		monkey.actives("Move") = bddComp.create_move(monkey,3,1)
		monkey.actives("AutoAttack") = bddComp.create_autoattack(monkey,3,3,3)
		monkey.image_path = "sprite_character_monkey.png"
		return monkey
	}

	def create_rabbit(player:Int):Personnage = {
		var rabbit = new Personnage
		rabbit.name = "Lapin"
		rabbit.pv_max = 65
		rabbit.pv_current = 65
		rabbit.player = player
		rabbit.actives("Move") = bddComp.create_move(rabbit,3,1)
		rabbit.actives("AutoAttack") = bddComp.create_autoattack(rabbit,3,3,3)
		rabbit.image_path = "sprite_character_rabbit.png"
		return rabbit
	}

	def create_snake(player:Int):Personnage = {
		var snake = new Personnage
		snake.name = "Serpent"
		snake.pv_max = 80
		snake.pv_current = 80
		snake.player = player
		snake.actives("Move") = bddComp.create_move(snake,3,1)
		snake.actives("AutoAttack") = bddComp.create_autoattack(snake,3,3,3)
		snake.image_path = "sprite_character_snake.png"
		return snake
	}

	def create_robot(player:Int):Personnage = {
		var robot = new Personnage
		robot.name = "Unité de Défense"
		robot.pv_max = 70
		robot.pv_current = 70
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,3,1)
		robot.actives("AutoAttack") = bddComp.create_autoattack(robot,3,3,3)
		robot.image_path = "sprite_character_turret.png"
		return robot
	}
}
