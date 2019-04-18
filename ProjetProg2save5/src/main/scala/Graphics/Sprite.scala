package Sprite
import scalafx.scene.image.Image
import Graphics2._
import Movable._

class Sprite(file:String) {
	var orientation:Orientation = Top()
	var image:Image = new Image(app.unknown_path())
	
	def set_orientation(orientation:Orientation)={
		this.orientation = orientation
	}

	def int_to_orient(d:Int)={
          if (d==0) {
            this.orientation = Top()
          } else {
            if (d==1) {
              this.orientation = Left()
            } else {
              if (d==2) {
                this.orientation = Bottom()
              } else {
                this.orientation = Right()
              }
            }
          }
	}

	def load_image():Unit={
		this.image = new Image(app.get_path(this.file))
	}
}

class LocatedSprite(file:String) extends Sprite(file) {
	var x:Int = -1
	var y:Int = -1
}

