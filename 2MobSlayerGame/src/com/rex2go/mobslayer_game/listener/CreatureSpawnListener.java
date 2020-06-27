package com.rex2go.mobslayer_game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.rex2go.mobslayer_core.MobSlayerCore;

public class CreatureSpawnListener implements Listener {

	public CreatureSpawnListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onCreateSpawnEvent(CreatureSpawnEvent event) {
		if(!event.getSpawnReason().equals(SpawnReason.CUSTOM)) {
			event.setCancelled(true);
		}
	}
}
