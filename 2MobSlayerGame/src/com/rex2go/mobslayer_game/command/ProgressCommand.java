package com.rex2go.mobslayer_game.command;

import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.user.GameUser;

public class ProgressCommand extends Command {

	public ProgressCommand() {
		super(new String[] { "progress" }, Rank.USER, "");
	}

	@Override
	public void handle(User user, String[] args) {
		GameUser gameUser = (GameUser) user;
		gameUser.sendProgress();
	}
}
