package bddCompetences

import Competence.{Active => Active,Passive => Passive}
import Personnage.{Jeton=>Jeton,_}
import Environnement.{Environnement=>Environnement}
import Algo.{Algo=>Algo}
import Graphics2.{app=>app}
import scalafx.scene.paint.Color._
import Movable._
import Utilities.utils._
import Sprite._

object bddCompetences {
	//Spells
	def create_move(p:Personnage,form:Int):Active={
		//Cooldown est le tps pour se déplacer d'une case; form est la facon dont il se déplace
		// -> 0:ground 1:fly 2:ghost
		val move_comp = new Active("Move")
		// Initialisation des variables
		move_comp.v_int("x_dest") = 1
		move_comp.v_int("y_dest") = 1
		move_comp.v_int("active") = 0
		
		var count = 0
		
		def refresh(target:Array[Int]):Unit = {
			//La destination du mouvement est une variable dans la compétence
			move_comp.v_int("x_dest") = target(0)
			move_comp.v_int("y_dest") = target(1)
			if (move_comp.v_int("active") == 0){
				move_comp.v_int("active") = 1
				val cd = p.get_cd_move_speed()
				printf("refresh move %d\n",cd.toInt)
				p.jeton.Env.clock.add_macro_event(cooldowned(event(_),cd))
			}
		}
		
		def event(typage:Unit):Int ={
			//Deplace le jeton (avec un astar, ...)
			val cd = p.get_cd_move_speed()
			if (count < cd) { count += 1; return 1} else { count = 0 }
			val x_dest = move_comp.v_int("x_dest")
			val y_dest = move_comp.v_int("y_dest")
			if (p.jeton.died == false){
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
			} else { return 1 } //Vu que la vitesse de deplacement peut varier, on doit mettre cd a jour dans l'event
		}
		move_comp.refresh = refresh
		move_comp.autocast_disable()
		//Definition des destinations
		return move_comp
	}
	

