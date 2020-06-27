package com.rex2go.mobslayer_game.mob.entity.slime;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSlime;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.util.NBT;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.Item;
import com.rex2go.mobslayer_game.mob.Drop;
import com.rex2go.mobslayer_game.mob.Drop.Rarity;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.PathfinderGoalTargetNearestPlayer;

public class MobSlime extends GameEntity {

	int taskId = -1;
	
	public MobSlime() {
		super(GameEntityType.SLIME);
		
		getDrops().add(new Drop(Item.SLIME_BALL, Rarity.COMMON));
	}

	@Override
	public Entity spawn(Location location) {
		org.bukkit.entity.Slime slime = (org.bukkit.entity.Slime) location.getWorld().spawnEntity(location.clone().add(0, 264, 0), GameEntityType.SLIME.getEntityType());
		MobSlayerGame.getGameEntityManager().getStorage().put(slime.getUniqueId(), this);
		
		slime.setRemoveWhenFarAway(false);
		slime.setSize(2);
		slime.setMaxHealth(25);
		slime.setHealth(25);
		slime.setCanPickupItems(false);
		slime.getEquipment().clear();
		
		NBT.setSpeed(slime, 1.5);
		
		CraftSlime craftSlime = ((CraftSlime) slime);
		EntitySlime entitySlime = craftSlime.getHandle();
		
		entitySlime.targetSelector = new PathfinderGoalSelector(((CraftWorld) location.getWorld()).getHandle() != null 
				&& ((CraftWorld) location.getWorld()).getHandle().methodProfiler != null ? ((CraftWorld) location.getWorld()).getHandle().methodProfiler : null);
        
		entitySlime.targetSelector.a(1, new PathfinderGoalTargetNearestPlayer(entitySlime));
		
		slime.teleport(location);
		
		return slime;
	}

	@Override
	public void attack(GameUser user, Entity attacker) {
		int random = ThreadLocalRandom.current().nextInt(1, 3 + 1);
		Player player = user.getPlayer();
		
		if(player.getInventory().getHelmet() != null) {
			if(player.getInventory().getHelmet().getType() == Material.IRON_HELMET || player.getInventory().getHelmet().getType() == Material.DIAMOND_HELMET) {
				return;
			}
		}
		
		if(player.getPassenger() == null) {
			if(random == 3) {
				user.getPlayer().setPassenger(attacker);
				
				if(taskId != -1) {
					Bukkit.getScheduler().cancelTask(taskId);
				}
				
				taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobSlayerCore.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						if(player.getPassenger() != null) {
							user.damage(1);
						} else {
							Bukkit.getScheduler().cancelTask(taskId);
							taskId = -1;
						}
						
					}
				}, 1, 1);
			}
		}
	}

	@Override
	public void die(Entity entity) {
		if(taskId != -1) {
			entity.remove();
			Bukkit.getScheduler().cancelTask(taskId);
			taskId = -1;
		}
	}

	@Override
	public void damage(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearTarget(Entity entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Entity getTarget(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
