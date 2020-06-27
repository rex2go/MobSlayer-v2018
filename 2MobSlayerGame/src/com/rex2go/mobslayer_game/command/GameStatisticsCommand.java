package com.rex2go.mobslayer_game.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.user.GameUser;

public class GameStatisticsCommand extends Command {

	public GameStatisticsCommand() {
		super(new String[] { "gamestatistics", "gamestats", "gstats" }, Rank.USER, "");
	}

	@Override
	public void handle(User user, String[] args) {
		GameUser gameUser = (GameUser) user;
		if(args.length > 0) {
			String name = args[0];
			
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(all.getName().equalsIgnoreCase(name)) {
					gameUser = (GameUser) MobSlayerCore.getUserManager().getUserByName(name);
				}
			}
		}
		
		user.getPlayer().sendMessage("§7     --- §6" + Translation.getTranslation("command.stats.game_statistics", user.getLanguage()) + " §7---");
		user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.points", user.getLanguage()) + ": §f" + gameUser.getInGamePoints());
		user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.coins", user.getLanguage()) + ": §f" + gameUser.getInGameCoins());
		user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.kills", user.getLanguage()) + ": §f" + gameUser.getKills());
		user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.deaths", user.getLanguage()) + ": §f" + gameUser.getDeaths());
		user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.revives", user.getLanguage()) + ": §f" + gameUser.getRevives());
		user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.stats.kd", user.getLanguage()) + ": §f" + gameUser.getInGameKD());
	}	
}
