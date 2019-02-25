package Mechanisms
// Pour la phase de loot en fin de niveau (sans exploration)
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


class Sprite_plan(plan : Plan) {
  var sprite_grid = Array.fill(25,25){List.Nil}
  val circuits = Array(Array( (6,21), (6,11), (9,8) ),
                       Array( (6,3), (6,13), (9,16) ),
                       Array( (18,3), (18,13), (15,6) ),
                       Array( (18,21), (18,11), (15,18) ) )
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
            ("sprite_tile_open-vault.png", 0),
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
  }
  def random_couple(r) {
    var c1 = r.nextInt(6);
    var c2 = r.nextInt(6);
    while ( c2 == c1 ) {
      c2 = r.nextInt(6);
    }
    return (c1, c2)
  }
  def mouv(i, j, k) {
    if( k == 0 ) {
         return (i-1,j);
      } else if( k == 1 ) {
         return (i,j+1);
      } else if( k == 2 ) {
         return (i+1,j);
      } else {
         return (i,j-1);
      }
  }
  def random_loot() {
    val r = scala.util.Random;
    for ( k <- 0 to 3 ) {
      for ( (i, j) <- circuits(k) ) {
        var (c1, c2) = this.random_couple(r);
        this.sprite_grid(i)(j) = Circuit(c1,c2,k,false);
        var (n, m) = move(i, j, k);
        while ( (plan.grid(n)(m)).is_an_obstacle ) {
          this.sprite_grid(n)(m) = Pipe(c1,k,false);
          (n, m) = move(n, m, k);
        }
        (n, m) = move(n, m, (k+2)%4);
        if ( r.nextInt(4) > 0 ) {
          this.sprite_grid(n)(m) = Vault(r.nextInt(this.stuff.length),false);
        } else {
          this.sprite_grid(n)(m) = Jail(r.nextInt(this.animal.length),false);
        }
        var l = (k+1)%4;
        (n, m) = move(i, j, l);
        while ( (plan.grid(n)(m)).is_an_obstacle ) {
          this.sprite_grid(n)(m) = Pipe(c2,l,false);
          (n, m) = move(n, m, l);
        }
        (n, m) = move(n, m, (l+2)%4);
        if ( r.nextInt(4) > 0 ) {
          this.sprite_grid(n)(m) = Vault(r.nextInt(this.stuff.length),false);
        } else {
          this.sprite_grid(n)(m) = Jail(r.nextInt(this.animal.length),false);
        }
      }
    }
  }
}
    
