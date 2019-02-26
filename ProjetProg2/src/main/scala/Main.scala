package Game

import Personnage.{Jeton=>Jeton,Personnage=>Personnage}
import Environnement.{Environnement=>Environnement,_}
import Graphics2.{app=>app}
import bddPersonnages.{bddPersonnages=>bddp}
import Schematics.{Tile=>Tile,Plan=>Plan}
import Mechanisms.{Sprite_plan=>Sprite_plan}

object Game {

	var Env = new Environnement

	def initialize():Unit = {
		//Initialise la démo de combat
		val plan = new Plan
		plan.random_fill
		val sprite_plan = new Sprite_plan(plan)
		sprite_plan.init_sprites
		this.Env.sprites = sprite_plan.all_the_sprites.transpose
		val tiles = plan.grid.transpose
		var personnages = Array.ofDim[Option[Personnage]](this.Env.size_x,this.Env.size_y)
		for (i <- 0 to personnages.length-1){
			for (j <- 0 to personnages(i).length-1){
				personnages(i)(j) = None
			}
		}
		personnages(12)(3) = Some(bddp.create_bird(0))
		personnages(13)(3) = Some(bddp.create_bird(0))
		personnages(12)(7) = Some(bddp.create_turtle(1))
		personnages(12)(10) = Some(bddp.create_turtle(1))
		
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
		app.load_images_environnement() // --> c'est une opti pas encore utile
		app.load_sprites() //-> Opti necessaire sinon java crash car trop de trucs a afficher
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