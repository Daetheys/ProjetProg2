package Environnement
import Personnage.{Jeton=>Jeton,_}

object Environnement {
	val real_size_x = 21
	val real_size_y = 15
	val factor_x = 3
	val factor_y = 3
	val size_x = real_size_x*factor_x
	val size_y = real_size_y*factor_y
	var tiles = Array.ofDim[Int](15*factor_x,21*factor_y)
	var units = Array.ofDim[Option[Jeton]](15*factor_x,21*factor_y)
	val clock = new Clock()

	def distance(j1:Jeton,j2:Jeton):Unit = {
		((j1.x - j2.x)^2 + (j1.y-j2.y)^2)
	}
}

class Clock {
	val macro_period = 0.1 //Timer entre 2 actions majeures
	val micro_period= 0.001 //Timer entre 2 actions mineures
	var macro_events:List[Unit=>Int]
	var micro_events:List[Unit=>Int]
	var active = true
	var stop = false

	def launch(){
		var macro_time = System.nanoTime
		var micro_time = System.nanoTime
		while (this.active){
			while (System.nanoTime - micro_time > this.micro_period) ()
			this.compute_micro_events()
			micro_time = System.nanoTime
			if (System.nanoTime - macro_time > this.macro_period) {
				this.compute_macro_events()
				macro_time = System.nanoTime
			}
			
		}	
	}

	def add_macro_event(event:Unit=>Int){
		this.macro_events = event :: this.macro_events
	}

	def add_micro_event(event:Unit=>Int){
		this.micro_events = event :: this.micro_events
	}

	def iter_events(events:List[Unit=>Int]):List[Unit=>Int] = { //Execute les fonctions de this.events et les enleve quand elles n'existent plus (si elles renvoient un truc != 1)
		events match {
				case t :: q => 	if (t()==1){
							t :: (this.iter_events(q))
						}
						else {
							this.iter_events(q)
						}
				case Nil => Nil
			}
	}

	def compute_macro_events(){
		this.macro_events = this.iter_events(this.macro_events)
	}

	def compute_micro_events(){
		this.micro_events = this.iter_events(this.micro_events)
	}
}