package com.rex2go.mobslayer_core.user;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.event.LanguageChangeEvent;
import com.rex2go.mobslayer_core.event.TitleUpEvent;
import com.rex2go.mobslayer_core.util.ItemUtil;
import com.rex2go.mobslayer_core.util.Language;
import com.rex2go.mobslayer_core.util.ServerUtil;
import com.rex2go.mobslayer_core.util.Translation;

public class User {

	boolean muted;
	
	Player player;
	int points;
	int coins;
	Rank rank;

	Settings settings;
	Statistics statistics;

	// Msg
	Player reply;
	ArrayList<UUID> ignored = new ArrayList<>();

	// Scoreboard
	Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
	Team team = board.registerNewTeam("mobslayer");
	Objective objective = board.registerNewObjective("test", "dummy");
	Score score;

	public User(Player player, int points, int coins, Rank rank, Settings settings, Statistics statistics) {
		this.player = player;
		this.points = points;
		this.coins = coins;
		this.rank = rank;
		this.settings = settings;
		this.statistics = statistics;
		setupScoreboard();
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}
	
	public boolean isMuted() {
		return muted;
	}
	
	public void setupScoreboard() {
		try {
			MobSlayerCore.getMultiTeamManager().addScoreboard(player, board);
			MobSlayerCore.getMultiTeamManager().addPlayer(player);
			player.setScoreboard(board);
		} catch (Exception e) {
		}
	}

