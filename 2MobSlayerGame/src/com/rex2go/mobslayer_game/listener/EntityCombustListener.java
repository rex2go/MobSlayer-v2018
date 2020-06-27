package com.rex2go.mobslayer_game.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_game.user.GameUser;

public class EntityCombustListener implements Listener {

	public EntityCombustListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}
	
	@EventHandler
	public void onCombust(EntityCombustEvent event) {
		Entity entity = event.getEntity();
		
		if(entity instanceof Player) {
			Player player = (Player) entity;
			GameUser gameUser = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());
			
			if(gameUser.getFireProtection() >= 4) {
				event.setCancelled(true);
			}
		}
	}
}
