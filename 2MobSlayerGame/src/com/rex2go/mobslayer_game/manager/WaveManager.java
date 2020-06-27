package com.rex2go.mobslayer_game.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.manager.Manager;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.LocationSerializer;
import com.rex2go.mobslayer_core.util.MySQL;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.event.WaveLoadEvent;
import com.rex2go.mobslayer_game.map.Map;
import com.rex2go.mobslayer_game.mob.BossGameEntity;
import com.rex2go.mobslayer_game.mob.GameEntity;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;
import com.rex2go.mobslayer_game.wave.BossWave;
import com.rex2go.mobslayer_game.wave.SpawnRequest;
import com.rex2go.mobslayer_game.wave.SpawnRequest.TriggerType;
import com.rex2go.mobslayer_game.wave.Wave;
import com.rex2go.mobslayer_game.wave.action.WaveAction;
import com.rex2go.mobslayer_game.wave.action.WaveActionType;

public class WaveManager extends Manager {
	
	ArrayList<Wave> waves = new ArrayList<>();
	
	BossWave bossWave;
	Wave activeWave;

	int multiplier = -1;
	
	public Wave getNextWave() {
		if (!waves.isEmpty()) {
			if (activeWave == null) {
				Wave wave = waves.get(0);
				
				for (Wave wave1 : waves) {
					if (wave.getWaveLevel() > wave1.getWaveLevel()) {
						wave = wave1;
					}
				}
				
				return wave;
			} else {
				for (Wave wave1 : waves) {
					if (activeWave.getWaveLevel()+1 == wave1.getWaveLevel()) {
						return wave1;
					}
				}
				
				for (Wave wave1 : waves) {
					if (activeWave.getWaveLevel() < wave1.getWaveLevel()) {
						return wave1;
					}
				}
			}
		}
		return null;
	}

	public boolean isBossWave(Wave wave) { // TODO testen
		if(wave instanceof BossWave) {
			return true;
		}
		return false;
	}
	
	public Wave getActiveWave() {
		return activeWave;
	}

	public void setActiveWave(Wave activeWave) {
		this.activeWave = activeWave;

		MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(new WaveLoadEvent(activeWave));
		
		for (User user : MobSlayerCore.getUserManager().getStorage()) {
			GameUser gameUser = (GameUser) user;
			gameUser.updateScoreboard();
		}
	}

	public ArrayList<Wave> getWaves() {
		return waves;
	}

	public void setWaves(ArrayList<Wave> waves) {
		this.waves = waves;
	}

