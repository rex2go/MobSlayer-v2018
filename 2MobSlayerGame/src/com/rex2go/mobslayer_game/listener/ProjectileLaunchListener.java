package com.rex2go.mobslayer_game.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;

public class ProjectileLaunchListener implements Listener {

	public ProjectileLaunchListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if (event.getEntity() instanceof ThrownPotion) {
			ThrownPotion potion = (ThrownPotion) event.getEntity();
			if (potion.getShooter() instanceof Witch) {
				if(MobSlayerGame.getGameEntityManager().isGameEntity((Entity) potion.getShooter())) {
					GameEntity gameEntity = MobSlayerGame.getGameEntityManager().getGameEntity((Entity) potion.getShooter());
					if(gameEntity.getGameEntityType() == GameEntityType.WITCH) {
						event.setCancelled(true);
						if(((Witch)potion.getShooter()).getTarget() != null) {
							if(((Witch)potion.getShooter()).getTarget() instanceof Player) {
								gameEntity.attack((GameUser) MobSlayerCore.getUserManager().getUserByUUID(((Witch)potion.getShooter()).getTarget().getUniqueId()), 
										(Entity) potion.getShooter());
								return;
							}
						}
					}
				}
			}
		}
		
		if(event.getEntity() != null) {
			if(event.getEntity().getShooter() != null) {
				if(event.getEntity().getShooter() instanceof Entity) {
					if(!(event.getEntity().getShooter() instanceof Player)) {
						if(MobSlayerGame.getGameEntityManager().isGameEntity((Entity) event.getEntity().getShooter())) {
							GameEntity gameEntity = MobSlayerGame.getGameEntityManager().getGameEntity((Entity) event.getEntity().getShooter());
							
							gameEntity.attack(null, (Entity) event.getEntity().getShooter());
						}
					}
				}
			}
		}
	}
}
