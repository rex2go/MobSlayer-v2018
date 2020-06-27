package com.rex2go.mobslayer_game.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.event.LanguageChangeEvent;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.user.GameUser;

public class LanguageChangeListener implements Listener {

	public LanguageChangeListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void onLanguageChange(LanguageChangeEvent event) {
		User user = event.getUser();
		GameUser gameUser = (GameUser) user;
		
		if(MobSlayerGame.getGameManager().getGameState() != GameState.INGAME) {
			gameUser.reset();
			Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					gameUser.loadLobbyItems();
					gameUser.updateScoreboard();
					
				}
			}, 1);
		} else {
			event.setCancelled(true);
			user.sendTranslatedMessage("command.only_in_lobby", Color.ERROR, null);
		}
	}
}
