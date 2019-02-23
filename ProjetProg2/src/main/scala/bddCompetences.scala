package bddCompetences

import Competence.{Active => Active,Passive => Passive}
import Personnage.{Jeton=>Jeton,_}
import Environnement.{Environnement=>Environnement}


object bddCompetences {

	def create_move(jeton:Jeton,speed:Int,form:Int){
		//Speed est le nb de déplacement par seconde; form est la facon dont il se déplace
		// -> 0:ground 1:fly 2:ghost
		val Env = jeton.Env
		val move_comp = new Active("Move")
		def initialize(target:Array[Int]):Unit = {
			//La destination du mouvement est une variable dans la compétence
			move_comp.v_int("x_dest") = target(0)
			move_comp.v_int("y_dest") = target(1)
			Env.clock.add_macro_event(frequence_move)
		}
		def frequence_move(typage:Unit):Int={
			val macro_period = Env.clock.macro_period
			if (move_comp.v_int("nb_wait").toDouble*macro_period > 1.0/speed.toDouble){
				move_comp.v_int("nb_wait") = 0
				return move()
			} else {
				move_comp.v_int("nb_wait") += 1
				return 0 // On garde l'evenement dans la liste 
			}
		}
		def move():Int ={ //Ne fonctionne que sur un monde sans obstacles
			val x_dest = move_comp.v_int("x_dest")
			val y_dest = move_comp.v_int("y_dest")
			if (x_dest == jeton.x){
				if (y_dest == jeton.y){
					return 1
				}else{
					jeton.y += (y_dest - jeton.y) / Math.abs(y_dest - jeton.y)
					return 0
				}
			}else if (y_dest == jeton.y){
				jeton.x += (x_dest - jeton.x) / Math.abs(x_dest - jeton.x)
				return 0
			}else{
				jeton.x += (x_dest - jeton.x) / Math.abs(x_dest - jeton.x)
				jeton.y += (y_dest - jeton.y) / Math.abs(y_dest - jeton.y)
				return 0
			}
		}
		move_comp.initialize = initialize
		move_comp.autocast_disable()
		//Definition des destinations
		return move_comp
	}

	/*def create_autoattack(jeton:Jeton):Active = {
		// Autoattack Function
		val autoattack = new Active("AutoAttack")
		def func(typage:Unit):Unit ={
			var minimum = 10000
			var jeton_minimum:Option[Jeton] = None
			var dist = 0
			val Env = jeton.Env
			for (i<-0 to Env.units.length) {
				for (j<-0 to Env.units(i).length) {
					Env.units(i)(j) match {
						case None => ()
						case Some(s:Jeton) => dist = (s.x - jeton.x)^2 + (s.y - jeton.y)^2
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
		autoattack.func = func
		autoattack.autocast_enable()
		return autoattack
	}*/
}