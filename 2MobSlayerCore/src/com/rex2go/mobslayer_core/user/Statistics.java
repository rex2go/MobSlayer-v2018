package com.rex2go.mobslayer_core.user;

import com.rex2go.mobslayer_core.util.MathUtil;

public class Statistics {

	int games, kills, deaths, playTime, wins, bestPlays;
	
	public Statistics(int games, int kills, int deaths, int playTime, int wins, int bestPlays) {
		this.games = games;
		this.kills = kills;
		this.deaths = deaths;
		this.playTime = playTime;
		this.wins = wins;
		this.bestPlays = bestPlays;
	}
	
	public double getKD() {
		if(deaths == 0) {
			return MathUtil.round((kills), 2);
		}
		return MathUtil.round((((double)kills)/((double)deaths)), 2);
	}
	
	public int getGames() {
		return games;
	}

	public void setGames(int games) {
		this.games = games;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getPlayTime() {
		return playTime;
	}

	public void setPlayTime(int playTime) {
		this.playTime = playTime;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}
	
	public int getBestPlays() {
		return bestPlays;
	}
}
