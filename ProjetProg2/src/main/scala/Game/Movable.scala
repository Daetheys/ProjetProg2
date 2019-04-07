package Movable
import Environnement.{Environnement}
import Utilities._
import Sprite._

class Movable(env:Environnement){
	var x:Int=0
	var y:Int=0
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
	
	def corresponding_orient(x1:Int,x2:Int,y1:Int,y2:Int):Orientation={
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
	
	def orient(o:Orientation)={
		this.orientation = o
		this.located_sprite.orient(o)
	}
	
	def animation_move(x1:Int,y1:Int,x2:Int,y2:Int,time:Double):Unit={
		val nb_iter_max = 10
		var no_iter = 0
		val dx = (x2-x1)/nb_iter_max
		val dy = (y2-y1)/nb_iter_max
		def event(typage:Unit):Int={
			this.located_sprite.x = x1+dx*no_iter
			this.located_sprite.y = y1+dy*no_iter
			if (no_iter == nb_iter_max){
				return 0
			} else {
				return 1
			}
		}
		this.Env.clock.add_micro_event(utils.cooldowned(event(_),time/10.0))
	}
	
	def move(o:Orientation,time:Double):Unit = {
		//Deplace le movable en time secondes
		o match {
			case Left() => 
				if (1 <= this.x && this.Env.tiles(this.x-1)(this.y) != 0) {
					this.set_position(this.x-1,this.y,time)
				}
			case Right() =>
				if (this.x <= this.Env.size_x - 2 && this.Env.tiles(this.x+1)(this.y) != 0) {
					this.set_position(this.x+1,this.y,time)
				}
			case Top() =>
				if (1 <= this.y && this.Env.tiles(this.x)(this.y-1) != 0) {
					this.set_position(this.x,this.y-1,time)
				}
			case Bottom() =>
				if (this.y <= this.Env.size_y - 2 && this.Env.tiles(this.x)(this.y+1) != 0) {
					this.set_position(this.x,this.y+1,time)
				}
		this.orient(o)
		}
	}
}

abstract class Orientation
case class Left() extends Orientation
case class Right() extends Orientation
case class Top() extends Orientation
case class Bottom() extends Orientation