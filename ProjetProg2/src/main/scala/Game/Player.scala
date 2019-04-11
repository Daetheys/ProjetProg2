package Player
import Personnage._
import scala.collection.mutable.{ListBuffer,ArrayBuffer}

class Player {
	val units:ArrayBuffer[Personnage] = ArrayBuffer()
	val nb_max_units = 6
	
	def lost()={
		this.units.forall( (e:Personnage) => (e.jeton.died) )
	}
	
	def add_unit(p:Personnage){
		if (this.units.length < this.nb_max_units){
			this.units += p
		} else {
			print("Too many units in the team : can't add more\n")
		}
	}
}