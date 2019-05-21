package bddPersonnages

import Personnage.{Personnage=>Personnage}
import bddCompetences.{bddCompetences=>bddComp}
import Player._
import bddBehaviour._

object bddPersonnages {


	def create_turtle(player:Player):Personnage = {
		var turtle = new Personnage
		turtle.name = "Tortue"
		//Base Stat
		turtle.strength = 50
		turtle.vivacity = 30
		turtle.intelligence = 50
		turtle.accuracy = 50
		turtle.dodge = 50
		//Base Stat
		turtle.player = player
		turtle.actives("Move") = bddComp.create_move(turtle,0) //speed,type(0:ground,1:air)
		turtle.actives("AutoAttack") = bddComp.create_autoattack(turtle) //range,dmg,speed
		turtle.image_path = "sprite_character_turtle.png"
		turtle.sheet_image = "sprite_sheet_turtle.png"
		turtle.add_spawn_call("AutoAttack")
		turtle.refresh_hp_max()
		turtle.full_heal()
		return turtle
	}

	def create_bird(player:Player):Personnage = {
		var bird = new Personnage
		bird.name = "Oiseau"
		//Base Stat
		bird.strength = 30
		bird.vivacity = 80
		bird.intelligence = 50
		bird.accuracy = 50
		bird.dodge = 50
		//Base Stat
		bird.player = player
		bird.actives("Move") = bddComp.create_move(bird,1)
		bird.actives("AutoAttack") = bddComp.create_autoattack(bird)
		bird.image_path = "sprite_character_bird.png"
		bird.sheet_image = "sprite_sheet_bird.png"
		bird.add_spawn_call("AutoAttack")
		bird.refresh_hp_max()
		bird.full_heal()
		return bird
	}
	
	def create_bees(player:Player):Personnage = {
		var bees = new Personnage
		bees.name = "Abeilles"
		//Base Stat
		bees.strength = 30
		bees.vivacity = 70
		bees.intelligence = 60
		bees.accuracy = 40
		bees.dodge = 60
		//Base Stat
		bees.player = player
		bees.actives("Move") = bddComp.create_move(bees,1)
		bees.actives("AutoAttack") = bddComp.create_autoattack(bees)
		bees.image_path = "sprite_character_bees.png"
		bees.sheet_image = "sprite_sheet_bees.png"
		bees.add_spawn_call("AutoAttack")
		bees.refresh_hp_max()
		bees.full_heal()
		return bees
	}

	def create_cat(player:Player):Personnage = {
		var cat = new Personnage
		cat.name = "Chat"
		//Base Stat
		cat.strength = 40
		cat.vivacity = 60
		cat.intelligence = 40
		cat.accuracy = 40
		cat.dodge = 50
		//Base Stat
		cat.player = player
		cat.actives("Move") = bddComp.create_move(cat,0)
		cat.actives("AutoAttack") = bddComp.create_autoattack(cat)
		cat.image_path = "sprite_character_cat.png"
		cat.sheet_image = "sprite_sheet_cat.png"
		cat.add_spawn_call("AutoAttack")
		cat.refresh_hp_max()
		cat.full_heal()
		return cat
	}

	def create_monkey(player:Player):Personnage = {
		var monkey = new Personnage
		monkey.name = "Singe"
		//Base Stat
		monkey.strength = 70
		monkey.vivacity = 40
		monkey.intelligence = 60
		monkey.accuracy = 30
		monkey.dodge = 30
		//Base Stat
		monkey.player = player
		monkey.actives("Move") = bddComp.create_move(monkey,0)
		monkey.actives("AutoAttack") = bddComp.create_autoattack(monkey)
		monkey.image_path = "sprite_character_monkey.png"
		monkey.sheet_image = "sprite_sheet_monkey.png"
		monkey.add_spawn_call("AutoAttack")
		monkey.refresh_hp_max()
		monkey.full_heal()
		return monkey
	}

