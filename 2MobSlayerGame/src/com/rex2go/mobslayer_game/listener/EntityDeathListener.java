package com.rex2go.mobslayer_game.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.GameItem;
import com.rex2go.mobslayer_game.item.Item;
import com.rex2go.mobslayer_game.mob.Drop;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.entity.MobChicken;
import com.rex2go.mobslayer_game.user.GameUser;

public class EntityDeathListener implements Listener {

	public EntityDeathListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();

		if (MobSlayerGame.getGameEntityManager().isGameEntity(entity)) {
			GameEntity gameEntity = MobSlayerGame.getGameEntityManager().getGameEntity(entity);

			event.setDroppedExp(0);
			event.getDrops().clear();
			
			gameEntity.die(entity);
			
			for(Drop drop : gameEntity.getDrops()) {
				GameItem itemStack = drop.drop();
				
				if(itemStack != null) {
					event.getDrops().add(itemStack.assignNBTData());
				}
			}
			
			for(int i = 0; i<3; i++) {
				if(Math.random() >= 1.0 - ((gameEntity.getPoints() / 100.0) / 2)) {
					event.getDrops().add(Item.GOLD_NUGGET.assignNBTData());
				}
			}
			
			if(gameEntity.getDamager() != null) {
				gameEntity.getDamager().setInGamePoints(gameEntity.getDamager().getInGamePoints()+gameEntity.getPoints());
				gameEntity.getDamager().setPoints(gameEntity.getDamager().getPoints()+gameEntity.getPoints());
				gameEntity.getDamager().getStatistics().setKills(gameEntity.getDamager().getStatistics().getKills()+1);
				gameEntity.getDamager().setWaveKills(gameEntity.getDamager().getWaveKills()+1);
				gameEntity.getDamager().setKills(gameEntity.getDamager().getKills()+1);
			}

			MobSlayerGame.getGameEntityManager().getStorage().remove(entity.getUniqueId());

			for (User user : MobSlayerCore.getUserManager().getStorage()) {
				GameUser gameUser = (GameUser) user;

				gameUser.updateScoreboard();
			}
		} else if (MobSlayerGame.getGameEntityManager().isChickenEntity(entity)) {
			MobChicken chicken = MobSlayerGame.getGameEntityManager().getChickenEntity(entity);
			
			entity.getLocation().getWorld().playSound(entity.getLocation(), Sound.CHICKEN_HURT, 1, 1);
			
			event.setDroppedExp(0);
			event.getDrops().clear();
			
			chicken.die(entity);
			
			for(Drop drop : chicken.getDrops()) {
				GameItem itemStack = drop.drop();
				
				if(itemStack != null) {
					event.getDrops().add(itemStack.assignNBTData());
				}
			}
			
			MobSlayerGame.getGameEntityManager().getChickenStorage().remove(entity.getUniqueId());
		}
	}
}
