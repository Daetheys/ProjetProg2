package Algo
import Environnement.{Environnement=>Environnement}
import Personnage.{Jeton=>Jeton}
import bddPersonnages.{bddPersonnages=>bddp}
import scala.collection.mutable.{ListBuffer,PriorityQueue}
import Schematics.{Tile=>Tile}
import mylib.misc.{FibonacciHeap,FNode}

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
	
	def astar(Env:Environnement,je:Jeton,target:(Int,Int)):(Int,Int)={
		var tuple = this.astar_algo(Env,je,target)
		var P = tuple._1
		var best_case = tuple._2
		var path:List[(Int,Int)] = List()
		def get_move(P:Array[Array[(Int,Int)]],pos:(Int,Int)):Unit={
			if (pos._1 == je.x && pos._2 == je.y){
				()
			} else {
				path = pos :: path
				P(pos._1)(pos._2) match {
					case a => get_move(P,a)
					case null => ()
				}
			}
		}
		get_move(P,(best_case._1,best_case._2))
		//println(scala.runtime.ScalaRunTime.stringOf(path))
		path match {
			case t::q => return t //Prochaine destination
			case Nil => return (je.x,je.y) //Ne bouge pas
		}
	}
	private def astar_algo(Env:Environnement,je:Jeton,target:(Int,Int)):(Array[Array[(Int,Int)]],(Int,Int))={
		var P:Array[Array[(Int,Int)]] = Array.ofDim[(Int,Int)](Env.size_x,Env.size_y)
		var d:Array[Array[Int]] = Array.ofDim[Int](Env.size_x,Env.size_y)
		var F:FibonacciHeap[(Int,Int)] = new FibonacciHeap[(Int,Int)]
		var nodesF:scala.collection.mutable.Map[(Int,Int),FNode[(Int,Int)]] = scala.collection.mutable.Map()
		def h(e:(Int,Int)):Int={
			return Math.pow(target._1-e._1,2).toInt + Math.pow(target._1-e._2,2).toInt
		}
		def cle(e:(Int,Int)):Int={
			return -(d(e._1)(e._2) + h(e)) //Les files de prio fonctionnent dans l'autre sens ! -> on a un -
		}
		def movement(e:(Int,Int)):Array[(Int,Int)]={
			return Array((e._1+1,e._2),(e._1,e._2+1),(e._1-1,e._2),(e._1,e._2-1))
		}
		def correct(e:(Int,Int)):Boolean={
			val x_correct = e._1 < Env.size_x && 0 <= e._1
			val y_correct = e._2 < Env.size_y && 0 <= e._2
			return x_correct && y_correct && (Env.tiles(e._1)(e._2) == 0) && Env.units(e._1)(e._2) == None
		}
		def w(u:(Int,Int),v:(Int,Int)):Int={
			return 1
		}
		def cut(e:(Int,Int,Int)):(Int,Int)={
			return (e._1,e._2)
		}
		def contain(v:(Int,Int),T:ListBuffer[(Int,Int)]):Boolean={
			T match {
				case t +: q => if (t._1 == v._1 && t._2 == v._2){
									true
								} else { contain(v,q) }
				case ListBuffer() => false
			}
		}
		nodesF((je.x,je.y)) = F.insert( (je.x,je.y),cle((je.x,je.y)) )
		var T:ListBuffer[(Int,Int)] = ListBuffer()
		var u:(Int,Int) = (-1,-1)
		var best_case:(Int,Int) = (je.x,je.y)
		var best_approx:Int = h((je.x,je.y))
		var mov:Array[(Int,Int)] = Array()
		var v:(Int,Int) = (-1,-1)
		while (!(F.isEmpty)) {
			u = F.removeMin()
			T += u
			if (u._1 == target._1 && u._2 == target._2){
				return (P,best_case)
			}
			mov = movement(u)
			for (i <- 0 to 3){
				v = mov(i)
				if (correct(v)){
					if (h(v) < best_approx){
						best_approx = h(v)
						best_case = v
					}
					if (nodesF.contains(v)){
						if (d(v._1)(v._2) > d(u._1)(u._2) + w(u,v)){
							d(v._1)(v._2) = d(u._1)(u._2) + w(u,v)
							P(v._1)(v._2) = u
							F.decreaseKey(nodesF(v),cle(v)) //cle(v) vient d'etre modifiée
						}
					} else {
						if (contain(v,T)) {
							if (d(v._1)(v._2) > d(u._1)(u._2) + w(u,v)){
								d(v._1)(v._2) = d(u._1)(u._2) + w(u,v)
								P(v._1)(v._2) = u
								F.insert((v._1,v._2),cle(v))
							}
						} else {
							d(v._1)(v._2) = d(u._1)(u._2) + w(u,v)
							P(v._1)(v._2) = u
							F.insert((v._1,v._2),cle(v))
						}
					}
				}
			}
		}
		return (P,best_case)
	}
}
			
			
			
			
			
			
			
			
			