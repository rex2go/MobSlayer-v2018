package com.rex2go.mobslayer_lobby.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.event.QuitEvent;
import com.rex2go.mobslayer_core.user.User;

public class QuitListener implements Listener {

	public QuitListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onQuit(QuitEvent event) {
		User user = event.getUser();
		@SuppressWarnings("unused")
		Player player = user.getPlayer();
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}
}
