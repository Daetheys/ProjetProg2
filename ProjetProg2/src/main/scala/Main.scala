package Game

import Personnage.{Jeton=>Jeton,Personnage=>Personnage}
import Environnement.{Environnement=>Environnement,_}
import Graphics2.{app=>app}

object Game {

	def initialize():Unit = {
		app.initialize()
		val map = Array.ofDim[Int](15,21)
		var personnages = Array.ofDim[Option[Personnage]](15,21)
		for (i <- 0 to personnages.length-1){
			for (j <- 0 to personnages(i).length-1){
				personnages(i)(j) = None
			}
		}
		var perso1 = new Personnage
		perso1.pv_max = 50
		perso1.pv_current = 20
		personnages(3)(3) = Some(perso1)
		this.launch_fight(map,personnages)
		//app.main(args)
		print("end")
	}
	def launch_fight(tiles:Array[Array[Int]],personnages:Array[Array[Option[Personnage]]]):Unit = {
		val Env = new Environnement
		Env.tiles = tiles
		app.Env = Env
		for (i <- 0 to personnages.length-1){
			for (j <- 0 to personnages(i).length-1){
				Env.units(i)(j) = None
				personnages(i)(j) match {
					case None =>
					case Some(p:Personnage) => {Env.spawn_personnage(p,i,j)}
				}
			}
		}
		app.aff_all()
	}
}