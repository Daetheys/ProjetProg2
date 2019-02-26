package Schematics

/*
The main use for this file :
  p = new Plan;
  p.random_fill();
  p.grid
returns a random array 25 * 25 where each cell is either an Obstacle or a Hallway tile
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
  var south = Array[Boolean](false,false,false,false,false)
  var east = Array[Boolean](false,false,false,false,false,false,false,false,false)
  var west = Array[Boolean](false,false,false,false,false,false,false,false,false)
  var north = Array[Boolean](false,false,false,false,false)
  var center = Array[Boolean](false,false,false)
  def room_init() {
  	val r = scala.util.Random // C'est ca la bonne syntaxe pour le module random visiblement
    this.south = Possibilities.verticals(r.nextInt(8))
    this.north = Possibilities.verticals(r.nextInt(8))
    this.east = Possibilities.outer(r.nextInt(40))
    this.west = Possibilities.outer(r.nextInt(40))
    this.center = Possibilities.inner(r.nextInt(3))
    }
}

class Tile {
  var is_an_obstacle = true
  var is_plain = true
  def not_an_obstacle() {
    this.is_an_obstacle = false;
  }
  def change_type() {
    this.is_plain = false;
  }
}
  

class Plan {
  val background = new Tile
  var hallway = new Tile
  var lab_wall = new Tile
  def plan_init() {
    (this.hallway).not_an_obstacle();
    (this.lab_wall).change_type();
  }
  var grid = Array.fill(25,25){this.background}
  var room = new Room
  def random_fill() {
    this.plan_init();
    (this.room).room_init();
    for ( i <- 6 to 18 ; j <- 3 to 21 ) {
      this.grid(i)(j) = this.lab_wall;
    }
    for ( i <- 2 to 3 ; j <- 11 to 13 ) {
      this.grid(i)(j) = this.hallway;
      this.grid(24-i)(j) = this.hallway;
    }
    this.grid(4)(12) = this.hallway;
    this.grid(20)(12) = this.hallway;
    for ( i <- 6 to 18 ) {
      this.grid(i)(2) = this.hallway;
      this.grid(i)(12) = this.hallway;
      this.grid(i)(22) = this.hallway;
      if ( i > 7 && i < 17 ) {
        this.grid(i)(5) = this.hallway;
        this.grid(i)(9) = this.hallway;
        this.grid(i)(15) = this.hallway;
        this.grid(i)(19) = this.hallway;
      }
    }
    for ( j <- 2 to 22 ) {
      this.grid(5)(j) = this.hallway;
      this.grid(19)(j) = this.hallway;
      if ( ( j > 5 && j < 9 ) || ( j > 15 && j < 19 ) ) {
        this.grid(8)(j) = this.hallway;
        this.grid(16)(j) = this.hallway;
      }
    }
    for ( j <- 0 to 4 ) {
      if (room.south(j)) {
        this.grid(17)(j+5) = this.hallway;
        this.grid(18)(j+5) = this.hallway;
        this.grid(6)(19-j) = this.hallway;
        this.grid(7)(19-j) = this.hallway;
      }
      if (room.north(j)) {
        this.grid(6)(j+5) = this.hallway;
        this.grid(7)(j+5) = this.hallway;
        this.grid(17)(19-j) = this.hallway;
        this.grid(18)(19-j) = this.hallway;
      }
    }
    for ( i <- 0 to 2 ) {
      if (room.center(i)) {
        this.grid(i+11)(6) = this.hallway;
        this.grid(i+11)(7) = this.hallway;
        this.grid(i+11)(8) = this.hallway;
        this.grid(13-i)(16) = this.hallway;
        this.grid(13-i)(17) = this.hallway;
        this.grid(13-i)(18) = this.hallway;
      }
    }
    for ( i <- 0 to 8 ) {
      if (room.east(i)) {
        this.grid(i+8)(10) = this.hallway;
        this.grid(i+8)(11) = this.hallway;
        this.grid(16-i)(13) = this.hallway;
        this.grid(16-i)(14) = this.hallway;
      }
      if (room.west(i)) {
        this.grid(i+8)(3) = this.hallway;
        this.grid(i+8)(4) = this.hallway;
        this.grid(16-i)(20) = this.hallway;
        this.grid(16-i)(21) = this.hallway;
      }
    }
  }  
}  

