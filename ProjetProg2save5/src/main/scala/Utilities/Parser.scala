package Parser

object Parseur {
	val parser_model : String = "#start# Generator : Unit 0 Turtle ( Bonus de Statistiques : STR % SPD % INT % ACC % EVA % // Equipement : Weapon ( WELEMENT % W_NAME % FORCE % CDATK % RANGE % ) Armor ( AELEMENT % A_NAME % DEFENSE % ASTR % ASPD % AINT % AACC % AEVA % ) // Inventaire Perso : [ Item 1 ( ID % ), ] ) & Unit 1 ( Personnage : P_NAME % // Bonus de Statistiques : STR % SPD % INT % ACC % EVA % // Equipement : // Inventaire Perso : [ ] ) ;; Inventaire Commun : Item 1 ( ID % QUANTITY % ) ;; Level 1 ( Couloirs : TYPE_01234 % TYPE_56789 % TYPE_ABCDEFGHI % TYPE_JKLMNOPQR % TYPE_STU % // Ennemis : [ Robot 1 ( R_NAME % POSITION % ), Robot 2 ( R_NAME % POSITION % ), Robot 3 ( R_NAME % POSITION % ), Robot 4 ( R_NAME % POSITION % ), Robot 5 ( R_NAME % POSITION % ), Robot 6 ( R_NAME % POSITION % ), ] // Mécanismes : a1 ( ELEMENT % Recompense ( R % ) ) a2 ( ELEMENT % Recompense ( R % ) ) b1 ( ELEMENT % Recompense ( R % ) ) b2 ( ELEMENT % Recompense ( R % ) ) c1 ( ELEMENT % Recompense ( R % ) ) c2 ( ELEMENT % Recompense ( R % ) ) d1 ( ELEMENT % Recompense ( R % ) ) d2 ( ELEMENT % Recompense ( R % ) ) e1 ( ELEMENT % Recompense ( R % ) ) e2 ( ELEMENT % Recompense ( R % ) ) f1 ( ELEMENT % Recompense ( R % ) ) f2 ( ELEMENT % Recompense ( R % ) ) g1 ( ELEMENT % Recompense ( R % ) ) g2 ( ELEMENT % Recompense ( R % ) ) h1 ( ELEMENT % Recompense ( R % ) ) h2 ( ELEMENT % Recompense ( R % ) ) i1 ( ELEMENT % Recompense ( R % ) ) i2 ( ELEMENT % Recompense ( R % ) ) j1 ( ELEMENT % Recompense ( R % ) ) j2 ( ELEMENT % Recompense ( R % ) ) k1 ( ELEMENT % Recompense ( R % ) ) k2 ( ELEMENT % Recompense ( R % ) ) l1 ( ELEMENT % Recompense ( R % ) ) l2 ( ELEMENT % Recompense ( R % ) ) ) & Level 2 ( Couloirs : TYPE_01234 % TYPE_56789 % TYPE_ABCDEFGHI % TYPE_JKLMNOPQR % TYPE_STU % // Ennemis : [ Robot 1 ( R_NAME % POSITION % ), Robot 2 ( R_NAME % POSITION % ), Robot 3 ( R_NAME % POSITION % ), Robot 4 ( R_NAME % POSITION % ), Robot 5 ( R_NAME % POSITION % ), Robot 6 ( R_NAME % POSITION % ), ] // Mécanismes : a1 ( ELEMENT % Recompense ( R % ) ) a2 ( ELEMENT % Recompense ( R % ) ) b1 ( ELEMENT % Recompense ( R % ) ) b2 ( ELEMENT % Recompense ( R % ) ) c1 ( ELEMENT % Recompense ( R % ) ) c2 ( ELEMENT % Recompense ( R % ) ) d1 ( ELEMENT % Recompense ( R % ) ) d2 ( ELEMENT % Recompense ( R % ) ) e1 ( ELEMENT % Recompense ( R % ) ) e2 ( ELEMENT % Recompense ( R % ) ) f1 ( ELEMENT % Recompense ( R % ) ) f2 ( ELEMENT % Recompense ( R % ) ) g1 ( ELEMENT % Recompense ( R % ) ) g2 ( ELEMENT % Recompense ( R % ) ) h1 ( ELEMENT % Recompense ( R % ) ) h2 ( ELEMENT % Recompense ( R % ) ) i1 ( ELEMENT % Recompense ( R % ) ) i2 ( ELEMENT % Recompense ( R % ) ) j1 ( ELEMENT % Recompense ( R % ) ) j2 ( ELEMENT % Recompense ( R % ) ) k1 ( ELEMENT % Recompense ( R % ) ) k2 ( ELEMENT % Recompense ( R % ) ) l1 ( ELEMENT % Recompense ( R % ) ) l2 ( ELEMENT % Recompense ( R % ) ) ) & Level 3 ( Couloirs : TYPE_01234 % TYPE_56789 % TYPE_ABCDEFGHI % TYPE_JKLMNOPQR % TYPE_STU % // Ennemis : [ Robot 1 ( R_NAME % POSITION % ), Robot 2 ( R_NAME % POSITION % ), Robot 3 ( R_NAME % POSITION % ), Robot 4 ( R_NAME % POSITION % ), Robot 5 ( R_NAME % POSITION % ), Robot 6 ( R_NAME % POSITION % ), ] // Mécanismes : a1 ( ELEMENT % Recompense ( R % ) ) a2 ( ELEMENT % Recompense ( R % ) ) b1 ( ELEMENT % Recompense ( R % ) ) b2 ( ELEMENT % Recompense ( R % ) ) c1 ( ELEMENT % Recompense ( R % ) ) c2 ( ELEMENT % Recompense ( R % ) ) d1 ( ELEMENT % Recompense ( R % ) ) d2 ( ELEMENT % Recompense ( R % ) ) e1 ( ELEMENT % Recompense ( R % ) ) e2 ( ELEMENT % Recompense ( R % ) ) f1 ( ELEMENT % Recompense ( R % ) ) f2 ( ELEMENT % Recompense ( R % ) ) g1 ( ELEMENT % Recompense ( R % ) ) g2 ( ELEMENT % Recompense ( R % ) ) h1 ( ELEMENT % Recompense ( R % ) ) h2 ( ELEMENT % Recompense ( R % ) ) i1 ( ELEMENT % Recompense ( R % ) ) i2 ( ELEMENT % Recompense ( R % ) ) j1 ( ELEMENT % Recompense ( R % ) ) j2 ( ELEMENT % Recompense ( R % ) ) k1 ( ELEMENT % Recompense ( R % ) ) k2 ( ELEMENT % Recompense ( R % ) ) l1 ( ELEMENT % Recompense ( R % ) ) l2 ( ELEMENT % Recompense ( R % ) ) ) & Level 4 ( Couloirs : TYPE_01234 % TYPE_56789 % TYPE_ABCDEFGHI % TYPE_JKLMNOPQR % TYPE_STU % // Ennemis : [ Robot 1 ( R_NAME % POSITION % ), Robot 2 ( R_NAME % POSITION % ), Robot 3 ( R_NAME % POSITION % ), Robot 4 ( R_NAME % POSITION % ), Robot 5 ( R_NAME % POSITION % ), Robot 6 ( R_NAME % POSITION % ), ] // Mécanismes : a1 ( ELEMENT % Recompense ( R % ) ) a2 ( ELEMENT % Recompense ( R % ) ) b1 ( ELEMENT % Recompense ( R % ) ) b2 ( ELEMENT % Recompense ( R % ) ) c1 ( ELEMENT % Recompense ( R % ) ) c2 ( ELEMENT % Recompense ( R % ) ) d1 ( ELEMENT % Recompense ( R % ) ) d2 ( ELEMENT % Recompense ( R % ) ) e1 ( ELEMENT % Recompense ( R % ) ) e2 ( ELEMENT % Recompense ( R % ) ) f1 ( ELEMENT % Recompense ( R % ) ) f2 ( ELEMENT % Recompense ( R % ) ) g1 ( ELEMENT % Recompense ( R % ) ) g2 ( ELEMENT % Recompense ( R % ) ) h1 ( ELEMENT % Recompense ( R % ) ) h2 ( ELEMENT % Recompense ( R % ) ) i1 ( ELEMENT % Recompense ( R % ) ) i2 ( ELEMENT % Recompense ( R % ) ) j1 ( ELEMENT % Recompense ( R % ) ) j2 ( ELEMENT % Recompense ( R % ) ) k1 ( ELEMENT % Recompense ( R % ) ) k2 ( ELEMENT % Recompense ( R % ) ) l1 ( ELEMENT % Recompense ( R % ) ) l2 ( ELEMENT % Recompense ( R % ) ) )"
	var parser_data : String = ""
}