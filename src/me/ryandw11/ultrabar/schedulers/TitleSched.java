package me.ryandw11.ultrabar.schedulers;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ryandw11.ultrabar.api.UltraBarAPI;
import me.ryandw11.ultrabar.core.UltraBar;

public class TitleSched extends BukkitRunnable {
	
	private int numberOfMessages;
	private boolean randomOrder;
	private UltraBar plugin;
	private UltraBarAPI bapi;
	private int currentNum;
	private int lastNum;
	
	public TitleSched(){
		plugin = UltraBar.plugin;
		bapi = new UltraBarAPI();
		numberOfMessages = plugin.getConfig().getInt("Title_Announcements.Number_Of_Messages");
		randomOrder = plugin.getConfig().getBoolean("Title_Announcements.Random_Order");
		currentNum = 1;
		lastNum = 1;
		
	}
	
	public void startProgram(){
		if(numberOfMessages <= 0){
			plugin.getLogger().warning("Title_Announcements.Number_Of_Messages is set to 0! Please change enabled to false or add a message!");
			return;
		}
		
		this.runTaskTimer(plugin, 1, (plugin.getConfig().getInt("Title_Announcements.Time") * 20));
		
	}

	@Override
	public void run() {
		
		
		String title = plugin.chatColorUtil.translateChatColor(plugin.getConfig().getString("Title_Announcements." + currentNum + ".Message"));
		String subtitle = plugin.chatColorUtil.translateChatColor(plugin.getConfig().getString("Title_Announcements." + currentNum + ".SubTitle"));
		int fadein = plugin.getConfig().getInt("Title_Announcements." + currentNum + ".fadein");
		int fadeout = plugin.getConfig().getInt("Title_Announcements." + currentNum + ".fadeout");
		int time = plugin.getConfig().getInt("Title_Announcements." + currentNum + ".time");
		for(Player p : Bukkit.getOnlinePlayers()){
			title = plugin.papi.getMessage(title, p);
			subtitle = plugin.papi.getMessage(subtitle, p);
			bapi.sendTitle(p, title, subtitle, fadein, time, fadeout);
		}
		
		lastNum = currentNum;
    	currentNum += 1;
    	
		if(currentNum > numberOfMessages){
			currentNum = 1;
		}
		
		if(randomOrder){
    		Random rand = new Random();
    		int n = lastNum;
    		while(n == lastNum){
    			n = rand.nextInt(numberOfMessages) + 1;
    		}
    		currentNum = n;
    	}
		
	}

}
