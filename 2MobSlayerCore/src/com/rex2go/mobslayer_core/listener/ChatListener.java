package com.rex2go.mobslayer_core.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;

public class ChatListener implements Listener {

	public ChatListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		User user = MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());
		String message = event.getMessage().replaceAll("%", "%%");

		if (user != null) {
			if(!user.isMuted()) {
				if (user.getRank() == Rank.ADMIN) {
					String first = message.substring(0, Math.min(message.length(), 1));
					if (first.equals("!")) {
						message.replaceAll("%%", "%");
						
						final String msg = message;
						
						Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {
							
							@Override
							public void run() {
								Bukkit.dispatchCommand(player, msg.substring(1));
								
							}
						}, 1);
						
						event.setCancelled(true);
						return;
					}
					
					message = ChatColor.translateAlternateColorCodes('&', message);
				}
				
				System.out.println(user.getRank().name() + " " + user.getPlayer().getName() + " > " + event.getMessage());
	
				event.setMessage(message);
			} else {
				user.sendTranslatedMessage("general.muted", "§c", null);
				event.setCancelled(true);
			}
		}
	}
}
