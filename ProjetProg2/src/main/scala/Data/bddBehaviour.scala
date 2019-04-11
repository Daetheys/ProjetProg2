package bddBehaviour
import Personnage._
import Game._
import scala.collection.mutable.{ListBuffer}
import Utilities.utils._
import bddPersonnages._
import Graphics2._
import Algo._

object bddBehaviour {
	
	def dummy(typage:Unit):Int={
		return 0
	}
	
	def create_rage(p:Personnage):Unit=>Int={
		//Attaque tout ce qui bouge et n'hesite pas a rattraper ses ennemis au corps à corps-> Pour les bourrins
		val aggro_range = 7 //Attaque l'unité la plus proche à moins de [aggro_range] cases
		var target:Option[Jeton] = None
		var target_move:(Int,Int) = (0,0)
		var count_move = 0
		val count_move_max = 20
		def scan_for_blood():Boolean={ //Cherche un ennemi a taper
			val h = p.jeton.Env.get_nearest_opposite_unit(p.jeton)
			val jeton_minimum = h._1
			val minimum = h._2
			jeton_minimum match {
				case None => return false
				case Some(j:Jeton) => if (minimum < aggro_range) { target = Some(j)}
										return true
			}
		}
		def go_for_blood(){
			//Se déplace vers la cible
			target match {
				case None => ()
				case Some(j:Jeton) => move(j.x,j.y)
					//Attack the target if it's in range 
					if (p.jeton.Env.dist(p.jeton,j) < p.actives("AutoAttack").v_int("range")) 
					{ p.actives("AutoAttack").v_jeton("target") = j }
				}
			}
		def move(x:Int,y:Int):Unit={
			count_move -= 1
			if (count_move == 0){
				get_move_target()
				count_move = count_move_max
			}
			p.actives("Move").refresh(Array(x,y))
		}
		def get_move_target(){
			val r = scala.util.Random
			val x = r.nextInt(p.jeton.Env.size_x)
			val y = r.nextInt(p.jeton.Env.size_y)
			target_move = (x,y)
		}
		def random_move():Unit={
			move(target_move._1,target_move._2)
		}
		def event(typage:Unit):Int={
			if (p.jeton.died) { return 0 }
			if (scan_for_blood()) { go_for_blood() } else { random_move() }
			return 1
		}
			
		return event
	}
	
	def create_flee(p:Personnage):Unit=>Int={
		//Fuit
		return this.dummy
	}
	
	def create_explosive(p:Personnage):Unit=>Int={
		//Go for blood and explode
		return this.dummy
	}
	
	def create_support(p:Personnage):Unit=>Int={
		//Aide ses coéquipiers
		return this.dummy
	}
	
	def create_hold(p:Personnage):Unit=>Int={
		//Ne bouge pas et attaque tout a sa portée
		return this.dummy
	}
	
	def create_shooter(p:Personnage):Unit=>Int={
		//Ne bouge pas et attaque tout a sa portée
		return this.dummy
	}
	
