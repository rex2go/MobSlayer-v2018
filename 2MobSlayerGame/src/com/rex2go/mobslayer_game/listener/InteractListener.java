package com.rex2go.mobslayer_game.listener;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.util.NBT;
import com.rex2go.mobslayer_core.util.Particle;
import com.rex2go.mobslayer_core.util.ParticleEffect;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.crafting.Craft;
import com.rex2go.mobslayer_game.item.GameItem;
import com.rex2go.mobslayer_game.item.GameItem.Attribute;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.misc.AirDrop;
import com.rex2go.mobslayer_game.misc.HealingStation;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.mob.Packet;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagString;

public class InteractListener implements Listener {

	public InteractListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		ItemStack itemStack = event.getItem();
		Action action = event.getAction();
		Player player = event.getPlayer();
		GameUser user = (GameUser) MobSlayerCore.getUserManager().getUserByName(player.getName());
		
		if (!MobSlayerGame.setup) {
			if (user != null) {
				if(user.isSpectating()) {
					event.setCancelled(true);
				} else {
					if (MobSlayerGame.getGameManager().getGameState() == GameState.INGAME) {
						if(itemStack != null) {
							if(MobSlayerGame.getGameItemManager().isGameItem(itemStack)) {
								GameItem gameItem = GameItem.fromItemStack(itemStack);
								boolean block = false;
								ArrayList<Material> blockInteraction = new ArrayList<>();
								
								blockInteraction.add(Material.CAULDRON);
								blockInteraction.add(Material.ENCHANTMENT_TABLE);
								blockInteraction.add(Material.BEACON);
								blockInteraction.add(Material.FURNACE);
								blockInteraction.add(Material.BURNING_FURNACE);
								blockInteraction.add(Material.CHEST);
								blockInteraction.add(Material.ENDER_CHEST);
								
								if(event.getClickedBlock() != null) {
									if(blockInteraction.contains(event.getClickedBlock().getType())) {
										block = true;
									}
								}
								
								if(!block) {
									
									// Grenade
									if(gameItem.getGameItemId() == com.rex2go.mobslayer_game.item.Item.GRENADE.getGameItemId()) {									
										if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
											ItemStack itemStack1 = new ItemStack(Material.MAGMA_CREAM);
											net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack1);
											NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
											
											if(!tag.hasKey("noPickup")) {
												tag.set("noPickup", new NBTTagString(UUID.randomUUID().toString()));
											}
											
											nmsItem.setTag(tag);
											
											itemStack1 = CraftItemStack.asCraftMirror(nmsItem);
											
											Item itemEntity = player.getWorld().dropItemNaturally(player.getEyeLocation().add(player.getLocation().getDirection()), itemStack1);
											itemEntity.setVelocity(player.getLocation().getDirection().multiply(1.2));
													
											MobSlayerGame.getGameManager().grenades.add(itemEntity);
												
											if(itemStack.getAmount() > 1) {
												itemStack.setAmount(itemStack.getAmount()-1);
											} else {
												player.setItemInHand(new ItemStack(Material.AIR));
											}
										}
									}
									
									// Heal 1
									if(gameItem.getGameItemId() == com.rex2go.mobslayer_game.item.Item.HEAL_1.getGameItemId()) {									
										if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
											if(player.getHealth() < player.getMaxHealth()) {
												user.heal(6, true);
												
												if(itemStack.getAmount() > 1) {
													itemStack.setAmount(itemStack.getAmount()-1);
												} else {
													player.setItemInHand(new ItemStack(Material.AIR));
												}
											}
										}
									}
									
									// Heal 2
									if(gameItem.getGameItemId() == com.rex2go.mobslayer_game.item.Item.HEAL_2.getGameItemId()) {									
										if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
											if(player.getHealth() < player.getMaxHealth()) {
												user.heal(8, true);
												
												if(itemStack.getAmount() > 1) {
													itemStack.setAmount(itemStack.getAmount()-1);
												} else {
													player.setItemInHand(new ItemStack(Material.AIR));
												}
											}
										}
									}
									
									// Heal 3
									if(gameItem.getGameItemId() == com.rex2go.mobslayer_game.item.Item.HEAL_3.getGameItemId()) {									
										if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
											if(player.getHealth() < player.getMaxHealth()) {
												user.heal(12, true);
												
												if(itemStack.getAmount() > 1) {
													itemStack.setAmount(itemStack.getAmount()-1);
												} else {
													player.setItemInHand(new ItemStack(Material.AIR));
												}
											}
										}
									}
									
									// Recipe Book
									if(gameItem.getGameItemId() == com.rex2go.mobslayer_game.item.Item.RECIPE_BOOK.getGameItemId()) {									
										if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
											user.openRecipeBook();
										}
									}
								}
							}
						}
						
						// Creeper
						if(event.getClickedBlock() != null) {
							if(event.getClickedBlock().getType() != null) {
								if(event.getClickedBlock().getType() == Material.WOODEN_DOOR) {
									for(Entity entity : MobSlayerGame.getMapManager().getGameMap().getWorld().getEntities()) {
										if(entity instanceof Creeper) {
											if(MobSlayerGame.getGameEntityManager().isGameEntity(entity)) {
												GameEntity gameEntity = MobSlayerGame.getGameEntityManager().getGameEntity(entity);
												
												if(gameEntity.getGameEntityType() == GameEntityType.CREEPER) {
													Location loc = event.getClickedBlock().getLocation();
													if(loc.distance(entity.getLocation()) < 25) {
														NBT.moveEntity((LivingEntity) entity, loc, 1.5F);	
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
				
				if (itemStack != null) {
					if (itemStack.getItemMeta() != null) {
						if (itemStack.getItemMeta().getDisplayName() != null) {
							if (MobSlayerGame.getGameManager().getGameState() != GameState.INGAME) {

								// Abstimmung
								if (itemStack.getItemMeta().getDisplayName().equals(
										"§6" + Translation.getTranslation("game.lobby.voting", user.getLanguage()))) {
									if (action != null) {
										if (action == Action.RIGHT_CLICK_AIR
												|| action == Action.RIGHT_CLICK_BLOCK) {
											if (MobSlayerGame.getVoteManager().isAllowVote()) {
												user.openVoteMenu();
											}
										}
									}
								}

								// Costumization
								if (itemStack.getItemMeta().getDisplayName().equals("§6"
										+ Translation.getTranslation("game.lobby.customization", user.getLanguage()))) {
									if (action != null) {
										if (action == Action.RIGHT_CLICK_AIR
												|| action == Action.RIGHT_CLICK_BLOCK) {
											user.openCustomizationMenu();
										}
									}
								}

								// Settings
								if (itemStack.getItemMeta().getDisplayName().equals(
										"§6" + Translation.getTranslation("game.lobby.settings", user.getLanguage()))) {
									if (action != null) {
										if (action == Action.RIGHT_CLICK_AIR
												|| action == Action.RIGHT_CLICK_BLOCK) {
											user.openSettings();
										}
									}
								}
							} else {
								
								// Spectator
								if (itemStack.getItemMeta().getDisplayName().equals("§6" + Translation
										.getTranslation("game.dead.switch_to_spectator_mode", user.getLanguage()))) {
									if (action != null) {
										if (action == Action.RIGHT_CLICK_AIR
												|| action == Action.RIGHT_CLICK_BLOCK) {
											if(!MobSlayerGame.getGameManager().alive.contains(user)) {
												user.openSpectatorMenu();
											}
										}
									}
								}
							}
						}
					}
				}
					
				if(event.getClickedBlock() != null) {
					if(event.getClickedBlock().getType() == Material.ENDER_CHEST) {
						if(action == Action.RIGHT_CLICK_BLOCK) {
							event.setCancelled(true);
							if(!user.isDead()) {
								if(!user.isSpectating()) {
									user.openShopMenu();
								}
							}
						}
					}
					
					if(event.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) {
						if(action == Action.RIGHT_CLICK_BLOCK) {
							event.setCancelled(true);
							if(!user.isDead()) {
								if(!user.isSpectating()) {
									user.openResearchMenu();
								}
							}
						}
					}
					
					if(event.getClickedBlock().getType() == Material.CHEST) {
						if(action == Action.RIGHT_CLICK_BLOCK) {
							event.setCancelled(true);
							if(!user.isDead()) {
								if(!user.isSpectating()) {
									if(MobSlayerGame.getAirDropManager().isAirDrop(event.getClickedBlock().getLocation())) {
										AirDrop airDrop = MobSlayerGame.getAirDropManager().getAirDrop(event.getClickedBlock().getLocation());
										if(airDrop.getState() != 0) {
											if(airDrop.getState() == 1) {
												airDrop.open();
												airDrop.setState(0);
											} else {
												airDrop.setState(airDrop.getState()-1);
											}
											ArrayList<Player> all = new ArrayList<>();
											
											for(Player all1 : Bukkit.getOnlinePlayers()) {
												all.add(all1);
											}
											
											ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.CHEST, (byte) 0), 1, 1, 1, 0.1F, 25, 
													event.getClickedBlock().getLocation().clone().add(0.5, -0.5, 0.5), all);
											
											airDrop.getBlock().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.ZOMBIE_WOODBREAK, 1, 1);
										}
									}
								}
							}
						}
					}
					
					if(event.getClickedBlock().getType() == Material.FURNACE || event.getClickedBlock().getType() == Material.FURNACE) {
						if(action == Action.RIGHT_CLICK_BLOCK) {
							event.setCancelled(true);
							if(!user.isDead()) {
								if(!user.isSpectating()) {
									if(itemStack != null) {
										if(itemStack.hasItemMeta()) {
											if(MobSlayerGame.getGameItemManager().isGameItem(itemStack)) {
												GameItem gameItem = GameItem.fromItemStack(itemStack);
												
												player.playSound(event.getClickedBlock().getLocation(), Sound.FIRE_IGNITE, 1, 1);
												
												if(gameItem.getGameItemId() == com.rex2go.mobslayer_game.item.Item.IRON_INGOT.getGameItemId()) {
													if(itemStack.getAmount() > 1) {
														itemStack.setAmount(itemStack.getAmount()-1);
														player.getInventory().addItem(com.rex2go.mobslayer_game.item.Item.GLOWING_IRON_INGOT.toItemStack(user));
													} else {
														player.setItemInHand(com.rex2go.mobslayer_game.item.Item.GLOWING_IRON_INGOT.toItemStack(user));
														
													}
												}
												if(gameItem.getGameItemId() == com.rex2go.mobslayer_game.item.Item.GOLD_INGOT.getGameItemId()) {
													if(itemStack.getAmount() > 1) {
														itemStack.setAmount(itemStack.getAmount()-1);
														player.getInventory().addItem(com.rex2go.mobslayer_game.item.Item.GLOWING_GOLD_INGOT.toItemStack(user));
													} else {
														player.setItemInHand(com.rex2go.mobslayer_game.item.Item.GLOWING_GOLD_INGOT.toItemStack(user));
														
													}
												}
											}
										}
									}
								}
							}
						}
					}
						
					if(event.getClickedBlock().getType() == Material.BEACON) {
						if(action == Action.RIGHT_CLICK_BLOCK) {
							event.setCancelled(true);
							
							if(!user.isDead()) {
								if(!user.isSpectating()) {
									HealingStation hs = null;
									
									for(HealingStation healingStation : HealingStation.getHealingStations()) {
										if(healingStation.getLocation().equals(event.getClickedBlock().getLocation())) {
											hs = healingStation;
											break;
										}
									}
									
									if(hs != null) {
										if(itemStack != null) {
											ItemStack item = itemStack;
											
											if(itemStack.getType() == Material.DIAMOND) {
												if(item.getItemMeta() != null) {
													if(MobSlayerGame.getGameItemManager().isGameItem(itemStack)) {
														if(hs.getState() < 3) {
															hs.setState(hs.getState()+1);
																
															if(itemStack.getAmount() > 1) {
																itemStack.setAmount(itemStack.getAmount()-1);
															} else {
																player.setItemInHand(new ItemStack(Material.AIR));
															}
																
															hs.getLocation().getWorld().playSound(hs.getLocation(), Sound.ANVIL_USE, 1, 1);
															
															user.sendTranslatedMessage("game.healing_station.repair", "§f", "");
														} else {
															hs.getLocation().getWorld().playSound(hs.getLocation(), Sound.NOTE_BASS, 1, 1);
															
															user.sendTranslatedMessage("game.healing_station.not_damaged", "§c", "");
														}
													}
												}
											} else {
												user.sendTranslatedMessage("game.healing_station.state_" + hs.getState(), "§7", "");
											}
										} else {
											user.sendTranslatedMessage("game.healing_station.state_" + hs.getState(), "§7", "");
										}
									}
								}
							}
						}
					}
						
					if(event.getClickedBlock().getType() == Material.CAULDRON) {
						if(action == Action.RIGHT_CLICK_BLOCK) {
							if(!user.isDead()) {
								if(!user.isSpectating()) {
									if(itemStack != null) {
										ItemStack item = itemStack;
										
										if(item.getItemMeta() != null) {
											if(itemStack.getItemMeta().hasLore()) {
												if(itemStack.getItemMeta().getLore().contains("§6" + Translation.getTranslation("game.general.soulbound", user.getLanguage())) ||
														GameItem.fromItemStack(itemStack).hasAttribute(Attribute.NO_CRAFT)) {
													if(itemStack.getItemMeta().getDisplayName() != null) {
														if(itemStack.getItemMeta().getDisplayName().equals("§f" + Translation.getTranslation("game.general.wand", 
																user.getLanguage()))) {
															ItemStack itemStack1 = Craft.craft(user);
															
															if(itemStack1 != null) {
																player.getInventory().addItem(itemStack1);
																user.getCrafting().clear();
																
																user.getPlayer().playSound(event.getClickedBlock().getLocation(), Sound.PORTAL_TRIGGER, 1, 1.5F);
																
																Location loc = event.getClickedBlock().getLocation();
																loc.add(0.5, 0, 0.5);
																
																Particle particle = new Particle(EnumParticle.SPELL, loc, false, 0, 0.1, 0, 1, 50);
																particle.sendPlayer(player);
															} else {
																user.getPlayer().playSound(event.getClickedBlock().getLocation(), Sound.NOTE_BASS, 1, 1);
																
																if(!user.getCrafting().isEmpty()) {
																	for(GameItem gameItem : user.getCrafting()) {
																		if(gameItem != null) {
																			user.getPlayer().getInventory().addItem(gameItem.toItemStack(user));
																		}
																	}
																	user.getCrafting().clear();
																	
																	Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {
																		
																		@Override
																		public void run() {
																			((CraftPlayer) user.getPlayer()).getHandle().playerConnection.sendPacket(Packet.blockChange(
																					event.getClickedBlock().getLocation(), Material.CAULDRON, (byte) (user.getCrafting().size()-1)));
																					
																			}
																		}, 1);
																}
															}
															
															// TODO Sounds etc
														}
													}
													
													Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {
														
														@Override
														public void run() {
															((CraftPlayer) user.getPlayer()).getHandle().playerConnection.sendPacket(Packet.blockChange(
																	event.getClickedBlock().getLocation(), Material.CAULDRON, (byte) (user.getCrafting().size()-1)));
																	
															}
														}, 1);
													return;
												}
											}
											
											if(MobSlayerGame.getGameItemManager().isGameItem(itemStack)) {
												GameItem gameItem = GameItem.fromItemStack(itemStack);
												
												if(gameItem.hasAttribute(Attribute.NO_CRAFT)) {
													return;
												}
											}
											
											for(Entity entity : MobSlayerGame.getNearbyEntities(event.getClickedBlock().getLocation(), 4)) {
												if(MobSlayerGame.getGameEntityManager().isGameEntity(entity)) {
													user.sendTranslatedMessage("game.crafting.enemies_nearby", "§c", "");
													user.getPlayer().playSound(event.getClickedBlock().getLocation(), Sound.NOTE_BASS, 1, 1);
													return;
												}
											}
											
											if(user.getCrafting().size() < 3) {												
												ItemStack craftItem = itemStack.clone();
												craftItem.setAmount(1);
												user.getCrafting().add(GameItem.fromItemStack(craftItem));
												
												if(itemStack.getAmount() > 1) {
													itemStack.setAmount(itemStack.getAmount()-1);
												} else {
													player.setItemInHand(new ItemStack(Material.AIR));
												}
														
												Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {
															
													@Override
													public void run() {
														((CraftPlayer) user.getPlayer()).getHandle().playerConnection.sendPacket(Packet.blockChange(
																event.getClickedBlock().getLocation(), Material.CAULDRON, (byte) (user.getCrafting().size()-1)));
																
														}
													}, 1);
											} else {
												for(GameItem gameItem : user.getCrafting()) {
													if(gameItem != null) {
														user.getPlayer().getInventory().addItem(gameItem.toItemStack(user));
													}
												}
												
												user.getPlayer().playSound(event.getClickedBlock().getLocation(), Sound.NOTE_BASS, 1, 1);
												
												user.getCrafting().clear();
														
												Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {
															
													@Override
													public void run() {
														((CraftPlayer) user.getPlayer()).getHandle().playerConnection.sendPacket(Packet.blockChange(
																event.getClickedBlock().getLocation(), Material.CAULDRON, (byte) 2));
																
													}
												}, 1);
											}
										}
									} else {
										if(!user.getCrafting().isEmpty()) {
											for(GameItem gameItem : user.getCrafting()) {
												if(gameItem != null) {
													user.getPlayer().getInventory().addItem(gameItem.toItemStack(user));
												}
											}
											
											user.getPlayer().playSound(event.getClickedBlock().getLocation(), Sound.NOTE_BASS, 1, 1);
											
											user.getCrafting().clear();
											
											Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {
												
												@Override
												public void run() {
													((CraftPlayer) user.getPlayer()).getHandle().playerConnection.sendPacket(Packet.blockChange(
															event.getClickedBlock().getLocation(), Material.CAULDRON, (byte) (user.getCrafting().size()-1)));
															
												}
											}, 1);
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
}
