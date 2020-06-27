package com.rex2go.mobslayer_game.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;

import com.rex2go.mobslayer_core.manager.Manager;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.map.Map;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.mob.entity.MobChicken;

import net.minecraft.server.v1_8_R3.DamageSource;

public class GameEntityManager extends Manager {

	HashMap<UUID, GameEntity> entityStorage = new HashMap<>();
	HashMap<UUID, MobChicken> chickenStorage = new HashMap<>();

	public GameEntity getGameEntity(Entity entity) {
		return entityStorage.get(entity.getUniqueId());
	}
	
	public boolean isGameEntity(Entity entity) {
		return entityStorage.containsKey(entity.getUniqueId());
	}
	
	public GameEntityType getGameEntityTypeById(int id) {
		for(GameEntityType type : GameEntityType.values()) {
			if(type.getId() == id) {
				return type;
			}
		}
		return null;
	}
	
	public void removeAll() {
		if(!MobSlayerGame.getMapManager().getLoadedMaps().isEmpty()) {
			removeAllChicken();
			for(Map map : MobSlayerGame.getMapManager().getLoadedMaps()) {
				for(Entity entity : map.getWorld().getEntities()) {
					if(entityStorage.containsKey(entity.getUniqueId())) {
						entityStorage.remove(entity.getUniqueId());
						entity.remove();
					}
				}
			}
		}
	}
	
	public void killAll() {
		fix();
		
		if(!MobSlayerGame.getMapManager().getLoadedMaps().isEmpty()) {
			for(Map map : MobSlayerGame.getMapManager().getLoadedMaps()) {
				for(Entity entity : map.getWorld().getEntities()) {
					if(entityStorage.containsKey(entity.getUniqueId())) {
						((CraftEntity) entity).getHandle().damageEntity(DamageSource.GENERIC, 999999f);
					}
				}
			}
		}
	}
	
	public void fix() {
		ArrayList<UUID> uuids = new ArrayList<>();
		
		entityStorage.forEach((k, v) -> {
			boolean alive = false;
			
			if(!MobSlayerGame.getMapManager().getLoadedMaps().isEmpty()) {
				for(Map map : MobSlayerGame.getMapManager().getLoadedMaps()) {
					for(Entity entity : map.getWorld().getEntities()) {
						if(entityStorage.containsKey(entity.getUniqueId())) {
							if(!((CraftEntity) entity).isDead()) {
								alive = true;
							}
						}
					}
				}
			}
			
			if(!alive) {
				uuids.add(k);
			}
        });
		
		for(UUID uuid : uuids) {
			entityStorage.remove(uuid);
		}
	}
	
	public int countAliveEntities() {
		int i = 0;
		
		fix();
		
		for(Map map : MobSlayerGame.getMapManager().getLoadedMaps()) {
			for(Entity entity : map.getWorld().getEntities()) {
				if(entityStorage.containsKey(entity.getUniqueId())) {
					i++; 
				}
			}
		}
		
		return i;
	}
	
	public HashMap<UUID, GameEntity> getStorage() {
		return entityStorage;
	}

	public MobChicken getChickenEntity(Entity entity) {
		if(isChickenEntity(entity)) {
			return chickenStorage.get(entity.getUniqueId());
		}
		return null;
	}
	
	public boolean isChickenEntity(Entity entity) {
		return chickenStorage.containsKey(entity.getUniqueId());
	}
	
	public void removeAllChicken() {
		if(!MobSlayerGame.getMapManager().getLoadedMaps().isEmpty()) {
			for(Map map : MobSlayerGame.getMapManager().getLoadedMaps()) {
				for(Entity entity : map.getWorld().getEntities()) {
					if(chickenStorage.containsKey(entity.getUniqueId())) {
						chickenStorage.remove(entity.getUniqueId());
						entity.remove();
					}
				}
			}
		}
	}
	
	public HashMap<UUID, MobChicken> getChickenStorage() {
		return chickenStorage;
	}
}
