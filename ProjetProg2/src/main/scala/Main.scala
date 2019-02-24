package Game

import Personnage.{Jeton=>Jeton,Personnage=>Personnage}
import Environnement.{Environnement=>Environnement,_}
import Graphics2.{app=>app}
import bddPersonnages.{bddPersonnages=>bddp}

object Game {

	var Env = new Environnement

	def initialize():Unit = {
		app.initialize()
		val map = Array.ofDim[Int](this.Env.size_x,this.Env.size_y)
		var personnages = Array.ofDim[Option[Personnage]](this.Env.size_x,this.Env.size_y)
		for (i <- 0 to personnages.length-1){
			for (j <- 0 to personnages(i).length-1){
				personnages(i)(j) = None
			}
		}
		personnages(3)(3) = Some(bddp.create_turtle())
		personnages(15)(7) = Some(bddp.create_turtle())
		this.launch_fight(map,personnages)
		//app.main(args)
		print("end initialization\n")
	}
	def launch_fight(tiles:Array[Array[Int]],personnages:Array[Array[Option[Personnage]]]):Unit = {
		this.Env.tiles = tiles
		app.Env = Env
		for (i <- 0 to personnages.length-1){
			for (j <- 0 to personnages(i).length-1){
				this.Env.units(i)(j) = None
				personnages(i)(j) match {
					case None =>
					case Some(p:Personnage) => {this.Env.spawn_personnage(p,i,j)}
				}
			}
		}
		app.load_commands()
		def aff_event(typage:Unit):Int={
			app.aff_all()
			return 1
		}
		Env.clock.add_micro_event(aff_event)
		Env.start_clock()
		
	}
}