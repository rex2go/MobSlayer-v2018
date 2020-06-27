package com.rex2go.mobslayer_game.mob.entity.witch;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftWitch;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.NBT;
import com.rex2go.mobslayer_core.util.Particle;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.Item;
import com.rex2go.mobslayer_game.mob.Drop;
import com.rex2go.mobslayer_game.mob.Drop.Rarity;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityWitch;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class MobWitch extends GameEntity {

	int charge = 0;
	int taskId = -1;
	int attack = 0;

	Player target = null;
	Vector vector = null;

	public MobWitch() {
		super(GameEntityType.WITCH);
		
		getDrops().add(new Drop(Item.GLOWSTONE_DUST, Rarity.FREQUENTLY));
		getDrops().add(new Drop(Item.MAGIC_POWDER, Rarity.FREQUENTLY));
		getDrops().add(new Drop(Item.MAGIC_POWDER, Rarity.FREQUENTLY));
		getDrops().add(new Drop(Item.FIRE_POWDER, Rarity.COMMON));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Entity spawn(Location location) {
		org.bukkit.entity.Witch witch = (org.bukkit.entity.Witch) location.getWorld()
				.spawnEntity(location.clone().add(0, 264, 0), GameEntityType.WITCH.getEntityType());
		MobSlayerGame.getGameEntityManager().getStorage().put(witch.getUniqueId(), this);

		witch.getEquipment().clear();
		witch.setRemoveWhenFarAway(false);
		witch.setMaxHealth(48);
		witch.setHealth(48);
		witch.setCanPickupItems(false);

		NBT.setSpeed(witch, 0.32);

		CraftWitch craftWitch = ((CraftWitch) witch);
		EntityWitch entityWitch = craftWitch.getHandle();
		
		entityWitch.goalSelector = new PathfinderGoalSelector(((CraftWorld) location.getWorld()).getHandle() != null 
				&& ((CraftWorld) location.getWorld()).getHandle().methodProfiler != null ? ((CraftWorld) location.getWorld()).getHandle().methodProfiler : null);
		entityWitch.targetSelector = new PathfinderGoalSelector(((CraftWorld) location.getWorld()).getHandle() != null 
				&& ((CraftWorld) location.getWorld()).getHandle().methodProfiler != null ? ((CraftWorld) location.getWorld()).getHandle().methodProfiler : null);
		
		entityWitch.goalSelector.a(0, new PathfinderGoalFloat(entityWitch));
		entityWitch.goalSelector.a(2, new PathfinderGoalArrowAttack(entityWitch, 1.0D, 60, 10.0F));
		entityWitch.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(entityWitch, 1.0D));
		entityWitch.goalSelector.a(7, new PathfinderGoalRandomStroll(entityWitch, 1.0D));
		entityWitch.goalSelector.a(8, new PathfinderGoalLookAtPlayer(entityWitch, EntityHuman.class, 8.0F));
		entityWitch.goalSelector.a(8, new PathfinderGoalRandomLookaround(entityWitch));
		
		entityWitch.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(entityWitch, EntityHuman.class, true));
		entityWitch.targetSelector.a(2, new PathfinderGoalHurtByTarget(entityWitch, false, new Class[0]));
		
		witch.teleport(location);
		
		return witch;
	}

	public int getCharge() {
		return charge;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	public void attackLevitation(org.bukkit.entity.Witch entity) {
		if (entity.getTarget() != null) {
			if (charge > 1) {
				if (target == null) {
					if (!entity.getNearbyEntities(4, 2, 4).isEmpty()) {
						if(entity.getTarget() instanceof Player) {
							target = (Player) entity.getTarget();
							if (entity.getNearbyEntities(4, 2, 4).contains(target)) {
								if (target != null) {
									NBT.setSpeed(entity, 0);
									
									if (taskId != -1) {
										Bukkit.getScheduler().cancelTask(taskId);
									}
	
									target.getLocation().getWorld().playSound(target.getLocation(), Sound.ZOMBIE_UNFECT, 1,
											0.5f);
	
									taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobSlayerCore.getInstance(),
											new Runnable() {
	
												@Override
												public void run() {													
													if (attack < 240) {
														if (MobSlayerGame.getGameManager().alive.contains(MobSlayerCore
																.getUserManager().getUserByUUID(target.getUniqueId()))) {
															float yaw = ((CraftEntity) entity).getHandle().getHeadRotation();
															entity.getLocation().setYaw(yaw);
															((CraftEntity) entity).getHandle().yaw = yaw;
															
															new BukkitRunnable() {
																double t = 0;
																Location loc = entity.getLocation();
																Vector direction = loc.getDirection().normalize();
																
																public void hit(Location loc) {
//																	Particle particle = new Particle(EnumParticle.CLOUD, loc, true, 0.5, 0.5, 0.5, 0.5, 3);
//																	particle.sendAll();
																}

																@Override
																public void run() {

																	t += 0.2;
																	
																	double x = direction.getX() * t;
																	double y = direction.getY() * t + 1.5;
																	double z = direction.getZ() * t;

																	loc.add(x, y, z);

																	Particle particle = new Particle(EnumParticle.SPELL_WITCH, loc, true, 0, 0, 0, 0.01, 1);
																	particle.sendAll();

																	for (User user : MobSlayerCore.getUserManager().getStorage()) {
																		if (user.getPlayer().getLocation().distance(loc.clone().subtract(0, 1, 0)) < 0.5) {
																			this.cancel();
																		}
																	}
																	
																	loc.getWorld().playSound(loc, Sound.DIG_WOOD, 1, 1);

																	if (loc.getBlock().getType() != Material.AIR) {
																		this.cancel();
																		
																		hit(loc);
																	}

																	loc.subtract(x, y, z);

																	if (t > 5) {
																		this.cancel();

																		hit(loc);
																	}
																}
															}.runTaskTimer(MobSlayerCore.getInstance(), 0, 1);
															
															GameUser gameUser = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(target.getUniqueId());
															if(entity.getLocation().getPitch() > -30) {
																target.setVelocity(new Vector(0, 0.025, 0));
															}
															gameUser.damage(0.5f / MobSlayerGame.getDamageReduced(target));
															
															target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, 10));
															attack++;
														} else {
															release(entity);
														}
													} else {
														release(entity);
													}
	
												}
											}, 1, 1);
								}
							} else {
								release(entity);
							}
						} else {
							release(entity);
						}
					}
				}
			} else {
				charge++;

				if (taskId != -1) {
					Bukkit.getScheduler().cancelTask(taskId);
				}
			}
		}
	}

	private void release(Entity entity) {
		if (taskId != -1) {
			Bukkit.getScheduler().cancelTask(taskId);
		}
		charge = 0;
		attack = 0;
		taskId = -1;
		target = null;
		NBT.setSpeed(entity, 0.32);
	}

	@Override
	public void attack(GameUser user, Entity attacker) {
		org.bukkit.entity.Witch witch = (org.bukkit.entity.Witch) attacker;

		float yaw = ((CraftEntity) attacker).getHandle().getHeadRotation();
		witch.getLocation().setYaw(yaw);
		((CraftEntity) attacker).getHandle().yaw = yaw;

		witch.getWorld().playSound(witch.getLocation(), Sound.FIREWORK_LAUNCH, 1, 1);
		
		if (taskId == -1) {
			new BukkitRunnable() {
				double t = 0;
				Location loc = witch.getLocation();
				Vector direction = loc.getDirection().normalize();

				@SuppressWarnings("unused")
				public void firework(Location loc) {
					Player p = user.getPlayer();
					Firework firework = p.getWorld().spawn(loc.clone().add(0, 2.4, 0), Firework.class);
					FireworkMeta data = (FireworkMeta) firework.getFireworkMeta();
					data.addEffects(FireworkEffect.builder().withColor(Color.RED).with(Type.BALL).build());
					data.setPower(0);
					firework.setFireworkMeta(data);

					new BukkitRunnable() {
						@Override
						public void run() {
							firework.detonate();
						}
					}.runTaskLater(MobSlayerCore.getInstance(), 2L);
				}
				
				public void hit(Location loc) {
					Particle particle = new Particle(EnumParticle.SPELL_WITCH, loc, true, 0.5, 0.5, 0.5, 0.5, 25);
					particle.sendAll();
				}

				@Override
				public void run() {

					t += 1;

					double x = direction.getX() * t;
					double y = direction.getY() * t + 1.5;
					double z = direction.getZ() * t;

					loc.add(x, y, z);

					Particle particle = new Particle(EnumParticle.FIREWORKS_SPARK, loc, true, 0, 0, 0, 0, 1);
					particle.sendAll();

					for (User user : MobSlayerCore.getUserManager().getStorage()) {
						if (user.getPlayer().getLocation().distance(loc.clone().subtract(0, 1, 0)) < 1.5) {
							GameUser gameUser = (GameUser) user;
							gameUser.damage(3.5 / MobSlayerGame.getDamageReduced(user.getPlayer()));

							this.cancel();
						}
					}

					if (loc.getBlock().getType() != Material.AIR) {
						this.cancel();

						loc.getWorld().playSound(loc, Sound.DIG_WOOD, 1, 1);
						
						hit(loc);
					}

					loc.subtract(x, y, z);

					if (t > 20) {
						this.cancel();

						hit(loc);
					}
				}
			}.runTaskTimer(MobSlayerCore.getInstance(), 0, 1);
		}
	}

	@Override
	public void die(Entity entity) {
		release(entity);
	}

	@Override
	public void damage(Entity entity) {
		release(entity);
	}

	@Override
	public void clearTarget(Entity entity) {
		org.bukkit.entity.Witch witch = (org.bukkit.entity.Witch) entity;
		witch.setTarget(null);
	}

	@Override
	public Entity getTarget(Entity entity) {
		org.bukkit.entity.Witch witch = (org.bukkit.entity.Witch) entity;
		return witch.getTarget();
	}
}
