package bddItem
import Inventory._


object bddItem {

	def create_first-aid-kit() : OneUse = {
		var o = new OneUse
		o.id = 1
		o.name = "Fist Aid Kit"
		o.image_path = "sprite_item_first-aid-kit.png"
		o.effect = "soigne"
		return o
	}
	
	def create_gun_confusion() : Weapon = {
		var w = new Weapon
		w.id = 2
		w.name = "Confusion Canon"
		w.image_path = "sprite_item_gun_confusion.png"
		w.element = "confusion"
		w.force = 5
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
		w.powerA = "poison_spell"
		w.effect = "empoisonne"
		return w
	}

	def create_recharge_confusion() : OneUse = {
		var o = new OneUse
		o.id = 8
		o.name = "Confusion Flash Ball"
		o.image_path = "sprite_item_recharge_confusion.png"
		o.element = "confusion"
		o.effect = "rechargeC"
		return o
	}

	def create_recharge_electricity() : OneUse = {
		var o = new OneUse
		o.id = 9
		o.name = "Electic Metal Net"
		o.image_path = "sprite_item_recharge_electricity.png"
		o.element = "electricity"
		o.effect = "rechargeE"
		return o
	}

	def create_recharge_fire() : OneUse = {
		var o = new OneUse
		o.id = 10
		o.name = "Fire Combustion Tank"
		o.image_path = "sprite_item_recharge_fire.png"
		o.element = "fire"
		o.effect = "rechargeF"
		return o
	}

	def create_recharge_ice() : OneUse = {
		var o = new OneUse
		o.id = 11
		o.name = "Ice Stabilizer Warhead"
		o.image_path = "sprite_item_recharge_ice.png"
		o.element = "ice"
		o.effect = "rechargeI"
		return o
	}

	def create_recharge_ink() : OneUse = {
		var o = new OneUse
		o.id = 12
		o.name = "Hyperdense Ink Cartridge"
		o.image_path = "sprite_item_recharge_ink.png"
		o.element = "ink"
		o.effect = "rechargeN"
		return o
	}

	def create_recharge_poison() : OneUse = {
		var o = new OneUse
		o.id = 13
		o.name = "Poison Bio Unit"
		o.image_path = "sprite_item_recharge_poison.png"
		o.element = "poison"
		o.effect = "rechargeP"
		return o
	}

	def create_vest_confusion() : Armor = {
		var a = new Armor
		a.id = 14
		a.name = "Mirror Cloak"
		a.image_path = "sprite_item_vest_confusion.png"
		a.element = "confusion"
		a.defense = 3
		a.powerP = "calm_mind"
		return a
	}

	def create_vest_electricity() : Armor = {
		var a = new Armor
		a.id = 15
		a.name = "Rubber Armor"
		a.image_path = "sprite_item_vest_electricity.png"
		a.element = "electricity"
		a.defense = 5
		a.powerP = "non_conductor"
		return a
	}

	def create_vest_fire() : Armor = {
		var a = new Armor
		a.id = 16
		a.name = "Leather Vest"
		a.image_path = "sprite_item_vest_fire.png"
		a.element = "fire"
		a.defense = 4
		a.powerP = "fire_proof"
		return a
	}

	def create_vest_confusion() : Armor = {
		var a = new Armor
		a.id = 17
		a.name = "Fur Coat"
		a.image_path = "sprite_item_vest_ice.png"
		a.element = "ice"
		a.defense = 6
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
		a.powerP = "never_sick"
		return a
	}

}

