package com.rex2go.mobslayer_game.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Particle;
import com.rex2go.mobslayer_core.util.ParticleEffect;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.GameItem;
import com.rex2go.mobslayer_game.item.GameItem.Attribute;
import com.rex2go.mobslayer_game.item.Item;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;

public class EntityDamageByEntityListener implements Listener {

	public EntityDamageByEntityListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity entity = event.getEntity();
		
		if(event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			if(event.getEntity() instanceof Player) {
				if(arrow.getShooter() != null) {
					if(arrow.getShooter() instanceof Player) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
		
		if (MobSlayerGame.getGameEntityManager().isChickenEntity(entity)) {
			if(event.getDamager() instanceof Player) {
				GameUser gameUser = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(event.getDamager().getUniqueId());
				
				if(gameUser.isSpectating()) {
					event.setCancelled(true);
					return;
				}
				
				ArrayList<Player> blood = new ArrayList<>();
				
				for(User user : MobSlayerCore.getUserManager().getStorage()) {
					if(user.getSettings().showBlood()) {
						blood.add(user.getPlayer());
					}
				}
				
				if(!blood.isEmpty()) {
					ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.1F, 15, 
							event.getEntity().getLocation(), blood);
				}
			}
		}
		
		if (MobSlayerGame.getGameEntityManager().isGameEntity(entity)) {
			GameEntity gameEntity = MobSlayerGame.getGameEntityManager().getGameEntity(entity);
			
			if(event.getDamager() instanceof Player) {
				GameUser gameUser = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(event.getDamager().getUniqueId());
				
				if(gameUser.isSpectating()) {
					event.setCancelled(true);
					return;
				}
				
				ItemStack itemStack = gameUser.getPlayer().getInventory().getItemInHand();
				
				if(itemStack != null) {
					if(MobSlayerGame.getGameItemManager().isGameItem(itemStack)) {
						GameItem gameItem = GameItem.fromItemStack(itemStack);
						
						if(gameItem.getGameItemId() == Item.FIRE_ROD.getGameItemId()) {
							event.getEntity().setFireTicks(100);
						} else if(gameItem.getGameItemId() == Item.ARMOR_BREAKER.getGameItemId()) {
							if(gameEntity.getGameEntityType().isBoss()) {
								gameUser.getPlayer().getInventory().setItemInHand(null);
								gameUser.getPlayer().getLocation().getWorld().playSound(gameUser.getPlayer().getLocation(), Sound.ITEM_BREAK, 1, 1);
							} else {
								if(entity instanceof LivingEntity) {
									LivingEntity entity1 = (LivingEntity) entity;
									
									if(GameItem.getUses(itemStack) < 10 || gameItem.hasAttribute(Attribute.ENCHANTMENT_INFINITE)) {
										if(entity1.getEquipment() != null) {
											entity1.getEquipment().setArmorContents(null);
											
											gameUser.getPlayer().getLocation().getWorld().playSound(gameUser.getPlayer().getLocation(), Sound.ZOMBIE_WOODBREAK, 1, 1);
											
											net.minecraft.server.v1_8_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
											NBTTagCompound tag;
											if (!nmsItemStack.hasTag()) {
												tag = new NBTTagCompound();
												nmsItemStack.setTag(tag);
											} else {
												tag = nmsItemStack.getTag();
											}
											
											tag.set("Unbreakable", new NBTTagInt(0));
											nmsItemStack.setTag(tag);
											itemStack.setData(CraftItemStack.asBukkitCopy(nmsItemStack).getData());
											itemStack.setItemMeta(CraftItemStack.asBukkitCopy(nmsItemStack).getItemMeta());
											
											GameItem.setUses(itemStack, GameItem.getUses(itemStack)+1);
											
											if(!gameItem.hasAttribute(Attribute.ENCHANTMENT_INFINITE)) {
												itemStack.setDurability((short) (GameItem.getUses(itemStack)*3+GameItem.getUses(itemStack)*0.2));
												
												if(GameItem.getUses(itemStack) == 10) {
													gameUser.getPlayer().getInventory().setItemInHand(null);
													gameUser.getPlayer().getLocation().getWorld().playSound(gameUser.getPlayer().getLocation(), Sound.ITEM_BREAK, 1, 1);
												}
											}
										}
									} else {
										gameUser.getPlayer().getInventory().setItemInHand(null);
										gameUser.getPlayer().getLocation().getWorld().playSound(gameUser.getPlayer().getLocation(), Sound.ITEM_BREAK, 1, 1);
									}
								}
							}
						} else if(gameItem.getGameItemId() == Item.IRON_AXE.getGameItemId()) {
							List<Entity> list =  event.getEntity().getNearbyEntities(1, 1, 1);
							
							for(Entity entity1 : list) {
								if(MobSlayerGame.getGameEntityManager().isGameEntity(entity1)) {
									GameEntity gameEntity2 = MobSlayerGame.getGameEntityManager().getGameEntity(entity1);
									
									net.minecraft.server.v1_8_R3.Entity entity2 = ((CraftEntity) entity1).getHandle();
									entity2.damageEntity(DamageSource.GENERIC, 1);
									gameEntity2.setDamager(gameUser);
									gameEntity2.damage(event.getDamager());
									
									if(gameEntity2.blood) {
										ArrayList<Player> blood = new ArrayList<>();
										
										for(User user : MobSlayerCore.getUserManager().getStorage()) {
											if(user.getSettings().showBlood()) {
												blood.add(user.getPlayer());
											}
										}
										
										if(!blood.isEmpty()) {
											ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.1F, 15, 
													entity1.getLocation().clone().add(0, 1, 0), blood);
										}
									}
								}
							}
						} else if(gameItem.getGameItemId() == Item.DIAMOND_AXE.getGameItemId()) {
							List<Entity> list =  event.getEntity().getNearbyEntities(1, 1, 1);
							
							for(Entity entity1 : list) {
								if(MobSlayerGame.getGameEntityManager().isGameEntity(entity1)) {
									GameEntity gameEntity2 = MobSlayerGame.getGameEntityManager().getGameEntity(entity1);
									
									net.minecraft.server.v1_8_R3.Entity entity2 = ((CraftEntity) entity1).getHandle();
									entity2.damageEntity(DamageSource.GENERIC, 2);
									gameEntity2.setDamager(gameUser);
									gameEntity2.damage(event.getDamager());
									
									if(gameEntity2.blood) {
										ArrayList<Player> blood = new ArrayList<>();
										
										for(User user : MobSlayerCore.getUserManager().getStorage()) {
											if(user.getSettings().showBlood()) {
												blood.add(user.getPlayer());
											}
										}
										
										if(!blood.isEmpty()) {
											ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.1F, 15, 
													entity1.getLocation().clone().add(0, 1, 0), blood);
										}
									}
								}
							}
						}
						
						if(gameItem.hasAttribute(Attribute.ENCHANTMENT_POISON)) {
							if(gameEntity.getGameEntityType() == GameEntityType.SPIDER) {
								int random = ThreadLocalRandom.current().nextInt(1, 5 + 1);
								
								if(random == 5) {
									entity.remove();
									try {
										CaveSpider cave = (CaveSpider) GameEntityType.CAVE_SPIDER.getClazz().newInstance().spawn(entity.getLocation());
										Spider spider = (Spider) entity;
										cave.setHealth(spider.getHealth()*1.5);
									} catch (InstantiationException | IllegalAccessException e) {
										e.printStackTrace();
									}
									Particle particle = new Particle(EnumParticle.VILLAGER_ANGRY, entity.getLocation(), false, 0.5, 0.5, 0.5, 0.1, 8);
									particle.sendAll();
									particle = new Particle(EnumParticle.EXPLOSION_HUGE, entity.getLocation(), false, 0.5, 0.5, 0.5, 0.1, 7);
									particle.sendAll();
									
									entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.EXPLODE, 1, 1);
									return;
								}
							}
							
							gameEntity.setPoison(30);
						}
					}
				}
				
				if(gameEntity.blood) {
					ArrayList<Player> blood = new ArrayList<>();
					
					for(User user : MobSlayerCore.getUserManager().getStorage()) {
						if(user.getSettings().showBlood()) {
							blood.add(user.getPlayer());
						}
					}
					
					if(!blood.isEmpty()) {
						ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.1F, 15, 
								event.getEntity().getLocation().clone().add(0, 1, 0), blood);
					}
				}
				
