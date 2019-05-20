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
import bddCompetences.bddCompetences._
import scala.collection.mutable.ListBuffer
import Status._
import bddStatus.bddStatus._

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
	
	var buffs:ListBuffer[Buff] = ListBuffer()
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
			case Health() => this.take_damages(-1*amount)
		}
		this.refresh_hp_max()
	}
	
	def add_buff(b:Buff){
		this.buffs += b
		b.run()
	}
	
	def remove_buff(b:Buff){
		def func(e:Buff) :Boolean= {if (e == b) { return true } else { return false }}
		val buff = this.buffs.find(func)
		buff match {
			case None => ()
			case Some(b) => b.end() //Ends the buff
							this.buffs -= b
			}
	}
	
	def find_buff_name(name:String):Buff={
		for (i<-0 to this.buffs.length-1){
			if (this.buffs(i).name == name){
				return this.buffs(i) 
			}
		}
		return null
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
	
	def is_in_inventory(i : Int) : Boolean = {
		this.inventory.exists( (e:Option[Item]) => e match { case None => false case Some(f) => f.id == i } )
	}
	
	def remove_from_inventory(o:Item) : Unit= {
		for (i<-0 to this.inventory.length-1){
			this.inventory(i) match {
				case Some(j : Item) => if (j == o) {this.inventory(i) = None;return ()}
				case _ => ()
			}
		}
		print("NOTHING TO REMOVE\n")
	}
	
	def remove_from_inventory(i : Int) : Unit= {
		for (j <- 0 to this.inventory.length-1){
			this.inventory(j) match {
				case None => ()
				case Some(item) => 	if (item.id == i) {
										this.inventory(j) = None
										return ()
									}
			}
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
			//Ajout de la compétence
			w.powerA match {
				case "fire_spell" => this.actives("Spell_Ball") = create_fire_spell(this)
				case "ice_spell" => this.actives("Spell_Ball") = create_ice_spell(this)
				case "poison_spell" => this.actives("Spell_Ball") = create_poison_spell(this)
				case "electricity_spell" => this.actives("Spell_Ball") = create_electricity_spell(this)
			}
		}
	}
	def equip_armor(a : Armor) = {
		if (this.armor.isEmpty) {
			this.armor = Some (a)
		}
		this.refresh_hp_max()
	}
	
	def swap_weapon(i: Int) = {
		// Swap the weapon with index i with the one equipped
		if (this.weapon.isEmpty) {
			this.inventory(i) match {
				case Some(w : Weapon) => 	this.equip_weapon(w)
											this.inventory(i) = None
				case x => ()
			}
		} else {
			this.inventory(i) match {
				case Some(w : Weapon) => this.inventory(i) = this.weapon
										this.weapon = None
										this.equip_weapon(w)
				case x => ()
			}
		}
	}
	
	def swap_armor(i: Int) = {
		// Swap the weapon with index i with the one equipped
		if (this.armor.isEmpty) {
			this.inventory(i) match {
				case Some(a : Armor) => 	this.armor = Some(a)
											this.inventory(i) = None
				case x => ()
			}
		} else {
			this.inventory(i) match {
				case Some(a : Armor) => val swap = this.armor
										this.armor = Some(a)
										this.inventory(i) = swap
				case x => ()
			}
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
		//check water interactions
		this.Env.tiles(this.x)(this.y) match {
			case 5 => 
			print(this.model.buffs,this.model.find_buff_name("Poison"),"\n")
			if (this.model.find_buff_name("Poison") == null) {this.model.add_buff(create_poison(this.model))} //Add a buff for every move
			case 6 => this.model.take_damages(5) //Electricity -> damages every move
			case x => ()
		}
	}
}