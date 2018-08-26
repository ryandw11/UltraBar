package me.ryandw11.ultrabar.listener;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.ryandw11.ultrabar.BossBarMessage;
import me.ryandw11.ultrabar.GrabBarStyles;
import me.ryandw11.ultrabar.core.UltraBar;

public class OnJoin implements Listener {

	private UltraBar plugin;
	public OnJoin(UltraBar plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		Player p = event.getPlayer();
		
		if(plugin.getConfig().getBoolean("OnJoin.BossBar.Enabled")){
			BarColor color = GrabBarStyles.barColor(plugin.getConfig().getString("OnJoin.BossBar.Color"));
			BarStyle style = GrabBarStyles.barStyle(plugin.getConfig().getString("OnJoin.BossBar.Style"));
			String message = plugin.papi.getMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("OnJoin.BossBar.Message").replace("{player}", p.getName())), p);
			int time = plugin.getConfig().getInt("OnJoin.BossBar.Time");
			double progress = plugin.getConfig().getDouble("OnJoin.BossBar.Health");
			
			BossBarMessage b = new BossBarMessage();
			b.createMessageJoin(p, message, color, style, time, progress);
		}
		if(plugin.getConfig().getBoolean("OnJoin.Title.Enabled")){
			String message = plugin.papi.getMessage(plugin.getConfig().getString("OnJoin.Title.Message"), p);
			String submessage = plugin.getConfig().getString("OnJoin.Title.SubTitle");
			int fadein = plugin.getConfig().getInt("OnJoin.Title.fadein");
			int fadeout = plugin.getConfig().getInt("OnJoin.Title.fadeout");
			int time = plugin.getConfig().getInt("OnJoin.Title.time");
			
			plugin.mgr.title(message.replace("&", "§").replace("{player}", p.getName()), p, fadein, time, fadeout, submessage.replace("&", "§"));
			
		}
		if(plugin.getConfig().getBoolean("OnJoin.ActionBar.Enabled")){
			String message = plugin.papi.getMessage(plugin.getConfig().getString("OnJoin.ActionBar.Message"), p);
			plugin.mgr.actionBar(p, message.replace("&", "§").replace("{player}", p.getName()));
		}
		
		/*
		 * Give the player all of the boss bars.
		 */
		for(BossBar b : UltraBar.bossbars){
			b.addPlayer(p);
		}
		if(!plugin.getConfig().getBoolean("BossBarMessages.World_Whitelist_Enabled") && plugin.getConfig().getBoolean("BossBarMessages.Enabled") && UltraBar.barMessage != null)
			UltraBar.barMessage.addPlayer(p);
		
	}
	
}
