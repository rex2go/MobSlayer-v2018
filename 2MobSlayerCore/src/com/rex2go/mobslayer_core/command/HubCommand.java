package com.rex2go.mobslayer_core.command;

import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;

public class HubCommand extends Command {

	public HubCommand() {
		// TODO
		super(new String[] { "hub" }, Rank.USER, "");
	}

	@Override
	public void handle(User user, String[] args) {
		user.sendToLobby();
	}
}
