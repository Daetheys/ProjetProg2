import Competence.{Active => Active,Passive => Passive}
import Personnage.{Jeton=>Jeton,_}
import Environnement.{Environnement=>Env}


object bdd {

	def create_move(speed:Int,form:Int){
		//Speed est le nb de déplacement par seconde; form est la facon dont il se déplace
		// -> 0:ground 1:fly 2:ghost
		val move = new Active("Move")
		move.autocast = false
		//Definition des destinations
		move.v_int("x_dest") = 0
		move.v_int("y_dest") = 0
		def move_func(jeton:Jeton){
			//La destination du mouvement est une variable dans la compétence
		}
	}






	def create_autoattack():Active = {
		// Autoattack Function
		val autoattack = new Active("AutoAttack")
		autoattack.autocast_enable()
		def autoattack_func(jeton:Jeton) {
			var minimum = 10000
			var jeton_minimum:Option[Jeton] = None
			var dist = 0
			for (i<-0 to Env.units.length) {
				for (j<-0 to Env.units(i).length) {
					Env.units(i)(j) match {
						case None => ()
						case Some(s) => dist = Env.distance(s,jeton)
										if (dist < minimum && dist != 0){
											minimum = dist
											jeton_minimum = Some(s)
										}
					}
				}
			}
			jeton_minimum match {
				case None => ()
				case Some(s) => autoattack.v_jeton("attack_order") = s
			}
		}
		autoattack.func = autoattack_func
		return autoattack
	}
}