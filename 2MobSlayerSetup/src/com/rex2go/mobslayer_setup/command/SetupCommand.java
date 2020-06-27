package com.rex2go.mobslayer_setup.command;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.map.Map;
import com.rex2go.mobslayer_game.map.Section;
import com.rex2go.mobslayer_game.task.Task;

public class SetupCommand extends Command {

	public static final ItemStack PLAYER_SPAWN_SETTER = new ItemStack(Material.STICK);
	public static final ItemStack MOB_SPAWN_SETTER = new ItemStack(Material.STICK);
	public static final ItemStack DEAD_SPAWN_SETTER = new ItemStack(Material.STICK);
	public static final ItemStack DOOR_BLOCK_SETTER = new ItemStack(Material.STICK);

	static {
		ItemMeta itemMeta;

		itemMeta = PLAYER_SPAWN_SETTER.getItemMeta();
		itemMeta.setDisplayName("§bPlayer Spawn Setter");
		PLAYER_SPAWN_SETTER.setItemMeta(itemMeta);

		itemMeta = MOB_SPAWN_SETTER.getItemMeta();
		itemMeta.setDisplayName("§bMob Spawn Setter");
		MOB_SPAWN_SETTER.setItemMeta(itemMeta);

		itemMeta = DEAD_SPAWN_SETTER.getItemMeta();
		itemMeta.setDisplayName("§bDead Spawn Setter");
		DEAD_SPAWN_SETTER.setItemMeta(itemMeta);

		itemMeta = DOOR_BLOCK_SETTER.getItemMeta();
		itemMeta.setDisplayName("§bDoor Block Setter");
		DOOR_BLOCK_SETTER.setItemMeta(itemMeta);
	}

	public static Map loadedMap;

	public static Section loadedSection;

	public SetupCommand() {
		// TODO
		super(new String[] { "setup" }, Rank.ADMIN, "");
		addArgSuggestion(new String[] { "loadmap", "unloadmap", "loadsection", "unloadsection", "save", "kit" });
	}

