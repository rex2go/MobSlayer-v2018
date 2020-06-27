package com.rex2go.mobslayer_bungee.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import com.rex2go.mobslayer_bungee.MobSlayerBungee;
import com.rex2go.mobslayer_bungee.user.Rank;
import com.rex2go.mobslayer_bungee.user.User;
import com.rex2go.mobslayer_bungee.util.Language;
import com.rex2go.mobslayer_bungee.util.MySQL;

public class UserManager {

	private ArrayList<User> userStorage = new ArrayList<>();

	public ArrayList<User> getStorage() {
		return userStorage;
	}

	public void setStorage(ArrayList<User> userStorage) {
		this.userStorage = userStorage;
	}

	public void addUser(User user) {
		if (getUserByName(user.getName()) != null) {
			removeUser(user);
		}
		userStorage.add(user);
	}

	public void removeUser(User user) {
		userStorage.remove(user);
	}

	public User getUserByName(String name) {
		for (User user : userStorage) {
			if (user.getName().equalsIgnoreCase(name)) {
				return user;
			}
		}
		return null;
	}

	public User getUserByUUID(UUID uuid) {
		for (User user : userStorage) {
			if (user.getUUID().equals(uuid)) {
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
	
	public boolean containsUser(UUID uuid) {
		if (getUserByUUID(uuid) != null) {
			return true;
		}
		return false;
	}

	public User loadUser(UUID uuid) {
		if (containsUser(uuid)) {
			return getUserByUUID(uuid);
		}
		
		ResultSet rs = MySQL.query("SELECT * FROM mobslayer_users WHERE uuid='" + uuid + "'");
		try {
			if (rs.next()) {
				User user = new User(uuid, "test", true, MobSlayerBungee.getRankManager().getRankById(rs.getInt("rank")), Language.GERMAN); // TODO
				addUser(user);
				return user;
			} else {
				User user = new User(uuid, "test", true, Rank.USER, Language.GERMAN); // TODO
				addUser(user);
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
