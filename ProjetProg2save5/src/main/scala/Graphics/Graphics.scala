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

object app extends JFXApp {
	private var mousePosX: Double = .0
	private var mousePosY: Double = .0
	private var mouseOldX: Double = .0
	private var mouseOldY: Double = .0
	
	private var canvas:scalafx.scene.canvas.Canvas = new Canvas(800,800)
	var gc:scalafx.scene.canvas.GraphicsContext = canvas.graphicsContext2D
	private var button = new Button("Start")
	button.onAction = new EventHandler[ActionEvent] {
            override def handle(event: ActionEvent) {
                Game.initialize()
            }
        }
	
	private val pane = new Group
	pane.children = List(canvas,button)
	var Env:Environnement = new Environnement
	
	stage = new JFXApp.PrimaryStage {
		
		title.value = "Game"
	
		scene = new Scene{
			content = pane
		}
	}
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
		//this.load_colored_cursors() //-> Les images sont introuvables !!! (même avec un chemin absolu)
		}
		
	def win_screen()={
		this.Env.phase = -1
		this.gc.fillText("WIN !",5*32,5*32,50)
	}
	
	def lose_screen()={
		this.Env.phase = -1
		this.gc.fillText("LOSE !",5*32,5*32,50)
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
			println(x,y)
			val path = this.Env.units(x)(y) match{
				case None => 	this.get_path("cursor_black.png")
				case Some(j:Jeton) => if (j.model.player == 0){
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
		this.stage.scene.value.onKeyPressed = (ke: KeyEvent) => {
			print("Key Pressed\n")
			(ke.code,this.Env.phase) match  {
				case (KeyCode.Ampersand,0) => if (Game.Human.units.length >= 1) {this.Env.select_unit(Game.Human.units(0).jeton)}
				case (KeyCode.Undefined,0) => if (Game.Human.units.length >= 2) {this.Env.select_unit(Game.Human.units(1).jeton)}
				case (KeyCode.Quotedbl,0) => if (Game.Human.units.length >= 3) {this.Env.select_unit(Game.Human.units(2).jeton)}
				case (KeyCode.Quote,0) => if (Game.Human.units.length >= 4) {this.Env.select_unit(Game.Human.units(3).jeton)}
				case (KeyCode.Control,0) => if (Game.Human.units.length >= 5) {this.Env.select_unit(Game.Human.units(4).jeton)}
				case (KeyCode.LeftParenthesis,0) => if (Game.Human.units.length >= 6) {this.Env.select_unit(Game.Human.units(5).jeton)}
				case (KeyCode.A,0) => this.Env.selected_unit match {
										case None => ()
										case Some(j:Jeton) => j.model.actives("Feu").refresh(Array())
										}
				case (KeyCode.LEFT,2) => ()
				case (KeyCode.RIGHT,2) => ()
				case (KeyCode.Up,2) => ()
				case (KeyCode.Down,2) => ()
				case _ => print(ke.code.toString+"\n")
			}
		}
		/* //Ancienne façon de faire :
		this.canvas.onMouseDragged = (e: MouseEvent) =>
			{
			this.mousePosX = e.x
			this.mousePosY = e.y
			//Affichage du rectangle de selection -> Crée trop de bugs pour le moment 
			val xrect = Math.min(this.mouseOldX,e.x)
			val yrect = Math.min(this.mouseOldY,e.y)
			val wrect = Math.abs(this.mouseOldX-e.x)
			val hrect = Math.abs(this.mouseOldY-e.y)
			this.gc.fill = Green
			this.gc.strokeRoundRect(xrect,yrect,wrect,hrect,2,2)
			//Calcul des unités selectionnées
			val x1 = borne_x((this.mouseOldX/32).toInt)
			val x2 = borne_x((e.x/32).toInt)
			val y1 = borne_y((this.mouseOldY/32).toInt)
			val y2 = borne_y((e.y/32).toInt)
			Env.select_units(x1,y1,x2,y2)
			e.consume()}
		this.canvas.onDragDetected = (e: MouseEvent) => 
			{
			this.mousePosX = e.x
			this.mousePosY = e.y
			this.mouseOldX = e.x
			this.mouseOldY = e.y
			e.consume()}*/
	}
	def right_click_command_load()={
		//Permet de déplacer les unités selectionnées
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
					//TODO TODO TODO TODO TODO TODO TODO TODO TODO
					//--------------> Detruire le mecanisme
					//TODO TODO TODO TODO TODO TODO TODO TODO TODO
					this.Env.phase = 3 //Passe en phase 3 
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
			this.draw_dmg_text(target.x,target.y,10,dmg,"-",Red)
			return 1
		}
		//print("add event+"+aff_damage(_).toString+"\n")
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
	
	def draw_dmg_text(x:Int,y:Int,length:Int,nb:Int,signe:String,color:scalafx.scene.paint.Color)={
		//Affiche un texte pour les dégats pris
		var timer = 20 // On suppose ne pas a voir trop d'attaque ne meme temps
		def aff_text(typage:Unit):Int={
			timer -= 1
			if (timer <= 0){
				return 0
			} else {
				this.gc.fill = color
				this.gc.fillText(signe+nb.toString,x*32+18,y*32+2,length)
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