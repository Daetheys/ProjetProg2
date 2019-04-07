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
		turtle.actives("Move") = bddComp.create_move(turtle,1.0,0) //speed,type(0:ground,1:air)
		turtle.actives("AutoAttack") = bddComp.create_autoattack(turtle,1,6,1.2) //range,dmg,speed
		turtle.image_path = "sprite_character_turtle.png"
		turtle.add_spawn_call("Move")
		turtle.add_spawn_call("AutoAttack")
		return turtle
	}

	def create_bird(player:Int):Personnage = {
		var bird = new Personnage
		bird.name = "Oiseau"
		bird.pv_max = 75
		bird.pv_current = 75
		bird.player = player
		bird.actives("Move") = bddComp.create_move(bird,0.3,1)
		bird.actives("AutoAttack") = bddComp.create_autoattack(bird,3,3,0.8)
		bird.image_path = "sprite_character_bird.png"
		return bird
	}
	
	def create_bees(player:Int):Personnage = {
		var bees = new Personnage
		bees.name = "Abeilles"
		bees.pv_max = 60
		bees.pv_current = 60
		bees.player = player
		bees.actives("Move") = bddComp.create_move(bees,2,1)
		bees.actives("AutoAttack") = bddComp.create_autoattack(bees,2,2,1.0)
		bees.image_path = "sprite_character_bees.png"
		return bees
	}

	def create_cat(player:Int):Personnage = {
		var cat = new Personnage
		cat.name = "Chat"
		cat.pv_max = 90
		cat.pv_current = 90
		cat.player = player
		cat.actives("Move") = bddComp.create_move(cat,3,0)
		cat.actives("AutoAttack") = bddComp.create_autoattack(cat,2,4,3.0)
		cat.image_path = "sprite_character_cat.png"
		return cat
	}

	def create_monkey(player:Int):Personnage = {
		var monkey = new Personnage
		monkey.name = "Singe"
		monkey.pv_max = 110
		monkey.pv_current = 110
		monkey.player = player
		monkey.actives("Move") = bddComp.create_move(monkey,2,0)
		monkey.actives("AutoAttack") = bddComp.create_autoattack(monkey,2,5,1.0)
		monkey.image_path = "sprite_character_monkey.png"
		return monkey
	}

	def create_rabbit(player:Int):Personnage = {
		var rabbit = new Personnage
		rabbit.name = "Lapin"
		rabbit.pv_max = 65
		rabbit.pv_current = 65
		rabbit.player = player
		rabbit.actives("Move") = bddComp.create_move(rabbit,4,0)
		rabbit.actives("AutoAttack") = bddComp.create_autoattack(rabbit,3,2,2.0)
		rabbit.image_path = "sprite_character_rabbit.png"
		return rabbit
	}

	def create_snake(player:Int):Personnage = {
		var snake = new Personnage
		snake.name = "Serpent"
		snake.pv_max = 80
		snake.pv_current = 80
		snake.player = player
		snake.actives("Move") = bddComp.create_move(snake,3,0)
		snake.actives("AutoAttack") = bddComp.create_autoattack(snake,1,4,1.0)
		snake.image_path = "sprite_character_snake.png"
		return snake
	}

	def create_robot(player:Int):Personnage = {
		var robot = new Personnage
		robot.name = "Unité de Défense"
		robot.pv_max = 50
		robot.pv_current = 50
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,3,1)
		robot.actives("AutoAttack") = bddComp.create_autoattack(robot,5,1,2.0)
		robot.image_path = "sprite_character_turret.png"
		return robot
	}
	
	//Boss
	def create_sentinel(player:Int):Personnage = {
		// 1er Boss
		var sentinel = new Personnage
		sentinel.name = "Sentinelle"
		sentinel.pv_max = 2300
		sentinel.pv_current = 2300
		sentinel.player = player
		sentinel.actives("AutoAttack1") = bddComp.create_autoattack(sentinel,5,1,0.3) //Attaque vite mais fait peu de dmg
		sentinel.actives("AutoAttack2") = bddComp.create_autoattack(sentinel,5,1,0.3) //2e attaque de base similaire a la 1ere
		sentinel.image_path = "sprite_character_turret.png" //Il faudra le mettre a jour
		return sentinel
	}
	
	def create_healbot(player:Int):Personnage = {
		//Add du 1er boss -> Soignent le boss quand ils arrivent au contact
		var robot = new Personnage
		robot.name = "Robot de Soin"
		robot.pv_max = 50
		robot.pv_current = 50
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,1.5,1)
		robot.actives("AutoAttack") = bddComp.create_autoattack(robot,2,1,2.0)
		robot.image_path = "sprite_character_turret.png"
		return robot
	}
	
	def create_attackbot(player:Int):Personnage = {
		//Add du 1er boss -> Attaquent les joueurs et tapent très fort
		var robot = new Personnage
		robot.name = "Robot d'Assaut"
		robot.pv_max = 30
		robot.pv_current = 30
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,1.5,1)
		robot.actives("AutoAttack") = bddComp.create_autoattack(robot,7,3,2.0) //Ils font très mal
		robot.image_path = "sprite_character_turret.png"
		return robot
	}
	
	def create_explosionbot(player:Int):Personnage = {
		//Add du 1er boss -> Explosent au contact
		var robot = new Personnage
		robot.name = "Robot explosifs"
		robot.pv_max = 20
		robot.pv_current = 20
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,0.7,1) //Se déplace très vite
		robot.image_path = "sprite_character_turret.png"
		return robot
	}
}
