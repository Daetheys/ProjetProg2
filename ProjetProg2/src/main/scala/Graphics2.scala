package Graphics2

import javafx.event.EventHandler

import javafx.event.ActionEvent

import scalafx.scene.control.Button
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene._
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle
import scalafx.scene.canvas._
import scalafx.scene.input.{KeyCode, KeyEvent, MouseEvent}
import Environnement.{Environnement=>Environnement,_}
import Personnage.{Jeton=>Jeton,_}
import Game.{Game=>Game}

object app extends JFXApp {
	var mousePosX: Double = .0
	var mousePosY: Double = .0
	var mouseOldX: Double = .0
	var mouseOldY: Double = .0
	
	var canvas:scalafx.scene.canvas.Canvas = new Canvas(1952,1440)
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
	def aff_all()={
		//this.gc = this.canvas.graphicsContext2D
		this.aff_environnement()
		this.aff_units()
	}
	def aff_units()={
		for (i <- 0 to Env.units.length-1){
			for (j <- 0 to Env.units(i).length-1){
				Env.units(i)(j) match{
					case None => ()
					case Some(jeton) => this.gc.fill = Red
										this.gc.fillRect(i*32,j*32,32,32)
				}
			}
		}
	}
	
	def aff_environnement() ={
		for (i <- 0 to Env.tiles.length-1){
			for (j <- 0 to Env.tiles(i).length-1){
				this.gc.fill = Green
				this.gc.fillRect(i*32,j*32,32,32)
			}
		}
	}
}