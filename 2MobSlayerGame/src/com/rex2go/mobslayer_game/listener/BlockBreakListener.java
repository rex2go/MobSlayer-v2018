package com.rex2go.mobslayer_game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_game.MobSlayerGame;

public class BlockBreakListener implements Listener {

	public BlockBreakListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(!MobSlayerGame.setup) {
			event.setCancelled(true);
		}
	}
}
