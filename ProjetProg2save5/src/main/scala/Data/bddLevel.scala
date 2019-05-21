package bddLevel

import Game._
import Sprite._
import Layer._
import bddPersonnages._
import Personnage._

object bddLevel {

	val human = Game.Human
	val ia = Game.IA

	def create_sentinel_level():(Array[Array[Int]],Array[Array[Option[Personnage]]],LayerSet)={
		val tiles = 
			Array(Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,2,2,2,2,2,2,2,2,2,2,0,2,2,2,2,2,2,2,2,2,2,1,1),
			Array(1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1),
			Array(1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1),
			Array(1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1),
			Array(1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1),
			Array(1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1),
			Array(1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1),
			Array(1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1),
			Array(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1))
					  	  
		val sentinel = bddPersonnages.create_sentinel(ia)
		val bird = bddPersonnages.create_bird(human)
		val turtle = bddPersonnages.create_turtle(human)
		val bees = bddPersonnages.create_bees(human)
		val units:Array[Array[Option[Personnage]]] = 
			Array(Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,Some(sentinel),None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None),
	  	  	Array(None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None))

		val layerset = new LayerSet(21,15)
		for (i<-0 to tiles.length-1){
			for (j<- 0 to tiles(i).length-1){
				if (tiles(i)(j) == 0){
					val ls = new LocatedSprite("background_tile_hallway.png")
					ls.x = i*32
					ls.y = j*32
					layerset.get_layer("Tiles").add_sprite(ls)
				} else if (tiles(i)(j) == 1){
					val ls = new LocatedSprite("background_tile_obstacle.png")
					ls.x = i*32
					ls.y = j*32
					layerset.get_layer("Tiles").add_sprite(ls)
				} else if (tiles(i)(j) == 2){
					//Tile
					val ls = new LocatedSprite("background_tile_hallway.png")
					ls.x = i*32
					ls.y = j*32
					layerset.get_layer("Tiles").add_sprite(ls)
					//UpTile
					val ls2 = new LocatedSprite("sprite_tile_water.png")
					ls2.x = i*32
					ls2.y = j*32
					layerset.get_layer("UpTiles").add_sprite(ls2)
				}
			}
		}
		return (tiles,units,layerset)
	}
}