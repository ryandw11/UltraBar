package me.ryandw11.ultrabar;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import me.ryandw11.ultrabar.core.UltraBar;

/**
 * Outdated way to make bossbars.
 * @author Ryan
 * @deprecated To be removed in future update. use {@link api.bars.BossBarBuilder}
 */
public class BossBarMessage {
	private UltraBar plugin;
	public BossBarMessage(){
		this.plugin = UltraBar.plugin;
	}
	private int time;
	public void createMessage(Collection<? extends Player> collection, String message, BarColor color, BarStyle notched10, int time, double progress){
		BossBar b = Bukkit.createBossBar(message, color, notched10);
		b.setProgress(progress);
		for(Player p : collection){
			b.addPlayer(p);
		}
		this.time = time * 20;
		BossBarTimer s = new BossBarTimer(b, this.time, 1, message);
		s.runTaskTimer(plugin, 5L, 1L);
		//UltraBar.bossbars.add(b);
	}
	public void createMessageAll(String message, BarColor color, BarStyle notched10, int time, double progress){
		BossBar b = Bukkit.createBossBar(message, color, notched10);
		b.setProgress(progress);
		for(Player p : Bukkit.getOnlinePlayers()){
			b.addPlayer(p);
		}
		this.time = time * 20;
		BossBarTimer s = new BossBarTimer(b, this.time, 1, message);
		s.runTaskTimer(plugin, 5L, 1L);
		UltraBar.bossbars.add(b);
	}
	public void createMessageAll(String message, BarColor color, BarStyle notched10, int time, double progress, World w){
		BossBar b = Bukkit.createBossBar(message, color, notched10);
		b.setProgress(progress);
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.getWorld() == w){
				b.addPlayer(p);
			}
		}
		this.time = time * 20;
		BossBarTimer s = new BossBarTimer(b, this.time, 1, message);
		s.runTaskTimer(plugin, 5L, 1L);
		UltraBar.bossbars.add(b);
	}
	public void createMessage(Player p, String message, BarColor color, BarStyle notched10, int time, double progress){
		BossBar b = Bukkit.createBossBar(message, color, notched10);
		b.setProgress(progress);
		b.addPlayer(p);
			
		this.time = time * 20;
		BossBarTimer s = new BossBarTimer(b, this.time, progress, message);
		s.runTaskTimer(plugin, 5L, 1L);
		//UltraBar.bossbars.add(b);
	}
	
	public void createMessageJoin(Player p, String message, BarColor color, BarStyle notched10, int time, double progress){
		BossBar b = Bukkit.createBossBar(message, color, notched10);
		b.setProgress(progress);
		b.addPlayer(p);
			
		this.time = time * 20;
		BossBarTimer s = new BossBarTimer(b, this.time, progress, message);
		s.runTaskTimer(plugin, 5L, 1L);
	}
	
	public void createMessage(Collection<? extends Player> collection, String message, BarColor color, BarStyle notched10, int time, double progress, World world){
		BossBar b = Bukkit.createBossBar(message, color, notched10);
		b.setProgress(progress);
		for(Player p : collection){
			if(p.getWorld() == world){
				b.addPlayer(p);
			}
		}
		this.time = time * 20;
		BossBarTimer s = new BossBarTimer(b, this.time, 1, message);
		s.runTaskTimer(plugin, 5L, 1L);
	}
	public void createPermMessageWorld(Collection<? extends Player> collection, String message, BarColor color, BarStyle notched10, double progress, World world){
		BossBar b = Bukkit.createBossBar(message, color, notched10);
		b.setProgress(progress);
		for(Player p : collection){
			if(p.getWorld() == world){
				b.addPlayer(p);
			}
		}
	}
	public void createPermMessage(Collection<? extends Player> collection, String message, BarColor color, BarStyle notched10, double progress){
		BossBar b = Bukkit.createBossBar(message, color, notched10);
		b.setProgress(progress);
		for(Player p : collection){
			b.addPlayer(p);
		}
		UltraBar.bossbars.add(b);
	}
}
