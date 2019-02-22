import Personnage.{Personnage=>Personnage}
import bddCompetences.{bddCompetences=>bddComp}

object bddPersonnages {
	
	def create_turtle():Personnage = {
		var turtle = new Personnage
		turtle.name = "Tortue"
		turtle.pv_max = 100
		turtle.pv_current = 100
		turtle.ressource_max = 0
		turtle.ressource_current = 0
		turtle.ressource_name = "Mana"
		return turtle
	}
	
	def create_bird():Personnage = {
		var bird = new Personnage
		bird.name = "Oiseau"
		bird.pv_max = 75
		bird.pv_current = 75
		bird.ressource_max = 20 // Il doit soigner -> Besoin de mana
		bird.ressource_current = 20
		bird.ressource_name = "Mana"
		return bird
	}
	//... -> faire la suite	
}