	public void loadWaves(Map map) {
		ResultSet rs;

		if(!MobSlayerGame.RANDOM_SPAWN_REQUESTS) {
			rs = MySQL.query("SELECT * FROM mobslayer_waves WHERE mobslayer_world = '" + map.getWorldName() + "'");

			try {
				while (rs.next()) {
					int level = rs.getInt("level");
					int prepareTime = rs.getInt("prepare_time");
					ArrayList<SpawnRequest> spawnRequests = MobSlayerGame.RANDOM_SPAWN_REQUESTS ? getRandomSpawnRequests(level)
							: getSpawnRequestsFromJson(rs.getString("spawn_requests"));
					ArrayList<WaveAction> waveActions = getWaveActionsFromJson(rs.getString("wave_script"));

					waves.add(new Wave(prepareTime, level, spawnRequests, waveActions));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			for(int i = 0; i<10; i++) {
				int level = i+1;
				int prepareTime = 60;
				ArrayList<SpawnRequest> spawnRequests = getRandomSpawnRequests(level);
				ArrayList<WaveAction> waveActions = new ArrayList<>();

				waves.add(new Wave(prepareTime, level, spawnRequests, waveActions));
			}
		}
		
		// Boss Wave laden
		
		rs = MySQL.query("SELECT * FROM mobslayer_boss_waves WHERE mobslayer_world = '" + map.getWorldName() + "'");

		try {
			while (rs.next()) {
				int prepareTime = rs.getInt("prepare_time");
				ArrayList<SpawnRequest> spawnRequests = getSpawnRequestsFromJson(rs.getString("spawn_requests"));
				ArrayList<WaveAction> waveActions = getWaveActionsFromJson(rs.getString("wave_script"));
				BossGameEntity bossGameEntity = null;
				String bossName = rs.getString("boss");
				Location bossLocation = LocationSerializer.deserialize(rs.getString("boss_location"));
				bossLocation.setWorld(MobSlayerGame.getMapManager().getGameMap().getWorld());
				
				for(GameEntityType gameEntityType : GameEntityType.values()) {
					if(gameEntityType.getName().equals(bossName)) {
						try {
							bossGameEntity = (BossGameEntity) gameEntityType.getClazz().newInstance();
							break;
						} catch (InstantiationException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
				
				waves.add(new BossWave(prepareTime, 99, spawnRequests, bossGameEntity, bossLocation, waveActions));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setWaveMultiplier(int multiplier) {
		this.multiplier = multiplier;
	}
	
	public int getWaveMultiplier() {
		if(multiplier != -1) {
			return multiplier;
		}
		
		int i = 1;
		if(Bukkit.getOnlinePlayers().size() >= 4) {
			i = 2;
		}
		if(Bukkit.getOnlinePlayers().size() >= 8) {
			i = 3;
		} 
		if(Bukkit.getOnlinePlayers().size() >= 12) {
			i = 3;
		}
		return i;
	}

	public ArrayList<SpawnRequest> getRandomSpawnRequests(int level) {
		ArrayList<SpawnRequest> spawnRequests = new ArrayList<>();
		ArrayList<GameEntityType> spawn = new ArrayList<>();

		for(GameEntityType gameEntityType : GameEntityType.values()) {
			if(gameEntityType != GameEntityType.BLAZE && gameEntityType != GameEntityType.PIG_ZOMBIE_CHICKEN && gameEntityType != GameEntityType.MAGMA_KING
					&& gameEntityType != GameEntityType.CREEPER) {
				spawn.add(gameEntityType);
			}
		}

		int count = level * 10 + 50;
		double allowed = level * 2.5 + 2.5;
		
		for(int i = 0; i<count; i++) {
			GameEntityType type = spawn.get(new Random().nextInt(spawn.size()));

			while(type.getPoints() > allowed) {
				type = spawn.get(new Random().nextInt(spawn.size()));
			}

			try {
				spawnRequests.add(new SpawnRequest(type.getClazz().newInstance(), spawnRequests.size()/10*(25-level*2), 0, TriggerType.TIME));
			} catch (Exception e) {
				e.printStackTrace();
			};
		}
		
		return spawnRequests;
	}

	public ArrayList<SpawnRequest> getSpawnRequestsFromJson(String json) {
		ArrayList<SpawnRequest> spawnRequests = new ArrayList<>();
		
		ArrayList<HashMap<String, Object>> spawnRequestsArr = new Gson().fromJson(json,
				new TypeToken<ArrayList<HashMap<String, Object>>>() {
				}.getType());
		
		for(HashMap<String, Object> test : spawnRequestsArr) {
			int id = new Double((double) test.get("id")).intValue();
			GameEntity gameEntity = null; // Hm
			
			for(GameEntityType gameEntityType : GameEntityType.values()) {
				if(gameEntityType.getId() == id) {
					try {
						gameEntity = gameEntityType.getClazz().newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
					break;
				}
			}
			
			for(int i = 0; i<getWaveMultiplier(); i++) {	
				int rand = (Math.random() <= 0.5) ? 1 : 2;
				
				if(i == 0 || rand == 1) {
					if(test.containsKey("time")) {
						if(test.containsKey("count")) {
							spawnRequests.add(new SpawnRequest(gameEntity, new Double((double) test.get("time")).intValue(), new Double((double) test.get("count")).intValue(), TriggerType.COUNT_AFTER_TIME));
						} else {
							spawnRequests.add(new SpawnRequest(gameEntity, new Double((double) test.get("time")).intValue(), 0, TriggerType.TIME));
						}
					} else if(test.containsKey("count")) {
						spawnRequests.add(new SpawnRequest(gameEntity, 0, new Double((double) test.get("count")).intValue(), TriggerType.COUNT));
					}
				}
			}
		}
		
		return spawnRequests;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<WaveAction> getWaveActionsFromJson(String json) {
		ArrayList<WaveAction> waveActions = new ArrayList<>();
		
		ArrayList<HashMap<String, Object>> waveActionArr = new Gson().fromJson(json,
				new TypeToken<ArrayList<HashMap<String, Object>>>() {
				}.getType());
		
		
		for(HashMap<String, Object> test : waveActionArr) {
			String action = (String) test.get("action");
			ArrayList<Object> args = (ArrayList<Object>) test.get("args");
			try {
				if(test.containsKey("time")) {
					if(test.containsKey("count")) {
						waveActions.add(WaveActionType.valueOf(action).getClazz().getDeclaredConstructor(int.class, int.class, int.class, TriggerType.class, args.getClass()).newInstance(
								new Double((double) test.get("time")).intValue(), new Double((double) test.get("count")).intValue(), new Double((double) test.get("delay")).intValue(),
								TriggerType.COUNT_AFTER_TIME, args));
					} else {
						waveActions.add(WaveActionType.valueOf(action).getClazz().getDeclaredConstructor(int.class, int.class, int.class, TriggerType.class, args.getClass()).newInstance(
								new Double((double) test.get("time")).intValue(), 0, new Double((double) test.get("delay")).intValue(), TriggerType.TIME, args));
					}
				} else if(test.containsKey("count")) {
					waveActions.add(WaveActionType.valueOf(action).getClazz().getDeclaredConstructor(int.class, int.class, int.class, TriggerType.class, args.getClass()).newInstance(0, 
							new Double((double) test.get("count")).intValue(), new Double((double) test.get("delay")).intValue(), TriggerType.COUNT, args));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return waveActions;
	}
}
