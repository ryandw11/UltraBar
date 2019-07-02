package me.ryandw11.ultrabar.api.bars;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import me.ryandw11.ultrabar.BossBarTimer;
import me.ryandw11.ultrabar.core.UltraBar;

/**
 * Any active bossbar on the server.
 * @author Ryandw11
 * @since 2.1
 *
 */
public class UBossBar {
	private String message;
	private BarColor color;
	private BarStyle style;
	private int time = -1;
	private double progress = -1;
	private Collection<Player> players;
	private World world;
	private boolean tracked;
	private BossBarTimer timer;
	private BossBar bar;
	private boolean publicBar;
	
	private Map<String, String> parameters;
	
	/**
	 *
	 * @param bbb The instance of the builder.
	 * @param timer The time (Can be null)
	 * 
	 */
	public UBossBar(BossBarBuilder bbb, BossBar bar, @Nullable BossBarTimer timer) {
		this.setMessage(bbb.getMessage());
		this.setColor(bbb.getColor());
		this.setStyle(bbb.getStyle());
		this.setTime(bbb.getTime());
		this.setProgress(bbb.getProgress());
		this.setPlayers(bbb.getPlayers());
		this.setWorld(bbb.getWorld());
		this.setTracked(bbb.getTracked());
		this.timer = timer;
		this.bar = bar;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BarColor getColor() {
		return color;
	}

	public void setColor(BarColor color) {
		this.color = color;
	}

	public BarStyle getStyle() {
		return style;
	}

	public void setStyle(BarStyle style) {
		this.style = style;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	/**
	 * Get the players.
	 * <p><b>Note:</b> If you want to remove players use {@link #removePlayer(Player)}</p>
	 * @return A collection of players.
	 */
	public Collection<Player> getPlayers() {
		return players;
	}
	
	/**
	 * A way to remove players as #remove does not work on Collections.
	 * @param p The player.
	 */
	public void removePlayer(Player p) {
		List<Player> temp = new LinkedList<Player>(players);
		temp.remove(p);
		players = temp;
	}
	
	/**
	 * Add a player to the player Collection
	 * @param p the player
	 */
	public void addPlayer(Player p) {
		List<Player> temp = new LinkedList<Player>(players);
		temp.add(p);
		players = temp;
	}

	public void setPlayers(Collection<Player> players) {
		this.players = players;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public boolean isTracked() {
		return tracked;
	}

	public void setTracked(boolean tracked) {
		this.tracked = tracked;
	}

	/**
	 * Get the timer asociated with the bar.
	 * @return The BossBarTimer class. (Null if the bar has not BossBarTimer associated)
	 */
	public BossBarTimer getTimer() {
		return timer;
	}

	public BossBar getBar() {
		return bar;
	}

	public void setBar(BossBar bar) {
		this.bar = bar;
	}
	
	/**
	 * Is null if the command was not use to form this bar.
	 * @return Parameters or null.
	 */
	public Map<String, String> getParameters(){
		return parameters;
	}
	
	public void setParameters(Map<String, String> param) {
		this.parameters = param;
	}
	
	/**
	 * Update the list of players on the live bar.
	 */
	public void updatePlayers() {
		bar.removeAll();
		for(Player p : players) {
			bar.addPlayer(p);
		}
	}
	
	/**
	 * Delete the bossbar. Once completed this action can not be undone.
	 */
	public void delete() {
		if(this.timer != null) {
			this.timer.cancel();
		}
		
		this.bar.setVisible(false);
		if(UltraBar.ubossbars.contains(this)) {
			UltraBar.ubossbars.remove(this);
		}
	}

	/**
	 * If the bar is a public bar.
	 * @return true or false.
	 */
	public boolean isPublicBar() {
		return publicBar;
	}

}
