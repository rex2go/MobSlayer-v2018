package com.rex2go.mobslayer_game.manager;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import com.rex2go.mobslayer_core.manager.Manager;
import com.rex2go.mobslayer_game.item.GameItem;

public class GameItemManager extends Manager {

	HashMap<Integer, GameItem> gameItemStorage = new HashMap<>();
	
	public HashMap<Integer, GameItem> getStorage() {
		return gameItemStorage;
	}
	
	public boolean isGameItem(ItemStack itemStack) {
		return GameItem.fromItemStack(itemStack) != null;
	}
}
