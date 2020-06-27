package com.rex2go.mobslayer_game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;

public class SlimeSplitListener implements Listener {

	public SlimeSplitListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onSlimeSplit(SlimeSplitEvent event) {
		event.setCancelled(true);
	}
}
