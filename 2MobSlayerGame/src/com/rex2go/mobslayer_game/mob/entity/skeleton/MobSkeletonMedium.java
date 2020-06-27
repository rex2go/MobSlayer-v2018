package com.rex2go.mobslayer_game.mob.entity.skeleton;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSkeleton;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPigZombie;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
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

public class MobSkeletonMedium extends GameEntity {
	
	public MobSkeletonMedium() {
		super(GameEntityType.SKELETON_MEDIUM);
		
		blood = false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Entity spawn(Location location) {
		Skeleton skeleton = (Skeleton) location.getWorld().spawnEntity(location.clone().add(0, 264, 0), GameEntityType.SKELETON_MEDIUM.getEntityType());
		MobSlayerGame.getGameEntityManager().getStorage().put(skeleton.getUniqueId(), this);
		
		skeleton.getEquipment().clear();
		
		ItemStack itemStack;
		
		itemStack = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
		armorMeta.setColor(Color.BLACK);
		itemStack.setItemMeta(armorMeta);
		skeleton.getEquipment().setHelmet(itemStack);
		
		itemStack = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		skeleton.getEquipment().setChestplate(itemStack);
		
		skeleton.setRemoveWhenFarAway(false);
		skeleton.setHealth(18);
		skeleton.setMaxHealth(18);
		skeleton.setCanPickupItems(false);
		
		skeleton.getEquipment().setItemInHand(new ItemStack(Material.GOLD_SWORD));
		
		CraftSkeleton craftSkeleton = ((CraftSkeleton) skeleton);
		EntitySkeleton entitySkeleton = craftSkeleton.getHandle();
		
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
