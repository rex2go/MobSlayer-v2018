package com.rex2go.mobslayer_game.item;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.ItemUtil;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.crafting.Craft;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;

public class GameItem {

	int gameItemId = MobSlayerGame.getGameItemManager().getStorage().size()+1;
	String translationPath;
	String color = "§f";
	ItemStack itemStack;
	Attribute[] attr;
	String[] lore;
	

	public GameItem(String translationPath, String color, ItemStack itemStack, Attribute[] attr, String ... lore) {
		this.translationPath = translationPath;
		if(color != null) this.color = color;
		this.itemStack = itemStack;
		this.attr = attr;
		this.lore = lore;
		
		MobSlayerGame.getGameItemManager().getStorage().put(gameItemId, this);
	}
	
	public GameItem(String translationPath, String color, ItemStack itemStack, Attribute[] attr, int id, String ... lore) {
		this.translationPath = translationPath;
		if(color != null) this.color = color;
		this.itemStack = itemStack;
		this.attr = attr;
		this.lore = lore;
		this.gameItemId = id;
	}
	
	public GameItem clone() {
//		@SuppressWarnings("rawtypes")
//		Class[] arg = new Class[6];
//		arg[0] = String.class;
//		arg[1] = String.class;
//		arg[2] = ItemStack.class;
//		arg[3] = Attribute[].class;
//		arg[4] = int.class;
//		arg[5] = String[].class;
//		try {
//			return getClass().getDeclaredConstructor(arg).newInstance(translationPath, color, itemStack, attr, gameItemId, lore);
//		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
//				| NoSuchMethodException | SecurityException e) {
//			e.printStackTrace();
//		}
//		return null;
		
		return new GameItem(translationPath == null ? null : new String(translationPath), 
				color == null ? null : new String(color), 
				itemStack == null ?  null : itemStack.clone(), 
				attr == null ? null : attr.clone(), 
				new Integer(gameItemId), lore.clone());
	}
	
	public static int getUses(ItemStack itemStack) {
		net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
		
		if(tag.hasKey("uses")) {
			return tag.getInt("uses");
		}
		return 0;
	}
	
	public static void setUses(ItemStack itemStack, int uses) {
		net.minecraft.server.v1_8_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound tag;
		if (!nmsItemStack.hasTag()) {
			tag = new NBTTagCompound();
			nmsItemStack.setTag(tag);
		} else {
			tag = nmsItemStack.getTag();
		}
		
		tag.set("uses", new NBTTagInt(uses));
		nmsItemStack.setTag(tag);
		itemStack.setData(CraftItemStack.asBukkitCopy(nmsItemStack).getData());
		itemStack.setItemMeta(CraftItemStack.asBukkitCopy(nmsItemStack).getItemMeta());
	}
	
	public int getGameItemId() {
		return gameItemId;
	}
	
	public String getTranslationPath() {
		return translationPath;
	}

