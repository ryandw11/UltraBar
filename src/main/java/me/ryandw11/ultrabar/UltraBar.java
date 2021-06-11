package me.ryandw11.ultrabar;

import me.ryandw11.ods.ObjectDataStructure;
import me.ryandw11.ods.Tag;
import me.ryandw11.ods.tags.ObjectTag;
import me.ryandw11.ultrabar.announcements.Announcer;
import me.ryandw11.ultrabar.announcements.IndividualBossBarAnnouncer;
import me.ryandw11.ultrabar.api.UBossBar;
import me.ryandw11.ultrabar.api.parameters.BarParameter;
import me.ryandw11.ultrabar.bstats.Metrics;
import me.ryandw11.ultrabar.bstats.UpdateChecker;
import me.ryandw11.ultrabar.chatcolor.ChatColorUtil;
import me.ryandw11.ultrabar.chatcolor.ChatColorUtil_1_16;
import me.ryandw11.ultrabar.chatcolor.ChatColorUtil_Old;
import me.ryandw11.ultrabar.commands.*;
import me.ryandw11.ultrabar.depends.PAPIExists;
import me.ryandw11.ultrabar.depends.PAPINotFound;
import me.ryandw11.ultrabar.depends.PlaceholderAPIDepend;
import me.ryandw11.ultrabar.io.BarTag;
import me.ryandw11.ultrabar.listener.*;
import me.ryandw11.ultrabar.parameters.CommandParameter;
import me.ryandw11.ultrabar.schedulers.ActionBarSched;
import me.ryandw11.ultrabar.schedulers.TitleSched;
import me.ryandw11.ultrabar.typemgr.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The main class for Ultra Bar
 *
 * @author Ryandw11
 * @version 2.3.0
 */
public class UltraBar extends JavaPlugin {

    /**
     * Contains all of the tracked ubossbars for anything.
     *
     * @since 2.1
     */
    public static List<UBossBar> trackedBars;
    public static UltraBar plugin;
    public ChatColorUtil chatColorUtil;
    public TypeManager typeManager;
    public PlaceholderAPIDepend papiTranslate;
    public boolean worldguard = false;
    public boolean placeholderAPI;
    // The list of players that have toggle enabled.
    private List<Player> toggledPlayers;
    // The list of bar parameters.
    private List<BarParameter> barParameters;
    // The bar announcer (based on the new system).
    private Announcer barAnnouncer;

