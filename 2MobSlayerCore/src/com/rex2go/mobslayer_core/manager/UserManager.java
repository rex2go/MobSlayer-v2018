package com.rex2go.mobslayer_core.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.Settings;
import com.rex2go.mobslayer_core.user.Statistics;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Language;
import com.rex2go.mobslayer_core.util.MySQL;

public class UserManager extends Manager {

	private ArrayList<User> userStorage = new ArrayList<>();

	public ArrayList<User> getStorage() {
		return userStorage;
	}

	public void setStorage(ArrayList<User> userStorage) {
		this.userStorage = userStorage;
	}

	public void addUser(User user) {
		if (getUserByName(user.getPlayer().getName()) != null) {
			removeUser(user);
		}
		userStorage.add(user);
	}

	public void removeUser(User user) {
		userStorage.remove(user);
	}

	public User getUserByName(String name) {
		for (User user : userStorage) {
			if (user.getPlayer().getName().equalsIgnoreCase(name)) {
				return user;
			}
		}
		return null;
	}

	public User getUserByUUID(UUID uuid) {
		for (User user : userStorage) {
			if (user.getPlayer().getUniqueId().equals(uuid)) {
				return user;
			}
		}
		return null;
	}

	public boolean containsUser(String name) {
		if (getUserByName(name) != null) {
			return true;
		}
		return false;
	}

