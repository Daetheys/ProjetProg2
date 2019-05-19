package Utilities
import Graphics2._
import Personnage._
import bddItems.bddItem._
import bddPersonnages.bddPersonnages._
import Inventory._
import Game._

//Helper functions
object utils {
	val animal:Array[String] = Array("bees","bird","cat","monkey","rabbit","snake")
  	val stuff:Array[String] = Array("gun_confusion","gun_electricity","gun_fire","gun_ice",
                    "gun_ink","gun_poison","vest_confusion","vest_electricity",
                    "vest_fire","vest_ice","vest_ink","vest_poison")
	def cooldowned_fun(func:Unit=>Int,cd:Double,period:Period,base_arg:Int):Unit=>Int={ //Considere que func est appelée toutes les macro_period
		//Monade pour ajouter un cooldown a des compétences -> déplacement plus lent // Attaque plus lente // ...
		var base = base_arg
		def cooldown (e:Unit):Int = {
			//print(cd.toString+"\n")
			//print(func.toString+"\n")
			val compare = base*( period match { 
				case Micro() => app.Env.clock.micro_period 
				case Macro() => app.Env.clock.macro_period 
				} )
			if (compare >= cd) {
				base = 1
				return func(e)
			}else{
				base += 1
				return 1
			}
		}
		return cooldown
	}
	
	def cooldowned(func:Unit=>Int,cd:Double):Unit=>Int={ //fresh initialement
		this.cooldowned_fun(func,cd,Macro(),cd.toInt+1)
	}
	
	def cooldowned_micro(func:Unit=>Int,cd:Double):Unit=>Int={
		this.cooldowned_fun(func,cd,Micro(),cd.toInt+1)
	}
	
	def delay(func:Unit=>Int,time:Double):Unit=>Int={ //Pas fresh initialement
		this.cooldowned_fun(func,time,Macro(),0)
	}
	
	def delay_micro(func:Unit=>Int,time:Double):Unit=>Int={
		this.cooldowned_fun(func,time,Micro(),0)
	}
	
	def chain(list_events:List[Unit=>Int]):Unit=>Int={ //-> Abus de prog fonctionelle
		def exec_event(event:Unit=>Int):Unit=>Boolean={ //-> Boxage
			var closed = false
			def box(typage:Unit):Boolean={
				if (closed) { return true } else { if (event() == 1) {return false } else {closed = true;return true}}
			}
			return box
		}
		def fold_fun(event:Unit=>Int,new_event:Unit=>Int):Unit=>Int={ //-> Recursivité complexe (pas hyper opti mais on ne lancera pas chain sur des listes trop grandes
			var closed = false
			def fun(typage:Unit):Int={
				if (closed || new_event() == 0){ //-> Surapplication
								closed = true
								return event()
				} else { 
								return 1
				}
			}
			return fun
		}
		def init_fun(typage:Unit):Int={
			return 0
		}
		return list_events.reverse.fold(init_fun(_))(fold_fun(_,_))
	}
	
	def dummy_event(typage:Unit):Int={
		return 0
	}
	
	def shoot(x1:Int,y1:Int,target:Jeton,dmg:Int)={
		//Utilisé pour les interactions de l'environnement
		def delayed(typage:Unit):Int={
			target.model.take_hit(dmg)
			return 0
		}
		app.draw_shoot_line(x1,y1,target.x,target.y)
		app.Env.clock.add_micro_event(delay_micro(delayed(_),20*target.Env.clock.micro_period))
	}
	
	def shoot(attacker:Jeton,target:Jeton,dmg:Int)={
		//Utilisé pour les combats entre jetons
		def delayed(typage:Unit):Int={
			val r = scala.util.Random
			printf("hit chance %f %d ",attacker.model.get_hit_chance(),dmg)
			if (attacker.model.get_hit_chance() >= r.nextDouble()*100)
				{target.model.take_hit(dmg)} else { app.draw_miss(target) }
			return 0
		}
		app.draw_shoot_line(attacker.x,attacker.y,target.x,target.y)
		app.Env.clock.add_micro_event(delay_micro(delayed(_),20*target.Env.clock.micro_period))
	}
	
	def get_object_stuff(s:Int):Item={
    	this.stuff(s) match {
    		case "gun_confusion" => create_gun_confusion()
    		case "gun_electricity" => create_gun_electricity()
    		case "gun_fire" => create_gun_fire()
    		case "gun_ice" => create_gun_ice()
    		case "gun_ink" => create_gun_ink()
    		case "gun_poison" => create_gun_poison()
    		case "vest_confusion" => create_vest_confusion()
    		case "vest_electricity" => create_vest_electricity()
    		case "vest_fire" => create_vest_fire()
    		case "vest_ice" => create_vest_ice()
    		case "vest_ink" => create_vest_ink()
    		case "vest_poison" => create_vest_poison()
    	}
    }
    
    def get_object_animal(a:Int):Personnage={
    	val h = Game.Human
    	this.animal(a) match {
    		case "bees" => create_bees(h)
    		case "bird" => create_bird(h)
    		case "cat" => create_cat(h)
    		case "monkey" => create_monkey(h)
    		case "rabbit" => create_rabbit(h)
    		case "snake" => create_snake(h)
    	}
    }
}

abstract class Period
case class Micro() extends Period
case class Macro() extends Period