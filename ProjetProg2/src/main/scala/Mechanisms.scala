package Mechanisms
// Pour la phase d'exploration
import Schematics.{Tile=>Tile,Plan=>Plan,_}

abstract class Sprite_group
  case class Circuit(comp1 : Int,
                     comp2 : Int,
                     direc : Int,
                     destr : Boolean ) extends Sprite_group
  case class Vault(stuff : Int,
                   open : Boolean ) extends Sprite_group
  case class Pipe(comp : Int,
                  direc : Int,
                  destr : Boolean ) extends Sprite_group
  case class Jail(anim : Int,
                  open : Boolean ) extends Sprite_group
  case class Plain() extends Sprite_group


class Sprite_plan(p : Plan) {
  var sprite_grid = Array.fill(25,25){List.Nil}
  val compet = Array("confusion","electricity","fire","ice","ink","poison")
  val animal = Array("bees","bird","cat","monkey","rabbit","snake")
  val stuff = Array("confusion_gun","electricity_gun","fire_gun","ice_gun",
                    "ink_gun","poison_gun","confusion_vest","electricity_vest",
                    "fire_vest","ice_vest","ink_vest","poison_vest")
  def sprite_list : Sprite_group => List {
    case Circuit(c1,c2,di,false) =>
      List( ("sprite_mechanism_switch_" + this.compet(c1) + ".png", di),
            ("sprite_mechanism_split.png", di),
            ("sprite_mechanism_switch_" + this.compet(c2) + ".png", (di+2)%4) )
    case Circuit(c1,c2,di,true) =>
      List( ("sprite_mechanism_switch_" + this.compet(c1) + ".png", di),
            ("sprite_mechanism_split.png", di),
            ("sprite_mechanism_switch_" + this.compet(c2) + ".png", (di+2)%4),
            ("sprite_mechanism_switch_used.png", di) )
    case Vault(s,false) =>
      List( ("sprite_tile_vault.png", 0) )
    case Vault(s,true) =>
      List( ("sprite_tile_vault.png", 0),
            ("sprite_tile_open_vault.png", 0),
            ("sprite_object_" + this.stuff(s) + ".png", 0) )
    case Pipe(c,d,false) =>
      List( ("sprite_mechanism_pipe_" + this.compet(c) + ".png", d) )
    case Pipe(c,d,true) =>
      List( ("sprite_mechanism_pipe_" + this.compet(c) + ".png", d),
            ("sprite_mechanism_pipe_used.png", d) )
    case Jail(a,false) =>
      List( ("sprite_character_" + this.animal(a) + ".png", 0),
            ("sprite_tile_jail.png", 0) )
    case Jail(a,true) =>
      List( ("sprite_tile_jail.png", 0),
            ("sprite_character_" + this.animal(a) + ".png", 0))
    case Plain() => List()
  }
  def random_loot() {
    val r = scala.util.Random
  }
}
    
