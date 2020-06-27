package com.rex2go.mobslayer_game.crafting;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import com.rex2go.mobslayer_core.user.Title;
import com.rex2go.mobslayer_game.item.GameItem;
import com.rex2go.mobslayer_game.item.GameItem.Attribute;
import com.rex2go.mobslayer_game.item.Item;
import com.rex2go.mobslayer_game.user.GameUser;

public enum Craft {
	
	// Misc
	
	FIRE_ROD(new GameItem[] { Item.FIRE_POWDER, Item.FIRE_POWDER, Item.FIRE_POWDER }, Item.FIRE_ROD),
	
	GRIP(new GameItem[] { Item.STICK, Item.STICK, Item.STICK }, Item.GRIP),
	
	FISHING_ROD(new GameItem[] { Item.STICK, Item.STRING, Item.STRING }, Item.FISHING_ROD),
	
	FERMENTED_SPIDER_EYE(new GameItem[] { Item.SPIDER_EYE, Item.GLOWSTONE_DUST }, Item.FERMENTED_SPIDER_EYE),
	FERMENTED_SPIDER_EYE_ALT(new GameItem[] { Item.SPIDER_EYE, Item.FIRE_POWDER }, Item.FERMENTED_SPIDER_EYE),
	
	GOLD_NUGGET(new GameItem[] { Item.GOLD_INGOT }, Item.GOLD_NUGGET, 3),
	GOLD_INGOT(new GameItem[] { Item.GOLD_NUGGET, Item.GOLD_NUGGET, Item.GOLD_NUGGET }, Item.GOLD_INGOT),
	GOLD_INGOT_ALT_1(new GameItem[] { Item.GLOWING_GOLD_INGOT }, Item.GOLD_INGOT),
	GOLD_INGOT_ALT_2(new GameItem[] { Item.DIAMOND }, Item.GOLD_INGOT, 3),
	GLOWING_GOLD_INGOT(new GameItem[] { Item.FIRE_POWDER, Item.GOLD_INGOT }, Item.GLOWING_GOLD_INGOT),
	
	CRYSTAL(new GameItem[] { Item.DIAMOND, Item.DIAMOND, Item.DIAMOND }, Item.CRYSTAL),
	DIAMOND(new GameItem[] { Item.GOLD_INGOT, Item.GOLD_INGOT, Item.GOLD_INGOT }, Item.DIAMOND),
	DIAMOND_ALT(new GameItem[] { Item.CRYSTAL }, Item.DIAMOND, 3),
	
	GLOWING_IRON_INGOT(new GameItem[] { Item.FIRE_POWDER, Item.IRON_INGOT}, Item.GLOWING_IRON_INGOT),
	IRON_INGOT(new GameItem[] { Item.GLOWING_IRON_INGOT}, Item.IRON_INGOT),
	CURSED_IRON_INGOT(new GameItem[] { Item.GLOWING_IRON_INGOT, Item.FERMENTED_SPIDER_EYE, Item.GLOWING_GOLD_INGOT}, Item.CURSED_IRON_INGOT),
	
	ARMOR_BREAKER(new GameItem[] { Item.GRIP, Item.CURSED_IRON_INGOT, Item.IRON_INGOT}, Item.ARMOR_BREAKER, 1, true, null, null, Title.HUNTER),
	ARMOR_BREAKER_INFINITE(new GameItem[] { Item.ARMOR_BREAKER, Item.ENCH_INFINITE}, Item.ARMOR_BREAKER, 1, true, null, null, Title.HUNTER, Attribute.ENCHANTMENT_INFINITE),
	
	// Healing
	
	HEAL_1(new GameItem[] { Item.FIRE_POWDER, Item.GLOWSTONE_DUST }, Item.HEAL_1),
	HEAL_2(new GameItem[] { Item.HEAL_1, Item.GLOWSTONE_DUST }, Item.HEAL_2),
	HEAL_2_ALT(new GameItem[] { Item.GLOWSTONE_DUST, Item.GLOWSTONE_DUST, Item.FIRE_POWDER }, Item.HEAL_2),
	HEAL_3(new GameItem[] { Item.HEAL_2, Item.GLOWSTONE_DUST }, Item.HEAL_3),
	HEAL_3_ALT(new GameItem[] { Item.HEAL_1, Item.GLOWSTONE_DUST, Item.GLOWSTONE_DUST}, Item.HEAL_3 ),
	HEALING_STATION(new GameItem[] { Item.DIAMOND, Item.HEAL_3, Item.DIAMOND }, Item.HEALING_STATION),
	
