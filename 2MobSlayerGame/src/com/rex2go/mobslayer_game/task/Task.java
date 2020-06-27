package com.rex2go.mobslayer_game.task;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.util.Vector;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.MobSlayerCore.Status;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Particle;
import com.rex2go.mobslayer_core.util.ParticleEffect;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.event.GameStartEvent;
import com.rex2go.mobslayer_game.event.PreWaveHandleEvent;
import com.rex2go.mobslayer_game.event.WaveHandleEvent;
import com.rex2go.mobslayer_game.event.WaveStartEvent;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;
import com.rex2go.mobslayer_game.vote.Vote;
import com.rex2go.mobslayer_game.wave.Wave;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class Task {

	private static int scoreboardTaskId = -1;
	private static int lobbyCountdownTaskId = -1;
	private static int gameTaskId = -1;
	private static int previewTaskId = -1;

	private static String title = "MobSlayer";
	public static String title1 = title;
	private static int index = 0;
	private static int time = 0;

	public static int lobbyWaitingTime = MobSlayerGame.LOBBY_WAITING_TIME;

	public static void startScoreboardTask() {
		if (scoreboardTaskId == -1) {
			scoreboardTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobSlayerCore.getInstance(),
					new Runnable() {

						@Override
						public void run() {
							title1 = title;

							if (time == 0) {
								for (User user : MobSlayerCore.getUserManager().getStorage()) {
									GameUser gameUser = (GameUser) user;
									if(user.getSettings().hasScoreboardEnabled()) {
										gameUser.getObjective().setDisplayName("§8» §6§l" + Task.title1 + " §8«");
									}
								}
							}

							if (time > 4 * 30) {
								if (index < title.length() - 1) {
									title1 = title.substring(0, index) + "§f§l" + title.substring(index, index + 1)
											+ "§6§l" + title.substring(index + 1);
									index++;

									for (User user : MobSlayerCore.getUserManager().getStorage()) {
										GameUser gameUser = (GameUser) user;
										if(user.getSettings().hasScoreboardEnabled()) {
											gameUser.getObjective().setDisplayName("§8» §6§l" + Task.title1 + " §8«");
										}
									}
								} else {
									title1 = title.substring(0, index) + "§f§l" + title.substring(index, index + 1)
											+ "§6§l" + title.substring(index + 1);
									index = 0;
									time = 0;

									for (User user : MobSlayerCore.getUserManager().getStorage()) {
										GameUser gameUser = (GameUser) user;
										if(user.getSettings().hasScoreboardEnabled()) {
											gameUser.getObjective().setDisplayName("§8» §6§l" + Task.title1 + " §8«");
										}
									}
								}
							} else {
								time++;
							}
						}
					}, 5, 5);
		}
	}

	public static void stopScoreboardTask() {
		if (scoreboardTaskId != -1) {
			Bukkit.getScheduler().cancelTask(scoreboardTaskId);
			scoreboardTaskId = -1;
		}
	}

	public static void startPreviewTask() {
		if (previewTaskId == -1) {
			previewTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobSlayerCore.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					for(User user : MobSlayerCore.getUserManager().getStorage()) {
						GameUser gameUser = (GameUser) user;
						Player player = gameUser.getPlayer();
						
						if(player.isBlocking()) {
							if(!gameUser.isBlocking()) {
								gameUser.setBlocking(true);
							}
						} else {
							if(gameUser.isBlocking()) {
								gameUser.setBlocking(false);
							}
						}
					}
					
				}
			}, 4, 4);
		}
	}
	
	public static void stopPreviewTask() {
		if(previewTaskId != -1) {
			Bukkit.getScheduler().cancelTask(previewTaskId);
			previewTaskId = -1;
		}
	}
	
	public static void startLobbyCountdownTask() {
		if (lobbyCountdownTaskId == -1) {
			lobbyCountdownTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobSlayerCore.getInstance(),
					new Runnable() {

						@Override
						public void run() {
							if (Bukkit.getOnlinePlayers().size() >= MobSlayerGame.PLAYER_COUNT_REQUIRED) {
								if (lobbyWaitingTime > 0) {
									for (Player all : Bukkit.getOnlinePlayers()) {
										all.setLevel(lobbyWaitingTime);
									}

									if (lobbyWaitingTime == 10) {
										MobSlayerGame.getVoteManager().setAllowVote(false);
										Vote vote = MobSlayerGame.getVoteManager().getMostVoted();
										MobSlayerGame.getMapManager().loadMap(vote.getWorldName());

										for (User user : MobSlayerCore.getUserManager().getStorage()) {
											GameUser gameUser = (GameUser) user;
											user.getPlayer().playSound(user.getPlayer().getLocation(), Sound.LEVEL_UP,
													1, 1);
											user.getPlayer()
													.sendMessage(MobSlayerCore.PREFIX + " §7"
															+ Translation.getTranslation("game.lobby.voting_ended",
																	user.getLanguage()));
											user.getPlayer().sendMessage(MobSlayerCore.PREFIX + " §7"+ Translation.getTranslation("game.lobby.map_announce",
																			user.getLanguage(), vote.getMapName(), vote.getAuthor()));

											gameUser.updateScoreboard();

											if (user.getPlayer().getOpenInventory() != null) {
												if (user.getPlayer().getOpenInventory().getTitle() != null) {
													if (user.getPlayer().getOpenInventory().getTitle()
															.equals(Translation.getTranslation("game.lobby.voting",
																	user.getLanguage()))) {
														user.getPlayer().closeInventory();
													}
												}
											}
										}

										MobSlayerGame.getWaveManager().loadWaves(MobSlayerGame.getMapManager().getGameMap());
									}

									if (lobbyWaitingTime <= 3) {
										if (lobbyWaitingTime != 1) {
											for (User user : MobSlayerCore.getUserManager().getStorage()) {
												user.getPlayer().playSound(user.getPlayer().getLocation(),
														Sound.ORB_PICKUP, 1, 1);

												user.getPlayer().sendMessage(MobSlayerCore.PREFIX + " §7" + Translation.getTranslation("game.lobby.starting_in_x_seconds",
																		user.getLanguage(), lobbyWaitingTime));

											}
										} else {
											for (User user : MobSlayerCore.getUserManager().getStorage()) {
												user.getPlayer().playSound(user.getPlayer().getLocation(),
														Sound.ORB_PICKUP, 1, 1);

												user.getPlayer()
														.sendMessage(MobSlayerCore.PREFIX + " §7"
																+ Translation.getTranslation(
																		"game.lobby.starting_in_1_second",
																		user.getLanguage()));

											}
										}
									}
									if (lobbyWaitingTime == 240) {
										for (User user : MobSlayerCore.getUserManager().getStorage()) {
											user.getPlayer().playSound(user.getPlayer().getLocation(), Sound.ORB_PICKUP,
													1, 1);
										}
										MobSlayerCore.broadcast("game.lobby.starting_in_4_minutes", Status.INFO);
									}

									if (lobbyWaitingTime == 180) {
										for (User user : MobSlayerCore.getUserManager().getStorage()) {
											user.getPlayer().playSound(user.getPlayer().getLocation(), Sound.ORB_PICKUP,
													1, 1);
										}
										MobSlayerCore.broadcast("game.lobby.starting_in_3_minutes", Status.INFO);
									}

									if (lobbyWaitingTime == 120) {
										for (User user : MobSlayerCore.getUserManager().getStorage()) {
											user.getPlayer().playSound(user.getPlayer().getLocation(), Sound.ORB_PICKUP,
													1, 1);
										}
										MobSlayerCore.broadcast("game.lobby.starting_in_2_minutes", Status.INFO);
									}

									if (lobbyWaitingTime == 60) {
										for (User user : MobSlayerCore.getUserManager().getStorage()) {
											user.getPlayer().playSound(user.getPlayer().getLocation(), Sound.ORB_PICKUP,
													1, 1);
										}
										MobSlayerCore.broadcast("game.lobby.starting_in_1_minute", Status.INFO);
									}

									if (lobbyWaitingTime == 30) {
										for (User user : MobSlayerCore.getUserManager().getStorage()) {
											user.getPlayer().playSound(user.getPlayer().getLocation(), Sound.ORB_PICKUP,
													1, 1);

											user.getPlayer().sendMessage(MobSlayerCore.PREFIX + " §7"+ Translation.getTranslation("game.lobby.starting_in_x_seconds",
																			user.getLanguage(), lobbyWaitingTime));
										}
									}

									lobbyWaitingTime--;

									if (lobbyWaitingTime > 120) {
										if (Bukkit.getOnlinePlayers()
												.size() >= MobSlayerGame.SHORTEN_TIME_PLAYER_COUNT_REQUIRED) {
											lobbyWaitingTime = 120;

											MobSlayerCore.broadcast("game.lobby.time_shortened",
													Status.INFO);
										}
									} else if (lobbyWaitingTime > 10) {
										if (Bukkit.getOnlinePlayers().size() >= MobSlayerGame.MAX_PLAYERS) {
											lobbyWaitingTime = 10;

											MobSlayerCore.broadcast("game.lobby.time_shortened",
													Status.INFO);
										}
									}
								} else {
									stopLobbyCountdownTask();
									stopPreviewTask();

									MobSlayerCore.getInstance().getServer().getPluginManager()
											.callEvent(new GameStartEvent());
								}
							} else {
								stopLobbyCountdownTask();
								lobbyWaitingTime = MobSlayerGame.LOBBY_WAITING_TIME;
								MobSlayerGame.getGameManager().setGameState(GameState.WAITING);

								for (Player all : Bukkit.getOnlinePlayers()) {
									all.setLevel(0);
									all.setExp(0);
								}
							}
						}
					}, 20, 20);
		}
	}

	public static void stopLobbyCountdownTask() {
		if (lobbyCountdownTaskId != -1) {
			Bukkit.getScheduler().cancelTask(lobbyCountdownTaskId);
			lobbyCountdownTaskId = -1;
		}
	}

	public static void startGameTask() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MobSlayerCore.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(GameUser gameUser : MobSlayerGame.getGameManager().alive) {
					if(gameUser.getPlayer().getFireTicks() > 0) {
						if(gameUser.getFireProtection() > 0) {
							if(gameUser.getFireProtection() < 4) {
								gameUser.getPlayer().setFireTicks(gameUser.getPlayer().getFireTicks()-gameUser.getFireProtection());
							} else {
								gameUser.getPlayer().setFireTicks(0);
							}
						}
					}
				}
				
				for(Entity entity : MobSlayerGame.getMapManager().getGameMap().getWorld().getEntities()) {
					if(((CraftEntity) entity).getHandle().noDamageTicks != 0) {
						((CraftEntity) entity).getHandle().noDamageTicks = ((CraftEntity) entity).getHandle().noDamageTicks - 1;
					}
					
					// Vergiftung
					if(MobSlayerGame.getGameEntityManager().isGameEntity(entity)) {GameEntity gameEntity = MobSlayerGame.getGameEntityManager().getGameEntity(entity);
						if(gameEntity.getPoisonTime() > 0) {
							gameEntity.setPoison(gameEntity.getPoisonTime()-1);
							if(gameEntity.getPoisonTime() % 10 == 0) {
								net.minecraft.server.v1_8_R3.Entity entity1 = ((CraftEntity) entity).getHandle();
								if(entity instanceof CaveSpider || entity instanceof Spider) {
									if(gameEntity.getGameEntityType() == GameEntityType.CAVE_SPIDER) {
										CaveSpider caveSpider = (CaveSpider) entity;
										
										if(caveSpider.getHealth() < caveSpider.getMaxHealth()) {
											caveSpider.setHealth(caveSpider.getHealth()+1);
										}
									} else if(gameEntity.getGameEntityType() == GameEntityType.SPIDER) {
										Spider caveSpider = (Spider) entity;
										
										if(caveSpider.getHealth() < caveSpider.getMaxHealth()) {
											caveSpider.setHealth(caveSpider.getHealth()+1);
										}
									}
									
									Particle particle = new Particle(EnumParticle.SPELL_WITCH, entity.getLocation(), false, 0.2, 0.2, 0.2, 0.01, 5);
									particle.sendAll();
								} else {
									entity1.damageEntity(DamageSource.GENERIC, 2);
									
									Particle particle = new Particle(EnumParticle.SPELL_WITCH, entity.getLocation().clone().add(0, 1, 0), false, 0.2, 0.2, 0.2, 0.01, 5);
									particle.sendAll();
									
									ArrayList<Player> blood = new ArrayList<>();
									
									for(User user : MobSlayerCore.getUserManager().getStorage()) {
										if(user.getSettings().showBlood()) {
											blood.add(user.getPlayer());
										}
									}
									
									if(gameEntity.blood) {
										if(!blood.isEmpty()) {
											ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.1F, 15, 
													entity.getLocation().clone().add(0, 1, 0), blood);
										}
									}
									
									if(gameEntity.getGameEntityType() != GameEntityType.PIG_ZOMBIE_TANK) {
										entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.HURT_FLESH, 1, 0.8f);
									} else {
										entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.ZOMBIE_METAL, 1, 0.8f);
									}
								}
							}
						}
					}
					
					if(MobSlayerGame.getGameManager().grenades.contains(entity)) {
						ArrayList<Material> blocks = new ArrayList<>();
						
						blocks.add(Material.YELLOW_FLOWER);
						blocks.add(Material.LONG_GRASS);
						blocks.add(Material.DEAD_BUSH);
						blocks.add(Material.SAPLING);
						blocks.add(Material.BROWN_MUSHROOM);
						blocks.add(Material.RED_MUSHROOM);
						blocks.add(Material.RED_ROSE);
						
						Location center = entity.getLocation();
						double radius = 0.75;
						
						double x2 = center.getX();
						double y2 = center.getY();
						double z2 = center.getZ();
						World world = center.getWorld();
						
						for (double x = -radius; x <= radius; x++) {
							for (double y = -radius; y <= radius; y++) {
								for (double z = -radius; z <= radius; z++) {
									Block b = world.getBlockAt((int) Math.round(x + x2), (int) Math.round(y + y2), (int) Math.round(z + z2));
									boolean block = false;
									
									if(!entity.getNearbyEntities(0.1, 0.5, 0.1).isEmpty()) {
										block = true;
									}
									
									if(b != null) {
										if(b.getType() != Material.AIR) {
											if(!blocks.contains(b.getType())) {
												block = true;
											}
										}
									}
									
									if (block) {
										Particle particle = new Particle(EnumParticle.EXPLOSION_LARGE, entity.getLocation(), true, 0.5, 0.5, 0.5, 0.005, 3);
										Particle particle1 = new Particle(EnumParticle.FIREWORKS_SPARK, entity.getLocation(), true, 0.5, 0.5, 0.5, 0.2, 5);
										entity.getWorld().playSound(entity.getLocation(), Sound.EXPLODE, 1, 1);
										particle.sendAll();
										particle1.sendAll();
										
										for(Entity entity2 : entity.getNearbyEntities(5, 3, 5)) {
											if(!(entity2 instanceof Player)) {
												if(entity2 instanceof LivingEntity) {
													LivingEntity livingEntity = (LivingEntity) entity2;
													
													Vector unitVector = entity2.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
													unitVector.add(new Vector(0, 0.2, 0));
													entity2.setVelocity(unitVector);
													
													livingEntity.damage(15);
												} else if(entity2 instanceof Item) {
													entity2.remove();
												}
											}
										}
										
										MobSlayerGame.getGameManager().grenades.remove(entity);
										entity.remove();
									}
								}
							}
						}
					}
				}
			}
		}, 2, 2);
		
		// Spectator Pfeile
