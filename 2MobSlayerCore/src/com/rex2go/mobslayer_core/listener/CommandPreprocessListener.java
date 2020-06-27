package com.rex2go.mobslayer_core.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.User;

public class CommandPreprocessListener implements Listener {

	public CommandPreprocessListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player player = e.getPlayer();
		User user = MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());
		if (user != null) {
			String command;
			String[] args1;
			command = e.getMessage().substring(1).split(" ")[0];
			args1 = e.getMessage().substring(1).split(" ");

			int n = args1.length - 1;
			String[] args = new String[n];
			System.arraycopy(args1, 1, args, 0, n);

			boolean contains = false;

			for (Command command1 : MobSlayerCore.getCommandManager().getCommands()) {
				for (String cmd : command1.getCommands()) {
					if (cmd.equalsIgnoreCase(command)) {
						contains = true;
						if (command1.getRequiredRank().getRankId() <= user.getRank().getRankId()) {
							try {
								command1.handle(user, args);
							} catch (Exception e1) {
								e1.printStackTrace();
								user.sendTranslatedMessage("command.error", Color.ERROR, null);
							}
						} else {
							user.sendTranslatedMessage("command.no_permission", Color.ERROR, null);
						}
					}
				}
			}
			if (!contains) {
				user.sendTranslatedMessage("command.unknown_command", Color.ERROR, null);
			}
		} else {
			// Error
		}
		e.setCancelled(true);
	}
}
