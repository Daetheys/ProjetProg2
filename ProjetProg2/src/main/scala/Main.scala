package Game

import Personnage.{Jeton=>Jeton,Personnage=>Personnage}
import Environnement.{Environnement=>Environnement,_}
import Graphics2.{app=>app}
import bddPersonnages.{bddPersonnages=>bddp}
import Schematics.{Tile=>Tile,Plan=>Plan}

object Game {

	var Env = new Environnement

	def initialize():Unit = {
		val plan = new Plan
		plan.random_fill
		val map = plan.grid.transpose
		var personnages = Array.ofDim[Option[Personnage]](this.Env.size_x,this.Env.size_y)
		for (i <- 0 to personnages.length-1){
			for (j <- 0 to personnages(i).length-1){
				personnages(i)(j) = None
			}
		}
		personnages(12)(3) = Some(bddp.create_bird(0))
		personnages(12)(7) = Some(bddp.create_turtle(1))
		personnages(12)(10) = Some(bddp.create_turtle(1))
		this.launch_fight(map,personnages)
		print("end initialization\n")
	}
	def launch_fight(tiles:Array[Array[Tile]],personnages:Array[Array[Option[Personnage]]]):Unit = {
		this.Env.tiles = tiles
		app.Env = this.Env
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
		//app.load_colored_cursors() // --> Fait planter (je pense que cette fonctionnalité n'est pas encore bien implémentée dans ScalaFx)
		//app.load_images_environnement() // --> c'est une opti pas encore utile
		// Affiche le terrain
		def aff_event(typage:Unit):Int={
			app.aff_all()
			return 1
		}
		Env.clock.add_micro_event(aff_event)
		//Lance la clock
		Env.start_clock()
		//app.aff_message("Bienvenue dans [Nom du jeu] ! Voici une démonstration du moteur d'affrontements du jeu. Merci de ne pas agrandir la feunetre pour eviter les bugs")
		
	}
}