	EXTRA_HEART(new GameItem[] { Item.DIAMOND, Item.HEAL_1 }, Item.EXTRA_HEART),
	SPEED(new GameItem[] { Item.DIAMOND, Item.FEATHER }, Item.SPEED),
	
	// Bogen & Pfeile
	
	BOW(new GameItem[] { Item.STICK, Item.STICK, Item.STRING }, Item.BOW),
	BOW_FIRE(new GameItem[] { Item.ENCH_FIRE_ASPECT, Item.BOW }, Item.BOW, 1, true, "item.bow.fire_bow", null, null, Attribute.GLOW, Attribute.ENCHANTMENT_FIRE_ASPECT),
	BOW_POISON(new GameItem[] { Item.ENCH_POISON_ASPECT, Item.BOW }, Item.BOW, 1, true, null, null, null, Attribute.GLOW, Attribute.ENCHANTMENT_POISON),
	BOW_INFINITE(new GameItem[] { Item.ENCH_INFINITE, Item.BOW }, Item.BOW, 1, true, null, null, null, Attribute.GLOW, Attribute.ENCHANTMENT_INFINITE),
	
	ARROW(new GameItem[] { Item.STICK, Item.FEATHER }, Item.ARROW, 16),
	
	// Schwerter & Äxte
	
	STONE_SWORD_FIRE(new GameItem[] { Item.STONE_SWORD, Item.ENCH_FIRE_ASPECT }, Item.STONE_SWORD, 1, true, null, null, null, Attribute.GLOW, Attribute.ENCHANTMENT_FIRE_ASPECT),
	STONE_SWORD_POISON(new GameItem[] { Item.STONE_SWORD, Item.ENCH_POISON_ASPECT }, Item.STONE_SWORD, 1, true, null, null, null, Attribute.GLOW, Attribute.ENCHANTMENT_POISON),
	
	IRON_SWORD(new GameItem[] { Item.DIAMOND, Item.STONE_SWORD }, Item.IRON_SWORD, 1, true, null, null, null),
	IRON_SWORD_1(new GameItem[] { Item.GRIP, Item.IRON_INGOT, Item.IRON_INGOT }, Item.IRON_SWORD, 1, true, null, null, null),
	
	IRON_SWORD_FIRE(new GameItem[] { Item.IRON_SWORD, Item.ENCH_FIRE_ASPECT }, Item.IRON_SWORD, 1, true, null, null, null, Attribute.GLOW, Attribute.ENCHANTMENT_FIRE_ASPECT),
	IRON_SWORD_POISON(new GameItem[] { Item.IRON_SWORD, Item.ENCH_POISON_ASPECT }, Item.IRON_SWORD, 1, true, null, null, null, Attribute.GLOW, Attribute.ENCHANTMENT_POISON),
	
	IRON_AXE(new GameItem[] { Item.IRON_SWORD, Item.IRON_INGOT }, Item.IRON_AXE, 1, true, null, null, null),
	
	IRON_AXE_FIRE(new GameItem[] { Item.IRON_AXE, Item.ENCH_FIRE_ASPECT }, Item.IRON_AXE, 1, true, null, null, null, Attribute.GLOW, Attribute.ENCHANTMENT_FIRE_ASPECT),
	IRON_AXE_POISON(new GameItem[] { Item.IRON_AXE, Item.ENCH_POISON_ASPECT }, Item.IRON_AXE, 1, true, null, null, null, Attribute.GLOW, Attribute.ENCHANTMENT_POISON),
	
	DIAMOND_SWORD(new GameItem[] { Item.DIAMOND, Item.IRON_SWORD }, Item.DIAMOND_SWORD, 1, true, null, null, null),
	DIAMOND_SWORD_1(new GameItem[] { Item.GRIP, Item.DIAMOND, Item.DIAMOND }, Item.DIAMOND_SWORD, 1, true, null, null, null),
	
	DIAMOND_SWORD_FIRE(new GameItem[] { Item.DIAMOND_SWORD, Item.ENCH_FIRE_ASPECT }, Item.DIAMOND_SWORD, 1, true, null, null, null, Attribute.GLOW, Attribute.ENCHANTMENT_FIRE_ASPECT),
	DIAMOND_SWORD_POISON(new GameItem[] { Item.DIAMOND_SWORD, Item.ENCH_POISON_ASPECT }, Item.DIAMOND_SWORD, 1, true, null, null, null, Attribute.GLOW, Attribute.ENCHANTMENT_POISON),
	
