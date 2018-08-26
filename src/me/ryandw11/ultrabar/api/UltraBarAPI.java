package me.ryandw11.ultrabar.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import me.ryandw11.ultrabar.BossBarMessage;
import me.ryandw11.ultrabar.core.UltraBar;

public class UltraBarAPI {
	private UltraBar plugin;
	public UltraBarAPI (){
		plugin = UltraBar.plugin;
	}
	/**
	 * Send a title using the system.
	 * @param p
	 * @param title
	 * @param subtitle
	 * @param fadein
	 * @param time
	 * @param fadeout
	 */
	public void sendTitle(Player p, String title, String subtitle, int fadein, int time, int fadeout){
		plugin.mgr.title(title, p, fadein, time, fadeout, subtitle);
	}
	/**
	 * Send an action bar using the plugin. (Automaticly gets the server version. NMS is handled with the plugin).
	 * @param p
	 * @param message
	 */
	public void sendActionBar(Player p, String message){
		plugin.mgr.actionBar(p, message);
	}
	/**
	 * Send a boss bar to a certain player
	 * @param p
	 * @param message
	 * @param color
	 * @param bstyle
	 * @param time
	 * @param progress
	 */
	public void sendBossBar(Player p, String message, BarColor color, BarStyle bstyle, int time, double progress){
		BossBarMessage bbm = new BossBarMessage();
		bbm.createMessage(p, message, color, bstyle, time, progress);
	}
	/**
	 * Send a boss bar to a collection of players.
	 * @param p
	 * @param message
	 * @param color
	 * @param bstyle
	 * @param time
	 * @param progress
	 */
	public void sendBossBar(Collection<Player> p, String message, BarColor color, BarStyle bstyle, int time, double progress){
		BossBarMessage bbm = new BossBarMessage();
		bbm.createMessage(p, message, color, bstyle, time, progress);
	}
	/**
	 * Send a boss bar where the bar progress does not tick down.
	 * @param p
	 * @param message
	 * @param color
	 * @param bstyle
	 * @param time
	 * @param progress
	 */
	public void sendBossBarNoCountDown(Collection<Player> p, String message, BarColor color, BarStyle bstyle, int time, double progress){
		BossBarMessage bbm = new BossBarMessage();
		bbm.createMessage(p, message, color, bstyle, time, progress);
	}
	/**
	 * Grab the active bossbars.
	 * @return A list of possbars.
	 */
	@SuppressWarnings("static-access")
	public List<BossBar> getActiveBars(){
		return plugin.bossbars;
	}
	
	/**
	 * Grab the list of toggled players. (API Use)
	 * @return
	 */
	public ArrayList<Player> getToggledPlayers(){
		return plugin.getToggledPlayers();
	}
	
	/**
	 * Add a player to the toggled list. (API Use)
	 * @param p
	 */
	public void addToggledPlayer(Player p){
		plugin.addTogglePlayer(p);
	}
	
	/**
	 * Add a player to the toggled list. (API Use)
	 * @param p
	 */
	public void removeToggledPlayer(Player p){
		plugin.removeTogglePlayer(p);
	}
}
