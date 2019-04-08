package Player
import Personnage._

class Player {
	val units:List[Personnage] = List()
	
	def lost()={
		this.units.forall( (e:Personnage) => !(e.jeton.died) )
	}
}