package com.rex2go.mobslayer_core.util;

import org.bukkit.inventory.ItemStack;

public class Item {

	ItemStack itemStack;
	int slot;
	
	public Item(ItemStack itemStack, int slot) {
		this.itemStack = itemStack;
		this.slot = slot;
	}
	
	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}
}
