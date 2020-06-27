package com.rex2go.mobslayer_lobby.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;

public class JoinListener implements Listener {

	public JoinListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		@SuppressWarnings("unused")
		Player player = event.getPlayer();
		
		event.setJoinMessage(null);
	}
}