	public User loadUser(Player player) {
		if (containsUser(player.getName())) {
			return getUserByName(player.getName());
		}

		ResultSet rs = MySQL.query("SELECT * FROM mobslayer_users WHERE uuid='" + player.getUniqueId() + "'");
		try {
			if (!rs.next()) {

				// Existiert nicht in der Datenbank
				
				MySQL.update("INSERT INTO mobslayer_users (username, uuid, points, coins, rank, firstjoin) VALUES ('"
						+ player.getName() + "', '" + player.getUniqueId() + "', '0', '0', '0', NOW())");
				
				ArrayList<Color> armorColors = new ArrayList<>();
				
				armorColors.add(null);
				armorColors.add(null);
				armorColors.add(null);
				armorColors.add(null);
				
				MySQL.update("INSERT INTO mobslayer_settings (uuid, language, enable_auto_join, armor_colors, block_bad_words, show_blood, enable_scoreboard, att_notification, "
						+ "simple_crafting, auto_respawn) VALUES ('" + player.getUniqueId() + "', '" + Language.GERMAN.getCode() + "', true, '" + new Gson().toJson(armorColors) 
						+ "', true, true, true, true, false, false)");
				
				MySQL.update("INSERT INTO mobslayer_statistics (uuid, games, kills, deaths, wins, play_time, best_plays) VALUES ('" + player.getUniqueId() + "', 0, 0, 0, 0, 0, 0)");
				
				User user = new User(player, 0, 0, Rank.USER, new Settings(Language.GERMAN, armorColors, true, true, true, true, true, false, false, false), new Statistics(0, 0, 0, 0, 0, 0));
				addUser(user);
				return user;
			} else {

				// Existiert in der Datenbank

				int points = rs.getInt("points");
				int coins = rs.getInt("coins");
				Rank rank = MobSlayerCore.getRankManager().getRankById(rs.getInt("rank"));
				Settings settings = null;
				Statistics statistics = null;
				
				ResultSet rs1;
				
				rs1 = MySQL.query("SELECT * FROM mobslayer_settings WHERE uuid='" + player.getUniqueId() + "'");
				if (rs1.next()) {

					// Settings

					Language language = MobSlayerCore.getLanguageManager().getLanguageByCode(rs1.getString("language"));
					
					ArrayList<Color> armorColors = new Gson().fromJson(rs1.getString("armor_colors"), new TypeToken<ArrayList<Color>>() {}.getType());
					settings = new Settings(language, armorColors, rs1.getBoolean("block_bad_words"), rs1.getBoolean("enable_auto_join"), 
							rs1.getBoolean("show_blood"), rs1.getBoolean("enable_scoreboard"), rs1.getBoolean("att_notification"), rs1.getBoolean("simple_crafting"),
							rs1.getBoolean("auto_respawn"), rs1.getBoolean("show_stars"));
				} else {

					// Keine Settings
					
					ArrayList<Color> armorColors = new ArrayList<>();
					
					armorColors.add(null);
					armorColors.add(null);
					armorColors.add(null);
					armorColors.add(null);

					MySQL.update("INSERT INTO mobslayer_settings (uuid, language, enable_auto_join, armor_colors, block_bad_words, show_blood, enable_scoreboard, att_notification, simple_crafting, "
							+ "auto_respawn, show_stars) VALUES ('" + player.getUniqueId() + "', '" + Language.GERMAN.getCode() + "', true, '" + new Gson().toJson(armorColors) + "', true, true, true, "
									+ "true, false, false, false)");
					settings = new Settings(Language.GERMAN, armorColors, true, true, true, true, true, false, false, false);
				}
				
				rs1 = MySQL.query("SELECT * FROM mobslayer_statistics WHERE uuid='" + player.getUniqueId() + "'");
				if (rs1.next()) {

					// Statistics
					
					statistics = new Statistics(rs1.getInt("games"), rs1.getInt("kills"), rs1.getInt("deaths"), rs1.getInt("play_time"), rs1.getInt("wins"), rs1.getInt("best_plays"));
				} else {

					// Keine Statistics
					
					MySQL.update("INSERT INTO mobslayer_statistics (uuid, games, kills, deaths, wins, play_time, best_plays) VALUES ('" + player.getUniqueId() + "', 0, 0, 0, 0, 0, 0)");
					
					statistics = new Statistics(0, 0, 0, 0, 0, 0);
				}
				
				User user = new User(player, points, coins, rank, settings, statistics);
				addUser(user);

				if (!rs.getString("username").equals(player.getName())) {

					// Neuer Name

					// TODO Nachricht

					MySQL.update("UPDATE mobslayer_users SET username='" + player.getName() + "' WHERE uuid='"
							+ player.getUniqueId() + "'");
				}
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Deprecated
	public User loadUser(String name) {
		if (containsUser(name)) {
			return getUserByName(name);
		}
		
		ResultSet rs = MySQL.query("SELECT * FROM mobslayer_users WHERE username='" + name + "'");
		try {
			if (rs.next()) {
				// Existiert in der Datenbank

				int points = rs.getInt("points");
				int coins = rs.getInt("coins");
				Rank rank = MobSlayerCore.getRankManager().getRankById(rs.getInt("rank"));
				Settings settings = null;
				Statistics statistics = null;
				String uuid = rs.getString("uuid");
				
				ResultSet rs1;
				
				rs1 = MySQL.query("SELECT * FROM mobslayer_settings WHERE uuid='" + uuid + "'");
				if (rs1.next()) {

					// Settings

					Language language = MobSlayerCore.getLanguageManager().getLanguageByCode(rs1.getString("language"));
					
					ArrayList<Color> armorColors = new Gson().fromJson(rs1.getString("armor_colors"), new TypeToken<ArrayList<Color>>() {}.getType());
					settings = new Settings(language, armorColors, rs1.getBoolean("block_bad_words"), rs1.getBoolean("enable_auto_join"), 
							rs1.getBoolean("show_blood"), rs1.getBoolean("enable_scoreboard"), rs1.getBoolean("att_notification"), rs1.getBoolean("simple_crafting"),
							rs1.getBoolean("auto_respawn"), rs1.getBoolean("show_stars"));
				}
				
				rs1 = MySQL.query("SELECT * FROM mobslayer_statistics WHERE uuid='" + uuid + "'");
				if (rs1.next()) {

					// Statistics
					
					statistics = new Statistics(rs1.getInt("games"), rs1.getInt("kills"), rs1.getInt("deaths"), rs1.getInt("play_time"), rs1.getInt("wins"), rs1.getInt("best_plays"));
				}
				
				User user = new User(null, points, coins, rank, settings, statistics);
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void saveUser(User user) {
		MySQL.update("UPDATE mobslayer_users SET points='" + user.getPoints() + "', coins='" + user.getCoins() + "', rank='" + user.getRank().getRankId()
				+ "' WHERE uuid='" + user.getPlayer().getUniqueId() + "'");
		
		MySQL.update("UPDATE mobslayer_settings SET language = '" + user.getLanguage().getCode() 
				+ "', armor_colors = '" + new Gson().toJson(user.getSettings().getArmorColors()) 
				+ "', enable_auto_join = " + user.getSettings().hasAutoJoinEnabled()
				+ ", block_bad_words = " + user.getSettings().blockBadWords()
				+ ", show_blood = " + user.getSettings().showBlood() 
				+ ", enable_scoreboard = " + user.getSettings().hasScoreboardEnabled()
				+ ", att_notification = " + user.getSettings().attNotification()
				+ ", simple_crafting = " + user.getSettings().useSimpleCrafting()
				+ ", auto_respawn = " + user.getSettings().autoRespawn()
				+ ", show_stars = " + user.getSettings().showStars()
				+ " WHERE uuid='" + user.getPlayer().getUniqueId() + "'");

		MySQL.update("UPDATE mobslayer_statistics SET games='" + user.getStatistics().getGames() + "', kills='" 
				+ user.getStatistics().getKills() + "', deaths='" + user.getStatistics().getDeaths() + "', wins='" 
				+ user.getStatistics().getWins()+ "', play_time='" + user.getStatistics().getPlayTime() + "', best_plays='" + user.getStatistics().getBestPlays() + "' WHERE uuid='" 
				+ user.getPlayer().getUniqueId() +"'");
	}

	public void saveAll() {
		for (Player all : Bukkit.getOnlinePlayers()) {
			User user = MobSlayerCore.getUserManager().getUserByUUID(all.getUniqueId());
			if (user != null) {
				saveUser(user);
			}
		}
	}

	public void loadAll() {
		for (Player all : Bukkit.getOnlinePlayers()) {
			loadUser(all);
		}
	}
}
