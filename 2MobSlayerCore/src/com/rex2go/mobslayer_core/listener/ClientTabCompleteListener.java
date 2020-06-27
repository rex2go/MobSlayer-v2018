package com.rex2go.mobslayer_core.listener;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.event.ClientTabCompleteEvent;
import com.rex2go.mobslayer_core.user.User;

public class ClientTabCompleteListener implements Listener {

	public ClientTabCompleteListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onClientTabComplete(ClientTabCompleteEvent e) {
		Player player = e.getPlayer();
		User user = MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());
		if (user != null) {
			if (e.getMessage().startsWith("/")) {
				e.setCancelled(true);
				ArrayList<String> commandsList = new ArrayList<>();
				for (Command command : MobSlayerCore.getCommandManager().getCommands()) {
					if (user.getRank().getRankId() >= command.getRequiredRank().getRankId()) {
						commandsList.add("/" + command.getCommands()[0]);
					}
				}
				
				if (!e.getMessage().contains(" ")) {
					ArrayList<String> cmds = new ArrayList<>();
					for (String command : commandsList) {
						if (command.length() >= e.getMessage().length()) {
							String cmd = command.substring(0, e.getMessage().length());
							if (cmd.equalsIgnoreCase(e.getMessage())) {
								cmds.add(command);
							}
						}
					}
					
					String[] commandsArr = new String[cmds.size()];
					commandsArr = cmds.toArray(commandsArr);
					PacketContainer tabComplete = new PacketContainer(PacketType.Play.Server.TAB_COMPLETE);
					tabComplete.getStringArrays().write(0, commandsArr);
					try {
						MobSlayerCore.getProtocolManager().sendServerPacket(e.getPlayer(), tabComplete);
					} catch (InvocationTargetException e1) {
						throw new RuntimeException("Cannot send packet.", e1);
					}
				} else {
					String[] parts = e.getMessage().split(" ");
					Command cmd = null;
					
					for (Command cmd1 : MobSlayerCore.getCommandManager().getCommands()) {
						for(String cmdName : cmd1.getCommands()) {
							if(cmdName.equalsIgnoreCase(parts[0].substring(1))) {
								cmd = MobSlayerCore.getCommandManager().getCommandByName(cmdName);
								break;
							}
						}
					}
					
					if (cmd != null) {
						if (cmd.getArgSuggestion() != null) {
							if (cmd.getArgSuggestion().size() >= parts.length) {
								try {
									
									String[] argSuggestion = cmd.getArgSuggestion().get(parts.length - 1);
									
									if (!argSuggestion[0].equalsIgnoreCase("%player%")) {
										PacketContainer tabComplete = new PacketContainer(
												PacketType.Play.Server.TAB_COMPLETE);
										tabComplete.getStringArrays().write(0, argSuggestion);
										try {
											MobSlayerCore.getProtocolManager().sendServerPacket(e.getPlayer(), tabComplete);
										} catch (InvocationTargetException e1) {
											throw new RuntimeException("Cannot send packet.", e1);
										}
									} else {
										ArrayList<String> names = new ArrayList<>();
										for (Player all : Bukkit.getOnlinePlayers()) {
											names.add(all.getName());
										}
										String[] namesArr = new String[names.size()];
										namesArr = names.toArray(namesArr);
										PacketContainer tabComplete = new PacketContainer(
												PacketType.Play.Server.TAB_COMPLETE);
										tabComplete.getStringArrays().write(0, namesArr);
										try {
											MobSlayerCore.getProtocolManager().sendServerPacket(e.getPlayer(), tabComplete);
										} catch (InvocationTargetException e1) {
											throw new RuntimeException("Cannot send packet.", e1);
										}
									}
								} catch (Exception e2) {
									// juckt nicht
								}
							}
						}
					}
				}
			} else if(e.getMessage().startsWith("@")) {
				if(e.getMessage().endsWith("@")) {
					ArrayList<String> names = new ArrayList<>();
					for (Player all : Bukkit.getOnlinePlayers()) {
						names.add("@" + all.getName());
					}
					
					String[] namesArr = new String[names.size()];
					namesArr = names.toArray(namesArr);
					PacketContainer tabComplete = new PacketContainer(
							PacketType.Play.Server.TAB_COMPLETE);
					tabComplete.getStringArrays().write(0, namesArr);
					
					try {
						MobSlayerCore.getProtocolManager().sendServerPacket(e.getPlayer(), tabComplete);
					} catch (InvocationTargetException e1) {
						throw new RuntimeException("Cannot send packet.", e1);
					}
				} else {
					String[] parts = e.getMessage().split("@");
					String post = parts[parts.length-1].toLowerCase();
					
					ArrayList<String> names = new ArrayList<>();
					for (Player all : Bukkit.getOnlinePlayers()) {
						if(all.getName().toLowerCase().startsWith(post)) {
							names.add("@" + all.getName());
						}
					}
					
					if(!names.isEmpty()) {
						String[] namesArr = new String[names.size()];
						namesArr = names.toArray(namesArr);
						PacketContainer tabComplete = new PacketContainer(PacketType.Play.Server.TAB_COMPLETE);
						tabComplete.getStringArrays().write(0, namesArr);
						
						try {
							MobSlayerCore.getProtocolManager().sendServerPacket(e.getPlayer(), tabComplete);
						} catch (InvocationTargetException e1) {
							throw new RuntimeException("Cannot send packet.", e1);
						}
					}
				}
			}
		}
	}
}
