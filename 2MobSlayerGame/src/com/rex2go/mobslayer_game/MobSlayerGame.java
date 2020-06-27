package com.rex2go.mobslayer_game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.Title;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.MySQL;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.command.AirDropCommand;
import com.rex2go.mobslayer_game.command.AliveCommand;
import com.rex2go.mobslayer_game.command.FixCommand;
import com.rex2go.mobslayer_game.command.ForceSkipCommand;
import com.rex2go.mobslayer_game.command.GameStatisticsCommand;
import com.rex2go.mobslayer_game.command.GiveCommand;
import com.rex2go.mobslayer_game.command.ItemInfoCommand;
import com.rex2go.mobslayer_game.command.KillAllCommand;
import com.rex2go.mobslayer_game.command.LearnCraftComand;
import com.rex2go.mobslayer_game.command.ProgressCommand;
import com.rex2go.mobslayer_game.command.ReviveCommand;
import com.rex2go.mobslayer_game.command.SetMultiplierCommand;
import com.rex2go.mobslayer_game.command.SkipCommand;
import com.rex2go.mobslayer_game.command.SpawnRequestCommand;
import com.rex2go.mobslayer_game.command.StartCommand;
import com.rex2go.mobslayer_game.command.StartWaveCommand;
import com.rex2go.mobslayer_game.command.SummonCommand;
import com.rex2go.mobslayer_game.command.WaveActionCommand;
import com.rex2go.mobslayer_game.crafting.Craft;
import com.rex2go.mobslayer_game.crafting.CraftRecipe;
import com.rex2go.mobslayer_game.item.GameItem;
import com.rex2go.mobslayer_game.listener.BlockBreakListener;
import com.rex2go.mobslayer_game.listener.BlockPlaceListener;
import com.rex2go.mobslayer_game.listener.ChatListener;
import com.rex2go.mobslayer_game.listener.ConsumeListener;
import com.rex2go.mobslayer_game.listener.CreatureSpawnListener;
import com.rex2go.mobslayer_game.listener.DropListener;
import com.rex2go.mobslayer_game.listener.EntityCombustListener;
import com.rex2go.mobslayer_game.listener.EntityDamageByEntityListener;
import com.rex2go.mobslayer_game.listener.EntityDamageListener;
import com.rex2go.mobslayer_game.listener.EntityDeathListener;
import com.rex2go.mobslayer_game.listener.EntityExplodeListener;
import com.rex2go.mobslayer_game.listener.EntityTargetListener;
import com.rex2go.mobslayer_game.listener.FoodLevelChangeListener;
import com.rex2go.mobslayer_game.listener.InteractListener;
import com.rex2go.mobslayer_game.listener.InventoryClickListener;
import com.rex2go.mobslayer_game.listener.InventoryCloseListener;
import com.rex2go.mobslayer_game.listener.JoinListener;
import com.rex2go.mobslayer_game.listener.LanguageChangeListener;
import com.rex2go.mobslayer_game.listener.LoginListener;
import com.rex2go.mobslayer_game.listener.PickupListener;
import com.rex2go.mobslayer_game.listener.PlayerDeathListener;
import com.rex2go.mobslayer_game.listener.ProjectileHitListener;
import com.rex2go.mobslayer_game.listener.ProjectileLaunchListener;
import com.rex2go.mobslayer_game.listener.QuitListener;
import com.rex2go.mobslayer_game.listener.SlimeSplitListener;
import com.rex2go.mobslayer_game.listener.WeatherChangeListener;
import com.rex2go.mobslayer_game.listener.custom.GameEndListener;
import com.rex2go.mobslayer_game.listener.custom.GameStartListener;
import com.rex2go.mobslayer_game.listener.custom.LobbyBlockToggleListener;
import com.rex2go.mobslayer_game.listener.custom.PluginMessageReceiveListener;
import com.rex2go.mobslayer_game.listener.custom.PreWaveHandleListener;
import com.rex2go.mobslayer_game.listener.custom.TitleUpListener;
import com.rex2go.mobslayer_game.listener.custom.WaveDoneListener;
import com.rex2go.mobslayer_game.listener.custom.WaveHandleListener;
import com.rex2go.mobslayer_game.listener.custom.WaveLoadListener;
import com.rex2go.mobslayer_game.listener.custom.WaveStartListener;
import com.rex2go.mobslayer_game.manager.AirDropManager;
import com.rex2go.mobslayer_game.manager.GameEntityManager;
import com.rex2go.mobslayer_game.manager.GameItemManager;
import com.rex2go.mobslayer_game.manager.GameManager;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.manager.MapManager;
import com.rex2go.mobslayer_game.manager.SectionManager;
import com.rex2go.mobslayer_game.manager.VoteManager;
import com.rex2go.mobslayer_game.manager.WaveManager;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.mob.Packet;
import com.rex2go.mobslayer_game.task.Task;
import com.rex2go.mobslayer_game.user.ArmorColor;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.EntityChicken;

