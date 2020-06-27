package com.rex2go.mobslayer_game.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.ParticleEffect;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;

public class EntityExplodeListener implements Listener {

	public EntityExplodeListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		Bukkit.broadcastMessage(event.getEntityType().name());
		
		if(event.getEntity() instanceof Creeper) {
			if(MobSlayerGame.getGameEntityManager().isGameEntity(event.getEntity()));
				GameEntity gameEntity = MobSlayerGame.getGameEntityManager().getGameEntity(event.getEntity());
				
				if(gameEntity.getGameEntityType() == GameEntityType.CREEPER) {
					com.rex2go.mobslayer_game.mob.entity.creeper.MobCreeper creeper = (com.rex2go.mobslayer_game.mob.entity.creeper.MobCreeper) gameEntity;
					
					creeper.die(event.getEntity());
					
					for (Block block : event.blockList()) {
			
						if (block.getType() == Material.WOODEN_DOOR) {
							if (block.getData() >= 8) {
								block = block.getRelative(BlockFace.DOWN);
							}
							if (block.getType() == Material.WOODEN_DOOR) {
								if (block.getData() < 4) {
									block.setData((byte) (block.getData() + 4));
									block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 0);
								}
							}
							
							block.getWorld().playSound(block.getLocation(), Sound.DIG_WOOD, 1, 1);
							
							ParticleEffect.BLOCK_CRACK.display(new ParticleEffect.BlockData(Material.WOODEN_DOOR, (byte) 0), 0.3F, 0.3F, 0.3F, 0.1F, 15, 
									block.getLocation(), new ArrayList<>(Bukkit.getOnlinePlayers()));
						}
					}
					
					event.blockList().clear();
					
					MobSlayerGame.getGameEntityManager().getStorage().remove(event.getEntity().getUniqueId());
					
					for(User user : MobSlayerCore.getUserManager().getStorage()) {
						GameUser gameUser = (GameUser) user;
						gameUser.updateScoreboard();
					}
				}
		}
	}
}
