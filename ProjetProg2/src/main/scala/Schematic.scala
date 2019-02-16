package Schematics

/*
The main use for this file :
  p = new Plan;
  p.random_fill();
  p.grid
returns a random array 21 * 15 where each cell is either an Obstacle or a Hallway tile
and where the hallways are placed in a coherent manner so that you can navigate in the room.
There are 307200 possible rooms with equal probability of getting any of them.
*/
  

object Possibilities{ //Pas de '=' pour les classes c'est un : nom_classe(args){...corps...}
  val verticals = Array(Array[Boolean](true,false,false,false,false),
                        Array[Boolean](false,true,false,false,false),
                        Array[Boolean](false,false,true,false,false),
                        Array[Boolean](false,false,false,true,false),
                        Array[Boolean](false,false,false,false,true),
                        Array[Boolean](true,false,false,true,false),
                        Array[Boolean](false,true,false,false,true),
                        Array[Boolean](true,false,false,false,true))
  val inner = Array(Array[Boolean](true,false,false),Array[Boolean](false,true,false),Array[Boolean](false,false,true))
  val outer = Array(Array[Boolean](true,false,false,false,false,false,false,false,false),
                    Array[Boolean](false,true,false,false,false,false,false,false,false),
                    Array[Boolean](false,false,true,false,false,false,false,false,false),
                    Array[Boolean](false,false,false,true,false,false,false,false,false),
                    Array[Boolean](false,false,false,false,true,false,false,false,false),
                    Array[Boolean](false,false,false,false,false,true,false,false,false),
                    Array[Boolean](false,false,false,false,false,false,true,false,false),
                    Array[Boolean](false,false,false,false,false,false,false,true,false),
                    Array[Boolean](false,false,false,false,false,false,false,false,true),
                    Array[Boolean](true,false,false,true,false,false,false,false,false),
                    Array[Boolean](true,false,false,false,true,false,false,false,false),
                    Array[Boolean](true,false,false,false,false,true,false,false,false),
                    Array[Boolean](true,false,false,false,false,false,true,false,false),
                    Array[Boolean](true,false,false,false,false,false,false,true,false),
                    Array[Boolean](true,false,false,false,false,false,false,false,true),
                    Array[Boolean](false,true,false,false,true,false,false,false,false),
                    Array[Boolean](false,true,false,false,false,true,false,false,false),
                    Array[Boolean](false,true,false,false,false,false,true,false,false),
                    Array[Boolean](false,true,false,false,false,false,false,true,false),
                    Array[Boolean](false,true,false,false,false,false,false,false,true),
                    Array[Boolean](false,false,true,false,false,true,false,false,false),
                    Array[Boolean](false,false,true,false,false,false,true,false,false),
                    Array[Boolean](false,false,true,false,false,false,false,true,false),
                    Array[Boolean](false,false,true,false,false,false,false,false,true),
                    Array[Boolean](false,false,false,true,false,false,true,false,false),
                    Array[Boolean](false,false,false,true,false,false,false,true,false),
                    Array[Boolean](false,false,false,true,false,false,false,false,true),
                    Array[Boolean](false,false,false,false,true,false,false,true,false),
                    Array[Boolean](false,false,false,false,true,false,false,false,true),
                    Array[Boolean](false,false,false,false,false,true,false,false,true),
                    Array[Boolean](true,false,false,true,false,false,true,false,false),
                    Array[Boolean](true,false,false,true,false,false,false,true,false),
                    Array[Boolean](true,false,false,true,false,false,false,false,true),
                    Array[Boolean](true,false,false,false,true,false,false,true,false),
                    Array[Boolean](true,false,false,false,true,false,false,false,true),
                    Array[Boolean](true,false,false,false,false,true,false,false,true),
                    Array[Boolean](false,true,false,false,true,false,false,true,false),
                    Array[Boolean](false,true,false,false,true,false,false,false,true),
                    Array[Boolean](false,true,false,false,false,true,false,false,true),
                    Array[Boolean](false,false,true,false,false,true,false,false,true))
}     

class Room {
  var south : Array[Boolean] = ??? //Tous les attributs doivent être initialisés dès le début
  var east : Array[Boolean] = ??? // Sinon la classe doit être abstraite
  var west : Array[Boolean] = ???
  var north : Array[Boolean] = ???
  var center : Array[Boolean] = ???
  def init() {
  	val r = scala.util.Random // C'est ca la bonne syntaxe pour le module random visiblement
    this.south = Possibilities.verticals(r.nextInt(8))
    this.north = Possibilities.verticals(r.nextInt(8))
    this.east = Possibilities.outer(r.nextInt(40))
    this.west = Possibilities.outer(r.nextInt(40))
    this.center = Possibilities.inner(r.nextInt(3))
    }
}

abstract class Tile {
  val is_empty : Boolean
  }
  
class Obstacle extends Tile {
  val is_empty = false
  }

class Hallway extends Tile {
  val is_empty = true
  }

class Plan{
  var grid = Array.fill(21,15){new Hallway} //Ici il y avait Obstacle a la place de Hallway
  var room = new Room						//or comme tu ajoutes remplies avec des Hallway
  def random_fill {							//c'est mal typé donc j'ai mis le type Hallway
    room.init()								//(il faut que tu verifie si c'est bien ca que
    for ( i <- 0 to 20 ) {					//tu voulais faire)
      grid(i)(0) = new Hallway;
      grid(i)(14) = new Hallway;
      if (( i > 2 && i < 8 ) || ( i > 12 && i < 18 )) {
        grid(i)(3) = new Hallway;
        grid(i)(11) = new Hallway;
      }
    }
    for ( j <- 1 to 13 ) {
      grid(0)(j) = new Hallway;
      grid(10)(j) = new Hallway;
      grid(20)(j) = new Hallway;
      if ( j > 3 && j < 11 ) {
        grid(3)(j) = new Hallway;
        grid(7)(j) = new Hallway;
        grid(13)(j) = new Hallway;
        grid(17)(j) = new Hallway;
      }
    }
    for ( i <- 0 to 4 ) {
      if (room.south(i)) {
        grid(i+3)(1) = new Hallway;
        grid(i+3)(2) = new Hallway;
        grid(17-i)(1) = new Hallway;
        grid(17-i)(2) = new Hallway;
      }
      if (room.north(i)) {
        grid(i+3)(13) = new Hallway;
        grid(i+3)(12) = new Hallway;
        grid(17-i)(13) = new Hallway;
        grid(17-i)(12) = new Hallway;
      }
    }
    for ( j <- 0 to 2 ) {
      if (room.center(j)) {
        grid(4)(j+6) = new Hallway;
        grid(5)(j+6) = new Hallway;
        grid(6)(j+6) = new Hallway;
        grid(14)(j+6) = new Hallway;
        grid(15)(j+6) = new Hallway;
        grid(16)(j+6) = new Hallway;
      }
    }
    for ( j <- 0 to 8 ) {
      if (room.east(j)) {
        grid(8)(j+3) = new Hallway;
        grid(9)(j+3) = new Hallway;
        grid(11)(j+3) = new Hallway;
        grid(12)(j+3) = new Hallway;
      }
      if (room.west(j)) {
        grid(1)(j+3) = new Hallway;
        grid(2)(j+3) = new Hallway;
        grid(19)(j+3) = new Hallway;
        grid(18)(j+3) = new Hallway;
      }
    }
  }  
}  
