package Personnage
import Environnement.{Environnement=>Environnement}
import Competence.{Competence=>Competence,Active=>Active,Passive=>Passive}
import Movable.{Movable}
import Player._
import Graphics2._

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
	var stuff = Map("head" -> None, "body" -> None, "gloves" -> None, "boots" -> None, "weapon" -> None)

	//Compétences
	var actives = scala.collection.mutable.Map[String,Active]()
	var passives = scala.collection.mutable.Map[String,Passive]()
	var call_when_spawn_list:List[String] = List()
	//Player
	var player:Player = new Player //Le joueur contrôlant l'unité
	
	var image_path:String = ""
	
	var ia:Unit=>Unit = (e:Unit) => ()
	
	var jeton = new Jeton(this,new Environnement)
	
	def add_active(act:Active){
		this.actives(act.name) = act
	}
	def take_damages(amount:Int){
		this.pv_current -= amount
		app.draw_damages(amount,this.jeton)
		if (this.pv_current <= 0){
			this.jeton.died = true
			this.jeton.Env.remove_unit(this.jeton)
		}
	}
	
	def add_spawn_call(e:String)={
		this.call_when_spawn_list = e::this.call_when_spawn_list
	}
	
	def call_when_spawn():Unit={
		this.call_when_spawn_list.map( (e:String) => this.actives(e).refresh(Array()) )
	}
}

class Jeton(modell:Personnage,env:Environnement) extends Movable(env){
	// Personnage en mode Baston (avec emplacement sur le terrain et les effets)
	var died = false
	var model = modell
	//Attributs graphiques
	var selected:Boolean = false
	//Status
	var status = None
	
	override def set_position(x:Int,y:Int,t:Double){ //Permet de mettre a jour le tableau des jetons de Env en plus de modifier les attributs necessaires
		this.Env.units(this.x)(this.y) = None
		this.Env.units(x)(y) = Some(this)
		super.set_position(x,y,t)
	}
}