package com.rex2go.mobslayer_game.command;

import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;

public class KillAllCommand extends Command {

	public KillAllCommand() {
		// TODO
		super(new String[] { "killall" }, Rank.MODERATOR, "");
	}

	@Override
	public void handle(User user, String[] args) {
		MobSlayerGame.getGameEntityManager().killAll();
	}
}