public class MobSlayerGame extends JavaPlugin {

    public static final int PLAYER_COUNT_REQUIRED = 1; // DEBUG
    public static final int SHORTEN_TIME_PLAYER_COUNT_REQUIRED = 4;
    public static final int MAX_PLAYERS = 12;
    public static final int SKIP_AFTER_TIME = 900;

    public static final boolean LEARN_CRAFT = false;
    public static final boolean RANDOM_SPAWN_REQUESTS = true;

    public static final int LOBBY_WAITING_TIME = 300;

    public static final ArmorColor[] ARMOR_COLORS = new ArmorColor[]{new ArmorColor(Color.RED, Title.BEGINNER), new ArmorColor(Color.GREEN, Title.BEGINNER), new ArmorColor(Color.BLUE,
            Title.BEGINNER), new ArmorColor(Color.ORANGE, Title.VETERAN), new ArmorColor(Color.PURPLE, Title.HUNTER), new ArmorColor(Color.WHITE, Title.MASTER),
            new ArmorColor(Color.BLACK, Title.LEGEND)};

    public static boolean setup = false;

    private static GameEntityManager gameEntityManager;
    private static GameManager gameManager;
    private static MapManager mapManager;
    private static VoteManager voteManager;
    private static SectionManager sectionManager;
    private static WaveManager waveManager;
    private static AirDropManager airDropManager;
    private static GameItemManager gameItemManager;

