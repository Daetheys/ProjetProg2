package bddCompetences

import Competence.{Active => Active,Passive => Passive}
import Personnage.{Jeton=>Jeton,_}
import Environnement.{Environnement=>Environnement}
import Algo.{Algo=>Algo}
import Graphics2.{app=>app}


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
			if (p.jeton.died == false){
				val macro_period = p.jeton.Env.clock.macro_period
				if (move_comp.v_int("nb_wait").toDouble*macro_period > 1.0/speed.toDouble){
					move_comp.v_int("nb_wait") = 0
					return move()
				} else {
					move_comp.v_int("nb_wait") += 1
					return 1 // On garde l'evenement dans la liste 
				}
			} else { return 0} //On enleve l'event si l'unité est morte
		}
		def move():Int ={ //Ne fonctionne que sur un monde sans obstacles
			val x_dest = move_comp.v_int("x_dest")
			val y_dest = move_comp.v_int("y_dest")
			val x = p.jeton.x
			val y = p.jeton.y
			var mvt = Algo.astar(p.jeton.Env,p.jeton,Array(x_dest,y_dest))
			if (mvt(0) == x && mvt(1) == y){
				move_comp.v_int("active") = 0
				return 0 //On est arrivé
			} else {
				p.jeton.Env.move(x,y,mvt(0),mvt(1))
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

	def create_autoattack(personnage:Personnage,range:Int,dmg:Int,attack_speed:Int):Active = {
		// Autoattack Function
		val autoattack = new Active("AutoAttack")
		def initialize(typage:Array[Int]):Unit ={
			autoattack.v_int("compt") = 0
			personnage.jeton.Env.clock.add_macro_event(func)
		}
		def verify_los(x1:Int,y1:Int,x2:Int,y2:Int)={
		
		}
		def func(typage:Unit):Int={
			if (personnage.jeton.died == false){
				if (autoattack.v_int("compt").toDouble*attack_speed > 1/personnage.jeton.Env.clock.macro_period.toDouble){
					autoattack.v_int("compt") = 0
					var minimum = 10000
					var jeton_minimum:Option[Jeton] = None
					var dist = -1
					val Env = personnage.jeton.Env
					for (i<-0 to Env.units.length-1) {
						for (j<-0 to Env.units(i).length-1) {
							Env.units(i)(j) match {
								case None => ()
								case Some(s:Jeton) => dist = Math.pow(s.x - personnage.jeton.x,2).toInt + Math.pow(s.y - personnage.jeton.y,2).toInt
												if (dist < minimum && dist != 0 && s.model.player != personnage.player){
													minimum = dist
													jeton_minimum = Some(s)
												}
							}
						}
					}
					if (minimum <= Math.pow(range,2).toInt){
						jeton_minimum match {
							case None => ()
							case Some(s) => app.draw_shoot_line(personnage.jeton.x,personnage.jeton.y,s.x,s.y)
											app.draw_dmg_text(s.x,s.y,10,dmg)
											s.model.take_damages(dmg)
											
						}			
					}
				} else {
					autoattack.v_int("compt") += 1
				}
				return 1 //Avec cette implémentation on garde tout le temps l'event
			} else { return 0 } //Si le personnage est mort -> on ne fait pas l'event
		}
		autoattack.initialize = initialize
		autoattack.autocast_enable()
		return autoattack
	}
}