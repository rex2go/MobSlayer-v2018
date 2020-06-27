package com.rex2go.mobslayer_core.manager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.Rank;

public class MultiTeamManager extends Manager {

	private HashMap<Player, Scoreboard> boards = new HashMap<Player, Scoreboard>();

	public void addScoreboard(Player p, Scoreboard board) {
		boards.put(p, board);
		setupTeams(p, board);
	}

	public void removeScoreboard(Player p) {
		boards.remove(p);
	}

	public void addPlayer(Player p) {
		if (isInAnyTeam(p)) {
			removePlayer(p);
		}
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (all != p) {
				if (MobSlayerCore.getUserManager().getUserByName(p.getName()).getRank() == Rank.USER) {
					((Scoreboard) boards.get(all)).getTeam("e").addEntry(p.getName());
				} else if (MobSlayerCore.getUserManager().getUserByName(p.getName()).getRank() == Rank.PREMIUM) {
					((Scoreboard) boards.get(all)).getTeam("d").addEntry(p.getName());
				} else if (MobSlayerCore.getUserManager().getUserByName(p.getName()).getRank() == Rank.VIP) {
					((Scoreboard) boards.get(all)).getTeam("c").addEntry(p.getName());
				} else if (MobSlayerCore.getUserManager().getUserByName(p.getName()).getRank() == Rank.MODERATOR) {
					((Scoreboard) boards.get(all)).getTeam("b").addEntry(p.getName());
				} else if (MobSlayerCore.getUserManager().getUserByName(p.getName()).getRank() == Rank.ADMIN) {
					((Scoreboard) boards.get(all)).getTeam("a").addEntry(p.getName());
				}
			} else {
				for (Player all1 : Bukkit.getOnlinePlayers()) {
					if (MobSlayerCore.getUserManager().getUserByName(all1.getName()).getRank() == Rank.USER) {
						((Scoreboard) boards.get(p)).getTeam("e").addEntry(all1.getName());
					} else if (MobSlayerCore.getUserManager().getUserByName(all1.getName()).getRank() == Rank.PREMIUM) {
						((Scoreboard) boards.get(p)).getTeam("d").addEntry(all1.getName());
					} else if (MobSlayerCore.getUserManager().getUserByName(all1.getName()).getRank() == Rank.VIP) {
						((Scoreboard) boards.get(p)).getTeam("c").addEntry(all1.getName());
					} else if (MobSlayerCore.getUserManager().getUserByName(all1.getName()).getRank() == Rank.MODERATOR) {
						((Scoreboard) boards.get(p)).getTeam("b").addEntry(all1.getName());
					} else if (MobSlayerCore.getUserManager().getUserByName(all1.getName()).getRank() == Rank.ADMIN) {
						((Scoreboard) boards.get(p)).getTeam("a").addEntry(all1.getName());
					}
				}
			}
		}
	}

	public void updateScoreboard(Player p) {
		if (isInAnyTeam(p)) {
			removePlayer(p);
		}
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (all != p) {
				if (MobSlayerCore.getUserManager().getUserByName(p.getName()).getRank() == Rank.USER) {
					((Scoreboard) boards.get(all)).getTeam("e").addEntry(p.getName());
				} else if (MobSlayerCore.getUserManager().getUserByName(p.getName()).getRank() == Rank.PREMIUM) {
					((Scoreboard) boards.get(all)).getTeam("d").addEntry(p.getName());
				} else if (MobSlayerCore.getUserManager().getUserByName(p.getName()).getRank() == Rank.VIP) {
					((Scoreboard) boards.get(all)).getTeam("c").addEntry(p.getName());
				} else if (MobSlayerCore.getUserManager().getUserByName(p.getName()).getRank() == Rank.MODERATOR) {
					((Scoreboard) boards.get(all)).getTeam("b").addEntry(p.getName());
				} else if (MobSlayerCore.getUserManager().getUserByName(p.getName()).getRank() == Rank.ADMIN) {
					((Scoreboard) boards.get(all)).getTeam("a").addEntry(p.getName());
				}
			} else {
				for (Player all1 : Bukkit.getOnlinePlayers()) {
					if (MobSlayerCore.getUserManager().getUserByName(all1.getName()).getRank() == Rank.USER) {
						((Scoreboard) boards.get(p)).getTeam("e").addEntry(all1.getName());
					} else if (MobSlayerCore.getUserManager().getUserByName(all1.getName()).getRank() == Rank.PREMIUM) {
						((Scoreboard) boards.get(p)).getTeam("d").addEntry(all1.getName());
					} else if (MobSlayerCore.getUserManager().getUserByName(all1.getName()).getRank() == Rank.VIP) {
						((Scoreboard) boards.get(p)).getTeam("c").addEntry(all1.getName());
					} else if (MobSlayerCore.getUserManager().getUserByName(all1.getName()).getRank() == Rank.MODERATOR) {
						((Scoreboard) boards.get(p)).getTeam("b").addEntry(all1.getName());
					} else if (MobSlayerCore.getUserManager().getUserByName(all1.getName()).getRank() == Rank.ADMIN) {
						((Scoreboard) boards.get(p)).getTeam("a").addEntry(all1.getName());
					}
				}
			}
		}
	}

	public void removePlayer(Player p) {
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (((Scoreboard) boards.get(p)).getTeam("a").hasEntry(p.getName())) {
				((Scoreboard) boards.get(all)).getTeam("a").removeEntry(p.getName());
			} else if (((Scoreboard) boards.get(p)).getTeam("b").hasEntry(p.getName())) {
				((Scoreboard) boards.get(all)).getTeam("b").removeEntry(p.getName());
			} else if (((Scoreboard) boards.get(p)).getTeam("c").hasEntry(p.getName())) {
				((Scoreboard) boards.get(all)).getTeam("c").removeEntry(p.getName());
			} else if (((Scoreboard) boards.get(p)).getTeam("d").hasEntry(p.getName())) {
				((Scoreboard) boards.get(all)).getTeam("d").removeEntry(p.getName());
			} else if (((Scoreboard) boards.get(p)).getTeam("e").hasEntry(p.getName())) {
				((Scoreboard) boards.get(all)).getTeam("e").removeEntry(p.getName());
			}
		}
	}

	public boolean isInAnyTeam(Player p) {
		if (boards != null) {
			if (boards.containsKey(p)) {
				for (Team team : ((Scoreboard) boards.get(p)).getTeams()) {
					if (team.getEntries().contains(p.getName())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public Team getTeam(Player p) {
		for (Team team : ((Scoreboard) boards.get(p)).getTeams()) {
			if (team.getEntries().contains(p.getName())) {
				return team;
			}
		}
		return null;
	}

	public void setupTeams(Player p, Scoreboard board) {
		if (board.getTeam("a") == null) {
			board.registerNewTeam("a");
		}
		if (board.getTeam("b") == null) {
			board.registerNewTeam("b");
		}
		if (board.getTeam("c") == null) {
			board.registerNewTeam("c");
		}
		if (board.getTeam("d") == null) {
			board.registerNewTeam("d");
		}
		if (board.getTeam("e") == null) {
			board.registerNewTeam("e");
		}
		board.getTeam("a").setPrefix(Rank.ADMIN.getRankColor());
		board.getTeam("b").setPrefix(Rank.MODERATOR.getRankColor());
		board.getTeam("c").setPrefix(Rank.VIP.getRankColor());
		board.getTeam("d").setPrefix(Rank.PREMIUM.getRankColor());
		board.getTeam("e").setPrefix(Rank.USER.getRankColor());
	}
}