//		Bukkit.getScheduler().scheduleSyncRepeatingTask(MobSlayerCore.getInstance(), new Runnable() {
//			
//			@Override
//			public void run() {
//				for(User user : MobSlayerCore.getUserManager().getStorage()) {
//					GameUser gameUser = (GameUser) user;
//					if(gameUser.isSpectating()) {
//						if(!gameUser.getPlayer().getNearbyEntities(2, 1, 2).isEmpty()) {
//							for(Entity entity : gameUser.getPlayer().getNearbyEntities(2, 1, 2)) {
//								if(entity instanceof Projectile) {
//									gameUser.getPlayer().setVelocity(new Vector(0, 1, 0));
//									gameUser.getPlayer().setFlying(true);
//									break;
//								}
//							}
//						}
//					}
//				}
//				
//			}
//		}, 1, 1);
		
		if (gameTaskId == -1) {
			gameTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobSlayerCore.getInstance(), new Runnable() {

				@Override
				public void run() {
					if (MobSlayerGame.getWaveManager().getActiveWave() != null) {
						Wave wave = MobSlayerGame.getWaveManager().getActiveWave();

						for(GameUser gameUser : MobSlayerGame.getGameManager().alive) {
							if(gameUser.getNoDamageSeconds() > 0) {
								gameUser.setNoDamageSeconds(gameUser.getNoDamageSeconds()-1);
							}
						}
						
						if (wave.getPrepareTime() > 0) {
							// Handle pre wave
							MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(new PreWaveHandleEvent(wave));
							
						} else {
							// Handle wave
							MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(new WaveHandleEvent(wave));
							
							if(!wave.isStarted()) {
								wave.setStarted(true);
								MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(new WaveStartEvent(wave));
							}
						}
					}
				}

			}, 20, 20);
		}
	}

	public static void stopGameTask() {
		if (gameTaskId != -1) {
			Bukkit.getScheduler().cancelTask(gameTaskId);
			gameTaskId = -1;
		}
	}
}
