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
		bird.name = "Cat"
		bird.pv_max = 100
		bird.pv_current = 100
		bird.force = 3
		bird.vitesse = 9
		bird.intelligence = 1
		bird.precision = 5
		bird.esquive = 7
		return bird
	}
	//... -> faire la suite	
}
