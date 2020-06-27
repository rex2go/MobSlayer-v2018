package com.rex2go.mobslayer_core.command;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;

public class GameModeCommand extends Command {

	public GameModeCommand() {
		// TODO
		super(new String[] { "gamemode", "gm" }, Rank.ADMIN, "");
		addArgSuggestion(new String[] {"0", "1", "2", "3"});
		addArgSuggestion(new String[] {"%player%"});
	}

	@Override
	public void handle(User user, String[] args) {
		Player player = user.getPlayer();
		if (args.length > 0) {
			if(args.length > 1) {
				Player target = null;
				for(Player all : Bukkit.getOnlinePlayers()) {
					if(all.getName().equalsIgnoreCase(args[1])) {
						target = all;
					}
				}
				if(target != null) {
					player = target;
				} else {
					user.sendTranslatedMessage("command.gamemode.user_not_online", Color.ERROR, null);
					return;
				}
			}
			if (args[0].equalsIgnoreCase("0")) {
				player.setGameMode(GameMode.SURVIVAL);
			} else if (args[0].equalsIgnoreCase("1")) {
				player.setGameMode(GameMode.CREATIVE);
			} else if (args[0].equalsIgnoreCase("2")) {
				player.setGameMode(GameMode.ADVENTURE);
			} else if (args[0].equalsIgnoreCase("3")) {
				player.setGameMode(GameMode.SPECTATOR);
			} else {
				user.sendTranslatedMessage("command.wrong_argument", Color.ERROR, null);
			}
		} else {
			player.sendMessage("§7/gamemode <0|1|2|3>");
		}
	}
}
