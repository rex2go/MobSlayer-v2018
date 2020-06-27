package com.rex2go.mobslayer_game.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.GameItem;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.user.GameUser;

public class DropListener implements Listener {

	public DropListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		if (!MobSlayerGame.setup) {
			Player player = event.getPlayer();
			GameUser user = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());

			if (MobSlayerGame.getGameManager().getGameState() != GameState.INGAME) {
				event.setCancelled(true);
			} else if (event.getItemDrop().getItemStack().getItemMeta() != null) {
				
				if(!MobSlayerGame.getGameManager().alive.contains(user)) {
					event.setCancelled(true);
				}
				
				if(MobSlayerGame.getGameItemManager().isGameItem(event.getItemDrop().getItemStack())) {
					GameItem gameItem = GameItem.fromItemStack(event.getItemDrop().getItemStack());
					
					if(gameItem.isSoulbound()) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
}
