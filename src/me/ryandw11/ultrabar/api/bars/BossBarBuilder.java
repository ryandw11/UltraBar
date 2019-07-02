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
	private boolean publicBar = false;
	
	/**
	 * The api for building bossbars.
	 * @param tracked If the UBossBar class should be stored in the active bars list once built.
	 */
	public BossBarBuilder(boolean tracked) {
		players = new ArrayList<Player>();
		this.tracked = tracked;
	}
	
	protected String getMessage() {
		return message;
	}
	
	protected BarColor getColor() {
		return color;
	}
	
	protected BarStyle getStyle() {
		return style;
	}
	
	protected int getTime() {
		return time;
	}
	
	protected double getProgress() {
		return progress;
	}
	
	protected Collection<Player> getPlayers(){
		return players;
	}
	
	protected World getWorld() {
		return world;
	}
	
	protected boolean getTracked() {
		return tracked;
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
	
	public boolean getPublicBar() {
		return this.publicBar;
	}
	
	public void setPublicBar(boolean bool) {
		this.publicBar = bool;
	}
	
	/**
	 * If there are any players associated witht he bar.
	 */
	public boolean hasPlayers() {
		return players.size() > 0;
	}
	
	/**
	 * Build the bar (aka, create it).
	 * @return The UBossBar class. <b>Returns null if the setup is invalid</b>
	 */
	public UBossBar build() {
		if(!hasPlayers()) return null;
		/*
		 * If there is only one player
		 */
		if(isOnePlayer()) {
			if(this.message == null) return null;
			if(this.color == null) return null;
			if(this.style == null) return null;
			if(this.progress < 0) return null;
			if(this.time < 0) return null;
			if(world != null) {
				if(Iterables.get(players, 0).getWorld() != world) {
					return null;
				}else {
					//Create the boss bar and add the player.
					BossBar b = Bukkit.createBossBar(message, color, style);
					b.setProgress(progress);
					b.addPlayer(Iterables.get(players, 0));
					BossBarTimer s = new BossBarTimer(time * 20, progress);
					s.runTaskTimer(UltraBar.plugin, 5L, 1L);
					UBossBar bb = new UBossBar(this,b,s);
					bb.getTimer().setupTimer(bb);
					if(tracked)
						UltraBar.ubossbars.add(bb);
					return bb;
				}
			} // If the world is not there.
			else {
				BossBar b = Bukkit.createBossBar(message, color, style);
				b.setProgress(progress);
				b.addPlayer(Iterables.get(players, 0));
				BossBarTimer s = new BossBarTimer(time * 20, progress);
				s.runTaskTimer(UltraBar.plugin, 5L, 1L);
				UBossBar bb = new UBossBar(this,b,s);
				bb.getTimer().setupTimer(bb);
				if(tracked)
					UltraBar.ubossbars.add(bb);
				return bb;
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
				BossBarTimer s = new BossBarTimer(time * 20, progress);
				s.runTaskTimer(UltraBar.plugin, 5L, 1L);
				UBossBar bb = new UBossBar(this,b,s);
				bb.getTimer().setupTimer(bb);
				if(tracked)
					UltraBar.ubossbars.add(bb);
				return bb;
			}else {
				BossBar b = Bukkit.createBossBar(message, color, style);
				b.setProgress(progress);
				for(Player p : players) {
						b.addPlayer(p);
				}
				BossBarTimer s = new BossBarTimer(time * 20, progress);
				s.runTaskTimer(UltraBar.plugin, 5L, 1L);
				UBossBar bb = new UBossBar(this,b,s);
				bb.getTimer().setupTimer(bb);
				if(tracked)
					UltraBar.ubossbars.add(bb);
				return bb;
			}
		}
	}
	
	/**
	 * Create a permanent bar.
	 * @return The UBossBar class. <b>Returns null if the setup is invalid</b>
	 */
	public UBossBar buildDead() {
		if(!hasPlayers()) return null;
		/*
		 * If there is only one player
		 */
		if(isOnePlayer()) {
			if(this.message == null) return null;
			if(this.color == null) return null;
			if(this.style == null) return null;
			if(this.progress < 0) return null;
			if(world != null) {
				if(Iterables.get(players, 0).getWorld() != world) {
					return null;
				}else {
					//Create the boss bar and add the player.
					BossBar b = Bukkit.createBossBar(message, color, style);
					b.setProgress(progress);
					b.addPlayer(Iterables.get(players, 0));
					UBossBar bb = new UBossBar(this, b, null);
					if(tracked)
						UltraBar.ubossbars.add(bb);
					return bb;
				}
			} // If the world is not there.
			else {
				BossBar b = Bukkit.createBossBar(message, color, style);
				b.setProgress(progress);
				b.addPlayer(Iterables.get(players, 0));
				UBossBar bb = new UBossBar(this, b, null);
				if(tracked)
					UltraBar.ubossbars.add(bb);
				return bb;
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
				UBossBar bb = new UBossBar(this, b, null);
				if(tracked)
					UltraBar.ubossbars.add(bb);
				return bb;
			}else {
				BossBar b = Bukkit.createBossBar(message, color, style);
				b.setProgress(progress);
				for(Player p : players) {
						b.addPlayer(p);
				}
				UBossBar bb = new UBossBar(this, b, null);
				if(tracked)
					UltraBar.ubossbars.add(bb);
				return bb;
			}
		}
	}
	
	
}
