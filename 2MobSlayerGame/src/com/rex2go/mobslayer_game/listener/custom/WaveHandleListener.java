package com.rex2go.mobslayer_game.listener.custom;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.event.WaveDoneEvent;
import com.rex2go.mobslayer_game.event.WaveHandleEvent;
import com.rex2go.mobslayer_game.event.WaveMobSpawnEvent;
import com.rex2go.mobslayer_game.misc.AirDrop;
import com.rex2go.mobslayer_game.misc.HealingStation;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.mob.entity.blaze.MobBlaze;
import com.rex2go.mobslayer_game.mob.entity.pig_zombie.MobPigZombieTank;
import com.rex2go.mobslayer_game.user.GameUser;
import com.rex2go.mobslayer_game.wave.SpawnRequest;
import com.rex2go.mobslayer_game.wave.SpawnRequest.TriggerType;
import com.rex2go.mobslayer_game.wave.Wave;
import com.rex2go.mobslayer_game.wave.action.WaveAction;

public class WaveHandleListener implements Listener {

	public WaveHandleListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onWaveHandle(WaveHandleEvent event) {
		Wave wave = event.getWave();
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			all.setHealth(all.getHealth());
		}
		
		// Level Anzeige
		for(Player all : Bukkit.getOnlinePlayers()) {
			all.setLevel(wave.getMaxTime());
		}
		
		if(wave.getMaxTime() > 0) {
			wave.setMaxTime(wave.getMaxTime()-1);
			// TODO Nachrichten
			
			if(MobSlayerGame.getWaveManager().isBossWave(wave)) {
				wave.setMaxTime(0);
			}
		} else {
			if(!wave.isSkippable()) {
				if(!MobSlayerGame.getWaveManager().isBossWave(wave)) {
					wave.setSkippable(true);
					
					for(User user : MobSlayerCore.getUserManager().getStorage()) {
						GameUser gameUser = (GameUser) user;
						user.getPlayer().sendMessage(MobSlayerCore.PREFIX + " §e" + Translation.getTranslation("game.skippable_message", user.getLanguage()));
						
						gameUser.giveTracker();
					}
				}
			}
		}
		
		ArrayList<Entity> gameEntities = new ArrayList<>();
		
		for(Entity entity : MobSlayerGame.getMapManager().getGameMap().getWorld().getEntities()) {
			if(MobSlayerGame.getGameEntityManager().isGameEntity(entity)) {
				gameEntities.add(entity);
			}
		}
		
		for(GameUser gameUser : MobSlayerGame.getGameManager().alive) {
			Entity nearest = null;
			
			for(Entity entity : gameEntities) {
				if(nearest != null) {
					if(gameUser.getPlayer().getLocation().distance(entity.getLocation()) < gameUser.getPlayer().getLocation().distance(nearest.getLocation())) {
						nearest = entity;
					}
				} else {
					nearest = entity;
				}
			}
			
			if(nearest != null) {
				gameUser.getPlayer().setCompassTarget(nearest.getLocation());
			}
		}
		