	def create_rabbit(player:Player):Personnage = {
		var rabbit = new Personnage
		rabbit.name = "Lapin"
		//Base Stat
		rabbit.strength = 40
		rabbit.vivacity = 50
		rabbit.intelligence = 50
		rabbit.accuracy = 40
		rabbit.dodge = 60
		//Base Stat
		rabbit.player = player
		rabbit.actives("Move") = bddComp.create_move(rabbit,0)
		rabbit.actives("AutoAttack") = bddComp.create_autoattack(rabbit)
		rabbit.image_path = "sprite_character_rabbit.png"
		rabbit.sheet_image = "sprite_sheet_rabbit.png"
		rabbit.add_spawn_call("AutoAttack")
		rabbit.refresh_hp_max()
		rabbit.full_heal()
		return rabbit
	}

	def create_snake(player:Player):Personnage = {
		var snake = new Personnage
		snake.name = "Serpent"
		//Base Stat
		snake.strength = 30
		snake.vivacity = 40
		snake.intelligence = 30
		snake.accuracy = 60
		snake.dodge = 70
		//Base Stat
		snake.player = player
		snake.actives("Move") = bddComp.create_move(snake,0)
		snake.actives("AutoAttack") = bddComp.create_autoattack(snake)
		snake.image_path = "sprite_character_snake.png"
		snake.sheet_image = "sprite_sheet_snake.png"
		snake.add_spawn_call("AutoAttack")
		snake.refresh_hp_max()
		snake.full_heal()
		return snake
	}

	def create_robot(player:Player):Personnage = {
		var robot = new Personnage
		robot.name = "Unité de Défense"
		robot.pv_max = 50
		robot.pv_current = 50
		robot.player = player
		//Base Stat
		robot.strength = 30
	 	robot.vivacity = 50
		robot.intelligence = 40
		robot.accuracy = 30
		robot.dodge = 20
		//Base Stat
		robot.actives("Move") = bddComp.create_move(robot,1)
		robot.actives("AutoAttack") = bddComp.create_autoattack(robot)
		robot.image_path = "sprite_character_turret.png"
		robot.add_spawn_call("AutoAttack")
		robot.ia = bddBehaviour.create_rage(robot)
		robot.refresh_hp_max()
		robot.full_heal()
		return robot
	}
	
	//Boss
	def create_sentinel(player:Player):Personnage = {
		// 1er Boss
		var sentinel = new Personnage
		sentinel.name = "Sentinelle"
		//Base Stat
		sentinel.strength = 120
	 	sentinel.vivacity = 140
		sentinel.intelligence = 160
		sentinel.accuracy = 220
		sentinel.dodge = 70
		//Base Stat
		sentinel.player = player
		sentinel.actives("AutoAttack") = bddComp.create_autoattack(sentinel) //Grosse attaque en mélée
		sentinel.image_path = "sprite_boss_drone.png" //Il faudra le mettre a jour
		sentinel.add_spawn_call("AutoAttack")
		sentinel.ia = bddBehaviour.create_sentinel(sentinel)
		sentinel.refresh_hp_max()
		sentinel.full_heal()
		return sentinel
	}
	