	@SuppressWarnings({ "deprecation" })
	@Override
	public void handle(User user, String[] args) {
		if (MobSlayerGame.getGameManager().getGameState() != GameState.INGAME) {
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("kit")) {
					user.getPlayer().getInventory().addItem(PLAYER_SPAWN_SETTER);
					user.getPlayer().getInventory().addItem(MOB_SPAWN_SETTER);
					user.getPlayer().getInventory().addItem(DEAD_SPAWN_SETTER);
					user.getPlayer().getInventory().addItem(DOOR_BLOCK_SETTER);
				} else if (args[0].equalsIgnoreCase("loadmap")) {
					if (loadedMap == null) {
						if (args.length > 1) {
							loadedMap = MobSlayerGame.getMapManager().loadMap(args[1]);
							
							Task.stopLobbyCountdownTask();
							
							MobSlayerGame.setup = true;

							// for(Location playerSpawn :
							// loadedMap.getPlayerSpawns()) {
							// playerSpawn.getBlock().setType(Material.WOOL);
							// playerSpawn.getBlock().setData((byte) 4);
							// }

							if(!MobSlayerGame.getMapManager().teleportPlayersToMap(loadedMap)) {
								for(Player all : Bukkit.getOnlinePlayers()) {
									all.teleport(loadedMap.getWorld().getSpawnLocation());
								}
							}

						} else {
							user.getPlayer().sendMessage("§7/setup loadmap <world name>");
						}
					} else {
						user.sendTranslatedMessage("command.setup.already_loaded_a_map", Color.ERROR, null);
						user.sendTranslatedMessage("command.setup.use_unload_to_unload", Color.ERROR, null);
					}
				} else if (args[0].equalsIgnoreCase("unloadmap")) {
					if (loadedMap != null) {
						for (Player all : Bukkit.getOnlinePlayers()) {
							all.teleport(new Location(Bukkit.getWorld("world"), 498.5, 14, -226.5, 90, 0));
						}
						MobSlayerGame.getMapManager().unload(loadedMap);
						loadedMap = null;
						loadedSection = null;
						
						MobSlayerGame.setup = false;
						
						Task.startLobbyCountdownTask();
					} else {
						user.sendTranslatedMessage("command.setup.no_map_loaded", Color.ERROR, null);
					}
				} else if (args[0].equalsIgnoreCase("save")) {
					if (loadedMap != null) {
						if (loadedSection != null) {
							ArrayList<Location> mobSpawns = new ArrayList<>();
							for (Location loc : loadedSection.getMobSpawns()) {
								if (loc.getBlock().getType() == Material.WOOL
										&& loc.getBlock().getData() == (byte) 13) {
									mobSpawns.add(loc);
								}
							}
							loadedSection.setMobSpawns(mobSpawns);

							ArrayList<Location> doorBlocks = new ArrayList<>();
							for (Location loc : loadedSection.getDoorBlocks()) {
								if (loc.getBlock().getType() == Material.WOOL && loc.getBlock().getData() == (byte) 6) {
									doorBlocks.add(loc);
								}
							}
							loadedSection.setDoorBlocks(doorBlocks);

							if (loadedSection.getDeadSpawn() != null) {
								if (!(loadedSection.getDeadSpawn().getBlock().getType() == Material.WOOL && loadedSection.getDeadSpawn().getBlock().getData() == (byte) 10)) {
									loadedSection.setDeadSpawn(null);
								}
							}
						}

						MobSlayerGame.getMapManager().save(loadedMap);
					} else {
						user.sendTranslatedMessage("command.setup.no_map_loaded", Color.ERROR, null);
					}
				} else if (args[0].equalsIgnoreCase("loadsection")) {
					if (loadedMap != null) {
						if (loadedSection == null) {
							if (args.length > 1) {
								try {
									int i = Integer.parseInt(args[1]);

									loadedSection = loadedMap.getSections().get(i);

									for (Location loc : loadedSection.getMobSpawns()) {
										loc.getBlock().setType(Material.WOOL);
										loc.getBlock().setData((byte) 13);
									}

									for (Location loc : loadedSection.getDoorBlocks()) {
										loc.getBlock().setType(Material.WOOL);
										loc.getBlock().setData((byte) 6);
									}

									if (loadedSection.getDeadSpawn() != null) {
										if (loadedSection.getDeadSpawn().getBlock() != null) {
											loadedSection.getDeadSpawn().getBlock().setType(Material.WOOL);
											loadedSection.getDeadSpawn().getBlock().setData((byte) 10);
										}
									}

								} catch (Exception e) {
									user.sendTranslatedMessage("command.wrong_argument", Color.ERROR, null);

									e.printStackTrace();
								}
							} else {
								user.getPlayer().sendMessage("§7/setup loadsection <index>");
							}
						} else {
							user.sendTranslatedMessage("command.setup.already_loaded_a_section", Color.ERROR, null);
							user.sendTranslatedMessage("command.setup.use_unloadsection_to_unload", Color.ERROR, null);
						}
					} else {
						user.sendTranslatedMessage("command.setup.no_map_loaded", Color.ERROR, null);
					}
				} else if (args[0].equalsIgnoreCase("unloadsection")) {
					if (loadedMap != null) {
						if (loadedSection != null) {

							for (Location loc : loadedSection.getMobSpawns()) {
								loc.getBlock().setType(Material.AIR);
							}

							for (Location loc : loadedSection.getDoorBlocks()) {
								loc.getBlock().setType(Material.AIR);
							}

							if (loadedSection.getDeadSpawn() != null) {
								loadedSection.getDeadSpawn().getBlock().setType(Material.AIR);
							}

							loadedSection = null;
						} else {
							user.sendTranslatedMessage("command.setup.no_section_loaded", Color.ERROR, null);
						}
					} else {
						user.sendTranslatedMessage("command.setup.no_map_loaded", Color.ERROR, null);
					}
				} else if (args[0].equalsIgnoreCase("newsection")) {
					if (loadedMap != null) {
						if (loadedSection == null) {
							Section section = new Section(new ArrayList<>(), null, new ArrayList<>());
							loadedMap.getSections().add(section);
							loadedSection = section;
						} else {
							user.sendTranslatedMessage("command.setup.already_loaded_a_section", Color.ERROR, null);
							user.sendTranslatedMessage("command.setup.use_unloadsection_to_unload", Color.ERROR, null);
						}
					} else {
						user.sendTranslatedMessage("command.setup.no_map_loaded", Color.ERROR, null);
					}
				} else {
					user.getPlayer()
							.sendMessage("§7/setup <loadmap|unloadmap|loadsection|unloadsection|newsection|save|kit>");
				}
			} else {
				user.getPlayer()
						.sendMessage("§7/setup <loadmap|unloadmap|loadsection|unloadsection|newsection|save|kit>");
			}
		} else

		{
			user.sendTranslatedMessage("command.only_in_lobby", Color.ERROR, null);
		}
	}
}
