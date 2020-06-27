package com.rex2go.mobslayer_game.listener.custom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.BountifulAPI;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.event.GameEndEvent;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.task.Task;
import com.rex2go.mobslayer_game.user.GameUser;

public class GameEndListener implements Listener {

	public GameEndListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onGameEnd(GameEndEvent event) {
		Bukkit.broadcastMessage("ende"); // DEBUG
		
		if(event.isWin()) {
			for(User user : MobSlayerCore.getUserManager().getStorage()) {
				GameUser gameUser = (GameUser) user;
				user.setCoins(user.getCoins()+250);
				gameUser.setInGameCoins(gameUser.getInGameCoins()+250);
				user.getStatistics().setWins(user.getStatistics().getWins()+1);
			}
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for(Player all : Bukkit.getOnlinePlayers()) {
					all.teleport(new Location(Bukkit.getWorld("world"), 498.5, 14, -226.5, 90, 0));
					BountifulAPI.clearTitle(all);
				}
				
			}
		}, 2);
		
		for(User user : MobSlayerCore.getUserManager().getStorage()) {
			GameUser gameUser = (GameUser) user;
			user.reset();
			gameUser.hideScoreboard();
		}
		
		MobSlayerGame.getGameManager().setGameState(GameState.ENDING);
		
		MobSlayerGame.getGameEntityManager().removeAll();
		
		Task.stopGameTask();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				for (Player all : Bukkit.getOnlinePlayers()) {
					User user = MobSlayerCore.getUserManager().getUserByUUID(all.getUniqueId());

					if (user != null) {
						user.sendToLobby();
					}
				}
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
						
					}
				}, 5);
				
			}
		}, 20*15);
	}
}
