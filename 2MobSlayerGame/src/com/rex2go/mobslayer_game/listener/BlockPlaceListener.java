package com.rex2go.mobslayer_game.listener;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.misc.HealingStation;
import com.rex2go.mobslayer_game.user.GameUser;

public class BlockPlaceListener implements Listener {
	
	public BlockPlaceListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		GameUser gameUser = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());
		
		if(!MobSlayerGame.setup) {
			event.setCancelled(true);
		}
		
		if(event.getBlock().getType() == Material.BEACON) {
			if(MobSlayerGame.getWaveManager().getActiveWave() != null) {
				event.setCancelled(false);
				new HealingStation(gameUser, event.getBlock().getLocation());
				
				event.getBlock().getLocation().getWorld().playSound(event.getBlock().getLocation(), Sound.ZOMBIE_REMEDY, 1, 1);
				
				for(User user : MobSlayerCore.getUserManager().getStorage()) {
					user.getPlayer().playSound(user.getPlayer().getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
					user.getPlayer().sendMessage(MobSlayerCore.PREFIX + " " + Translation.getTranslation("game.healing_station_placed", user.getLanguage(), 
							gameUser.getRank().getRankColor() + gameUser.getPlayer().getName()));
					user.getPlayer().sendMessage(MobSlayerCore.PREFIX + " §7X:§e " + event.getBlock().getLocation().getBlockX() + " §7Y:§e " + 
							event.getBlock().getLocation().getBlockY() + " §7Z:§e " + event.getBlock().getLocation().getBlockZ());
				}
			} else {
				gameUser.sendTranslatedMessage("item.healing_station.can_only_be_placed_while_a_wave", "§c", null);
			}
		}
	}
}
