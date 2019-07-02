package me.ryandw11.ultrabar.core;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.ryandw11.ultrabar.BossBarSced;
import me.ryandw11.ultrabar.api.bars.UBossBar;
import me.ryandw11.ultrabar.bstats.Metrics;
import me.ryandw11.ultrabar.bstats.UpdateChecker;
import me.ryandw11.ultrabar.commands.ActionBarCommands;
import me.ryandw11.ultrabar.commands.BarCommandTabCompleter;
import me.ryandw11.ultrabar.commands.Help;
import me.ryandw11.ultrabar.commands.NewBarCommand;
import me.ryandw11.ultrabar.commands.TitleCommands;
import me.ryandw11.ultrabar.depends.PAPIExists;
import me.ryandw11.ultrabar.depends.PAPINotFound;
import me.ryandw11.ultrabar.depends.PlaceholderAPIDepend;
import me.ryandw11.ultrabar.listener.OnChangeWorld;
import me.ryandw11.ultrabar.listener.OnCommand;
import me.ryandw11.ultrabar.listener.OnDeath;
import me.ryandw11.ultrabar.listener.OnJoin;
import me.ryandw11.ultrabar.listener.OnMove_1_12_R1;
import me.ryandw11.ultrabar.listener.OnMove_1_13_R1;
//import me.ryandw11.ultrabar.listener.OnMove;
import me.ryandw11.ultrabar.schedulers.ActionBarSched;
import me.ryandw11.ultrabar.schedulers.TitleSched;
import me.ryandw11.ultrabar.typemgr.Typemgr;
import me.ryandw11.ultrabar.typemgr.Typemgr_1_11_R1;
import me.ryandw11.ultrabar.typemgr.Typemgr_1_12_R1;
import me.ryandw11.ultrabar.typemgr.Typemgr_1_13_R1;
import me.ryandw11.ultrabar.typemgr.Typemgr_1_13_R2;
import me.ryandw11.ultrabar.typemgr.Typemgr_1_14_R1;

/**
 * @author Ryandw11
 * Main Class
 */
public class UltraBar extends JavaPlugin{

	/**
	 * Contains all of the tracked ubossbars for anything.
	 * @since 2.1
	 */
	public static ArrayList<UBossBar> ubossbars;
	public static BossBar barMessage; //TODO remove static maybe?
	public static UltraBar plugin;
	public Typemgr mgr;
	public PlaceholderAPIDepend papi;
	public boolean worldguard = false;
	public boolean placeholderAPI;
	private ArrayList<Player> toggledPlayers;
	
