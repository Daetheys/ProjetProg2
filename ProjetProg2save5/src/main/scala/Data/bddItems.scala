package bddItems
import Inventory._
import Personnage._
import scala.math._


object bddItem {

	//-----------------------------------------
	//Utilities
	def create_first_aid_kit() : OneUse = {
		var o = new OneUse
		o.id = 1
		o.name = "Fist Aid Kit"
		o.image_path = "sprite_item_first-aid-kit.png"
		o.effect = "soigne"
		def first_aid_kit(p:Personnage)= { p.pv_current = min(p.pv_current,p.pv_max) }
		o.use = first_aid_kit
		return o
	}
	
	//-----------------------------------------
	//Weapons
	def create_gun_confusion() : Weapon = {
		var w = new Weapon
		w.id = 2
		w.name = "Confusion Canon"
		w.image_path = "sprite_item_gun_confusion.png"
		w.element = "confusion"
		w.force = 5
		w.cd_attack = 0.5
		w.range = 5
		w.powerA = "confusion_spell"
		w.effect = "confus"
		return w
	}

	def create_gun_electricity() : Weapon = {
		var w = new Weapon
		w.id = 3
		w.name = "Electric Eliminator"
		w.image_path = "sprite_item_gun_electricity.png"
		w.element = "electricity"
		w.force = 8
		w.cd_attack = 0.7
		w.range = 4
		w.powerA = "electricity_spell"
		w.effect = "paralyse"
		return w
	}
		
	def create_gun_fire() : Weapon = {
		var w = new Weapon
		w.id = 4
		w.name = "Furious Flamethrower"
		w.image_path = "sprite_item_gun_fire.png"
		w.element = "fire"
		w.force = 10
		w.cd_attack = 1.2
		w.range = 6
		w.powerA = "fire_spell"
		w.effect = "brule"
		return w
	}

	def create_gun_ice() : Weapon = {
		var w = new Weapon
		w.id = 5
		w.name = "Ice IPBM"
		w.image_path = "sprite_item_gun_ice.png"
		w.element = "ice"
		w.force = 6
		w.cd_attack = 0.7
		w.range = 7
		w.powerA = "ice_spell"
		w.effect = "gele"
		return w
	}

	def create_gun_ink() : Weapon = {
		var w = new Weapon
		w.id = 6
		w.name = "Ink Incubator"
		w.image_path = "sprite_item_gun_ink.png"
		w.element = "ink"
		w.force = 7
		w.cd_attack = 0.5
		w.range = 4
		w.powerA = "ink_spell"
		w.effect = "aveugle"
		return w
	}

	def create_gun_poison() : Weapon = {
		var w = new Weapon
		w.id = 7
		w.name = "Poison Pistol"
		w.image_path = "sprite_item_gun_poison.png"
		w.element = "poison"
		w.force = 9
		w.cd_attack = 1.5
		w.range = 6
		w.powerA = "poison_spell"
		w.effect = "empoisonne"
		return w
	}
	//-----------------------------------------
	//Usefull functions
	def dummy(p:Personnage) = { }

	//-----------------------------------------
	//Recharges
	def create_recharge_confusion() : OneUse = {
		var o = new OneUse
		o.id = 8
		o.name = "Confusion Flash Ball"
		o.image_path = "sprite_item_recharge_confusion.png"
		o.element = "confusion"
		o.effect = "rechargeC"
		o.use = dummy //Doesn't do anything when used
		return o
	}

	def create_recharge_electricity() : OneUse = {
		var o = new OneUse
		o.id = 9
		o.name = "Electic Metal Net"
		o.image_path = "sprite_item_recharge_electricity.png"
		o.element = "electricity"
		o.effect = "rechargeE"
		o.use = dummy //Doesn't do anything when used
		return o
	}

	def create_recharge_fire() : OneUse = {
		var o = new OneUse
		o.id = 10
		o.name = "Fire Combustion Tank"
		o.image_path = "sprite_item_recharge_fire.png"
		o.element = "fire"
		o.effect = "rechargeF"
		o.use = dummy //Doesn't do anything when used
		return o
	}

	def create_recharge_ice() : OneUse = {
		var o = new OneUse
		o.id = 11
		o.name = "Ice Stabilizer Warhead"
		o.image_path = "sprite_item_recharge_ice.png"
		o.element = "ice"
		o.effect = "rechargeI"
		o.use = dummy //Doesn't do anything when used
		return o
	}

