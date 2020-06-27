package com.rex2go.mobslayer_game.listener;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.Title;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.ItemUtil;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.crafting.Craft;
import com.rex2go.mobslayer_game.item.GameItem;
import com.rex2go.mobslayer_game.item.GameItem.Attribute;
import com.rex2go.mobslayer_game.item.ShopItem;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.user.GameUser;
import com.rex2go.mobslayer_game.vote.Vote;

public class InventoryClickListener implements Listener {

	public InventoryClickListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!MobSlayerGame.setup) {
			Player player = (Player) event.getWhoClicked();
			GameUser user = (GameUser) MobSlayerCore.getUserManager().getUserByName(player.getName());
			
			if (event.getClick() != null) {
				if (event.getClickedInventory() != null) {
					if (event.getCurrentItem() != null) {
						if (event.getClickedInventory().getName() != null) {

							if (MobSlayerGame.getGameManager().getGameState() != GameState.INGAME) {

								event.setCancelled(true);

								// Vote Menü
								if (event.getClickedInventory().getName()
										.equals(Translation.getTranslation("game.lobby.voting", user.getLanguage()))) {
									if (MobSlayerGame.getVoteManager().isAllowVote()) {
										if (event.getCurrentItem().getType() == Material.PAPER) {
											if (event.getCurrentItem().getItemMeta() != null) {
												if (event.getCurrentItem().getItemMeta().getDisplayName() != null) {
													String mapName = event.getCurrentItem().getItemMeta()
															.getDisplayName().substring(2)
															.substring(0, event.getCurrentItem().getItemMeta()
																	.getDisplayName().length() - 2 - 12);
													Vote vote = MobSlayerGame.getVoteManager()
															.getVoteByMapName(mapName);

													if (vote == null) {
														mapName = event.getCurrentItem().getItemMeta().getDisplayName()
																.substring(2)
																.substring(0, event.getCurrentItem().getItemMeta()
																		.getDisplayName().length() - 2 - 11);
														vote = MobSlayerGame.getVoteManager().getVoteByMapName(mapName);
													}

													if (vote != null) {
														if (user.getVote() == null) {
															user.setVote(vote);
															vote.setVotes(vote.getVotes() + 1);
															user.getPlayer().sendMessage(MobSlayerCore.PREFIX + "§e " + Translation.getTranslation("game.lobby.voting.voted_for_map", 
																	user.getLanguage(), vote.getMapName()));
															user.getPlayer().playSound(user.getPlayer().getLocation(),
																	Sound.NOTE_PLING, 1, 1);
														} else {
															if (user.getVote() != vote) {
																user.getVote().setVotes(user.getVote().getVotes() - 1);
																user.setVote(vote);
																vote.setVotes(vote.getVotes() + 1);
																user.getPlayer().sendMessage(MobSlayerCore.PREFIX + "§e " + Translation.getTranslation("game.lobby.voting.voted_for_map",
																				user.getLanguage(), vote.getMapName()));
																user.getPlayer().playSound(
																		user.getPlayer().getLocation(),
																		Sound.NOTE_PLING, 1, 1);
															} else {
																user.getPlayer()
																		.sendMessage(MobSlayerCore.PREFIX + "§e "
																				+ Translation.getTranslation(
																						"game.lobby.voting.already_voted_for_map",
																						user.getLanguage()));
																user.getPlayer().playSound(
																		user.getPlayer().getLocation(), Sound.NOTE_BASS,
																		1, 1);
															}
														}
													}

													player.closeInventory();
												}
											}
										}
									} else {
										player.closeInventory();
									}
								}

								// Customization Menü
								if (event.getClickedInventory().getName().equals(Translation.getTranslation(
										"game.lobby.customization.choose_an_armor_part", user.getLanguage()))) {
									if (event.getCurrentItem().getType() != null) {
										if (event.getCurrentItem().getType() != Material.AIR) {
											user.openCustomizationMenu(event.getCurrentItem());
										}
									}
								}

								// Customization Menü 2
								if (event.getClickedInventory().getName().equals(Translation.getTranslation(
										"game.lobby.customization.choose_a_color", user.getLanguage()))) {
									if (event.getCurrentItem().getType() != null) {
										if (event.getCurrentItem().getType() != Material.AIR) {
											if (event.getCurrentItem().getType() != Material.GLASS) {
												if(event.getCurrentItem().getType() == Material.BARRIER) {
													player.playSound(player.getLocation(), Sound.ZOMBIE_METAL, 1, 1);
													return;
												}
												
												Color color = ((LeatherArmorMeta) event.getCurrentItem().getItemMeta())
														.getColor();

												if (event.getCurrentItem().getType() == Material.LEATHER_HELMET) {
													user.getSettings().getArmorColors().set(0, color);
												} else if (event.getCurrentItem()
														.getType() == Material.LEATHER_CHESTPLATE) {
													user.getSettings().getArmorColors().set(1, color);
												} else if (event.getCurrentItem()
														.getType() == Material.LEATHER_LEGGINGS) {
													user.getSettings().getArmorColors().set(2, color);
												} else if (event.getCurrentItem().getType() == Material.LEATHER_BOOTS) {
													user.getSettings().getArmorColors().set(3, color);
												}

												user.openCustomizationMenu();
											} else {
												if (event.getClickedInventory().contains(Material.LEATHER_HELMET)) {
													user.getSettings().getArmorColors().set(0, null);
												} else if (event.getClickedInventory()
														.contains(Material.LEATHER_CHESTPLATE)) {
													user.getSettings().getArmorColors().set(1, null);
												} else if (event.getClickedInventory()
														.contains(Material.LEATHER_LEGGINGS)) {
													user.getSettings().getArmorColors().set(2, null);
												} else if (event.getClickedInventory()
														.contains(Material.LEATHER_BOOTS)) {
													user.getSettings().getArmorColors().set(3, null);
												}

												user.openCustomizationMenu();
											}
										}
									}
								}
							} else {
								if(!MobSlayerGame.getGameManager().alive.contains(user)) {
									event.setCancelled(true);
								}
								
								// Soulbound
								if (event.getCurrentItem().getItemMeta() != null) {
									if (event.getCurrentItem().getItemMeta().getLore() != null) {
										if (event.getCurrentItem().getItemMeta().getLore().contains("§6" + Translation
												.getTranslation("game.general.soulbound", user.getLanguage()))) {
											event.setCancelled(true);
										}
									}
								}
								
								// Research
								if (user.getPlayer().getOpenInventory().getTitle().equals(Translation.getTranslation("game.research_table.title", user.getLanguage()))) {
									if (event.getCurrentItem().getType() != null) {
//										if((event.getClick() == ClickType.SHIFT_LEFT && event.getClickedInventory().getType() == InventoryType.PLAYER) 
//												||  (event.getClick() == ClickType.SHIFT_LEFT && event.getClickedInventory().getName().equals(
//														Translation.getTranslation("game.research_table.title", user.getLanguage())))) {
										
										if(event.getClick() == ClickType.LEFT) {
											if(event.getClickedInventory().getName().equals(Translation.getTranslation("game.research_table.title", user.getLanguage()))) {
												if(event.getSlot() != 4) {
													event.setCancelled(true);
													
													if(event.getCurrentItem() != null) {
														if(event.getCurrentItem().getType() != Material.AIR) {
															if(player.getOpenInventory().getItem(4).getType() != Material.AIR) {
																GameItem gameItem = GameItem.fromItemStack(player.getOpenInventory().getItem(4));
																
																if(gameItem != null) {
																	if(event.getCurrentItem().getType() == Material.ENCHANTMENT_TABLE) {
																		if(gameItem.hasAttribute(Attribute.CRAFTED)) {
																			if(user.getCoins() >= gameItem.getWorth()) {
																				ArrayList<Craft> crafts = Craft.getCraft(gameItem);
																				boolean new1 = false;
																				Title title = null;
																				
																				for(Craft craft : crafts) {
																					if(!user.knowCraft(craft)) {
																						title = user.learnCraft(craft);
																						if(title == null) {
																							new1 = true;
																							break;
																						}
																					}
																				}
																				
																				if(title == null) {
																					if(new1) {
																						player.getOpenInventory().setItem(4, new ItemStack(Material.AIR));
																						user.setCoins(user.getCoins()-gameItem.getWorth());
																						user.setInGameCoins(user.getInGameCoins()-gameItem.getWorth());
																						player.closeInventory();
																					}
																				} else {
																					player.sendMessage("§c" + Translation.getTranslation("general.craft.locked", user.getLanguage(), 
																							Translation.getTranslation(title.getTranslationPath(), user.getLanguage())));
																					player.playSound(player.getLocation(), Sound.ZOMBIE_METAL, 1, 1);
																					
																					user.sendProgress(title, true);
																					player.closeInventory();
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												} else {
													event.setCancelled(false);
													
													player.getOpenInventory().setItem(3, ItemUtil.setDisplayname(player.getOpenInventory().getItem(3), "§f" 
															+ Translation.getTranslation("game.research_table.put_in_item", user.getLanguage())));
													player.getOpenInventory().setItem(3, ItemUtil.setDisplayname(player.getOpenInventory().getItem(5), "§f" 
															+ Translation.getTranslation("game.research_table.put_in_item", user.getLanguage())));
													
													player.getInventory().addItem(event.getCurrentItem());
													player.getOpenInventory().setItem(4, new ItemStack(Material.AIR));
												}
											} else {
												if(event.getCurrentItem() != null) {
													if(event.getCurrentItem().getType() != Material.AIR) {
														if(player.getOpenInventory().getItem(4).getType() == Material.AIR) {
															GameItem gameItem = GameItem.fromItemStack(event.getCurrentItem());
															
															if(gameItem != null) {
																if(gameItem.hasAttribute(Attribute.CRAFTED)) {
																	ArrayList<Craft> crafts = Craft.getCraft(gameItem);
																	boolean know = false;
																	if(!crafts.isEmpty()) {
																		for(Craft c : crafts) {
																			if(user.knowCraft(c)) {
																				know = true;
																				break;
																			}
																		}
																	}
														
																	if(!know) {
																		player.getOpenInventory().setItem(3, ItemUtil.setDisplayname(player.getOpenInventory().getItem(5), "§f" 
																				+ Translation.getTranslation("game.research_table.research_for", user.getLanguage(), gameItem.getWorth() + "")));
																	} else {
																		player.getOpenInventory().setItem(3, ItemUtil.setDisplayname(player.getOpenInventory().getItem(5), "§f" 
																				+ Translation.getTranslation("game.research_table.already_researched", user.getLanguage())));
																	}
																} else {
																	player.getOpenInventory().setItem(3, ItemUtil.setDisplayname(player.getOpenInventory().getItem(5), "§f" 
																			+ Translation.getTranslation("game.research_table.not_researchable", user.getLanguage())));
																}
																
																if(!gameItem.hasAttribute(Attribute.SOUL_BOUND)) {
																	ItemStack itemStack1 = event.getCurrentItem().clone();
																	itemStack1.setAmount(1);
																	
																	player.getOpenInventory().setItem(4, itemStack1);
																	
																	if(event.getCurrentItem().getAmount() > 1) {
																		event.getCurrentItem().setAmount(event.getCurrentItem().getAmount()-1);
																	} else {
																		event.getClickedInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
																	}
																	
																	event.setCancelled(true);
																}
															}
														}
													}
												}
											}
										} else {
											event.setCancelled(true);
										}
											
										player.updateInventory();
									} else {
										event.setCancelled(true);
									}
//									}
								}
								
								// Recipe Menu
								if (event.getInventory().getName().equals(Translation.getTranslation("game.general.recipe_book", user.getLanguage()))) {
									if (event.getCurrentItem().getType() != null) {
										if (event.getCurrentItem().getType() != Material.AIR) {
											event.setCancelled(true);
											
											if(MobSlayerGame.getGameItemManager().isGameItem(event.getCurrentItem())) {
												user.openRecipe(GameItem.fromItemStack(event.getCurrentItem()));
											}
										}
									}
								}
								
								// Recipe Display
								if (event.getInventory().getName().equals(Translation.getTranslation("game.craft.recipe_display", user.getLanguage()))) {
									if (event.getCurrentItem().getType() != null) {
										if (event.getCurrentItem().getType() != Material.AIR) {
											event.setCancelled(true);
											
											if(!MobSlayerGame.getGameItemManager().isGameItem(event.getCurrentItem())) {
												if(event.getCurrentItem().getType() == Material.BARRIER) {
													user.openRecipeBook();
												}
											} else {
												user.openRecipe(GameItem.fromItemStack(event.getCurrentItem()));
											}
										}
									}
								}
								
								// Shop Menü
								if (event.getInventory().getName().equals(Translation.getTranslation("game.shop", user.getLanguage()))) {
									if (event.getCurrentItem().getType() != null) {
										if (event.getCurrentItem().getType() != Material.AIR) {
											event.setCancelled(true);
											
											if(MobSlayerGame.getGameItemManager().isGameItem(event.getCurrentItem())) {
												GameItem gameItem = GameItem.fromItemStack(event.getCurrentItem());
												ShopItem shopItem = null;
												
												for(ShopItem shopItem2 : ShopItem.getShopItems()) {
													if(shopItem2.getGameItem().getGameItemId() == gameItem.getGameItemId()) {
														shopItem = shopItem2;
														break;
													}
												}
												
												if(shopItem != null) {
													if(event.getClickedInventory().getTitle().equals(Translation.getTranslation("game.shop", user.getLanguage()))) {
														// Kaufen
														
														if(user.getCoins() >= shopItem.getPrice()) {
															user.setCoins(user.getCoins()-shopItem.getPrice());
															user.setInGameCoins(user.getInGameCoins()-shopItem.getPrice());
															player.getInventory().addItem(gameItem.toItemStack(user));
															user.updateScoreboard();
														} else {
															// TODO nicht genug Coins
														}
													} else {
														// Verkaufen
														
														if(!event.isShiftClick()) {
															user.setCoins(user.getCoins()+shopItem.getSellValue());
															user.setInGameCoins(user.getInGameCoins()+shopItem.getSellValue());
															user.updateScoreboard();
															
															if(event.getCurrentItem().getAmount() > 1) {
																event.getCurrentItem().setAmount(event.getCurrentItem().getAmount()-1);
															} else {
																event.getClickedInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
															}
															
															// TODO Nachricht
														} else {
															int coins = 0;
															
															for(int i = 0; i<event.getCurrentItem().getAmount(); i++) {
																coins += shopItem.getSellValue();
															}
															
															user.setCoins(user.getCoins()+coins);
															user.setInGameCoins(user.getInGameCoins()+coins);
															user.updateScoreboard();
															
															event.getClickedInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
															
															// TODO Nachricht
														}
													}
												}
											}
										}
									}
								}
								
								// Spectate Menü
								if (event.getClickedInventory().getName().equals(Translation.getTranslation(
										"game.spectate.select_a_player", user.getLanguage()))) {
									if (event.getCurrentItem().getType() != null) {
										if (event.getCurrentItem().getType() != Material.AIR) {
											if(event.getCurrentItem().getType() == Material.SKULL_ITEM) {
												if(event.getCurrentItem().getItemMeta() != null) {
													if(event.getCurrentItem().getItemMeta().getDisplayName() != null) {
														String name = event.getCurrentItem().getItemMeta().getDisplayName().substring(2);
														GameUser target = null;
														
														for(User user1 : MobSlayerCore.getUserManager().getStorage()) {
															if(user1.getPlayer().getName().equals(name)) {
																target = (GameUser) user1;
																break;
															}
														}
														
														if(target != null) {
															if(!target.isDead()) {
																if(user.isDead()) {
																	if(!user.isSpectating()) {
																		user.setSpectating(true);
																	}
																	
																	user.getPlayer().teleport(target.getPlayer().getLocation().clone().add(0, 2, 0));
																	user.getPlayer().setFlying(true);
																}
															} else {
																user.sendTranslatedMessage("game.spectate.player_is_dead", "§c", null);
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
				}
			}
		}
	}
}
