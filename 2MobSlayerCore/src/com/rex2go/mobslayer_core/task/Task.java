package com.rex2go.mobslayer_core.task;

import org.bukkit.Bukkit;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;

public class Task {

	private static int taskId = -1;
	
	public static void startOnlineTimeUpdater() {
		if(taskId != -1) {
			stopOnlineTimeUpdater();
		}
		
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobSlayerCore.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(User user : MobSlayerCore.getUserManager().getStorage()) {
					if(user.getStatistics() != null) {
						user.getStatistics().setPlayTime(user.getStatistics().getPlayTime()+1);
					}
				}
				
			}
		}, 20, 20);
	}
	
	public static void stopOnlineTimeUpdater() {
		if(taskId == -1) {
			Bukkit.getScheduler().cancelTask(taskId);
		}
	}
}
