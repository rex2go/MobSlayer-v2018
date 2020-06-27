package com.rex2go.mobslayer_core;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.google.common.io.ByteArrayDataInput;
import com.rex2go.mobslayer_core.command.AddCoinsCommand;
import com.rex2go.mobslayer_core.command.CoinsCommand;
import com.rex2go.mobslayer_core.command.DemoteCommand;
import com.rex2go.mobslayer_core.command.GameModeCommand;
import com.rex2go.mobslayer_core.command.HubCommand;
import com.rex2go.mobslayer_core.command.LanguageCommand;
import com.rex2go.mobslayer_core.command.PromoteCommand;
import com.rex2go.mobslayer_core.command.ReloadCommand;
import com.rex2go.mobslayer_core.command.RemoveCoinsCommand;
import com.rex2go.mobslayer_core.command.SettingsCommand;
import com.rex2go.mobslayer_core.command.StatisticsCommand;
import com.rex2go.mobslayer_core.event.ClientTabCompleteEvent;
import com.rex2go.mobslayer_core.listener.BlockFadeListener;
import com.rex2go.mobslayer_core.listener.ChatListener;
import com.rex2go.mobslayer_core.listener.ClientTabCompleteListener;
import com.rex2go.mobslayer_core.listener.CommandPreprocessListener;
import com.rex2go.mobslayer_core.listener.InventoryClickListener;
import com.rex2go.mobslayer_core.listener.JoinListener;
import com.rex2go.mobslayer_core.listener.PluginChannelListener;
import com.rex2go.mobslayer_core.listener.QuitListener;
import com.rex2go.mobslayer_core.manager.CommandManager;
import com.rex2go.mobslayer_core.manager.LanguageManager;
import com.rex2go.mobslayer_core.manager.MultiTeamManager;
import com.rex2go.mobslayer_core.manager.RankManager;
import com.rex2go.mobslayer_core.manager.UserManager;
import com.rex2go.mobslayer_core.task.Task;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.MySQL;
import com.rex2go.mobslayer_core.util.Translation;

public class MobSlayerCore extends JavaPlugin {

	public static ArrayList<String> badWordStorage = new ArrayList<>();

	private static MobSlayerCore instance;
	
	private static ProtocolManager protocolManager;

	public static final boolean DEBUG = false;
	public static final String PREFIX = "§8[§6MobSlayer§8]";
	public static  String bungeeServerName;
	
	public static Translation translation;
	
	private static UserManager userManager;
	private static RankManager rankManager;
	private static CommandManager commandManager;
	private static LanguageManager languageManager;
	private static MultiTeamManager multiTeamManager;

	public void onEnable() {
		instance = this;
		translation = new Translation();
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginChannelListener());
		
		getInstance().getConfig().addDefault("host", "");
		getInstance().getConfig().addDefault("user", "");
		getInstance().getConfig().addDefault("database", "");
		getInstance().getConfig().addDefault("password", "");
			
		getConfig().options().copyDefaults(true);
		
		getInstance().saveConfig();
		
		connectMySQL();
		
		protocolManager = ProtocolLibrary.getProtocolManager();

		userManager = new UserManager();
		rankManager = new RankManager();
		commandManager = new CommandManager();
		languageManager = new LanguageManager();
		multiTeamManager = new MultiTeamManager();

		getUserManager().loadAll();

		registerListener();
		registerCommands();

		setupTranslations();

		// ClientTabCompleteEvent

		protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL,
				new PacketType[] { PacketType.Play.Client.TAB_COMPLETE }) {
			public void onPacketReceiving(PacketEvent event) {
				if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE) {
					try {
						PacketContainer packet = event.getPacket();
						String message = ((String) packet.getSpecificModifier(String.class).read(0)).toLowerCase();
						
						ClientTabCompleteEvent tabCompleteEvent = new ClientTabCompleteEvent(message,
								event.getPlayer());
						getServer().getPluginManager().callEvent(tabCompleteEvent);
						event.setCancelled(tabCompleteEvent.isCancelled());
					} catch (FieldAccessException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		Task.startOnlineTimeUpdater();
	}

	public void onDisable() {
		getUserManager().saveAll();
		MySQL.close();

		for (Player all : Bukkit.getOnlinePlayers()) {
			all.kickPlayer("");
		}
	}

	public void registerListener() {
		new ChatListener();
		new ClientTabCompleteListener();
		new CommandPreprocessListener();
		new JoinListener();
		new QuitListener();
		new BlockFadeListener();
		new InventoryClickListener();
	}

	public void registerCommands() {
		getCommandManager().registerCommand(new DemoteCommand());
		getCommandManager().registerCommand(new GameModeCommand());
		getCommandManager().registerCommand(new PromoteCommand());
		getCommandManager().registerCommand(new ReloadCommand());
		getCommandManager().registerCommand(new LanguageCommand());
		getCommandManager().registerCommand(new SettingsCommand());
		getCommandManager().registerCommand(new StatisticsCommand());
		getCommandManager().registerCommand(new AddCoinsCommand());
		getCommandManager().registerCommand(new RemoveCoinsCommand());
		getCommandManager().registerCommand(new CoinsCommand());
		getCommandManager().registerCommand(new HubCommand());
	}

	public static void debug(String message) {

	}

	private static void connectMySQL() {
		new MySQL(getInstance().getConfig().getString("host"), getInstance().getConfig().getString("database"), getInstance().getConfig().getString("user"), 
				getInstance().getConfig().getString("password"));

		MySQL.update("CREATE TABLE IF NOT EXISTS `mobslayer_users` ( `username` VARCHAR(255) NOT NULL , `uuid` VARCHAR(255) NOT NULL , `points` INT(11) NOT NULL , "
				+ "`coins` INT(11) NOT NULL , `rank` INT(11) NOT NULL , `firstjoin` VARCHAR(255) NOT NULL , PRIMARY KEY (`uuid`)) ENGINE = MyISAM;");
		
		MySQL.update("CREATE TABLE IF NOT EXISTS `mobslayer_settings` ( `uuid` VARCHAR(255) NOT NULL , `language` VARCHAR(255) NOT NULL , "
				+ "`enable_auto_join` BOOLEAN NOT NULL , `armor_colors` TEXT NOT NULL , `block_bad_words` BOOLEAN NOT NULL , `show_blood` BOOLEAN NOT NULL , `enable_scoreboard` "
				+ "BOOLEAN NOT NULL , `att_notification` BOOLEAN NOT NULL , `simple_crafting` BOOLEAN NOT NULL , `auto_respawn` BOOLEAN NOT NULL , `show_stars` BOOLEAN NOT NULL , "
				+ "PRIMARY KEY (`uuid`)) ENGINE = MyISAM;");
		
		MySQL.update("CREATE TABLE IF NOT EXISTS `mobslayer_badwords` ( `badword` VARCHAR(255) NOT NULL ) ENGINE = MyISAM;");
		
		MySQL.update("CREATE TABLE IF NOT EXISTS `mobslayer_maps` ( `map_name` VARCHAR(255) NOT NULL , `world_name` VARCHAR(255) NOT NULL , "
				+ "`author` VARCHAR(255) NOT NULL , `player_spawns` TEXT NOT NULL , `sections` LONGTEXT NOT NULL , `boss_section` TEXT NOT NULL , PRIMARY KEY (`map_name`)) "
				+ "ENGINE = MyISAM;");
		
		MySQL.update("CREATE TABLE IF NOT EXISTS `mobslayer_waves` ( `mobslayer_world` VARCHAR(255) NOT NULL , `level` INT(11) NOT NULL , `prepare_time` INT(11) NOT NULL , "
				+ "`spawn_requests` LONGTEXT NOT NULL , `wave_script` LONGTEXT NOT NULL) ENGINE = MyISAM;");
		
		MySQL.update("CREATE TABLE IF NOT EXISTS `mobslayer_boss_waves` ( `mobslayer_world` VARCHAR(255) NOT NULL , `prepare_time` INT NOT NULL , `spawn_requests` TEXT NOT NULL , "
				+ "`boss` VARCHAR(255) NOT NULL  , `boss_location` VARCHAR(255) NOT NULL , `wave_script` LONGTEXT NOT NULL ) ENGINE = MyISAM;");
		
		MySQL.update("CREATE TABLE IF NOT EXISTS `mobslayer_statistics` ( `uuid` VARCHAR(255) NOT NULL , `games` INT(11) NOT NULL , `kills` INT(11) NOT NULL , `deaths` "
				+ "INT(11) NOT NULL , `wins` INT(11) NOT NULL , `play_time` INT(11) NOT NULL , `best_plays` INT(11) NOT NULL , PRIMARY KEY (`uuid`)) ENGINE = MyISAM;");

		MySQL.update("CREATE TABLE IF NOT EXISTS `mobslayer_translations` ( `path` VARCHAR(255) NOT NULL , `translation` TEXT NOT NULL , PRIMARY KEY (`path`)) ENGINE = MyISAM;");
	}

	public static MobSlayerCore getInstance() {
		return instance;
	}

	public static ProtocolManager getProtocolManager() {
		return protocolManager;
	}

	public static UserManager getUserManager() {
		return userManager;
	}

	public static RankManager getRankManager() {
		return rankManager;
	}

	public static CommandManager getCommandManager() {
		return commandManager;
	}

	public static LanguageManager getLanguageManager() {
		return languageManager;
	}

	public static MultiTeamManager getMultiTeamManager() {
		return multiTeamManager;
	}

	private void loadBadWords() {
		ResultSet rs = MySQL.query("SELECT * FROM mobslayer_badwords");
		try {
			while (rs.next()) {
				badWordStorage.add(rs.getString("badword"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public enum Status {
		ERROR, INFO
	}

	public static void broadcast(String translationPath, Status status) {
		for (User all : getUserManager().getStorage()) {
			if(status == Status.ERROR) {
				all.getPlayer().sendMessage(Color.ERROR + Translation.getTranslation(translationPath, all.getLanguage()));
			} else if (status == Status.INFO) {
				all.getPlayer().sendMessage(PREFIX + "§7 " + Translation.getTranslation(translationPath, all.getLanguage()));
			}
		}
	}

	private void setupTranslations() {


		Translation.addTranslation("command.unknown_command", new String[] { "Unbekannter Befehl", "Unknown command" });
		Translation.addTranslation("command.no_permission",
				new String[] { "Du hast keine Berechtigung diesen Befehl auszuführen",
						"You don't have permission to execute this command" });
		Translation.addTranslation("command.wrong_argument",
				new String[] { "Ungültiges Argument", "Invalid argument" });
		Translation.addTranslation("command.gamemode.user_not_online",
				new String[] { "Spieler ist nicht online", "User is not online" });
		Translation.addTranslation("command.promote.rank_updated",
				new String[] { "Rang erfolgreich geändert", "Rank successfully updated" });
		Translation.addTranslation("command.promote.your_rank_has_been_updated",
				new String[] { "Dein Rang wurde aktualisiert", "Your rank has been updated" });
		Translation.addTranslation("command.promote.user_not_existing",
				new String[] { "Spieler nicht gefunden", "User not found" });
		Translation.addTranslation("command.language.supported_languages",
				new String[] { "Unterstützte Sprachen", "Supported languages" });
		
		Translation.addTranslation("general.settings", new String[] { "Einstellungen", "Settings" });
		
		Translation.addTranslation("general.settings.enabled", new String[] { "Aktiviert", "Enabled" });
		Translation.addTranslation("general.settings.disabled", new String[] { "Deaktiviert", "Disabled" });
		
		Translation.addTranslation("general.settings.auto_join", new String[] { "Automatisches Beitreten eines Gameservers", "Automated join to a game server" });
		Translation.addTranslation("general.settings.block_bad_words", new String[] { "Beleidigungen blockieren", "Block bad words" });
		Translation.addTranslation("general.settings.show_blood", new String[] { "Blut anzeigen", "Show blood" });
		Translation.addTranslation("general.settings.enable_scoreboard", new String[] { "Scoreboard anzeigen", "Show scoreboard" });
		Translation.addTranslation("general.settings.att_notification", new String[] { "@-Erwähnungungston (Chat)", "@-Mention Sound (Chat)" });
		Translation.addTranslation("general.settings.simple_crafting", new String[] { "Simples Crafting", "Simple Crafting" });
		Translation.addTranslation("general.settings.auto_respawn", new String[] { "Automatisierter Respawn (kostet 500 Coins pro Respawn)", "Automated Respawn (costs 500 coins each time)" });
		Translation.addTranslation("general.settings.show_stars", new String[] { "Sterne vor dem Titel im Chat anzeigen", "Show stars in front of title in chat" });
		
		Translation.addTranslation("language.changed_language_to",
				new String[] { "Du hast deine Sprache zu §e%s §7geändert.", "You changed your language to §e%s§7." });
		
		Translation.addTranslation("general.muted", new String[] { "Du kannst nichts schreiben, da du stummgeschaltet wurdest.", "You cannot chat because you have been muted." });
		
		Translation.addTranslation("command.stats.statistics", new String[] { "Statistiken", "Statistics" });
		Translation.addTranslation("command.stats.statistics_of", new String[] { "Statistiken von %s", "Statistics of %s" });
		Translation.addTranslation("command.stats.play_time", new String[] { "Spielzeit", "Play time" });
		Translation.addTranslation("command.stats.played_games", new String[] { "Gespielte Spiele", "Played games" });
		Translation.addTranslation("command.stats.wins", new String[] { "Gewonnene Spiele", "Games won" });
		Translation.addTranslation("command.stats.kills", new String[] { "Kills", "Kills" });
		Translation.addTranslation("command.stats.deaths", new String[] { "Tode", "Deaths" });
		Translation.addTranslation("command.stats.kd", new String[] { "K/D", "K/D" });
		Translation.addTranslation("command.stats.coins", new String[] { "Coins", "Coins" });
		Translation.addTranslation("command.stats.points", new String[] { "Punkte", "Points" });
		
		Translation.addTranslation("command.addcoins.added", new String[] { "Du hast %1$s %2$s Coin(s) gegeben.", "You gave %1$s %2$s coin(s)." });
		Translation.addTranslation("command.addcoins.received", new String[] { "Du hast %s Coin(s) bekommen.", "You received %s coin(s)." });
		
		Translation.addTranslation("command.removecoins.removed", new String[] { "Du hast %1$s %2$s Coin(s) entfernt.", "You removed %1$s %2$s coin(s)." });
		Translation.addTranslation("command.removecoins.received_remove", new String[] { "Dir wurden %s Coin(s) entfernt.", "%s coin(s) has been removed from your balance." });
		
		Translation.addTranslation("command.coins.coins", new String[] { "Coins", "Coins" });
		Translation.addTranslation("command.coins.coins_of", new String[] { "Coins von %s", "Coins of %s" });
		
		Translation.addTranslation("chat.title.beginner", new String[] { "Anfänger", "Beginner" });
		Translation.addTranslation("chat.title.recruit", new String[] { "Rekrut", "Recruit" });
		Translation.addTranslation("chat.title.soldier", new String[] { "Soldat", "Soldier" });
		Translation.addTranslation("chat.title.veteran", new String[] { "Veteran", "Veteran" });
		Translation.addTranslation("chat.title.hunter", new String[] { "Jäger", "Hunter" });
		Translation.addTranslation("chat.title.master", new String[] { "Meister", "Master" });
		Translation.addTranslation("chat.title.legend", new String[] { "L§4egende", "L§4egend" });
		
		Translation.addTranslation("general.win", new String[] { "Win", "Win" });
		Translation.addTranslation("general.wins", new String[] { "Wins", "Wins" });
	}
	
	public static String filter(String message) {
		for (String badWord : badWordStorage) {
			if (message.toLowerCase().contains(badWord)) {
				for (String msg : message.split(" ")) {
					if (msg.toLowerCase().contains(badWord)) {
						String replacement = "";
						for (int i = 0; i < msg.length(); i++) {
							replacement += "*";
						}
						message = message.replaceAll(msg, replacement);
					}
				}
			}
		}
		return message;
	}
	
	public static void sendToBungeeCord(Player player, String ... args) {
		if(args == null) return;
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(stream);
		
		try {
			for(String s : args) {
				out.writeUTF(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(Bukkit.getOnlinePlayers().size() > 0) {
			if(player != null) {
				player.sendPluginMessage(MobSlayerCore.getInstance(), "BungeeCord", stream.toByteArray());
			} else {
				Bukkit.getServer().sendPluginMessage(MobSlayerCore.getInstance(), "BungeeCord", stream.toByteArray());
			}
		}
	}
	
	public static void sendToBungeeCord(Player player, Callback callBack, String ... args) {
		sendToBungeeCord(player, args);
		PluginChannelListener.callbacks.add(callBack);
	}
	
	public void whoAmI(Player player) {
		if(bungeeServerName == null) {
			Callback callback = new Callback() {
				
				@Override
				public void fail() {}
				
				@Override
				protected void doResponse(ByteArrayDataInput in) {
					in.readUTF();
					in.readUTF();
					bungeeServerName = in.readUTF();
				}
			};
			
			sendToBungeeCord(player, callback, "WhoAmI", callback.getId(), player.getName());
		}
	}
}
