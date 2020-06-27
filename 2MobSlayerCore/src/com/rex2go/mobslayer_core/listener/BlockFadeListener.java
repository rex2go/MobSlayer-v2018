package com.rex2go.mobslayer_core.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;

public class BlockFadeListener implements Listener {

	public BlockFadeListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onBlockFade(BlockFadeEvent e) {
		if(e.getBlock().getType() == Material.ICE || e.getBlock().getType() == Material.PACKED_ICE) {
			e.setCancelled(true);
		}
	}
}
