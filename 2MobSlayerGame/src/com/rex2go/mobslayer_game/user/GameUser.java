package com.rex2go.mobslayer_game.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.Title;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.BountifulAPI;
import com.rex2go.mobslayer_core.util.ItemUtil;
import com.rex2go.mobslayer_core.util.MathUtil;
import com.rex2go.mobslayer_core.util.MySQL;
import com.rex2go.mobslayer_core.util.ParticleUtil;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.crafting.Craft;
import com.rex2go.mobslayer_game.event.LobbyBlockToggleEvent;
import com.rex2go.mobslayer_game.item.GameItem;
import com.rex2go.mobslayer_game.item.GameItem.Attribute;
import com.rex2go.mobslayer_game.item.Item;
import com.rex2go.mobslayer_game.item.ShopItem;
import com.rex2go.mobslayer_game.listener.PlayerDeathListener;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.task.Task;
import com.rex2go.mobslayer_game.vote.Vote;

import net.minecraft.server.v1_8_R3.EnumParticle;

public class GameUser extends User implements Cloneable {

	// ingame
	
	int kills = 0;
	int deaths = 0;
	int inGamePoints = 0;
	int inGameCoins = 0;
	int revives = 0;
	
	int waveKills = 0;
	
	Location deathLocation = null;
	
	int noDamageSeconds = 0;

	Vote vote;
	
	boolean skipWave;

	ArrayList<GameItem> crafting = new ArrayList<>();
	ArrayList<String> learnedCrafts = new ArrayList<>();
	ArrayList<GameItem> itemQue = new ArrayList<>();

	boolean spectating = false;
	boolean blocking = false;
	
	Objective healthObjective = getBoard().registerNewObjective("showhealth", "health");

	public GameUser(User user) {
		super(user.getPlayer(), user.getPoints(), user.getCoins(), user.getRank(), user.getSettings(),
				user.getStatistics());
	}
	
	public ArrayList<GameItem> getItemQue() {
		return itemQue;
	}
	
