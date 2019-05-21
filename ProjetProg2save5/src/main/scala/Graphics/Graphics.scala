package Graphics2

import javafx.event.EventHandler

import javafx.event.ActionEvent


import scala.collection.mutable.ListBuffer
import scalafx.scene.control.Button
import scalafx.Includes._
import scalafx.application.{Platform,JFXApp}
import scalafx.scene._
import scalafx.scene.control.Label
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle
import scalafx.scene.canvas._
import scalafx.scene.input.{KeyCode, KeyEvent, MouseEvent,MouseDragEvent,DragEvent,MouseButton}
import Environnement.{Environnement=>Environnement,_}
import Personnage.{Jeton=>Jeton,_}
import Game.{Game=>Game}
import Movable._
import Sprite._
import Utilities.utils._
import Loot._
import Inventory._
import Mechanisms._
import Schematics._

object app extends JFXApp {
	private var mousePosX: Double = .0
	private var mousePosY: Double = .0
	private var mouseOldX: Double = .0
	private var mouseOldY: Double = .0
	
	private var canvas:scalafx.scene.canvas.Canvas = new Canvas(800,800)
	var gc:scalafx.scene.canvas.GraphicsContext = canvas.graphicsContext2D
	
	private val pane = new Group
	pane.children = List(canvas)
	var Env:Environnement = new Environnement
	
	stage = new JFXApp.PrimaryStage {
		
		title.value = "Game"
	
		scene = new Scene{
			content = pane
		}
	}
	Game.initialize()
	//private var env_images:Array[Array[Image]] = Array.ofDim[Image](Env.size_x,Env.size_y)
	//private var sprites_images:Array[Array[List[(Image,Int)]]]= Array.ofDim[List[(Image,Int)]](Env.size_x,Env.size_y)
	
	private var message_buffer:ListBuffer[String] = ListBuffer()
	private var message_active:Boolean = false
	private val timer = 2 //Nb de secondes d'affichage par message
	private var time = 0 //Compteur pour savoir depuis cb de temps le message est affiché
	//Game.initialize()
	
	def unknown_path()={
		this.get_path("unknown.png")
	}
	
	def load_commands()={
		//charge les commandes du jeu
		this.load_drag_selection_command()
		this.right_click_command_load()
		this.load_colored_cursors() //-> Les images sont introuvables !!! (même avec un chemin absolu)
		}
		
	def win_screen()={
		this.Env.phase = -1
		this.gc.fill = Green
		this.gc.fillText("WIN !",375,350)
		this.gc.fillText("Good Job you beat the dongeon with your turtle alive",250,400)
	}
	
	def lose_screen()={
		this.Env.phase = -1
		this.gc.fill = Red
		this.gc.fillText("YOU LOSE !",375,350)
		this.gc.fillText("You should take care of your turtle next time",250,400)
	}
	
	def aff_text(x:Int,y:Int,text:String){
		this.gc.fill = Black
		this.gc.fillText(text,x,y,text.length)
	}
		
	def load_colored_cursors()={
		//Colorie le curseur en fonction de l'unité qu'il y a en dessous
		//Pour le moment ce programme plante
		this.canvas.onMouseMoved = (e: MouseEvent) =>
			{val x = (e.x/32).toInt
			val y = (e.y/32).toInt
			val path = this.Env.units(x)(y) match{
				case None => 	this.get_path("cursor_black.png")
				case Some(j:Jeton) => if (j.model.player == Game.Human){
											this.get_path("cursor_green.png")
										} else {
											this.get_path("cursor_red.png")
										}
				}
			val image = new Image(path)
			this.stage.scene.value.setCursor(new ImageCursor(image))
			}
	}
	
