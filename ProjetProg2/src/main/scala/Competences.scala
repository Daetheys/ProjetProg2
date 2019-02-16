package Competence
import Personnage.{Jeton=>Jeton,_}

abstract class Competence (name:String){
	var owner = None //Personnage possedant cette compétence
	var v_int = scala.collection.mutable.Map[String,Int]() //Dictionnaire contenant un ensemble de variables utiles 
	var v_jeton = scala.collection.mutable.Map[String,Jeton]()
}

class Active (name:String) extends Competence(name){
	//Autocast
	var autocast = false
	val autocast_possible = false

	var func:Unit=>Unit = Unit => ()

	def autocast_enable(){
		this.autocast = true
	}

	def autocast_disable(){
		this.autocast = false
	}
}

class Passive (name:String) extends Competence(name){

}