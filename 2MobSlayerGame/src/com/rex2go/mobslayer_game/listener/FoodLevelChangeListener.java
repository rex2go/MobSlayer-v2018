package com.rex2go.mobslayer_game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;

public class FoodLevelChangeListener implements Listener {

	public FoodLevelChangeListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onJoin(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
}
