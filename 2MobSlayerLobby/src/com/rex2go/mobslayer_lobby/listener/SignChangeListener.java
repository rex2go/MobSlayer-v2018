package com.rex2go.mobslayer_lobby.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.google.common.io.ByteArrayDataInput;
import com.rex2go.mobslayer_core.Callback;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_lobby.sign.ServerSign;
import com.rex2go.mobslayer_lobby.sign.ServerSignUpdater;
import com.rex2go.mobslayer_lobby.sign.ServerStatus;

public class SignChangeListener implements Listener {

	public SignChangeListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		User user = MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());
		
		if(user.getRank().getRankId() >= Rank.ADMIN.getRankId()) {
			for(int i = 0; i<event.getLines().length; i++) {
				event.setLine(i, event.getLine(i).replaceAll("&", "§"));
			}
			
			if(event.getLine(1).equalsIgnoreCase("[GameServer]") && event.getLine(2) != null && !event.getLine(2).isEmpty()) {
				Callback callback = new Callback() {
					
					@Override
					public void fail() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					protected void doResponse(ByteArrayDataInput in) {
						in.readUTF();
						in.readUTF();
						boolean b = Boolean.parseBoolean(in.readUTF());
						
						if(b) {
							ServerSign serverSign = new ServerSign(event.getBlock().getLocation(), new ServerStatus(event.getLine(2)));
							ServerSignUpdater.serverSigns.add(serverSign);
						} else {
							event.getBlock().breakNaturally();
						}
					}
				};
				
				MobSlayerCore.sendToBungeeCord(player, callback, "Exists", callback.getId(), MobSlayerCore.bungeeServerName, event.getLine(2));
			}
		}
	}
}
