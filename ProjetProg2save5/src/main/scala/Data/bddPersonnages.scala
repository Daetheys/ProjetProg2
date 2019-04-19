package bddPersonnages

import Personnage.{Personnage=>Personnage}
import bddCompetences.{bddCompetences=>bddComp}
import Player._
import bddBehaviour._

object bddPersonnages {


	def create_turtle(player:Player):Personnage = {
		var turtle = new Personnage
		turtle.name = "Tortue"
		turtle.pv_max = 100
		turtle.pv_current = 100
		turtle.player = player
		turtle.actives("Move") = bddComp.create_move(turtle,2.0,0) //speed,type(0:ground,1:air)
		turtle.actives("AutoAttack") = bddComp.create_autoattack(turtle,2,6,1.2) //range,dmg,speed
		turtle.actives("Feu") = bddComp.create_fire_spell(turtle)
		turtle.image_path = "sprite_character_turtle.png"
		turtle.sheet_image = "sprite_sheet_turtle.png"
		turtle.add_spawn_call("AutoAttack")
		return turtle
	}

	def create_bird(player:Player):Personnage = {
		var bird = new Personnage
		bird.name = "Oiseau"
		bird.pv_max = 75
		bird.pv_current = 75
		bird.player = player
		bird.actives("Move") = bddComp.create_move(bird,0.1,1)
		bird.actives("AutoAttack") = bddComp.create_autoattack(bird,40,60,0.1)
		bird.image_path = "sprite_character_bird.png"
		bird.sheet_image = "sprite_sheet_bird.png"
		bird.add_spawn_call("AutoAttack")
		return bird
	}
	
	def create_bees(player:Player):Personnage = {
		var bees = new Personnage
		bees.name = "Abeilles"
		bees.pv_max = 60
		bees.pv_current = 60
		bees.player = player
		bees.actives("Move") = bddComp.create_move(bees,0.7,1)
		bees.actives("AutoAttack") = bddComp.create_autoattack(bees,2,2,0.4)
		bees.image_path = "sprite_character_bees.png"
		bees.sheet_image = "sprite_sheet_bees.png"
		bees.add_spawn_call("AutoAttack")
		return bees
	}

	def create_cat(player:Player):Personnage = {
		var cat = new Personnage
		cat.name = "Chat"
		cat.pv_max = 90
		cat.pv_current = 90
		cat.player = player
		cat.actives("Move") = bddComp.create_move(cat,0.9,0)
		cat.actives("AutoAttack") = bddComp.create_autoattack(cat,2,4,1.2)
		cat.image_path = "sprite_character_cat.png"
		cat.sheet_image = "sprite_sheet_cat.png"
		cat.add_spawn_call("AutoAttack")
		return cat
	}

	def create_monkey(player:Player):Personnage = {
		var monkey = new Personnage
		monkey.name = "Singe"
		monkey.pv_max = 110
		monkey.pv_current = 110
		monkey.player = player
		monkey.actives("Move") = bddComp.create_move(monkey,1.2,0)
		monkey.actives("AutoAttack") = bddComp.create_autoattack(monkey,3,7,1.5)
		monkey.image_path = "sprite_character_monkey.png"
		monkey.sheet_image = "sprite_sheet_monkey.png"
		monkey.add_spawn_call("AutoAttack")
		return monkey
	}

	def create_rabbit(player:Player):Personnage = {
		var rabbit = new Personnage
		rabbit.name = "Lapin"
		rabbit.pv_max = 65
		rabbit.pv_current = 65
		rabbit.player = player
		rabbit.actives("Move") = bddComp.create_move(rabbit,0.6,0)
		rabbit.actives("AutoAttack") = bddComp.create_autoattack(rabbit,3,2,1.2)
		rabbit.image_path = "sprite_character_rabbit.png"
		rabbit.sheet_image = "sprite_sheet_rabbit.png"
		rabbit.add_spawn_call("AutoAttack")
		return rabbit
	}

	def create_snake(player:Player):Personnage = {
		var snake = new Personnage
		snake.name = "Serpent"
		snake.pv_max = 80
		snake.pv_current = 80
		snake.player = player
		snake.actives("Move") = bddComp.create_move(snake,1.1,0)
		snake.actives("AutoAttack") = bddComp.create_autoattack(snake,1,4,1.0)
		snake.image_path = "sprite_character_snake.png"
		snake.sheet_image = "sprite_sheet_snake.png"
		snake.add_spawn_call("AutoAttack")
		return snake
	}

	def create_robot(player:Player):Personnage = {
		var robot = new Personnage
		robot.name = "Unité de Défense"
		robot.pv_max = 50
		robot.pv_current = 50
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,0.9,1)
		robot.actives("AutoAttack") = bddComp.create_autoattack(robot,5,2,1.1)
		robot.image_path = "sprite_character_turret.png"
		robot.add_spawn_call("AutoAttack")
		robot.ia = bddBehaviour.create_rage(robot)
		return robot
	}
	
	//Boss
	def create_sentinel(player:Player):Personnage = {
		// 1er Boss
		var sentinel = new Personnage
		sentinel.name = "Sentinelle"
		sentinel.pv_max = 510
		sentinel.pv_current = 510
		sentinel.player = player
		sentinel.actives("AutoAttack") = bddComp.create_autoattack(sentinel,2,25,3.0) //Grosse attaque en mélée
		sentinel.image_path = "sprite_character_turret.png" //Il faudra le mettre a jour
		sentinel.add_spawn_call("AutoAttack")
		sentinel.ia = bddBehaviour.create_sentinel(sentinel)
		return sentinel
	}
	
	def create_healbot(player:Player):Personnage = {
		//Add du 1er boss -> Soignent le boss quand ils arrivent au contact
		var robot = new Personnage
		robot.name = "Robot de Soin"
		robot.pv_max = 50
		robot.pv_current = 50
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,1.5,1)
		robot.actives("AutoAttack") = bddComp.create_autoattack(robot,2,1,2.0)
		robot.image_path = "sprite_character_tank_healer.png"
		robot.add_spawn_call("AutoAttack")
		robot.ia = bddBehaviour.create_rage(robot)
		return robot
	}
	
	def create_attackbot(player:Player):Personnage = {
		//Add du 1er boss -> Attaquent les joueurs et tapent très fort
		var robot = new Personnage
		robot.name = "Robot d'Assaut"
		robot.pv_max = 30
		robot.pv_current = 30
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,1.5,1)
		robot.actives("AutoAttack") = bddComp.create_autoattack(robot,3,7,2.0) //Ils font très mal
		robot.image_path = "sprite_character_tank_fighter.png"
		robot.ia = bddBehaviour.create_rage(robot)
		return robot
	}
	
	def create_explosionbot(player:Player):Personnage = {
		//Add du 1er boss -> Explosent au contact
		var robot = new Personnage
		robot.name = "Robot explosifs"
		robot.pv_max = 20
		robot.pv_current = 20
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,0.7,1) //Se déplace très vite
		robot.actives("Explode") = bddComp.create_explode(robot,15)
		robot.image_path = "sprite_character_tank_plasma.png"
		robot.add_spawn_call("AutoAttack")
		robot.ia = bddBehaviour.create_rage(robot)
		return robot
	}
}
