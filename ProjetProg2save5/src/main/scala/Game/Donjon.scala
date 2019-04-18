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
	val index_boss_stages:List[Int] = List(2)
	var index_current_stage:Int = -1
	// Attention boss_stages va etre remise a List() dans la suite
	var boss_stages:List[(Array[Array[Int]],Array[Array[Option[Personnage]]],LayerSet)] = List(bddLevel.create_sentinel_level())
	val boss_stages_ref = boss_stages
	val stages:Array[Environnement] = Array.ofDim[Environnement](profondeur)
	val personnages:Array[Array[Array[Option[Personnage]]]] = Array.ofDim[Array[Array[Option[Personnage]]]](profondeur)
	
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
			this.personnages(i) = personnages
			stages(i) = env
			boss_stages = boss_stages.init
		} else {
			stages(i) = this.gen_level(i)
		}
	}
	boss_stages = boss_stages_ref
	
	def start():Unit={
		if (index_current_stage == profondeur){
			// WIN !!
			app.win_screen()
		} else {
			val next_stage = this.get_next_stage()
			//Chargement des unités
			this.personnages_spawn(next_stage,personnages(index_current_stage))
			next_stage.start()
		}
	}
	
	def get_next_stage():Environnement={
		this.index_current_stage += 1
		return this.stages(index_current_stage)
	}
	
	def personnages_spawn(Env:Environnement,personnages:Array[Array[Option[Personnage]]])={
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
	
	def gen_level(i:Int):Environnement={
		//Chargement de l'objet contenant l'initialisation du terrain
		val Env = new Environnement
		val plan = new Plan
		plan.random_fill()
		var sprite_plan = new Sprite_plan(plan);
  		sprite_plan.random_loot();
  		sprite_plan.fill_layer_0135();
		val display = new All_sprites(sprite_plan)
		display.load_stage()
		sprite_plan.everything.transpose()
		Env.layerset = sprite_plan.everything
		val personnages = display.personnages.transpose
		this.personnages(i) = personnages
		//this.personnages_spawn(Env,personnages)
		Env.tiles = sprite_plan.tile_type.transpose
		return Env
	}
}