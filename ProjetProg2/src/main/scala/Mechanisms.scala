package Mechanisms
// Pour la phase d'exploration
import Schematics.{Tile=>Tile,_}

abstract class Sprite_group
  case class Circuit(comp1 : Int,
                     comp2 : Int,
                     direc : Boolean,
                     destr : Boolean ) extends Sprite_group
  case class Vault(stuff : Int,
                   open : Boolean ) extends Sprite_group
  case class Pipe(comp : Int,
                  direc : Boolean,
                  destr : Boolean ) extends Sprite_group
  case class Jail(anim : Int,
                  open : Boolean ) extends Sprite_group

class ObstacleType(d : String, t : Tiletype) extends Obstacle {
  val description = d
  val sprite = t
}



class Sprite_grid
