package Display
import Personnage._
import Layer._
import Item._

/*
The main use for this file :
  (m is an initialized sprite matrix from Mechanisms.scala)
  (listJ is a list of jetons representing the characters in the level)
  t = new All_sprites(m);
  t.jetons = listJ;
  t.add_jetons()
returns the 25 * 25 matrix where each cells contains the list of
all the sprites that must be displayed on the screen (sorted by layers)
*/


import Mechanisms.{Sprite_group=>Sprite_groupe,Sprite_plan=>Sprite_plan,_}
import Personnage.{Personnage=>Personnage,Jeton=>Jeton,_}
import bddPersonnages.{bddPersonnages=>bddP,_}
import Game._

class All_sprites(plan:Sprite_plan) {
  val layerset = new LayerSet(25,25)
  var personnages:Array[Array[Option[Personnage]]] = Array()
  val robots_pos = Array((2,5),(9,5),(15,5),(22,5),(5,8),(19,8),(5,13),(19,13),(9,16),(15,16),(2,19),(22,19))
  val start_pos = Array((12,22),(12,20),(11,21),(12,21),(13,21),(13,22))
  def load_stage() {
  	var p:Array[Array[Option[Personnage]]] = Array.ofDim[Option[Personnage]](main_grid.length,main_grid(0).length)
  	for (i <- 0 to p.length-1){
  		for (j <- 0 to p(i).length-1){
  			p(i)(j) = None
  		}
  	}
  	//Creation des unités du joueur
  	for (i<-0 to Game.Human.units.length-1){
  		val h = start_pos(i)
  		p(h._1)(h._2) = Game.Human.units(i)
  	}
  	//Creation des robots
    for ( (x,y) <- this.robots_pos ) {
      p(x)(y) = (Some(bddP.create_robot(Game.IA)));
    }
    this.personnages = p
    //Creation du layerset
  }
}

object sheet_slots {
	val X = Array(22, 22, 22, 23, 23, 23)
	val Y = Array(4, 6, 8, 4, 6, 8)
	val xequip = 24
	val yequip = Array(4, 7)
	val compet_slot = Array()
}

class Sheet(p : Personnage) {
	def afficher(l : LayerSet) = {
		l.layers(6).clear()
		var token = new LocatedSprite("background_sheet.png")
		token.x = 21*32; token.y = 0
		l.layers(6).add_sprite(token)
		token = new LocatedSprite(p.sheet_image)
		token.x = 22*32; token.y = 32
		l.layers(6).add_sprite(token)
		for (i <- 0 to 5) {
			p.inventory(i) match {
				case Some(item:Item) => {
					token = new LocatedSprite(item.image_path)
					token.x = 32*sheet_slots.X(i); token.y = 32*sheet_slots.Y(i)
					l.layers(6).add_sprite(token)
				}
				case None => ()
			} 
		}
		for (i <- 0 to 1) {
			p.equipment(i) match {
				case Some(item:Item) => {
					token = new LocatedSprite("sprite_sheet_frame_" + item.element + ".png")
					token.x = 32*sheet_slots.xequip; token.y = 32*sheet_slots.yequip(i)
					l.layers(6).add_sprite(token)
					token = new LocatedSprite(item.image_path)
					token.x = 32*sheet_slots.xequip; token.y = 32*sheet_slots.yequip(i)
					l.layers(6).add_sprite(token)
				}
				case None => ()
			}
		}
	}
}
