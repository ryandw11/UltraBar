package me.ryandw11.ultrabar.api.bars;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import com.google.common.collect.Iterables;

import me.ryandw11.ultrabar.BossBarTimer;
import me.ryandw11.ultrabar.core.UltraBar;

/**
 * Clean way to create a bossbar via the plugin.
 * <p>The recommended way</p>
 * @author Ryandw11
 *
 */
public class BossBarBuilder {
	private String message;
	private BarColor color;
	private BarStyle style;
	private int time = -1;
	private double progress = -1;
	private Collection<Player> players;
	private World world;
	private boolean tracked;
	
	public BossBarBuilder(boolean tracked) {
		players = new ArrayList<Player>();
		this.tracked = tracked;
	}
	
	/**
	 * Set the message of the boss bar.
	 * <b>Required</b>
	 * @param s
	 */
	public void setMessage(String s) {
		message = s;
	}
	
	/**
	 * Set the color of the boss bar.
	 * <b>Required</b>
	 * @param color
	 */
	public void setColor(BarColor color) {
		this.color = color;
	}
	
	/**
	 * Set the style of the boss bar.
	 * <b>Required</b>
	 * @param style
	 */
	public void setStyle(BarStyle style) {
		this.style = style;
	}
	/**
	 * Set the time of the bossbar.
	 * <b>Required</b>
	 * @param time
	 */
	public void setTime(int time) {
		this.time = time;
	}
	
	/**
	 * Set the progress of the bossbar.
	 * <b>Required</b>
	 * @param progress
	 */
	public void setProgress(double progress) {
		this.progress = progress;
	}
	
	/**
	 * Set a single player to a bar.
	 * @param p The player
	 */
	public void setSinglePlayer(Player p) {
		players.add(p);
	}
	
	/**
	 * Set a collection of players.
	 * @param players The collection of players.
	 */
	public void setPlayerCollection(Collection<Player> players) {
		this.players = players;
	}
	
	/**
	 * Set the world a player/players need to be in.
	 * @param w
	 */
	public void setWorld(World w) {
		this.world = w;
	}
	
	/**
	 * If there is only one player associated with the bar.
	 * @return true or false
	 */
	public boolean isOnePlayer() {
		return players.size() == 1;
	}
	
	/**
	 * If there are any players associated witht he bar.
	 */
	public boolean hasPlayers() {
		return players.size() > 0;
	}
	
	/**
	 * Build the bar (aka, create it).
	 * @return true if successful, false if failed.
	 */
	public boolean build() {
		if(!hasPlayers()) return false;
		/*
		 * If there is only one player
		 */
		if(isOnePlayer()) {
			if(this.message == null) return false;
			if(this.color == null) return false;
			if(this.style == null) return false;
			if(this.progress < 0) return false;
			if(this.time < 0) return false;
			if(world != null) {
				if(Iterables.get(players, 0).getWorld() != world) {
					return false;
				}else {
					//Create the boss bar and add the player.
					BossBar b = Bukkit.createBossBar(message, color, style);
					b.setProgress(progress);
					b.addPlayer(Iterables.get(players, 0));
					BossBarTimer s = new BossBarTimer(b, time * 20, progress, message);
					s.runTaskTimer(UltraBar.plugin, 5L, 1L);
					if(tracked)
						UltraBar.bossbars.add(b);
				}
			} // If the world is not there.
			else {
				BossBar b = Bukkit.createBossBar(message, color, style);
				b.setProgress(progress);
				b.addPlayer(Iterables.get(players, 0));
				BossBarTimer s = new BossBarTimer(b, time * 20, progress, message);
				s.runTaskTimer(UltraBar.plugin, 5L, 1L);
				if(tracked)
					UltraBar.bossbars.add(b);
			}
		}
		/*
		 * Else
		 */
		else {
			if(world != null) {
				BossBar b = Bukkit.createBossBar(message, color, style);
				b.setProgress(progress);
				for(Player p : players) {
					if(p.getWorld() == this.world) {
						b.addPlayer(p);
					}
				}
				BossBarTimer s = new BossBarTimer(b, time * 20, progress, message);
				s.runTaskTimer(UltraBar.plugin, 5L, 1L);
				if(tracked)
					UltraBar.bossbars.add(b);
			}else {
				BossBar b = Bukkit.createBossBar(message, color, style);
				b.setProgress(progress);
				for(Player p : players) {
						b.addPlayer(p);
				}
				BossBarTimer s = new BossBarTimer(b, time * 20, progress, message);
				s.runTaskTimer(UltraBar.plugin, 5L, 1L);
				if(tracked)
					UltraBar.bossbars.add(b);
			}
		}
		return true;
	}
	
	/**
	 * Create a permanent bar.
	 * @return If successful.
	 */
	public boolean buildDead() {
		if(!hasPlayers()) return false;
		/*
		 * If there is only one player
		 */
		if(isOnePlayer()) {
			if(this.message == null) return false;
			if(this.color == null) return false;
			if(this.style == null) return false;
			if(this.progress < 0) return false;
			if(world != null) {
				if(Iterables.get(players, 0).getWorld() != world) {
					return false;
				}else {
					//Create the boss bar and add the player.
					BossBar b = Bukkit.createBossBar(message, color, style);
					b.setProgress(progress);
					b.addPlayer(Iterables.get(players, 0));
					if(tracked)
						UltraBar.bossbars.add(b);
				}
			} // If the world is not there.
			else {
				BossBar b = Bukkit.createBossBar(message, color, style);
				b.setProgress(progress);
				b.addPlayer(Iterables.get(players, 0));
				if(tracked)
					UltraBar.bossbars.add(b);
			}
		}
		/*
		 * Else
		 */
		else {
			if(world != null) {
				BossBar b = Bukkit.createBossBar(message, color, style);
				b.setProgress(progress);
				for(Player p : players) {
					if(p.getWorld() == this.world) {
						b.addPlayer(p);
					}
				}
				if(tracked)
					UltraBar.bossbars.add(b);
			}else {
				BossBar b = Bukkit.createBossBar(message, color, style);
				b.setProgress(progress);
				for(Player p : players) {
						b.addPlayer(p);
				}
				if(tracked)
					UltraBar.bossbars.add(b);
			}
		}
		return true;
	}
	
	
}
