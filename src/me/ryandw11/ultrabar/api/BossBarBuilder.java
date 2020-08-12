package me.ryandw11.ultrabar.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import com.google.common.collect.Iterables;

import me.ryandw11.ultrabar.BossBarTimer;
import me.ryandw11.ultrabar.UltraBar;

import javax.annotation.Nullable;

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
	private int id = -1;
	private Map<String, String> storedData;
	private String permission;
	
	/**
	 * The api for building bossbars.
	 * @param tracked If the UBossBar class should be stored in the active bars list once built.
	 */
	public BossBarBuilder(boolean tracked) {
		players = new ArrayList<>();
		this.tracked = tracked;
		this.storedData = new HashMap<>();
		permission = null;
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
	
	protected int getId() {
		return id;
	}
	
	/**
	 * Set the message of the boss bar.
	 * <b>Required</b>
	 * @param s
	 */
	public BossBarBuilder setMessage(String s) {
		message = s;
		return this;
	}
	
	/**
	 * Set the color of the boss bar.
	 * <b>Required</b>
	 * @param color
	 */
	public BossBarBuilder setColor(BarColor color) {
		this.color = color;
		return this;
	}
	
	/**
	 * Set the style of the boss bar.
	 * <b>Required</b>
	 * @param style
	 */
	public BossBarBuilder setStyle(BarStyle style) {
		this.style = style;
		return this;
	}
	/**
	 * Set the time of the bossbar.
	 * <b>Required</b>
	 * @param time
	 */
	public BossBarBuilder setTime(int time) {
		this.time = time;
		return this;
	}
	
	/**
	 * Set the progress of the bossbar.
	 * <b>Required</b>
	 * @param progress The progress of the boss bar.
	 */
	public BossBarBuilder setProgress(double progress) {
		this.progress = progress;
		return this;
	}
	
	/**
	 * Set a single player to a bar.
	 * @param p The player
	 */
	public BossBarBuilder setSinglePlayer(Player p) {
		players.add(p);
		return this;
	}
	
	/**
	 * Set a collection of players.
	 * <p>If a the permission string does not equal null, then only the players
	 * with the specified permission will get added.</p>
	 * @param players The collection of players.
	 */
	public BossBarBuilder setPlayerCollection(Collection<Player> players) {
		if(permission != null){
			for(Player p : players){
				if(p.hasPermission(permission)){
					this.players.add(p);
				}
			}
		}else{
			this.players = players;
		}
		return this;
	}
	
	/**
	 * Set the world a player/players need to be in.
	 * @param w
	 */
	public BossBarBuilder setWorld(World w) {
		this.world = w;
		return this;
	}
	
	/**
	 * Set the id of the bar. (Can be used to retrieve the bar later)
	 * @param id The int id.
	 */
	public BossBarBuilder setId(int id) {
		this.id = id;
		return this;
	}
	
	public BossBarBuilder setTracked(boolean value) {
		this.tracked = value;
		return this;
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
	
	public BossBarBuilder setPublicBar(boolean bool) {
		this.publicBar = bool;
		return this;
	}
	
	/**
	 * If there are any players associated witht he bar.
	 */
	public boolean hasPlayers() {
		return players.size() > 0;
	}
	
	/**
	 * Store data on the boss bar.
	 * @param key The key for the data.
	 * @param value The data itself.
	 */
	public void setData(String key, String value) {
		this.storedData.put(key, value);
	}
	
	protected Map<String, String> getData() {
		return this.storedData;
	}

	/**
	 * Sets the permission for the bossbar.
	 * <p>If the player list was already set, then players without the permission are removed.</p>
	 * @param permission The permission.
	 */
	public void setPermission(String permission){
		this.permission = permission;
		if(hasPlayers()){
			this.players = players.stream().filter(player -> player.hasPermission(permission)).collect(Collectors.toList());
		}
	}

	/**
	 * Get the permission of the bar.
	 * @return The permission.
	 */
	@Nullable
	public String getPermission(){
		return permission;
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
					s.runTaskTimer(UltraBar.plugin, 0, 1L);
					UBossBar bb = new UBossBar(this,b,s);
					bb.getTimer().setupTimer(bb);
					if(tracked)
						UltraBar.trackedBars.add(bb);
					return bb;
				}
			} // If the world is not there.
			else {
				BossBar b = Bukkit.createBossBar(message, color, style);
				b.setProgress(progress);
				b.addPlayer(Iterables.get(players, 0));
				BossBarTimer s = new BossBarTimer(time * 20, progress);
				s.runTaskTimer(UltraBar.plugin, 0, 1L);
				UBossBar bb = new UBossBar(this,b,s);
				bb.getTimer().setupTimer(bb);
				if(tracked)
					UltraBar.trackedBars.add(bb);
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
				s.runTaskTimer(UltraBar.plugin, 0, 1L);
				UBossBar bb = new UBossBar(this,b,s);
				bb.getTimer().setupTimer(bb);
				if(tracked)
					UltraBar.trackedBars.add(bb);
				return bb;
			}else {
				BossBar b = Bukkit.createBossBar(message, color, style);
				b.setProgress(progress);
				for(Player p : players) {
						b.addPlayer(p);
				}
				BossBarTimer s = new BossBarTimer(time * 20, progress);
				s.runTaskTimer(UltraBar.plugin, 0, 1L);
				UBossBar bb = new UBossBar(this,b,s);
				bb.getTimer().setupTimer(bb);
				if(tracked)
					UltraBar.trackedBars.add(bb);
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
						UltraBar.trackedBars.add(bb);
					return bb;
				}
			} // If the world is not there.
			else {
				BossBar b = Bukkit.createBossBar(message, color, style);
				b.setProgress(progress);
				b.addPlayer(Iterables.get(players, 0));
				UBossBar bb = new UBossBar(this, b, null);
				if(tracked)
					UltraBar.trackedBars.add(bb);
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
					UltraBar.trackedBars.add(bb);
				return bb;
			}else {
				BossBar b = Bukkit.createBossBar(message, color, style);
				b.setProgress(progress);
				for(Player p : players) {
						b.addPlayer(p);
				}
				UBossBar bb = new UBossBar(this, b, null);
				if(tracked)
					UltraBar.trackedBars.add(bb);
				return bb;
			}
		}
	}
	
	
}
