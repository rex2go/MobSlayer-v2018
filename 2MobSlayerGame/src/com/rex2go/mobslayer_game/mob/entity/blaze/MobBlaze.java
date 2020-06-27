package com.rex2go.mobslayer_game.mob.entity.blaze;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftBlaze;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.Item;
import com.rex2go.mobslayer_game.mob.Drop;
import com.rex2go.mobslayer_game.mob.Drop.Rarity;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.EntityBlaze;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPigZombie;
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

public class MobBlaze extends GameEntity {
	
	public MobBlaze() {
		super(GameEntityType.BLAZE);
		
		getDrops().add(new Drop(Item.FIRE_ROD, Rarity.SPECIAL));
		getDrops().add(new Drop(Item.FIRE_ROD, Rarity.RARE));
		
		getDrops().add(new Drop(Item.FIRE_POWDER, Rarity.COMMON));
		getDrops().add(new Drop(Item.FIRE_POWDER, Rarity.FREQUENTLY));
		
		blood = true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Entity spawn(Location location) {
		Blaze skeleton = (Blaze) location.getWorld().spawnEntity(location.clone().add(0, 264, 0), GameEntityType.BLAZE.getEntityType());
		MobSlayerGame.getGameEntityManager().getStorage().put(skeleton.getUniqueId(), this);
		
		skeleton.setRemoveWhenFarAway(false);
		skeleton.setCanPickupItems(false);
		skeleton.getEquipment().clear();
		
		CraftBlaze craftSkeleton = ((CraftBlaze) skeleton);
		EntityBlaze entitySkeleton = craftSkeleton.getHandle();
		
		entitySkeleton.goalSelector = new PathfinderGoalSelector(((CraftWorld) location.getWorld()).getHandle() != null 
				&& ((CraftWorld) location.getWorld()).getHandle().methodProfiler != null ? ((CraftWorld) location.getWorld()).getHandle().methodProfiler : null);
		entitySkeleton.targetSelector = new PathfinderGoalSelector(((CraftWorld) location.getWorld()).getHandle() != null 
				&& ((CraftWorld) location.getWorld()).getHandle().methodProfiler != null ? ((CraftWorld) location.getWorld()).getHandle().methodProfiler : null);
		
		entitySkeleton.goalSelector.a(0, new PathfinderGoalFloat(entitySkeleton));
		entitySkeleton.goalSelector.a(2, new PathfinderGoalMeleeAttack(entitySkeleton, EntityHuman.class, 1.0D, false));
		entitySkeleton.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(entitySkeleton, 1.0D));
		entitySkeleton.goalSelector.a(7, new PathfinderGoalRandomStroll(entitySkeleton, 1.0D));
		entitySkeleton.goalSelector.a(8, new PathfinderGoalLookAtPlayer(entitySkeleton, EntityHuman.class, 8.0F));
		entitySkeleton.goalSelector.a(8, new PathfinderGoalRandomLookaround(entitySkeleton));
		
		entitySkeleton.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(entitySkeleton, 1.0D, false));
		
		entitySkeleton.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(entitySkeleton, EntityHuman.class, true));
		entitySkeleton.targetSelector.a(2, new PathfinderGoalHurtByTarget(entitySkeleton, false, new Class[0]));
		
		entitySkeleton.targetSelector.a(1, new PathfinderGoalHurtByTarget(entitySkeleton, true, new Class[] { EntityPigZombie.class}));
		entitySkeleton.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(entitySkeleton, EntityHuman.class, true));
		
		skeleton.teleport(location);
		
		return skeleton;
	}
	
	@Override
	public void attack(GameUser user, Entity attacker) {
	}
	
	@SuppressWarnings("deprecation")
	public void attackFlame(Entity entity1) {
		Random random = new Random();
	    Location from = entity1.getLocation();
	    from.setPitch(from.getPitch() - 10.0F);
	    Vector baseDirection = from.getDirection();
	    from.add(baseDirection.multiply(1.5D));
	    int flames = (int)Math.floor(random.nextFloat() + 3 / 5.0D);
	    for (int i = 0; i < flames; i++) {
	      MaterialData flameType = new MaterialData(Material.FIRE);
	      FallingBlock entity = entity1.getWorld().spawnFallingBlock(from, flameType.getItemType(), flameType.getData());
	      entity.setFireTicks(1200);
	      double dx = baseDirection.getX() * 0.6D + random.nextGaussian() * 0.2D;
	      double dy = baseDirection.getY() * 0.6D + random.nextDouble() * 0.4D;
	      double dz = baseDirection.getZ() * 0.6D + random.nextGaussian() * 0.2D;
	      entity.setVelocity(new Vector(dx, dy, dz));
	      entity.setDropItem(false);
	    }
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
		Blaze skeleton = (Blaze) entity;
		skeleton.setTarget(null);
	}

	@Override
	public Entity getTarget(Entity entity) {
		Blaze skeleton = (Blaze) entity;
		return skeleton.getTarget();
	}
}
