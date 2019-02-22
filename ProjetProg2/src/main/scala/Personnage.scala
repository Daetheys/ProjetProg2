package Personnage
import Environnement.{Environnement=>Environnement}
import Competence.{Competence=>Competence}

class Personnage {
	// Représente un personnage de manière générale

	//Stats principales
	var name:String = ""
	var pv_max:Int = 0
	var pv_current:Int = 0
	
	var force = 0 //PV + dmg attaque melee
	var vitesse = 0 //Vitesse depl + augmente un peu la cadence de tir (l'essentiel est sur l'arme)
	var intelligence = 0 //Dmg de loin
	var precision = 0 //Chances de toucher
	var esquive = 0 //Chances d'esquive
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
	var orientation:Int=0
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