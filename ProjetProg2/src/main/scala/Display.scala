{\rtf1\ansi\ansicpg1252\cocoartf1671\cocoasubrtf200
{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\paperw11900\paperh16840\margl1440\margr1440\vieww15300\viewh6960\viewkind0
\pard\tx566\tx1133\tx1700\tx2267\tx2834\tx3401\tx3968\tx4535\tx5102\tx5669\tx6236\tx6803\pardirnatural\partightenfactor0

\f0\fs24 \cf0 package Display\
\
import Mechanisms.\{Sprite_group=>Sprite_groupe,Sprite_plan=>Sprite_plan,_\}\
import Personnage.\{Personnage=>Personnage,Jeton=>Jeton,_\}\
\
class All_sprites(plan:Sprite_plan, jetons:List[Jeton]) \{\
	val main_grid = plan.all_the_sprites\
	def add_jetons():Array[Array[List[(String, Int]]] = \{\
		var g = this.main_grid;\
		for ( j <- jetons ) \{\
			g(j.x)(j.y) = g(j.x)(j.y) :+ (j.image_path, j.orientation);		\}\
		return g;\
	\}\
\}\
		}