package com.rex2go.mobslayer_bungee;

import com.rex2go.mobslayer_bungee.listener.ChannelListener;
import com.rex2go.mobslayer_bungee.listener.LoginListener;
import com.rex2go.mobslayer_bungee.listener.PermissionCheckListener;
import com.rex2go.mobslayer_bungee.listener.ServerConnectListener;
import com.rex2go.mobslayer_bungee.listener.ServerPingListener;
import com.rex2go.mobslayer_bungee.manager.RankManager;
import com.rex2go.mobslayer_bungee.manager.UserManager;
import com.rex2go.mobslayer_bungee.util.MySQL;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class MobSlayerBungee extends Plugin {

	private static MobSlayerBungee instance;
	
	private static UserManager userManager;
	private static RankManager rankManager;
	
	public static final int MAX_PLAYERS_GAME = 2; // DEBUG
	
	public static final String PREFIX = "§8[§6MobSlayer§8]";
	
	public void onEnable() {
		instance = this;

		connectMySQL();
		
        BungeeCord.getInstance().registerChannel("BungeeCord");
		
		registerListener();
		registerManager();
	}

	public void onDisable() {
		MySQL.close();
	}
	
	private void registerManager() {
		userManager = new UserManager();
		rankManager = new RankManager();
	}

	private void registerListener() {
		new PermissionCheckListener();
		new ServerConnectListener();
		new LoginListener();
		new ServerPingListener();
		new ChannelListener();
	}

	private static void connectMySQL() {
		new MySQL("", "", "", "");
	}

	public static void broadcast(String message) {
		TextComponent text = new TextComponent(message);
		ProxyServer.getInstance().broadcast(text);
	}

	public static MobSlayerBungee getInstance() {
		return instance;
	}
	
	public static UserManager getUserManager() {
		return userManager;
	}
	
	public static RankManager getRankManager() {
		return rankManager;
	}
}
