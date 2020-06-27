package com.rex2go.mobslayer_game.mob.entity.iron_golem;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftIronGolem;
import org.bukkit.entity.Entity;

import com.rex2go.mobslayer_core.util.NBT;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.Item;
import com.rex2go.mobslayer_game.mob.Drop;
import com.rex2go.mobslayer_game.mob.Drop.Rarity;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class MobIronGolem extends GameEntity {
	
	int charge = 0;
	int taskId = -1;
	
	public MobIronGolem() {
		super(GameEntityType.IRON_GOLEM);
		
		getDrops().add(new Drop(Item.STICK, Rarity.SPECIAL));
		getDrops().add(new Drop(Item.IRON_INGOT, Rarity.COMMON));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Entity spawn(Location location) {
		org.bukkit.entity.IronGolem golem = (org.bukkit.entity.IronGolem) location.getWorld().spawnEntity(location.clone().add(0, 264, 0), GameEntityType.IRON_GOLEM.getEntityType());
		MobSlayerGame.getGameEntityManager().getStorage().put(golem.getUniqueId(), this);
		
		golem.setRemoveWhenFarAway(false);
		golem.setMaxHealth(64);
		golem.setHealth(64);
		golem.setCanPickupItems(false);
		golem.getEquipment().clear();
		
		NBT.setSpeed(golem, 0.2);
		
		CraftIronGolem craftGolem = ((CraftIronGolem) golem);
		EntityIronGolem entityGolem = craftGolem.getHandle();
		
		entityGolem.goalSelector = new PathfinderGoalSelector(((CraftWorld) location.getWorld()).getHandle() != null 
				&& ((CraftWorld) location.getWorld()).getHandle().methodProfiler != null ? ((CraftWorld) location.getWorld()).getHandle().methodProfiler : null);
		entityGolem.targetSelector = new PathfinderGoalSelector(((CraftWorld) location.getWorld()).getHandle() != null 
				&& ((CraftWorld) location.getWorld()).getHandle().methodProfiler != null ? ((CraftWorld) location.getWorld()).getHandle().methodProfiler : null);
		
		entityGolem.goalSelector.a(0, new PathfinderGoalFloat(entityGolem));
		entityGolem.goalSelector.a(2, new PathfinderGoalMeleeAttack(entityGolem, EntityHuman.class, 1.0D, false));
		entityGolem.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(entityGolem, 1.0D));
		entityGolem.goalSelector.a(7, new PathfinderGoalRandomStroll(entityGolem, 1.0D));
		entityGolem.goalSelector.a(8, new PathfinderGoalRandomLookaround(entityGolem));
		
		entityGolem.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(entityGolem, EntityHuman.class, true));
		entityGolem.targetSelector.a(2, new PathfinderGoalHurtByTarget(entityGolem, false, new Class[0]));
		
		golem.teleport(location);
		
		return golem;
	}

	public int getCharge() {
		return charge;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	public void attackShockwave(org.bukkit.entity.IronGolem entity) {
		// TODO
	}
	
	public void stopTask() {
		if(taskId != -1) {
			Bukkit.getScheduler().cancelTask(taskId);
		}
	}
	
	@Override
	public void attack(GameUser user, Entity attacker) {
		user.getPlayer().setVelocity(user.getPlayer().getVelocity().multiply(3));
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
		org.bukkit.entity.IronGolem golem = (org.bukkit.entity.IronGolem) entity;
		golem.setTarget(null);
	}

	@Override
	public Entity getTarget(Entity entity) {
		org.bukkit.entity.IronGolem golem = (org.bukkit.entity.IronGolem) entity;
		return golem.getTarget();
	}
}
