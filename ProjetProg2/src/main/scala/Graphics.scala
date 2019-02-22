/*package Graphics

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene._
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle
import scalafx.scene.input.{KeyCode, KeyEvent, MouseEvent}
import Environnement.{Environnement=>Environnement,_}
import Personnage.{Jeton=>Jeton,_}

import scala.io.Source
import java.io.File

object app extends JFXApp {
	val root = new Group()
	val pwd:String = System.getProperty("user.dir")
	var mousePosX: Double = .0
	var mousePosY: Double = .0
	var mouseOldX: Double = .0
	var mouseOldY: Double = .0
	var Env:Option[Environnement] = None
	
	stage = new JFXApp.PrimaryStage {
		
		title.value = "Game"
	
		scene = new Scene(root,800,600){
			content = List()
		}
	}
	
	def add_content(observable:javafx.scene.Node){
			this.root.children += observable
			//this.stage.scene.content = observable::this.stage.scene.content
	}
	
	def load_commands(){
		this.Env match {
			case None => print("Erreur : il n'y a pas d'environnement")
			case Some(env:Environnement)=> {
				this.stage.scene().onMousePressed = (me: MouseEvent) => {
					if (me.isPrimaryButtonDown){
						this.mousePosX = me.sceneX
						this.mousePosY = me.sceneY
						this.mouseOldX = me.sceneX
						this.mouseOldY = me.sceneY
					}
					else if (me.isSecondaryButtonDown){
							
					}
				}
				this.stage.scene().onMouseDragged = (me: MouseEvent) => {
					if (me.isPrimaryButtonDown){
						this.mousePosX = mousePosX
						this.mousePosY = mousePosY
						this.mouseOldX = me.sceneX
						this.mouseOldY = me.sceneY
						val x1 = this.mouseOldX/32
						val x2 = this.mousePosX/32
						val y1 = this.mouseOldY/32
						val y2 = this.mousePosY/32
						env.select_units(x1.toInt,y1.toInt,x2.toInt,y2.toInt)
					}
				}
			}
		}
	}
	def add_life_bar(jeton:Jeton){
		val bar = new Rectangle {
			x <== jeton.x
			y <== jeton.y + 25 //offset a ajuster
			width <== 30*(jeton.pv_current/jeton.pv_max)
			height = 5
			fill = Green
		}
		add_content(bar)
		jeton.life_bar = Some(bar)
	}
	def add_jeton(jeton:Jeton){
		print("----------------")
		val path = this.pwd+"../ressources/sprite.png"
		val view = new Rectangle{
					x = jeton.x*32
					y = jeton.y*32
					width = 32
					height = 32
					fill = Red
				}
		//val view = 	new ImageView(val img = new Image(path) {}) {
			//this.x <== jeton.x
			//this.y <== jeton.y
		//}
		add_content(view)
		jeton.label = Some(view)
	}
	def add_Env(Env:Environnement){
		val path = "file:/src/main/ressources/tile.png"
		for (i <- 0 to Env.tiles.length){
			for (j <- 0 to Env.tiles(i).length){
				print("aaa")
				val view = new Rectangle{
					x = i*32
					y = j*32
					width = 32
					height = 32
					fill = Black
				}
				//val view = new ImageView(new Image(path)){
					//x = i*32
					//y = j*32
				//}
				Env.tiles_graphics(i)(j) = Some(view)
				add_content(view)
			}
		}
	}
}*/