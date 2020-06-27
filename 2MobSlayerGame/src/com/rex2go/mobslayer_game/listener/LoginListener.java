package com.rex2go.mobslayer_game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;

public class LoginListener implements Listener {

	public LoginListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
//		if(MobSlayerGame.getGameManager().getGameState() == GameState.INGAME) {
//			event.disallow(Result.KICK_OTHER, "");
//		}
	}
}
