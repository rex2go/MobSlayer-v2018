package com.rex2go.mobslayer_core.listener;

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
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		User user = MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());

		QuitEvent quitEvent = new QuitEvent(user);
		MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(quitEvent);
		
		if (user != null) {
			MobSlayerCore.getUserManager().saveUser(user);
			MobSlayerCore.getUserManager().removeUser(user);
		}
		
		e.setQuitMessage(null);
	}
}
