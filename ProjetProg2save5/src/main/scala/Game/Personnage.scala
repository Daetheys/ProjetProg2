package Personnage
import Environnement.{Environnement=>Environnement}
import Competence.{Competence=>Competence,Active=>Active,Passive=>Passive}
import Movable.{Movable}
import Player._
import Graphics2._
import bddBehaviour._
import Inventory._
import Attribute._
import scala.math._

class Personnage {
	// Représente un personnage de manière générale

	//Stats principales
	var name:String = ""
	
	//Primary attributes
	var strength:Int = 50 //PV + dmg attaque
	var vivacity:Int = 50 //Vitesse depl + augmente un peu la cadence de tir
	var intelligence:Int = 50 //Dmg et un peu la précision
	var accuracy:Int = 50 //Precision, portée et un peu les dmg
	var dodge:Int = 50 //Chances d'esquive
	
	//Secondary attributes
	var pv_current:Int = 100
	
	//Deductible attributes
	var pv_max:Int = 100
	
	//Stuff
	var weapon : Option[Weapon] = None
	var armor : Option[Armor] = None
	var inventory:Array[Option[Item]] = Array(None, None, None, None, None, None)

	//Compétences
	var actives = scala.collection.mutable.Map[String,Active]()
	var passives = scala.collection.mutable.Map[String,Passive]()
	var call_when_spawn_list:List[String] = List()
	//Player
	var player:Player = new Player //Le joueur contrôlant l'unité
	
	var image_path:String = ""
	var sheet_image:String = ""
	
	var ia:Unit=>Int = bddBehaviour.dummy
	
	var jeton = new Jeton(this,new Environnement)
	
	
	//--------------------------------------------
	// Buffs
	def apply_buff_effect(att:Attribute,amount:Int)
	{
		att match {
			case Strength() => this.strength += amount
			case Vivacity() => this.vivacity += amount
			case Intelligence() => this.intelligence += amount
			case Accuracy() => this.accuracy += amount
			case Dodge() => this.dodge += amount
			case Health() => this.pv_current += amount
		}
		this.refresh_hp_max()
	}
	
	//--------------------------------------------
	// Attributes
	
	def get_strength():Int={
		return this.strength + 
		(this.armor match {
			case None => 0
			case Some(a) => a.bonus_strength
		})
	}
	
	def get_vivacity():Int={
		return this.vivacity + 
		(this.armor match {
			case None => 0
			case Some(a) => a.bonus_vivacity
		})
	}
	def get_intelligence():Int={
		return this.intelligence + 
		(this.armor match {
			case None => 0
			case Some(a) => a.bonus_intelligence
		})
	}
	def get_accuracy():Int={
		return this.accuracy + 
		(this.armor match {
			case None => 0
			case Some(a) => a.bonus_accuracy
		})
	}
	def get_dodge():Int={
		return this.dodge + 
		(this.armor match {
			case None => 0
			case Some(a) => a.bonus_dodge
		})
	}
	
	def get_cd_move_speed():Double={
		if (this.get_vivacity()==0){ return 10000 }
		return 100/(2.2*this.get_vivacity()+50)
	}
	
	def get_cd_melee_attack():Double={
		return this.get_vivacity().toDouble/50.0
	}
	
	def get_melee_attack_dmg():Double={
		return this.get_strength() / 4.0
	}
	
	def get_cd_attack_fact():Double={
		if (this.get_vivacity()==0){ return 10000 }
		return this.vivacity.toDouble/50.0
	}
	
	def get_attack_dmg_fact():Double={ //Will multiply effective damages of the weapon
		return (this.get_strength().toDouble/2 + this.get_intelligence().toDouble + this.get_accuracy().toDouble/3)/200.0
	}
	
	def get_hp_max():Int={
		return (this.get_strength()*5)
	}
	
	def get_dodge_chance():Double={
		return (this.get_dodge().toDouble*0.35)
	}
	
	def get_range_fact():Double={
		return (this.get_accuracy().toDouble*0.02)
	}
	
	def get_hit_chance():Double={
		def sigmoid(x:Double):Double =  { return 1/(1.0+exp(-1*x/50)) }
		return (50+50*sigmoid(this.get_accuracy().toDouble+this.get_intelligence().toDouble*2/3))
	}
	
	def refresh_hp_max():Unit={
		this.pv_max = this.get_hp_max()
	}
	
	def full_heal():Unit={
		this.pv_current = this.pv_max
	}
	
	//--------------------------------------------
	// Inventory
	def full_inventory() :Boolean ={
		for (opt <- this.inventory) {
			if (opt.isEmpty) { return false }
		}
		return true
	}
	def add_item(i : Item) = {
		var j = 0
		while (j < 6 && !(this.inventory(j).isEmpty)) {
				j += 1
		}
		if (j < 6) {
			this.inventory(j) = Some(i)
		}
	}
	
	def get_weapon() :Option[Weapon]= {
		return this.weapon
	}
	
	def get_armor() :Option[Armor]= {
		return this.armor
	}
	
	def equip_weapon(w : Weapon) = {
		if (this.weapon.isEmpty) {
			this.weapon = Some (w)
		}
	}
	def equip_armor(a : Armor) = {
		if (this.armor.isEmpty) {
			this.armor = Some (a)
		}
	}
	
	//--------------------------------------------
	// Skills
	def add_active(act:Active){
		this.actives(act.name) = act
	}
	//--------------------------------------------
	// General
	
	def take_hit(amount:Int){
		val r = scala.util.Random
		if (this.get_dodge_chance()  < r.nextDouble*100) 
			{ 
				val effective_damages = this.armor match {
											case None => amount
											case Some(armor) => (amount.toDouble*10.0/armor.defense.toDouble).toInt+1
											}
				this.take_damages(effective_damages)
			}
		else 
			{ 
				app.draw_dodge(this.jeton)
			}
	}
	
	def take_damages(amount:Int){
		this.pv_current -= amount
		app.draw_damages(amount,this.jeton)
		if (this.pv_current <= 0){
			this.jeton.died = true
			this.jeton.Env.remove_unit(this.jeton)
		}
	}
	//--------------------------------------------
	// Environment
	def add_spawn_call(e:String)={
		this.call_when_spawn_list = e::this.call_when_spawn_list
	}
	
	def call_when_spawn():Unit={
		this.call_when_spawn_list.map( (e:String) => this.actives(e).refresh(Array()) )
		this.refresh_hp_max()
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