	def create_sentinel(p:Personnage):Unit=>Int={
		//Pattern 1 - Tir soutenu sur 2 membres de l'équipe du joueur (5sec)
		//			- Tir élémentaire (3sec) -> (poison, glace, feu, electricité) [loop]
		//			- Tir soutenu sur 2 membres de l'équipe du joueur (7sec)
		//Pattern 2 - Invocation des bots (15sec) (attaque,soin,explosion) [loop]
		//			- Tir soutenu sur 2 membres de l'équipe du joueur (5sec)
		//Pattern 3 - Mitraillette (attaque en cone dans une direction aléatoire)
		//			- Tir soutenu sur 2 membres de l'équipe du joueur (7sec)
		//Rotation : (1 2 3) [loop]
		
		var ref = 0
		val r = scala.util.Random
		
		def loop(typage:Unit):Unit=>Int={
			print("LOOP CALLED\n")
			var computed = false
			var chained_events:Unit=>Int = dummy_event
			def compute(){
				chained_events = chain(pattern_1((ref+1)%4) ::: pattern_2(ref%3) ::: pattern_3)
				computed = true
			}
			def event(typage:Unit):Int={
				if (!computed) { compute }
				if (chained_events() == 1) {return 1} else {print("---RESET---\n");p.jeton.Env.clock.add_macro_event(loop());return 0}
			}
			ref += 1
			return event(_)
		}
		
		def pattern_1(elem:Int):List[Unit=>Int]={ //0->3
			List(load_tir_soutenu(5.0,2),load_tir_elementaire(20,elem),load_tir_soutenu(5.0,2))
		}
		
		def pattern_2(phase:Int):List[Unit=>Int]={ //0->2
			List(load_invocation_bots(phase),load_tir_soutenu(5.0,2))
		}
		
		def pattern_3():List[Unit=>Int]={
			List(load_mitraillette(5,0.5,10),load_tir_soutenu(5.0,2))
		}
		
		def load_tir_soutenu(time:Double,nb_targets:Int):Unit=>Int={
			print("LOAD TIR SOUTENU\n")
			val sentinel = p.jeton
			val attack_speed = 0.5
			var array = Game.Human.units.toArray.toBuffer
			var targets:ListBuffer[Personnage] = ListBuffer()
			var targets_count = 0
			while (targets_count < nb_targets && array.length > 0){
				val target = array(r.nextInt(array.length))
				targets += target
				targets_count += 1
				array -= target
			}
			def attack(typage:Unit):Int={
				print("attack\n")
				targets.map( (t:Personnage) => shoot(sentinel.x,sentinel.y,t.jeton,1) )
				return 1
			}
			var count = (time/attack_speed).toInt
			def repeted_attack_limited_time (typage:Unit):Int={
				count -= 1
				if (count == 0){
					return 0
				} else {
					attack() //return déja un int
				}
			}
			cooldowned(repeted_attack_limited_time,attack_speed)
		}
		
		def load_tir_elementaire(dmg:Int,element:Int):Unit=>Int={ //0-Fire 1-Ice 2-Poison 3-Electricity
			print("---LOAD TIR ELEM\n")
			val sentinel = p.jeton
			val Env = sentinel.Env
			var array = Game.Human.units.toArray.toBuffer
			val target = array(r.nextInt(array.length))
			val dist = Math.pow(Math.pow(target.jeton.x-sentinel.x,2)+Math.pow(target.jeton.y-sentinel.y,2),0.5)
			val vecteur_dir = ((target.jeton.x-sentinel.x).toDouble/dist,(target.jeton.y-sentinel.y).toDouble/dist)
			val sg = if (target.jeton.x < sentinel.x) { -1 } else { 1} //Sert a renverset une inégalité plus loin
			def event(typage:Unit):Int={
				print("tir elem\n")
				target.take_damages(dmg)
				var count = 0
				while ( sg*(sentinel.x + vecteur_dir._1*count) < sg*target.jeton.x && sg*(sentinel.y + vecteur_dir._2*count) < sg*target.jeton.y){ //+1 car on veut etre sur que la case sur laquelle le personnnage se trouve va etre prise en compte
					Env.tile_elem_effect( (sentinel.x + vecteur_dir._1*count+0.5).toInt , (sentinel.y + vecteur_dir._2*count+0.5).toInt ,element)
					count += 1
				}
				//Animation
				val color = (if (element == 0) { scalafx.scene.paint.Color.Red } 
												else if (element == 1) { scalafx.scene.paint.Color.White } 
												else if (element == 2) { scalafx.scene.paint.Color.Purple } 
												else { scalafx.scene.paint.Color.Blue })
				app.draw_line(sentinel.x,sentinel.y,target.jeton.x,target.jeton.y,color,100)
				return 0
			}
			return event
		}
		
		def load_invocation_bots(phase:Int):Unit=>Int={
			print("LOAD INVOCATION BOTS\n")
			val Env = p.jeton.Env
			def spawn_attack_bots(area:List[(Int,Int)]){
				def apply(h:(Int,Int))={
					val hb = bddPersonnages.create_attackbot(Game.IA) 
					Env.spawn_personnage(hb,h._1,h._2)
				}
				area.map(apply(_))
			}
			
			def spawn_heal_bots(area:List[(Int,Int)]){
				def apply(h:(Int,Int))={
					val hb = bddPersonnages.create_healbot(Game.IA) 
					Env.spawn_personnage(hb,h._1,h._2)
				}
				area.map(apply(_))
			}
			
			def spawn_explosion_bots(area:List[(Int,Int)]){
				def apply(h:(Int,Int))={
					val hb = bddPersonnages.create_explosionbot(Game.IA) 
					Env.spawn_personnage(hb,h._1,h._2)
				}
				area.map(apply(_))
			}
			val areas = List((4,8),(8,8),(16,8),(20,8))
			def spawn(typage:Unit):Int={
				print("spawn\n")
				if (phase == 0){
					spawn_attack_bots(areas)
				} else if (phase == 1){
					spawn_heal_bots(areas)
				} else if (phase == 2){
					spawn_explosion_bots(areas)
				}
				return 0
			}
			return spawn
		}
		
		def load_mitraillette(dmg:Int,attack_speed:Double,nb_fire:Int):Unit=>Int={
			print("LOAD MITRAILLETTE\n")
			val sentinel = p.jeton
			val Env = sentinel.Env
			val cone_angle = 30.0 //30degrés de largeur pour le cone
			val cone_direction = (r.nextInt(90)-45).toDouble //Direction du Cone
			val a0 = Math.sin(cone_direction)/Math.cos(cone_direction)
			val a1 = Math.sin(cone_direction+cone_angle/2)/Math.cos(cone_direction+cone_angle/2)
			val a2 = Math.sin(cone_direction-cone_angle/2)/Math.cos(cone_direction-cone_angle/2)
			def is_in_cone(j:Jeton):Boolean={
				val x_center = sentinel.x
				val y_center = sentinel.y
				def f(a:Double):Double={
					return (-a)*(j.x-x_center)+y_center
				}
				return (f(a2) < j.y && j.y < f(a1))
			}
			var count = nb_fire
			def deal_damages(typage:Unit):Int={
				print("mitraillette - deal damages\n")
				//Dégats infligés
				count -= 1
				if (count == 0){
					return 0
				}
				for (i<-0 to Env.units.length-1){
					for (j<-0 to Env.units.length-1){
						Env.units(i)(j) match {
							case None => ()
							case Some(j:Jeton) => if (j != sentinel && is_in_cone(j)) {j.model.take_damages(dmg)}
						}
					}
				}
				//Animation
				val vecteur_dir1 = (Math.cos(cone_direction+cone_angle/2),Math.sin(cone_direction+cone_angle/2))
				val vecteur_dir2 = (Math.cos(cone_direction-cone_angle/2),Math.sin(cone_direction-cone_angle/2))
				val dist = 10.0
				val vecteur_final1 = (vecteur_dir1._1*dist,vecteur_dir1._2*dist)
				val vecteur_final2 = (vecteur_dir2._1*dist,vecteur_dir2._2*dist)
				val vecteur_trans = (vecteur_final2._1-vecteur_final1._1,vecteur_final2._2-vecteur_final1._2)
				val sub = 20
				val vecteur_trans_sub = (vecteur_trans._1/sub,vecteur_trans._2/sub)
				val nb = 15
				for (i<-0 to nb){
					val rdValue = r.nextInt(20)
					var vect_add = (vecteur_trans_sub._1*rdValue+vecteur_final1._1+sentinel.x,vecteur_trans_sub._2*rdValue+vecteur_final1._2+sentinel.y)
					app.draw_shoot_line(sentinel.x,sentinel.y,vect_add._1.toInt,vect_add._2.toInt)
				}
				return 1
			}
			cooldowned(deal_damages(_),attack_speed)
		}
		val result = loop()
		return result
	}
	
}