		// Handle spawn requests
		ArrayList<SpawnRequest> remove = new ArrayList<>();
		SpawnRequest last = null;
		for (SpawnRequest spawnRequest : wave.getSpawnRequests()) {
			if(spawnRequest.getTriggerType() == TriggerType.TIME || spawnRequest.getTriggerType() == TriggerType.COUNT_AFTER_TIME) {
				if (spawnRequest.getTime() > 0) {
					spawnRequest.setTime(spawnRequest.getTime() - 1);
				} else {
					boolean cont  = true;
					
					if(spawnRequest.getTriggerType() == TriggerType.COUNT_AFTER_TIME) {
						if(!(spawnRequest.getCount() >= MobSlayerGame.getGameEntityManager().countAliveEntities())) {
							cont = false;
						}
					}
					
					if(cont) {
						try {
							GameEntity gameEntity1 = spawnRequest.getGameEntity().getClass().newInstance();
							WaveMobSpawnEvent mobSpawnEvent = new WaveMobSpawnEvent(gameEntity1);
							MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(new WaveMobSpawnEvent(gameEntity1));
							
							if(!mobSpawnEvent.isCancelled()) {
								gameEntity1.spawn(MobSlayerGame.getSectionManager().getRandomMobSpawn());
							}
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
								| SecurityException e) {
							e.printStackTrace();
						}
						
						remove.add(spawnRequest);
					}
				}
				
				if(last != null) {
					if(last.getTime() < spawnRequest.getTime()) {
						last = spawnRequest;
					}
				} else {
					last = spawnRequest;
				}
				
			} else if(spawnRequest.getTriggerType() == TriggerType.COUNT) {
				if(spawnRequest.getCount() >= MobSlayerGame.getGameEntityManager().countAliveEntities()) {
					try {
						GameEntity gameEntity1 = spawnRequest.getGameEntity().getClass().newInstance();
						WaveMobSpawnEvent mobSpawnEvent = new WaveMobSpawnEvent(gameEntity1);
						MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(new WaveMobSpawnEvent(gameEntity1));
						
						if(!mobSpawnEvent.isCancelled()) {
							gameEntity1.spawn(MobSlayerGame.getSectionManager().getRandomMobSpawn());
						}
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| SecurityException e) {
						e.printStackTrace();
					}
					
					remove.add(spawnRequest);
				}
			}
		}
		
		// Handle wave actions
		ArrayList<WaveAction >remove1 = new ArrayList<>();
		WaveAction last1 = null;
		for (WaveAction waveAction : wave.getWaveActions()) {
			if(waveAction.getTriggerType() == TriggerType.TIME || waveAction.getTriggerType() == TriggerType.COUNT_AFTER_TIME) {
				if (waveAction.getTime() > 0) {
					waveAction.setTime(waveAction.getTime() - 1);
				} else {
					boolean cont  = true;
					
					if(waveAction.getTriggerType() == TriggerType.COUNT_AFTER_TIME) {
						if(!(waveAction.getCount() >= wave.getSpawnRequests().size() +MobSlayerGame.getGameEntityManager().countAliveEntities())) {
							cont = false;
						}
					}
					
					if(cont) {
						waveAction.handle();
						
						remove1.add(waveAction);
					}
				}
						
				if(last1 != null) {
					if(last1.getTime() < waveAction.getTime()) {
						last1 = waveAction;
					}
				} else {
					last1 = waveAction;
				}		
			} else if(waveAction.getTriggerType() == TriggerType.COUNT) {
				if(waveAction.getCount() >= wave.getSpawnRequests().size() + MobSlayerGame.getGameEntityManager().countAliveEntities()) {
					waveAction.handle();
							
					remove1.add(waveAction);
				}
			}
		}
		
		for (SpawnRequest spawnRequest : remove) {
			wave.getSpawnRequests().remove(spawnRequest);
		}
		
		for (WaveAction waveAction : remove1) {
			wave.getWaveActions().remove(waveAction);
		}
		
		// XP Leiste
		for(User user : MobSlayerCore.getUserManager().getStorage()) {
			GameUser gameUser = (GameUser) user;
			
			if(MobSlayerGame.getWaveManager().getActiveWave() != null) {
				if(last != null) {
					float f = (float) (1 - ((double) last.getTime()) / ((double) MobSlayerGame.getWaveManager().getActiveWave().getLastSpawnRequest().getTime()));
			
					gameUser.getPlayer().setExp(f);
				} else {
					gameUser.getPlayer().setExp(1);;
				}
			} else {
				gameUser.getPlayer().setExp(0);
			}
			
			if(user.getPlayer().getFireTicks() > 1) {
				if(gameUser.getFireProtection() < 4) {
					user.getPlayer().damage(1);
				}
			}
		}
		
		HealingStation.healAll();
		HealingStation.sendParticleAll();
		
		int random = ThreadLocalRandom.current().nextInt(1, 600 + 1);
		
