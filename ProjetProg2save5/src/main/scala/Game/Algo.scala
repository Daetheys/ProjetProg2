package Algo
import Environnement.{Environnement=>Environnement}
import Personnage.{Jeton=>Jeton}
import bddPersonnages.{bddPersonnages=>bddp}
import scala.collection.mutable.{ListBuffer,PriorityQueue,ArrayBuffer}
import Schematics.{Tile=>Tile}


class FileP{
	var array:ArrayBuffer[((Int,Int),Double)] = ArrayBuffer()
	var dict = scala.collection.mutable.Map[(Int,Int),Double]() //Position -> priority
	
	private def find_place(a:Int,b:Int,key:Double):Int={
		val c = (b+a)/2
		//print(this.array.toString+" "+a.toString+" "+b.toString+" "+key.toString+"\n")
		if (b-a <= 1){
			if (this.array(a)._2 >= key){
				return a
			}else if (this.array(b)._2 >= key) {
				return b
			} else {
				return b+1
			}
		}
		if (this.array(c)._2 <= key){
			return find_place(c,b,key)
		} else {
			return find_place(a,c,key)
		}
	}
	
	def insert(elmt:(Int,Int),key:Double):Unit={
		this.dict(elmt) = key
		if (this.array.length == 0){
			this.array += ((elmt,key))
		} else {
			this.array.insert(this.find_place(0,this.array.length-1,key),(elmt,key))
		}
	}
	
	def removeMin():(Int,Int)={
		//print(this.array.toString+"\n")
		val r = this.array(0)
		this.array.remove(0)
		this.dict = this.dict - r._1
		return r._1
	}
	
	def removeKey(e:(Int,Int))={
		val key = this.dict(e)
		this.dict = this.dict - e
		this.array.remove(this.find_place(0,this.array.length-1,key))
	}
	
	def decreaseKey(v:(Int,Int),value:Double){
		this.removeKey(v)
		this.insert(v,value)
	}
	
	def contains(e:(Int,Int)):Boolean={
		this.dict.contains(e)
	}
	
	def isEmpty():Boolean={
		return (this.array.length == 0)
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
		//print(path.toString+"\n")
		path match {
			case t::q => 
				//print(t.toString+"\n")
				return t //Prochaine destination
			case Nil => return (je.x,je.y) //Ne bouge pas
		}
	}
	private def astar_algo(Env:Environnement,je:Jeton,target:(Int,Int)):(Array[Array[(Int,Int)]],(Int,Int))={
		var P:Array[Array[(Int,Int)]] = Array.ofDim[(Int,Int)](Env.size_x,Env.size_y)
		var d:Array[Array[Double]] = Array.ofDim[Double](Env.size_x,Env.size_y)
		//var F:FibonacciHeap[(Int,Int)] = new FibonacciHeap[(Int,Int)]
		var F:FileP = new FileP
		def h(e:(Int,Int)):Double={
			return Math.pow(Math.pow(target._1-e._1,2).toInt + Math.pow(target._2-e._2,2).toInt,0.5)
		}
		def cle(e:(Int,Int)):Double={
			return d(e._1)(e._2) + h(e)
		}
		def movement(e:(Int,Int)):Array[(Int,Int)]={
			return Array((e._1+1,e._2),(e._1,e._2+1),(e._1-1,e._2),(e._1,e._2-1))
		}
		def correct(e:(Int,Int)):Boolean={
			val x_correct = e._1 < Env.size_x && 0 <= e._1
			val y_correct = e._2 < Env.size_y && 0 <= e._2
			return x_correct && y_correct && (Env.tiles(e._1)(e._2) != 1) && Env.units(e._1)(e._2) == None
		}
		def w(u:(Int,Int),v:(Int,Int)):Double={
			return 1
		}
		def cut(e:(Int,Int,Double)):(Int,Int)={
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
		F.insert( (je.x,je.y),cle((je.x,je.y)) )
		var T:ListBuffer[(Int,Int)] = ListBuffer()
		var u:(Int,Int) = (-1,-1)
		var best_case:(Int,Int) = (je.x,je.y)
		var best_approx:Double = h((je.x,je.y))
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
				//print(u.toString+" "+v.toString+" "+cle(v).toString+"/"+correct(v).toString+"->"+target.toString+"\n")
				if (correct(v)){
					if (h(v) < best_approx){
						best_approx = h(v)
						best_case = v
					}
					if (F.contains(v)){
						if (d(v._1)(v._2) > d(u._1)(u._2) + w(u,v)){
							d(v._1)(v._2) = d(u._1)(u._2) + w(u,v)
							P(v._1)(v._2) = u
							//F.delete(nodesF(v))
							//nodesF((v._1,v._2)) = F.insert((v._1,v._2),cle(v))
							F.decreaseKey(v,cle(v)) //cle(v) vient d'etre modifiée
							//F.insert((v._1,v._2),cle(v))
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
			
			
			
			