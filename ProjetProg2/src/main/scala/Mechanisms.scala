package Mechanisms
// Pour créer le background et la phase de loot en fin de niveau (sans exploration)
/*
The main use for this file :
  (p is an initialized Plan from Schematics.scala)
  background = new Sprite_plan(p);
  background.random_loot();
  background.init_sprites();
  background.all_the_sprites
returns the 25 * 25 matrix where each cells contains the list of
("sprite_name.png", orientation) sorted from bottom layer to top
*/
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


class Sprite_plan(plan : Plan) {
  var sprite_grid:Array[Array[Sprite_group]] = Array.fill(25,25){Mechanisms.Plain()}
  val circuits = Array(Array( (6,21), (6,11), (9,8) ),
                       Array( (6,3), (6,13), (9,16) ),
                       Array( (18,3), (18,13), (15,6) ),
                       Array( (18,21), (18,11), (15,18) ) )
  val compet = Array("confusion","electricity","fire","ice","ink","poison")
  val animal = Array("bees","bird","cat","monkey","rabbit","snake")
  val stuff = Array("confusion_gun","electricity_gun","fire_gun","ice_gun",
                    "ink_gun","poison_gun","confusion_vest","electricity_vest",
                    "fire_vest","ice_vest","ink_vest","poison_vest")
  def sprite_list(s_g:Sprite_group):List[(String, Int)] = {
    s_g match {
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
      case Plain() => Nil
    }
  }
  def random_couple(r:scala.util.Random):(Int, Int) = {
    var c1 = r.nextInt(6);
    var c2 = r.nextInt(6);
    while ( c2 == c1 ) {
      c2 = r.nextInt(6);
    }
    return (c1, c2)
  }
  def move(coord:Array[Int], k:Int) {
    if ( k == 0 ) {
         coord(1) = coord(1)-1;
      } else if ( k == 1 ) {
         coord(0) = coord(0)+1;
      } else if ( k == 2 ) {
         coord(1) = coord(1)+1;
      } else {
         coord(0) = coord(0)-1;
      }
  }
  def random_loot() {
    val r = scala.util.Random;
    var i = 0;
    var j = 0;
    var c1 = 0;
    var c2 = 0;
    var couple = (0,0);
    val coord = Array(0,0);
    var l = 0;
    for ( k <- 0 to 3 ) {
      for ( a <- this.circuits(k) ) {
	i = a._1; j =a._2;
        couple = this.random_couple(r);
	c1 = couple._1; c2 = couple._2;
        this.sprite_grid(i)(j) = Mechanisms.Circuit(c1,c2,k,false);
        coord(0) = i; coord(1) = j;
	this.move(coord, k);
        while ( (plan.grid(coord(0))(coord(1))).is_an_obstacle ) {
          this.sprite_grid(coord(0))(coord(1)) = Mechanisms.Pipe(c1,k,false);
          this.move(coord, k);
        }
        this.move(coord, (k+2)%4);
        if ( r.nextInt(4) > 0 ) {
          this.sprite_grid(coord(0))(coord(1)) = Mechanisms.Vault(r.nextInt(this.stuff.length),false);
        } else {
          this.sprite_grid(coord(0))(coord(1)) = Mechanisms.Jail(r.nextInt(this.animal.length),false);
        }
        l = (k+1)%4;
	coord(0) = i; coord(1) = j;
        this.move(coord, l);
        while ( (plan.grid(coord(0))(coord(1))).is_an_obstacle ) {
          this.sprite_grid(coord(0))(coord(1)) = Mechanisms.Pipe(c2,l,false);
          this.move(coord, l);
        }
        this.move(coord, (l+2)%4);
        if ( r.nextInt(4) > 0 ) {
          this.sprite_grid(coord(0))(coord(1)) = Mechanisms.Vault(r.nextInt(this.stuff.length),false);
        } else {
          this.sprite_grid(coord(0))(coord(1)) = Mechanisms.Jail(r.nextInt(this.animal.length),false);
        }
      }
    }
  }
  var all_the_sprites:Array[Array[List[(String, Int)]]] = Array.fill(25,25){Nil}
  def init_sprites() {
    for ( i <- 0 to 24 ; j <- 0 to 24 ) {
      if ( (plan.grid(i)(j)).is_an_obstacle ) {
        if ( (plan.grid(i)(j)).is_plain ) {
          this.all_the_sprites(i)(j) = List(("background_tile_plain.png",0));
        } else {
          this.all_the_sprites(i)(j) = ("background_tile_obstacle.png",0) ::
                                       (this.sprite_list(this.sprite_grid(i)(j)));
        }
      } else {
        this.all_the_sprites(i)(j) = List(("background_tile_hallway.png",0));
      }
    }
  }
  def destruct(x:Int,y:Int) {
    (this.sprite_grid(x)(y)) match {
      case Circuit(c1,c2,di,false) =>
        this.sprite_grid(x)(y) = Circuit(c1,c2,di,true)
      case Vault(s,false) =>
        this.sprite_grid(x)(y) = Vault(s,true)
      case Pipe(c,d,false) =>
        this.sprite_grid(x)(y) = Pipe(c,d,true)
      case Jail(a,false) =>
        this.sprite_grid(x)(y) = Jail(a,true)
      case x => x
    }
  }   
  def activation(x:Int, y:Int, c:Int) {
    (this.sprite_grid(x)(y)) match {
      case Circuit(c1,c2,d,false) =>
      	print("ok")
        if ( c1 == c ) {
		  this.destruct(x,y);
		  var coord = Array(x, y);
		  this.move(coord, d);
		  while ( (plan.grid(coord(0))(coord(1))).is_an_obstacle ) {
			this.destruct(coord(0),coord(1));
			this.move(coord, d);
	  		}
		} else if ( c2 == c ) {
		  this.destruct(x,y);
		  var coord = Array(x, y);
		  val e = (d+1)%4;
		  this.move(coord, e);
		  while ( (plan.grid(coord(0))(coord(1))).is_an_obstacle ) {
			this.destruct(coord(0),coord(1));
			this.move(coord, e);
		  }
		}
    case x => println(x)
    }
  }
}
