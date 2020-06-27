package com.rex2go.mobslayer_bungee.listener;

import com.rex2go.mobslayer_bungee.MobSlayerBungee;
import com.rex2go.mobslayer_bungee.user.User;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnectListener implements Listener {

	public ServerConnectListener() {
		ProxyServer.getInstance().getPluginManager().registerListener(MobSlayerBungee.getInstance(), this);
	}

	@EventHandler
	public void onConnect(ServerConnectEvent e) {
		@SuppressWarnings("unused")
		User user = MobSlayerBungee.getUserManager().getUserByUUID(e.getPlayer().getUniqueId());
	}
}
