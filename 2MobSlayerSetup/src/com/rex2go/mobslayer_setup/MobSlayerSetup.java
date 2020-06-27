package com.rex2go.mobslayer_setup;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_setup.command.SetupCommand;
import com.rex2go.mobslayer_setup.listener.InteractListener;

public class MobSlayerSetup extends JavaPlugin {

	public void onEnable() {
		setupTranslation();

		registerListener();
		registerCommands();
	}

	public void onDisable() {
		if (SetupCommand.loadedMap != null) {
			Bukkit.getServer().unloadWorld(SetupCommand.loadedMap.getWorldName(), false);
		}
	}

	public void registerListener() {
		new InteractListener();
	}

	public void registerCommands() {
		MobSlayerCore.getCommandManager().registerCommand(new SetupCommand());
	}

	private void setupTranslation() {
		Translation.addTranslation("command.setup.no_map_loaded", new String[]{"Keine Map geladen", "No map loaded"});
		Translation.addTranslation("command.setup.already_loaded_a_map", new String[]{"Eine Map ist bereits geladen", "A map is already loaded"});
		Translation.addTranslation("command.setup.use_unload_to_unload", new String[]{"Nutze /unload um die Map zu entladen", "Use /unload to unload the map"});
	}
}
