package Display
import Personnage._
import Layer._
import Inventory._
import Sprite._
import scala.collection.mutable.ListBuffer
import Graphics2._
import Mechanisms.{Sprite_group=>Sprite_groupe,Sprite_plan=>Sprite_plan,_}
import Personnage.{Personnage=>Personnage,Jeton=>Jeton,_}
import bddPersonnages.{bddPersonnages=>bddP,_}
import Game._


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



class All_sprites(plan:Sprite_plan) {
  val layerset = new LayerSet(25,25)
  var personnages:Array[Array[Option[Personnage]]] = Array()
  val robots_pos = Array((2,5),(9,5),(15,5),(22,5),(5,8),(19,8),(5,13),(19,13),(9,16),(15,16),(2,19),(22,19))
  val start_pos = Array((12,22),(12,20),(11,21),(12,21),(13,21),(13,22))
  def load_stage() {
  	var p:Array[Array[Option[Personnage]]] = Array.ofDim[Option[Personnage]](25,25)
  	for (i <- 0 to p.length-1){
  		for (j <- 0 to p(i).length-1){
  			p(i)(j) = None
  		}
  	}
  	//Creation des unités du joueur
  	for (i<-0 to Game.Human.units.length-1){
  		val h = start_pos(i)
  		p(h._1)(h._2) = Some(Game.Human.units(i))
  	}
  	//Creation des robots
    for ( (x,y) <- this.robots_pos ) {
      p(x)(y) = Some(bddP.create_robot(Game.IA));
    }
    this.personnages = p
    //Creation du layerset
  }
}

object sheet_slots {
	val X = Array(21, 21, 21, 22, 22, 22)
	val Y = Array(4, 6, 8, 4, 6, 8)
	val equipX = 23
	val equipY = Array(4, 7)
}

