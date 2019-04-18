package Environnement
import Personnage.{Jeton=>Jeton,Personnage=>Personnage}
import scala.collection.mutable.ListBuffer
import Schematics.{Tile=>Tile}
import Mechanisms.{Sprite_plan => Sprite_plan}
import Layer._
import scalafx.application.{Platform}
import Sound._
import Graphics2._
import Sprite._
import Game._
import Utilities._
import Display._

class Environnement {
	//Représente une carte
	val real_size_x = 25
	val real_size_y = 25
	val factor_x = 1
	val factor_y = 1
	val size_x = real_size_x*factor_x
	val size_y = real_size_y*factor_y
	var layerset = new LayerSet(size_x,size_y) //Sert à l'affichage graphique
	var tiles = Array.ofDim[Int](size_x,size_y)
	var units = Array.ofDim[Option[Jeton]](size_x,size_y)
	for (i<-0 to size_x-1){
		for (j<-0 to size_y-1){
			tiles(i)(j) = 0
			units(i)(j) = None
		}
	}
	var inv_tabs = new InventoryTabs
	var phase:Int = 0 //0-Phase de Baston 1-Phase de loot 2-Phase de repartition
	val clock = new Clock(this)
	var selected_unit:Option[Jeton] = None
	
	def start()={
		//Lancement du jeu dans l'environnement
		app.Env = this
		app.load_static_layers()
		app.load_commands()
		// Affiche le terrain
		def aff_event(typage:Unit):Int={
			app.load_refresh_layers()
			app.aff_layers()
			if (this.phase < 2) {app.aff_life_bars()} //On ne refresh pas les life bar en phase 3
			return 1
		}
		
		def check_win(typage:Unit):Int={
			if (Game.Human.lost()) { app.lose_screen(); return 0 } //Le joueur a perdu
			if (Game.IA.lost()) { this.phase = 1; return 0 } //Le joueur passe à la phase de loot
			return 1
		}
		//Chargement de l'event de raffraichissement de l'environnement
		this.clock.add_micro_event(aff_event(_))
		this.clock.add_macro_event(check_win(_))
		
		
		
		
		//Préparation de l'event pour la loop -> les events ca sert a tout
		val music = new Sound("dash_runner.wav")
		//music.loop_on()
		//music.play()
		
		this.start_clock()
	}
	
	def start_inventory_phase(){
		this.inv_tabs = new InventoryTabs
		this.inv_tabs.afficher()
	}
	
	def end_stage(){
		this.phase = -1
	}
	
	def apply_active(name:String,arg:Array[Int])={
		//L'unité séléctionnée lance la compétence dont le nom est spécifié avec les arguments donnés
		this.selected_unit match {
			case None => ()
			case Some(p:Jeton) => p.model.actives(name).refresh(arg)
			}
	}
	
	def start_clock()={
		//Lance la clock
		this.clock.launch()
	}
	
	def remove_unit(j:Jeton)={
		// Enleve le jeton spécifié des unités séléctionnées (utile en cas de décès)
		this.units(j.x)(j.y) = None
		selected_unit match {
				case None => ()
				case Some(j:Jeton) => if (j==this.selected_unit) { this.selected_unit = None } 
				}
		this.layerset.get_layer("Units").remove(j.located_sprite)
	}
	
	def dist(j1:Jeton,j2:Jeton):Double = {
		return Math.pow(Math.pow(j1.x-j2.x,2)+Math.pow(j1.y-j2.y,2),0.5)
	}
	
	def get_nearest_opposite_unit(jeton:Jeton):(Option[Jeton],Double)={
		// Renvoie le jeton ennemi le plus proche du jeton donné
		var minimum = 10000.0
		var jeton_minimum:Option[Jeton] = None
		var dist = -1.0
		for (i<-0 to this.units.length-1) {
			for (j<-0 to this.units(i).length-1) {
				this.units(i)(j) match {
					case None => ()
					case Some(s:Jeton) => 
						dist = this.dist(jeton,s)
						if (dist < minimum && dist != 0 && s.model.player != jeton.model.player){
							minimum = dist
							jeton_minimum = Some(s)
						}
				}
			}
		}
		return (jeton_minimum,minimum)
	}
	
	def unselect_unit()={
		// Deselectionne toutes les unités
		this.selected_unit match {
			case None => ()
			case Some(j:Jeton) =>  	j.selected = false
									this.selected_unit = None
		}
	}
	
