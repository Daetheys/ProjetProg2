package Donjon
import Environnement._
import Schematics._
import Mechanisms._
import Personnage._
import Game._
import Layer._
import bddLevel._
import Graphics2._
import Display._

class Donjon(taille:Int) {
	val profondeur:Int = taille
	val index_boss_stages:List[Int] = List(0)
	// Attention boss_stages va etre remise a List() dans la suite
	var boss_stages:List[(Array[Array[Int]],Array[Array[Option[Personnage]]],LayerSet)] = List(bddLevel.create_sentinel_level())
	val stages:Array[Environnement] = Array.ofDim[Environnement](profondeur)
	for (i<-0 to profondeur-1){
		if (index_boss_stages.contains(i)){
			val h = boss_stages.last
			val tiles = h._1
			var personnages = h._2
			var layerset = h._3
			val env = new Environnement
			env.tiles = tiles.transpose
			layerset.transpose()
			env.layerset = layerset
			this.personnages_spawn(env,personnages)
			stages(i) = env
			boss_stages = boss_stages.init
		} else {
			stages(i) = this.gen_level()
		}
	}
	
	var index_current_stage:Int = -1
	
	def start()={
		if (index_current_stage == profondeur){
			// WIN !!
			app.win_screen()
		} else {
			//TODO -> Il faut stop le thread
			val next_stage = this.get_next_stage()
			//next_stage.load()
			next_stage.start()
		}
	}
	
	def get_next_stage():Environnement={
		this.index_current_stage += 1
		return this.stages(index_current_stage)
	}
	
	private def personnages_spawn(Env:Environnement,personnages:Array[Array[Option[Personnage]]])={
		//Spawn des unités sur l'environnement
		for (i <- 0 to personnages.length-1){
			for (j <- 0 to personnages(i).length-1){
				Env.units(j)(i) = None
				personnages(i)(j) match {
					case None => ()
					case Some(p:Personnage) => Env.spawn_personnage(p,j,i)
				}
			}
		}
	}
	
	private def gen_level():Environnement={
		//Chargement de l'objet contenant l'initialisation du terrain -> Marche pas 
		val Env = new Environnement
		/*val plan = new Plan
		var sprite_plan = new Sprite_plan(plan);
  		sprite_plan.random_loot();
  		sprite_plan.fill_layer_0135();
		val display = new All_sprites(sprite_plan)
		Env.layerset = sprite_plan.everything
		val personnages = display.personnages
		this.personnages_spawn(Env,personnages)
		Env.tiles = sprite_plan.tile_type*/
		return Env
	}
}