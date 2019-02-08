package Competence
import Personnage.{Jeton=>Jeton,_}

class Competence (name:String){
	var owner = None //Personnage possedant cette compétence
	var jeton:Jeton //Objet jeton ayant cette compétence
	var v_int = scala.collection.mutable.Map[String,Int]() //Dictionnaire contenant un ensemble de variables utiles 
	var v_jeton = scala.collection.mutable.Map[String,Jeton]()
}

class Active (name:String) extends Competence(name){
	//Autocast
	var autocast = false
	val autocast_possible = false
	var func:Jeton=>Unit

	def activate(){
		this.func(this.jeton)
	}

	def autocast_enable(){
		this.autocast = true
	}

	def autocast_desable(){
		this.autocast = false
	}
}

class Passive (name:String) extends Competence(name){

}