class Sheet(p : Personnage) {
	def afficher(l : LayerSet) = {
		l.layers(6).clear()
		var token = new LocatedSprite("background_sheet.png")
		token.x = 20*32; token.y = 0
		l.layers(6).add_sprite(token)
		token = new LocatedSprite(p.sheet_image)
		token.x = 21*32; token.y = 32
		l.layers(6).add_sprite(token)
		for (i <- 0 to 5) {
			p.inventory(i) match {
				case None => ()
				case Some(item:Item) => {
					token = new LocatedSprite(item.image_path)
					token.x = 32*sheet_slots.X(i); token.y = 32*sheet_slots.Y(i)
					l.layers(6).add_sprite(token)
				}
			} 
		}
		val equipment:Array[Option[Item]] = Array(p.weapon,p.armor)
		for (i <- 0 to 1) {
			equipment(i) match {
				case None => ()
				case Some(item:Item) => {
					token = new LocatedSprite("sprite_sheet_frame_" + item.element + ".png")
					token.x = 32*sheet_slots.equipX; token.y = 32*sheet_slots.equipY(i)
					l.layers(6).add_sprite(token)
					token = new LocatedSprite(item.image_path)
					token.x = 32*sheet_slots.equipX; token.y = 32*sheet_slots.equipY(i)
					l.layers(6).add_sprite(token)
				}
			}
		}
		l.layers(6).transpose()
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

class InventoryTabs {
	var m = Game.Human.inventory
	var selected_tab = 0
	var selected_item = 0
	var itemArray : Array[LocatedSprite] = Array()
	var token = new LocatedSprite("")

	def oneTab(i : Int, s : String, l : LayerSet) : Unit = {
		this.token = new LocatedSprite(s)
		this.token.x = inventory_slots.tabX*32
		this.token.y = inventory_slots.tabY(i)*32
		l.get_layer("UI").add_sprite(this.token)
	}

	def tabDisplay(j : Int, l : LayerSet) = {
		this.oneTab(0, "sprite_inventory_tab.png", l)
		this.token = new LocatedSprite("sprite_inventory_bag.png")
		this.token.x = inventory_slots.tabX*32+16
		this.token.y = inventory_slots.tabY(0)*32+32
		l.get_layer("UI").add_sprite(this.token)
		val tab_limit = Game.Human.units.length
		for (i <- 0 to tab_limit - 1) {
			this.oneTab(i+1, "sprite_inventory_tab.png", l)
			this.token = new LocatedSprite(Game.Human.units(i).image_path)
			this.token.x = inventory_slots.tabX*32+16
			this.token.y = inventory_slots.tabY(i+1)*32 + 32
			l.get_layer("UI").add_sprite(this.token)
		}
		oneTab(j, "sprite_inventory_tab_selection.png", l)
	}

	def persoDisplay(j : Int, l : LayerSet) = { //j est 1+l'indice dans units du perso sélectionné
		var p = Game.Human.units(j-1)
		var ilist : List[LocatedSprite] = Nil
		l.get_layer("UI").clear()
		this.token = new LocatedSprite("background_inventory.png")
		this.token.x = 5*32; this.token.y = 2*32
		l.get_layer("UI").add_sprite(this.token)
		this.tabDisplay(j, l)
		for (i <- 0 to 5) {
			p.inventory(i) match {
				case None => ()
				case Some(item:Item) => {
					this.token = new LocatedSprite(item.image_path)
					ilist = this.token :: ilist
					this.token.x = 32*inventory_slots.persoX(i)
					this.token.y = 32*inventory_slots.persoY
					l.get_layer("UI").add_sprite(this.token)
				}
			} 
		}
		val equipment:Array[Option[Item]] = Array(p.weapon,p.armor)
		for (i <- 0 to 1) {
			equipment(i) match {
				case None => ()
				case Some(item:Item) => {
					this.token = new LocatedSprite(
						"sprite_sheet_frame_" + item.element + ".png")
					this.token.x = 32*inventory_slots.equipX(i)
					this.token.y = 32*inventory_slots.equipY
					l.get_layer("UI").add_sprite(this.token)
					this.token = new LocatedSprite(item.image_path)
					ilist = this.token :: ilist
					this.token.x = 32*inventory_slots.equipX(i)
					this.token.y = 32*inventory_slots.equipY
					l.get_layer("UI").add_sprite(this.token)
				}
			}
		}
		this.itemArray = ilist.toArray
	}

	def mainDisplay() = {
		var l = app.Env.layerset
		var ilist : List[LocatedSprite] = Nil
		l.get_layer("UI").clear()
		this.token = new LocatedSprite("background_inventory.png")
		this.token.x = 5*32; this.token.y = 2*32
		l.get_layer("UI").add_sprite(this.token)
		this.tabDisplay(0, l)
		var item = new Item
		for (i <- 0 to 18) {
			if (m.content(i).quantity > 0) {
				item = m.content(i).it
				this.token = new LocatedSprite(item.image_path)
				ilist = this.token :: ilist
				this.token.x = 32*inventory_slots.X(i)
				this.token.y = 32*inventory_slots.Y(i)
				l.get_layer("UI").add_sprite(this.token)
			}
		}
		this.itemArray = ilist.toArray
	}

	def afficher() = {
		val l = app.Env.layerset
		if (this.selected_tab == 0) {
			this.mainDisplay()
		} else {
			this.persoDisplay(this.selected_tab, l)
		}
		if (this.itemArray.length > 0) {
			this.token = new LocatedSprite("sprite_inventory_slot_selection.png")
			this.token.x = this.itemArray(this.selected_item).x
			this.token.y = this.itemArray(this.selected_item).y
			l.get_layer("UI").add_sprite(this.token)
		}
		l.get_layer("UI").transpose()
		l.get_layer("UI").load_layer()
	}

	def right_tab() {
		this.selected_item = 0
		if (this.selected_tab < Game.Human.units.length) {
			this.selected_tab += 1
		} else {
			this.selected_tab = Game.Human.units.length-1
		}
		this.afficher()
	}
	
	def left_tab() {
		this.selected_item = 0
		if (this.selected_tab > 0) {
			this.selected_tab -= 1
		} else {
			this.selected_tab = 0
		}
		this.afficher()
	}

	def down_item() {
		if (this.selected_item < this.itemArray.length-1) {
			this.selected_item += 1
		} else {
			this.selected_item = 0
		}
		this.afficher()
	}
	
	def up_item() {
		if (this.selected_item > 0) {
			this.selected_item -= 1
		} else {
			this.selected_item = this.itemArray.length-1
		}
		this.afficher()
	}
	
	def get_id_item_main():Int= {
			var count = this.selected_item
			for (i<-m.content.length-1 to 0 by -1){
				print("count",i,m.content(i).quantity,count)
				if (m.content(i).quantity > 0){
					count -= 1
					if (count < 0) {
					 	return i
					}
				}
				println("-->",m.content(i).quantity,count)
			}
			return -7 //Ne doit pas arriver
		}
		
	def get_id_item_units():Item={
			val inv = Game.Human.units(this.selected_tab-1).inventory
			var count = this.selected_item
			if (Game.Human.units(this.selected_tab-1).weapon != None){
				count -= 1
			}
			if (Game.Human.units(this.selected_tab-1).armor != None){
				count -= 1
			}
			
			for (i<-inv.length-1 to 0 by -1){
				inv(i) match {
					case None => ()
					case Some(e:Item) => 	count -= 1
											if (count<0){
												return inv(i).get
											}
				}
			}
			return null //Ne devrait pas arriver
		}
		
	def send_item(i:Int) :Unit ={
		if (this.selected_tab == 0) {
			val id_item = get_id_item_main()
			Game.Human.inventory.send_to(id_item,i)
			Game.Human.inventory.content(id_item).quantity -= 1
		} else {
			val item = get_id_item_units()
			var forbidden = 0
			if (Game.Human.units(this.selected_tab-1).weapon != None){
				forbidden += 1
			}
			if (Game.Human.units(this.selected_tab-1).armor != None){
				forbidden += 1
			}
			if (forbidden > this.selected_item) { return () }
			Game.Human.units(this.selected_tab-1).remove_from_inventory(item)
			if (i==0){
				Game.Human.inventory.send_to(item.id-1,0)
			} else {
				Game.Human.units(i-1).add_item(item)
			}
		}
		this.mainDisplay()
		this.selected_item -= 1
		if (this.selected_item < 0) { this.selected_item = 0 }
	}
}