	DIAMOND_AXE(new GameItem[] { Item.DIAMOND_SWORD, Item.DIAMOND }, Item.DIAMOND_AXE, 1, true, null, null, null),
	DIAMOND_AXE_1(new GameItem[] { Item.IRON_AXE, Item.DIAMOND }, Item.DIAMOND_AXE, 1, true, null, null, null),
	
	DIAMOND_AXE_FIRE(new GameItem[] { Item.DIAMOND_AXE, Item.ENCH_FIRE_ASPECT }, Item.DIAMOND_AXE, 1, true, null, null, null, Attribute.GLOW, Attribute.ENCHANTMENT_FIRE_ASPECT),
	DIAMOND_AXE_POISON(new GameItem[] { Item.DIAMOND_AXE, Item.ENCH_POISON_ASPECT }, Item.DIAMOND_AXE, 1, true, null, null, null, Attribute.GLOW, Attribute.ENCHANTMENT_POISON),
	
	// Helm
	
	CHAIN_HELMET(new GameItem[] { Item.DIAMOND, Item.LEATHER_HELMET }, Item.CHAIN_HELMET, 1, true, null, null, null),
	CHAIN_HELMET_FIRE_EXTINGUISH(new GameItem[] { Item.CHAIN_HELMET, Item.ENCH_FIRE_EXTINGUISH }, Item.CHAIN_HELMET, 1, true, null, null, null, Attribute.ENCHANTMENT_FIRE_EXTINGUISH, 
			Attribute.GLOW),
	
	IRON_HELMET(new GameItem[] { Item.DIAMOND, Item.CHAIN_HELMET }, Item.IRON_HELMET, 1, true, null, null, null),
	IRON_HELMET_FIRE_EXTINGUISH(new GameItem[] { Item.IRON_HELMET, Item.ENCH_FIRE_EXTINGUISH }, Item.IRON_HELMET, 1, true, null, null, null, Attribute.ENCHANTMENT_FIRE_EXTINGUISH, 
			Attribute.GLOW),
	
	DIAMOND_HELMET(new GameItem[] { Item.DIAMOND, Item.IRON_HELMET }, Item.DIAMOND_HELMET, 1, true, null, null, null),
	DIAMOND_HELMET_FIRE_EXTINGUISH(new GameItem[] { Item.DIAMOND_HELMET, Item.ENCH_FIRE_EXTINGUISH }, Item.DIAMOND_HELMET, 1, true, null, null, null, Attribute.ENCHANTMENT_FIRE_EXTINGUISH, 
			Attribute.GLOW),
	
	// Chestplate
	
	CHAIN_CHESTPLATE(new GameItem[] { Item.DIAMOND, Item.LEATHER_CHESTPLATE}, Item.CHAIN_CHESTPLATE, 1, true, null, null, null),
	CHAIN_CHESTPLATE_FIRE_EXTINGUISH(new GameItem[] { Item.CHAIN_CHESTPLATE, Item.ENCH_FIRE_EXTINGUISH}, Item.CHAIN_CHESTPLATE, 1, true, null, null, null, Attribute.ENCHANTMENT_FIRE_EXTINGUISH, 
			Attribute.GLOW),
	
	IRON_CHESTPLATE(new GameItem[] { Item.DIAMOND, Item.CHAIN_CHESTPLATE }, Item.IRON_CHESTPLATE, 1, true, null, null, null),
	IRON_CHESTPLATE_FIRE_EXTINGUISH(new GameItem[] { Item.IRON_CHESTPLATE, Item.ENCH_FIRE_EXTINGUISH }, Item.IRON_CHESTPLATE, 1, true, null, null, null, Attribute.ENCHANTMENT_FIRE_EXTINGUISH, 
			Attribute.GLOW),
	
	DIAMOND_CHESTPLATE(new GameItem[] { Item.DIAMOND, Item.IRON_CHESTPLATE }, Item.DIAMOND_CHESTPLATE, 1, true, null, null, null),
	DIAMOND_CHESTPLATE_FIRE_EXTINGUISH(new GameItem[] { Item.DIAMOND_CHESTPLATE, Item.ENCH_FIRE_EXTINGUISH }, Item.DIAMOND_CHESTPLATE, 1, true, null, null, null, Attribute.ENCHANTMENT_FIRE_EXTINGUISH, 
			Attribute.GLOW),
	