	def move(x1:Int,y1:Int,x2:Int,y2:Int)={
		//Deplace un jeton d'un point (x1,y1) vers un point (x2,y2)
		this.units(x2)(y2) match {
			case None => 	this.units(x1)(y1) match {
							case None => println("No one is there -> no one to move")
							case Some(je:Jeton) =>  	this.units(x2)(y2) = this.units(x1)(y1)
														this.units(x1)(y1) = None
														je.x = x2
														je.y = y2
							}
			case Some(j:Jeton) => print("someone is already there")
			}
	}
	
	def select_unit(j:Jeton)={
		//Selectionne toutes les unités dans le rectangle spécifié
		this.unselect_unit()
		this.selected_unit = Some(j)
		j.selected = true
		//Affiche l'inventaire
		val s = new Sheet(j.model)
		s.afficher(this.layerset)
	}
	
	def tile_elem_effect(i:Int,j:Int,elem:Int)={
		//On va faire simple
		this.tiles(i)(j) += 1 + elem
		var file = "sprite_tile_water_"+(if (elem == 0) { "fire" } 
											else if (elem == 1) { "ice" } 
											else if (elem == 2) { "poison" } 
											else if (elem == 3) { "electricity" })
		val ls = new LocatedSprite(file)
		ls.x = i*32
		ls.y = j*32
		this.layerset.get_layer("UpTiles").add_sprite(ls) //C'est très pas beau (on n'a pas enlevé celui d'en dessous)
		this.layerset.get_layer("UpTiles").load_layer()
	}

	def spawn_personnage(personnage:Personnage,x:Int,y:Int):Unit={
		//Instancie un jeton à partir d'un personnage et le place sur le terrain
		if (this.units(x)(y) == None || this.units(x)(y) == null) {
			val jeton = new Jeton(personnage,this)
			jeton.x = x
			jeton.y = y
			personnage.player.add_unit(personnage)
			personnage.jeton = jeton
			this.units(x)(y) = Some(jeton)
			val ls = new LocatedSprite(personnage.image_path)
			ls.x = x*32
			ls.y = y*32
			this.layerset.get_layer("Units").add_sprite(ls)
			personnage.jeton.located_sprite = ls
			//Events de automatiques du jeton
			personnage.call_when_spawn()
			//Loading of AI
			def load_ia(typage:Unit):Int={
				if (personnage.jeton.died){
					return 0
				} else {
					personnage.ia()
					return 1
				}
			}
			this.clock.add_macro_event( load_ia )
		}else{
			 throw new IllegalArgumentException("Someone is already there"); //Il y a deja quelqu'un ici
		}
	}
}

class Clock(env:Environnement) {
	//Représente une horloge pour les phases de baston (permet de coordonner les events)
	val macro_period = 0.1 //Timer entre 2 actions majeures
	val micro_period = 0.01 //Timer entre 2 actions mineures
	var macro_events:ListBuffer[Unit=>Int] = ListBuffer()
	var micro_events:ListBuffer[Unit=>Int] = ListBuffer()
	var l:List[Int] = List()
	var active = true
	var Env = env
	
	private var thread_clock = new Thread
							
	private var nb_micro_loop:Int = 0
	
	def launch()={ //Attention le thread n'est pas tué quand on quitte l'application !!! (je ne sais pas comment faire)
		//Initialise et lance la clock
		val clock = this
		val Env = this.Env
		this.thread_clock = new Thread { 
								override def run { 
											while (true) {
												Platform.runLater{ //Instruction magique qui empeche le moteur graphique de planter :D !
													clock.iter_clock()
												}
												Thread.sleep((clock.micro_period*1000).toLong)
												if (Env.phase == -1){ return 0 }
												}
											}
										}
		this.thread_clock.start					
	}
	
	def iter_clock(){
		//Lance un tour de clock
		this.compute_micro_events()
		this.nb_micro_loop += 1
		if (this.micro_period * this.nb_micro_loop > this.macro_period) {
			this.compute_macro_events()
			this.nb_micro_loop = 0
		}
	}

	def add_macro_event(event:Unit=>Int){
		//Ajoute un macro event
		this.macro_events += event
	}

	def add_micro_event(event:Unit=>Int){
		//Ajoute un micro event
		this.micro_events += event
	}

	def iter_events(events_type:Period){
	//Execute les fonctions de this.events et les enleve quand elles n'existent plus (si elles renvoient un truc != 1)
		var events = (events_type match {
					case Micro() => this.micro_events
					case Macro() => this.macro_events})
		def apply(t:Unit=>Int) = {
				if (t()!=1){
					(events_type match {
						case Micro() => this.micro_events
						case Macro() => this.macro_events
					}) -= t
				}}
		events.map(apply(_)) 
	}

	def compute_macro_events(){
		//Execute les macro events
		this.iter_events(Macro())
	}

	def compute_micro_events(){
		//Execute les micro events
		this.iter_events(Micro())
	}
}