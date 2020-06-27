package com.rex2go.mobslayer_setup.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_setup.command.SetupCommand;

public class InteractListener implements Listener {
	
	public InteractListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		User user = MobSlayerCore.getUserManager().getUserByName(player.getName());
		
		if(user != null) {
			if(e.getItem() != null) {
				if(e.getItem().getItemMeta() != null) {
					if(e.getItem().getItemMeta().getDisplayName() != null) {
						// Player Spawn Setter
						if(e.getItem().getType() == SetupCommand.PLAYER_SPAWN_SETTER.getType()) {
							if(e.getItem().getItemMeta().getDisplayName().equals(SetupCommand.PLAYER_SPAWN_SETTER.getItemMeta().getDisplayName())) {
								if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
									if(SetupCommand.loadedMap != null) {
										Location loc = e.getClickedBlock().getLocation().add(0.5, 1, 0.5);
										e.getClickedBlock().getLocation().getWorld().getBlockAt(loc).setType(Material.WOOL);
										e.getClickedBlock().getLocation().getWorld().getBlockAt(loc).setData((byte) 4);
										SetupCommand.loadedMap.getPlayerSpawns().add(loc);
										
										Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {
											
											@Override
											public void run() {
												if(loc.getBlock().getType() == Material.AIR) {
													SetupCommand.loadedMap.getPlayerSpawns().remove(loc);
												} else {
													loc.getBlock().setType(Material.AIR);
												}
												
											}
										}, 20*5);
									} else {
										user.sendTranslatedMessage("command.setup.no_map_loaded", Color.ERROR, null);
									}
								}
							}
						}
						// Mob Spawn Setter
						if(e.getItem().getType() == SetupCommand.MOB_SPAWN_SETTER.getType()) {
							if(e.getItem().getItemMeta().getDisplayName().equals(SetupCommand.MOB_SPAWN_SETTER.getItemMeta().getDisplayName())) {
								if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
									if(SetupCommand.loadedSection != null) {
										Location loc = e.getClickedBlock().getLocation().add(0.5, 1, 0.5);
										e.getClickedBlock().getLocation().getWorld().getBlockAt(loc).setType(Material.WOOL);
										e.getClickedBlock().getLocation().getWorld().getBlockAt(loc).setData((byte) 13);
										SetupCommand.loadedSection.getMobSpawns().add(loc);
									} else {
										user.sendTranslatedMessage("command.setup.no_section_loaded", Color.ERROR, null);
									}
								}
							}
						}
						// Dead Spawn Setter
						if(e.getItem().getType() == SetupCommand.DEAD_SPAWN_SETTER.getType()) {
							if(e.getItem().getItemMeta().getDisplayName().equals(SetupCommand.DEAD_SPAWN_SETTER.getItemMeta().getDisplayName())) {
								if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
									if(SetupCommand.loadedSection != null) {
										Location loc = e.getClickedBlock().getLocation().add(0.5, 1, 0.5);
										e.getClickedBlock().getLocation().getWorld().getBlockAt(loc).setType(Material.WOOL);
										e.getClickedBlock().getLocation().getWorld().getBlockAt(loc).setData((byte) 10);
										SetupCommand.loadedSection.setDeadSpawn(loc);
									} else {
										user.sendTranslatedMessage("command.setup.no_section_loaded", Color.ERROR, null);
									}
								}
							}
						}
						// Door Block Setter
						if(e.getItem().getType() == SetupCommand.DOOR_BLOCK_SETTER.getType()) {
							if(e.getItem().getItemMeta().getDisplayName().equals(SetupCommand.DOOR_BLOCK_SETTER.getItemMeta().getDisplayName())) {
								if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
									if(SetupCommand.loadedSection != null) {
										Location loc = e.getClickedBlock().getLocation();
										e.getClickedBlock().getLocation().getWorld().getBlockAt(loc).setType(Material.WOOL);
										e.getClickedBlock().getLocation().getWorld().getBlockAt(loc).setData((byte) 6);
										SetupCommand.loadedSection.getDoorBlocks().add(loc);
									} else {
										user.sendTranslatedMessage("command.setup.no_section_loaded", Color.ERROR, null);
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
