package com.rex2go.mobslayer_game.mob.entity.skeleton;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSkeleton;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;

import com.rex2go.mobslayer_core.util.NBT;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.Item;
import com.rex2go.mobslayer_game.mob.Drop;
import com.rex2go.mobslayer_game.mob.Drop.Rarity;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import net.minecraft.server.v1_8_R3.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class MobSkeletonArcher extends GameEntity {
	
	public MobSkeletonArcher() {
		super(GameEntityType.SKELETON_ARCHER);
		
		getDrops().add(new Drop(Item.ARROW, Rarity.COMMON));
		getDrops().add(new Drop(Item.ARROW, Rarity.COMMON));
		
		getDrops().add(new Drop(Item.BOW, Rarity.VERY_RARE));
		
		blood = false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Entity spawn(Location location) {
		Skeleton skeleton = (Skeleton) location.getWorld().spawnEntity(location.clone().add(0, 264, 0), GameEntityType.SKELETON_ARCHER.getEntityType());
		MobSlayerGame.getGameEntityManager().getStorage().put(skeleton.getUniqueId(), this);
		
		skeleton.setRemoveWhenFarAway(false);
		skeleton.setHealth(15);
		skeleton.setMaxHealth(15);
		skeleton.setCanPickupItems(false);
		
		NBT.setSpeed(skeleton, 0.275);
		
		CraftSkeleton craftSkeleton = ((CraftSkeleton) skeleton);
		EntitySkeleton entitySkeleton = craftSkeleton.getHandle();
		
		entitySkeleton.targetSelector = new PathfinderGoalSelector(((CraftWorld) location.getWorld()).getHandle() != null 
				&& ((CraftWorld) location.getWorld()).getHandle().methodProfiler != null ? ((CraftWorld) location.getWorld()).getHandle().methodProfiler : null);
		
		entitySkeleton.targetSelector.a(1, new PathfinderGoalHurtByTarget(entitySkeleton, false, new Class[0]));
		entitySkeleton.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(entitySkeleton, EntityHuman.class, true));
		
		skeleton.teleport(location);
		
		return skeleton;
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
		Skeleton skeleton = (Skeleton) entity;
		skeleton.setTarget(null);
	}

	@Override
	public Entity getTarget(Entity entity) {
		Skeleton skeleton = (Skeleton) entity;
		return skeleton.getTarget();
	}
}
