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
import Generator._
import bddPersonnages.bddPersonnages._

class Donjon(taille:Int) {
	val profondeur:Int = taille
	val index_boss_stages:List[Int] = List(5)
	var index_current_stage:Int = -1
	// Attention boss_stages va etre remise a List() dans la suite
	var boss_stages:List[(Array[Array[Int]],Array[Array[Option[Personnage]]],LayerSet)] = List(bddLevel.create_sentinel_level())
	val boss_stages_ref = boss_stages
	val stages:Array[Environnement] = Array.ofDim[Environnement](profondeur)
	val personnages:Array[Unit=>Array[Array[Option[Personnage]]]] = Array.ofDim[Unit=>Array[Array[Option[Personnage]]]](profondeur)
	
	{if (!(Game.gen_parseur)) {
	Game.Human.add_unit(create_turtle(Game.Human))
	Game.Human.add_unit(create_bird(Game.Human))
	Game.Human.inventory.send_to(3,1)
	Game.Human.inventory.send_to(17,1)
	//Generation classique
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
			def get_personnages(e:Unit):Array[Array[Option[Personnage]]]={
				var p = personnages
				for (i<-0 to Game.Human.units.length-1){
					p(15)(10+i) = Some(Game.Human.units(i))
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
	} else {
	
	
	//Generation Parsing
	{//Gen donjon
	this.gen_donjon_parse()
	//Gen boss
	val h = boss_stages.last
	val tiles = h._1
	var personnages = h._2
	var layerset = h._3
	val env = new Environnement
	env.tiles = tiles.transpose
	layerset.transpose()
	env.layerset = layerset
	def get_personnages(e:Unit):Array[Array[Option[Personnage]]]={
		var p = personnages
		for (i<-0 to Game.Human.units.length-1){
			p(15)(10+i) = Some(Game.Human.units(i))
		}
		return p
	}
	this.personnages(4) = get_personnages
	stages(4) = env}
	}}
	
	def invert_1_0(l:Array[Array[Int]]){
		for (i<-0 to l.length-1){
			for (j<-0 to l(i).length-1){
				l(i)(j) match {
					case 0 => l(i)(j) = 1
					case 1 => l(i)(j) = 0
					case x => ()
				}
			}
		}
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
	
	def personnages_spawn(Env:Environnement,personnages:Array[Array[Option[Personnage]]])={
		//Spawn des unités sur l'environnement
		for (i <- 0 to personnages.length-1){
			for (j <- 0 to personnages(i).length-1){
				Env.units(j)(i) = None
				personnages(i)(j) match {
					case None => ()
					case Some(p:Personnage) => if (p.player == Game.Human) 
													{ Env.spawn_personnage(p,j,i) } 
												else 
													{ Env.spawn_new_personnage(p,j,i) }
				}
			}
		}
	}
	
	def gen_donjon_parse()={
		var ge = new Generate(Game.seed); 
		ge.generate(); 
		var go = new Generator; 
		go.get_model(); 
		val init_team = go.team //list des perso
		init_team.foreach((p:Personnage) => Game.Human.add_unit(p))
		val init_inv = go.inventoryG //contents d'un mainInventory
		Game.Human.inventory.content = init_inv
		val hl = go.alllevels //List[(Array[Array[Int]], Plan, Sprite_plan, LayerSet, List[(Personnage, Int, Int)])]
		def create_envs(i:Int,h:(Array[Array[Int]], Plan, Sprite_plan, LayerSet, List[(Personnage, Int, Int)])) = {
			val tiles = h._1
			val plan = h._2
			val sprite_plan = h._3
			val layerset = h._4
			val personnages_pos = h._5
			var env = new Environnement
			val start_pos = Array((12,22),(12,20),(11,21),(12,21),(13,21),(13,22))
			this.invert_1_0(tiles)
			env.tiles  = tiles.transpose
			env.plan = plan
			env.sprite_grid = sprite_plan.sprite_grid.transpose
			layerset.transpose()
			def get_personnages_human():List[(Personnage,Int,Int)]={
				var l:List[(Personnage,Int,Int)] = List()
				for (j<-0 to Game.Human.units.length-1){
					val h = start_pos(j)
					val x = h._1
					val y = h._2
					l = (Game.Human.units(j),x,y)::l
				}
				return l
			}
			env.layerset = layerset
			personnages(i) = ((t:Unit) => {create_personnages_array(personnages_pos ::: get_personnages_human())})
			stages(i) = env
		}
		def iter_all(i:Int,l:List[(Array[Array[Int]], Plan, Sprite_plan, LayerSet, List[(Personnage, Int, Int)])]){
			l match {
				case Nil => ()
				case t::q => create_envs(i,t);iter_all(i+1,q)
			}
		}
		def create_personnages_array(l:List[(Personnage,Int,Int)]):Array[Array[Option[Personnage]]]={
			val arr:Array[Array[Option[Personnage]]] = Array.ofDim(25,25)
			for (i<-0 to 24){
				for (j<-0 to 24){
					arr(i)(j) = None
				}
			}
			def func(e:(Personnage,Int,Int))={
				val x = e._2
				val y = e._3
				arr(y)(x) = Some(e._1)
			}
			def iter(l:List[(Personnage,Int,Int)]){
				l match {
					case Nil => ()
					case e::q => func(e);iter(q)
				}
			}
			iter(l)
			return arr
		}
		iter_all(0,hl)
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
		def get_personnages(typage:Unit) :Array[Array[Option[Personnage]]]= { return personnages}
		this.personnages(i) = get_personnages //LISTE ENNEMIS
		//this.personnages_spawn(Env,personnages)
		Env.tiles = sprite_plan.tile_type.transpose //ARRAY TILES 0-1-2
		Env.plan = plan 									//PLAN
		Env.sprite_grid = sprite_plan.sprite_grid.transpose //SPRITE PLAN
		return Env
	}
}