		if(random == 1) {
			// AIRDROP! TODO Message
			
			ArrayList<Material> blocks = new ArrayList<>();
			
			blocks.add(Material.YELLOW_FLOWER);
			blocks.add(Material.LONG_GRASS);
			blocks.add(Material.DEAD_BUSH);
			blocks.add(Material.SAPLING);
			blocks.add(Material.BROWN_MUSHROOM);
			blocks.add(Material.RED_MUSHROOM);
			blocks.add(Material.RED_ROSE);
			blocks.add(Material.AIR);
			
			Location loc = null;
			boolean clear = false;
			int ii = 0;
			
			while(!clear && ii < 3) {
				ii++;
				clear = true;
				loc = MobSlayerGame.getSectionManager().getRandomMobSpawn().clone();
				
				for(int i = loc.getBlockY(); i<loc.getBlockY() + 60; i++) {
					if(!blocks.contains(loc.getWorld().getBlockAt(loc.getBlockX(), i, loc.getBlockY()).getType())) {
						clear = false;
						break;
					}
				}
			}
			
			if(clear) {
				new AirDrop(loc).drop();
			}
		}
		
		for(AirDrop airDrop : MobSlayerGame.getAirDropManager().getStorage()) {
			airDrop.handle();
		}
		
		for(Entity entity : MobSlayerGame.getMapManager().getGameMap().getWorld().getEntities()) {
			if(MobSlayerGame.getGameEntityManager().isGameEntity(entity)) {
				GameEntity gameEntity = MobSlayerGame.getGameEntityManager().getGameEntity(entity);
				
				if(gameEntity.getTarget(entity) != null) {
					if(gameEntity.getTarget(entity) instanceof Player) {
						Player player = (Player) gameEntity.getTarget(entity);
						GameUser gameUser = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());
						
						if(gameUser.isSpectating() || gameUser.isDead()) {
							gameEntity.clearTarget(entity);
						}
					}
				}
				
				if(gameEntity.getGameEntityType() == GameEntityType.PIG_ZOMBIE_TANK) {
					PigZombie pigZombie = (PigZombie) entity;
					MobPigZombieTank tankPigZombie = (MobPigZombieTank) gameEntity;
					pigZombie.setAngry(true);
					tankPigZombie.attackShockwave(pigZombie);		
				} else if(gameEntity.getGameEntityType() == GameEntityType.IRON_GOLEM) {
					IronGolem ironGolem = (IronGolem) entity;
					com.rex2go.mobslayer_game.mob.entity.iron_golem.MobIronGolem ironGolem1 = (com.rex2go.mobslayer_game.mob.entity.iron_golem.MobIronGolem) gameEntity;
					ironGolem1.attackShockwave(ironGolem);		
				} else if(gameEntity.getGameEntityType() == GameEntityType.CAVE_SPIDER) {
					com.rex2go.mobslayer_game.mob.entity.cave_spider.MobCaveSpider caveSpider = (com.rex2go.mobslayer_game.mob.entity.cave_spider.MobCaveSpider) gameEntity;
					caveSpider.attackShoot((CaveSpider) entity);
				} else if(gameEntity.getGameEntityType() == GameEntityType.WITCH) {
					Witch witch = (Witch) entity;
					com.rex2go.mobslayer_game.mob.entity.witch.MobWitch witch1 = (com.rex2go.mobslayer_game.mob.entity.witch.MobWitch) gameEntity;
					witch1.attackLevitation(witch);
				} else if(gameEntity.getGameEntityType() == GameEntityType.CREEPER) {
					Creeper creeper = (Creeper) entity;
					com.rex2go.mobslayer_game.mob.entity.creeper.MobCreeper creeper1 = (com.rex2go.mobslayer_game.mob.entity.creeper.MobCreeper) gameEntity;
					creeper1.fuseCheck(creeper);
				} else if(gameEntity.getGameEntityType() == GameEntityType.BLAZE) {
					Blaze blaze = (Blaze) entity;
					MobBlaze mobBlaze = (MobBlaze) gameEntity;
					
					if(mobBlaze.getTarget(blaze) != null) {
						mobBlaze.attackFlame(blaze);
					}
				}
			}
		}
		
		if (MobSlayerGame.getGameEntityManager().getStorage().size()
				+ wave.getSpawnRequests().size() == 0 && wave.getActiveWaveAction().isEmpty()) {
			
			MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(new WaveDoneEvent(wave));
		}
	}
}
