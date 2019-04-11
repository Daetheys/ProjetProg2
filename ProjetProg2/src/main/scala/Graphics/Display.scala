package Display
import Personnage._
import Layer._
import Inventory._

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

/*
import Mechanisms.{Sprite_group=>Sprite_groupe,Sprite_plan=>Sprite_plan,_}
import Personnage.{Personnage=>Personnage,Jeton=>Jeton,_}
import bddPersonnages.{bddPersonnages=>bddP,_}
import Game._

class All_sprites(plan:Sprite_plan) {
  val main_grid = plan.all_the_sprites
  var personnages:Array[Array[Option[Personnage]]] = Array()
  val robots_pos = Array((2,5),(9,5),(15,5),(22,5),(5,8),(19,8),(5,13),(19,13),(9,16),(15,16),(2,19),(22,19))
  def load_demo_version1() {
  	var p:Array[Array[Option[Personnage]]] = Array.ofDim[Option[Personnage]](main_grid.length,main_grid(0).length)
  	for (i <- 0 to p.length-1){
  		for (j <- 0 to p(i).length-1){
  			p(i)(j) = None
  		}
  	}
    p(12)(22) = Some(bddP.create_turtle(Game.Human))
    p(12)(20) = Some(bddP.create_bird(Game.Human))
    p(11)(21) = Some(bddP.create_cat(Game.Human))
    p(12)(21) = Some(bddP.create_monkey(Game.Human))
    p(13)(21) = Some(bddP.create_snake(Game.Human))
    for ( (x,y) <- this.robots_pos ) {
      p(x)(y) = (Some(bddP.create_robot(Game.IA)));
    }
    this.personnages = p
  }
} */

object sheet_slots {
	val X = Array(22, 22, 22, 23, 23, 23)
	val Y = Array(4, 6, 8, 4, 6, 8)
	val equipX = 24
	val equipY = Array(4, 7)
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
		l.layers(6).load_layer()
	}
}

object inventory_slots {
	val tabX = 3
	val tabY = Array(2, 5, 8, 11, 14, 17, 20)
	val X = Array(7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17)
	val Y = Array(3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13)
	val persoX = Array(7, 9, 11, 13, 15, 17)
	val persoY = 3
	val equipX = Array(9, 15)
	val equipY = 13
}

class InventoryTabs (m : mainInventory) {
	var selected_tab = 0
	var selected_item = 0
	val tab_limit = m.equip.length + 1
	val item_limit = 0
	var token = new LocatedSprite("")
	var curseur = new LocatedSprite("sprite_inventory_slot_selected.png")
	

	def oneTab(i : Int, s : String, l : LayerSet) : Unit = {
		this.token = new LocatedSprite(s)
		this.token.x = inventory_slots.tabX*32; this.token.y = inventory_slots.tabY(i)*32
		l.layers(6).add_sprite(this.token)
	}

	def tabDisplay(j : Int, l : LayerSet) = {
		this.oneTab(0, "sprite_inventory_tab.png", l)
		this.token = new LocatedSprite("sprite_inventory_bag.png")
		this.token.x = inventory_slots.tabX*32+16
		this.token.y = inventory_slots.tabY(0)*32
		l.layers(6).add_sprite(this.token)
		for (i <- 0 to tab_limit - 1) {
			this.oneTab(i+1, "sprite_inventory_tab.png", l)
			this.token = new LocatedSprite(m.equip(i).image_path)
			this.token.x = inventory_slots.tabX*32+16
			this.token.y = inventory_slots.tabY(i+1)*32
			l.layers(6).add_sprite(this.token)
		}
		oneTab(j, "sprite_inventory_tab_selection.png", l)
	}

	def persoDisplay(j : Int, l : LayerSet) = { //j est 1+l'indice dans units du perso sélectionné
		var p = m.equip(j-1)
		l.layers(6).clear()
		this.token = new LocatedSprite("background_inventory.png")
		this.token.x = 5*32; this.token.y = 2*32
		l.layers(6).add_sprite(this.token)
		this.tabDisplay(j, l)
		for (i <- 0 to 5) {
			p.inventory(i) match {
				case Some(item:Item) => {
					this.token = new LocatedSprite(item.image_path)
					this.token.x = 32*inventory_slots.persoX(i)
					this.token.y = 32*inventory_slots.persoY
					l.layers(6).add_sprite(this.token)
				}
				case None => ()
			} 
		}
		for (i <- 0 to 1) {
			p.equipment(i) match {
				case Some(item:Item) => {
					this.token = new LocatedSprite(
						"sprite_sheet_frame_" + item.element + ".png")
					this.token.x = 32*inventory_slots.equipX(i)
					this.token.y = 32*inventory_slots.equipY
					l.layers(6).add_sprite(this.token)
					this.token = new LocatedSprite(item.image_path)
					this.token.x = 32*inventory_slots.equipX(i)
					this.token.y = 32*inventory_slots.equipY
					l.layers(6).add_sprite(this.token)
				}
				case None => ()
			}
		}
	}

	def mainDisplay(l : LayerSet) = {
		l.layers(6).clear()
		this.token = new LocatedSprite("background_inventory.png")
		this.token.x = 5*32; this.token.y = 2*32
		l.layers(6).add_sprite(this.token)
		this.tabDisplay(0, l)
		var item = new Item
		for (i <- 0 to 18) {
			if (m.content(i).quantity > 0) {
				item = m.content(i).it
				this.token = new LocatedSprite(item.image_path)
				this.token.x = 32*inventory_slots.X(i)
				this.token.y = 32*inventory_slots.Y(i)
				l.layers(6).add_sprite(this.token)
			}
		}
	}
	
	def coord_item(j : Int, ls_list : ListBuffer[LocatedSprite]) = {
		ls_list match {
			case (LocatedSprite(s) as ls) :: tail => {
				if (s.startsWith("sprite_item")) {
					if (j > 0) {
						return this.coord_item(j-1, tail)
					} else {
						return (ls.x, ls.y)
					}
				} else {
					return this.coord_item(j, tail)
			case Nil => return (X(0), Y(0))
		}
	}

	def afficher(l : LayerSet) = {
		if (this.selected_tab == 0) {
			this.mainDisplay(l)
		} else {
			this.persoDisplay(this.selected_tab, l)
		}
		this.token = new LocatedSprite("sprite_inventory_slot_selection.png")
		(this.token.x, this.token.y) = this.coord_item(this.selected_item, l.layers(6).content)
		l.layers(6).add_sprite(this.token)
		l.layers(6).load_layer()
	}

	def move_curseur(l : LayerSet) = {
		l.layers(6).remove(this.curseur)
		(this.curseur.x, this.curseur.y) = this.coord_item(this.selected_item, l.layers(6).content)
		l.layers(6).add_sprite(this.curseur)
		l.layers(6).load_layer()
	}

	def right_tab(l : LayerSet) {
		if (this.selected_tab < this.tab_limit) {
			this.selected_tab += 1
		} else {
			this.selected_tab = 0
		}
		this.afficher(l)
	}
	
	def left_tab(l : LayerSet) {
		if (this.selected_tab > 0) {
			this.selected_tab -= 1
		} else {
			this.selected_tab = this.tab_limit
		}
		this.afficher(l)
	}

	def down_item(l : LayerSet) {
		if (this.selected_item < this.item_limit) {
			this.selected_item += 1
		} else {
			this.selected_item = 0
		}
		this.move_curseur(l)
	}
	
	def up_item(l : LayerSet) {
		if (this.selected_item > 0) {
			this.selected_item -= 1
		} else {
			this.selected_item = this.item_limit
		}
		this.move_curseur(l)
	}
}

