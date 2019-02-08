package Personnage
import Environnement.{Environnement=>Environnement,_}

class Personnage {
	// Représente un personnage de manière générale

	//Stats principales
	val name:String = ""
	var pv_max:Int = 0
	var pv_current:Int = 0
	var ressource_max:Int = 0
	var ressource_current:Int = 0
	var ressource_name:String = "Mana"
	var speed:Float = 0 //Should be less or equal to 5
	var range:Int = -1 // Number of real tiles that can be attacked by the unit

	//Stuff
	var stuff = Map("head" -> None, "body" -> None, "gloves" -> None, "boots" -> None)

	//Compétences
	var competences = List()

	//Jeton link
	var jeton_link = None //Object jeton lié à ce personnage
}

class Jeton extends Personnage {
	// Personnage en mode Baston (avec emplacement sur le terrain et les effets)
	var x = -1
	var y = -1
	var player = -1 //Le joueur contrôlant l'unité -> 0 (joueur) // 1 (ia)
	var died = false

	//Status
	var status = None
}