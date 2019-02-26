package Game

import Personnage.{Jeton=>Jeton,Personnage=>Personnage}
import Environnement.{Environnement=>Environnement,_}
import Graphics2.{app=>app}
import bddPersonnages.{bddPersonnages=>bddp}
import Schematics.{Tile=>Tile,Plan=>Plan}
import Mechanisms.{Sprite_plan=>Sprite_plan}
import Display.{All_sprites=>All_sprites}
import java.net.URL
import javax.sound.sampled._

object Game {

	var Env = new Environnement

	def initialize():Unit = {
		//Initialise la démo de combat
		//Transfert de l'environnement
		app.Env = this.Env
		
		//Chargement de l'objet contenant l'initialisation du terrain
		val plan = new Plan
		plan.random_fill
		val sprite_plan = new Sprite_plan(plan)
		sprite_plan.random_loot
		sprite_plan.init_sprites
		val all_sprites = new All_sprites(sprite_plan)
		this.Env.sprites = all_sprites.main_grid.transpose
		//print(scala.runtime.ScalaRunTime.stringOf(this.Env.sprites))
		val tiles = plan.grid.transpose //-> Ne sert plus que pour les collisions
		this.Env.tiles = tiles
		this.Env.sprite_plan = sprite_plan
		
		//Chargement des personnages
		all_sprites.load_demo_version1
		var personnages = all_sprites.personnages
		
		//Spawn des unités sur l'environnement
		for (i <- 0 to personnages.length-1){
			for (j <- 0 to personnages(i).length-1){
				this.Env.units(i)(j) = None
				personnages(i)(j) match {
					case None =>
					case Some(p:Personnage) => {this.Env.spawn_personnage(p,i,j)}
				}
			}
		}
		
		//Affichage de l'environnement
		app.load_commands()
		//app.load_colored_cursors() // --> Fait planter (je pense que cette fonctionnalité n'est pas encore bien implémentée dans ScalaFx)
		//app.load_images_environnement() // --> c'est une opti pas encore utile
		app.load_sprites() //-> Opti necessaire sinon java crash car trop de trucs a afficher
		
		// Affiche le terrain
		def aff_event(typage:Unit):Int={
			app.aff_all()
			return 1
		}
		
		//Chargement de l'event de raffraichissement de l'environnement
		Env.clock.add_micro_event(aff_event)
		
		//Préparation de l'event pour la loop -> les events ca sert a tout
		val timer = 30*10
		var time = 0
		def play_audio(typage:Unit):Int={
			println("event")
			if (time <= 0){
				println("start")
				val url = new URL(app.get_path("dash_runner.wav"))
				val audioIn = AudioSystem.getAudioInputStream(url)
				val clip = AudioSystem.getClip
				clip.open(audioIn)
				clip.start
				time = timer
				return 1
			} else { time -= 1
					return 1}
		}
		Env.clock.add_macro_event(play_audio)
			


		
		//Lance la clock et le jeu
		Env.start_clock()
	}
}