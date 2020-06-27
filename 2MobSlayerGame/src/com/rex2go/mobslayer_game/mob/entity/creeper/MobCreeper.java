package com.rex2go.mobslayer_game.mob.entity.creeper;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreeper;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import com.rex2go.mobslayer_core.util.NBT;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.Item;
import com.rex2go.mobslayer_game.mob.Drop;
import com.rex2go.mobslayer_game.mob.Drop.Rarity;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.mob.Packet;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.EntityCreeper;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;

public class MobCreeper extends GameEntity {

	Villager villager;

	public MobCreeper() {
		super(GameEntityType.CREEPER);

		getDrops().add(new Drop(Item.GUN_POWDER, Rarity.COMMON));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Entity spawn(Location location) {
		org.bukkit.entity.Creeper creeper = (org.bukkit.entity.Creeper) location.getWorld()
				.spawnEntity(location.clone().add(0, 264, 0), GameEntityType.CREEPER.getEntityType());
		MobSlayerGame.getGameEntityManager().getStorage().put(creeper.getUniqueId(), this);

		creeper.setRemoveWhenFarAway(false);
		creeper.setHealth(20);
		creeper.setMaxHealth(20);
		creeper.setCanPickupItems(false);
		creeper.getEquipment().clear();

		NBT.setSpeed(creeper, 0.275);

		CraftCreeper craftCreeper = ((CraftCreeper) creeper);
		EntityCreeper entityCreeper = craftCreeper.getHandle();

		entityCreeper.targetSelector.a(1,
				new PathfinderGoalNearestAttackableTarget(entityCreeper, EntityVillager.class, true));

		creeper.teleport(location);
		
		return creeper;
	}

	public void fuseCheck(Entity entity) {
		org.bukkit.entity.Creeper creeper = (org.bukkit.entity.Creeper) entity;

		if (doorBlocksNearby(entity.getLocation(), 2)) {
			if (villager != null) {
				villager.remove();
			}
			
			villager = (Villager) creeper.getWorld().spawnEntity(creeper.getLocation().clone().add(0, 250, 0),
					EntityType.VILLAGER);
			
			Packet.destroyEntity(((CraftEntity)villager).getHandle());
			NBT.noAI(villager);
			NBT.noSound(villager);
			
			villager.setBaby();
			
			villager.teleport(creeper.getLocation().clone().add(0, 2, 0));
		}
	}

	private boolean doorBlocksNearby(Location center, int radius) {
		int x2 = center.getBlockX();
		int y2 = center.getBlockY();
		int z2 = center.getBlockZ();
		World world = center.getWorld();
		
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					Block b = world.getBlockAt(x + x2, y + y2, z + z2);
					if (b.getType() == Material.WOODEN_DOOR) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void attack(GameUser user, Entity attacker) {
	}

	@Override
	public void die(Entity entity) {
		if (villager != null) {
			villager.remove();
		}
	}

	@Override
	public void damage(Entity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearTarget(Entity entity) {
		org.bukkit.entity.Creeper zombie = (org.bukkit.entity.Creeper) entity;
		zombie.setTarget(null);
	}

	@Override
	public Entity getTarget(Entity entity) {
		org.bukkit.entity.Creeper zombie = (org.bukkit.entity.Creeper) entity;
		return zombie.getTarget();
	}
}
