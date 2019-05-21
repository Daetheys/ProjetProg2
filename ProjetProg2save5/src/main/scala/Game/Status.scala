package Status
import Attribute._
import Personnage._


abstract class Status(){
	var name:String
	var effect_desc:String
	var icon:String //10*10
	var personnage:Personnage
	
}

class Buff(p:Personnage) extends Status{ //Stats increase or decrease
	var name:String = null
	var active:Boolean = false
	var effect_desc:String = null
	var icon:String = null
	var personnage:Personnage = p
	var data:List[(Attribute,Int,Double,Double)] = List() //(Attribute to buff, amount, start of the buff, duration of the buff)
	
	var schedule:List[(Double,Attribute,Int)] = List() //(Time, attribute to buff, amount)
	
	def prepare_schedule()={
		//Creates the schedule so as to be more efficient from a point of view of complexity
		def create_actions(l:List[(Attribute,Int,Double,Double)]):List[(Double,Attribute,Int)]={ //From data a list of (Attribute,Amount,Start,Duration), il will create a new list of every action (increase and decrease after)
			l match {
				case List() => List()
				case t::q => 
					t._1 match {
						case p : PrimaryAttribute => (t._3,t._1,t._2)::(t._3+t._4,t._1,-t._2)::(create_actions(q)) //A primary attribute can't be definitly reduced that way
						case s : SecondaryAttribute => (t._3,t._1,t._2)::(create_actions(q))
					}
			}
		}
		val actions = create_actions(data)
		//Now it would be better to sort the list by the 1st component so as to have a better complexity
		def less1(x:(Double,Attribute,Int),y:(Double,Attribute,Int)) = { x._1 < y._1 }
		this.schedule = actions.sortWith(less1)
	}
	
	def run()={
		//This function will add an event which will execute the schedule and check if this.active is true
		this.prepare_schedule()
		this.active = true
		var counter = 0
		def event(typage:Unit):Int={
			if (!this.active || this.schedule.isEmpty) {this.personnage.remove_buff(this);return 0}
			counter += 1
			val macro_period:Double = this.personnage.jeton.Env.clock.macro_period
			val time_running:Double = macro_period * counter
			while (!(this.schedule.isEmpty) && time_running > this.schedule.head._1){
				//The effect must be applied
				p.apply_buff_effect(this.schedule.head._2,this.schedule.head._3)
				this.schedule = this.schedule.tail
				
			}
			return 1
		}
		this.personnage.jeton.Env.clock.add_macro_event(event)
	}
	
	def terminate()={
		//This function will end this buff on all it's components about PrimaryAttributes (if the game terminates while someone has a debuff it will remove it and refund points for PrimaryAttributes
		def terminate_func (e:(Double,Attribute,Int))={
			e._2 match {
				case p : PrimaryAttribute => this.p.apply_buff_effect(e._2,e._3)
				case s : SecondaryAttribute => () 
				}
		}
		this.schedule.foreach(terminate_func)
	}
	
	def end()={ //Terminates the buff -> Cures the debuff / Buff
		this.active = false
		this.terminate()
	}
}