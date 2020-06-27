package com.rex2go.mobslayer_lobby.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_lobby.sign.ServerSign;
import com.rex2go.mobslayer_lobby.sign.ServerSignUpdater;
import com.rex2go.mobslayer_lobby.sign.ServerStatus;

public class InteractListener implements Listener {

	public InteractListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		User user = MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());
		Action action = event.getAction();
		Block block = event.getClickedBlock();
		
		if(block != null) {
			if(block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
				if(action == Action.RIGHT_CLICK_BLOCK) {
					for(ServerSign serverSign : ServerSignUpdater.serverSigns) {
						if(serverSign.getLocation().equals(block.getLocation())) {
							ServerStatus serverStatus = serverSign.getServerStatus();
							
							switch (serverStatus.getStatus()) {
							case ENDING:
								user.sendTranslatedMessage("lobby.connect.ending", Color.ERROR, null);
								break;
							case FULL:
								sendToGameServer(user, serverSign, true);
								break;
							case OFFLINE:
								user.sendTranslatedMessage("lobby.connect.offline", Color.ERROR, null);
								break;
							case INGAME:
								sendToGameServer(user, serverSign, false);
								break;
							case STARTING:
							case WAITING:
								sendToGameServer(user, serverSign, true);
								break;
							default:
								user.sendTranslatedMessage("lobby.connect.unknown_error", Color.ERROR, null);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private boolean sendToGameServer(User user, ServerSign serverSign, boolean kick) {
		ServerStatus serverStatus = serverSign.getServerStatus();
		
		if(!serverStatus.isFull()) {
			user.send(serverStatus.getServerName());
			serverStatus.setOnlineCount(serverStatus.getOnlineCount()+1);
			serverSign.update();
			return true;
		} else if(kick) {
			// TODO Kick
		} else {
			user.sendTranslatedMessage("lobby.connect.full", Color.ERROR, null);
		}
		return false;
	}
}
