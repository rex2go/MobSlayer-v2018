package com.rex2go.mobslayer_core.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;

public class ItemUtil {

	public static ItemStack hideFlags(ItemStack itemStack) {
		net.minecraft.server.v1_8_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound tag;
		if (!nmsItemStack.hasTag()) {
			tag = new NBTTagCompound();
			nmsItemStack.setTag(tag);
		} else {
			tag = nmsItemStack.getTag();
		}
	
		tag.set("HideFlags", new NBTTagInt(63));
		nmsItemStack.setTag(tag);
		itemStack.setData(CraftItemStack.asBukkitCopy(nmsItemStack).getData());
		itemStack.setItemMeta(CraftItemStack.asBukkitCopy(nmsItemStack).getItemMeta());
		return itemStack;
	}

	public static ItemStack setDisplayname(ItemStack itemStack, String name) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		itemStack.setItemMeta(meta);
		return itemStack;
	}
	
	public static ItemStack setUnbreakable(ItemStack itemStack) {
		net.minecraft.server.v1_8_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound tag;
		if (!nmsItemStack.hasTag()) {
			tag = new NBTTagCompound();
			nmsItemStack.setTag(tag);
		} else {
			tag = nmsItemStack.getTag();
		}
		tag.set("Unbreakable", new NBTTagInt(1));
		nmsItemStack.setTag(tag);
		itemStack.setData(CraftItemStack.asBukkitCopy(nmsItemStack).getData());
		itemStack.setItemMeta(CraftItemStack.asBukkitCopy(nmsItemStack).getItemMeta());
		return itemStack;
	}
	
	public static ItemStack addLore(ItemStack itemStack, String lore) {
		ItemMeta meta = itemStack.getItemMeta();
		List<String> loreList = null;
		if(meta.getLore() != null) {
			loreList = meta.getLore();
		} else {
			loreList = new ArrayList<String>();
		}
		loreList.add(lore);
		meta.setLore(loreList);
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	public static ItemStack clearLore(ItemStack itemStack) {
		ItemMeta meta = itemStack.getItemMeta();
		meta.setLore(new ArrayList<String>());
		itemStack.setItemMeta(meta);
		return itemStack;
	}
	
	public static ItemStack addEnchantment(ItemStack itemStack, Enchantment enchantment, int level) {
		itemStack.addUnsafeEnchantment(enchantment, level);
		return itemStack;
	}
	
	public static ItemStack setColor(ItemStack itemStack, Color color) {
		LeatherArmorMeta lam = (LeatherArmorMeta) itemStack.getItemMeta();
		lam.setColor(color);
		itemStack.setItemMeta(lam);
		return itemStack;
	}
}
