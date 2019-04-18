package Loot
import Inventory._
import Environnement._
import Mechanisms._
import Schematics._
import Personnage._

class Loot_phase(m : mainInventory, sprite_grid : Array[Array[Sprite_group]], plan: Plan) {

	def check(compet : String) : Boolean = {
		for (p <- m.equipe) {
			if (!(p.equipment(0).isEmpty)) {
				if (p.equipment(0).get.element == compet) {
					return true
				}
			}
			if (!(p.equipment(1).isEmpty)) {
				if (p.equipment(1).get.element == compet) {
					return true
				}
			}
		}
		return false
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

	
	def destruct(x:Int,y:Int) = {
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
   
	def activation(x:Int, y:Int, c:Int) = {
		(this.sprite_grid(x)(y)) match {
			case Circuit(c1,c2,d,false) =>
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
    			case x => ()
    		}
	}
}