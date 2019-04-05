package Layer

class LayerSet(width:Int,height:Int) {
	nb_layers = 8
	layers = Array.ofDim[Layer](nb_layers)
	
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
		
	def get_layer(name:String):List[Sprite]={
		return this.layers(this.get_index(name))
	}
	
	def get_static_layers()={ // Layers which do not need to be refresh during combat
		return [0,1,3,5,6]
	}
	
	def get_refresh_layers()={ //Layers which need to be refreshed each frame
		return [4,7]
	}
}
	
class Layer(width:Int,height:Int) {
	content = List[Sprite,Int,Int]
	images = Array.ofDim[Image](width,height)
	
	def load_image(sprite,x,y)={
		
	
	def load_images()={
		for (i<-this.content){ //for i in this.content
			val (sprite,x,y) = i
			images(x)(y) = sprite
			
	
}