package com.rex2go.mobslayer_game.mob.entity.cave_spider;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCaveSpider;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
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

import net.minecraft.server.v1_8_R3.EntityCaveSpider;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLeapAtTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class MobCaveSpider extends GameEntity {
	
	public MobCaveSpider() {
		super(GameEntityType.CAVE_SPIDER);
		
		getDrops().add(new Drop(Item.STRING, Rarity.FREQUENTLY));
		getDrops().add(new Drop(Item.SPIDER_EYE, Rarity.COMMON));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Entity spawn(Location location) {
		org.bukkit.entity.CaveSpider caveSpider = (org.bukkit.entity.CaveSpider) location.getWorld()
				.spawnEntity(location.clone().add(0, 264, 0), GameEntityType.CAVE_SPIDER.getEntityType());
		MobSlayerGame.getGameEntityManager().getStorage().put(caveSpider.getUniqueId(), this);

		caveSpider.getEquipment().clear();
		caveSpider.setRemoveWhenFarAway(false);
		caveSpider.setMaxHealth(36);
		caveSpider.setHealth(36);
		caveSpider.setCanPickupItems(false);

		NBT.setSpeed(caveSpider, 0.5);

		CraftCaveSpider craftSpider = ((CraftCaveSpider) caveSpider);
		EntityCaveSpider entitySpider = craftSpider.getHandle();

		entitySpider.goalSelector = new PathfinderGoalSelector(((CraftWorld) location.getWorld()).getHandle() != null
				&& ((CraftWorld) location.getWorld()).getHandle().methodProfiler != null
						? ((CraftWorld) location.getWorld()).getHandle().methodProfiler : null);
		
		entitySpider.targetSelector = new PathfinderGoalSelector(((CraftWorld) location.getWorld()).getHandle() != null
				&& ((CraftWorld) location.getWorld()).getHandle().methodProfiler != null
						? ((CraftWorld) location.getWorld()).getHandle().methodProfiler : null);

		entitySpider.goalSelector.a(1, new PathfinderGoalFloat(entitySpider));
		entitySpider.goalSelector.a(3, new PathfinderGoalLeapAtTarget(entitySpider, 0.4F));
		entitySpider.goalSelector.a(4, new PathfinderGoalMeleeAttack(entitySpider, EntityHuman.class, 1.0D, false));
		entitySpider.goalSelector.a(6, new PathfinderGoalRandomLookaround(entitySpider));
		
		entitySpider.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(entitySpider, EntityHuman.class, true));
		entitySpider.targetSelector.a(2, new PathfinderGoalHurtByTarget(entitySpider, false, new Class[0]));
		
		caveSpider.teleport(location);
		
		return caveSpider;
	}

	public void attackShoot(org.bukkit.entity.CaveSpider spider) {
		if (spider.getTarget() != null) {
			Vector unitVector = spider.getLocation().toVector()
					.subtract(spider.getTarget().getLocation().toVector()).normalize();
			unitVector.add(new Vector(0, 0.2, 0));
			spider.setVelocity(unitVector.multiply(-1));
			
			float yaw = ((CraftEntity) spider).getHandle().getHeadRotation();
			spider.getLocation().setYaw(yaw);
			((CraftEntity) spider).getHandle().yaw = yaw;

			new BukkitRunnable() {
				double t = 0;
				Location loc = spider.getLocation();
				Vector direction = loc.getDirection().normalize();

				@Override
				public void run() {

					t += 2;
						
					double x = direction.getX() * t;
					double y = direction.getY() * t;
					double z = direction.getZ() * t;

					loc.add(x, y, z);

					Particle particle = new Particle(EnumParticle.FIREWORKS_SPARK, loc, true, 0, 0, 0, 0, 1);
					particle.sendAll();

					for (User user : MobSlayerCore.getUserManager().getStorage()) {
						if (user.getPlayer().getLocation().distance(loc.clone().subtract(0, 1, 0)) < 1.5) {
							GameUser gameUser = (GameUser) user;
							gameUser.damage(3 / MobSlayerGame.getDamageReduced(user.getPlayer()));

							this.cancel();
						}
					}

					if (loc.getBlock().getType() != Material.AIR) {
						this.cancel();
					}

					loc.subtract(x, y, z);

					if (t > 15) {
						this.cancel();
					}
				}
			}.runTaskTimer(MobSlayerCore.getInstance(), 0, 1);
		}
	}

	@Override
	public void attack(GameUser user, Entity attacker) {
		int random = ThreadLocalRandom.current().nextInt(1, 3 + 1);
		
		if(random == 3) {
			user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 50, 2));
		}
	}

	@Override
	public void die(Entity entity) {
	}

	@Override
	public void damage(Entity entity) {
	}

	@Override
	public void clearTarget(Entity entity) {
		org.bukkit.entity.Spider spider = (org.bukkit.entity.Spider) entity;
		spider.setTarget(null);
	}

	@Override
	public Entity getTarget(Entity entity) {
		org.bukkit.entity.Spider spider = (org.bukkit.entity.Spider) entity;
		return spider.getTarget();
	}
}
