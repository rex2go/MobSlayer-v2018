package com.rex2go.mobslayer_game.listener.custom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_game.event.LobbyBlockToggleEvent;
import com.rex2go.mobslayer_game.user.GameUser;

public class LobbyBlockToggleListener implements Listener {

	public LobbyBlockToggleListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onLobbyBlockToggle(LobbyBlockToggleEvent event) {
		GameUser gameUser = event.getGameUser();
		
		if(gameUser.isBlocking()) {
			gameUser.reset();
			gameUser.loadEquip();
		} else {
			gameUser.reset();
			gameUser.getPlayer().getInventory().setArmorContents(null);
			gameUser.loadLobbyItems();
		}
	}
}
