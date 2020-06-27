package com.rex2go.mobslayer_bungee.listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.rex2go.mobslayer_bungee.MobSlayerBungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChannelListener implements Listener {

	public ChannelListener() {
		ProxyServer.getInstance().getPluginManager().registerListener(MobSlayerBungee.getInstance(), this);
	}

	@EventHandler
	public void onPluginMessage(PluginMessageEvent e) {
		if (e.getTag().equalsIgnoreCase("BungeeCord")) {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
			try {
				String channel = in.readUTF();
				
				if(channel.equals("ToServer")) {
					String callbackId = in.readUTF();
					String from = in.readUTF();
					String to = in.readUTF();
					String message = in.readUTF();
					
					ServerInfo serverInfo = MobSlayerBungee.getInstance().getProxy().getServerInfo(to);
					
					if(serverInfo != null) {
						sendToBukkit("FromServer", serverInfo, callbackId, from, message);
					}
				} else if(channel.equals("ServerAddress")) {
					String callbackId = in.readUTF();
					String sender = in.readUTF();
					String server = in.readUTF();
					
					ServerInfo serverInfo = MobSlayerBungee.getInstance().getProxy().getServerInfo(sender);
					ServerInfo serverInfo1 = MobSlayerBungee.getInstance().getProxy().getServerInfo(server);
					
					if(serverInfo != null) {
						sendToBukkit("ServerAddress", serverInfo, callbackId, serverInfo1.getAddress().getAddress() + ", " + serverInfo1.getAddress().getPort());
					}
				} else if(channel.equals("WhoAmI")) {
					String callbackId = in.readUTF();
					String playerName = in.readUTF();
					
					ServerInfo serverInfo = MobSlayerBungee.getInstance().getProxy().getServerInfo(MobSlayerBungee.getInstance().getProxy().getPlayer(playerName)
							.getServer().getInfo().getName());
					
					if(serverInfo != null) {
						sendToBukkit("WhoAmI", serverInfo, callbackId, serverInfo.getName());
					}
				} else if(channel.equals("Exists")) {
					String callbackId = in.readUTF();
					String sender = in.readUTF();
					String server = in.readUTF();
					
					ServerInfo serverInfo = MobSlayerBungee.getInstance().getProxy().getServerInfo(sender);
					
					if(serverInfo != null) {
						sendToBukkit("Exists", serverInfo, callbackId, MobSlayerBungee.getInstance().getProxy().getServers().containsKey(server) + "");
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}
	}

	public void sendToBukkit(String channel, ServerInfo server, String ... message) {
		if(message != null) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(stream);
			try {
				out.writeUTF(channel);
				
				for(String s : message) {
					out.writeUTF(s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			server.sendData("BungeeCord", stream.toByteArray());
		}
	}
}