	public void updateScoreboard() {
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		Title nextTitle = Title.LEGEND;
		
		if(getTitle() != Title.LEGEND) {
			for(Title title : Title.values()) {
				if(title.getPoints() > getTitle().getPoints()) {
					if(title.getPoints()-getPoints()<nextTitle.getPoints()-getPoints()) {
						nextTitle = title;
					}
				}
			}
			
			if(points >= nextTitle.getPoints()) {
				this.points = points;
				MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(new TitleUpEvent(this));
				return;
			}
		}
		
		this.points = points;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public Rank getRank() {
		return rank;
	}

	public Title getTitle() {
		Title title1 = Title.BEGINNER;
		
		for(Title title : Title.values()) {
			if(getPoints() >= title.getPoints()) {
				if(title.getPoints() > title1.getPoints()) {
					title1 = title;
				}
			}
		}
		
		return title1;
	}
	
	public void setRank(Rank rank) {
		this.rank = rank;
		if (MobSlayerCore.getUserManager().getStorage().contains(this)) {
			MobSlayerCore.getMultiTeamManager().updateScoreboard(getPlayer());
		}
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public Statistics getStatistics() {
		return statistics;
	}

	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}

	public Player getReply() {
		return reply;
	}

	public void setReplyPlayer(Player reply) {
		this.reply = reply;
	}

	public ArrayList<UUID> getIgnored() {
		return ignored;
	}

	public void setIgnoredList(ArrayList<UUID> ignoredList) {
		this.ignored = ignoredList;
	}

	public void sendTranslatedMessage(String message) {
		getPlayer().sendMessage(
				MobSlayerCore.PREFIX + "§7 " + Translation.getTranslation(message, getLanguage()));
	}

	public void sendTranslatedMessage(String message, String color) {
		getPlayer().sendMessage(
				MobSlayerCore.PREFIX + color + " " + Translation.getTranslation(message, getLanguage()));
	}

	public void sendTranslatedMessage(String message, String color, String prefix) {
		if (prefix == null) {
			getPlayer().sendMessage(color + Translation.getTranslation(message, getLanguage()));
		} else {
			if(prefix.equals("")) {
				getPlayer().sendMessage(color + Translation.getTranslation(message, getLanguage()));
			} else {
				getPlayer().sendMessage(prefix + color + " " + Translation.getTranslation(message, getLanguage()));
			}
		}
	}

	public void reset() {
		getPlayer().getInventory().clear();
		getPlayer().getInventory().setArmorContents(null);
		getPlayer().setLevel(0);
		getPlayer().setExp(0);
		getPlayer().setFallDistance(0);
		getPlayer().setFireTicks(0);
		getPlayer().setHealth(20);
		getPlayer().setMaxHealth(20);
		getPlayer().setFoodLevel(20);
		for (PotionEffect effect : player.getActivePotionEffects())
	        player.removePotionEffect(effect.getType());
	}

	public Scoreboard getBoard() {
		return board;
	}

	public void setBoard(Scoreboard board) {
		this.board = board;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Objective getObjective() {
		return objective;
	}

	public void setObjective(Objective objective) {
		this.objective = objective;
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}
	
	public void openSettings() {
		Inventory inv = Bukkit.createInventory(player, 9, Translation.getTranslation("general.settings", getLanguage()));
		
		ItemStack itemStack;
		
		itemStack = new ItemStack(Material.INK_SACK, 1, (short) (getSettings().hasAutoJoinEnabled() ? 10 : 8));
		itemStack = ItemUtil.setDisplayname(itemStack, "§f" + Translation.getTranslation("general.settings.auto_join", getLanguage()));
		itemStack = ItemUtil.addLore(itemStack, getSettings().hasAutoJoinEnabled() ? "§a<" 
									+ Translation.getTranslation("general.settings.enabled", getLanguage()) + ">" 
									: "§c<" + Translation.getTranslation("general.settings.disabled", getLanguage()) + ">");
		
		inv.addItem(itemStack);
		
		itemStack = new ItemStack(Material.INK_SACK, 1, (short) (getSettings().blockBadWords() ? 10 : 8));
		itemStack = ItemUtil.setDisplayname(itemStack, "§f" + Translation.getTranslation("general.settings.block_bad_words", getLanguage()));
		itemStack = ItemUtil.addLore(itemStack, getSettings().blockBadWords() ? "§a<" 
				+ Translation.getTranslation("general.settings.enabled", getLanguage()) + ">" 
				: "§8<" + Translation.getTranslation("general.settings.disabled", getLanguage()) + ">");
		
		inv.addItem(itemStack);
		
		itemStack = new ItemStack(Material.INK_SACK, 1, (short) (getSettings().showBlood() ? 10 : 8));
		itemStack = ItemUtil.setDisplayname(itemStack, "§f" + Translation.getTranslation("general.settings.show_blood", getLanguage()));
		itemStack = ItemUtil.addLore(itemStack, getSettings().showBlood() ? "§a<" 
				+ Translation.getTranslation("general.settings.enabled", getLanguage()) + ">" 
				: "§8<" + Translation.getTranslation("general.settings.disabled", getLanguage()) + ">");
		
		inv.addItem(itemStack);
		
		itemStack = new ItemStack(Material.INK_SACK, 1, (short) (getSettings().hasScoreboardEnabled() ? 10 : 8));
		itemStack = ItemUtil.setDisplayname(itemStack, "§f" + Translation.getTranslation("general.settings.enable_scoreboard", getLanguage()));
		itemStack = ItemUtil.addLore(itemStack, getSettings().hasScoreboardEnabled() ? "§a<" 
				+ Translation.getTranslation("general.settings.enabled", getLanguage()) + ">" 
				: "§8<" + Translation.getTranslation("general.settings.disabled", getLanguage()) + ">");
		
		inv.addItem(itemStack);
		
		itemStack = new ItemStack(Material.INK_SACK, 1, (short) (getSettings().attNotification() ? 10 : 8));
		itemStack = ItemUtil.setDisplayname(itemStack, "§f" + Translation.getTranslation("general.settings.att_notification", getLanguage()));
		itemStack = ItemUtil.addLore(itemStack, getSettings().attNotification() ? "§a<" 
				+ Translation.getTranslation("general.settings.enabled", getLanguage()) + ">" 
				: "§8<" + Translation.getTranslation("general.settings.disabled", getLanguage()) + ">");
		
		inv.addItem(itemStack);
		
		itemStack = new ItemStack(Material.INK_SACK, 1, (short) (getSettings().useSimpleCrafting() ? 10 : 8));
		itemStack = ItemUtil.setDisplayname(itemStack, "§f" + Translation.getTranslation("general.settings.simple_crafting", getLanguage()));
		itemStack = ItemUtil.addLore(itemStack, getSettings().useSimpleCrafting() ? "§a<" 
				+ Translation.getTranslation("general.settings.enabled", getLanguage()) + ">" 
				: "§8<" + Translation.getTranslation("general.settings.disabled", getLanguage()) + ">");
		
		inv.addItem(itemStack);
		
		itemStack = new ItemStack(Material.INK_SACK, 1, (short) (getSettings().autoRespawn() ? 10 : 8));
		itemStack = ItemUtil.setDisplayname(itemStack, "§f" + Translation.getTranslation("general.settings.auto_respawn", getLanguage()));
		itemStack = ItemUtil.addLore(itemStack, getSettings().autoRespawn() ? "§a<" 
				+ Translation.getTranslation("general.settings.enabled", getLanguage()) + ">" 
				: "§8<" + Translation.getTranslation("general.settings.disabled", getLanguage()) + ">");
		
		inv.addItem(itemStack);
		
		itemStack = new ItemStack(Material.INK_SACK, 1, (short) (getSettings().showStars() ? 10 : 8));
		itemStack = ItemUtil.setDisplayname(itemStack, "§f" + Translation.getTranslation("general.settings.show_stars", getLanguage()));
		
		itemStack = ItemUtil.addLore(itemStack, getSettings().showStars() ? "§a<" 
				+ Translation.getTranslation("general.settings.enabled", getLanguage()) + ">" 
				: "§8<" + Translation.getTranslation("general.settings.disabled", getLanguage()) + ">");
		
		itemStack = ItemUtil.addLore(itemStack, "§7");
		itemStack = ItemUtil.addLore(itemStack, "§7✯  §7= 1 " + Translation.getTranslation("general.win", getLanguage()));
		itemStack = ItemUtil.addLore(itemStack, "§6✯  §7= 5 " + Translation.getTranslation("general.wins", getLanguage()));
		itemStack = ItemUtil.addLore(itemStack, "§b✯  §7= 25 " + Translation.getTranslation("general.wins", getLanguage()));
		itemStack = ItemUtil.addLore(itemStack, "§f✯  §7= 125 " + Translation.getTranslation("general.wins", getLanguage()));
		itemStack = ItemUtil.addLore(itemStack, "§d✯  §7= 625 " + Translation.getTranslation("general.wins", getLanguage()));
		
		inv.addItem(itemStack);
		
		player.openInventory(inv);
	}
	
	@SuppressWarnings("deprecation")
	public boolean setLanguage(Language language) {		
		LanguageChangeEvent languageChangeEvent = new LanguageChangeEvent(this, language);
		MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(languageChangeEvent);
		
		if(!languageChangeEvent.isCancelled()) {
			settings.setLanguage(language);
		}
		
		return !languageChangeEvent.isCancelled();
	}
	
	@SuppressWarnings("deprecation")
	public Language getLanguage() {
		return settings.getLanguage();
	}
	
	public void send(String server) {
		ServerUtil.connect(player, server);
	}
	
	public void sendToLobby() {
		ServerUtil.connect(player, "lobby");
	}
	
	public String getStars() {
		int wins = statistics.getWins();
		String stars = "";
		
		if(wins < 5) {
			for(int i = 0; i<wins; i++) {
				stars += "§7✭";
			}
		} else if(wins < 25) {
			for(int i = 0; i<wins+1; i++) {
				if(i % 5 == 0 && i != 0) {
					stars += "§6✭";
				}
			}
		} else if(wins < 125) {
			for(int i = 0; i<wins+1; i++) {
				if(i % 25 == 0 && i != 0) {
					stars += "§b✭";
				}
			}
		} else if(wins < 625) {
			for(int i = 0; i<wins+1; i++) {
				if(i % 125 == 0 && i != 0) {
					stars += "§f✭";
				}
			}
		} else if(wins < 3125) {
			for(int i = 0; i<wins+1; i++) {
				if(i % 625 == 0 && i != 0) {
					stars += "§d✭";
				}
			}
		}
		
		return stars;
	}
}
