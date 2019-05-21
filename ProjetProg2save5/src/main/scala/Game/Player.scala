package Player
import Personnage._
import scala.collection.mutable.{ListBuffer,ArrayBuffer}
import Inventory._

class Player {
	val units:ArrayBuffer[Personnage] = ArrayBuffer()
	val nb_max_units = 10000
	var inventory = new mainInventory(this)
	
	def lost():Boolean={
		return this.units.forall( (e:Personnage) => (e.jeton.died) )
	}
	
	def full_heal()={
		this.units.foreach( (e:Personnage) => e.full_heal() )
	}
	
	def add_unit(p:Personnage){
		println("add_unit",p.name)
		if (this.units.length < this.nb_max_units){
			this.units += p
		} else {
			print("Too many units in the team : can't add more\n")
		}
	}
}