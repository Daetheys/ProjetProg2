object Possibilities =
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
                    

class Room {
  var south : Array[Boolean]
  var east : Array[Boolean]
  var west : Array[Boolean]
  var north : Array[Boolean]
  var center : Array[Boolean]
  def init {
    r = new Random()
    south = Possibilities.verticals(r.nextInt(8))
    north = Possibilities.verticals(r.nextInt(8))
    east = Possibilities.outer(r.nextInt(40))
    west = Possibilities.outer(r.nextInt(40))
    center = Possibilities.inner(r.nextInt(3))
    }

abstract class Tile {
  val is_empty : Boolean
  }
  
class Obsacle extends Tile {
  val is_empty = false
  }

class Hallway extends Tile {
  val is_empty = true
  }

object Plan =
  var grid = Array.fill(21,15){new Obstacle}
  
  room = new Room
  room.init()
  