    public void onEnable() {
        setupTranslations();

        registerListener();
        registerCommands();
        registerManager();

        Task.startScoreboardTask();
        Task.startPreviewTask();

        MySQL.update("CREATE TABLE IF NOT EXISTS `mobslayer_mobs` ( `id` INT NOT NULL , `mob_name` VARCHAR(255) NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM;");
        MySQL.update("CREATE TABLE IF NOT EXISTS `mobslayer_user_game_data` ( `uuid` VARCHAR(255) NOT NULL , `data` LONGTEXT NOT NULL , PRIMARY KEY (`uuid`)) ENGINE = MyISAM;");

        ResultSet rs = MySQL.query("SELECT * FROM mobslayer_mobs");
        ArrayList<Integer> arr = new ArrayList<>();

        try {
            while (rs.next()) {
                arr.add(rs.getInt("id"));
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        for (GameEntityType gameEntityType : GameEntityType.values()) {
            if (!arr.contains(Integer.valueOf(gameEntityType.getId()))) {
                MySQL.update("INSERT INTO mobslayer_mobs (id, mob_name) VALUES (" + gameEntityType.getId() + ", '" + gameEntityType.getName() + "')");
            }
        }

        // Crafting deaktivieren

        Bukkit.getServer().clearRecipes();

        // Chicken disguise

        MobSlayerCore.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL,
                new PacketType[]{PacketType.Play.Server.SPAWN_ENTITY_LIVING}) {
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
                    PacketContainer packet = event.getPacket();
                    StructureModifier<Object> t = packet.getModifier();

                    int entityId = (int) t.getValues().get(0);
                    int entityTypeId = (int) t.getValues().get(1);

                    if (entityTypeId == 57) {
                        if (getGameManager().getGameState() == GameState.INGAME) {
                            for (Entity entity : MobSlayerGame.getMapManager().getGameMap().getWorld().getEntities()) {
                                if (entity.getType() == EntityType.PIG_ZOMBIE) {
                                    if (entityId == entity.getEntityId()) {
                                        if (getGameEntityManager().isChickenEntity(entity)) {
                                            event.setCancelled(true);
                                            Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(),
                                                    new Runnable() {

                                                        @Override
                                                        public void run() {
                                                            ((CraftPlayer) event.getPlayer())
                                                                    .getHandle().playerConnection.sendPacket(
                                                                    Packet.spawnEntityLiving(new EntityChicken(
                                                                                    ((CraftWorld) entity.getWorld())
                                                                                            .getHandle()),
                                                                            entity.getEntityId()));

                                                        }
                                                    }, 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void onDisable() {
        getGameEntityManager().removeAll();

        getMapManager().unloadAllMaps();

        for (Player all : Bukkit.getOnlinePlayers()) {
            User user = MobSlayerCore.getUserManager().getUserByUUID(all.getUniqueId());

            if (user != null) {
                GameUser gameUser = (GameUser) user;
                gameUser.saveGameData();
                user.sendToLobby();
            } else {
                all.kickPlayer("");
            }
        }
    }

    public void registerManager() {
        gameManager = new GameManager();
        mapManager = new MapManager();
        voteManager = new VoteManager();
        gameEntityManager = new GameEntityManager();
        sectionManager = new SectionManager();
        waveManager = new WaveManager();
        airDropManager = new AirDropManager();
        gameItemManager = new GameItemManager();
    }

    public void registerListener() {
        new JoinListener();
        new ChatListener();
        new InteractListener();
        new PluginMessageReceiveListener();
        new InventoryClickListener();
        new FoodLevelChangeListener();
        new QuitListener();
        new GameStartListener();
        new WeatherChangeListener();
        new EntityDamageByEntityListener();
        new ConsumeListener();
        new EntityDeathListener();
        new EntityDamageListener();
        new DropListener();
        new LanguageChangeListener();
        new PlayerDeathListener();
        new SlimeSplitListener();
        new BlockBreakListener();
        new BlockPlaceListener();
        new WaveHandleListener();
        new WaveLoadListener();
        new WaveDoneListener();
        new WaveStartListener();
        new ProjectileLaunchListener();
        new GameEndListener();
        new PreWaveHandleListener();
        new LoginListener();
        new CreatureSpawnListener();
        new PickupListener();
        new EntityTargetListener();
        new ProjectileHitListener();
        new EntityExplodeListener();
        new LobbyBlockToggleListener();
        new TitleUpListener();
        new InventoryCloseListener();
        new EntityCombustListener();
        new ForceSkipCommand();
    }

    public void registerCommands() {
        MobSlayerCore.getCommandManager().registerCommand(new StartCommand());
        MobSlayerCore.getCommandManager().registerCommand(new KillAllCommand());
        MobSlayerCore.getCommandManager().registerCommand(new ReviveCommand());
        MobSlayerCore.getCommandManager().registerCommand(new StartWaveCommand());
        MobSlayerCore.getCommandManager().registerCommand(new SummonCommand());
        MobSlayerCore.getCommandManager().registerCommand(new SkipCommand());
        MobSlayerCore.getCommandManager().registerCommand(new GameStatisticsCommand());
        MobSlayerCore.getCommandManager().registerCommand(new SetMultiplierCommand());
        MobSlayerCore.getCommandManager().registerCommand(new AliveCommand());
        MobSlayerCore.getCommandManager().registerCommand(new AirDropCommand());
        MobSlayerCore.getCommandManager().registerCommand(new ItemInfoCommand());
        MobSlayerCore.getCommandManager().registerCommand(new GiveCommand());
        MobSlayerCore.getCommandManager().registerCommand(new SpawnRequestCommand());
        MobSlayerCore.getCommandManager().registerCommand(new WaveActionCommand());
        MobSlayerCore.getCommandManager().registerCommand(new LearnCraftComand());
        MobSlayerCore.getCommandManager().registerCommand(new ProgressCommand());
        MobSlayerCore.getCommandManager().registerCommand(new FixCommand());
    }

    private void setupTranslations() {
        Translation.addTranslation("game.join_message",
                new String[]{"%s §7ist dem Spiel beigetreten.", "%s §7has joined the game."});
        Translation.addTranslation("game.quit_message",
                new String[]{"%s §7hat das Spiel verlassen.", "%s §7has left the game."});
        Translation.addTranslation("map_manager.map_manager.map_not_existing_in_database",
                new String[]{"Die Karte konnte nicht in der Datenbank gefunden werden",
                        "The map could not be found in the database"});
        Translation.addTranslation("command.only_in_lobby", new String[]{
                "Dieser Befehl ist nur in der Lobby verwendbar", "This command is only available in lobby"});
        Translation.addTranslation("map_manager.too_few_player_spawns",
                new String[]{"Es gibt zu wenige Spielerspawns", "There are too few player spawns"});
        Translation.addTranslation("game.enough_players_online",
                new String[]{"Es sind nun §egenügend Spieler §7online. Das Spiel startet in §e5 Minuten§7.",
                        "There are now §eenough players §7online. The game is starting in §e5 minutes§7."});

        Translation.addTranslation("game.you_died", new String[]{"Du bist gestorben.", "You died."});
        Translation.addTranslation("game.respawn_message",
                new String[]{"Wird die Welle überstanden, wirst du wiederbelebt.",
                        "If the wave gets completed, you will respawn."});

        Translation.addTranslation("game.lobby.time_shortened",
                new String[]{"Die Zeit wurde §egekürzt§7.", "The time has been §eshortened§7."});

        Translation.addTranslation("game.general.points", new String[]{"Punkte", "Points"});
        Translation.addTranslation("game.general.by", new String[]{"von", "by"});
        Translation.addTranslation("game.general.right_click", new String[]{"Rechtsklick", "Right Click"});

        Translation.addTranslation("game.lobby.preview", new String[]{"Vorschau", "Preview"});
        Translation.addTranslation("game.lobby.voting", new String[]{"Abstimmung", "Voting"});
        Translation.addTranslation("game.lobby.vote", new String[]{"Vote", "Vote"});
        Translation.addTranslation("game.lobby.votes", new String[]{"Votes", "Votes"});
        Translation.addTranslation("game.lobby.customization", new String[]{"Anpassung", "Customization"});
        Translation.addTranslation("game.lobby.settings", new String[]{"Einstellungen", "Settings"});

        Translation.addTranslation("game.lobby.customization.locked", new String[]{"Gesperrt", "Locked"});
        Translation.addTranslation("game.lobby.customization.progress", new String[]{"Fortschritt", "Progress"});
        Translation.addTranslation("game.lobby.customization.recently_unlocked", new String[]{"Kürzlich freigeschaltet!", "Recently unlocked!"});

        Translation.addTranslation("game.lobby.voting_ended",
                new String[]{"Die Abstimmung wurde beendet.", "The voting has ended."});
        Translation.addTranslation("game.lobby.map_announce", new String[]{
                "Die Map §e%1$s §7von §e%2$s §7hat gewonnen.", "The map §e%1$s §7by §e%2$s §7has won."});

        Translation.addTranslation("game.lobby.starting_in_x_seconds", new String[]{
                "Das Spiel startet in §e%s Sekunden§7.", "The game is starting in §e%s seconds§7."});
        Translation.addTranslation("game.lobby.starting_in_1_second",
                new String[]{"Das Spiel startet in §eeiner Sekunde§7.", "The game is starting in §e1 second§7."});

        Translation.addTranslation("game.lobby.starting_in_1_minute",
                new String[]{"Das Spiel startet in §eeiner Minute§7.", "The game is starting in §e1 minute§7."});
        Translation.addTranslation("game.lobby.starting_in_2_minutes",
                new String[]{"Das Spiel startet in §e2 Minuten§7.", "The game is starting in §e2 minutes§7."});
        Translation.addTranslation("game.lobby.starting_in_3_minutes",
                new String[]{"Das Spiel startet in §e3 Minuten§7.", "The game is starting in §e3 minutes§7."});
        Translation.addTranslation("game.lobby.starting_in_4_minutes",
                new String[]{"Das Spiel startet in §e4 Minuten§7.", "The game is starting in §e4 minutes§7."});

        Translation.addTranslation("game.lobby.voting.voted_for_map",
                new String[]{"Du hast für die Map §l%s §eabgestimmt.", "You voted for the map §l%s"});
        Translation.addTranslation("game.lobby.voting.already_voted_for_map",
                new String[]{"Du hast bereits für diese Map abgestimmt.", "You already voted for this map."});

        Translation.addTranslation("game.general.wave", new String[]{"Welle", "Wave"});
        Translation.addTranslation("game.general.warmup", new String[]{"Vorbereitung", "Warmup"});
        Translation.addTranslation("game.general.alive", new String[]{"Am Leben", "Alive"});
        Translation.addTranslation("game.general.player", new String[]{"Spieler", "Player"});
        Translation.addTranslation("game.general.players", new String[]{"Spieler", "Players"});
        Translation.addTranslation("game.general.enemy", new String[]{"Gegner", "Enemy"});
        Translation.addTranslation("game.general.enemies", new String[]{"Gegner", "Enemies"});

        Translation.addTranslation("game.lobby.customization.choose_an_armor_part",
                new String[]{"Wähle ein Rüstungsteil", "Choose an armor part"});
        Translation.addTranslation("game.lobby.customization.choose_a_color",
                new String[]{"Wähle eine Farbe", "Choose a color"});

        Translation.addTranslation("game.wave.successfully_completed",
                new String[]{"Welle %s erfolgreich beendet!", "Wave %s successfully completed!"});
        Translation.addTranslation("game.wave.next_wave_starting_in",
                new String[]{"Die nächste Welle startet in §e%s Sekunden§7.",
                        "The next wave is starting in §e%s seconds§7."});
        Translation.addTranslation("game.wave.first_wave_starting_in",
                new String[]{"Die erste Welle startet in §e%s Sekunden§7.",
                        "The first wave is starting in §e%s seconds§7."});

        Translation.addTranslation("game.player_death",
                new String[]{"%s §cist gestorben.", "%s §cdied."});

        Translation.addTranslation("game.lobby.customization.choose_this_color",
                new String[]{"Diese Farbe auswählen", "Choose this color"});
        Translation.addTranslation("game.lobby.customization.default_color",
                new String[]{"Standardfarbe", "Default color"});

        Translation.addTranslation("game.general.recipe_book", new String[]{"Rezeptbuch", "Recipe Book"});
        Translation.addTranslation("game.general.wand", new String[]{"Zauberstab", "Wand"});

        Translation.addTranslation("game.general.soulbound", new String[]{"Seelengebunden", "Soulbound"});

        Translation.addTranslation("game.dead.switch_to_spectator_mode", new String[]{"Zuschauen", "Spectate"});
        Translation.addTranslation("game.spectate.select_a_player",
                new String[]{"Wähle einen Spieler", "Select a player"});

        Translation.addTranslation("item.use.crafting_item", new String[]{"Crafting Gegenstand", "Crafting Item"});
        Translation.addTranslation("item.use.crafted_item", new String[]{"Gecrafteter Gegenstand", "Crafted Item"});

        Translation.addTranslation("game.revive.player_revived_player", new String[]{
                "%1$s §ehat %2$s §ewiederbelebt.", "%2$s §ehas been revived by %1$s§e."});
        Translation.addTranslation("game.revive.you_have_been_revived_by",
                new String[]{"§bDu wurdest von §f%s §bwiederbelebt. Sei dankbar!", "§bYou have been revived by §f%s§b. Be thankful!"});
        Translation.addTranslation("game.revive.you_revived_player",
                new String[]{"§bDu hast §f%s §bfür §f500 Coins §bwiederbelebt.",
                        "§bYou revived §f%s §bfor §f500 Coins§b."});
        Translation.addTranslation("game.revive.player_revived",
                new String[]{"%s §ewurde wiederbelebt.", "%s §ehas been revived."});
        Translation.addTranslation("game.revive.you_revived_yourself", new String[]{
                "§bDu hast dich selbst für §f500 Coins §bwiederbelebt.", "§bYou revived yourself for §f500 Coins§b."});
        Translation.addTranslation("game.revive.auto_respawn", new String[]{
                "§bAutomatischer Respawn ist aktiviert. Du hast dich selbst für §f500 Coins §bwiederbelebt.", "§bAutomated Respawn is activated. You revived yourself for §f500 Coins§b."});

        Translation.addTranslation("game.remaing_players_count", new String[]{
                "Es sind §f%s Spieler §7verbleibend.", "There are §f%s players §7remaining."});
        Translation.addTranslation("game.remaing_player_count",
                new String[]{"Es ist §fein Spieler §7verbleibend.", "There is §f1 player §7remaining."});

        Translation.addTranslation("coins.not_enough_coins",
                new String[]{"Du hast nicht genug Coins", "You don't have enough coins"});

        Translation.addTranslation("game.skippable_message", new String[]{
                "Die Welle kann nun mit §f/skip §egeskippt werden.", "The wave is now skippable with §f/skip§e."});
        Translation.addTranslation("game.skip_message",
                new String[]{"%s §7ist dafür, §edie Welle zu überspringen§7. §8(/skip)",
                        "%s §7voted to §eskip the current wave. §8(/skip)"});

        Translation.addTranslation("game.shop", new String[]{"Shop", "Shop"});
        Translation.addTranslation("game.shop.price", new String[]{"Preis", "Price"});
        Translation.addTranslation("game.shop.sell_value", new String[]{"Verkaufswert", "Sell value"});

        Translation.addTranslation("item.stick.name", new String[]{"Stab", "Stick"});
        Translation.addTranslation("item.slime_ball.name", new String[]{"Schleimball", "Slime Ball"});
        Translation.addTranslation("item.feather.name", new String[]{"Feder", "Feather"});
        Translation.addTranslation("item.spider_eye.name", new String[]{"Spinnenauge", "Spider Eye"});
        Translation.addTranslation("item.glowstone_dust.name", new String[]{"Glowstonestaub", "Glowstone Dust"});
        Translation.addTranslation("item.gun_powder.name", new String[]{"Schwarzpulver", "Gun Powder"});
        Translation.addTranslation("item.fire_powder.name", new String[]{"Feuerpulver", "Fire Powder"});
        Translation.addTranslation("item.fire_rod.name", new String[]{"Feuerstab", "Fire Rod"});
        Translation.addTranslation("item.string.name", new String[]{"Faden", "String"});
        Translation.addTranslation("item.diamond.name", new String[]{"Diamant", "Diamond"});
        Translation.addTranslation("item.crystal.name", new String[]{"Crystal", "Crystal"});

        Translation.addTranslation("item.heal_1.name", new String[]{"Heilung I", "Heal I"});
        Translation.addTranslation("item.heal_2.name", new String[]{"Heilung II", "Heal II"});
        Translation.addTranslation("item.heal_3.name", new String[]{"Heilung III", "Heal III"});

        Translation.addTranslation("item.grenade.name", new String[]{"Granate", "Grenade"});
        Translation.addTranslation("item.knockback_grenade.name", new String[]{"Rückstoßgranate", "Knockback Grenade"});

        Translation.addTranslation("item.healing_station.name", new String[]{"Heilungsstation", "Healing Station"});

        Translation.addTranslation("item.fermented_spider_eye.name", new String[]{"Fermentiertes Spinnenauge", "Fermented Spider Eye"});

        Translation.addTranslation("item.bow.fire_bow", new String[]{"Feuerbogen", "Fire Bow"});

        Translation.addTranslation("command.stats.game_statistics",
                new String[]{"Spielstatistiken", "Game Statistics"});
        Translation.addTranslation("command.stats.revives", new String[]{"Wiederbelebungen", "Revives"});

        Translation.addTranslation("game.enchantments.damage_all", new String[]{"Schärfe", "Sharpness"});
        Translation.addTranslation("game.enchantments.arrow_fire", new String[]{"Flamme", "Flame"});
        Translation.addTranslation("game.enchantments.arrow_damage", new String[]{"Stärke", "Power"});
        Translation.addTranslation("game.enchantments.fire_aspect", new String[]{"Verbrennung", "Fire Aspect"});
        Translation.addTranslation("game.enchantments.protection_environmental", new String[]{"Schutz", "Protection"});
        Translation.addTranslation("game.enchantments.fire_protection", new String[]{"Feuerschutz", "Fire Protection"});
        Translation.addTranslation("game.enchantments.poison", new String[]{"Vergiftung", "Poison Aspect"});
        Translation.addTranslation("game.enchantments.infinite", new String[]{"Unendlichkeit", "Infinite"});
        Translation.addTranslation("game.enchantments.fire_extinguish", new String[]{"Feuerlöschung", "Fire Extinguish"});

        Translation.addTranslation("game.enchantments.enchantment", new String[]{"Verzauberung", "Enchantment"});


        Translation.addTranslation("item.healing_station.can_only_be_placed_while_a_wave",
                new String[]{"Eine Heilungsstation kann nur während einer Runde platziert werden.",
                        "A healing station can only be placed while a wave."});

        Translation.addTranslation("game.healing_station_placed", new String[]{"%s §7hat eine §eHeilungsstation §7aufgestellt.", "%s §7has placed a §eHealing Station§7."});

        Translation.addTranslation("command.alive.players_alive", new String[]{"Spieler am Leben", "Players alive"});

        Translation.addTranslation("game.general.mob_tracker", new String[]{"Mobtracker", "Mob Tracker"});

        Translation.addTranslation("game.healing_station.state_1", new String[]{"Zustand: §a#§8## §7(1/3)", "State: §a#§8## §7(1/3)"});
        Translation.addTranslation("game.healing_station.state_2", new String[]{"Zustand: §a##§8# §7(2/3)", "State: §a##§8# §7(2/3)"});
        Translation.addTranslation("game.healing_station.state_3", new String[]{"Zustand: §a### §7(3/3)", "State: §a### §7(3/3)"});

        Translation.addTranslation("game.healing_station.already_full", new String[]{"Diese Heilungsstation ist im exzellenten Zustand.",
                "This Healing Station is in an excellent condition."});
        Translation.addTranslation("game.healing_station.already_full", new String[]{"Du hast diese Heilungsstation repariert.", "You repaired the Healing Station."});

        Translation.addTranslation("item.armor_breaker.name", new String[]{"Rüstungsbrecher", "Armor Breaker"});
        Translation.addTranslation("item.armor_breaker.lore", new String[]{"Bricht Rüstung mit einem Treffer", "Breaks armor with one hit"});

        Translation.addTranslation("item.iron_ingot.name", new String[]{"Eisenbarren", "Iron Ingot"});
        Translation.addTranslation("item.glowing_iron_ingot.name", new String[]{"Glühender Eisenbarren", "Glowing Iron Ingot"});
        Translation.addTranslation("item.cursed_iron_ingot.name", new String[]{"Verfluchter Eisenbarren", "Cursed Iron Ingot"});

        Translation.addTranslation("item.gold_ingot.name", new String[]{"Goldbarren", "Gold Ingot"});
        Translation.addTranslation("item.glowing_gold_ingot.name", new String[]{"Glühender Goldbarren", "Glowing Gold Ingot"});
        Translation.addTranslation("item.gold_nugget.name", new String[]{"Goldstück", "Gold Nugget"});

        Translation.addTranslation("item.magic_powder.name", new String[]{"Zauberpulver", "Magic Powder"});

        Translation.addTranslation("item.use.upgraded_item", new String[]{"Verbesserter Gegenstand", "Upgraded Item"});

        Translation.addTranslation("game.crafting.enemies_nearby", new String[]{"Es sind Gegner in der Nähe.", "There are enemies nearby."});

        Translation.addTranslation("game.craft.click_to_show_recipe", new String[]{"Klicke um Rezept anzuzeigen", "Click to show recipe"});
        Translation.addTranslation("game.craft.recipe_display", new String[]{"Rezeptanzeige", "Recipe Display"});
        Translation.addTranslation("game.craft.use_cauldron", new String[]{"In Kessel werfen", "Put in cauldron"});
        Translation.addTranslation("game.craft.go_back", new String[]{"Gehe zurück", "Go back"});

        Translation.addTranslation("command.iteminfo.title", new String[]{"Iteminfo", "Item Info"});
        Translation.addTranslation("command.iteminfo.gameitem_id", new String[]{"GameItem ID", "Game Item ID"});
        Translation.addTranslation("command.iteminfo.name", new String[]{"Name", "Name"});
        Translation.addTranslation("command.iteminfo.translation_path", new String[]{"Übersetzungspfad", "Translation Path"});
        Translation.addTranslation("command.iteminfo.attributes", new String[]{"Attribute", "Attributes"});
        Translation.addTranslation("command.iteminfo.no_item_in_hand", new String[]{"Kein Item in der Hand.", "No item in hand."});
        Translation.addTranslation("command.iteminfo.no_game_item", new String[]{"Dies ist kein GameItem!", "That's no Game Item!"});
        Translation.addTranslation("command.iteminfo.worth", new String[]{"Wert", "Worth"});

        Translation.addTranslation("game.item.researchable", new String[]{"Erforschbar", "Researchable"});

        Translation.addTranslation("general.wave_script.obtain_item", new String[]{"Du hast einen Gegenstand erhalten", "You have received an item"});
        Translation.addTranslation("general.craft.learned_new_recipe", new String[]{"Du hast ein neues Rezept gelernt", "You learned a new recipe"});

        Translation.addTranslation("game.research_table.locked", new String[]{"Du kannst Forschungstische erst ab Rang §e%s §cnutzen.", "You can use Research Tables with rank %s§c."});

        Translation.addTranslation("general.title.you_are_now", new String[]{"Du bist nun §7%s", "You are now §7%s"});

        Translation.addTranslation("game.research_table.title", new String[]{"Erforschungstisch", "Research Table"});
        Translation.addTranslation("game.research_table.research_for", new String[]{"Erforschen für %s Coins", "Research for %s Coins"});
        Translation.addTranslation("game.research_table.put_in_item", new String[]{"Gegenstand einlegen", "Put in Item"});
        Translation.addTranslation("game.research_table.already_researched", new String[]{"Bereits bekannt", "Known Recipe"});
        Translation.addTranslation("game.research_table.not_researchable", new String[]{"Nicht erforschbar", "Not researchable"});

        Translation.addTranslation("general.craft.locked", new String[]{"Du kannst diesen Gegenstand erst ab Rang §e%s §cerforschen.",
                "You need to be at least rank §e%s §cto research this item."});

        Translation.addTranslation("item.book.name", new String[]{"Buch", "Book"});
        
        Translation.addTranslation("item.grip.name", new String[]{"Griff", "Grip"});
        Translation.addTranslation("item.extra_heart.name", new String[]{"Extraherztrank", "Extra Heart Potion"});
        Translation.addTranslation("item.extra_heart.lore", new String[]{"Erweitert dein Leben um ein Herz (dauerhaft)", "Increases your health by one heart (permanently)"});
        Translation.addTranslation("item.speed.name", new String[]{"Geschwindigkeitstrank", "Speed Potion"});
        Translation.addTranslation("item.speed.lore", new String[]{"25 Prozent schnelleres Bewegen (dauerhaft)", "25 percent faster movement (permanently)"});
        
        Translation.addTranslation("item.iron_axe.lore", new String[]{"Flächenschaden", "Increased damage area"});
        Translation.addTranslation("item.diamond_axe.lore", new String[]{"Flächenschaden", "Increased damage area"});
    }

    public static GameEntityManager getGameEntityManager() {
        return gameEntityManager;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }

    public static MapManager getMapManager() {
        return mapManager;
    }

    public static VoteManager getVoteManager() {
        return voteManager;
    }

    public static SectionManager getSectionManager() {
        return sectionManager;
    }

    public static WaveManager getWaveManager() {
        return waveManager;
    }

    public static AirDropManager getAirDropManager() {
        return airDropManager;
    }

    public static GameItemManager getGameItemManager() {
        return gameItemManager;
    }

    public static double getDamageReduced(Player player) {
        org.bukkit.inventory.PlayerInventory inv = player.getInventory();
        ItemStack boots = inv.getBoots();
        ItemStack helmet = inv.getHelmet();
        ItemStack chest = inv.getChestplate();
        ItemStack pants = inv.getLeggings();
        double red = 0.0;

        if (helmet != null) {
            if (helmet.getType() == Material.LEATHER_HELMET)
                red = red + 0.04;
            else if (helmet.getType() == Material.GOLD_HELMET)
                red = red + 0.08;
            else if (helmet.getType() == Material.CHAINMAIL_HELMET)
                red = red + 0.08;
            else if (helmet.getType() == Material.IRON_HELMET)
                red = red + 0.08;
            else if (helmet.getType() == Material.DIAMOND_HELMET)
                red = red + 0.12;
        }

        if (boots != null) {
            if (boots.getType() == Material.LEATHER_BOOTS)
                red = red + 0.04;
            else if (boots.getType() == Material.GOLD_BOOTS)
                red = red + 0.04;
            else if (boots.getType() == Material.CHAINMAIL_BOOTS)
                red = red + 0.04;
            else if (boots.getType() == Material.IRON_BOOTS)
                red = red + 0.08;
            else if (boots.getType() == Material.DIAMOND_BOOTS)
                red = red + 0.12;
        }

        if (pants != null) {
            if (pants.getType() == Material.LEATHER_LEGGINGS)
                red = red + 0.08;
            else if (pants.getType() == Material.GOLD_LEGGINGS)
                red = red + 0.12;
            else if (pants.getType() == Material.CHAINMAIL_LEGGINGS)
                red = red + 0.16;
            else if (pants.getType() == Material.IRON_LEGGINGS)
                red = red + 0.20;
            else if (pants.getType() == Material.DIAMOND_LEGGINGS)
                red = red + 0.24;
        }

        if (chest != null) {
            if (chest.getType() == Material.LEATHER_CHESTPLATE)
                red = red + 0.12;
            else if (chest.getType() == Material.GOLD_CHESTPLATE)
                red = red + 0.20;
            else if (chest.getType() == Material.CHAINMAIL_CHESTPLATE)
                red = red + 0.20;
            else if (chest.getType() == Material.IRON_CHESTPLATE)
                red = red + 0.24;
            else if (chest.getType() == Material.DIAMOND_CHESTPLATE)
                red = red + 0.32;
        }

        if (player.isBlocking()) {
            red = red + 0.24;
        }

        return red * 4;
    }

    public static Entity[] getNearbyEntities(Location l, int radius) {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();
        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk()
                        .getEntities()) {
                    if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
                        radiusEntities.add(e);
                }
            }
        }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

    public static CraftRecipe getCraftRecipe(GameItem gameItem) {
        for (Craft craft : Craft.values()) {
            if (craft.getCraftRecipe().getResult().getGameItemId() == gameItem.getGameItemId()) {
                return craft.getCraftRecipe();
            }
        }
        return null;
    }
}
