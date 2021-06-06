package me.ryandw11.ultrabar.listener;

import java.util.List;
import java.util.Objects;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.ryandw11.ultrabar.GrabBarStyles;
import me.ryandw11.ultrabar.api.BossBarBuilder;
import me.ryandw11.ultrabar.api.UBossBar;
import me.ryandw11.ultrabar.api.UltraBarAPI;
import me.ryandw11.ultrabar.UltraBar;

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
					String msg = plugin.papiTranslate.getMessage(plugin.chatColorUtil.translateChatColor(plugin.getConfig().getString("OnCommand." + s + ".BossBar.Message")), e.getPlayer());
					BarColor color = GrabBarStyles.barColor(Objects.requireNonNull(plugin.getConfig().getString("OnCommand." + s + ".BossBar.Color")));
					BarStyle style = GrabBarStyles.barStyle(Objects.requireNonNull(plugin.getConfig().getString("OnCommand." + s + ".BossBar.Style")));
					int time = plugin.getConfig().getInt("OnCommand." + s + ".BossBar.Time");
					double health = plugin.getConfig().getDouble("OnCommand." + s + ".BossBar.Health");
					
					BossBarBuilder bbb = new BossBarBuilder(false);
					bbb.setSinglePlayer(e.getPlayer());
					bbb.setMessage(msg);
					bbb.setColor(color);
					bbb.setStyle(style);
					bbb.setTime(time);
					bbb.setProgress(health);
					UBossBar bb = bbb.build();
					UltraBar.trackedBars.add(bb);
				}
				if(plugin.getConfig().contains("OnCommand." + s + ".Title")){
					String msg = plugin.papiTranslate.getMessage(plugin.chatColorUtil.translateChatColor(plugin.getConfig().getString("OnCommand." + s + ".Title.Message")), e.getPlayer());
					String submsg = plugin.papiTranslate.getMessage(plugin.chatColorUtil.translateChatColor(plugin.getConfig().getString("OnCommand." + s + ".Title.SubTitle")), e.getPlayer());
					int fadein = plugin.getConfig().getInt("OnCommand." + s + ".Title.fadein");
					int fadeout = plugin.getConfig().getInt("OnCommand." + s + ".Title.fadeout");
					int time = plugin.getConfig().getInt("OnCommand." + s + ".Title.time");
					
					bapi.sendTitle(e.getPlayer(), msg, submsg, fadein, time, fadeout);
				}
				if(plugin.getConfig().contains("OnCommand." + s + ".ActionBar")){
					String msg = plugin.papiTranslate.getMessage(plugin.chatColorUtil.translateChatColor(plugin.getConfig().getString("OnCommand." + s +".ActionBar.Message")), e.getPlayer());
					
					bapi.sendActionBar(e.getPlayer(), msg);
				}
			}
		}
	}
}
