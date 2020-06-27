package com.rex2go.mobslayer_game.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.rex2go.mobslayer_game.item.GameItem.Attribute;

public class Item {

	// --- Equip ---
	
	// Misc
	
	public final static GameItem RECIPE_BOOK;
	public final static GameItem WAND;
	public final static GameItem MOB_TRACKER;
	
	static {
		RECIPE_BOOK = new GameItem("game.general.recipe_book", null, new ItemStack(Material.BOOK), new GameItem.Attribute[]{Attribute.SOUL_BOUND});
		WAND = new GameItem("game.general.wand", null, new ItemStack(Material.STICK), new GameItem.Attribute[]{Attribute.SOUL_BOUND});
		MOB_TRACKER = new GameItem("game.general.mob_tracker", null, new ItemStack(Material.COMPASS), new GameItem.Attribute[]{});
	}
	
	// Bogen

	public static final GameItem BOW;
	public static final GameItem ARROW;

	static {
		BOW = new GameItem(null, null, new ItemStack(Material.BOW), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED});
		ARROW = new GameItem(null, null, new ItemStack(Material.ARROW), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED});
	}

	// Schwerter

	public static final GameItem STONE_SWORD;
	public static final GameItem IRON_SWORD;
	public static final GameItem DIAMOND_SWORD;

	static {
		STONE_SWORD = new GameItem(null, null, new ItemStack(Material.STONE_SWORD), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.NATIVE});
		IRON_SWORD = new GameItem(null, null, new ItemStack(Material.IRON_SWORD), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE, Attribute.NATIVE});
		DIAMOND_SWORD = new GameItem(null, null, new ItemStack(Material.DIAMOND_SWORD), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE, Attribute.NATIVE});
	}

	// Äxte
	
	public static final GameItem IRON_AXE;
	public static final GameItem DIAMOND_AXE;
	
	static {
		IRON_AXE = new GameItem(null, null, new ItemStack(Material.IRON_AXE), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE}, "item.iron_axe.lore");
		DIAMOND_AXE = new GameItem(null, null, new ItemStack(Material.DIAMOND_AXE), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE}, "item.diamond_axe.lore");
	}
	
	// Helme

	public static final GameItem LEATHER_HELMET;
	public static final GameItem CHAIN_HELMET;
	public static final GameItem IRON_HELMET;
	public static final GameItem DIAMOND_HELMET;

	static {
		LEATHER_HELMET = new GameItem(null, null, new ItemStack(Material.LEATHER_HELMET), new GameItem.Attribute[]{});
		CHAIN_HELMET = new GameItem(null, null, new ItemStack(Material.CHAINMAIL_HELMET), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE, Attribute.NATIVE});
		IRON_HELMET = new GameItem(null, null, new ItemStack(Material.IRON_HELMET), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE, Attribute.NATIVE});
		DIAMOND_HELMET = new GameItem(null, null, new ItemStack(Material.DIAMOND_HELMET), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE, Attribute.NATIVE});
	}

	// Chestplates

	public static final GameItem LEATHER_CHESTPLATE;
	public static final GameItem CHAIN_CHESTPLATE;
	public static final GameItem IRON_CHESTPLATE;
	public static final GameItem DIAMOND_CHESTPLATE;

	static {
		LEATHER_CHESTPLATE = new GameItem(null, null, new ItemStack(Material.LEATHER_CHESTPLATE), new GameItem.Attribute[]{});
		CHAIN_CHESTPLATE = new GameItem(null, null, new ItemStack(Material.CHAINMAIL_CHESTPLATE), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE, Attribute.NATIVE});
		IRON_CHESTPLATE = new GameItem(null, null, new ItemStack(Material.IRON_CHESTPLATE), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE, Attribute.NATIVE});
		DIAMOND_CHESTPLATE = new GameItem(null, null, new ItemStack(Material.DIAMOND_CHESTPLATE), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE, Attribute.NATIVE});
	}

	// Leggings

	public static final GameItem LEATHER_LEGGINGS;
	public static final GameItem CHAIN_LEGGINGS;
	public static final GameItem IRON_LEGGINGS;
	public static final GameItem DIAMOND_LEGGINGS;

	static {
		LEATHER_LEGGINGS = new GameItem(null, null, new ItemStack(Material.LEATHER_LEGGINGS), new GameItem.Attribute[]{});
		CHAIN_LEGGINGS = new GameItem(null, null, new ItemStack(Material.CHAINMAIL_LEGGINGS), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE, Attribute.NATIVE});
		IRON_LEGGINGS = new GameItem(null, null, new ItemStack(Material.IRON_LEGGINGS), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE, Attribute.NATIVE});
		DIAMOND_LEGGINGS = new GameItem(null, null, new ItemStack(Material.DIAMOND_LEGGINGS), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE, Attribute.NATIVE});
	}

	// Boots

	public static final GameItem LEATHER_BOOTS;
	public static final GameItem CHAIN_BOOTS;
	public static final GameItem IRON_BOOTS;
	public static final GameItem DIAMOND_BOOTS;

	static {
		LEATHER_BOOTS = new GameItem(null, null, new ItemStack(Material.LEATHER_BOOTS), new GameItem.Attribute[]{});
		CHAIN_BOOTS = new GameItem(null, null, new ItemStack(Material.CHAINMAIL_BOOTS), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE, Attribute.NATIVE});
		IRON_BOOTS = new GameItem(null, null, new ItemStack(Material.IRON_BOOTS), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE, Attribute.NATIVE});
		DIAMOND_BOOTS = new GameItem(null, null, new ItemStack(Material.DIAMOND_BOOTS), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.UPGRADE, Attribute.NATIVE});
	}

	// --- Drops ---

	public static final GameItem SLIME_BALL;
	public static final GameItem FEATHER;
	public static final  GameItem STICK;
	public static final GameItem STRING;
	public static final GameItem SPIDER_EYE;
	public static final GameItem GLOWSTONE_DUST;
	public static final GameItem GUN_POWDER;
	public static final GameItem FIRE_POWDER;
	public static final GameItem FIRE_ROD;
	public static final GameItem DIAMOND;
	public static final GameItem CRYSTAL;
	public static final GameItem IRON_INGOT;
	public static final GameItem GLOWING_IRON_INGOT;
	public static final GameItem CURSED_IRON_INGOT;
	public static final GameItem GOLD_NUGGET;
	public static final GameItem GOLD_INGOT;
	public static final GameItem GLOWING_GOLD_INGOT;
	public static final GameItem MAGIC_POWDER;

	static {
		SLIME_BALL = new GameItem("item.slime_ball.name", null, new ItemStack(Material.SLIME_BALL), new GameItem.Attribute[]{Attribute.CRAFTING});
		FEATHER = new GameItem("item.feather.name", null, new ItemStack(Material.FEATHER), new GameItem.Attribute[]{Attribute.CRAFTING});
		STICK = new GameItem("item.stick.name", null, new ItemStack(Material.STICK), new GameItem.Attribute[]{Attribute.CRAFTING});
		STRING = new GameItem("item.string.name", null, new ItemStack(Material.STRING), new GameItem.Attribute[]{Attribute.CRAFTING});
		SPIDER_EYE = new GameItem("item.spider_eye.name", null, new ItemStack(Material.SPIDER_EYE), new GameItem.Attribute[]{Attribute.CRAFTING});
		GLOWSTONE_DUST = new GameItem("item.glowstone_dust.name", null, new ItemStack(Material.GLOWSTONE_DUST), new GameItem.Attribute[]{Attribute.CRAFTING});
		GUN_POWDER = new GameItem("item.gun_powder.name", null, new ItemStack(Material.SULPHUR), new GameItem.Attribute[]{Attribute.CRAFTING});
		FIRE_POWDER = new GameItem("item.fire_powder.name", null, new ItemStack(Material.BLAZE_POWDER), new GameItem.Attribute[]{Attribute.CRAFTING});
		FIRE_ROD = new GameItem("item.fire_rod.name", null, new ItemStack(Material.BLAZE_ROD), new GameItem.Attribute[]{Attribute.CRAFTING, Attribute.CRAFTED});
		IRON_INGOT = new GameItem("item.iron_ingot.name", null, new ItemStack(Material.IRON_INGOT), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTING, Attribute.NATIVE});
		GLOWING_IRON_INGOT = new GameItem("item.glowing_iron_ingot.name", null, new ItemStack(Material.IRON_INGOT), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.GLOW, Attribute.CRAFTING, 
				Attribute.NATIVE});
		CURSED_IRON_INGOT = new GameItem("item.cursed_iron_ingot.name", "§c", new ItemStack(Material.NETHER_BRICK_ITEM), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTING, 
				Attribute.CRAFTED});
		GOLD_NUGGET = new GameItem("item.gold_nugget.name", null, new ItemStack(Material.GOLD_NUGGET), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTING, Attribute.NATIVE}); // !
		GOLD_INGOT = new GameItem("item.gold_ingot.name", null, new ItemStack(Material.GOLD_INGOT), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTING, Attribute.NATIVE}); // !
		GLOWING_GOLD_INGOT = new GameItem("item.glowing_gold_ingot.name", null, new ItemStack(Material.GOLD_INGOT), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.GLOW, Attribute.CRAFTING, 
				Attribute.NATIVE}); // !
		DIAMOND = new GameItem("item.diamond.name", "§d", new ItemStack(Material.DIAMOND), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.GLOW, Attribute.CRAFTING, Attribute.NATIVE});
		CRYSTAL = new GameItem("item.crystal.name", "§d", new ItemStack(Material.NETHER_STAR), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.GLOW, Attribute.CRAFTING, Attribute.NATIVE});
		MAGIC_POWDER = new GameItem("item.magic_powder.name", null, new ItemStack(Material.INK_SACK, 1, (byte) 5), new GameItem.Attribute[]{Attribute.CRAFTING});
	}
	
	// --- Crafted ---
	
	public static final GameItem GRENADE;
	public static final GameItem KNOCKBACK_GRENADE;
	public static final GameItem HEAL_1;
	public static final GameItem HEAL_2;
	public static final GameItem HEAL_3;
	public static final GameItem HEALING_STATION;
	public static final GameItem FISHING_ROD;
	public static final GameItem FERMENTED_SPIDER_EYE;
	public static final GameItem ARMOR_BREAKER;
	
	public static final GameItem EXTRA_HEART;
	public static final GameItem SPEED;
	
	public static final GameItem GRIP;
	
	static {
		GRENADE = new GameItem("item.grenade.name", null, new ItemStack(Material.MAGMA_CREAM), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED});
		KNOCKBACK_GRENADE = new GameItem("item.knockback_grenade.name", null, new ItemStack(Material.SNOW_BALL), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED});
		HEAL_1 = new GameItem("item.heal_1.name", null, new ItemStack(Material.INK_SACK, 1, (short) 1), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED});
		HEAL_2 = new GameItem("item.heal_2.name", null, new ItemStack(Material.INK_SACK, 1, (short) 1), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED});
		HEAL_3 = new GameItem("item.heal_3.name", null, new ItemStack(Material.INK_SACK, 1, (short) 1), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED});
		HEALING_STATION = new GameItem("item.healing_station.name", null, new ItemStack(Material.BEACON), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED});
		FISHING_ROD = new GameItem(null, null, new ItemStack(Material.FISHING_ROD), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED});
		FERMENTED_SPIDER_EYE = new GameItem("item.fermented_spider_eye.name", null, new ItemStack(Material.FERMENTED_SPIDER_EYE), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTING, 
				Attribute.CRAFTED});
		ARMOR_BREAKER = new GameItem("item.armor_breaker.name", "§c", new ItemStack(Material.GOLD_AXE), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.GLOW, 
				Attribute.CRAFTED}, "item.armor_breaker.lore");
		
		EXTRA_HEART = new GameItem("item.extra_heart.name", "§d", new ItemStack(Material.POTION, 1, (short) 8261), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED}, 
				"item.extra_heart.lore");
		SPEED = new GameItem("item.speed.name", "§d", new ItemStack(Material.POTION, 1, (short) 8226), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED}, 
				"item.speed.lore");
		
		GRIP = new GameItem("item.grip.name", null, new ItemStack(Material.LEVER), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED, Attribute.CRAFTING});
	}
	
	// Ench
	
	public final static GameItem BOOK;
	public final static GameItem ENCH_POISON_ASPECT;
	public final static GameItem ENCH_FIRE_ASPECT;
	public final static GameItem ENCH_FIRE_EXTINGUISH;
	public final static GameItem ENCH_INFINITE;
	
	static {
		BOOK = new GameItem("item.book.name", null, new ItemStack(Material.BOOK), new GameItem.Attribute[]{Attribute.CRAFTABLE});
		
		ENCH_POISON_ASPECT = new GameItem("game.enchantments.poison", null, new ItemStack(Material.ENCHANTED_BOOK), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED},
				"game.enchantments.enchantment");
		ENCH_FIRE_ASPECT = new GameItem("game.enchantments.fire_aspect", null, new ItemStack(Material.ENCHANTED_BOOK), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED},
				"game.enchantments.enchantment");
		ENCH_FIRE_EXTINGUISH = new GameItem("game.enchantments.fire_extinguish", null, new ItemStack(Material.ENCHANTED_BOOK), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED}, 
				"game.enchantments.enchantment");
		ENCH_INFINITE = new GameItem("game.enchantments.infinite", null, new ItemStack(Material.ENCHANTED_BOOK), new GameItem.Attribute[]{Attribute.CRAFTABLE, Attribute.CRAFTED}, 
				"game.enchantments.enchantment");
	}
}
