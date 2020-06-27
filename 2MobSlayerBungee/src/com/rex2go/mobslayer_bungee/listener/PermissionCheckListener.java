package com.rex2go.mobslayer_bungee.listener;

import java.util.ArrayList;

import com.rex2go.mobslayer_bungee.MobSlayerBungee;
import com.rex2go.mobslayer_bungee.user.User;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PermissionCheckListener implements Listener {

	public PermissionCheckListener() {
		ProxyServer.getInstance().getPluginManager().registerListener(MobSlayerBungee.getInstance(), this);
	}

	@EventHandler
	public void onLogin(PermissionCheckEvent e) {
		ProxiedPlayer player = (ProxiedPlayer) e.getSender();
		User user = MobSlayerBungee.getUserManager().loadUser(player.getUniqueId());
		
		ArrayList<String> builderPermissions = new ArrayList<>();
		builderPermissions.add("bungeecord.command.server");
		
		ArrayList<String> modPermissions = new ArrayList<>();
		modPermissions.add("bungeecord.command.server");
		modPermissions.add("bungeecord.command.send");
		modPermissions.add("bungeecord.command.find");
		modPermissions.add("bungeecord.command.glist");
		
		switch (user.getRank()) {
		case BUILDER:
			e.setHasPermission(builderPermissions.contains(e.getPermission()));
			break;
		case MODERATOR:
			e.setHasPermission(modPermissions.contains(e.getPermission()));
			break;
		case ADMIN:
			e.setHasPermission(true);
			break;
		default:
			e.setHasPermission(false);
			break;
		}
	}
}
