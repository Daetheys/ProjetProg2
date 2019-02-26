package Display

import Mechanisms.{Sprite_group=>Sprite_groupe,Sprite_plan=>Sprite_plan,_}
import Personnage.{Personnage=>Personnage,Jeton=>Jeton,_}

class All_sprites(plan:Sprite_plan, jetons:List[Jeton]) {
  val main_grid = plan.all_the_sprites
  def add_jetons():Array[Array[List[(String, Int)]]] = {
    var g = this.main_grid;
    for ( j <- jetons ) {
      g(j.x)(j.y) = g(j.x)(j.y) :+ (j.image_path, j.orientation);
    }
    return g;
  }
}
