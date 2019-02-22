import Personnage.{Personnage=>Personnage}
import bddCompetences.{bddCompetences=>bddComp}

object bddPersonnages {
	
	def create_turtle():Personnage = {
		var turtle = new Personnage
		turtle.name = "Tortue"
		turtle.pv_max = 100
		turtle.pv_current = 100
		turtle.force = 8
		turtle.vitesse = 2
		turtle.intelligence = 6
		turtle.precision = 4
		turtle.esquive = 3
		return turtle
	}
	
	def create_bird():Personnage = {
		var bird = new Personnage
		bird.name = "Oiseau"
		bird.pv_max = 100
		bird.pv_current = 100
		bird.force = 3
		bird.vitesse = 9
		bird.intelligence = 1
		bird.precision = 5
		bird.esquive = 7
		return bird
	}
	
	def create_cat():Personnage = {
		var cat = new Personnage
		cat.name = "Cat"
		cat.pv_max = 100
		cat.pv_current = 100
		cat.force = 4
		cat.vitesse = 6
		cat.intelligence = 4
		cat.precision = 2
		cat.esquive = 8
		return cat
	}
	
	def create_bees():Personnage = {
		var bees = new Personnage
		bees.name = "Bees"
		bees.pv_max = 100
		bees.pv_current = 100
		bees.force = 2
		bees.vitesse = 5
		bees.intelligence = 2
		bees.precision = 3
		bees.esquive = 9
		return bees
	}
	
	def create_monkey():Personnage = {
		var monkey = new Personnage
		monkey.name = "Monkey"
		monkey.pv_max = 100
		monkey.pv_current = 100
		monkey.force = 6
		monkey.vitesse = 3
		monkey.intelligence = 8
		monkey.precision = 4
		monkey.esquive = 5
		return monkey
	}
	
	def create_rabbit():Personnage = {
		var rabbit = new Personnage
		rabbit.name = "Rabbit"
		rabbit.pv_max = 100
		rabbit.pv_current = 100
		rabbit.force = 4
		rabbit.vitesse = 7
		rabbit.intelligence = 3
		rabbit.precision = 8
		rabbit.esquive = 5
		return rabbit
	}
	
	def create_snake():Personnage = {
		var snake = new Personnage
		snake.name = "Snake"
		snake.pv_max = 100
		snake.pv_current = 100
		snake.force = 1
		snake.vitesse = 8
		snake.intelligence = 5
		snake.precision = 4
		snake.esquive = 7
		return snake
	}
}
