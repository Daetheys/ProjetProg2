package Layer
import Sprite._
import scala.collection.mutable.{ListBuffer}

class LayerSet(width:Int,height:Int) {
	val nb_layers = 8
	var layers = Array.ofDim[Layer](nb_layers)
	for (i<-0 to layers.length-1){
		layers(i) = new Layer(width,height)
	}
	
	def get_index(name:String):Int={
		return name match {
			case "Tiles" => 0 
			case "UpTiles" => 1 //Water, ...
			case "Corpses" => 2 //				-- Semi static layer --> Refreshed when someone die
			case "Rewards" => 3
			case "Units" => 4
			case "Mechanisms" => 5
			case "UI" => 6
			case "Spells" => 7
		}
	}
	
	def transpose()={
		for (i<-0 to this.layers.length-1){
			this.layers(i).transpose()
		}
	}
	
	def get_layer(name:String):Layer={
		return this.layers(this.get_index(name))
	}
	
	def get_static_layers():List[Int]={ // Layers which do not need to be refresh during combat
		return List(0,1,3,6)
	}
	
	def get_refresh_layers():List[Int]={ //Layers which need to be refreshed each frame
		return List(4,5,7)
	}
}
	
class Layer(width:Int,height:Int) {
	var content:ListBuffer[LocatedSprite] = ListBuffer() //Les coordonees sont souvent des multiples de 32 mais pas forcement
	
	def add_sprite(ls:LocatedSprite):Unit={
		this.content += ls
	} 

	def load_layer():Unit={
		//Charge les images du layer
		this.content.map( (e:LocatedSprite) => e.load_image() )
	}
	
	def remove(l:LocatedSprite):Unit={
		this.content -= l
	}
	
	def get(x:Int,y:Int):LocatedSprite={
		var element:LocatedSprite = null
		this.content.foreach( (e:LocatedSprite) => if (e.x == x && e.y == y) {element = e} )
		return element
	}
	
	def transpose()={
		this.content.map( (e:LocatedSprite) => {val z = e.x; e.x = e.y; e.y = z} )
	}

	def clear() = {
		this.content = ListBuffer()
	}
	
}