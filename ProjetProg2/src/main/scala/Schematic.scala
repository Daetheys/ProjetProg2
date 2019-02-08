object Possibilities =
  val verticals = Array(Array[Bool](true,false,false,false,false),
                        Array[Bool](false,true,false,false,false),
                        Array[Bool](false,false,true,false,false),
                        Array[Bool](false,false,false,true,false),
                        Array[Bool](false,false,false,false,true),
                        Array[Bool](true,false,false,true,false),
                        Array[Bool](false,true,false,false,true),
                        Array[Bool](true,false,false,false,true))
  val inner = Array(Array[Bool](true,false,false),Array[Bool](false,true,false),Array[Bool](false,false,true))
  val outer = Array(Array[Bool](true,false,false,false,false,false,false,false,false),
                    Array[Bool](false,true,false,false,false,false,false,false,false),
                    Array[Bool](false,false,true,false,false,false,false,false,false),
                    Array[Bool](false,false,false,true,false,false,false,false,false),
                    Array[Bool](false,false,false,false,true,false,false,false,false),
                    Array[Bool](false,false,false,false,false,true,false,false,false),
                    Array[Bool](false,false,false,false,false,false,true,false,false),
                    Array[Bool](false,false,false,false,false,false,false,true,false),
                    Array[Bool](false,false,false,false,false,false,false,false,true),
                    Array[Bool](true,false,true,false,false,false,false,false,false),
                    Array[Bool](true,false,false,true,false,false,false,false,false),
                    Array[Bool](true,false,false,false,true,false,false,false,false),
                    Array[Bool](true,false,false,false,false,true,false,false,false),
                    Array[Bool](true,false,false,false,false,false,true,false,false),
                    Array[Bool](true,false,false,false,false,false,false,true,false),
                    Array[Bool](true,false,false,false,false,false,false,false,true),
                    Array[Bool](false,true,false,false,true,false,false,false,false),
                    Array[Bool](false,true,false,false,false,true,false,false,false),
                    Array[Bool](false,true,false,false,false,false,true,false,false),
                    Array[Bool](false,true,false,false,false,false,false,true,false),
                    Array[Bool](false,true,false,false,false,false,false,false,true),
                    Array[Bool](false,false,true,false,false,true,false,false,false),
                    Array[Bool](false,false,true,false,false,false,true,false,false),
                    Array[Bool](false,false,true,false,false,false,false,true,false),
                    Array[Bool](false,false,true,false,false,false,false,false,true),
                    Array[Bool](false,false,false,true,false,false,true,false,false),
                    Array[Bool](false,false,false,true,false,false,false,true,false),
                    Array[Bool](false,false,false,true,false,false,false,false,true),
                    Array[Bool](false,false,false,false,true,false,false,true,false),
                    Array[Bool](false,false,false,false,true,false,false,false,true),
                    Array[Bool](false,false,false,false,false,true,false,false,true),
                    Array[Bool](true,false,false,true,false,false,true,false,false),
                    Array[Bool](true,false,false,true,false,false,false,true,false),
                    Array[Bool](true,false,false,true,false,false,false,false,true),
                    Array[Bool](true,false,false,false,true,false,false,true,false),
                    Array[Bool](true,false,false,false,true,false,false,false,true),
                    Array[Bool](true,false,false,false,false,true,false,false,true),
                    Array[Bool](false,true,false,false,true,false,false,true,false),
                    Array[Bool](false,true,false,false,true,false,false,false,true),
                    Array[Bool](false,true,false,false,false,true,false,false,true),
                    Array[Bool](false,false,true,false,false,true,false,false,true))
                    

class Room {
  var south : Array[Bool]
  var east : Array[Bool]
  var west : Array[Bool]
  var north : Array[Bool]
  var center : Array[Bool]
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

object Plan_init =
  TODO
