package bddCompetences

import Competence.{Active => Active,Passive => Passive}
import Personnage.{Jeton=>Jeton,_}
import Environnement.{Environnement=>Environnement}
import Algo.{Algo=>Algo}
import Graphics2.{app=>app}
import scalafx.scene.paint.Color._
import Movable._
import Utilities.utils._

object bddCompetences {
	//Spells
	def create_move(p:Personnage,cd:Double,form:Int):Active={
		//Cooldown est le tps pour se déplacer d'une case; form est la facon dont il se déplace
		// -> 0:ground 1:fly 2:ghost
		val move_comp = new Active("Move")
		// Initialisation des variables
		move_comp.v_int("x_dest") = 1
		move_comp.v_int("y_dest") = 1
		move_comp.v_int("active") = 0
		
		def refresh(target:Array[Int]):Unit = {
			//La destination du mouvement est une variable dans la compétence
			move_comp.v_int("x_dest") = target(0)
			move_comp.v_int("y_dest") = target(1)
			if (move_comp.v_int("active") == 0){
				move_comp.v_int("active") = 1
				p.jeton.Env.clock.add_macro_event(cooldowned(event(_),cd))
			}
		}
		def event(typage:Unit):Int ={
			//Deplace le jeton (avec un astar, ...)
			if (p.jeton.died == false){
				val x_dest = move_comp.v_int("x_dest")
				val y_dest = move_comp.v_int("y_dest")
				val x = p.jeton.x
				val y = p.jeton.y
				var mvt = Algo.astar(p.jeton.Env,p.jeton,(x_dest,y_dest))
				if (mvt._1 == x && mvt._2 == y){
					move_comp.v_int("active") = 0
					return 0 //On est arrivé
				} else {
					p.jeton.move(p.jeton.corresponding_orient(x,y,mvt._1,mvt._2),cd)
					return 1
				}
			} else { return 0 }
		}
		move_comp.refresh = refresh
		move_comp.autocast_disable()
		//Definition des destinations
		return move_comp
	}
	

	def create_autoattack(personnage:Personnage,range:Int,dmg:Int,attack_speed:Double):Active = {
		// Renvoie la compétence d'attaque automatique
		val autoattack = new Active("AutoAttack")
		val Env = personnage.jeton.Env
		autoattack.v_jeton("target") = new Jeton(new Personnage,Env) //Un jeton quelconque qui ne pourra jamais etre attaqué
		
		def refresh(typage:Array[Int]):Unit ={
			get_new_target()
			personnage.jeton.Env.clock.add_macro_event(cooldowned(event(_),attack_speed))
		}
		
		def target_alive():Boolean={
			return !(autoattack.v_jeton("target").died)
		}
		
		def target_in_range():Boolean={
			return Env.dist(personnage.jeton,autoattack.v_jeton("target")) < range
		}
		
		def get_new_target()={
			//Recherche de l'ennemi le plus proche
			val result = personnage.jeton.Env.get_nearest_opposite_unit(personnage.jeton)
			val jeton_minimum = result._1
			val minimum = result._2
			//Verification qu'il est a portée et selection de la cible si c'est le cas
			if (minimum <= range){
				jeton_minimum match {
					case Some(s:Jeton) => autoattack.v_jeton("target") = s
					case None => ()
				}
			}
		}
		
		def event(typage:Unit):Int={
			//Fonction d'attaque
			if (personnage.jeton.died){
				return 0
			}
			if (target_alive() && target_in_range()){
				val s = autoattack.v_jeton("target")
				def delayed(typage:Unit):Int={
					s.model.take_damages(dmg)
					return 0
				}
				app.draw_shoot_line(personnage.jeton.x,personnage.jeton.y,s.x,s.y)
				app.Env.clock.add_micro_event(delay_micro(delayed(_),20*personnage.jeton.Env.clock.micro_period))
				return 1
			} else {
				get_new_target()
				return 1
			}	
		}
		
		autoattack.refresh = refresh
		autoattack.autocast_enable()
		return autoattack
	}
	
	def fire_spell(personnage:Personnage,power:Int,step_speed:Double){
		val orb = new Movable(personnage.jeton.Env)
		val orient = personnage.jeton.orientation
		def explose() = {
			for (i <- -1 to 1){
				for (j <- -1 to 1){
					if (0 <= orb.x + i && orb.x + i <= orb.Env.size_x -1 && 0 <= orb.y + j && orb.y + j <= orb.Env.size_y -1){
						orb.Env.units(orb.x+i)(orb.y+j) match {
							case Some(j:Jeton) => j.model.take_damages(power)
							case None => ()
						}
					}
				}
			}
		}
		def func(typage:Unit):Int = {
			orient match {
				case Left() =>
					if (1 <= orb.x && orb.Env.tiles(orb.x-1)(orb.y) != 1 && orb.Env.units(orb.x-1)(orb.y) != None) {
						orb.move(Left(),step_speed)
						return 1
					} else {
						explose()
						return 0
					}
				case Right() =>
					if (orb.x <= orb.Env.size_x - 2 && orb.Env.tiles(orb.x+1)(orb.y) != 1 && orb.Env.units(orb.x+1)(orb.y) != None) {
						orb.move(Right(),step_speed)
						return 1
					} else {
						explose()
						return 0
					}
				case Top() =>
					if (1 <= orb.y && orb.Env.tiles(orb.x)(orb.y-1) != 1 && orb.Env.units(orb.x)(orb.y-1) != None) {
						orb.move(Top(),step_speed)
						return 1
					} else {
						explose()
						return 0
					}
				case Bottom() =>
					if (orb.y <= orb.Env.size_y - 2 && orb.Env.tiles(orb.x)(orb.y+1) != 1 && orb.Env.units(orb.x)(orb.y+1) != None) {
						orb.move(Bottom(),step_speed)
						return 1
					} else {
						explose()
						return 0
					}
			}
		}
		personnage.jeton.Env.clock.add_macro_event(cooldowned(func(_),0.5))
	}
}