	def create_autoattack(personnage:Personnage):Active = {
		// Renvoie la compétence d'attaque automatique
		val autoattack = new Active("AutoAttack")
		val Env = personnage.jeton.Env
		autoattack.v_jeton("target") = new Jeton(new Personnage,Env) //Un jeton quelconque qui ne pourra jamais etre attaqué
		val weapon = personnage.get_weapon()
		val range = weapon match {
							case Some(w) => (w.range*personnage.get_range_fact()).toInt
							case None => 2 //Melee attack
						}
		var count = 0
		
		autoattack.v_int("range") = range
		
		def refresh(typage:Array[Int]):Unit ={
			get_new_target()
			//print("refresh_attack\n");
			personnage.jeton.Env.clock.add_macro_event(event(_))
		}
		
		def target_alive():Boolean={
			return !(autoattack.v_jeton("target").died)
		}
		
		def target_in_range():Boolean={
			val weapon = personnage.get_weapon()
			val range = weapon match {
							case Some(w) => (w.range*personnage.get_range_fact()).toInt
							case None => 2 //Melee attack
			}
			return Env.dist(personnage.jeton,autoattack.v_jeton("target")) < range
		}
		
		def get_new_target()={
			//Recherche de l'ennemi le plus proche
			val weapon = personnage.get_weapon()
			val range = weapon match {
							case Some(w) => (w.range*personnage.get_range_fact()).toInt
							case None => 2 // Melee attack
			}
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
			val weapon = personnage.get_weapon()
			val cd_attack :Double = weapon match {
								case Some(w) => w.cd_attack*personnage.get_cd_attack_fact()
								case None => personnage.get_cd_melee_attack //Melee attack
								}
			if (count*personnage.jeton.Env.clock.macro_period < cd_attack) { count += 1; return 1} else { count = 0 }
			val range :Int= weapon match {
							case Some(w) => (w.range*personnage.get_range_fact()).toInt
							case None => 2 //Melee attack
						}
			if (personnage.jeton.died){
				return 0
			}
			//printf("target alive %b target in range %b range %d %s %d %d\n",target_alive(),target_in_range(),range,personnage.name,autoattack.v_jeton("target").x,autoattack.v_jeton("target").y)
			if (target_alive() && target_in_range()){
				val s = autoattack.v_jeton("target")
				weapon match {
						case Some(w) => val dmg = (w.force*personnage.get_attack_dmg_fact()).toInt
										shoot(personnage.jeton,s,dmg)
						case None => val dmg = (personnage.get_melee_attack_dmg()).toInt
										shoot(personnage.jeton,s,dmg) //Il y aura un tir laser mais ce n'est pas grave
						}
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
	
	def create_explode(personnage:Personnage,dmg:Int):Active={
		def refresh_fun(e:Array[Int]){
			personnage.jeton.Env.clock.add_macro_event(event_func(_))
		}
		
		def explose() = {
			val orb = personnage.jeton
			for (i <- -1 to 1){
				for (j <- -1 to 1){
					if (0 <= orb.x + i && orb.x + i <= orb.Env.size_x -1 && 0 <= orb.y + j && orb.y + j <= orb.Env.size_y -1){
						orb.Env.units(orb.x+i)(orb.y+j) match {
							case Some(j:Jeton) => 
								//Direct damages
								j.model.take_damages(dmg)
							case None => ()
						}
					}
				}
			}
		}
	
		def event_func(typage:Unit):Int={
			if (personnage.jeton.died) { return 0 }
			val h = personnage.jeton.Env.get_nearest_opposite_unit(personnage.jeton)
			if (h._2 <= 1.5) { explose() } //Explose a proximite
			return 1
		}
	
		val explode = new Active("Explode")
		explode.refresh = refresh_fun
		return explode
			
	}
	
	def orb_spell(p:Personnage,power:Int,tick_dmg:Int,nb_dot_tick:Int,tick_period:Double,step_speed:Double,elem:Int,file:String){
		val recharge_id = elem match { case 1 => 10 case 2 => 11 case 3 => 13 case 4 => 9 }
		print(p.inventory,p.is_in_inventory(recharge_id),recharge_id)
		if (p.is_in_inventory(recharge_id)) { p.remove_from_inventory(recharge_id) } else { return () }
		val orb = new Movable(p.jeton.Env)
		orb.x = p.jeton.x
		orb.y = p.jeton.y
		val ls = new LocatedSprite(file)
		ls.x = p.jeton.x*32
		ls.y = p.jeton.y*32
		p.jeton.Env.layerset.get_layer("Spells").add_sprite(ls)
		orb.located_sprite = ls
		orb.set_orientation(p.jeton.orientation)
		def explode() = {
			for (i <- -1 to 1){
				for (j <- -1 to 1){
					if (0 <= orb.x + i && orb.x + i <= orb.Env.size_x -1 && 0 <= orb.y + j && orb.y + j <= orb.Env.size_y -1){
						orb.Env.units(orb.x+i)(orb.y+j) match {
							case Some(j:Jeton) => 
								//Direct damages
								j.model.take_damages(power)
								//Dot damages
								var count_ignite = nb_dot_tick //Inglige 5 fois des dégats
								def ignite(typage:Unit):Int={
									count_ignite -= 1
									if (count_ignite == 0){
										return 0
									}
									j.model.take_damages(tick_dmg)
									return 1
								}
								j.Env.clock.add_macro_event(cooldowned(ignite,tick_period))
								
							case None => ()
						}
					}
				}
			}
			orb.Env.layerset.get_layer("Spells").remove(orb.located_sprite)
		}
		def func(typage:Unit):Int = {
			p.jeton.Env.tile_elem_effect(orb.x,orb.y,elem)
			orb.orientation match {
				case Left() =>
					if (1 <= orb.x && orb.Env.tiles(orb.x-1)(orb.y) != 1 && orb.Env.units(orb.x-1)(orb.y) == None) {
						orb.move(Left(),step_speed)
						return 1
					} else {
						explode()
						return 0
					}
				case Right() =>
					if (orb.x <= orb.Env.size_x - 2 && orb.Env.tiles(orb.x+1)(orb.y) != 1 && orb.Env.units(orb.x+1)(orb.y) == None) {
						orb.move(Right(),step_speed)
						return 1
					} else {
						explode()
						return 0
					}
				case Top() =>
					if (1 <= orb.y && orb.Env.tiles(orb.x)(orb.y-1) != 1 && orb.Env.units(orb.x)(orb.y-1) == None) {
						orb.move(Top(),step_speed)
						return 1
					} else {
						explode()
						return 0
					}
				case Bottom() =>
					if (orb.y <= orb.Env.size_y - 2 && orb.Env.tiles(orb.x)(orb.y+1) != 1 && orb.Env.units(orb.x)(orb.y+1) == None) {
						orb.move(Bottom(),step_speed)
						return 1
					} else {
						explode()
						return 0
					}
			}
		}
		p.jeton.Env.clock.add_macro_event(cooldowned(func(_),0.5))
	}
	def create_fire_spell(p:Personnage):Active={
		val fire_spell = new Active("Fire")
		fire_spell.refresh = (e:Array[Int]) => orb_spell(p,25,2,5,1.0,0.5,1,"sprite_charge_fire.png")
		return fire_spell
	}
	def create_ice_spell(p:Personnage):Active={
		val fire_spell = new Active("Ice")
		fire_spell.refresh = (e:Array[Int]) => orb_spell(p,30,0,0,0,0.5,2,"sprite_charge_ice.png")
		return fire_spell
	}
	def create_poison_spell(p:Personnage):Active={
		val fire_spell = new Active("Poison")
		fire_spell.refresh = (e:Array[Int]) => orb_spell(p,15,3,7,1.2,0.5,3,"sprite_charge_poison.png")
		return fire_spell
	}
	def create_electricity_spell(p:Personnage):Active={
		val fire_spell = new Active("Electricity")
		fire_spell.refresh = (e:Array[Int]) => orb_spell(p,20,0,0,0,0.5,4,"sprite_charge_electricity.png")
		return fire_spell
	}
	
}