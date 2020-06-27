package com.rex2go.mobslayer_game.mob.entity.boss;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.util.NBT;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.mob.BossGameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;
import com.rex2go.mobslayer_game.util.BarUtil;

public class MagmaKing extends BossGameEntity {
	
	int taskId = -1;
	
	public MagmaKing() {
		super(GameEntityType.MAGMA_KING, "§6", null);
	}

	@Override
	public Entity spawn(Location location) {
		MagmaCube magmaCube = (MagmaCube) location.getWorld().spawnEntity(location.clone().add(0, 264, 0), GameEntityType.MAGMA_KING.getEntityType());
		MobSlayerGame.getGameEntityManager().getStorage().put(magmaCube.getUniqueId(), this);
		
		magmaCube.setRemoveWhenFarAway(false);
		magmaCube.setMaxHealth(100);
		magmaCube.setHealth(100);
		magmaCube.setSize(8);
		magmaCube.setCanPickupItems(false);
		magmaCube.getEquipment().clear();
		
		setupBossBar(magmaCube);
		
		NBT.setSpeed(magmaCube, 0);
		
		magmaCube.teleport(location);
		
		startBossTask(magmaCube);
		
		return magmaCube;
	}
	
	
	public void startBossTask(Entity entity) {
		MagmaCube magmaCube = (MagmaCube) entity;
		
		if(taskId != -1) {
			Bukkit.getScheduler().cancelTask(taskId);
		}
		
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MobSlayerCore.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				updateBossBar(magmaCube);
				
			}
		}, 20, 20);
	}

	@Override
	public void setupBossBar(Entity entity) {
		MagmaCube magmaCube = (MagmaCube) entity;
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			BarUtil.setBar(all, color + "§l" + GameEntityType.MAGMA_KING.getName(), (float) (100 / magmaCube.getMaxHealth() * magmaCube.getHealth()));
		}
	}
	
	@Override
	public void updateBossBar(Entity entity) {
		MagmaCube magmaCube = (MagmaCube) entity;
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			BarUtil.updateHealth(all, (float) (100f / magmaCube.getMaxHealth() * magmaCube.getHealth()));
			BarUtil.teleportBar(all);
		}
	}
	
	@Override
	public void attack(GameUser user, Entity attacker) {
	}

	@Override
	public void die(Entity entity) {
		for(Player all : Bukkit.getOnlinePlayers()) {
			BarUtil.removeBar(all);
		}
		if(taskId != -1) {
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
