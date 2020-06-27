package com.rex2go.mobslayer_game.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.MobSlayerCore.Status;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.task.Task;
import com.rex2go.mobslayer_game.user.GameUser;

public class JoinListener implements Listener {

	public JoinListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		User user = MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());
		
		// Game User anlegen
		GameUser gameUser = new GameUser(user);
		MobSlayerCore.getUserManager().getStorage().remove(user);
		MobSlayerCore.getUserManager().getStorage().add(gameUser);
		
		gameUser.loadGameData();
		
		player.setWalkSpeed((float) 0.2);
		
		if(MobSlayerGame.getGameManager().getGameState() != GameState.INGAME) {
			player.teleport(new Location(Bukkit.getWorld("world"), 498.5, 14, -226.5, 90, 0));
			if (user != null) {
				user.getPlayer().setGameMode(GameMode.ADVENTURE);
				
				// Join Nachricht
				for (User user1 : MobSlayerCore.getUserManager().getStorage()) {
					user1.getPlayer().sendMessage(MobSlayerCore.PREFIX + " " + Translation.getTranslation("game.join_message", user1.getLanguage(), 
							user.getRank().getRankColor() + player.getName()));
				}
				
				// Lobby Items laden
				gameUser.loadLobbyItems();
				
				gameUser.getPlayer().setMaxHealth(20);
				gameUser.getPlayer().setHealth(20);
				
				// Update Scoreboard
				gameUser.updateScoreboard();
				
				// Countdown starten
				if(Bukkit.getOnlinePlayers().size() == MobSlayerGame.PLAYER_COUNT_REQUIRED) {
					if(MobSlayerGame.getGameManager().getGameState() == GameState.WAITING) {
						MobSlayerGame.getGameManager().setGameState(GameState.STARTING);
						Task.startLobbyCountdownTask();
						MobSlayerCore.broadcast("game.enough_players_online", Status.INFO);
						for (User user1 : MobSlayerCore.getUserManager().getStorage()) {
							user1.getPlayer().playSound(user1.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
						}
					}
				}
			} else {
				player.kickPlayer("");
			}
		} else {
			GameUser gameUserOld = null;
			
			for(GameUser gameUser2 : MobSlayerGame.getGameManager().offline) {
				if(gameUser2.getPlayer().getUniqueId().equals(gameUser.getPlayer().getUniqueId())) {
					gameUserOld = gameUser2;
					break;
				}
			}
			
			if(gameUserOld == null) {
				MobSlayerGame.getGameManager().getSpectators().add(gameUser);
				gameUser.setSpectating(true);
			} else {
				MobSlayerCore.getUserManager().removeUser(user);
				MobSlayerCore.getUserManager().addUser(gameUserOld);
				
				MobSlayerGame.getGameManager().offline.remove(gameUserOld);
			}
			
			gameUser.loadSpectatorItems();
			
			player.teleport(MobSlayerGame.getSectionManager().getActiveSections().get(MobSlayerGame.getSectionManager().getActiveSections().size()-1).getDeadSpawn());
			
			// Update Scoreboard
			gameUser.updateScoreboard();
			gameUser.updateHealthScoreboard();
		}
		
		event.setJoinMessage(null);
	}
}
