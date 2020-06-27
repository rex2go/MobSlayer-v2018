package com.rex2go.mobslayer_game.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.user.GameUser;

public class InventoryCloseListener implements Listener {
	
	public InventoryCloseListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		GameUser user = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());
		Inventory inv = event.getInventory();
		
		if(inv.getName().equals(Translation.getTranslation("game.research_table.title", user.getLanguage()))) {
			if(inv.getItem(4) != null) {
				if(inv.getItem(4).getType() != Material.AIR) {
					player.getInventory().addItem(inv.getItem(4));
				}
			}
		}
	}
}