	@SuppressWarnings("unchecked")
	public void loadGameData() {
		ResultSet rs;
		boolean f = false;
		rs = MySQL.query("SELECT data FROM mobslayer_user_game_data WHERE uuid = '" + getPlayer().getUniqueId().toString() + "'");
		
		try {
			while (rs.next()) {
				f = true;
				HashMap<String, Object> arr = new Gson().fromJson(rs.getString("data"),new TypeToken<HashMap<String, Object>>() {}.getType());
				
				if(arr.containsKey("learnedCrafts")) {
					ArrayList<String> args = (ArrayList<String>) arr.get("learnedCrafts");
					
					for(String craft : args) {
						learnedCrafts.add(craft);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(!f) {
			JSONObject obj = new JSONObject();
			JSONArray crafts = new JSONArray();
			
			for(Craft craft : Craft.values()) {
				if(craft.getAttributes() != null) {
					for(Attribute attr : craft.getCraftRecipe().getResult().getAttributes()) {
						if(attr == Attribute.NATIVE) {
							learnedCrafts.add(craft.name());
							crafts.add(craft.name());
						}
					}
				}
			}
			
			obj.put("learnedCrafts", crafts);
			
			MySQL.update("INSERT INTO mobslayer_user_game_data (uuid, data) VALUES ('" + getPlayer().getUniqueId().toString() + "', '" + obj.toJSONString() + "')");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void saveGameData() {
		if(!getLearnedCrafts().isEmpty()) {
			JSONObject obj = new JSONObject();
			JSONArray crafts = new JSONArray();
			
			for(String s : getLearnedCrafts()) {
				crafts.add(s);
			}
			
			obj.put("learnedCrafts", crafts);
			
			MySQL.update("UPDATE mobslayer_user_game_data SET data = '" + obj.toJSONString() + "' WHERE uuid = '" + getPlayer().getUniqueId().toString() + "'");
		}
	}
	
	public ArrayList<String> getLearnedCrafts() {
		return learnedCrafts;
	}
	
	public boolean knowCraft(Craft craft) {
		if(!MobSlayerGame.LEARN_CRAFT) {
		    return true;
        }
		if(craft.getCraftRecipe().getResult().hasAttribute(Attribute.NATIVE)) {
			return true;
		}
		
		return getLearnedCrafts().contains(craft.name());
	}
	
	public Title learnCraft(Craft craft) {
		return learnCraft(craft, true);
	}
	
	public Title learnCraft(Craft craft, boolean message) {
		if(!knowCraft(craft)) {
			if(craft.getTitle() != null) {
				if(getPoints() < craft.getTitle().getPoints()) {
					return craft.getTitle();
				}
			}
			getLearnedCrafts().add(craft.name());
			
			if(message) {
				getPlayer().playSound(getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
				String trans = Translation.getTranslation(craft.getCraftRecipe().getResult()
						.getTranslationPath(), getLanguage());
				
				if(trans != null) {
					getPlayer().sendMessage("§7" + Translation.getTranslation("general.craft.learned_new_recipe", getLanguage()) + ": §6" + Translation.getTranslation(craft.getCraftRecipe().getResult()
							.getTranslationPath(), getLanguage()));
				} else {
					getPlayer().sendMessage("§7" + Translation.getTranslation("general.craft.learned_new_recipe", getLanguage()) + ".");
				}
			}
			
			reloadGameInventory();
		}
		return null;
	}
	
	public void revive(Location location) {
		setRevives(getRevives()+1);
		
		BountifulAPI.clearTitle(getPlayer());
		
		setNoDamageSeconds(5);
		
		if(!MobSlayerGame.getGameManager().alive.contains(this)) {
			MobSlayerGame.getGameManager().alive.add(this);
		}
		
		getPlayer().setGameMode(GameMode.SURVIVAL);
		getPlayer().closeInventory();
		
		setSpectating(false);
		
		if(PlayerDeathListener.inventory.containsKey(getPlayer().getUniqueId().toString())) {
			getPlayer().getInventory().setContents(PlayerDeathListener.inventory.get(getPlayer().getUniqueId().toString()));
			PlayerDeathListener.inventory.remove(getPlayer().getUniqueId().toString());
		}
		
		if(PlayerDeathListener.inventoryArmor.containsKey(getPlayer().getUniqueId().toString())) {
			getPlayer().getInventory().setArmorContents(PlayerDeathListener.inventoryArmor.get(getPlayer().getUniqueId().toString()));
			PlayerDeathListener.inventoryArmor.remove(getPlayer().getUniqueId().toString());
		}
		
		for(GameItem gameItem : itemQue) {
			getPlayer().getInventory().addItem(gameItem.toItemStack(this));
			
			getPlayer().playSound(getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
			getPlayer().sendMessage("§7" + Translation.getTranslation("general.wave_script.obtain_item", getLanguage()) + ": §6" + Translation.getTranslation(gameItem.getTranslationPath(), 
					getLanguage()));
		}
		
		itemQue.clear();
		
		giveTracker();
		
		getPlayer().playSound(getPlayer().getLocation(), Sound.ZOMBIE_REMEDY, 1, 1);
		
		getPlayer().teleport(location);
		
		ParticleUtil.circle(EnumParticle.FIREWORKS_SPARK, getPlayer().getLocation().clone().add(0, 0.5, 0), 35, 0.5f);
		ParticleUtil.circle(EnumParticle.FIREWORKS_SPARK, getPlayer().getLocation().clone().add(0, 1, 0), 35, 0.5f);
		
		showHealthScoreboard();
		
		for(User user : MobSlayerCore.getUserManager().getStorage()) {
			GameUser gameUser = (GameUser) user;
			gameUser.updateScoreboard();
		}
	}
	
	public void giveTracker() {
		if(!MobSlayerGame.getGameEntityManager().getStorage().isEmpty()) {
			if(MobSlayerGame.getWaveManager().getActiveWave().isStarted()) {
				if(MobSlayerGame.getWaveManager().getActiveWave().isSkippable()) {
					if(!isDead()) {
						for(ItemStack itemStack : getPlayer().getInventory().getContents()) {
							if(itemStack != null) {
								if(GameItem.fromItemStack(itemStack) != null) {
									if(GameItem.fromItemStack(itemStack) == Item.MOB_TRACKER) {
										return;
									}
								}
							}
						}
						
						getPlayer().getInventory().addItem(Item.MOB_TRACKER.toItemStack(this));
					}
				}
			}
		}
	}
	
	public void removeTracker() {
		ArrayList<ItemStack> newContents = new ArrayList<>();
		
		for(ItemStack itemStack : getPlayer().getInventory().getContents()) {
			if(itemStack != null) {
				if(itemStack.getType() != Material.COMPASS) {
					newContents.add(itemStack);
				} else {
					newContents.add(null);
				}
			} else {
				newContents.add(null);
			}
		}
		
		getPlayer().getInventory().setContents(newContents.toArray(new ItemStack[0]));
	}
	
	public void updateHealthScoreboard() {		
//		for(GameUser gameUser : MobSlayerGame.getGameManager().alive) {
//			gameUser.getPlayer().setHealth(getPlayer().getHealth());
//		}
	}
	
	public void showHealthScoreboard() {
		getHealthObjective().setDisplaySlot(DisplaySlot.BELOW_NAME);
		getHealthObjective().setDisplayName("HP");
		
		getPlayer().setScoreboard(getBoard());
		
		updateHealthScoreboard();
	}
	
	public Objective getHealthObjective() {
		return healthObjective;
	}

	public void setHealthObjective(Objective healthObjective) {
		this.healthObjective = healthObjective;
	}
	
	public void updateScoreboard() {
		if(getSettings().hasScoreboardEnabled()) {
			if (MobSlayerGame.getGameManager().getGameState() != GameState.INGAME) {
				try {
					getObjective().unregister();
				} catch (Exception e) {}
				setObjective(getBoard().registerNewObjective("test", "dummy"));
				getObjective().setDisplaySlot(DisplaySlot.SIDEBAR);
				getObjective().setDisplayName("§8» §6§l" + Task.title1 + " §8«");
				setScore(getObjective().getScore("   \u0020"));
				getScore().setScore(11);
				setScore(getObjective().getScore("§7Map"));
				getScore().setScore(10);
				if (MobSlayerGame.getVoteManager().isAllowVote() && MobSlayerGame.getMapManager().getGameMap() == null) {
					setScore(getObjective().getScore("§f---"));
				} else {
					setScore(getObjective()
							.getScore("§f" + MobSlayerGame.getMapManager().getGameMap().getMapName()));
				}
				getScore().setScore(9);
				setScore(getObjective().getScore("  \u0020"));
				getScore().setScore(8);
				setScore(getObjective().getScore("§7" + Translation.getTranslation("game.general.points", getLanguage())));
				getScore().setScore(7);
				setScore(getObjective().getScore("§f" + getPoints()));
				getScore().setScore(6);
				setScore(getObjective().getScore(" \u0020"));
				getScore().setScore(5);
				setScore(getObjective().getScore("§7Coins"));
				getScore().setScore(4);
				setScore(getObjective().getScore("§f" + getCoins()));
				getScore().setScore(3);
				setScore(getObjective().getScore("\u0020"));
				getScore().setScore(2);
				setScore(getObjective().getScore("§cmbslyr.net"));
				getScore().setScore(1);
				getPlayer().setScoreboard(getBoard());
			} else {
				try {
					getObjective().unregister();
				} catch (Exception e) {}
				setObjective(getBoard().registerNewObjective("test", "dummy"));
				getObjective().setDisplaySlot(DisplaySlot.SIDEBAR);
				getObjective().setDisplayName("§8» §6§l" + Task.title1 + " §8«");
				setScore(getObjective().getScore("  \u0020"));
				getScore().setScore(8);
				if(MobSlayerGame.getWaveManager().getActiveWave() != null) {
					if(MobSlayerGame.getWaveManager().isBossWave(MobSlayerGame.getWaveManager().getActiveWave())) {
						setScore(getObjective().getScore("§7" + Translation.getTranslation("game.general.wave", getLanguage()) 
						+ " §c§lBOSS"));
					} else {
						setScore(getObjective().getScore("§7" + Translation.getTranslation("game.general.wave", getLanguage()) 
								+ " " + MobSlayerGame.getWaveManager().getActiveWave().getWaveLevel()));
					}
				} else {
					// Spiel zu ende
				}
				getScore().setScore(7);
				
				int add = 0;
				if(MobSlayerGame.getWaveManager().getActiveWave() != null) {
					add = MobSlayerGame.getWaveManager().getActiveWave().getSpawnRequests().size();
					
					if(MobSlayerGame.getWaveManager().getActiveWave().getPrepareTime() != 0) {
						setScore(getObjective().getScore("§f" + Translation.getTranslation("game.general.warmup", getLanguage())));
					} else {
						int enemies = MobSlayerGame.getGameEntityManager().getStorage().size() + add;
						
						if(enemies != 1) {
							setScore(getObjective().getScore("§f" + enemies  + " " + Translation.getTranslation("game.general.enemies", getLanguage())));
						} else {
							setScore(getObjective().getScore("§f" + enemies  + " " + Translation.getTranslation("game.general.enemy", getLanguage())));
						}
					}
				}
				
				getScore().setScore(6);
				setScore(getObjective().getScore(" \u0020"));
				getScore().setScore(5);
				setScore(getObjective().getScore("§7" + Translation.getTranslation("game.general.alive", getLanguage())));
				getScore().setScore(4);
				if(MobSlayerGame.getGameManager().alive.size() != 1) {
					setScore(getObjective().getScore("§f" + MobSlayerGame.getGameManager().alive.size() + " " 
							+ Translation.getTranslation("game.general.players", getLanguage())));
				} else {
					setScore(getObjective().getScore("§f" + MobSlayerGame.getGameManager().alive.size() + " " 
							+ Translation.getTranslation("game.general.player", getLanguage())));
				}
				getScore().setScore(3);
				setScore(getObjective().getScore("\u0020"));
				getScore().setScore(2);
				setScore(getObjective().getScore("§cmbslyr.net"));
				getScore().setScore(1);
				getPlayer().setScoreboard(getBoard());
			}
		} else {
			hideScoreboard();
		}
	}
	
	public void hideScoreboard() {
		try {
			getObjective().unregister();
		} catch (Exception e) {}
	}
	
	public boolean isSpectating() {
		return spectating;
	}
	
	public boolean isBlocking() {
		return blocking;
	}
	
	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
		MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(new LobbyBlockToggleEvent(this));
	}
	
	public ArrayList<GameItem> getCrafting() {
		return crafting;
	}

	public void setCrafting(ArrayList<GameItem> crafting) {
		this.crafting = crafting;
	}

	public boolean isDead() {
		return !MobSlayerGame.getGameManager().alive.contains(this);
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public Vote getVote() {
		return vote;
	}

	public void setVote(Vote vote) {
		this.vote = vote;
	}
	
	public int getInGamePoints() {
		return inGamePoints;
	}

	public void setInGamePoints(int inGamePoints) {
		this.inGamePoints = inGamePoints;
	}
	
	public void loadLobbyItems() {
		ItemStack itemStack;

		itemStack = new ItemStack(Material.IRON_SWORD);
		itemStack = ItemUtil.hideFlags(itemStack);
		itemStack = ItemUtil.setDisplayname(itemStack,
				"§6" + Translation.getTranslation("game.lobby.preview", getLanguage()));
		itemStack = ItemUtil.addLore(itemStack,
				"§7§o<" + Translation.getTranslation("game.general.right_click", getLanguage()) + ">");
		itemStack = ItemUtil.setUnbreakable(itemStack);
		itemStack = ItemUtil.hideFlags(itemStack);

		getPlayer().getInventory().setItem(0, itemStack);

		itemStack = new ItemStack(Material.BOOK);
		itemStack = ItemUtil.hideFlags(itemStack);
		itemStack = ItemUtil.setDisplayname(itemStack,
				"§6" + Translation.getTranslation("game.lobby.voting", getLanguage()));
		itemStack = ItemUtil.addLore(itemStack,
				"§7§o<" + Translation.getTranslation("game.general.right_click", getLanguage()) + ">");

		getPlayer().getInventory().setItem(1, itemStack);

		itemStack = new ItemStack(Material.NAME_TAG);
		itemStack = ItemUtil.hideFlags(itemStack);
		itemStack = ItemUtil.setDisplayname(itemStack,
				"§6" + Translation.getTranslation("game.lobby.customization", getLanguage()));
		itemStack = ItemUtil.addLore(itemStack,
				"§7§o<" + Translation.getTranslation("game.general.right_click", getLanguage()) + ">");

		getPlayer().getInventory().setItem(7, itemStack);

		itemStack = new ItemStack(Material.REDSTONE_COMPARATOR);
		itemStack = ItemUtil.hideFlags(itemStack);
		itemStack = ItemUtil.setDisplayname(itemStack,
				"§6" + Translation.getTranslation("game.lobby.settings", getLanguage()));
		itemStack = ItemUtil.addLore(itemStack,
				"§7§o<" + Translation.getTranslation("game.general.right_click", getLanguage()) + ">");

		getPlayer().getInventory().setItem(8, itemStack);
	}
	
	public void loadEquip() {
		ItemStack itemStack;
		
		itemStack = Item.STONE_SWORD.toItemStack(this);
		
		getPlayer().getInventory().setItem(0, itemStack);
		
		itemStack = Item.RECIPE_BOOK.toItemStack(this);
		
		getPlayer().getInventory().setItem(7, itemStack);
		
		itemStack = Item.WAND.toItemStack(this);
		
		getPlayer().getInventory().setItem(8, itemStack);
		
		loadArmor();
	}
	
	public void loadArmor() {
		ItemStack itemStack;
		LeatherArmorMeta  armorMeta;
		
		itemStack = Item.LEATHER_HELMET.toItemStack(this);
		if(getSettings().getArmorColors().get(0) != null) {
			armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
			armorMeta.setColor(getSettings().getArmorColors().get(0));
			itemStack.setItemMeta(armorMeta);
		}
		
		getPlayer().getInventory().setHelmet(itemStack);
		
		itemStack = Item.LEATHER_CHESTPLATE.toItemStack(this);
		if(getSettings().getArmorColors().get(1) != null) {
			armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
			armorMeta.setColor(getSettings().getArmorColors().get(1));
			itemStack.setItemMeta(armorMeta);
		}
		
		getPlayer().getInventory().setChestplate(itemStack);
		
		itemStack = Item.LEATHER_LEGGINGS.toItemStack(this);
		itemStack = ItemUtil.setUnbreakable(itemStack);
		if(getSettings().getArmorColors().get(2) != null) {
			armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
			armorMeta.setColor(getSettings().getArmorColors().get(2));
			itemStack.setItemMeta(armorMeta);
		}
		
		getPlayer().getInventory().setLeggings(itemStack);
		
		itemStack = Item.LEATHER_BOOTS.toItemStack(this);
		if(getSettings().getArmorColors().get(3) != null) {
			armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
			armorMeta.setColor(getSettings().getArmorColors().get(3));
			itemStack.setItemMeta(armorMeta);
		}
		
		getPlayer().getInventory().setBoots(itemStack);
	}
	
	public void openVoteMenu() {
		int size = 9;
		while(MobSlayerGame.getVoteManager().getAvailableVotes().size() > size) {
			size += 9;
		}
		
		Inventory inv = Bukkit.createInventory(getPlayer(), size, Translation.getTranslation("game.lobby.voting", getLanguage()));
		
		for(Vote vote : MobSlayerGame.getVoteManager().getAvailableVotes()) {
			ItemStack itemStack = new ItemStack(Material.PAPER);
			
			if(vote.getVotes() == 1) {
				itemStack = ItemUtil.setDisplayname(itemStack, "§6" + vote.getMapName() + " §e<" + vote.getVotes() + " " +
							Translation.getTranslation("game.lobby.vote", getLanguage()) + ">");
			} else {
				itemStack = ItemUtil.setDisplayname(itemStack, "§6" + vote.getMapName() + " §e<" + vote.getVotes() + " " + 
						Translation.getTranslation("game.lobby.votes", getLanguage()) + ">");
			}
			
			itemStack = ItemUtil.addLore(itemStack, "§7" + Translation.getTranslation("game.general.by", getLanguage()) + " " 
						+ vote.getAuthor());
			
			inv.addItem(itemStack);
		}
		
		getPlayer().openInventory(inv);
	}
	
	public void openCustomizationMenu() {
		Inventory inv = Bukkit.createInventory(getPlayer(), 9, Translation.getTranslation("game.lobby.customization.choose_an_armor_part", getLanguage()));
		
		ItemStack itemStack;
		LeatherArmorMeta armorMeta;
		
		itemStack = new ItemStack(Material.LEATHER_HELMET);
		armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
		if(getSettings().getArmorColors().get(0) != null) {
			armorMeta.setColor(getSettings().getArmorColors().get(0));
		}
		itemStack.setItemMeta(armorMeta);
		
		inv.setItem(2, itemStack);
		
		itemStack = new ItemStack(Material.LEATHER_CHESTPLATE);
		armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
		if(getSettings().getArmorColors().get(1) != null) {
			armorMeta.setColor(getSettings().getArmorColors().get(1));
		}
		itemStack.setItemMeta(armorMeta);
		
		inv.setItem(3, itemStack);
		
		itemStack = new ItemStack(Material.LEATHER_LEGGINGS);
		armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
		if(getSettings().getArmorColors().get(2) != null) {
			armorMeta.setColor(getSettings().getArmorColors().get(2));
		}
		itemStack.setItemMeta(armorMeta);
		
		inv.setItem(5, itemStack);
		
		itemStack = new ItemStack(Material.LEATHER_BOOTS);
		armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
		if(getSettings().getArmorColors().get(3) != null) {
			armorMeta.setColor(getSettings().getArmorColors().get(3));
		}
		itemStack.setItemMeta(armorMeta);
		
		inv.setItem(6, itemStack);
		
		getPlayer().openInventory(inv);
	}
	
	public void openCustomizationMenu(ItemStack itemStack1) {
		Inventory inv = Bukkit.createInventory(getPlayer(), 9, Translation.getTranslation("game.lobby.customization.choose_a_color", getLanguage()));
		
		ItemStack itemStack;
		LeatherArmorMeta armorMeta;
			
		itemStack = new ItemStack(Material.GLASS);
		itemStack = ItemUtil.setDisplayname(itemStack, "§f" + Translation.getTranslation("game.lobby.customization.default_color", getLanguage()));
		inv.setItem(0, itemStack);
			
		int slot = 2;
				
		for(ArmorColor armorColor : MobSlayerGame.ARMOR_COLORS) {
			if(getPoints() >= armorColor.getTitle().getPoints()) {
				itemStack = new ItemStack(itemStack1.getType());
				armorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
				armorMeta.setColor(armorColor.getColor());
				armorMeta.setDisplayName("§f" + Translation.getTranslation("game.lobby.customization.choose_this_color", getLanguage()));
				itemStack.setItemMeta(armorMeta);
				itemStack = ItemUtil.hideFlags(itemStack);
				itemStack = ItemUtil.clearLore(itemStack);
				
				if(armorColor.getTitle() == getTitle() && armorColor.getTitle().getPoints() > Title.SOLDIER.getPoints() && getPoints() - armorColor.getTitle().getPoints() < 2500) {
					itemStack = ItemUtil.addLore(itemStack, "§d" + Translation.getTranslation("game.lobby.customization.recently_unlocked", getLanguage()));
				}
				
				inv.setItem(slot, itemStack);
				slot++;
			} else {
				itemStack = new ItemStack(Material.BARRIER);
				itemStack = ItemUtil.setDisplayname(itemStack, "§4" + Translation.getTranslation("game.lobby.customization.locked", getLanguage()) + " §f(" 
						+ Translation.getTranslation(armorColor.getTitle().getTranslationPath(), getLanguage()).replaceAll("§4", "") + ")");
				itemStack = ItemUtil.hideFlags(itemStack);
				itemStack = ItemUtil.clearLore(itemStack);
				
				double process = MathUtil.round((double) getPoints() / armorColor.getTitle().getPoints() * 100F, 2);
				
				itemStack = ItemUtil.addLore(itemStack, "§7" + Translation.getTranslation("game.lobby.customization.progress", getLanguage()) + ": " + process + "%");
				
				inv.setItem(slot, itemStack);
				slot++;
			}
		}
			
		getPlayer().openInventory(inv);
	}
	
	public void openSpectatorMenu() {
		int size = 9;
		while(MobSlayerGame.getGameManager().alive.size() > size) {
			size += 9;
		}
		
		Inventory inv = Bukkit.createInventory(getPlayer(), size, Translation.getTranslation("game.spectate.select_a_player", getLanguage()));
		
		for(GameUser user : MobSlayerGame.getGameManager().alive) {
			
			ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
			SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
			meta.setOwner(user.getPlayer().getName());
			itemStack.setItemMeta(meta);
			
			itemStack = ItemUtil.setDisplayname(itemStack, "§f" + user.getPlayer().getName());
			
			inv.addItem(itemStack);
		}
		
		getPlayer().openInventory(inv);
	}
	
	public void loadSpectatorItems() {
		getPlayer().getInventory().clear();
		
		ItemStack itemStack;
		
		itemStack = new ItemStack(Material.NETHER_STAR);
		itemStack = ItemUtil.setDisplayname(itemStack, "§6" + Translation.getTranslation("game.dead.switch_to_spectator_mode", getLanguage()));
		
		getPlayer().getInventory().setItem(4, itemStack);
	}
	
	public void openShopMenu() {
		int size = 9;
		
		while(ShopItem.getShopItems().size() > size) {
			size += 9;
		}
		
		Inventory inv = Bukkit.createInventory(getPlayer(), size, Translation.getTranslation("game.shop", getLanguage()));
		
		for(ShopItem shopItem : ShopItem.getShopItems()) {
			GameItem gameItem = shopItem.getGameItem();
			ItemStack itemStack = gameItem.toItemStack(this);
			ItemMeta itemMeta = itemStack.getItemMeta();
			
			ArrayList<String> lore = new ArrayList<>();
			
			lore.add("§7" + Translation.getTranslation("game.shop.price", getLanguage()) + ": §b" + shopItem.getPrice() + " Coins");
			lore.add("§7" + Translation.getTranslation("game.shop.sell_value", getLanguage()) + ": §b" + shopItem.getSellValue() + " Coins");
			
			itemMeta.setLore(lore);
			itemStack.setItemMeta(itemMeta);
			
			inv.addItem(itemStack);
		}
		
		getPlayer().openInventory(inv);
	}
	
	public void setSpectating(boolean spectate) {
		if(spectating != spectate) {
			if(spectate) {
				getPlayer().setAllowFlight(true);
				getPlayer().setFlying(true);
				
				getPlayer().spigot().setCollidesWithEntities(false);
				
				for(Player all : Bukkit.getOnlinePlayers()) {
					GameUser gameUser = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(all.getUniqueId());
					
					if(!MobSlayerGame.getGameManager().isSpectator(gameUser)) {
						all.hidePlayer(getPlayer());
					}
				}
				
			} else {
				getPlayer().setAllowFlight(false);
				getPlayer().setFlying(false);
			
				getPlayer().spigot().setCollidesWithEntities(true);
				
				for(Player all : Bukkit.getOnlinePlayers()) {
					all.showPlayer(getPlayer());
				}
			}
		}
		
		this.spectating = spectate;
	}
	
	public void damage(double damage) {
		if(!isSpectating()) {
			getPlayer().damage(damage);
		}
	}
	
	public boolean skipWave() {
		return skipWave;
	}

	public void setSkipWave(boolean skipWave) {
		this.skipWave = skipWave;
	}
	
	public int getInGameCoins() {
		return inGameCoins;
	}

	public void setInGameCoins(int inGameCoins) {
		this.inGameCoins = inGameCoins;
	}

	public int getRevives() {
		return revives;
	}

	public void setRevives(int revives) {
		this.revives = revives;
	}
	
	public double getInGameKD() {
		if(deaths == 0) {
			return MathUtil.round((kills), 2);
		}
		return MathUtil.round((((double)kills)/((double)deaths)), 2);
	}
	
	public void heal(double val, boolean particle) {
		double heal = getPlayer().getHealth() + val < getPlayer().getMaxHealth() ? getPlayer().getHealth() + val : getPlayer().getMaxHealth();
		getPlayer().setHealth(heal);
		
		if(particle) {
			ParticleUtil.circle(EnumParticle.HEART, getPlayer().getEyeLocation(), 5, 0.5F);
		}
	}
	
	public int getNoDamageSeconds() {
		return noDamageSeconds;
	}

	public void setNoDamageSeconds(int noDamageSeconds) {
		this.noDamageSeconds = noDamageSeconds;
	}
	
	public GameUser clone() {
	    GameUser clone = null;
		try {
			clone = (GameUser)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	   	return clone;
	}
	
	public Location getDeathLocation() {
		return deathLocation;
	}
	
	public void setDeathLocation(Location loc) {
		this.deathLocation = loc;
	}
	
	public void openRecipeBook() {
		ArrayList<GameItem> crafts = new ArrayList<>();
		
		for(Craft craft : Craft.values()) {
			if(knowCraft(craft)) {
				if(!crafts.contains(craft.getCraftRecipe().getResult())) {
					crafts.add(craft.getCraftRecipe().getResult());
				}
			}
		}
		
		int size = 9;
		while(crafts.size() > size) {
			size += 9;
		}
		
		Inventory inv = Bukkit.createInventory(getPlayer(), size, Translation.getTranslation("game.general.recipe_book", getLanguage()));
		
		for(GameItem gameItem : crafts) {
			ItemStack itemStack = gameItem.toItemStack(this);
			itemStack = ItemUtil.addLore(itemStack, "§e" + Translation.getTranslation("game.craft.click_to_show_recipe", getLanguage()));
			
			inv.addItem(itemStack);
		}
		
		getPlayer().openInventory(inv);
	}
	
	@SuppressWarnings("static-access")
	public void reloadGameInventory() {
		if(MobSlayerGame.getGameManager().getGameState() == GameState.INGAME) {
			for(int i = 0; i<getPlayer().getInventory().getSize(); i++) {
				ItemStack itemStack = getPlayer().getInventory().getItem(i);
				GameItem gameItem = GameItem.fromItemStack(itemStack);
				
				if(gameItem != null) {
					gameItem.translate(itemStack, this);
				}
			}
		}
	}
	
	public void openRecipe(GameItem gameItem) {
		ArrayList<Craft> crafts = Craft.getCraft(gameItem);
		ArrayList<Craft> learnedCrafts = new ArrayList<>();
		
		if(gameItem == null) {
			return;
		}
		
		for(Craft c : crafts){
			if(knowCraft(c)) {
				learnedCrafts.add(c);
			}
		}
		
		if(crafts.isEmpty() || learnedCrafts.isEmpty()) {
			return;
		}
		
		Inventory inv = Bukkit.createInventory(getPlayer(), learnedCrafts.size()*9, Translation.getTranslation("game.craft.recipe_display", getLanguage()));
		
		for(int i = 0; i<learnedCrafts.size(); i++) {
			int ii = 0;
			Craft craft = learnedCrafts.get(i);
			
			for(GameItem gameItem1 : craft.getCraftRecipe().getResources()) {
				inv.setItem(ii+i*9, gameItem1.clone().toItemStack(this));
				ii++;
			}
			
			ItemStack itemStack = new ItemStack(Material.CAULDRON_ITEM);
			itemStack = ItemUtil.setDisplayname(itemStack, "§f" + Translation.getTranslation("game.craft.use_cauldron", getLanguage()));
			
			inv.setItem(ii+i*9, itemStack);
			ii++;
			
			itemStack = Craft.setupItem(gameItem.clone(), craft, this, false);
			itemStack.setAmount(craft.getAmount());
			
			inv.setItem(ii+i*9, itemStack);
			ii++;
		}
		
		ItemStack itemStack = new ItemStack(Material.BARRIER);
		itemStack = ItemUtil.setDisplayname(itemStack, "§f" + Translation.getTranslation("game.craft.go_back", getLanguage()));
		
		inv.setItem(8, itemStack);
		
		getPlayer().openInventory(inv);
	}
	
	public void setWaveKills(int kills) {
		this.waveKills = kills;
	}
	
	public int getWaveKills() {
		return waveKills;
	}
	
	public void openResearchMenu() {
		if(getTitle().getPoints() >= Title.RECRUIT.getPoints()) {
			Inventory inv = Bukkit.createInventory(getPlayer(), 9, Translation.getTranslation("game.research_table.title", getLanguage()));
			
			ItemStack itemStack = new ItemStack(Material.PAPER);
			itemStack = ItemUtil.setDisplayname(itemStack, "§7");
			
			inv.setItem(0, itemStack.clone());
			inv.setItem(1, itemStack.clone());
			inv.setItem(2, itemStack.clone());
			inv.setItem(6, itemStack.clone());
			inv.setItem(7, itemStack.clone());
			inv.setItem(8, itemStack.clone());
			
			itemStack = new ItemStack(Material.ENCHANTMENT_TABLE);
			itemStack = ItemUtil.setDisplayname(itemStack, "§f" + Translation.getTranslation("game.research_table.put_in_item", getLanguage()));
			
			inv.setItem(3, itemStack.clone());
			inv.setItem(5, itemStack.clone());
			
			getPlayer().openInventory(inv);
		} else {
			getPlayer().sendMessage("§c" + Translation.getTranslation("game.research_table.locked", getLanguage(), Translation.getTranslation(Title.RECRUIT.getTranslationPath(), getLanguage())));
			getPlayer().playSound(getPlayer().getLocation(), Sound.ZOMBIE_METAL, 1, 1);
			
			sendProgress();
		}
	}

	public void sendProgress() {
		if(getTitle() == getNextTitle()) {
			return;
		}
		sendProgress(getNextTitle(), false);
	}

	public Title getNextTitle() {
	    Title title = Title.LEGEND;

	    for(Title title1 : Title.values()) {
	        if(title1.getPoints() > getTitle().getPoints() && title1.getPoints() < title.getPoints()) {
	            title = title1;
            }
        }

        return title;
    }

	public void sendProgress(Title title, boolean fullRange) {	
		double progress = 0;
		
		if(fullRange) {
			progress = MathUtil.round((double) getPoints() / title.getPoints() * 100F, 2);
		} else {
			progress = MathUtil.round((double) (getPoints() - getTitle().getPoints()) / (title.getPoints() - getTitle().getPoints()) * 100F, 2);
		}
		
		String bar = "";
		
		if(progress < 100) {
			for(int i = 1; i<progress; i++) {
				if(i % 5 == 0) {
					if(i % 10 == 0) {
						bar += "§a#";
					}
				}
			}
			
			int l = bar.equals("") ? bar.split("#").length-1 : bar.split("#").length;
			if(progress - (l * 10) >= 5) {
				bar += "§e#";
			}
			
			while(bar.split("#").length < 10) {
				bar += "§7#";
			}
		} else {
			bar = "§a#########§7";
		}
		
		getPlayer().sendMessage("§a" + Translation.getTranslation("game.lobby.customization.progress", getLanguage()) + ": §f" + progress + "% §7" + bar + " §o(" 
				+ (fullRange ? getPoints() + "/" + title.getPoints() : (getPoints() - getTitle().getPoints()) + "/" + (title.getPoints() - getTitle().getPoints()) + ") §8» " 
						+ title.getColor() + Translation.getTranslation(title.getTranslationPath(), getLanguage())));
	}
	
	public int getFireProtection() {
		int lvl = 0;
		
		if(getPlayer().getInventory().getArmorContents() != null) {
			for(ItemStack itemStack : getPlayer().getInventory().getArmorContents()) {
				GameItem gameItem = GameItem.fromItemStack(itemStack);
				
				if(gameItem != null) {
					if(gameItem.hasAttribute(Attribute.ENCHANTMENT_FIRE_EXTINGUISH)) {
						lvl++;
					}
				}
			}
		}
		return lvl;
	}
	
}
