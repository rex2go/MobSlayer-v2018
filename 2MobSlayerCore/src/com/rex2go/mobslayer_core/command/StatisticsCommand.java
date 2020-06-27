package com.rex2go.mobslayer_core.command;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Translation;

public class StatisticsCommand extends Command {

	public StatisticsCommand() {
		super(new String[] { "statistics", "stats" }, Rank.USER, "");
		addArgSuggestion(new String[] { "%player%" });
	}

	@Override
	public void handle(User user, String[] args) {
		if(args.length > 0) {
			@SuppressWarnings("deprecation")
			User user1 = MobSlayerCore.getUserManager().loadUser(args[0]);
			if(user1 != null) {
				int totalSecs = user1.getStatistics().getPlayTime();
				int hours = totalSecs / 3600;
				int minutes = (totalSecs % 3600) / 60;
				int seconds = totalSecs % 60;

				String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
				
				user.getPlayer().sendMessage("§7     --- §6" + Translation.getTranslation("command.stats.statistics_of", user.getLanguage(), 
						user1.getPlayer() == null ? args[0] : user1.getPlayer().getName()) + " §7---");
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.play_time", user.getLanguage()) + ": §f" + timeString);
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.played_games", user.getLanguage()) + ": §f" + user1.getStatistics().getGames());
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.wins", user.getLanguage()) + ": §f" + user1.getStatistics().getWins());
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.points", user.getLanguage()) + ": §f" + user1.getPoints());
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.coins", user.getLanguage()) + ": §f" + user1.getCoins());
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.kills", user.getLanguage()) + ": §f" + user1.getStatistics().getKills());
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.deaths", user.getLanguage()) + ": §f" + user1.getStatistics().getDeaths());
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.kd", user.getLanguage()) + ": §f" + user1.getStatistics().getKD());
			} else {
				int totalSecs = 0;
				int hours = totalSecs / 3600;
				int minutes = (totalSecs % 3600) / 60;
				int seconds = totalSecs % 60;

				String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
				
				user.getPlayer().sendMessage("§7     --- §6" + Translation.getTranslation("command.stats.statistics_of", user.getLanguage(), args[0]) + " §7---");
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.play_time", user.getLanguage()) + ": §f" + timeString);
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.played_games", user.getLanguage()) + ": §f" + 0);
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.wins", user.getLanguage()) + ": §f" + 0);
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.points", user.getLanguage()) + ": §f" + 0);
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.coins", user.getLanguage()) + ": §f" + 0);
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.kills", user.getLanguage()) + ": §f" + 0);
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.deaths", user.getLanguage()) + ": §f" + 0);
				user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.kd", user.getLanguage()) + ": §f" + 0);
			}
		} else {
			int totalSecs = user.getStatistics().getPlayTime();
			int hours = totalSecs / 3600;
			int minutes = (totalSecs % 3600) / 60;
			int seconds = totalSecs % 60;

			String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
			
			user.getPlayer().sendMessage("§7     --- §6" + Translation.getTranslation("command.stats.statistics", user.getLanguage()) + " §7---");
			user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.play_time", user.getLanguage()) + ": §f" + timeString);
			user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.played_games", user.getLanguage()) + ": §f" + user.getStatistics().getGames());
			user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.wins", user.getLanguage()) + ": §f" + user.getStatistics().getWins());
			user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.points", user.getLanguage()) + ": §f" + user.getPoints());
			user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.coins", user.getLanguage()) + ": §f" + user.getCoins());
			user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.kills", user.getLanguage()) + ": §f" + user.getStatistics().getKills());
			user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.deaths", user.getLanguage()) + ": §f" + user.getStatistics().getDeaths());
			user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.kd", user.getLanguage()) + ": §f" + user.getStatistics().getKD());
		}
	}
}
