package Environnement
import Personnage.{Jeton=>Jeton,Personnage=>Personnage}
import scala.collection.mutable.ListBuffer


class Environnement {
	val real_size_x = 25
	val real_size_y = 25
	val factor_x = 1
	val factor_y = 1
	val size_x = real_size_x*factor_x
	val size_y = real_size_y*factor_y
	var tiles = Array.ofDim[Int](real_size_x*factor_x,real_size_y*factor_y)
	var units = Array.ofDim[Option[Jeton]](real_size_x*factor_x,real_size_y*factor_y)
	val clock = new Clock(this)
	var selected_units:List[Jeton] = List()
	
	def apply_active(name:String,arg:Array[Int])={
		this.selected_units.map((j:Jeton) => j.model.actives(name).initialize(arg))
	}
	
	def start_clock()={
		this.clock.launch()
	}
	
	def unselect_all_units()={
		this.selected_units = List()
		for (i <- 0 to this.units.length -1){
			for (j <- 0 to this.units(i).length -1){
				this.units(i)(j) match{
					case None => ()
					// Pas opti mais on verra plus tard
					case Some (j) =>{
						j.selected = false
					 }
				}
			}
		}
	}
	
	def move(x1:Int,y1:Int,x2:Int,y2:Int)={
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
	
	def select_units(x1:Int,y1:Int,x2:Int,y2:Int)={
		this.unselect_all_units()
		for (i <- Math.min(x1,x2) to Math.max(x1,x2)){
			for (j <- Math.min(y1,y2) to Math.max(y1,y2)){
				this.units(i)(j) match{
					case None =>
					// Pas opti mais on verra plus tard
					case Some (j) =>{
						if (j.model.player == 0){
							this.selected_units = j::this.selected_units
							j.selected = true
						}
					 }
				}
			}
		}
	}

	def spawn_personnage(personnage:Personnage,x:Int,y:Int){
		if (this.units(x)(y) == None || this.units(x)(y) == null) {
			val jeton = new Jeton(personnage,this)
			jeton.x = x
			jeton.y = y
			personnage.jeton = jeton
			this.units(x)(y) = Some(jeton)
		}else{
			 throw new IllegalArgumentException("Someone is already there"); //Il y a deja quelqu'un ici
		}
	}
	
	def distance(j1:Jeton,j2:Jeton):Int = {
		((j1.x - j2.x)^2 + (j1.y-j2.y)^2)
	}
}

class Clock(env:Environnement) {
	val macro_period = 0.1 //Timer entre 2 actions majeures
	val micro_period = 0.01 //Timer entre 2 actions mineures
	var macro_events = ListBuffer[Unit=>Int]()
	var micro_events = ListBuffer[Unit=>Int]()
	var active = true
	var Env = env
	
	private var thread_clock = new Thread
							
	private var nb_micro_loop:Int = 0
	
	def launch()={ //Attention le thread n'est pas tué quand on quitte l'application !!! (je ne sais pas comment faire)
		val clock = this
		this.thread_clock = new Thread { 
								override def run { 
											while (true) {
												clock.iter_clock()
												Thread.sleep((clock.micro_period*1000).toLong)
												}
											}
										}
		this.thread_clock.start					
	}
	
	def start()={
		this.thread_clock.start
	}
	
	def stop()={
		this.thread_clock.stop
	}
	def iter_clock(){
		this.compute_micro_events()
		this.nb_micro_loop += 1
		if (this.micro_period * this.nb_micro_loop > this.macro_period) {
			this.compute_macro_events()
			this.nb_micro_loop = 0
		}
	}

	def add_macro_event(event:Unit=>Int){
		this.macro_events += event
	}

	def add_micro_event(event:Unit=>Int){
		this.micro_events += event
	}

	def iter_events(events:ListBuffer[Unit=>Int]):ListBuffer[Unit=>Int] = { //Execute les fonctions de this.events et les enleve quand elles n'existent plus (si elles renvoient un truc != 1)
		events match {
				case t +: q => 	
						if (t()==1){
							t +: (this.iter_events(q))
						}
						else {
							this.iter_events(q)
						}
				case ListBuffer() => ListBuffer()
			}
	}

	def compute_macro_events(){
		this.macro_events = this.iter_events(this.macro_events)
	}

	def compute_micro_events(){
		this.micro_events = this.iter_events(this.micro_events)
	}
}