	// Leggings
	
	CHAIN_LEGGINGS(new GameItem[] { Item.DIAMOND, Item.LEATHER_LEGGINGS }, Item.CHAIN_LEGGINGS, 1, true, null, null, null),
	CHAIN_LEGGINGS_FIRE_EXTINGUISH(new GameItem[] { Item.CHAIN_LEGGINGS, Item.ENCH_FIRE_EXTINGUISH }, Item.CHAIN_LEGGINGS, 1, true, null, null, null, Attribute.ENCHANTMENT_FIRE_EXTINGUISH, 
			Attribute.GLOW),
	
	IRON_LEGGINGS(new GameItem[] { Item.DIAMOND, Item.CHAIN_LEGGINGS }, Item.IRON_LEGGINGS, 1, true, null, null, null),
	IRON_LEGGINGS_FIRE_EXTINGUISH(new GameItem[] { Item.IRON_LEGGINGS, Item.ENCH_FIRE_EXTINGUISH }, Item.IRON_LEGGINGS, 1, true, null, null, null, Attribute.ENCHANTMENT_FIRE_EXTINGUISH, 
			Attribute.GLOW),
	
	DIAMOND_LEGGINGS(new GameItem[] { Item.DIAMOND, Item.IRON_LEGGINGS }, Item.DIAMOND_LEGGINGS, 1, true, null, null, null),
	DIAMOND_LEGGINGS_FIRE_EXTINGUISH(new GameItem[] { Item.DIAMOND_LEGGINGS, Item.ENCH_FIRE_EXTINGUISH }, Item.DIAMOND_LEGGINGS, 1, true, null, null, null, Attribute.ENCHANTMENT_FIRE_EXTINGUISH, 
			Attribute.GLOW),
	
	// Boots
	
	CHAIN_BOOTS(new GameItem[] { Item.DIAMOND, Item.LEATHER_BOOTS }, Item.CHAIN_BOOTS, 1, true, null, null, null),
	CHAIN_BOOTS_FIRE_EXTINGUISH(new GameItem[] { Item.CHAIN_BOOTS, Item.ENCH_FIRE_EXTINGUISH }, Item.CHAIN_BOOTS, 1, true, null, null, null, Attribute.ENCHANTMENT_FIRE_EXTINGUISH, 
			Attribute.GLOW),
	
	IRON_BOOTS(new GameItem[] { Item.DIAMOND, Item.CHAIN_BOOTS }, Item.IRON_BOOTS, 1, true, null, null, null),
	IRON_BOOTS_FIRE_EXTINGUISH(new GameItem[] { Item.IRON_BOOTS, Item.ENCH_FIRE_EXTINGUISH }, Item.IRON_BOOTS, 1, true, null, null, null, Attribute.ENCHANTMENT_FIRE_EXTINGUISH, 
			Attribute.GLOW),
	
	DIAMOND_BOOTS(new GameItem[] { Item.DIAMOND, Item.IRON_BOOTS }, Item.DIAMOND_BOOTS, 1, true, null, null, null),
	DIAMOND_BOOTS_FIRE_EXTINGUISH(new GameItem[] { Item.DIAMOND_BOOTS, Item.ENCH_FIRE_EXTINGUISH }, Item.DIAMOND_BOOTS, 1, true, null, null, null, Attribute.ENCHANTMENT_FIRE_EXTINGUISH, 
			Attribute.GLOW),
	
	// Grenade
	
	GRENADE(new GameItem[] { Item.SLIME_BALL, Item.GUN_POWDER }, Item.GRENADE, 3),
	KNOCKBACK_GRENADE(new GameItem[] { Item.FEATHER, Item.GUN_POWDER }, Item.KNOCKBACK_GRENADE, 3),
	
	// Enchantments
	
	ENCHANTMENT_BOOK_FIRE_PROTECTION(new GameItem[] { Item.BOOK, Item.MAGIC_POWDER, Item.FIRE_POWDER }, Item.ENCH_FIRE_EXTINGUISH, 1),
	ENCHANTMENT_BOOK_POISON_ASPECT(new GameItem[] { Item.BOOK, Item.MAGIC_POWDER, Item.FERMENTED_SPIDER_EYE }, Item.ENCH_POISON_ASPECT, 1),
	ENCHANTMENT_BOOK_FIRE_ASPECT(new GameItem[] { Item.BOOK, Item.MAGIC_POWDER, Item.FIRE_ROD }, Item.ENCH_FIRE_ASPECT, 1),
	ENCHANTMENT_BOOK_INFINITE(new GameItem[] { Item.BOOK, Item.MAGIC_POWDER, Item.CRYSTAL }, Item.ENCH_INFINITE, 1);
	