	def create_recharge_ink() : OneUse = {
		var o = new OneUse
		o.id = 12
		o.name = "Hyperdense Ink Cartridge"
		o.image_path = "sprite_item_recharge_ink.png"
		o.element = "ink"
		o.effect = "rechargeN"
		o.use = dummy //Doesn't do anything when used
		return o
	}

	def create_recharge_poison() : OneUse = {
		var o = new OneUse
		o.id = 13
		o.name = "Poison Bio Unit"
		o.image_path = "sprite_item_recharge_poison.png"
		o.element = "poison"
		o.effect = "rechargeP"
		o.use = dummy //Doesn't do anything when used
		return o
	}

	def create_vest_confusion() : Armor = {
		var a = new Armor
		a.id = 14
		a.name = "Mirror Cloak"
		a.image_path = "sprite_item_vest_confusion.png"
		a.element = "confusion"
		a.defense = 30
		a.bonus_strength = 20
		a.bonus_vivacity = 30
		a.bonus_intelligence = 40
		a.bonus_accuracy = 10
		a.bonus_dodge = 30
		a.powerP = "calm_mind"
		return a
	}

	def create_vest_electricity() : Armor = {
		var a = new Armor
		a.id = 15
		a.name = "Rubber Armor"
		a.image_path = "sprite_item_vest_electricity.png"
		a.element = "electricity"
		a.defense = 50
		a.bonus_strength = 10
		a.bonus_vivacity = 40
		a.bonus_intelligence = 30
		a.bonus_accuracy = 20
		a.bonus_dodge = 10
		a.powerP = "non_conductor"
		return a
	}

	def create_vest_fire() : Armor = {
		var a = new Armor
		a.id = 16
		a.name = "Leather Vest"
		a.image_path = "sprite_item_vest_fire.png"
		a.element = "fire"
		a.defense = 40
		a.bonus_strength = 40
		a.bonus_vivacity = 20
		a.bonus_intelligence = 20
		a.bonus_accuracy = 10
		a.bonus_dodge = 10
		a.powerP = "fire_proof"
		return a
	}

	def create_vest_ice() : Armor = {
		var a = new Armor
		a.id = 17
		a.name = "Fur Coat"
		a.image_path = "sprite_item_vest_ice.png"
		a.element = "ice"
		a.defense = 60
		a.bonus_strength = 50
		a.bonus_vivacity = 30
		a.bonus_intelligence = 40
		a.bonus_accuracy = 10
		a.bonus_dodge = 30
		a.powerP = "insulated_body"
		return a
	}

	def create_vest_ink() : Armor = {
		var a = new Armor
		a.id = 18
		a.name = "Hydrophobe Mantle"
		a.image_path = "sprite_item_vest_ink.png"
		a.element = "ink"
		a.defense = 2
		a.bonus_strength = 20
		a.bonus_vivacity = 30
		a.bonus_intelligence = 40
		a.bonus_accuracy = 40
		a.bonus_dodge = 10
		a.powerP = "perfect_sight"
		return a
	}

	def create_vest_poison() : Armor = {
		var a = new Armor
		a.id = 19
		a.name = "Biohazard Suit"
		a.image_path = "sprite_item_vest_poison.png"
		a.element = "poison"
		a.defense = 1
		a.bonus_strength = 20
		a.bonus_vivacity = 30
		a.bonus_intelligence = 30
		a.bonus_accuracy = 20
		a.bonus_dodge = 10
		a.powerP = "never_sick"
		return a
	}
	
	//-----------------------------------------
	//Debug items
	
	def create_gun_god() : Weapon = {
		var w = new Weapon
		w.id = 20
		w.name = "God gun"
		w.image_path = "sprite_item_gun_fire.png"
		w.element = "fire"
		w.force = 200
		w.cd_attack = 0.4
		w.range = 45
		w.powerA = "fire_spell"
		w.effect = "brule"
		return w
	}
	
	def create_vest_god() : Armor = {
		var a = new Armor
		a.id = 21
		a.name = "God vest"
		a.image_path = "sprite_item_vest_poison.png"
		a.element = "poison"
		a.defense = 1
		a.bonus_strength = 500
		a.bonus_vivacity = 500
		a.bonus_intelligence = 500
		a.bonus_accuracy = 500
		a.bonus_dodge = 500
		a.powerP = "never_sick"
		return a
	}

}

