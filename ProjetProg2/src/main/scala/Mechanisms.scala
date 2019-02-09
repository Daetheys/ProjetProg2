Package Mechanisms
// Pour la phase d'exploration
import Schematics.{Obstacle=>Obstacle,_}

abstract class Tiletype
  case class Circuit(c1:Int,c2:Int,dir:Boolean) extends Tiletype
  case class Vault extends Tiletype
  case class Pipe(c:Int,dir:Boolean) extends Tiletype
  case class Plain

class ObstacleType(d : String, t : Tiletype) extends Obstacle {
  val description = d
  val sprite = t
}
