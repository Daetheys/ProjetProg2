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
import Mechanisms.{Sprite_group=>Sprite_groupe,Sprite_plan=>Sprite_plan,_}
import Personnage.{Personnage=>Personnage,Jeton=>Jeton,_}
import bddPersonnages.{bddPersonnages=>bddP,_}

class All_sprites(plan:Sprite_plan) {
  val main_grid = plan.all_the_sprites
  var jetons:List[Jeton] = Nil
  def add_jetons():Array[Array[List[(String, Int)]]] = {
    var g = this.main_grid;
    for ( j <- jetons ) {
      g(j.y)(j.x) = g(j.y)(j.x) :+ (j.image_path, j.orientation);
    }
    return g;
  }
  val robots_pos = Array((2,5),(9,5),(15,5),(22,5),(5,8),(19,8),(5,13),(19,13),(9,16),(15,16),(2,19),(22,19))
  def demo_version1() {
    var j_list = Nil;
    var j = (bddP.create_turtle(0)).jeton;
    j.position(12,22); j_list = j :: j_list;
    j = (bddP.create_bird(0)).jeton;
    j.position(12,20); j_list = j :: j_list;
    j = (bddP.create_cat(0)).jeton;
    j.position(11,21); j_list = j :: j_list;
    j = (bddP.create_monkey(0)).jeton;
    j.position(12,21); j_list = j :: j_list;
    j = (bddP.create_bird(0)).jeton;
    j.position(13,21); j_list = j :: j_list;
    for ( (x,y) <- this.robots_pos ) {
      j = (bddP.create_robot(1)).jeton;
      j.position(x,y); j_list = j :: j_list;
    }
    this.jetons = j_list;
  }
}
