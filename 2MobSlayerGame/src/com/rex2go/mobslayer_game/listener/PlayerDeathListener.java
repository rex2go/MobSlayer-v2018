package com.rex2go.mobslayer_game.listener;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.BountifulAPI;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.event.GameEndEvent;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.misc.HealingStation;
import com.rex2go.mobslayer_game.task.Task;
import com.rex2go.mobslayer_game.user.GameUser;

public class PlayerDeathListener implements Listener {

	public PlayerDeathListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}
	
	public static HashMap<String, ItemStack[]> inventory = new HashMap<>();
	public static HashMap<String, ItemStack[]> inventoryArmor = new HashMap<>();

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		User user = MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());
		GameUser gameUser = (GameUser) user;
		
		if(MobSlayerGame.getGameManager().getGameState() == GameState.INGAME) {
			if(MobSlayerGame.getGameManager().alive.contains(gameUser)) {
				MobSlayerGame.getGameManager().alive.remove(gameUser);
			}
			
			user.getStatistics().setDeaths(user.getStatistics().getDeaths()+1);
			gameUser.setDeaths(gameUser.getDeaths()+1);
			
			if(player.getPassenger() != null) {
				Entity entity = player.getPassenger();
				player.eject();
				entity.setVelocity(new Vector(0, 0.5, 0));
			}
			
			for(User user1 : MobSlayerCore.getUserManager().getStorage()) {
				GameUser gameUser1 = (GameUser) user1;
				gameUser1.updateScoreboard();
				
				gameUser1.getPlayer().sendMessage(MobSlayerCore.PREFIX + " §c" + Translation.getTranslation("game.player_death", gameUser1.getLanguage(), 
						gameUser.getRank().getRankColor() + gameUser.getPlayer().getName()));
				
				if(MobSlayerGame.getGameManager().alive.size() != 1) {
					gameUser1.getPlayer().sendMessage(MobSlayerCore.PREFIX + " §7" + Translation.getTranslation("game.remaing_players_count", gameUser1.getLanguage(), 
							MobSlayerGame.getGameManager().alive.size()));
				} else {
					gameUser1.getPlayer().sendMessage(MobSlayerCore.PREFIX + " §7" + Translation.getTranslation("game.remaing_player_count", gameUser1.getLanguage()));
				}
			}
			
			player.setGameMode(GameMode.CREATIVE);
			player.setGameMode(GameMode.ADVENTURE);
			
			gameUser.setNoDamageSeconds(0);
			
			inventory.put(player.getUniqueId().toString(), player.getInventory().getContents().clone());
			inventoryArmor.put(player.getUniqueId().toString(), player.getInventory().getArmorContents().clone());
			gameUser.loadSpectatorItems();
			player.getInventory().setArmorContents(null);
			
			if(MobSlayerGame.getGameManager().alive.isEmpty()) {
				MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(new GameEndEvent(false));
				Task.stopGameTask();
			}
			
			player.setHealth(player.getMaxHealth());
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					player.setFireTicks(1);
					((CraftPlayer)player).getHandle().fireTicks = 1;
					
					for (PotionEffect effect : player.getActivePotionEffects())
				        player.removePotionEffect(effect.getType());
					
				}
			}, 1);
			
			gameUser.updateHealthScoreboard();
			
			gameUser.setDeathLocation(player.getLocation());
			
			BountifulAPI.sendTitle(user.getPlayer(), 5, 100, 5, "§4" + Translation.getTranslation("game.you_died", user.getLanguage()), 
					"§7" + Translation.getTranslation("game.respawn_message", user.getLanguage()));
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					player.teleport(MobSlayerGame.getSectionManager().getActiveSections().get(MobSlayerGame.getSectionManager().getActiveSections().size()-1).getDeadSpawn());
					
				}
			}.runTaskLater(MobSlayerCore.getInstance(), 1);
		}
		
		event.setDeathMessage(null);
		event.setDroppedExp(0);
		event.setKeepInventory(true);
		
		new BukkitRunnable() {
			public void run() {
				if(MobSlayerGame.getGameManager().getGameState() == GameState.INGAME) {
					if(gameUser.isDead()) {
						if(gameUser.getSettings().autoRespawn()) {
							if(gameUser.getCoins() >= 500) {
								gameUser.setCoins(gameUser.getCoins() - 500);
								gameUser.setInGameCoins(gameUser.getInGameCoins()-500);
								
								if(!HealingStation.getHealingStations().isEmpty() && gameUser.getDeathLocation() != null) {
									HealingStation nearest = null;
									
									for(HealingStation hs : HealingStation.getHealingStations()) {
										if(nearest == null) {
											nearest = hs;
										} else if(nearest.getLocation().distanceSquared(gameUser.getDeathLocation()) > hs.getLocation().distanceSquared(gameUser.getDeathLocation())) {
											nearest = hs;
										}
									}
									
									if(nearest != null) {
										gameUser.revive(nearest.getLocation().add(0.5, 1, 0.5));
									} else {
										gameUser.revive(MobSlayerGame.getMapManager().getGameMap().getPlayerSpawns().get(0));
									}
								} else {
									gameUser.revive(MobSlayerGame.getMapManager().getGameMap().getPlayerSpawns().get(0));
								}
				
								for (User user1 : MobSlayerCore.getUserManager().getStorage()) {
									user1.getPlayer().sendMessage(MobSlayerCore.PREFIX + " " + Translation.getTranslation("game.revive.player_revived", user1.getLanguage(), 
											user.getRank().getRankColor() + user.getPlayer().getName()));
									user1.getPlayer().playSound(user1.getPlayer().getLocation(), Sound.ZOMBIE_REMEDY, 1, 1);
								}
								user.sendTranslatedMessage("game.revive.auto_respawn");
							}
						}
					}
				}
			}
		}.runTaskLater(MobSlayerCore.getInstance(), 2);
	}
}
