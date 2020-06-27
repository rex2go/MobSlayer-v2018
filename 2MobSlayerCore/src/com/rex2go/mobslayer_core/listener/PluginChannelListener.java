package com.rex2go.mobslayer_core.listener;

import java.util.ArrayList;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.rex2go.mobslayer_core.Callback;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.event.PluginMessageReceiveEvent;

public class PluginChannelListener implements PluginMessageListener {

	public static ArrayList<Callback> callbacks = new ArrayList<>();
	
	@Override
	public synchronized void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		
		PluginMessageReceiveEvent pluginMessageReceiveEvent = new PluginMessageReceiveEvent(ByteStreams.newDataInput(message));
		MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(pluginMessageReceiveEvent);
		
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		
		in.readUTF();
		String callbackId = in.readUTF();
		
		if(!callbacks.isEmpty()) {
			Optional<Callback> callback = callbacks.stream().filter(c -> c.getId().equals(callbackId)).findAny();
			
			if(callback.isPresent()) {
				try {
					callback.get().response(ByteStreams.newDataInput(message)); 
				} catch (Exception e) {
					e.printStackTrace();
				}
				callbacks.remove(callback.get());
			}
		}
	}
}
