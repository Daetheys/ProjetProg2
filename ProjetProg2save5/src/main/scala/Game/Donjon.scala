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
	var index_current_stage:Int = -1
	// Attention boss_stages va etre remise a List() dans la suite
	var boss_stages:List[(Array[Array[Int]],Array[Array[Option[Personnage]]],LayerSet)] = List(bddLevel.create_sentinel_level())
	val boss_stages_ref = boss_stages
	val stages:Array[Environnement] = Array.ofDim[Environnement](profondeur)
	val personnages:Array[Unit=>Array[Array[(Option[Personnage],Boolean)]]] = Array.ofDim[Unit=>Array[Array[(Option[Personnage],Boolean)]]](profondeur)
	
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
			def get_personnages(e:Unit):Array[Array[(Option[Personnage],Boolean)]]={
				var p = this.couple(personnages)
				for (i<-0 to Game.Human.units.length-1){
					p(15)(10+i) = (Some(Game.Human.units(i)),true)
				}
				return p
			}
			this.personnages(i) = get_personnages
			stages(i) = env
			boss_stages = boss_stages.init
		} else {
			stages(i) = this.gen_level(i)
		}
	}
	boss_stages = boss_stages_ref
	
	def couple(a:Array[Array[Option[Personnage]]]):Array[Array[(Option[Personnage],Boolean)]] ={
		var r:Array[Array[(Option[Personnage],Boolean)]] = Array.ofDim(a.length,a(0).length)
		for (i<- 0 to a.length-1){
			for (j<- 0 to a(i).length-1){
				r(i)(j) = (a(i)(j),false)
			}
		}
		return r
	}
	
	def start():Unit={
		if (index_current_stage == profondeur){
			// WIN !!
			app.win_screen()
		} else {
			val next_stage = this.get_next_stage()
			//Chargement des unités
			this.personnages_spawn(next_stage,this.personnages(index_current_stage)())
			next_stage.start()
		}
	}
	
	def get_next_stage():Environnement={
		this.index_current_stage += 1
		return this.stages(index_current_stage)
	}
	
	def personnages_spawn(Env:Environnement,personnages:Array[Array[(Option[Personnage],Boolean)]])={
		//Spawn des unités sur l'environnement
		for (i <- 0 to personnages.length-1){
			for (j <- 0 to personnages(i).length-1){
				Env.units(j)(i) = None
				personnages(i)(j) match {
					case (None,_) => ()
					case (Some(p:Personnage),false) => println(false,p.name);Env.spawn_new_personnage(p,j,i) 
					case (Some(p:Personnage),true) => println(true,p.name);Env.spawn_personnage(p,j,i)
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
		Env.layerset = sprite_plan.everything //LAYERSET
		val personnages = display.personnages.transpose
		def get_personnages(typage:Unit) :Array[Array[(Option[Personnage],Boolean)]]= { return personnages}
		this.personnages(i) = get_personnages //LISTE ENNEMIS
		//this.personnages_spawn(Env,personnages)
		Env.tiles = sprite_plan.tile_type.transpose //ARRAY TILES 0-1-2
		Env.plan = plan 									//PLAN
		Env.sprite_grid = sprite_plan.sprite_grid.transpose //SPRITE PLAN
		return Env
	}
}