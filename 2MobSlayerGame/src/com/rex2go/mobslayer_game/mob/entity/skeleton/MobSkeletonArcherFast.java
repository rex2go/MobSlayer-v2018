package com.rex2go.mobslayer_game.mob.entity.skeleton;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSkeleton;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.rex2go.mobslayer_core.MobSlayerCore;
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

public class MobSkeletonArcherFast extends GameEntity {

	int shots = 0;
	int taskId = -1;
	
	public MobSkeletonArcherFast() {
		super(GameEntityType.SKELETON_ARCHER_FAST);

		getDrops().add(new Drop(Item.ARROW, Rarity.COMMON));
		getDrops().add(new Drop(Item.ARROW, Rarity.COMMON));
		getDrops().add(new Drop(Item.ARROW, Rarity.COMMON));
		getDrops().add(new Drop(Item.ARROW, Rarity.FREQUENTLY));
		
		getDrops().add(new Drop(Item.BOW, Rarity.RARE));
		
		blood = false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Entity spawn(Location location) {
		Skeleton skeleton = (Skeleton) location.getWorld().spawnEntity(location.clone().add(0, 264, 0),
				GameEntityType.SKELETON_ARCHER_FAST.getEntityType());
		MobSlayerGame.getGameEntityManager().getStorage().put(skeleton.getUniqueId(), this);

		ItemStack itemStack;
		
		itemStack = Item.LEATHER_HELMET.assignNBTData();
		LeatherArmorMeta armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
		armorMeta.setColor(Color.TEAL);
		itemStack.setItemMeta(armorMeta);
		
		skeleton.getEquipment().setHelmet(itemStack);
		
		itemStack = Item.CHAIN_CHESTPLATE.assignNBTData();
		
		skeleton.getEquipment().setChestplate(itemStack);

		skeleton.setRemoveWhenFarAway(false);
		skeleton.setMaxHealth(25);
		skeleton.setHealth(25);
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
		stopTask();
		Skeleton skeleton = (Skeleton) attacker;
		
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobSlayerCore.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				float yaw = ((CraftEntity) attacker).getHandle().getHeadRotation();
				skeleton.getLocation().setYaw(yaw);
				((CraftEntity) attacker).getHandle().yaw = yaw;
				
				if(!attacker.isDead()) {
					if(shots < 5) {
						skeleton.launchProjectile(Arrow.class);
						skeleton.getWorld().playSound(skeleton.getLocation(), Sound.SHOOT_ARROW, 1, 1);
						shots++;
					} else {
						stopTask();
						shots = 0;
					}
				} else {
					stopTask();
				}
				
			}
		}, 4, 4);
	}
	
	public void stopTask() {
		if(taskId != -1) {
			Bukkit.getScheduler().cancelTask(taskId);
			taskId = -1;
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
		Skeleton skeleton = (Skeleton) entity;
		skeleton.setTarget(null);
	}

	@Override
	public Entity getTarget(Entity entity) {
		Skeleton skeleton = (Skeleton) entity;
		return skeleton.getTarget();
	}
}
