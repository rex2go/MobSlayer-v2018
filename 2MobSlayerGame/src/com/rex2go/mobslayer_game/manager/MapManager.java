package com.rex2go.mobslayer_game.manager;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.MobSlayerCore.Status;
import com.rex2go.mobslayer_core.manager.Manager;
import com.rex2go.mobslayer_core.util.LocationSerializer;
import com.rex2go.mobslayer_core.util.MySQL;
import com.rex2go.mobslayer_game.map.Map;
import com.rex2go.mobslayer_game.map.Section;

public class MapManager extends Manager {

	private ArrayList<Map> loadedMaps = new ArrayList<>();

	public Map loadMap(String worldName) {
		ResultSet rs = MySQL.query("SELECT * FROM mobslayer_maps WHERE world_name='" + worldName + "'");

		String mapName = null, author = null;
		ArrayList<Location> playerSpawns = new ArrayList<>();
		ArrayList<Section> sections = new ArrayList<>();

		try {
			if (!rs.next()) {
				// Map nicht in Datenbank
				MobSlayerCore.broadcast("map_manager.map_not_existing_in_database", Status.ERROR);
			} else {
				// Pfade definieren
				File srcDir = new File(MobSlayerCore.getInstance().getServer().getWorldContainer().getAbsolutePath()
						+ "/maps/" + worldName);
				File destDir = new File(MobSlayerCore.getInstance().getServer().getWorldContainer().getAbsolutePath()
						+ "/" + worldName);

				// Kopieren der Welt
				try {
					FileUtils.copyDirectory(srcDir, destDir);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Welt laden und ein paar Einstellungen setzen
				World world = WorldCreator.name(rs.getString("world_name")).createWorld();
				world.setPVP(true);
				world.setTime(16000);
				world.setDifficulty(Difficulty.EASY);
				world.setGameRuleValue("doDaylightCycle", "false");
				world.setAutoSave(false);

				mapName = rs.getString("map_name");
				author = rs.getString("author");

				// Player Spawns laden, falls vorhanden
				if (rs.getString("player_spawns") != "") {
					@SuppressWarnings({ "unchecked" })
					ArrayList<String> test = new Gson().fromJson(rs.getString("player_spawns"), ArrayList.class);
					for (String s : test) {
						playerSpawns.add(LocationSerializer.deserialize(s));
					}
				}

				// Sections laden, falls vorhanden
				if (rs.getString("sections") != "") {
					ArrayList<HashMap<String, ArrayList<String>>> sectionss = new Gson().fromJson(
							rs.getString("sections"), new TypeToken<ArrayList<HashMap<String, ArrayList<String>>>>() {
							}.getType());
					for (HashMap<String, ArrayList<String>> s : sectionss) {
						ArrayList<String> doorBlocksSerialized = s.get("doorBlocks");
						ArrayList<String> mobSpawnsSerialized = s.get("mobSpawns");
						ArrayList<String> deadSpawnSerialized = s.get("deadSpawn");

						ArrayList<Location> doorBlocks = new ArrayList<>();
						ArrayList<Location> mobSpawns = new ArrayList<>();
						Location deadSpawn = null;

						for (String ss : doorBlocksSerialized) {
							doorBlocks.add(LocationSerializer.deserialize(ss));
						}

						for (String ss : mobSpawnsSerialized) {
							mobSpawns.add(LocationSerializer.deserialize(ss));
						}

						if (!deadSpawnSerialized.isEmpty()) {
							deadSpawn = LocationSerializer.deserialize(deadSpawnSerialized.get(0));
						}

						sections.add(new Section(doorBlocks, deadSpawn, mobSpawns));
					}
				}

				// TODO boss section laden

				Map map = new Map(mapName, worldName, author, playerSpawns, sections, null);
				map.setWorld(world);
				loadedMaps.add(map);
				return map;
			}
		} catch (SQLException e) {
			Bukkit.broadcastMessage(Color.ERROR + "Error");
			e.printStackTrace();
		}
		return null;
	}

	public void unload(Map map) {
		// Welt entladen
		Bukkit.getServer().unloadWorld(map.getWorldName(), false);

		// Pfad des Ordners der Welt
		File dir = new File(MobSlayerCore.getInstance().getServer().getWorldContainer().getAbsolutePath() + "/"
				+ map.getWorldName());

		// Löschen des Ordners
		try {
			FileUtils.deleteDirectory(dir);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (loadedMaps.contains(map))
			loadedMaps.remove(map);
	}

	public void unloadAllMaps() {
		for (Player all : Bukkit.getOnlinePlayers()) {
			all.teleport(new Location(Bukkit.getWorld("world"), 498.5, 14, -226.5, 90, 0));
		}
		for (Map map : loadedMaps) {
			// Welt entladen
			Bukkit.getServer().unloadWorld(map.getWorldName(), false);

			// Pfad des Ordners der Welt
			File dir = new File(MobSlayerCore.getInstance().getServer().getWorldContainer().getAbsolutePath() + "/"
					+ map.getWorldName());

			// Löschen des Ordners
			try {
				FileUtils.deleteDirectory(dir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		loadedMaps.clear();
	}

	public void save(Map map) {
		// Pfade definieren
		File srcDir = new File(MobSlayerCore.getInstance().getServer().getWorldContainer().getAbsolutePath() + "/"
				+ map.getWorldName());
		File destDir = new File(MobSlayerCore.getInstance().getServer().getWorldContainer().getAbsolutePath() + "/maps/"
				+ map.getWorldName());

		// Kopieren der Welt
		try {
			FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// MySQL Daten speichern
		ArrayList<String> playerSpawns = new ArrayList<>();
		for (Location loc : map.getPlayerSpawns()) {
			playerSpawns.add(LocationSerializer.serialize(loc));
		}

		ArrayList<HashMap<String, ArrayList<String>>> sections = new ArrayList<>();
		for (Section sec : map.getSections()) {
			HashMap<String, ArrayList<String>> section = new HashMap<>();

			ArrayList<String> doorBlocks = new ArrayList<>();
			ArrayList<String> mobSpawns = new ArrayList<>();
			ArrayList<String> deadSpawn = new ArrayList<>();

			for (Location loc : sec.getDoorBlocks()) {
				doorBlocks.add(LocationSerializer.serialize(loc));
			}
			for (Location loc : sec.getMobSpawns()) {
				mobSpawns.add(LocationSerializer.serialize(loc));
			}
			if (sec.getDeadSpawn() != null) {
				deadSpawn.add(LocationSerializer.serialize(sec.getDeadSpawn()));
			}

			section.put("doorBlocks", doorBlocks);
			section.put("mobSpawns", mobSpawns);
			section.put("deadSpawn", deadSpawn);

			sections.add(section);
		}

		String jsonPlayerSpawns = new Gson().toJson(playerSpawns);
		String jsonSections = new Gson().toJson(sections);
		String jsonBossSection = new Gson().toJson(map.getBossSection()); // TODO

		MySQL.update("UPDATE mobslayer_maps SET map_name = '" + map.getMapName() + "', author = '" + map.getAuthor()
				+ "', player_spawns = '" + jsonPlayerSpawns + "', sections = '" + jsonSections + "', boss_section = '"
				+ jsonBossSection + "' " + "WHERE world_name = '" + map.getWorldName() + "'");
	}

	public boolean teleportPlayersToMap(Map map) {
		if (map.getPlayerSpawns().size() >= Bukkit.getOnlinePlayers().size()) {
			int i = 0;
			for (Player all : Bukkit.getOnlinePlayers()) {
				all.teleport(map.getPlayerSpawns().get(i));
				i++;
			}
			return true;
		} else {
			MobSlayerCore.broadcast("map_manager.too_few_player_spawns", Status.ERROR);
		}
		return false;
	}

	public ArrayList<Map> getLoadedMaps() {
		return loadedMaps;
	}

	public Map getGameMap() {
		if (!loadedMaps.isEmpty()) {
			return loadedMaps.get(0);
		}
		return null;
	}
}