	def load_drag_selection_command()={
		//Permet de selectionner des unités
		def borne_x(nb:Int):Int={
			return Math.min(Math.max(nb,0),Env.size_x-1)
			}
		def borne_y(nb:Int):Int={
			return Math.min(Math.max(nb,0),Env.size_y-1)
			}
		def use_item(i:Int):Unit={
			print("use item\n")
			this.Env.selected_unit match { 
				case None => () 
				case Some(u) => print("unit selected\n");u.model.inventory(i) match { 
													case None => () 
													case Some(o) => print(o);o match {
																		case ou : OneUse => print("ou\n");ou.use(u.model)
																		case w : Weapon => print("weapon\n");u.model.swap_weapon(i)
																		case a : Armor => u.model.swap_armor(i)
																		case x => print("Unrecognize weapon \n")
													}
								}
			}
		}
		this.stage.scene.value.onKeyPressed = (ke: KeyEvent) => {
			print("Key Pressed : "+ke.code.toString+" "+this.Env.phase.toString+"\n")
			(ke.code,this.Env.phase) match  {
				case (KeyCode.Ampersand,0)|(KeyCode.DIGIT1,0) => if (Game.Human.units.length >= 1) {this.Env.select_unit(Game.Human.units(0).jeton)}
				case (KeyCode.Undefined,0)|(KeyCode.DIGIT2,0) => if (Game.Human.units.length >= 2) {this.Env.select_unit(Game.Human.units(1).jeton)}
				case (KeyCode.Quotedbl,0)|(KeyCode.DIGIT3,0) => if (Game.Human.units.length >= 3) {this.Env.select_unit(Game.Human.units(2).jeton)}
				case (KeyCode.Quote,0)|(KeyCode.DIGIT4,0) => if (Game.Human.units.length >= 4) {this.Env.select_unit(Game.Human.units(3).jeton)}
				case (KeyCode.Control,0)|(KeyCode.DIGIT5,0) => if (Game.Human.units.length >= 5) {this.Env.select_unit(Game.Human.units(4).jeton)}
				case (KeyCode.LeftParenthesis,0)|(KeyCode.DIGIT6,0) => if (Game.Human.units.length >= 6) {this.Env.select_unit(Game.Human.units(5).jeton)}
				case (KeyCode.A,0) => 	//Launch Spell
										this.Env.selected_unit match {
										case None => print("No one selected\n")
										case Some(j:Jeton) => 
											print("Jeton selected\n")
											j.model.actives("Spell_Ball").refresh(Array())
										}
				//Use items
				case (KeyCode.F1,0) => use_item(0)
				case (KeyCode.F2,0) => use_item(1)
				case (KeyCode.F3,0) => use_item(2)
				case (KeyCode.F4,0) => use_item(3)
				case (KeyCode.F5,0) => use_item(4)
				case (KeyCode.F6,0) => use_item(5)
				//Phase 2
				case (KeyCode.Q,2) =>
					this.Env.inv_tabs.left_tab()
				case (KeyCode.D,2) =>
					this.Env.inv_tabs.right_tab()
				case (KeyCode.Z,2) => this.Env.inv_tabs.down_item()
				case (KeyCode.S,2) => this.Env.inv_tabs.up_item()
				//Switch inv
				case (KeyCode.Ampersand,2)|(KeyCode.DIGIT1,2) => if (Game.Human.units.length >= 1) {this.Env.inv_tabs.send_item(1)}
				case (KeyCode.Undefined,2)|(KeyCode.DIGIT2,2) => if (Game.Human.units.length >= 2) {this.Env.inv_tabs.send_item(2)}
				case (KeyCode.Quotedbl,2)|(KeyCode.DIGIT3,2) => if (Game.Human.units.length >= 3) {this.Env.inv_tabs.send_item(3)}
				case (KeyCode.Quote,2)|(KeyCode.DIGIT4,2) => if (Game.Human.units.length >= 4) {this.Env.inv_tabs.send_item(4)}
				case (KeyCode.Control,2)|(KeyCode.DIGIT5,2) => if (Game.Human.units.length >= 5) {this.Env.inv_tabs.send_item(5)}
				case (KeyCode.LeftParenthesis,2)|(KeyCode.DIGIT6,2) => if (Game.Human.units.length >= 6) {this.Env.inv_tabs.send_item(6)}
				case (KeyCode.A,2) => this.Env.inv_tabs.send_item(0)
				//End Phase
				case (KeyCode.E,2) => {this.Env.end_stage();Game.Donjon.start()}
				case _ => println("not found : ",ke.code.toString)
			}
		}
	}
	def right_click_command_load()={
		var count = 0 //Number of rewards already taken (3 max per room)
		this.canvas.onMousePressed = (e: MouseEvent) =>
			{
				if (e.isSecondaryButtonDown && this.Env.phase == 0){
					var target = Array((e.x/32).toInt,(e.y/32).toInt)
					this.Env.apply_active("Move",target)
				}
				if (e.isPrimaryButtonDown && this.Env.phase == 1){ //Casse les mécanismes
					//Casse les mécanismes (se ferra avec des compétences précises plus tard)
					val x = (e.x/32).toInt
					val y = (e.y/32).toInt
					count += this.Env.loot_phase.mainSwitch(x,y) //On a récupéré une récompense ou non 
					//Affichage des changements de sprites
					val sprite_grid = this.Env.loot_phase.sprite_grid
					val sprite_plan = new Sprite_plan(this.Env.plan)
					/*for (i<-0 to sprite_grid.length-1){
						for (j<-0 to sprite_grid.length-1){
							print(sprite_grid(i)(j))
						}
						println("")
					}*/
					sprite_plan.sprite_grid = sprite_grid.transpose
					sprite_plan.fill_layer_0135()
					sprite_plan.everything.get_layer("Mechanisms").transpose()
					val layer = sprite_plan.everything.get_layer("Mechanisms")
					val index = this.Env.layerset.get_index("Mechanisms") 
					this.Env.layerset.layers(index) = layer
					this.Env.layerset.layers(index).load_layer()
					printf("count %d\n",count)
					if (count >= 3) {
						this.Env.phase = 2 //Passe en phase 3 
						this.Env.start_inventory_phase()
						count = 0
					}
				}
			}
		}
		
