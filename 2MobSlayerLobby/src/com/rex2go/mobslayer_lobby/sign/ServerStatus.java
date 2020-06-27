package com.rex2go.mobslayer_lobby.sign;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.bukkit.Bukkit;

import com.google.common.io.ByteArrayDataInput;
import com.rex2go.mobslayer_core.Callback;
import com.rex2go.mobslayer_core.MobSlayerCore;

public class ServerStatus {

	String serverName, motd, ip;
	int slots = 12, onlineCount = 0, port;
	Status status = Status.OFFLINE;
	boolean online = false;

	public ServerStatus(String serverName) {
		this.serverName = serverName;

		Callback callback = new Callback() {

			@Override
			protected void doResponse(ByteArrayDataInput in) {
				in.readUTF();
				in.readUTF();
				String[] mixed = in.readUTF().split(", ");
				ip = mixed[0].contains("/") ? mixed[0].split("/")[1] : mixed[0];
				port = Integer.parseInt(mixed[1]);
			}

			@Override
			public void fail() {}
			
		};
		
		MobSlayerCore.sendToBungeeCord(null, callback, "ServerAddress", callback.getId(), "lobby", serverName);

		checkOnline();
		update();
	}

	@SuppressWarnings("deprecation")
	public void update() {
		if(status == Status.OFFLINE && online) {
			status = Status.WAITING;
		}
		Bukkit.getScheduler().scheduleAsyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if(online) {
					Callback callback = new Callback() {
						
						@Override
						public void doResponse(ByteArrayDataInput in) {							
							in.readUTF();
							in.readUTF();
							in.readUTF();
							String[] message = in.readUTF().split(", ");
							motd = message[0];
							onlineCount = Integer.parseInt(message[1]);
							
							for(Status s : Status.values()) {
								if(s.name().equals(motd)) {
									status = s;
									break;
								}
							}
							
							if(status == Status.WAITING && slots == onlineCount || status == Status.STARTING && slots == onlineCount) {
								status = Status.FULL;
							}
							
						}

						@Override
						public void fail() {
							onlineCount = 0;
							
							if(online) {
								status = Status.WAITING;
							}
						}
						
					};
					
					MobSlayerCore.sendToBungeeCord(null, callback, "ToServer", callback.getId(), "lobby", serverName, "ServerStatus");
				}
				
			}
		}, 15);
	}
	
	@SuppressWarnings("deprecation")
	public void checkOnline() {
		if(ip != null && port != 0) {
			Bukkit.getScheduler().scheduleAsyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {
	
				@Override
				public void run() {
					try {
						Socket s = new Socket();
						s.connect(new InetSocketAddress(ip, port), 15);
						s.close();
	
						online = true;
					} catch (IOException e) {
						online = false;
						status = Status.OFFLINE;
					}
					
				}
			}, 1);
		} else {
			online = false;
			status = Status.OFFLINE;
		}
	}

	public enum Status {
		OFFLINE("§8"), WAITING("§2"), STARTING("§6"), FULL("§4"), INGAME("§c"), ENDING("§c");
		
		String color;
		
		private Status(String color) {
			this.color = color;
		}
		
		public String getColor() {
			return color;
		}
	}
	
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getMotd() {
		return motd;
	}

	public void setMotd(String motd) {
		this.motd = motd;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getSlots() {
		return slots;
	}

	public void setSlots(int slots) {
		this.slots = slots;
	}

	public int getOnlineCount() {
		return onlineCount;
	}

	public void setOnlineCount(int onlineCount) {
		this.onlineCount = onlineCount;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}
	
	public boolean isFull() {
		return onlineCount >= slots;
	}
}
