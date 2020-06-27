package com.rex2go.mobslayer_game.mob.entity;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rex2go.mobslayer_core.util.NBT;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.Item;
import com.rex2go.mobslayer_game.mob.Drop;
import com.rex2go.mobslayer_game.mob.Drop.Rarity;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.mob.Packet;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityChicken;

public class MobChicken extends GameEntity {

	public MobChicken() {
		super(GameEntityType.PIG_ZOMBIE_CHICKEN);
		
		getDrops().add(new Drop(Item.FEATHER, Rarity.FREQUENTLY));
	}
	
	@Override
	public org.bukkit.entity.Entity spawn(Location location) {
		PigZombie zombie = (PigZombie) location.getWorld().spawnEntity(location.clone().add(0, 264, 0),
				GameEntityType.PIG_ZOMBIE_CHICKEN.getEntityType());
		MobSlayerGame.getGameEntityManager().getChickenStorage().put(zombie.getUniqueId(), this);
		
		zombie.getEquipment().clear();

		zombie.setRemoveWhenFarAway(false);
		zombie.setMaxHealth(10);
		zombie.setHealth(10);
		zombie.setBaby(false);
		zombie.setCanPickupItems(false);

		zombie.setAngry(false);
		
		zombie.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5, 1));

		NBT.setSpeed(zombie, 0.30);
		NBT.setDamage(zombie, 0);
		NBT.noSound(zombie);
		
		disguise(((CraftZombie) zombie).getHandle());
		
		int yaw = ThreadLocalRandom.current().nextInt(1, 360 + 1) - 180;
		location.setYaw(yaw);
		zombie.teleport(location);
		
		return zombie;
	}
	
	public void disguise(Entity entity) {
		for(Player all : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) all).getHandle().playerConnection.sendPacket(Packet.destroyEntity(entity));
			((CraftPlayer) all).getHandle().playerConnection.sendPacket(Packet.spawnEntityLiving(((Entity) new EntityChicken(entity.getWorld())), entity.getId()));
		}
	}

	@Override
	public void attack(GameUser user, org.bukkit.entity.Entity attacker) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void die(org.bukkit.entity.Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void damage(org.bukkit.entity.Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearTarget(org.bukkit.entity.Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public org.bukkit.entity.Entity getTarget(org.bukkit.entity.Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
