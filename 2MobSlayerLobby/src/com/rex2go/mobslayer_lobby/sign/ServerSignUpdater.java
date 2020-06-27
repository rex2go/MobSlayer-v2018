package com.rex2go.mobslayer_lobby.sign;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.rex2go.mobslayer_core.MobSlayerCore;

public class ServerSignUpdater {

	public static ArrayList<ServerSign> serverSigns = new ArrayList<>();
	
	public ServerSignUpdater() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MobSlayerCore.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if(Bukkit.getOnlinePlayers().size() > 0) {
					if(MobSlayerCore.bungeeServerName == null) {
						MobSlayerCore.getInstance().whoAmI((Player) Bukkit.getOnlinePlayers().toArray()[0]);
					}
					
					for(ServerSign serverSign : serverSigns) {
						serverSign.getServerStatus().update();
						serverSign.update();
					}
				}
				
			}
		}, 2*20, 2*20);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MobSlayerCore.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if(Bukkit.getOnlinePlayers().size() > 0) {
					for(ServerSign serverSign : serverSigns) {
						serverSign.getServerStatus().checkOnline();
						serverSign.update();
					}
				}
				
			}
		}, 3*20, 3*20);
	}
}