				gameEntity.setDamager(gameUser);
			} else if(event.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) event.getDamager();
				
				if(!(event.getEntity() instanceof Player)) {
					if(arrow.getShooter() != null) {
						if(arrow.getShooter() instanceof Player) {
							Player player = (Player) arrow.getShooter();
							ArrayList<Player> blood = new ArrayList<>();
							
							for(User user : MobSlayerCore.getUserManager().getStorage()) {
								if(user.getSettings().showBlood()) {
									blood.add(user.getPlayer());
								}
							}
							
							player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 0.5F);
							
							if(gameEntity.blood) {
								if(!blood.isEmpty()) {
									ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.3F, 0.3F, 0.3F, 0.1F, 15, 
											event.getEntity().getLocation().clone().add(0, 1, 0), blood);
								}
							}
						}
					}
				}
			}
			
			if(gameEntity.getGameEntityType() != GameEntityType.PIG_ZOMBIE_TANK) {
				entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.HURT_FLESH, 1, 0.8f);
			} else {
				entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.ZOMBIE_METAL, 1, 0.8f);
			}
		}
		
		if(entity instanceof Player) {
			GameUser gameUser = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(entity.getUniqueId());
			
			if(gameUser.isSpectating()) {
				event.setCancelled(true);
				return;
			}
			
			if(MobSlayerGame.getGameEntityManager().isChickenEntity(event.getDamager())) {
				event.setDamage(0);
			}
			
			if(event.getDamager() instanceof Player) {
				event.setCancelled(true);
				return;
			}
			
			if(MobSlayerGame.getGameEntityManager().isGameEntity(event.getDamager())) {
				GameEntity gameEntity = MobSlayerGame.getGameEntityManager().getGameEntity(event.getDamager());
				gameEntity.attack((GameUser) MobSlayerCore.getUserManager().getUserByUUID(entity.getUniqueId()), event.getDamager());
			}
		}
	}
}
