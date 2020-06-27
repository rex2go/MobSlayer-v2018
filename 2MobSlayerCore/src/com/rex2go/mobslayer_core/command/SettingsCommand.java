package com.rex2go.mobslayer_core.command;

import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;

public class SettingsCommand extends Command {

	public SettingsCommand() {
		super(new String[] { "settings" }, Rank.USER, "");
	}

	@Override
	public void handle(User user, String[] args) {
		if(user != null) {
			user.openSettings();
		}
	}
}