	def draw_damages(dmg:Int,target:Jeton)={
		var timer = (0.5/this.Env.clock.micro_period).toInt
		def aff_damage(typage:Unit):Int={
			timer -= 1
			if (timer == 0){
				return 0
			}
			this.draw_popup(target.x,target.y,15,(if (dmg >= 0) {"-"} else {"+"}) +dmg.toString,if (dmg >= 0) {Red} else {Green})
			return 1
		}
		this.Env.clock.add_micro_event(aff_damage(_)) //reste 0.5 sec
	}

	
	def draw_dodge(jeton:Jeton)={
		var timer = (0.5/this.Env.clock.micro_period).toInt
		def aff_damage(typage:Unit):Int={
			timer -= 1
			if (timer == 0){
				return 0
			}
			this.draw_popup(jeton.x,jeton.y,15,"dodge",LightBlue)
			return 1
		}
		this.Env.clock.add_micro_event(aff_damage(_)) //reste 0.5 sec
	}
	
	def draw_miss(target:Jeton)={
		var timer = (0.5/this.Env.clock.micro_period).toInt
		def aff_damage(typage:Unit):Int={
			timer -= 1
			if (timer == 0){
				return 0
			}
			this.draw_popup(target.x,target.y,15,"miss",Grey)
			return 1
		}
		this.Env.clock.add_micro_event(aff_damage(_)) //reste 0.5 sec
	}
	
	def draw_line(x1:Int,y1:Int,x2:Int,y2:Int,color:scalafx.scene.paint.Color,projectile_length:Int){
		//Affiche un projectile pour représenter une attaque
		val timer_max = 20
		var timer = timer_max
		val r = scala.util.Random
		var x_offset = r.nextInt(16)
		var y_offset = r.nextInt(16)
		def aff_line(typage:Unit):Int={
			timer -= 1
			if (timer <= 0){
				return 0
			} else {
				this.gc.stroke = color
				val init_x = x1.toDouble*32+16
				val init_y = y1.toDouble*32+16
				val target_x = x2.toDouble*32+8+x_offset.toDouble
				val target_y = y2.toDouble*32+8+y_offset.toDouble
				val position_x = init_x*(timer.toDouble/timer_max.toDouble) + target_x*(1-timer.toDouble/timer_max.toDouble)
				val position_y = init_y*(timer.toDouble/timer_max.toDouble) + target_y*(1-timer.toDouble/timer_max.toDouble)
				val dist = Math.pow(Math.pow(target_x-init_x,2) + Math.pow(target_y-init_y,2),0.5)
				val height = (target_y-init_y)/dist*projectile_length
				val width = (target_x-init_x)/dist*projectile_length
				this.gc.strokeLine(position_x.toInt,position_y.toInt,width.toInt+position_x.toInt,height.toInt+position_y.toInt)
				return 1
			}
		}
		this.Env.clock.add_micro_event(aff_line(_))
	}
		
	def draw_shoot_line(x1:Int,y1:Int,x2:Int,y2:Int)={
		this.draw_line(x1,y1,x2,y2,Red,10)
	}
	
	def draw_popup(x:Int,y:Int,length:Int,text:String,color:scalafx.scene.paint.Color)={
		//Affiche un texte pour les dégats pris
		var timer = 20 // On suppose ne pas a voir trop d'attaque ne meme temps
		def aff_text(typage:Unit):Int={
			timer -= 1
			if (timer <= 0){
				return 0
			} else {
				this.gc.fill = color
				this.gc.fillText(text,x*32+12,y*32+2,length)
				return 1
			}
		}
		this.Env.clock.add_micro_event(aff_text)
		
	}
		
