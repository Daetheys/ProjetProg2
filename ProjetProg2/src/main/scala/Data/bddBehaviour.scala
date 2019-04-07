package bddBehaviour
import Personnage._

object bddBehaviour {
	
	def create_rage(j:Jeton):Unit=>Unit={
		//Attaque tout ce qui bouge et n'hesite pas a rattraper ses ennemis-> Pour les bourrins
		val aggro_range = 5 //Attaque l'unité la plus proche à moins de [aggro_range] cases
		return (e:Unit)=>()
	}
	
	def create_flee(j:Jeton):Unit=>Unit={
		//Fuit
		return (e:Unit)=>()
	}
	
	def create_support(j:Jeton):Unit=>Unit={
		//Aide ses coéquipiers
		return (e:Unit)=>()
	}
	
	def create_hold(j:Jeton):Unit=>Unit={
		//Ne bouge pas et attaque tout a sa portée
		return (e:Unit)=>()
	}
	
}