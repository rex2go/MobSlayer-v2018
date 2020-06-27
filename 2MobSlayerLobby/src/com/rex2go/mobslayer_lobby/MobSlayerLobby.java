package com.rex2go.mobslayer_lobby;

import org.bukkit.plugin.java.JavaPlugin;

import com.rex2go.mobslayer_core.listener.PluginChannelListener;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_lobby.listener.InteractListener;
import com.rex2go.mobslayer_lobby.listener.JoinListener;
import com.rex2go.mobslayer_lobby.listener.QuitListener;
import com.rex2go.mobslayer_lobby.listener.SignChangeListener;
import com.rex2go.mobslayer_lobby.sign.ServerSignUpdater;

public class MobSlayerLobby extends JavaPlugin {

	public PluginChannelListener pluginChannelListener;
	
	public static String serverName;

	public void onEnable() {
		setupTranslations();
		registerListener();
		
		new ServerSignUpdater();
	}

	private void registerListener() {
		new SignChangeListener();
		new JoinListener();
		new QuitListener();
		new InteractListener();
	}
	
	private void setupTranslations() {
		Translation.addTranslation("lobby.connect.offline", new String[] {"Der Server ist offline", "Server is offline"});
		Translation.addTranslation("lobby.connect.unknown_error", new String[] {"Unbekannter Fehler", "Unknown error"});
		Translation.addTranslation("lobby.connect.full", new String[] {"Der Server ist voll", "Server is full"});
		Translation.addTranslation("lobby.connect.ending", new String[] {"Das Spiel endet", "The game is ending"});
	}
}
