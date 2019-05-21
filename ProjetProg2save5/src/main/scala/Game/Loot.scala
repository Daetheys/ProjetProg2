package Loot
import Inventory._
import Environnement._
import Mechanisms._
import Schematics._
import Personnage._
import bddItems.{bddItem => ibdd}
import Utilities.utils._
import Game._

abstract class Recompense
	case class Animal (a : Int) extends Recompense
	case class Stuff (s : Int) extends Recompense
	case class NoRecomp () extends Recompense


class Loot_phase(m : mainInventory, sg : Array[Array[Sprite_group]], plan : Plan) {

	val compet = Array("confusion","electricity","fire","ice","ink","poison")
	val animal = Array("bees","bird","cat","monkey","rabbit","snake")
	val stuff = Array("gun_confusion", "gun_electricity", "gun_fire", "gun_ice",
			"gun_ink", "gun_poison", "vest_confusion", "vest_electricity",
			"vest_fire", "vest_ice", "vest_ink", "vest_poison")
	val sprite_grid:Array[Array[Sprite_group]] = sg
	
	def mainSwitch(x : Int, y: Int) :Int={ //renvoie le nombre de récompenses obtenues
		var recomp : Array[Recompense] = Array(NoRecomp())
		//this.sprite_grid.transpose
		(sprite_grid(x)(y)) match {
			case Circuit(c1,c2,d,false) =>
				println("Circuit",this.compet(c1),this.compet(c2),d)
				if (this.check(this.compet(c1))) {
					println("check passed1")
					this.activation(x, y, c1, recomp)
				} else if (this.check(this.compet(c2))) {
					println("check passed2")
					this.activation(x, y, c2, recomp)
				}
			case x => print(x)
		};
		//this.sprite_grid.transpose
		recomp(0) match {
			case NoRecomp() => return 0
			case Stuff(s) =>
				m.add_to_main(s)
				return 1
			case Animal(a)=>
				Game.Human.add_unit(get_object_animal(a))
				return 1
		}
	}

	def check(compet : String) : Boolean = {
		for (p <- Game.Human.units) {
			if (!(p.weapon.isEmpty)) {
				print(p.weapon.get.element,compet)
				if (p.weapon.get.element == compet) {
					return true
				}
			}
			if (!(p.armor.isEmpty)) {
				print(p.armor.get.element,compet)
				if (p.armor.get.element == compet) {
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
  	
  	def transmove(coord:Array[Int], k:Int) {
    		if ( k == 0 ) { //Bas
         		coord(0) = coord(0)-1;
      		} else if ( k == 1 ) { //Droite
      			coord(1) = coord(1)+1;
      		} else if ( k == 2 ) { //Haut
         		coord(0) = coord(0)+1;
      		} else { //Gauche
         		coord(1) = coord(1)-1;
      		}
  	}

	
	def destruct(y:Int,x:Int) : Recompense = {
		(sprite_grid(x)(y)) match {
			case Circuit(c1,c2,di,false) =>
				this.sprite_grid(x)(y) = Circuit(c1,c2,di,true)
				return NoRecomp()
      		case Vault(s,false) =>
				this.sprite_grid(x)(y) = Vault(s,true)
				return Stuff(s)
			case Pipe(c,d,false) =>
				this.sprite_grid(x)(y) = Pipe(c,d,true)
				return NoRecomp()
			case Jail(a,false) =>
				this.sprite_grid(x)(y) = Jail(a,true)
				return Animal(a)
			case x => return NoRecomp()
		}
	}
   
	def activation(x:Int, y:Int, c:Int, r:Array[Recompense]) = {
		(sprite_grid(x)(y)) match {
			case Circuit(c1,c2,d,false) =>
				if ( c1 == c ) {
					r(0) = this.destruct(y,x);
					var coord = Array(y, x);
					val d2 = d
					println(d2,"c1 == c")
					this.move(coord, d2);
					println(plan.grid(coord(0))(coord(1)),coord(0),coord(1))
					while ( (plan.grid(coord(0))(coord(1))).is_an_obstacle ) {
						print(plan.grid(coord(0))(coord(1)),coord(0),coord(1));
						r(0) = this.destruct(coord(0),coord(1));
						this.move(coord, d2);
						print(plan.grid(coord(0))(coord(1)),coord(0),coord(1),"\n");
	  				}
				} else if ( c2 == c ) {
		  			r(0) = this.destruct(y,x);
		  			var coord = Array(y, x);
		  			val e = (d+1)%4;
		  			val d2 = e
		  			println(d2,"c2 == c")
		  			this.move(coord, d2);
		  			println(plan.grid(coord(0))(coord(1)),coord(0),coord(1))
		  			while ( (plan.grid(coord(0))(coord(1))).is_an_obstacle ) {
		  				println(plan.grid(coord(0))(coord(1)),coord(0),coord(1))
						r(0) = this.destruct(coord(0),coord(1));
						this.move(coord, d2);
						print(plan.grid(coord(0))(coord(1)),coord(0),coord(1),"\n")
		  			}
				}
    			case x => ()
    		}
	}
}