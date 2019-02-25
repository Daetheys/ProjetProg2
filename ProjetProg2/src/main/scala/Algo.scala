package Algo
import Environnement.{Environnement=>Environnement}
import Personnage.{Jeton=>Jeton}
import bddPersonnages.{bddPersonnages=>bddp}
import scala.collection.mutable.ListBuffer
import Schematics.{Tile=>Tile}

class FileP{
	var l:List[Array[Int]] = List()
	
	def add(elmt:Array[Int]):Unit={
		def find_place(e:Array[Int],l:List[Array[Int]]):List[Array[Int]]={
			l match {
				case t::q => if (t(2) >= e(2)){
								e::t::q
							} else { t::find_place(e,q) }
				case Nil => e::Nil
			}
		}
		this.l = find_place(elmt,this.l)
	}
	
	def pop():Array[Int]={
		this.l match {
			case t::q => 	this.l = q
							return t
			case Nil => print("Error : no one in file")
						Array()
		}
	}
	
	def maj(v:Array[Int],value:Int){
		def find_place(e:Array[Int],l:List[Array[Int]]):List[Array[Int]]={
			l match {
				case t::q => if (t(2) >= e(2)){
								e::t::q
							} else { t::find_place(e,q) }
				case Nil => e::Nil
			}
		}
		def remove(e:Array[Int],l:List[Array[Int]]):List[Array[Int]]={
			l match {
				case t::q => if (t(0) == e(0) && t(1) == e(1)){
								t(2) = value
								q
							} else { t::remove(e,q) }
				case Nil => print("Warning : File.maj -> remove -> not in list")
							Nil
			}
		}
		this.l = find_place(Array(v(0),v(1),value),remove(v,this.l))
	}
	
	def contains(e:Array[Int]):Boolean={
		def aux(e:Array[Int],l:List[Array[Int]]):Boolean={
			l match {
				case t::q => if (t(0) == e(0) && t(1) == e(1)){
								return true
							} else {
								aux(e,q)
							}
				case Nil => return false
			}
		}
		aux(e,this.l)
	}
	
	def not_empty():Boolean={
		this.l match {
			case t::q => true
			case Nil => false
		}
	}
}

object Algo{
	
	def astar(Env:Environnement,je:Jeton,target:Array[Int]):Array[Int]={
		var tuple = this.astar_algo(Env,je,target)
		var P = tuple._1
		var best_case = tuple._2
		var path:List[Array[Int]] = List()
		def get_move(P:Array[Array[Array[Int]]],pos:Array[Int]):Unit={
			if (pos(0) == je.x && pos(1) == je.y){
				()
			} else {
				path = pos :: path
				P(pos(0))(pos(1)) match {
					case a => get_move(P,a)
					case null => ()
				}
			}
		}
		get_move(P,Array(best_case(0),best_case(1)))
		//println(scala.runtime.ScalaRunTime.stringOf(path))
		path match {
			case t::q => return t //Prochaine destination
			case Nil => return Array(je.x,je.y) //Ne bouge pas
		}
	}
	private def astar_algo(Env:Environnement,je:Jeton,target:Array[Int]):(Array[Array[Array[Int]]],Array[Int])={
		var P:Array[Array[Array[Int]]] = Array.ofDim[Int](Env.size_x,Env.size_y,3)
		var d:Array[Array[Int]] = Array.ofDim[Int](Env.size_x,Env.size_y)
		var F:FileP = new FileP
		def h(e:Array[Int]):Int={
			return Math.pow(target(0)-e(0),2).toInt + Math.pow(target(1)-e(1),2).toInt
		}
		def cle(e:Array[Int]):Int={
			return d(e(0))(e(1)) + h(e)
		}
		def movement(e:Array[Int]):Array[Array[Int]]={
			return Array(Array(e(0)+1,e(1)),Array(e(0),e(1)+1),Array(e(0)-1,e(1)),Array(e(0),e(1)-1))
		}
		def correct(e:Array[Int]):Boolean={
			val x_correct = e(0) < Env.size_x && 0 <= e(0)
			val y_correct = e(1) < Env.size_y && 0 <= e(1)
			return x_correct && y_correct && (Env.tiles(e(0))(e(1)).is_an_obstacle == false) && Env.units(e(0))(e(1)) == None
		}
		def w(u:Array[Int],v:Array[Int]):Int={
			return 1
		}
		def cut(e:Array[Int]):Array[Int]={
			return Array(e(0),e(1))
		}
		def contain(v:Array[Int],T:ListBuffer[Array[Int]]):Boolean={
			T match {
				case t +: q => if (t(0) == v(0) && t(1) == v(1)){
									true
								} else { contain(v,q) }
				case ListBuffer() => false
			}
		}
		F.add(Array(je.x,je.y,cle(Array(je.x,je.y))))
		var T:ListBuffer[Array[Int]] = ListBuffer()
		var u:Array[Int] = Array()
		var best_case:Array[Int] = Array(je.x,je.y)
		var best_approx:Int = 10000
		while (F.not_empty) {
			u = cut(F.pop())
			T += u
			if (u(0) == target(0) && u(1) == target(1)){
				return (P,best_case)
			}
			var mov = movement(u)
			for (i <- 0 to 3){
				var v = mov(i)
				if (correct(v)){
					if (h(v) < best_approx){
						best_approx = h(v)
						best_case = v
					}
					if (F.contains(v)){
						if (d(v(0))(v(1)) > d(u(0))(u(1)) + w(u,v)){
							d(v(0))(v(1)) = d(u(0))(u(1)) + w(u,v)
							P(v(0))(v(1)) = u
							F.maj(v,cle(v)) //cle(v) vient d'etre modifiée
						}
					} else {
						if (contain(v,T)) {
							if (d(v(0))(v(1)) > d(u(0))(u(1)) + w(u,v)){
								d(v(0))(v(1)) = d(u(0))(u(1)) + w(u,v)
								P(v(0))(v(1)) = u
								F.add(Array(v(0),v(1),cle(v)))
							}
						} else {
							d(v(0))(v(1)) = d(u(0))(u(1)) + w(u,v)
							P(v(0))(v(1)) = u
							F.add(Array(v(0),v(1),cle(v)))
						}
					}
				}
			}
		}
		//print("Error : Astar no solution")
		return (P,best_case)
	}
}
			
			
			
			
			
			
			
			
			