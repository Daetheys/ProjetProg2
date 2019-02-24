package bddCompetences

import Competence.{Active => Active,Passive => Passive}
import Personnage.{Jeton=>Jeton,_}
import Environnement.{Environnement=>Environnement}


object bddCompetences {

	def create_move(p:Personnage,speed:Int,form:Int):Active={
		//Speed est le nb de déplacement par seconde; form est la facon dont il se déplace
		// -> 0:ground 1:fly 2:ghost
		val move_comp = new Active("Move")
		// Initialisation des variables
		move_comp.v_int("x_dest") = 1
		move_comp.v_int("y_dest") = 1
		move_comp.v_int("nb_wait") = 0
		move_comp.v_int("active") = 0
		
		def initialize(target:Array[Int]):Unit = {
			//La destination du mouvement est une variable dans la compétence
			move_comp.v_int("x_dest") = target(0)
			move_comp.v_int("y_dest") = target(1)
			if (move_comp.v_int("active") == 0){
				move_comp.v_int("active") = 1
				p.jeton.Env.clock.add_macro_event(frequence_move)
			}
		}
		def frequence_move(typage:Unit):Int={
			val macro_period = p.jeton.Env.clock.macro_period
			if (move_comp.v_int("nb_wait").toDouble*macro_period > 1.0/speed.toDouble){
				move_comp.v_int("nb_wait") = 0
				return move()
			} else {
				move_comp.v_int("nb_wait") += 1
				return 1 // On garde l'evenement dans la liste 
			}
		}
		def move():Int ={ //Ne fonctionne que sur un monde sans obstacles
			print((move_comp.v_int("x_dest"),move_comp.v_int("y_dest")))
			print((p.jeton.x,p.jeton.y))
			val x_dest = move_comp.v_int("x_dest")
			val y_dest = move_comp.v_int("y_dest")
			val x = p.jeton.x
			val y = p.jeton.y
			if (x_dest == p.jeton.x){
				if (y_dest == p.jeton.y){
					move_comp.v_int("active") = 0
					return 0
				}else{
					p.jeton.Env.move(x,y,x,y+(y_dest - p.jeton.y) / Math.abs(y_dest - p.jeton.y))
					return 1
				}
			}else if (y_dest == p.jeton.y){
				p.jeton.Env.move(x,y,x+(x_dest - p.jeton.x) / Math.abs(x_dest - p.jeton.x),y)
				return 1
			}else{
				p.jeton.Env.move(x,y,x+(x_dest - p.jeton.x) / Math.abs(x_dest - p.jeton.x),y+(y_dest - p.jeton.y) / Math.abs(y_dest - p.jeton.y))
				return 1
			}
		}
		move_comp.initialize = initialize
		move_comp.autocast_disable()
		//Definition des destinations
		return move_comp
	}
	
	def create_attack(personnage:Personnage):Active = {
		val attack = new Active("Attack")
		def initialize(typage:Array[Int]):Unit = {
			
		}
		def is_in_range(jeton:Jeton){
			
		}
		return attack
	}

	def create_autoattack(personnage:Personnage):Active = {
		// Autoattack Function
		val autoattack = new Active("AutoAttack")
		def initialize(typage:Array[Int]):Unit ={
			var minimum = 10000
			var jeton_minimum:Option[Jeton] = None
			var dist = 0
			val Env = personnage.jeton.Env
			for (i<-0 to Env.units.length) {
				for (j<-0 to Env.units(i).length) {
					Env.units(i)(j) match {
						case None => ()
						case Some(s:Jeton) => dist = (s.x - personnage.jeton.x)^2 + (s.y - personnage.jeton.y)^2
										if (dist < minimum && dist != 0){
											minimum = dist
											jeton_minimum = Some(s)
										}
					}
				}
			}
			if (minimum < autoattack.v_int("range")){
				jeton_minimum match {
					case None => ()
					case Some(s) => autoattack.v_jeton("target") = s
				}			
			}
		}
		autoattack.initialize = initialize
		autoattack.autocast_enable()
		return autoattack
	}
}