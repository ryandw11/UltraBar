package me.ryandw11.ultrabar.listener;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.ryandw11.ultrabar.GrabBarStyles;
import me.ryandw11.ultrabar.api.UltraBarAPI;
import me.ryandw11.ultrabar.core.UltraBar;

public class OnCommand implements Listener{
	private UltraBar plugin;
	private UltraBarAPI bapi;
	public OnCommand(){
		this.plugin = UltraBar.plugin;
		bapi = new UltraBarAPI();
	}
	
	@EventHandler
	public void commandEvent(PlayerCommandPreprocessEvent e){
		List<String> cmds = plugin.getConfig().getStringList("OnCommand.list");
		for(String s : cmds){
			if(e.getMessage().startsWith("/" + s)){
				if(plugin.getConfig().contains("OnCommand." + s + ".BossBar")){
					String msg = plugin.papi.getMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("OnCommand." + s + ".BossBar.Message")), e.getPlayer());
					BarColor color = GrabBarStyles.barColor(plugin.getConfig().getString("OnCommand." + s + ".BossBar.Color"));
					BarStyle style = GrabBarStyles.barStyle(plugin.getConfig().getString("OnCommand." + s + ".BossBar.Style"));
					int time = plugin.getConfig().getInt("OnCommand." + s + ".BossBar.Time");
					double health = plugin.getConfig().getDouble("OnCommand." + s + ".BossBar.Health");
					
					bapi.sendBossBar(e.getPlayer(), msg, color, style, time, health);
				}
				if(plugin.getConfig().contains("OnCommand." + s + ".Title")){
					String msg = plugin.papi.getMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("OnCommand." + s + ".Title.Message")), e.getPlayer());
					String submsg = plugin.papi.getMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("OnCommand." + s + ".Title.SubTitle")), e.getPlayer());
					int fadein = plugin.getConfig().getInt("OnCommand." + s + ".Title.fadein");
					int fadeout = plugin.getConfig().getInt("OnCommand." + s + ".Title.fadeout");
					int time = plugin.getConfig().getInt("OnCommand." + s + ".Title.time");
					
					bapi.sendTitle(e.getPlayer(), msg, submsg, fadein, time, fadeout);
				}
				if(plugin.getConfig().contains("OnCommand." + s + ".ActionBar")){
					String msg = plugin.papi.getMessage( plugin.getConfig().getString("OnCommand." + s +".ActionBar.Message").replace('&', '�'), e.getPlayer());
					
					bapi.sendActionBar(e.getPlayer(), msg);
				}
			}
		}
	}
}
