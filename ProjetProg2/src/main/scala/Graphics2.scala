package Graphics2

import javafx.event.EventHandler

import javafx.event.ActionEvent

import scalafx.scene.control.Button
import scalafx.Includes._
import scalafx.application.JFXApp
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

object app extends JFXApp {
	var mousePosX: Double = .0
	var mousePosY: Double = .0
	var mouseOldX: Double = .0
	var mouseOldY: Double = .0
	
	var canvas:scalafx.scene.canvas.Canvas = new Canvas(800,600)
	var gc:scalafx.scene.canvas.GraphicsContext = canvas.graphicsContext2D
	var button = new Button("Start")
	button.onAction = new EventHandler[ActionEvent] {
            override def handle(event: ActionEvent) {
                Game.initialize()
            }
        }
	
	val pane = new Group
	pane.children = List(canvas,button)
	var Env:Environnement = new Environnement
	
	stage = new JFXApp.PrimaryStage {
		
		title.value = "Game"
	
		scene = new Scene{
			content = pane
		}
	}
	
	//Game.initialize()
	
	def initialize() ={
		
	}
	
	def load_commands()={
		this.load_drag_selection_command()
		this.right_click_command_load()
		}
	
	def load_drag_selection_command()={
		def borne_x(nb:Int):Int={
			return Math.min(Math.max(nb,0),Env.size_x-1)
			}
		def borne_y(nb:Int):Int={
			return Math.min(Math.max(nb,0),Env.size_y-1)
			}
		this.canvas.onMouseDragged = (e: MouseEvent) =>
			{
			this.mousePosX = e.x
			this.mousePosY = e.y
			val x1 = borne_x((this.mouseOldX/32).toInt)
			val x2 = borne_x((e.x/32).toInt)
			val y1 = borne_y((this.mouseOldY/32).toInt)
			val y2 = borne_y((e.y/32).toInt)
			Env.select_units(x1,y1,x2,y2)
			e.consume()}
		this.canvas.onDragDetected = (e: MouseEvent) => 
			{print("in\n")
			this.mousePosX = e.x
			this.mousePosY = e.y
			this.mouseOldX = e.x
			this.mouseOldY = e.y
			e.consume()}
	}
	def right_click_command_load()={
		this.canvas.onMousePressed = (e: MouseEvent) =>
			{
				if (e.isSecondaryButtonDown){
					var target = Array((e.x/32).toInt,(e.y/32).toInt)
					Env.apply_active("Move",target)
				}
			}
			
		}
	def aff_all()={
		//this.gc = this.canvas.graphicsContext2D
		this.aff_environnement()
		this.aff_units()
		this.aff_life_bars()
	}
	def aff_units()={
		for (i <- 0 to Env.units.length-1){
			for (j <- 0 to Env.units(i).length-1){
				Env.units(i)(j) match{
					case None => ()
					case Some(jeton:Jeton) =>
						if (jeton.selected) {
							this.gc.fill = Orange
						} else { this.gc.fill = LightGreen}
						this.gc.fillRect(i*32,j*32,32,32)
				}
			}
		}
	}
	
	def aff_life_bars() = {
		for (i <- 0 to Env.units.length-1){
			for (j <- 0 to Env.units(i).length-1){
				Env.units(i)(j) match{
					case None => ()
					case Some(jeton:Jeton) =>
						this.gc.fill = Green
						if (jeton.model.pv_max != 0){
							var length = 24*jeton.model.pv_current/jeton.model.pv_max
							this.gc.fillRect(i*32+4,j*32-2,length,5)
						}
				}
			}
		}
	}
	
	def aff_environnement() ={
		for (i <- 0 to Env.tiles.length-1){
			for (j <- 0 to Env.tiles(i).length-1){
				this.gc.fill = Grey
				this.gc.fillRect(i*32,j*32,32,32)
			}
		}
	}
}