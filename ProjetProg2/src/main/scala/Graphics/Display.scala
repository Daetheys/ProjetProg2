package Display
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
