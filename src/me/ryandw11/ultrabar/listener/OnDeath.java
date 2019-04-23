package me.ryandw11.ultrabar.listener;

import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.ryandw11.ultrabar.GrabBarStyles;
import me.ryandw11.ultrabar.api.UltraBarAPI;
import me.ryandw11.ultrabar.core.UltraBar;

public class OnDeath implements Listener{
	private UltraBar plugin;
	private UltraBarAPI bapi;
	public OnDeath(){
		this.plugin = UltraBar.plugin;
		bapi = new UltraBarAPI();
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		if(plugin.getConfig().getBoolean("OnDeath.BossBar.Enabled")){
			String msg = plugin.papi.getMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("OnDeath.BossBar.Message")), e.getEntity());
			BarColor color = GrabBarStyles.barColor(plugin.getConfig().getString("OnDeath.BossBar.Color"));
			BarStyle style = GrabBarStyles.barStyle(plugin.getConfig().getString("OnDeath.BossBar.Style"));
			int time = plugin.getConfig().getInt("OnDeath.BossBar.Time");
			double health = plugin.getConfig().getDouble("OnDeath.BossBar.Health");
			
			bapi.sendBossBar(e.getEntity(), msg, color, style, time, health, -1);
		}
		if(plugin.getConfig().getBoolean("OnDeath.Title.Enabled")){
			String msg = plugin.papi.getMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("OnDeath.Title.Message")), e.getEntity());
			String submsg = plugin.papi.getMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("OnDeath.Title.SubTitle")), e.getEntity());
			int fadein = plugin.getConfig().getInt("OnDeath.Title.fadein");
			int fadeout = plugin.getConfig().getInt("OnDeath.Title.fadeout");
			int time = plugin.getConfig().getInt("OnDeath.Title.time");
			
			bapi.sendTitle(e.getEntity(), msg, submsg, fadein, time, fadeout);
		}
		if(plugin.getConfig().getBoolean("OnDeath.ActionBar.Enabled")){
			String msg = plugin.papi.getMessage(plugin.getConfig().getString("OnDeath.ActionBar.Message").replace('&', '§'), e.getEntity());
			
			bapi.sendActionBar(e.getEntity(), msg);
		}
	}
}
