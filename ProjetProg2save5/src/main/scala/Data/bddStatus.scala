import Attribute._
import Personnage._
import Status._

object bddStatus {

	def create_strength_up(p:Personnage):Buff={
		var buff = new Buff(p)
		buff.name = "Strength up"
		buff.effect_desc = "Increases strength by 5 points for 10 seconds"
		buff.data = List((Strength(),5,0.0,10.0))
		return buff
	}
	
	def create_late_heal(p:Personnage):Buff={
		var buff = new Buff(p)
		buff.name = "Late Heal"
		buff.effect_desc = "Heals for 10hp 4 seconds later"
		buff.data = List((Health(),10,4.0,0.0))
		return buff
	}
}