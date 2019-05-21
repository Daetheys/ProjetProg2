package Generator
import Personnage._
import bddPersonnages.bddPersonnages._
import Inventory._
import Layer._
import java.io._
import Schematics._
import Mechanisms._
import Player._
import Game._
import bddItems.bddItem._

class Generator {
	val circuits = Array(Array((6,21), (6,11), (9,8)), Array((6,3), (6,13), (9,16)), Array((18,3), (18,13), (15,6)), Array((18,21), (18,11), (15,18)))
	val grid012 : Array[Array[Int]] = Array(
		Array(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
		Array(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0), 
		Array(0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0),
		Array(0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0),
		Array(0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0),
		Array(0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0),
		Array(0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0),
		Array(0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0),
		Array(0,0,1,0,0,1,1,1,1,1,0,0,1,0,0,1,1,1,1,1,0,0,1,0,0),
		Array(0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0),
		Array(0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0),
		Array(0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0),  
		Array(0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0),  
		Array(0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0),  
		Array(0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0),  
		Array(0,0,1,0,0,1,0,0,0,1,0,0,1,0,0,1,0,0,0,1,0,0,1,0,0),  
		Array(0,0,1,0,0,1,1,1,1,1,0,0,1,0,0,1,1,1,1,1,0,0,1,0,0),  
		Array(0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0),  
		Array(0,0,1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1,0,0),  
		Array(0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0),  
		Array(0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0),
		Array(0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0),
		Array(0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0),
		Array(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0),
		Array(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0))
	val address : String = "generator.txt"
	var model : List[String] = Nil
	var team : List[Personnage] = Nil
	var inventoryG : Array[Empile] = Array()
	var alllevels : List[(Array[Array[Int]], Plan, Sprite_plan, LayerSet, List[(Personnage, Int, Int)])] = Nil

	def split(s : String, l : List[String], acc : List[String]) : List[List[String]] = {
		l match {
			case Nil => return Nil
			case x::t => {
				if (x == s) {
					return acc.reverse :: split(s, t, Nil)
				} else {
					return split(s, t, x::acc)
				}
			}
		}
	}
	def get_next(l : List[String], s : String) : String = {
		l match {
			case x :: (y :: t) => {
				if (x == s) {
					return y
				} else {
					return get_next(y::t, s)
				}
			}
			case _ => ""
		}
	}
	def get_allnext(l : List[String], s : String) : List[String] = {
		l match {
			case x :: (y :: t) => {
				if (x == s) {
					return y :: get_allnext(t, s)
				} else {
					return get_allnext(y::t, s)
				}
			}
			case _ => Nil
		}
	}
	def elemtoweapon(s : String) : Weapon = {
		s match {
			case "fire" => return create_gun_fire()
			case "electricity" => return create_gun_electricity()
			case "ink" => return create_gun_ink()
			case "ice" => return create_gun_ice()
			case "poison" => return create_gun_poison()
			case "confusion" => return create_gun_confusion()
		}
	}
	def elemtoarmor(s : String) : Armor = {
		s match {
			case "fire" => return create_vest_fire()
			case "electricity" => return create_vest_electricity()
			case "ink" => return create_vest_ink()
			case "ice" => return create_vest_ice()
			case "poison" => return create_vest_poison()
			case "confusion" => return create_vest_confusion()
		}
	}
	def idtoitem(s : String) : Item = {
		s match {
			case "0" => create_first_aid_kit()
			case "1" => create_gun_confusion()
			case "2" => create_gun_electricity()
			case "3" => create_gun_fire()
			case "4" => create_gun_ice()
			case "5" => create_gun_ink()
			case "6" => create_gun_poison()
			case "7" => create_recharge_confusion()
			case "8" => create_recharge_electricity()
			case "9" => create_recharge_fire()
			case "10" => create_recharge_ice()
			case "11" => create_recharge_ink()
			case "12" => create_recharge_poison()
			case "13" => create_vest_confusion()
			case "14" => create_vest_electricity()
			case "15" => create_vest_fire()
			case "16" => create_vest_confusion()
			case "17" => create_vest_ink()
			case "18" => create_vest_poison()
			case _ => create_first_aid_kit()
		}
	}
	def nametoperso(s : String) : Personnage = {
		s match {
			case "bird" => return create_bird(Game.Human)
			case "bees" => return create_bees(Game.Human)
			case "cat" => return create_cat(Game.Human)
			case "monkey" => return create_monkey(Game.Human)
			case "snake" => return create_snake(Game.Human)
			case "rabbit" => return create_rabbit(Game.Human)
			case _ => return create_turtle(Game.Human)
		}
	}
	def ennemitoperso(s : String) : Personnage = {
		s match {
			case "defense_unit" => return create_robot(Game.IA)
			case "healer" => return create_healbot(Game.IA)
			case "assault" => return create_attackbot(Game.IA)
			case "kamikaze" => return create_explosionbot(Game.IA)
		}
	}
	def elemtoid(s : String) : Int = {
		s match {
			case "fire" => 2
			case "electricity" => 1
			case "ink" => 4
			case "ice" => 3
			case "poison" => 5
			case "confusion" => 0
		}

	}
	def animaltoid(s : String) : Int = {
		s match {
			case "bird" => 1
			case "bees" => 0
			case "cat" => 2
			case "monkey" => 3
			case "snake" => 5
			case "rabbit" => 4
		}
	}
	def move(coord:Array[Int], k:Int) : Unit = {
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
	def chartoX(c : Char) : Int = {
		return c - 'a' + 2
	}
	def chartoY(c : Char) : Int = {
		return c - 'A' + 5
	}

	def turtle(l : List[String]) : Personnage = {
		val t = create_turtle(Game.Human);
		t.strength = t.strength + (this.get_next(l, "STR")).toInt;
		t.vivacity = t.vivacity + (this.get_next(l, "SPD")).toInt;
		t.intelligence = t.intelligence + (this.get_next(l, "INT")).toInt;
		t.accuracy = t.accuracy + (this.get_next(l, "ACC")).toInt;
		t.dodge = t.dodge + (this.get_next(l, "EVA")).toInt;
		if (l.contains("Weapon")) {
			val e = this.get_next(l, "WELEMENT");
			val w = this.elemtoweapon(e);
			w.force = w.force + (this.get_next(l, "FORCE")).toInt;
			w.cd_attack = (0.2 + w.cd_attack * (this.get_next(l, "CDATK")).toDouble);
			w.range = w.range + (this.get_next(l, "RANGE")).toInt;
			t.weapon = Some(w)
		}
		if (l.contains("Armor")) {
			val e = this.get_next(l, "AELEMENT");
			val a = this.elemtoarmor(e);
			a.bonus_strength = a.bonus_strength + (this.get_next(l, "ASTR")).toInt;
			a.bonus_vivacity = a.bonus_vivacity + (this.get_next(l, "ASPD")).toInt;
			a.bonus_intelligence = a.bonus_intelligence + (this.get_next(l, "AINT")).toInt;
			a.bonus_accuracy = a.bonus_accuracy + (this.get_next(l, "AACC")).toInt;
			a.bonus_dodge = a.bonus_dodge + (this.get_next(l, "AEVA")).toInt;
			t.armor = Some(a)
		}
		if (l.contains("Item")) {
			val ids = this.get_allnext(l, "ID");
			var max = 6;
			for (id <- ids) {
				if (max > 0) {
					max = max - 1;
					t.add_item(this.idtoitem(id))
				}
			}
		}
		return t
		
	}
	def animal(l : List[String]) : Personnage = {
		val t = this.nametoperso(this.get_next(l, "P_NAME"));
		t.strength = t.strength + (this.get_next(l, "STR")).toInt;
		t.vivacity = t.vivacity + (this.get_next(l, "SPD")).toInt;
		t.intelligence = t.intelligence + (this.get_next(l, "INT")).toInt;
		t.accuracy = t.accuracy + (this.get_next(l, "ACC")).toInt;
		t.dodge = t.dodge + (this.get_next(l, "EVA")).toInt;
		if (l.contains("Weapon")) {
			val e = this.get_next(l, "WELEMENT");
			val w = this.elemtoweapon(e);
			w.force = w.force + (this.get_next(l, "FORCE")).toInt;
			w.cd_attack = 0.2 + w.cd_attack * (this.get_next(l, "CDATK")).toDouble;
			w.range = w.range + (this.get_next(l, "RANGE")).toInt;
			t.weapon = Some(w)
		}
		if (l.contains("Armor")) {
			val e = this.get_next(l, "AELEMENT");
			val a = this.elemtoarmor(e);
			a.bonus_strength = a.bonus_strength + (this.get_next(l, "ASTR")).toInt;
			a.bonus_vivacity = a.bonus_vivacity + (this.get_next(l, "ASPD")).toInt;
			a.bonus_intelligence = a.bonus_intelligence + (this.get_next(l, "AINT")).toInt;
			a.bonus_accuracy = a.bonus_accuracy + (this.get_next(l, "AACC")).toInt;
			a.bonus_dodge = a.bonus_dodge + (this.get_next(l, "AEVA")).toInt;
			t.armor = Some(a)
		}
		if (l.contains("Item")) {
			val ids = this.get_allnext(l, "ID");
			var max = 6;
			for (id <- ids) {
				if (max > 0) {
					max = max - 1;
					t.add_item(this.idtoitem(id))
				}
			}
		}
		return t
		
	}
	
	

	def get_model() : Unit = {
		this.model = scala.io.Source.fromFile(this.address).getLines.flatMap(_.split("\\W+")).toList;
		val parts = this.split(";;", this.model, Nil);
		val teammembers = this.split("&", parts.head, Nil);
		this.team = (this.turtle(teammembers.head))::(this.team)
		for (member <- teammembers.tail) {
			this.team = (this.animal(member))::(this.team)
		}
		val inventory = parts.tail.head;
		val content : Array[Empile] = {
			val c = new Array[Empile](19)		
			c(0) = new Empile (create_first_aid_kit())
			c(1) = new Empile (create_gun_confusion())
			c(2) = new Empile (create_gun_electricity())
			c(3) = new Empile (create_gun_fire())
			c(4) = new Empile (create_gun_ice())
			c(5) = new Empile (create_gun_ink())
			c(6) = new Empile (create_gun_poison())
			c(7) = new Empile (create_recharge_confusion())
			c(8) = new Empile (create_recharge_electricity())
			c(9) = new Empile (create_recharge_fire())
			c(10) = new Empile (create_recharge_ice())
			c(11) = new Empile (create_recharge_ink())
			c(12) = new Empile (create_recharge_poison())
			c(13) = new Empile (create_vest_confusion())
			c(14) = new Empile (create_vest_electricity())
			c(15) = new Empile (create_vest_fire())
			c(16) = new Empile (create_vest_confusion())
			c(17) = new Empile (create_vest_ink())
			c(18) = new Empile (create_vest_poison())
			c	
		}
		val ids = this.get_allnext(inventory, "ID");
		var quantities = this.get_allnext(inventory, "QUANTITY");
		for (id <- ids) {
			content(id.toInt).incr((quantities.head).toInt);
			quantities = quantities.tail
		}
		this.inventoryG = content;
		val levels = this.split("&", parts.tail.tail.head, Nil);
		var couloir = "";
		for (lvl <- levels) {
			var g = this.grid012;
			couloir = this.get_next(lvl, "TYPE_01234");
			var xi = 0;
			for (i <- 0 to 4) {
				xi = couloir.charAt(i) - '0';
				g(6)(i+5) = xi;
				g(7)(i+5) = xi;
				g(18)(19-i) = xi;
				g(17)(19-i) = xi;
				if (xi == 2) {
					g(5)(i+5) = xi;
					g(8)(i+5) = xi;
					g(19)(19-i) = xi;
					g(16)(19-i) = xi;
				}
			}
			couloir = this.get_next(lvl, "TYPE_56789");
			for (i <- 0 to 4) {
				xi = couloir.charAt(i) - '0';
				g(6)(i+15) = xi;
				g(7)(i+15) = xi;
				g(18)(9-i) = xi;
				g(17)(9-i) = xi;
				if (xi == 2) {
					g(5)(i+15) = xi;
					g(8)(i+15) = xi;
					g(19)(9-i) = xi;
					g(16)(9-i) = xi;
				}
			}
			couloir = this.get_next(lvl, "TYPE_ABCDEFGHI");
			for (i <- 0 to 8) {
				xi = couloir.charAt(i) - '0';
				g(8+i)(3) = xi;
				g(8+i)(4) = xi;
				g(16-i)(20) = xi;
				g(16-i)(21) = xi;
				if (xi == 2) {
					g(8+i)(2) = xi;
					g(8+i)(5) = xi;
					g(16-i)(19) = xi;
					g(16-i)(22) = xi;
				}
			}
			couloir = this.get_next(lvl, "TYPE_JKLMNOPQR");
			for (i <- 0 to 8) {
				xi = couloir.charAt(i) - '0';
				g(8+i)(10) = xi;
				g(8+i)(11) = xi;
				g(16-i)(13) = xi;
				g(16-i)(14) = xi;
				if (xi == 2) {
					g(8+i)(9) = xi;
					g(8+i)(12) = xi;
					g(16-i)(12) = xi;
					g(16-i)(15) = xi;
				}
			}
			couloir = this.get_next(lvl, "TYPE_STU");
			for (i <- 0 to 2) {
				xi = couloir.charAt(i) - '0';
				g(11+i)(6) = xi;
				g(11+i)(7) = xi;
				g(11+i)(8) = xi;
				g(13-i)(16) = xi;
				g(13-i)(17) = xi;
				g(13-i)(18) = xi;
				if (xi == 2) {
					g(11+i)(5) = xi;
					g(11+i)(9) = xi;
					g(13-i)(15) = xi;
					g(13-i)(19) = xi;
				}
			}
			val plan = new Plan;
			plan.plan_init();
			plan.grid(4)(12) = plan.hallway;
			plan.grid(20)(12) = plan.hallway;
			for (i <- 11 to 13) {
				plan.grid(2)(i) = plan.hallway;
				plan.grid(3)(i) = plan.hallway;
				plan.grid(22)(i) = plan.hallway;
				plan.grid(21)(i) = plan.hallway;
			}
			for (i <- 5 to 19 ; j <- 2 to 22) {
				g(i)(j) match {
					case 0 => plan.grid(i)(j) = plan.lab_wall;
					case 1 => plan.grid(i)(j) = plan.hallway;
					case 2 => plan.grid(i)(j) = plan.waterH1;
				}
			}
			val sprites = new Sprite_plan(plan);
			var meca_elems = this.get_allnext(lvl, "ELEMENT");
			var meca_recom = this.get_allnext(lvl, "R");
			val coord = Array(0,0);
			var i = 0; var j = 0; var c1 = 0; var c2 = 0; var k = 0; var l = 0;
			for (k <- 0 to 3) {
				for (c <- this.circuits(k)) {
					i = c._1;
					j = c._2;
					c1 = this.elemtoid(meca_elems.head);
					c2 = this.elemtoid(meca_elems.tail.head);
					sprites.sprite_grid(i)(j) = Mechanisms.Circuit(c1,c2,k,false);
					meca_elems = meca_elems.tail.tail;
					coord(0) = i;
					coord(1) = j;
					this.move(coord, k);
					while ((plan.grid(coord(0))(coord(1))).is_an_obstacle) {
						sprites.sprite_grid(coord(0))(coord(1)) = Mechanisms.Pipe(c1,k,false);
						this.move(coord, k);
					}
					this.move(coord, (k+2)%4);
					if (meca_recom.head.forall(_.isDigit)) {
						sprites.sprite_grid(coord(0))(coord(1)) = Mechanisms.Vault(meca_recom.head.toInt,false);
					} else {
						sprites.sprite_grid(coord(0))(coord(1)) = Mechanisms.Jail(this.animaltoid(meca_recom.head),false);
					}
					l = (k+1)%4;
					coord(0) = i;
					coord(1) = j;
					this.move(coord, l);
					while ( (plan.grid(coord(0))(coord(1))).is_an_obstacle ) {
						sprites.sprite_grid(coord(0))(coord(1)) = Mechanisms.Pipe(c2,l,false);
						this.move(coord, l);
					}
					this.move(coord, (l+2)%4);
					if (meca_recom.tail.head.forall(_.isDigit)) {
						sprites.sprite_grid(coord(0))(coord(1)) = Mechanisms.Vault(meca_recom.tail.head.toInt,false);
					} else {
						sprites.sprite_grid(coord(0))(coord(1)) = Mechanisms.Jail(this.animaltoid(meca_recom.tail.head),false);
					}
					meca_recom = meca_recom.tail.tail;
				}
			}
			sprites.fill_layer_0135();
			var r_names = this.get_allnext(lvl, "R_NAME");
			var position = this.get_allnext(lvl, "POSITION");
			var robotlist : List[(Personnage, Int, Int)]= Nil;
			for (r <- r_names) {
				robotlist =  (this.ennemitoperso(r_names.head), this.chartoX(position.head.charAt(1)), this.chartoY(position.head.charAt(0))) :: robotlist;
				r_names = r_names.tail;
				position = position.tail;
			}
			this.alllevels =  (g, plan, sprites, sprites.everything, robotlist) :: this.alllevels
		}	
	}
}


class Generate(seed : Int) {
	val address_model : String = "model_generator.txt"
	val address_data : String = "generator.txt"
	val r = new scala.util.Random(seed)
	val range_str : Int = 10
	val range_spd : Int = 10
	val range_int : Int = 10
	val range_acc : Int = 10
	val range_eva : Int = 10
	val elements = Array("fire", "electricity", "poison", "ink", "ice", "confusion")
	val range_force : Int = 5
	val range_cdatk : Double = 0.63212055882
	val range_range : Int = 5
	val range_defense : Int = 20
	val id_max : Int = 19
	val lootmax : Int = 12
	val perso = Array("bird", "bees", "cat", "monkey", "rabbit", "snake")
	val couloirsV = Array("10000", "01000", "00100", "00010", "00001", "10010", "10001", "01001", "20000", "02000", "00200", "00020", "00002", "20010", "20001", "02001", "10020", "10002", "01002")
	val couloirsH = Array("100000000", "010000000", "001000000", "000100000", "000010000", "000001000", "000000100", "000000010", "000000001", "100100000", "100010000", "100001000", "100000100", "100000010", "100000001", "010010000", "010001000", "010000100", "010000010", "010000001", "001001000", "001000100", "001000010", "001000001", "000100100", "000100010", "000100001", "000010010", "000010001", "000001001", "100100100", "100100010", "100100001", "100010010", "100010001", "100001001", "010010010", "010010001", "010001001", "001001001", "200000000", "020000000", "002000000", "000200000", "000020000", "000002000", "000000200", "000000020", "000000002", "200100000", "200010000", "200001000", "200000100", "200000010", "200000001", "020010000", "020001000", "020000100", "020000010", "020000001", "002001000", "002000100", "002000010", "002000001", "000200100", "000200010", "000200001", "000020010", "000020001", "000002001", "200100100", "200100010", "200100001", "200010010", "200010001", "200001001", "020010010", "020010001", "020001001", "002001001", "100200000", "100020000", "100002000", "100000200", "100000020", "100000002", "010020000", "010002000", "010000200", "010000020", "010000002", "001002000", "001000200", "001000020", "001000002", "000100200", "000100020", "000100002", "000010020", "000010002", "000001002", "100200100", "100200010", "100200001", "100020010", "100020001", "100002001", "010020010", "010020001", "010002001", "001002001", "100100200", "100100020", "100100002", "100010020", "100010002", "100001002", "010010020", "010010002", "010001002", "001001002")
	val couloirsC = Array("100", "010", "001", "200", "020", "002")
	val ennemis = Array("defense_unit", "healer", "assault", "kamikaze")
	val positions = Array("Aa", "Ab", "Ac", "Ad", "Ae", "Af", "Ag", "Ah", "Ai", "Aj", "Ak", "Al", "Am", "An", "Ao", "Ap", "Aq", "Ar", "As", "At", "Au", "Ba", "Bk", "Bu", "Ca", "Ck", "Cu", "Da", "Dd", "De", "Df", "Dg", "Dh", "Dk", "Dn", "Do", "Dp", "Dq", "Dr", "Du", "Ea", "Ed", "Eh", "Ek", "En", "Er", "Eu", "Fa", "Fd", "Fh", "Fk", "Fn", "Fr", "Fu", "Ga", "Gd", "Gh", "Gk", "Gn", "Gr", "Gu", "Ha", "Hd", "Hh", "Hk", "Hn", "Hr", "Hu", "Ia", "Id", "Ih", "Ik", "In", "Ir", "Iu", "Ja", "Jd", "Jh", "Jk", "Jn", "Jr", "Ju", "Ka", "Kd", "Kh", "Kk", "Kn", "Kr", "Ku", "La", "Ld", "Le", "Lf", "Lg", "Lh", "Lk", "Ln", "Lo", "Lp", "Lq", "Lr", "Lu", "Ma", "Mk", "Mu", "Na", "Nk", "Nu", "Oa", "Ob", "Oc", "Od", "Oe", "Of", "Og", "Oh", "Oi", "Oj", "Ok", "Ol", "Om", "On", "Oo", "Op", "Oq", "Or", "Os", "Ot", "Ou")
	val proba_jail : Int = 12
	var last_element : String = ""
	var occupe : List[String] = Nil

	def weapon_name(element : String, n : Int) : String = {
		element match {
			case "fire" => return "FX-" + n.toString
			case "electricity" => return "EQ-" + n.toString
			case "poison" => return "PZ-" + n.toString
			case "ink" => return "IK-" + n.toString
			case "ice" => return "GW-" + n.toString
			case "confusion" => return "LY-" + n.toString
		}
	}
	def armor_name(element : String, n : Int) : String = {
		element match {
			case "fire" => "UB-" + n.toString
			case "electricity" => "CD-" + n.toString
			case "poison" => "NH-" + n.toString
			case "ink" => "RJ-" + n.toString
			case "ice" => "AO-" + n.toString
			case "confusion" => "MS-" + n.toString
		}
	}

	def randomItem(id : Int, r : scala.util.Random) : String = {
		id match {
			case 0 => {
				return "Item ( ID 0 )"
			}
			case 1 => {
				val force = r.nextInt(this.range_force);
				val cdatk = r.nextDouble()*this.range_cdatk;
				val range = r.nextInt(this.range_range);
				return "Weapon ( ELEMENT confusion W_NAME " + this.weapon_name("confusion",r.nextInt(1000)) + " FORCE " + force.toString() + " CDATK " + cdatk.toString() + " RANGE " + range.toString() + " ) "				}
			case 2 => {
				val force = r.nextInt(this.range_force);
				val cdatk = r.nextDouble()*this.range_cdatk;
				val range = r.nextInt(this.range_range);
				return "Weapon ( ELEMENT electricity W_NAME " + this.weapon_name("electricity",r.nextInt(1000)) + " FORCE " + force.toString() + " CDATK " + cdatk.toString() + " RANGE " + range.toString() + " ) "				}
			case 3 => {
				val force = r.nextInt(this.range_force);
				val cdatk = r.nextDouble()*this.range_cdatk;
				val range = r.nextInt(this.range_range);
				return "Weapon ( ELEMENT fire W_NAME " + this.weapon_name("fire",r.nextInt(1000)) + " FORCE " + force.toString() + " CDATK " + cdatk.toString() + " RANGE " + range.toString() + " ) "					}
			case 4 => {
				val force = r.nextInt(this.range_force);
				val cdatk = r.nextDouble()*this.range_cdatk;
				val range = r.nextInt(this.range_range);
				return "Weapon ( ELEMENT ice W_NAME " + this.weapon_name("ice",r.nextInt(1000)) + " FORCE " + force.toString() + " CDATK " + cdatk.toString() + " RANGE " + range.toString() + " ) "					}
			case 5 => {
				val force = r.nextInt(this.range_force);
				val cdatk = r.nextDouble()*this.range_cdatk;
				val range = r.nextInt(this.range_range);
				return "Weapon ( ELEMENT ink W_NAME " + this.weapon_name("ink",r.nextInt(1000)) + " FORCE " + force.toString() + " CDATK " + cdatk.toString() + " RANGE " + range.toString() + " ) "					}
			case 6 => {
				val force = r.nextInt(this.range_force);
				val cdatk = r.nextDouble()*this.range_cdatk;
				val range = r.nextInt(this.range_range);
				return "Weapon ( ELEMENT poison W_NAME " + this.weapon_name("poison",r.nextInt(1000)) + " FORCE " + force.toString() + " CDATK " + cdatk.toString() + " RANGE " + range.toString() + " ) "					}
			case 7 => {
				return "Item ( ID 7 )"
			}
			case 8 => {
				return "Item ( ID 8 )"
			}
			case 9 => {
				return "Item ( ID 9 )"
			}
			case 10 => {
				return "Item ( ID 10 )"
			}
			case 11 => {
				return "Item ( ID 11 )"
			}
			case 12 => {
				return "Item ( ID 12 )"
			}
			case 13 => {
				val defense = r.nextInt(this.range_defense);
				val str = r.nextInt(this.range_str);
				val spd = r.nextInt(this.range_spd);
				val int = r.nextInt(this.range_int);
				val acc = r.nextInt(this.range_acc);
				val eva = r.nextInt(this.range_eva);
				return "Armor ( ELEMENT confusion A_NAME " + this.armor_name("confusion",r.nextInt(1000)) + " DEFENSE " + defense.toString() + " ASTR " + str.toString() + " ASPD " + spd.toString() + " AINT " + int.toString() + " AACC " + acc.toString() + " AEVA " + eva.toString() + " ) "
			}
			case 14 => {
				val defense = r.nextInt(this.range_defense);
				val str = r.nextInt(this.range_str);
				val spd = r.nextInt(this.range_spd);
				val int = r.nextInt(this.range_int);
				val acc = r.nextInt(this.range_acc);
				val eva = r.nextInt(this.range_eva);
				return "Armor ( ELEMENT electricity A_NAME " + this.armor_name("electricity",r.nextInt(1000)) + " DEFENSE " + defense.toString() + " ASTR " + str.toString() + " ASPD " + spd.toString() + " AINT " + int.toString() + " AACC " + acc.toString() + " AEVA " + eva.toString() + " ) "
			}
			case 15 => {
				val defense = r.nextInt(this.range_defense);
				val str = r.nextInt(this.range_str);
				val spd = r.nextInt(this.range_spd);
				val int = r.nextInt(this.range_int);
				val acc = r.nextInt(this.range_acc);
				val eva = r.nextInt(this.range_eva);
				return "Armor ( ELEMENT fire A_NAME " + this.armor_name("fire",r.nextInt(1000)) + " DEFENSE " + defense.toString() + " ASTR " + str.toString() + " ASPD " + spd.toString() + " AINT " + int.toString() + " AACC " + acc.toString() + " AEVA " + eva.toString() + " ) "
			}
			case 16 => {
				val defense = r.nextInt(this.range_defense);
				val str = r.nextInt(this.range_str);
				val spd = r.nextInt(this.range_spd);
				val int = r.nextInt(this.range_int);
				val acc = r.nextInt(this.range_acc);
				val eva = r.nextInt(this.range_eva);
				return "Armor ( ELEMENT ice A_NAME " + this.armor_name("ice",r.nextInt(1000)) + " DEFENSE " + defense.toString() + " ASTR " + str.toString() + " ASPD " + spd.toString() + " AINT " + int.toString() + " AACC " + acc.toString() + " AEVA " + eva.toString() + " ) "
			}
			case 17 => {
				val defense = r.nextInt(this.range_defense);
				val str = r.nextInt(this.range_str);
				val spd = r.nextInt(this.range_spd);
				val int = r.nextInt(this.range_int);
				val acc = r.nextInt(this.range_acc);
				val eva = r.nextInt(this.range_eva);
				return "Armor ( ELEMENT ink A_NAME " + this.armor_name("ink",r.nextInt(1000)) + " DEFENSE " + defense.toString() + " ASTR " + str.toString() + " ASPD " + spd.toString() + " AINT " + int.toString() + " AACC " + acc.toString() + " AEVA " + eva.toString() + " ) "
			}
			case 18 => {
				val defense = r.nextInt(this.range_defense);
				val str = r.nextInt(this.range_str);
				val spd = r.nextInt(this.range_spd);
				val int = r.nextInt(this.range_int);
				val acc = r.nextInt(this.range_acc);
				val eva = r.nextInt(this.range_eva);
				return "Armor ( ELEMENT poison A_NAME " + this.armor_name("poison",r.nextInt(1000)) + " DEFENSE " + defense.toString() + " ASTR " + str.toString() + " ASPD " + spd.toString() + " AINT " + int.toString() + " AACC " + acc.toString() + " AEVA " + eva.toString() + " ) "
			}

		}
	}

	def replace(key : String) : String = {
		key match {
			case "STR" => {
				val s = this.r.nextInt(this.range_str);
				return s.toString()
			}
			case "SPD" => {
				val s = this.r.nextInt(this.range_spd);
				return s.toString()
			}
			case "INT" => {
				val s = this.r.nextInt(this.range_int);
				return s.toString()
			}
			case "ACC" => {
				val s = this.r.nextInt(this.range_acc);
				return s.toString()
			}
			case "EVA" => {
				val s = this.r.nextInt(this.range_eva);
				return s.toString()
			}
			case "ELEMENT" => {
				this.last_element = this.elements(this.r.nextInt(6));
				return this.last_element
			}
			case "WELEMENT" => {
				this.last_element = this.elements(this.r.nextInt(6));
				return this.last_element
			}
			case "AELEMENT" => {
				this.last_element = this.elements(this.r.nextInt(6));
				return this.last_element
			}
			case "W_NAME" => {
				return this.weapon_name(this.last_element, this.r.nextInt(1000))
			}
			case "FORCE" => {
				val s = this.r.nextInt(this.range_force);
				return s.toString()
			}
			case "CDATK" => {
				val s = this.r.nextDouble() * (this.range_cdatk);
				return s.toString()
			}
			case "RANGE" => {
				val s = this.r.nextDouble()*this.range_range;
				return s.toString()
			}
			case "A_NAME" => {
				return this.armor_name(this.last_element, this.r.nextInt(1000))
			}
			case "DEFENSE" => {
				val s = this.r.nextInt(this.range_defense);
				return s.toString()
			}
			case "ASTR" => {
				val s = this.r.nextInt(this.range_str);
				return s.toString()
			}
			case "ASPD" => {
				val s = this.r.nextInt(this.range_spd);
				return s.toString()
			}
			case "AINT" => {
				val s = this.r.nextInt(this.range_int);
				return s.toString()
			}
			case "AACC" => {
				val s = this.r.nextInt(this.range_acc);
				return s.toString()
			}
			case "AEVA" => {
				val s = this.r.nextInt(this.range_eva);
				return s.toString()
			}
			case "ID" => {
				val s = this.r.nextInt(this.id_max);
				return s.toString()
			}
			case "P_NAME" => {
				return this.perso(this.r.nextInt(6))
			}
			case "QUANTITY" => {
				return "1"
			}
			case "TYPE_01234" => {
				return this.couloirsV(this.r.nextInt(this.couloirsV.length))
			}
			case "TYPE_56789" => {
				return this.couloirsV(this.r.nextInt(this.couloirsV.length))
			}
			case "TYPE_ABCDEFGHI" => {
				return this.couloirsH(this.r.nextInt(this.couloirsH.length))
			}
			case "TYPE_JKLMNOPQR" => {
				return this.couloirsH(this.r.nextInt(this.couloirsH.length))
			}
			case "TYPE_STU" => {
				return this.couloirsC(this.r.nextInt(this.couloirsC.length))
			}
			case "R_NAME" => {
				return this.ennemis(this.r.nextInt(this.ennemis.length))
			}
			case "POSITION" => {
				var p = this.positions(this.r.nextInt(this.positions.length));
				while (this.occupe.contains(p)) {
					p = this.positions(this.r.nextInt(this.positions.length));
				}
				this.occupe = p :: this.occupe;
				return p
			}
			case "R" => {
				this.occupe = Nil;
				if (this.r.nextInt(this.proba_jail) == 0) {
					return this.perso(this.r.nextInt(6))
				} else {
					val s = this.r.nextInt(this.lootmax);
					return s.toString()
				}
			}

		}
	}
	
	def insert(model : List[String]) : List[String] = {
		model match {
			case x::("%"::t) =>
				return x :: (this.replace(x) :: insert(t))
			case _ => Nil
		}
	}
	
	def generate() : Unit = {
		val model = scala.io.Source.fromFile(this.address_model).getLines.flatMap(_.split("\\W+")).toList;
		val newgame = this.insert(model);
		val bw = new BufferedWriter(new FileWriter(this.address_data));
		for (s <- newgame) {
			bw.write(s);
			bw.write("\n")
		}
		bw.close()		
	}

}
