package com.rex2go.mobslayer_game.listener.custom;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.google.common.io.ByteArrayDataInput;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.event.PluginMessageReceiveEvent;
import com.rex2go.mobslayer_game.MobSlayerGame;

public class PluginMessageReceiveListener implements Listener {

	public PluginMessageReceiveListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onPluginMessageReceived(PluginMessageReceiveEvent event) {
		ByteArrayDataInput in = event.getByteArrayDataInput();
		String channel = in.readUTF();
		String callbackId = in.readUTF();
		
		if(channel.equals("FromServer")) {
			String from = in.readUTF();
			String message = in.readUTF();
			
			if(message.equals("ServerStatus")) {
				MobSlayerCore.sendToBungeeCord(null, "ToServer", callbackId, "game", from, MobSlayerGame.getGameManager().getGameState().name() + ", " + Bukkit.getOnlinePlayers().size());
			}
		}
	}
}
