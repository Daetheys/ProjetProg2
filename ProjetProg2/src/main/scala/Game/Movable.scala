package Movable
import Environnement.{Environnement}

class Movable(env:Environnement){
	var x:Int=0
	var y:Int=0
	var orientation:Int=0
	var Env:Environnement=env
	
	def set_position(x:Int,y:Int) = {
		this.x = x
		this.y = y
	}
	
	def move(orient:Orientation):Unit = {
		orient match {
			case Left => 
				if (1 <= this.x && this.Env.tiles(this.x-1)(this.y) != 0) {
					this.Env.this.set_position(this.x-1,this.y)
				}
			case Right =>
				if (this.x <= this.Env.size_x - 2 && this.Env.tiles(this.x+1)(this.y) != 0) {
					this.Env.this.set_position(this.x+1,this.y)
				}
			case Top =>
				if (1 <= this.y && this.Env.tiles(this.x)(this.y-1) != 0) {
					this.Env.this.set_position(this.x,this.y-1)
				}
			case Bottom =>
				if (this.y <= this.Env.size_y - 2 && this.Env.tiles(this.x)(this.y+1) != 0) {
					this.Env.this.set_position(this.x,this.y+1)
				}
	}
}

abstract class Orientation
case class Left extends Orientation
case class Right extends Orientation
case class Top extends Orientation
case class Bottom extends Orientation