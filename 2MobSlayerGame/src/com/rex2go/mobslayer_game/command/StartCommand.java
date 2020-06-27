package com.rex2go.mobslayer_game.command;

import org.bukkit.Bukkit;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.task.Task;

public class StartCommand extends Command {

	public StartCommand() {
		// TODO
		super(new String[] { "start" }, Rank.VIP, "");
	}

	@Override
	public void handle(User user, String[] args) {
		if (MobSlayerGame.getGameManager().getGameState() != GameState.INGAME) {
			if (Bukkit.getOnlinePlayers().size() >= MobSlayerGame.PLAYER_COUNT_REQUIRED) {
				if (Task.lobbyWaitingTime > 10) {
					Task.lobbyWaitingTime = 10;
				}
			} else {
				// TODO Nachricht
			}
		} else {
			user.sendTranslatedMessage("command.only_in_lobby", Color.ERROR, null);
		}
	}
}