	def create_healbot(player:Player):Personnage = {
		//Add du 1er boss -> Soignent le boss quand ils arrivent au contact
		var robot = new Personnage
		robot.name = "Robot de Soin"
		//Base Stat
		robot.strength = 20
	 	robot.vivacity = 60
		robot.intelligence = 40
		robot.accuracy = 40
		robot.dodge = 30
		//Base Stat
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,1)
		robot.actives("AutoAttack") = bddComp.create_autoattack(robot)
		robot.image_path = "sprite_character_tank_healer.png"
		robot.add_spawn_call("AutoAttack")
		robot.ia = bddBehaviour.create_rage(robot)
		robot.refresh_hp_max()
		robot.full_heal()
		return robot
	}
	
	def create_attackbot(player:Player):Personnage = {
		//Add du 1er boss -> Attaquent les joueurs et tapent très fort
		var robot = new Personnage
		robot.name = "Robot d'Assaut"
		//Base Stat
		robot.strength = 40
	 	robot.vivacity = 20
		robot.intelligence = 80
		robot.accuracy = 70
		robot.dodge = 20
		//Base Stat
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,1)
		robot.actives("AutoAttack") = bddComp.create_autoattack(robot) //Ils font très mal
		robot.image_path = "sprite_character_tank_fighter.png"
		robot.ia = bddBehaviour.create_rage(robot)
		robot.refresh_hp_max()
		robot.full_heal()
		return robot
	}
	
	def create_explosionbot(player:Player):Personnage = {
		//Add du 1er boss -> Explosent au contact
		var robot = new Personnage
		robot.name = "Robot explosifs"
		//Base Stat
		robot.strength = 10
	 	robot.vivacity = 40
		robot.intelligence = 60
		robot.accuracy = 30
		robot.dodge = 10
		//Base Stat
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,1) //Se déplace très vite
		robot.actives("Explode") = bddComp.create_explode(robot,15)
		robot.image_path = "sprite_character_tank_plasma.png"
		robot.add_spawn_call("AutoAttack")
		robot.ia = bddBehaviour.create_rage(robot)
		robot.refresh_hp_max()
		robot.full_heal()
		return robot
	}
	
	def create_firebot(player:Player):Personnage = {
		//Add du 1er boss -> Explosent au contact
		var robot = new Personnage
		robot.name = "Robot de feu"
		//Base Stat
		robot.strength = 30
	 	robot.vivacity = 45
		robot.intelligence = 65
		robot.accuracy = 35
		robot.dodge = 10
		//Base Stat
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,1) //Se déplace très vite
		robot.actives("AutoAttack") = bddComp.create_autoattack(robot)
		robot.image_path = "sprite_character_tank_fire.png"
		robot.add_spawn_call("AutoAttack")
		robot.ia = bddBehaviour.create_rage(robot)
		robot.refresh_hp_max()
		robot.full_heal()
		return robot
	}
	
	def create_icebot(player:Player):Personnage = {
		//Add du 1er boss -> Explosent au contact
		var robot = new Personnage
		robot.name = "Robot de glace"
		//Base Stat
		robot.strength = 25
	 	robot.vivacity = 40
		robot.intelligence = 85
		robot.accuracy = 45
		robot.dodge = 15
		//Base Stat
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,1) //Se déplace très vite
		robot.actives("AutoAttack") = bddComp.create_autoattack(robot)
		robot.image_path = "sprite_character_tank_ice.png"
		robot.add_spawn_call("AutoAttack")
		robot.ia = bddBehaviour.create_rage(robot)
		robot.refresh_hp_max()
		robot.full_heal()
		return robot
	}
	
	def create_poisonbot(player:Player):Personnage = {
		//Add du 1er boss -> Explosent au contact
		var robot = new Personnage
		robot.name = "Robot de poison"
		//Base Stat
		robot.strength = 75
	 	robot.vivacity = 40
		robot.intelligence = 65
		robot.accuracy = 25
		robot.dodge = 30
		//Base Stat
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,1) //Se déplace très vite
		robot.actives("Explode") = bddComp.create_autoattack(robot)
		robot.image_path = "sprite_character_tank_poison.png"
		robot.add_spawn_call("AutoAttack")
		robot.ia = bddBehaviour.create_rage(robot)
		robot.refresh_hp_max()
		robot.full_heal()
		return robot
	}
	
	def create_electricitybot(player:Player):Personnage = {
		//Add du 1er boss -> Explosent au contact
		var robot = new Personnage
		robot.name = "Robot d'electricite"
		//Base Stat
		robot.strength = 45
	 	robot.vivacity = 30
		robot.intelligence = 35
		robot.accuracy = 15
		robot.dodge = 75
		//Base Stat
		robot.player = player
		robot.actives("Move") = bddComp.create_move(robot,1) //Se déplace très vite
		robot.actives("AutoAttack") = bddComp.create_autoattack(robot)
		robot.image_path = "sprite_character_tank_electricity.png"
		robot.add_spawn_call("AutoAttack")
		robot.ia = bddBehaviour.create_rage(robot)
		robot.refresh_hp_max()
		robot.full_heal()
		return robot
	}
}