	def aff_message(text:String)={
		//Affiche une boîte de dialogue
		this.message_buffer += text
		def reset_time():Unit={
			this.time = timer*(1/this.Env.clock.micro_period).toInt
		}
		def aff_message_event():Int={
			this.message_buffer match{
				case text +: q => 
					val x = 0
					val y = 680
					val w = 800
					val h = 120
					val x_offset = 10
					val y_offset = 20
					this.gc.fill = LightGrey
					this.gc.fillRect(x,y,w,h)
					this.gc.fill = Black
					this.gc.fillText(text,x+x_offset,y+y_offset,w-2*x_offset)
					this.time -= 1
					if (time <= 0){
						this.message_buffer = q
						reset_time()
						return 1
					} else {
						return 1
					}
				case ListBuffer() => 	this.message_active = false
										return 0
			}
		}
		if (this.message_active == false){
			this.message_active = true
			reset_time()
			this.Env.clock.add_micro_event((typage:Unit) => aff_message_event())
		}
	}
	
	def get_path(path:String):String={
		//Renvoie le path dans les ressources et ajoute .png a la fin
		val abs_path = System.getProperty("user.dir")
		return "file://"+abs_path+"/src/main/ressources/"+path
	}
	
	def aff_life_bars() = {
		//Affiche les barres de pv
		var color1 = Blue
		var color2 = Blue
		for (i <- 0 to Env.units.length-1){
			for (j <- 0 to Env.units(i).length-1){
				Env.units(i)(j) match{
					case None => ()
					case Some(jeton:Jeton) =>
						if (jeton.model.pv_max != 0){
							if (jeton.model.pv_current.toDouble/jeton.model.pv_max.toDouble > 0.8){
								color1 = Green
								color2 = DarkGreen
							} else { if (jeton.model.pv_current.toDouble/jeton.model.pv_max.toDouble > 0.3){
										color1 = Orange
										color2 = DarkOrange
									} else {
										color1 = Red
										color2 = DarkRed
									}
							}
							var length = 24*jeton.model.pv_current.toDouble/jeton.model.pv_max.toDouble
							if (jeton.selected){
								this.gc.fill = color1
							} else { this.gc.fill = color2 }
							this.gc.fillRect(i*32+4,j*32-2,length.toInt+1,5) //Le +1 sert a ce que la barre disparaisse exactement quand la cible va disparaitre
							//C'est illisible de mettre les pv au dessus de la barre de pv (trop petit)
							//this.gc.fill = color2
							//this.gc.fillText(jeton.model.pv_current.toString+"/"+jeton.model.pv_max.toString,i*32-2,j*32+10)
						}
				}
			}
		}
	}
	
	def load_static_layers()={
		val static_layers = Env.layerset.get_static_layers()
		static_layers.map( (e:Int) => this.load_layer(e) )//this.Env.layerset.layers(e).load_layer() )
	}
	
	def load_refresh_layers()={
		val refresh_layers = this.Env.layerset.get_refresh_layers()
		refresh_layers.map( (e:Int) => this.load_layer(e) )
	}
	
	def load_layer(k:Int)={
		this.Env.layerset.layers(k).load_layer()
	}
	
	def aff_layers() = {
		def aff_image(x:Int,y:Int,image:Image,o:Orientation)={
			def rotate(x:Double,y:Double,d2:Double):(Int,Int)={
				//d est donné en degrés donc il faut le convertir
				val d = d2/180*3.141592654 //Math.Pi ne marchait pas			
				val x2 = (x*Math.cos(d) - y*Math.sin(d))
				val y2 = x*Math.sin(d) + y*Math.cos(d)
				return ((x2+0.5).toInt,(y2+0.5).toInt) //Arrondis
			}
			def toInt(b:Boolean):Int={
				if (b) {return 1} else {return 0}
			}
			val deg = (o match {
							case Left() => 270.0
							case Right() => 90.0
							case Top() => 0.0
							case Bottom() => 180.0
						})
			this.gc.save()
			this.gc.translate(x+16,y+16)
			this.gc.rotate(deg)
			//val y_offset = toInt(List(90,180).contains(Math.abs(deg).toInt%360))
			//val x_offset = toInt(List(180,270).contains(Math.abs(deg).toInt%360)) 
			//var t2 = rotate(x+(x_offset)*32.0,y+(y_offset)*32.0,-deg)
			this.gc.drawImage(image,-16,-16)
			this.gc.restore()
		}
		for (k <- 0 to this.Env.layerset.layers.length-1){ //Respect de l'empilement des layers
			this.Env.layerset.layers(k).content.map( (e:LocatedSprite) => aff_image(e.x,e.y,e.image,e.orientation) )
		}
	}
}