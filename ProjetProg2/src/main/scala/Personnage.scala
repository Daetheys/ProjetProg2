package Personnage
import Environnement.{Environnement=>Environnement}
import Competence.{Competence=>Competence}

class Personnage {
	// Représente un personnage de manière générale

	//Stats principales
	var name:String = ""
	var pv_max:Int = 0
	var pv_current:Int = 0
	var ressource_max:Int = 0
	var ressource_current:Int = 0
	var ressource_name:String = "Mana"
	//Stuff
	var stuff = Map("head" -> None, "body" -> None, "gloves" -> None, "boots" -> None)

	//Compétences
	var competences:List[Competence] = List()
	
	def add_competence(comp:Competence){
		this.competences = comp::this.competences
	}
}

class Jeton(model:Personnage,env:Environnement) extends Personnage {
	// Personnage en mode Baston (avec emplacement sur le terrain et les effets)
	var player = -1 //Le joueur contrôlant l'unité -> 0 (joueur) // 1 (ia)
	var died = false

	//Attributs graphiques
	var x:Int=0
	var y:Int=0
	var Env:Environnement = env

	//Status
	var status = None

	def init() {
		//Copy Personnage
		this.name = model.name
		this.pv_max = model.pv_max
		this.pv_current = model.pv_current
		this.ressource_max = model.ressource_max
		this.ressource_current = model.ressource_current
		this.ressource_name = model.ressource_name
		this.stuff = model.stuff
		this.competences = model.competences
	}
	def position(x:Int,y:Int){
		this.x = x
		this.y = y
	}
}