    @Override
    public void onEnable() {
        plugin = this;
        trackedBars = new CopyOnWriteArrayList<>();
        toggledPlayers = new ArrayList<>();
        barParameters = new ArrayList<>();

        if (Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
            getLogger().info("WorldGuard detected. WorldGuard addon activated.");
            worldguard = true;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("PlaceholderAPI detected. PlaceholderAPI addon activated.");
            placeholderAPI = true;
        } else {
            placeholderAPI = false;
        }
        setupPlaceholderAPI();

        if (setupVersionSpecificCode()) {
            loadMethod();
            registerConfig();
            if (!checkForUpdate()) {
                Bukkit.getPluginManager().disablePlugin(this);
                getLogger().severe("UltraBar has been disabled to prevent damage.");
                return;
            }
            getLogger().info(String.format("UltraBar is enabled and running fine! V: %s", getDescription().getVersion()));
            loadSched();
        } else {
            getLogger().severe(ChatColor.RED + "UltraBar does not support the version you are currently on!");
            getLogger().info("This version is only for 1.12 - 1.17+.");
            getLogger().info("The plugin will now be disabled!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        if (getConfig().getBoolean("bstats")) {
            Metrics metrics = new Metrics(this);
            getLogger().info("Bstat metrics for this plugin is enabled. Disable it in the config if you do not want it on.");
        } else {
            getLogger().info("Bstat metrics is disabled for this plugin.");
        }

        if (getConfig().getBoolean("update_checker")) {
            UpdateChecker updater = new UpdateChecker(this, 20113);
            try {
                if (updater.checkForUpdates())
                    getLogger().info("An update was found! New version: " + updater.getLatestVersion() + " download: " + updater.getResourceURL());
            } catch (Exception e) {
                getLogger().warning("Could not check for updates! Stacktrace:");
                e.printStackTrace();
            }
        } //End of update checker

        // Add back unused bars.
        if (!getConfig().getBoolean("save_persistent_bars")) return;
        File f = new File(getDataFolder() + File.separator + "saved_bars.ubr");
        if (!f.exists()) return;
        ObjectDataStructure ods = new ObjectDataStructure(f);
        List<Tag<?>> tags = ods.getAll();
        for (Tag<?> tag : tags) {
            ObjectTag obj = (ObjectTag) tag;
            new BarTag(obj).buildBossBar();
        }
    }


    @Override
    public void onDisable() {
        ObjectDataStructure ods = new ObjectDataStructure(new File(getDataFolder() + File.separator + "saved_bars.ubr"));
        List<Tag<?>> tags = new ArrayList<>();
        getLogger().info("Saving tracked bars to file.");
        for (UBossBar bar : trackedBars) {
            tags.add(new BarTag(bar));
            if (bar.getTimer() != null)
                bar.getTimer().cancel();
            if (bar.getBar() != null)
                bar.getBar().setVisible(false);
        }
        if (getConfig().getBoolean("save_persistent_bars"))
            ods.save(tags);


        getLogger().info("Save complete!");
        trackedBars.clear();
        barAnnouncer.stopProgram();
        getLogger().info("UltraBar was successfully disabled!");
    }


    private void registerConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    /**
     * Loads all of the requires commands and listeners for the plugin.
     */
    public void loadMethod() {
        Objects.requireNonNull(getCommand("bar")).setExecutor(new BarCommand());
        Objects.requireNonNull(getCommand("bar")).setTabCompleter(new BarCommandTabCompleter());
        Objects.requireNonNull(getCommand("utitle")).setExecutor(new TitleCommand(this));
        Objects.requireNonNull(getCommand("utitle")).setTabCompleter(new TitleCommandTabCompleter());
        Objects.requireNonNull(getCommand("actionbar")).setExecutor(new ActionBarCommands(this));
        Objects.requireNonNull(getCommand("actionbar")).setTabCompleter(new ActionBarCommandTabCompleter());
        Objects.requireNonNull(getCommand("ultrabar")).setExecutor(new UltraBarCommand(this));
        Objects.requireNonNull(getCommand("ultrabar")).setTabCompleter(new UltraBarCommandTabCompleter());
        Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnDeath(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnCommand(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnChangeWorld(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnLeave(), this);
        this.registerParameter(new CommandParameter());
    }

    private void loadSched() {
        if (getConfig().getBoolean("BossBarMessages.Enabled")) {
//            BossBarSced b = new BossBarSced();
//            b.startProgram();
            this.barAnnouncer = new IndividualBossBarAnnouncer();
            barAnnouncer.startProgram();
        }
        if (getConfig().getBoolean("Title_Announcements.Enabled")) {
            TitleSched ts = new TitleSched();
            ts.startProgram();
        }
        if (getConfig().getBoolean("Action_Announcements.Enabled")) {
            ActionBarSched as = new ActionBarSched();
            as.startProgram();
        }
    }

    /**
     * Setup code specific to a single version of minecraft.
     * <p>
     * 1.12 - 1.17 is supported.
     *
     * @return If the setup was successful.
     */
    private boolean setupVersionSpecificCode() {
        String version;
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException w0w) {
            return false;
        }

        getLogger().info("Your server is running version " + version + "!");
        switch (version) {
            case "v1_16_R3":
                typeManager = new TypeManager_1_16_R3();
                if (getConfig().getBoolean("WorldGuardRegion.Enabled") && plugin.worldguard)
                    Bukkit.getServer().getPluginManager().registerEvents(new WorldGuardOnMove(this), this);
                chatColorUtil = new ChatColorUtil_1_16();
                break;
            case "v1_16_R2":
                typeManager = new TypeManager_1_16_R2();
                if (getConfig().getBoolean("WorldGuardRegion.Enabled") && plugin.worldguard)
                    Bukkit.getServer().getPluginManager().registerEvents(new WorldGuardOnMove(this), this);
                chatColorUtil = new ChatColorUtil_1_16();
                break;
            case "v1_16_R1":
                typeManager = new TypeManager_1_16_R1();
                if (getConfig().getBoolean("WorldGuardRegion.Enabled") && plugin.worldguard)
                    Bukkit.getServer().getPluginManager().registerEvents(new WorldGuardOnMove(this), this);
                chatColorUtil = new ChatColorUtil_1_16();
                break;
            case "v1_15_R1":
                typeManager = new TypeManager_1_15_R1();
                if (getConfig().getBoolean("WorldGuardRegion.Enabled") && plugin.worldguard)
                    Bukkit.getServer().getPluginManager().registerEvents(new WorldGuardOnMove(this), this);
                chatColorUtil = new ChatColorUtil_Old();
                break;
            case "v1_14_R1":
                typeManager = new TypeManager_1_14_R1();
                if (getConfig().getBoolean("WorldGuardRegion.Enabled") && plugin.worldguard)
                    Bukkit.getServer().getPluginManager().registerEvents(new WorldGuardOnMove(this), this);
                chatColorUtil = new ChatColorUtil_Old();
                break;
            case "v1_13_R2":
                typeManager = new TypeManager_1_13_R2();
                if (getConfig().getBoolean("WorldGuardRegion.Enabled") && plugin.worldguard)
                    Bukkit.getServer().getPluginManager().registerEvents(new WorldGuardOnMove(this), this);
                chatColorUtil = new ChatColorUtil_Old();
                break;
            case "v1_12_R1":
                typeManager = new TypeManager_1_12_R1();
                chatColorUtil = new ChatColorUtil_Old();
                break;
            // UltraBar should now work by default for all new updates.
            default:
                typeManager = new ModernTypeManager();
                if (getConfig().getBoolean("WorldGuardRegion.Enabled") && plugin.worldguard)
                    Bukkit.getServer().getPluginManager().registerEvents(new WorldGuardOnMove(this), this);
                chatColorUtil = new ChatColorUtil_1_16();
                break;
        }

        return typeManager != null;
    }

    /**
     * Setup PlaceholderAPI support.
     */
    private void setupPlaceholderAPI() {
        if (placeholderAPI) {
            papiTranslate = new PAPIExists();
            return;
        }
        papiTranslate = new PAPINotFound();
    }

    /**
     * Grab the list of players that have the messages toggled.
     *
     * @return List of toggled
     */
    public List<Player> getToggledPlayers() {
        return toggledPlayers;
    }

    /**
     * Add a player to the list of messages toggled.
     *
     * @param p The player to add to the toggle list.
     */
    public void addTogglePlayer(Player p) {
        toggledPlayers.add(p);
    }

    /**
     * Remove a player from the list of messages toggled.
     *
     * @param p The player to remove from the toggle list.
     */
    public void removeTogglePlayer(Player p) {
        toggledPlayers.remove(p);
    }

    /**
     * Not to be used.
     * See {@link me.ryandw11.ultrabar.api.UltraBarAPI#registerParameter(BarParameter)}
     *
     * @param bp The bar parameter.
     */
    public void registerParameter(BarParameter bp) {
        barParameters.add(bp);
    }

    /**
     * Get all of the bar parameters.
     *
     * @return The list of bar parameters.
     */
    public List<BarParameter> getBarParameters() {
        return new ArrayList<>(this.barParameters);
    }

    /**
     * Get the boss bar announcer.
     *
     * @return The boss bar announcer.
     */
    public Announcer getBarAnnouncer() {
        return barAnnouncer;
    }

    /**
     * Reset the bar announcer.
     */
    public void resetBarAnnouncer() {
        this.barAnnouncer = new IndividualBossBarAnnouncer();
        this.barAnnouncer.startProgram();
    }

    private boolean checkForUpdate() {
        if (plugin.getConfig().contains("config_version") && !plugin.getConfig().contains("BossBarMessages.Number_Of_Messages")) {
            return true;
        }
        getLogger().info("An older version of the plugin is detected. Automatically updating config file.");
        File config = new File(getDataFolder(), "config.yml");
        File configBackup = new File(getDataFolder(), "config.yml.backup");
        try {
            configBackup.createNewFile();
            FileUtils.copyFile(config, configBackup);
        } catch (IOException ex) {
            getLogger().severe("A critical error was encountered when attempting to update plugin configuration" +
                    " files!");
            getLogger().severe("Unable to create backup of the config.yml file.");

            return false;
        }

        getConfig().set("BossBarMessages.Messages", null);
        saveConfig();

        List<ConfigurationSection> sections = new ArrayList<>();
        int numberOfBars = getConfig().getInt("BossBarMessages.Number_Of_Messages");
        for (int i = 1; i <= numberOfBars; i++) {
            sections.add(getConfig().getConfigurationSection("BossBarMessages." + i));
            getConfig().set("BossBarMessages." + i, null);
        }
        getConfig().set("BossBarMessages.Number_Of_Messages", null);

        int i = 1;
        for (ConfigurationSection section : sections) {
            getConfig().set("BossBarMessages.Messages.Message" + i, section);
            i++;
        }
        getConfig().set("config_version", 1);

        getLogger().info("Successfully updated the config to the latest version!");

        saveConfig();
        return true;
    }
}