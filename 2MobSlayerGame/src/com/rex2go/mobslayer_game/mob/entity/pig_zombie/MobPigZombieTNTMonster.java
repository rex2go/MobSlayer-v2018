package com.rex2go.mobslayer_game.mob.entity.pig_zombie;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPigZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.rex2go.mobslayer_core.util.NBT;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;

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

public class MobPigZombieTNTMonster extends GameEntity {

	public MobPigZombieTNTMonster() {
		super(GameEntityType.PIG_ZOMBIE_TNT_MONSTER);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Entity spawn(Location location) {
		PigZombie zombie = (PigZombie) location.getWorld().spawnEntity(location.clone().add(0, 264, 0),
				GameEntityType.PIG_ZOMBIE_TNT_MONSTER.getEntityType());
		MobSlayerGame.getGameEntityManager().getStorage().put(zombie.getUniqueId(), this);

		zombie.getEquipment().clear();

		ItemStack itemStack;
		LeatherArmorMeta armorMeta;
		
		zombie.getEquipment().setItemInHand(new ItemStack(Material.TNT));

		itemStack = new ItemStack(Material.LEATHER_CHESTPLATE);
		armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
		armorMeta.setColor(Color.RED);
		itemStack.setItemMeta(armorMeta);
		zombie.getEquipment().setChestplate(itemStack);

		itemStack = new ItemStack(Material.LEATHER_LEGGINGS);
		armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
		armorMeta.setColor(Color.RED);
		itemStack.setItemMeta(armorMeta);
		zombie.getEquipment().setLeggings(itemStack);

		itemStack = new ItemStack(Material.LEATHER_BOOTS);
		armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
		armorMeta.setColor(Color.RED);
		itemStack.setItemMeta(armorMeta);
		zombie.getEquipment().setBoots(itemStack);

		zombie.setRemoveWhenFarAway(false);
		zombie.setMaxHealth(48);
		zombie.setHealth(48);
		zombie.setBaby(false);
		zombie.setCanPickupItems(false);

		zombie.setAngry(true);

		NBT.setSpeed(zombie, 0.1);
		NBT.setKnockback(zombie, 1);

		CraftPigZombie craftZombie = ((CraftPigZombie) zombie);
		EntityPigZombie entityZombie = craftZombie.getHandle();
		
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

	@Override
	public void attack(GameUser user, Entity attacker) {
		attacker.getLocation().getWorld().createExplosion(attacker.getLocation().getX(), attacker.getLocation().getY(), attacker.getLocation().getZ(), 3.0F, false, false);
	}

	@Override
	public void die(Entity entity) {
	}

	@Override
	public void damage(Entity entity) {
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