	public void setTranslationPath(String translationPath) {
		this.translationPath = translationPath;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isSoulbound() {
		return hasAttribute(Attribute.SOUL_BOUND);
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public Attribute[] getAttributes() {
		return attr;
	}

	public void setAttributes(Attribute[] attr) {
		this.attr = attr;
	}
	
	public ItemStack assignNBTData() {
		ItemStack itemStack = this.itemStack.clone();
		net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
		
		if(!tag.hasKey("gameItemId")) {
			tag.set("gameItemId", new NBTTagInt(gameItemId));
		}
		
		if(!tag.hasKey("uses")) {
			tag.set("uses", new NBTTagInt(0));
		}
		
		NBTTagList list = new NBTTagList();
		for(Attribute attr : getAttributes()) {
			list.add(new NBTTagString(attr.name()));
		}
		
		tag.set("attributes", list);
		
		if(hasAttribute(Attribute.GLOW)) {
			if(!tag.hasKey("ench")) {
				tag.set("ench", new NBTTagList());
			}
		}
		
		nmsItem.setTag(tag);
		
		itemStack = CraftItemStack.asCraftMirror(nmsItem);
		
		itemStack = ItemUtil.setUnbreakable(itemStack);
		itemStack = ItemUtil.hideFlags(itemStack);
		return itemStack;
	}
	
	public static ItemStack translate(ItemStack itemStack, User user) {
		GameItem gameItem = fromItemStack(itemStack);	
		GameUser gameUser = (GameUser) user;
		ItemMeta itemMeta = itemStack.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();
		ArrayList<Craft> crafts = Craft.getCraft(gameItem);
		boolean know = true;
		
		if(gameItem == null) return null;
		
		if(gameItem.translationPath != null) {
			itemMeta.setDisplayName(gameItem.color + Translation.getTranslation(gameItem.translationPath, user.getLanguage()));
		}
		
		if(gameItem.getLore() != null) {
			for(String lore1 : gameItem.getLore()) {
				lore.add("§7" + Translation.getTranslation(lore1, gameUser.getLanguage()));
			}
		}
		
		for(Attribute att : gameItem.attr) {
			if(att.getTranslationPath() != null) {
				lore.add(att.color + Translation.getTranslation(att.translationPath, user.getLanguage()));
			}
		}
		
		if(gameItem.hasAttribute(Attribute.CRAFTED)) {
			if(!crafts.isEmpty()) {
				for(Craft c : crafts) {
					if(!gameUser.knowCraft(c)) {
						know = false;
						break;
					}
				}
			}
			
			if(!know) {
				lore.add("§c" + Translation.getTranslation("game.item.researchable", user.getLanguage()));
			}
		}
		
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
		
		return itemStack;
	}
	
	public static GameItem fromItemStack(ItemStack itemStack) {
		net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
		
		if(itemStack != null) {
			if(nmsItem != null) {
				if(nmsItem.hasTag()) {
					NBTTagCompound tag = nmsItem.getTag();
					
					if(tag.hasKey("gameItemId")) {
						GameItem gameItem =  MobSlayerGame.getGameItemManager().getStorage().get(Integer.valueOf(tag.getInt("gameItemId"))).clone();
						
						if(tag.hasKey("attributes")) {
							NBTTagList list = tag.getList("attributes", 8);
							ArrayList<Attribute> attrList = new ArrayList<>();
							
							for(int i = 0; i<list.size(); i++) {
								attrList.add(Attribute.valueOf(list.getString(i)));
							}
							
							for(Attribute attr : gameItem.getAttributes()) {
								if(!attrList.contains(attr)) {
									attrList.add(attr);
								}
							}
							
							Attribute[] attr = attrList.toArray(new Attribute[0]);
							gameItem.setAttributes(attr);
						}
						
						return gameItem;
					}
				}
			}
		}
		
		return null;
	}
	
	public ItemStack toItemStack(User user) {
		ItemStack itemStack = assignNBTData();
		translate(itemStack, user);
		
		for(Attribute attr : getAttributes()) {
			if(attr.name().startsWith("ENCHANTMENT_")) {
				String ench = attr.name().substring(12);
				Enchantment enchh = Enchantment.getByName(ench);
				
				if(enchh != null) {
					if(enchh.equals(Enchantment.FIRE_ASPECT)) {
						if(itemStack.getType() == Material.BOW) {
							itemStack = ItemUtil.addEnchantment(itemStack, Enchantment.ARROW_FIRE, 1);
						} else {
							itemStack = ItemUtil.addEnchantment(itemStack, Enchantment.FIRE_ASPECT, 1);
						}
					} else {
						itemStack = ItemUtil.addEnchantment(itemStack, Enchantment.getByName(ench), 1);
					}
				} else {
					if(ench.equals("INFINITE")) {
						if(itemStack.getType() == Material.BOW) {
							itemStack = ItemUtil.addEnchantment(itemStack, Enchantment.ARROW_INFINITE, 1);
						}
					}
				}
			}
		}
		
		return itemStack;
	}
	
	public enum Attribute {
		CRAFTING("item.use.crafting_item"), CRAFTED(null), NO_CRAFT(null), GLOW(null), SOUL_BOUND("game.general.soulbound", "§6"), UPGRADE("item.use.upgraded_item"), CRAFTABLE(null),
		ENCHANTMENT_FIRE_ASPECT("game.enchantments.fire_aspect"), ENCHANTMENT_DAMAGE_ALL("game.enchantments.damage_all"), ENCHANTMENT_ARROW_DAMAGE("game.enchantments.arrow_damage"),
		ENCHANTMENT_POISON("game.enchantments.poison"), ENCHANTMENT_PROTECTION_ENVIRONMENTAL("game.enchantments.protection_environmental"),
		NATIVE(null), ENCHANTMENT_FIRE_EXTINGUISH("game.enchantments.fire_extinguish"), ENCHANTMENT_INFINITE("game.enchantments.infinite");

		String translationPath;
		String color = "§7";

		private Attribute(String translationPath, String color) {
			this.translationPath = translationPath;
			this.color = color;
		}
		
		private Attribute(String translationPath) {
			this.translationPath = translationPath;
		}

		public String getTranslationPath() {
			return translationPath;
		}

		public void setTranslationPath(String translationPath) {
			this.translationPath = translationPath;
		}
		
		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}
	}
	
	public boolean hasAttribute(Attribute attr) {
		for(Attribute attr1 : getAttributes()) {
			if(attr1.equals(attr)) {
				return true;
			}
		}
		return false;
	}
	
	public int getWorth() {
		int worth = 0;
		if(hasAttribute(Attribute.CRAFTED)) {
			for(Craft craft : Craft.values()) {
				if(craft.getCraftRecipe().getResult().getGameItemId() == getGameItemId()) {
					for(GameItem res : craft.getCraftRecipe().getResources()) {
						ArrayList<Craft> craft1 = Craft.getCraft(res);
						
						if(!craft1.isEmpty()) {
							for(Craft craft2 : craft1) {
								for(GameItem res2 : craft2.getCraftRecipe().getResources()) {
									for(ShopItem shopItem : ShopItem.getShopItems()) {
										if(res2.getGameItemId() == shopItem.getGameItem().getGameItemId()) {
											worth += shopItem.getPrice();
										}
									}
								}
							}
						} else {
							for(ShopItem shopItem : ShopItem.getShopItems()) {
								if(res.getGameItemId() == shopItem.getGameItem().getGameItemId()) {
									worth += shopItem.getPrice();
								}
							}
						}
					}
					
					if(worth != 0) {
						break;
					}
				}
			}
		}
		return worth + 50;
	}
	
	public ItemStack addAttribute(Attribute attri) {	
		ArrayList<Attribute> attrs = new ArrayList<>();
		
		for(Attribute attr : getAttributes()) {
			attrs.add(attr);
		}
		
		attrs.add(attri);
		
		setAttributes(attrs.toArray(new Attribute[0]));
		
		return null;
	}
	
	public String[] getLore() {
		return lore;
	}
}
