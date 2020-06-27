package com.rex2go.mobslayer_game.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_game.user.GameUser;

public class EntityTargetListener implements Listener {

	public EntityTargetListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onTarget(EntityTargetEvent event) {
		Entity entity = event.getTarget();
		
		if(entity instanceof Player) {
			GameUser user = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(entity.getUniqueId());
			
			if(user.isDead()) {
				event.setCancelled(true);
				return;
			}
		}
	}
}
