package Utilities
import Graphics2._
import Personnage._

//Helper functions
object utils {
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
		this.cooldowned_fun(func,time,Macro(),1)
	}
	
	def delay_micro(func:Unit=>Int,time:Double):Unit=>Int={
		this.cooldowned_fun(func,time,Micro(),1)
	}
	
	def chain(list_events:List[Unit=>Int]):Unit=>Int={ //-> Abus de prog fonctionelle
		def fold_fun(event:Unit=>Int,new_event:Unit=>Int):Unit=>Int={ //-> Recursivité complexe (pas hyper opti mais on ne lancera pas chain sur des listes trop grandes
			var closed = false
			def fun(typage:Unit):Int={
				if (closed || new_event() == 0){
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
		def delayed(typage:Unit):Int={
			target.model.take_damages(dmg)
			return 0
		}
		app.draw_shoot_line(x1,y1,target.x,target.y)
		app.Env.clock.add_micro_event(delay_micro(delayed(_),20*target.Env.clock.micro_period))
	}
}

abstract class Period
case class Micro() extends Period
case class Macro() extends Period