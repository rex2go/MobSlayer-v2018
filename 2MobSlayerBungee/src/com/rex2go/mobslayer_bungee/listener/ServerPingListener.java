package com.rex2go.mobslayer_bungee.listener;

import com.rex2go.mobslayer_bungee.MobSlayerBungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerPingListener implements Listener {

	public ServerPingListener() {
		ProxyServer.getInstance().getPluginManager().registerListener(MobSlayerBungee.getInstance(), this);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPing(ProxyPingEvent e) {
		e.setResponse(new ServerPing(new Protocol("MobSlayer", e.getConnection().getVersion()), 
				new Players(100, MobSlayerBungee.getInstance().getProxy().getOnlineCount(), null),
				"§8[§6MobSlayer§8] - §7A Minecraft Game Mode \n§7INFO:§c CLOSED-ALPHA §8» §fmobslayergame.com/apply", ""));
	}
}