	CraftRecipe craftRecipe;
	int amount = 1;
	Attribute[] attr;
	String name, color;
	Title title;
	boolean adoptEnchantments = false;
	
	private Craft(GameItem[] resources, GameItem craft) {
		this.craftRecipe = new CraftRecipe(resources, craft);
	}
	
	private Craft(GameItem[] resources, GameItem craft, int amount) {
		this.craftRecipe = new CraftRecipe(resources, craft);
		this.amount = amount;
	}
	
	private Craft(GameItem[] resources, GameItem craft, int amount, boolean adoptEnchantments, String name, String color, Title title, Attribute ... attr) {
		this.craftRecipe = new CraftRecipe(resources, craft);
		this.amount = amount;
		this.adoptEnchantments = adoptEnchantments;
		this.name = name;
		this.color = color;
		this.title = title;
		this.attr = attr;
	}
	
	public CraftRecipe getCraftRecipe() {
		return craftRecipe;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public Attribute[] getAttributes() {
		return attr;
	}
	
	public String getTranslationPath() {
		return name;
	}
	
	public String getColor() {
		return color;
	}
	
	public Title getTitle() {
		return title;
	}
	
	public boolean adoptEnchantments() {
		return adoptEnchantments;
	}

	public static ItemStack craft(GameUser gameUser) {
		if (gameUser != null) {
			if (!gameUser.isDead()) {
				if (!gameUser.isSpectating()) {
					if(gameUser.getCrafting() != null) {
						if(!gameUser.getCrafting().isEmpty()) {
							for(Craft craft : values()) {
								if(gameUser.knowCraft(craft)) {
									ArrayList<Integer> res = new ArrayList<>();
									
									for(GameItem gameItem : craft.craftRecipe.resources) {
										res.add(Integer.valueOf(gameItem.getGameItemId()));
									}
									
									if(res.size() == gameUser.getCrafting().size()) {
										boolean contains = true;
										
										for(GameItem gameItem : gameUser.getCrafting()) {
											if(res.contains(Integer.valueOf(gameItem.getGameItemId()))) {
												res.remove(res.indexOf(Integer.valueOf(gameItem.getGameItemId())));
											} else {
												contains = false;
											}
										}
										
										if(contains) {
											GameItem gameItem = craft.craftRecipe.getResult().clone();
											return setupItem(gameItem, craft, gameUser, craft.adoptEnchantments());
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	public static ItemStack setupItem(GameItem gameItem, Craft craft, GameUser gameUser, boolean adoptEnchantments) {
		if(craft.getTranslationPath() != null) {
			gameItem.setTranslationPath(craft.getTranslationPath());
		}
		
		if(craft.getColor() != null) {
			gameItem.setColor(craft.getColor());
		}
		
		if(craft.getAttributes() != null) {
			for(Attribute attr : craft.getAttributes()) {
				if(!gameItem.hasAttribute(attr)) {
					gameItem.addAttribute(attr);
				}
			}
		}
		
		ArrayList<Attribute> ench = new ArrayList<>();
		
		if(adoptEnchantments) {
			for(GameItem res1 : gameUser.getCrafting()) {
				for(Attribute attr : res1.getAttributes()) {
					if(attr.name().startsWith("ENCHANTMENT_")) {
						if(!ench.contains(ench)) {
							ench.add(attr);
						}
					}
				}
			}
			
			for(Attribute attr : ench) {
				if(!gameItem.hasAttribute(Attribute.GLOW)) {
					gameItem.addAttribute(Attribute.GLOW);
				}
				
				if(!gameItem.hasAttribute(attr)) {
					gameItem.addAttribute(attr);
				}
			}
		}
		
		ItemStack itemStack = gameItem.toItemStack(gameUser);
		itemStack.setAmount(craft.getAmount());
		return GameItem.translate(itemStack, gameUser);
	}
	
	public static ArrayList<Craft> getCraft(GameItem gameItem) {
		ArrayList<Craft> crafts = new ArrayList<>();
		
		for(Craft craft : values()) {
			if(craft.getCraftRecipe().getResult().getGameItemId() == gameItem.getGameItemId()) {
				crafts.add(craft);
			}
		}
		
		return crafts;
	}
}
