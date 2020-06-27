package com.rex2go.mobslayer_game.mob.entity.spider;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSpider;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.Item;
import com.rex2go.mobslayer_game.mob.Drop;
import com.rex2go.mobslayer_game.mob.Drop.Rarity;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntitySpider;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalLeapAtTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class MobSpider extends GameEntity {

	public MobSpider() {
		super(GameEntityType.SPIDER);

		getDrops().add(new Drop(Item.STRING, Rarity.SPECIAL));
		getDrops().add(new Drop(Item.SPIDER_EYE, Rarity.SPECIAL));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Entity spawn(Location location) {
		org.bukkit.entity.Spider spider = (org.bukkit.entity.Spider) location.getWorld()
				.spawnEntity(location.clone().add(0, 264, 0), GameEntityType.SPIDER.getEntityType());
		MobSlayerGame.getGameEntityManager().getStorage().put(spider.getUniqueId(), this);

		spider.getEquipment().clear();
		spider.setRemoveWhenFarAway(false);
		spider.setCanPickupItems(false);

		CraftSpider craftSpider = ((CraftSpider) spider);
		EntitySpider entitySpider = craftSpider.getHandle();

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
		
		spider.teleport(location.clone().add(0, 1, 0));
		
		return spider;
	}

	@Override
	public void attack(GameUser user, Entity attacker) {
		int random = ThreadLocalRandom.current().nextInt(1, 6 + 1);

		if (random == 3) {
			user.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 5 * 20, 1));
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