	@Override
	public void onEnable(){
		plugin = this;
		ubossbars = new ArrayList<UBossBar>();
		toggledPlayers = new ArrayList<Player>();
		
		if(setupPlug()){
			loadMethod();
			registerConfig();
			getLogger().info(String.format("UltraBar is enabled and running fine! V: %s", getDescription().getVersion()));
			loadSched();
			if(Bukkit.getPluginManager().isPluginEnabled("WorldGuard")){
				getLogger().info("WorldGuard detected. WorldGuard addon activated");
				worldguard = true;
			}
		}
		else{
			getLogger().severe(ChatColor.RED + "UltraBar does not support the version you are currently on!");
			getLogger().info("This version is only for 1.11 - 1.14.3. Please download 1.4.9 in order to use the plugin for 1.9 - 1.10");
			getLogger().info("The plugin will now be disabled!");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		if(getConfig().getBoolean("bstats")){
			@SuppressWarnings("unused")
			Metrics metrics = new Metrics(this);
			getLogger().info("Bstat metrics for this plugin is enabled. Disable it in the config if you do not want it on.");
		}else{
			getLogger().info("Bstat metrics is disabled for this plugin.");
		}
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
			getLogger().info("PlaceholderAPI detected. PlaceholderAPI addon detected.");
			placeholderAPI = true;
		}
		else
			placeholderAPI = false;
		setupPlaceholderAPI();
		
		if(getConfig().getBoolean("update_checker")){
			UpdateChecker updater = new UpdateChecker(this, 20113);
			try {
				if (updater.checkForUpdates())
					getLogger().info("An update was found! New version: " + updater.getLatestVersion() + " download: " + updater.getResourceURL());
			} catch (Exception e) {
				getLogger().warning("Could not check for updates! Stacktrace:");
				e.printStackTrace();
			}
		} //End of update checker
	}
	
	
	@Override
	public void onDisable(){
		for(UBossBar b : ubossbars){
			if(b != null)
				b.delete();
		}
		if(barMessage != null){
			barMessage.setVisible(false);
			barMessage.removeAll();
			barMessage = null;
		}
		getLogger().info("UltraBar for 1.11 - 1.14.3 has been disabled correctly!"); // same thing
		
	}
	
	
	private void registerConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	public void loadMethod(){
		getCommand("bar").setExecutor(new NewBarCommand());
		getCommand("bar").setTabCompleter(new BarCommandTabCompleter());
		getCommand("utitle").setExecutor(new TitleCommands(this));
		getCommand("actionbar").setExecutor(new ActionBarCommands(this));
		getCommand("ultrabar").setExecutor(new Help(this));
		Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new OnDeath(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new OnCommand(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new OnChangeWorld(), this);
		
		
	}
	
	private void loadSched(){
		if(getConfig().getBoolean("BossBarMessages.Enabled")){
			BossBarSced b = new BossBarSced();
			b.startProgram();
		}
		if(getConfig().getBoolean("Title_Announcements.Enabled")){
			TitleSched ts = new TitleSched();
			ts.startProgram();
		}
		if(getConfig().getBoolean("Action_Announcements.Enabled")){
			ActionBarSched as = new ActionBarSched();
			as.startProgram();
		}
	}
	
	private boolean setupPlug() {

        String version;

        try {

            version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];

        } catch (ArrayIndexOutOfBoundsException w0w) {
            return false;
        }

        getLogger().info("Your server is running version " + version + "!");
        if(version.equals("v1_14_R1")) {
        	mgr = new Typemgr_1_14_R1();
        	if(getConfig().getBoolean("WorldGuardRegion.Enabled") && plugin.worldguard)
    			Bukkit.getServer().getPluginManager().registerEvents(new OnMove_1_13_R1(this), this);
        }
        else if (version.equals("v1_13_R2")) {
            
            mgr = new Typemgr_1_13_R2();
            if(getConfig().getBoolean("WorldGuardRegion.Enabled") && plugin.worldguard)
    			Bukkit.getServer().getPluginManager().registerEvents(new OnMove_1_13_R1(this), this);
        }
        else if (version.equals("v1_13_R1")) {
            
            mgr = new Typemgr_1_13_R1();
            if(getConfig().getBoolean("WorldGuardRegion.Enabled") && plugin.worldguard)
    			Bukkit.getServer().getPluginManager().registerEvents(new OnMove_1_13_R1(this), this);
        }
        else if (version.equals("v1_12_R1")) {
            
            mgr = new Typemgr_1_12_R1();
            if(getConfig().getBoolean("WorldGuardRegion.Enabled") && plugin.worldguard)
    			Bukkit.getServer().getPluginManager().registerEvents(new OnMove_1_12_R1(this), this);
        }
        else if (version.equals("v1_11_R1")) {
            
            mgr = new Typemgr_1_11_R1();
            if(getConfig().getBoolean("WorldGuardRegion.Enabled") && plugin.worldguard)
    			Bukkit.getServer().getPluginManager().registerEvents(new OnMove_1_12_R1(this), this);
        }
        
        return mgr != null;
    }
	
	private void setupPlaceholderAPI(){
		if(placeholderAPI){
			papi = new PAPIExists();
			return;
		}
		papi = new PAPINotFound();
	}
	/**
	 * Grab the list of players that have the messages toggled.
	 * @return List of toggled
	 */
	public ArrayList<Player> getToggledPlayers(){
		return toggledPlayers;
	}
	
	/**
	 * Add a player to the list of messages toggled.
	 * @param p
	 */	
	public void addTogglePlayer(Player p){
		toggledPlayers.add(p);
	}
	/**
	 * Remove a player from the list of messages toggled.
	 * @param p
	 */
	public void removeTogglePlayer(Player p){
		toggledPlayers.remove(p);
	}
}

/*
 * ultrabar.title.send
 * ultrabar.title.sendall
 * ultrabar.subtitle.send
 * ultrabar.subtitle.sendall
 * ultrabar.bar.send
 * ultrabar.bar.sendall
 * ultrabar.help
 * ultrabar.reload
*/
