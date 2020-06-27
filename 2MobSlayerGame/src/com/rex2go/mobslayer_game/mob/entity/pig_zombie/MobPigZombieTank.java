package com.rex2go.mobslayer_game.mob.entity.pig_zombie;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.util.NBT;
import com.rex2go.mobslayer_core.util.ParticleUtil;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.mob.Drop;
import com.rex2go.mobslayer_game.mob.Drop.Rarity;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPigZombie;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class MobPigZombieTank extends GameEntity {

	int charge = 0;

	public MobPigZombieTank() {
		super(GameEntityType.PIG_ZOMBIE_TANK);
		
		getDrops().add(new Drop(com.rex2go.mobslayer_game.item.Item.IRON_INGOT, Rarity.SPECIAL));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Entity spawn(Location location) {
		PigZombie zombie = (PigZombie) location.getWorld().spawnEntity(location.clone().add(0, 264, 0),
				GameEntityType.PIG_ZOMBIE_TANK.getEntityType());
		MobSlayerGame.getGameEntityManager().getStorage().put(zombie.getUniqueId(), this);

		zombie.getEquipment().clear();

		ItemStack itemStack;

		itemStack = new ItemStack(Material.DIAMOND_SWORD);
		zombie.getEquipment().setItemInHand(itemStack);

		itemStack = new ItemStack(Material.DIAMOND_HELMET);
		zombie.getEquipment().setHelmet(itemStack);

		itemStack = new ItemStack(Material.DIAMOND_CHESTPLATE);
		zombie.getEquipment().setChestplate(itemStack);

		itemStack = new ItemStack(Material.IRON_LEGGINGS);
		zombie.getEquipment().setLeggings(itemStack);

		itemStack = new ItemStack(Material.DIAMOND_BOOTS);
		zombie.getEquipment().setBoots(itemStack);

		zombie.setRemoveWhenFarAway(false);
		zombie.setMaxHealth(48);
		zombie.setHealth(48);
		zombie.setBaby(false);
		zombie.setCanPickupItems(false);

		zombie.setAngry(true);

		NBT.setSpeed(zombie, 0.12);
		NBT.setKnockback(zombie, 1);

		CraftZombie craftZombie = ((CraftZombie) zombie);
		EntityZombie entityZombie = craftZombie.getHandle();
		
		entityZombie.goalSelector = new PathfinderGoalSelector(((CraftWorld) location.getWorld()).getHandle() != null 
				&& ((CraftWorld) location.getWorld()).getHandle().methodProfiler != null ? ((CraftWorld) location.getWorld()).getHandle().methodProfiler : null);
		entityZombie.targetSelector = new PathfinderGoalSelector(((CraftWorld) location.getWorld()).getHandle() != null 
				&& ((CraftWorld) location.getWorld()).getHandle().methodProfiler != null ? ((CraftWorld) location.getWorld()).getHandle().methodProfiler : null);
		
		entityZombie.goalSelector.a(0, new PathfinderGoalFloat(entityZombie));
		entityZombie.goalSelector.a(2, new PathfinderGoalMeleeAttack(entityZombie, EntityHuman.class, 1.0D, false));
		entityZombie.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(entityZombie, 1.0D));
		entityZombie.goalSelector.a(7, new PathfinderGoalRandomStroll(entityZombie, 1.0D));
		entityZombie.goalSelector.a(8, new PathfinderGoalLookAtPlayer(entityZombie, EntityHuman.class, 8.0F));
		entityZombie.goalSelector.a(8, new PathfinderGoalRandomLookaround(entityZombie));
		
		entityZombie.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(entityZombie, 1.0D, false));
		
		entityZombie.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(entityZombie, EntityHuman.class, true));
		entityZombie.targetSelector.a(2, new PathfinderGoalHurtByTarget(entityZombie, false, new Class[0]));
		
		entityZombie.targetSelector.a(1, new PathfinderGoalHurtByTarget(entityZombie, true, new Class[] { EntityPigZombie.class}));
		entityZombie.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(entityZombie, EntityHuman.class, true));
		
		zombie.teleport(location);
		
		return zombie;
	}

	public int getCharge() {
		return charge;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	public void attackShockwave(PigZombie entity) {
		if (entity.getTarget() != null) {
			if (charge > 10) {
				if (!entity.getNearbyEntities(3, 2, 3).isEmpty()) {
					boolean test = false;
					for (Entity entity1 : entity.getNearbyEntities(3, 2, 3)) {
						if (entity1 instanceof Player) {
							GameUser gameUser = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(entity1.getUniqueId());
							if(!gameUser.isSpectating()) {
								test = true;
								break;
							}
						}
					}

					if (test) {
						charge = 0;

						NBT.setSpeed(entity, 0);

						entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.ZOMBIE_UNFECT, 1, 1);

						Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {

							@Override
							public void run() {
								if(!entity.isDead()) {
								
									for (Player all : Bukkit.getOnlinePlayers()) {
										PacketPlayOutAnimation packet = new PacketPlayOutAnimation(
												((CraftEntity) entity).getHandle(), 0);
										((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
									}
	
									entity.setVelocity(new Vector(0, 0.25, 0));
	
									entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.ZOMBIE_WOODBREAK,
											1, 1);
	
									ParticleUtil.circle(EnumParticle.CRIT_MAGIC, entity.getLocation(), 10, 0.7f);
	
									Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(),
											new Runnable() {
	
												@Override
												public void run() {
													ParticleUtil.circle(EnumParticle.CRIT_MAGIC, entity.getLocation(), 20,
															1.4f);
	
													Bukkit.getScheduler().scheduleSyncDelayedTask(
															MobSlayerCore.getInstance(), new Runnable() {
	
																@Override
																public void run() {
																	ParticleUtil.circle(EnumParticle.CRIT_MAGIC,
																			entity.getLocation(), 30, 2.1f);
	
																	for (Entity entity1 : entity.getNearbyEntities(3, 2,
																			3)) {
																		if(entity1 instanceof LivingEntity || entity1 instanceof Item) {
																			Vector unitVector = entity1.getLocation().toVector()
																					.subtract(entity.getLocation().toVector())
																					.normalize();
																			unitVector.add(new Vector(0, 0.2, 0));
																			entity1.setVelocity(unitVector);
		
																			float damage = 10;
		
																			if (entity1 instanceof Player) {
																				Player player = (Player) entity1;
																				GameUser gameUser = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());
																				
																				damage = (float) (damage - MobSlayerGame
																						.getDamageReduced(player));
																				
																				gameUser.damage(damage);
																				
																			} else {
																				((CraftEntity) entity1).getHandle().damageEntity(DamageSource.GENERIC, damage);
																			}
																		}
																	}
	
																	Bukkit.getScheduler().scheduleSyncDelayedTask(
																			MobSlayerCore.getInstance(), new Runnable() {
	
																				@Override
																				public void run() {
																					ParticleUtil.circle(
																							EnumParticle.CRIT_MAGIC,
																							entity.getLocation(), 40, 2.8f);
	
																					Bukkit.getScheduler()
																							.scheduleSyncDelayedTask(
																									MobSlayerCore
																											.getInstance(),
																									new Runnable() {
	
																										@Override
																										public void run() {
																											ParticleUtil
																													.circle(EnumParticle.CRIT_MAGIC,
																															entity.getLocation(),
																															50,
																															3.5f);
	
																											NBT.setSpeed(
																													entity,
																													0.12);
																										}
																									}, 2);
																				}
																			}, 2);
																}
															}, 2);
												}
											}, 2);
								}
							}
						}, 20);
					}
				}
			} else {
				charge++;
			}
		}
	}

	@Override
	public void attack(GameUser user, Entity attacker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void die(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void damage(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearTarget(Entity entity) {
		PigZombie pigZombie = (PigZombie) entity;
		pigZombie.setTarget(null);
	}

	@Override
	public Entity getTarget(Entity entity) {
		PigZombie pigZombie = (PigZombie) entity;
		return pigZombie.getTarget();
	}

}
