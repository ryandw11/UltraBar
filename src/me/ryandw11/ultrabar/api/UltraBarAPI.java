package me.ryandw11.ultrabar.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.ryandw11.ultrabar.api.bars.UBossBar;
import me.ryandw11.ultrabar.core.UltraBar;

/**
 * Removed depracted #sendBossBar in version 2.1.
 * {@link api.bars.BossBarBuilder}
 * @author Ryandw11
 *
 */
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
	 * Grab the active bossbars.
	 * @return A list of active bars.
	 */
	public List<UBossBar> getActiveBars(){
		return UltraBar.ubossbars;
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
	
	/**
	 * Find all active bars with a player.
	 * @param p The player you want to find bars for.
	 * @return Active bars for a player.
	 * @since 2.1
	 */
	public List<UBossBar> getBarsForPlayer(Player p) {
		List<UBossBar> output = new ArrayList<>();
		for(UBossBar bb : UltraBar.ubossbars) {
			if(bb.getPlayers().contains(p))
				output.add(bb);
		}
		
		return output;
	}
	
	/**
	 * Find all active bars with a certain id.
	 * @param id The int id of the bar.
	 * @return Active bars for the id.
	 * @since 2.1.1
	 */
	public List<UBossBar> getBarsWithId(int id){
		List<UBossBar> output = new ArrayList<>();
		for(UBossBar bb : UltraBar.ubossbars) {
			if(bb.getId() == id)
				output.add(bb);
		}
		return output;
	}
}
