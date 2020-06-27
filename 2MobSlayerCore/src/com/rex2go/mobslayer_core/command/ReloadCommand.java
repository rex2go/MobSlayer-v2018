package com.rex2go.mobslayer_core.command;

import org.bukkit.Bukkit;

import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;

public class ReloadCommand extends Command {

	public ReloadCommand() {
		// TODO
		super(new String[] { "reload", "rl" }, Rank.ADMIN, "");
	}

	@Override
	public void handle(User user, String[] args) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload");
	}
}
