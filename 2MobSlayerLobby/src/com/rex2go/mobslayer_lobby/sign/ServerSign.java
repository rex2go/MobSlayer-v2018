package com.rex2go.mobslayer_lobby.sign;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;

import com.rex2go.mobslayer_lobby.sign.ServerStatus.Status;

public class ServerSign {

	Location location;
	ServerStatus serverStatus;

	public ServerSign(Location location, ServerStatus serverStatus) {
		this.location = location;
		this.serverStatus = serverStatus;
	}
	
	public void update() {
		if(location.getBlock() != null) {
			if(location.getBlock().getType() == Material.SIGN_POST || location.getBlock().getType() == Material.WALL_SIGN) {
				Sign sign = (Sign) location.getBlock().getState();
				
				if(serverStatus.isFull() && serverStatus.getStatus() == Status.WAITING || serverStatus.isFull() && serverStatus.getStatus() == Status.STARTING) {
					serverStatus.setStatus(Status.FULL);
				}
				
				sign.setLine(0, "- MobSlayer -");
				sign.setLine(1, serverStatus.getStatus().getColor() + serverStatus.getStatus().name());
				sign.setLine(2, "[" + serverStatus.getOnlineCount() + "/" + serverStatus.getSlots() + "]");
				sign.setLine(3, "§7" + serverStatus.getServerName().toUpperCase());
				
				sign.update();
				return;
			}
		}
		
		location.getBlock().setType(Material.WALL_SIGN);
	}
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ServerStatus getServerStatus() {
		return serverStatus;
	}

	public void setServerStatus(ServerStatus serverStatus) {
		this.serverStatus = serverStatus;
	}
}
