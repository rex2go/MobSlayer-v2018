package com.rex2go.mobslayer_game.listener;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.user.GameUser;

public class EntityDamageListener implements Listener {

	public EntityDamageListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if(MobSlayerGame.getGameManager().getGameState() != GameState.INGAME) {
			if(event.getEntity() instanceof Player) {
				event.setCancelled(true);
				return;
			}
		} else {
			if(event.getEntity() instanceof Player) {
				GameUser gameUser = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(event.getEntity().getUniqueId());
				
				if(gameUser.getNoDamageSeconds() > 0) {
					event.setCancelled(true);
					return;
				}
				
				if(gameUser.isDead()) {
					event.setCancelled(true);
					return;
				}
				
				if(event.getCause() == DamageCause.FIRE_TICK) {
					event.setCancelled(true);
				}
				
				if(event.getCause() == DamageCause.LAVA || event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK) {
					if(gameUser.getFireProtection() >= 4) {
						event.setCancelled(true);
					}
					
					if(gameUser.getPlayer().getLocation().getBlock().getType() == Material.LAVA || gameUser.getPlayer().getLocation().getBlock().getType() == Material.STATIONARY_LAVA) {
						gameUser.damage(100);
					}
				}
			}
			if(MobSlayerGame.getGameEntityManager().isGameEntity(event.getEntity())) {
				GameEntity gameEntity = MobSlayerGame.getGameEntityManager().getGameEntity(event.getEntity());
				gameEntity.damage(event.getEntity());
			}
		}
		
		if(MobSlayerGame.getGameEntityManager().isChickenEntity(event.getEntity())) {
			if(event.getEntity() instanceof Player) {
				GameUser gameUser = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(event.getEntity().getUniqueId());
				
				if(gameUser.isDead()) {
					event.setCancelled(true);
					return;
				}
			}
			
			event.getEntity().getLocation().getWorld().playSound(event.getEntity().getLocation(), Sound.CHICKEN_HURT, 1, 1);
			event.getEntity().getLocation().getWorld().playSound(event.getEntity().getLocation(), Sound.HURT_FLESH, 1, 1);
		}
	}
}
