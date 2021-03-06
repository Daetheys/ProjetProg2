package Movable
import Environnement.{Environnement}
import Utilities._
import Sprite._

class Movable(env:Environnement){
	var x:Int= -1000 //Pour etre sur qu'il ne puisse pas etre attaqué quand on fait pop un jeton quelconque pour fill quand on n'a personne a attaquer
	var y:Int= -1000
	var orientation:Orientation = Top()
	var image_path:String = ""
	var located_sprite= new LocatedSprite(image_path)
	var Env:Environnement=env
	
	def set_position(x:Int,y:Int,time:Double) = {
		// ATTENTION : Ne bouge pas le sprite (on a des animations pour ca)
		this.animation_move(this.x,this.y,x,y,time)
		this.x = x
		this.y = y
	}
	
	def corresponding_orient(x1:Int,y1:Int,x2:Int,y2:Int):Orientation={
		val dx = x2-x1
		val dy = y2-y1
		if (Math.abs(dx) > Math.abs(dy)){
			if (dx > 0){
				return Right()
			} else {
				return Left()
			}
		} else {
			if (dy > 0){
				return Bottom()
			} else {
				return Top()
			}
		}
	}
	
	def set_orientation(o:Orientation)={
		this.orientation = o
		this.located_sprite.set_orientation(o)
	}
	
	def animation_move(x1:Int,y1:Int,x2:Int,y2:Int,time:Double):Unit={
		val nb_iter_max = (time/this.Env.clock.micro_period).toInt+1
		var no_iter = 0
		val dx = (x2-x1)*32
		val dy = (y2-y1)*32
		def event(typage:Unit):Int={
			this.located_sprite.x = x1*32+(dx*no_iter)/nb_iter_max
			this.located_sprite.y = y1*32+(dy*no_iter)/nb_iter_max
			//printf("animation "+this.located_sprite.x.toString+" "+this.located_sprite.y.toString+"-"+no_iter.toString+" "+nb_iter_max.toString+"\n")
			if (no_iter == nb_iter_max){
				//Evite les bugs de perte de virgule
				this.located_sprite.x = x2*32
				this.located_sprite.y = y2*32
				return 0
			} else {
				no_iter += 1
				return 1
			}
		}
		this.Env.clock.add_micro_event(event(_))
	}
	
	def move(o:Orientation,time:Double):Unit = {
		//Deplace le movable en time secondes
		o match {
			case Left() => 
				if (1 <= this.x && this.Env.tiles(this.x-1)(this.y) != 1) {
					this.set_position(this.x-1,this.y,time)
				}
			case Right() =>
				if (this.x <= this.Env.size_x - 2 && this.Env.tiles(this.x+1)(this.y) != 1) {
					this.set_position(this.x+1,this.y,time)
				}
			case Top() =>
				if (1 <= this.y && this.Env.tiles(this.x)(this.y-1) != 1) {
					this.set_position(this.x,this.y-1,time)
				}
			case Bottom() =>
				if (this.y <= this.Env.size_y - 2 && this.Env.tiles(this.x)(this.y+1) != 1) {
					this.set_position(this.x,this.y+1,time)
				}
		}
		this.set_orientation(o)
		//Check water interactions
		if (this.Env.tiles(this.x)(this.y) == 4) {this.move(o,time)}
	}
}

abstract class Orientation
case class Left() extends Orientation
case class Right() extends Orientation
case class Top() extends Orientation
case class Bottom() extends Orientation