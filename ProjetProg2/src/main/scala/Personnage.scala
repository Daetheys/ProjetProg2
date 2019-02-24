package Personnage
import Environnement.{Environnement=>Environnement}
import Competence.{Competence=>Competence,Active=>Active,Passive=>Passive}

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
	var actives = scala.collection.mutable.Map[String,Active]()
	var passives = scala.collection.mutable.Map[String,Passive]()
	
	var jeton = new Jeton(this,new Environnement)
	
	def add_active(act:Active){
		this.actives(act.name) = act
	}
}

class Jeton(modell:Personnage,env:Environnement){
	// Personnage en mode Baston (avec emplacement sur le terrain et les effets)
	var player = -1 //Le joueur contrôlant l'unité -> 0 (joueur) // 1 (ia)
	var died = false
	var model = modell

	//Attributs graphiques
	var x:Int=0
	var y:Int=0
	var orientation:Int=0
	var Env:Environnement = env
	var selected:Boolean = false
	//Status
	var status = None

	def position(x:Int,y:Int){
		this.x = x
		this.y = y
	}
}