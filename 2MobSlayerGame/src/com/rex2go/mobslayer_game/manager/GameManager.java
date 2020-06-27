package com.rex2go.mobslayer_game.manager;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Entity;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.manager.Manager;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.user.GameUser;

public class GameManager extends Manager {

	public ArrayList<GameUser> alive = new ArrayList<>();
	public ArrayList<GameUser> offline = new ArrayList<>();
	public ArrayList<Entity> grenades = new ArrayList<>();
	
	ArrayList<GameUser> spectatorStorage = new ArrayList<>();
	
	private GameState gameState;
	
	public GameManager() {
		setGameState(GameState.WAITING);
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
		((CraftServer)Bukkit.getServer()).getServer().setMotd(gameState.name());
	}

	public GameState getGameState() {
		return this.gameState;
	}
	
	public enum GameState {
		WAITING, STARTING, INGAME, ENDING
	}
	
	public boolean isSpectator(GameUser gameUser) {
		return spectatorStorage.contains(gameUser);
	}
	
	public ArrayList<GameUser> getSpectators() {
		return spectatorStorage;
	}
	
	public int getPlayers() {
		int players = MobSlayerGame.getGameManager().alive.size();
		
		for(User user1 : MobSlayerCore.getUserManager().getStorage()) {
			GameUser gameUser1 = (GameUser) user1;
			if(gameUser1.isDead()) {
				players++;
			}
		}
		
		return players;
	}
	
	public GameUser getBestPlayer() {
		GameUser gameUser = null;
		
		for(User user : MobSlayerCore.getUserManager().getStorage()) {
			GameUser g = (GameUser) user;
			if(gameUser == null) {
				gameUser = g;
			} else if(g.getWaveKills() > gameUser.getWaveKills()) {
				gameUser = g;
			}
		}
		
		return gameUser;
	}
}
