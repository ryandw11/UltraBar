package me.ryandw11.ultrabar.schedulers;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ryandw11.ultrabar.api.UltraBarAPI;
import me.ryandw11.ultrabar.core.UltraBar;

public class ActionBarSched extends BukkitRunnable {

	private int numberOfMessages;
	private boolean randomOrder;
	private UltraBar plugin;
	private UltraBarAPI bapi;
	private int currentNum;
	private int lastNum;
	
	public ActionBarSched(){
		plugin = UltraBar.plugin;
		bapi = new UltraBarAPI();
		numberOfMessages = plugin.getConfig().getInt("Action_Announcements.Number_Of_Messages");
		randomOrder = plugin.getConfig().getBoolean("Action_Announcements.Random_Order");
		currentNum = 1;
		lastNum = 1;
		
	}
	
	public void startProgram(){
		if(numberOfMessages <= 0){
			plugin.getLogger().warning("Action_Announcements.Number_Of_Messages is set to 0! Please change enabled to false or add a message!");
			return;
		}
		
		this.runTaskTimer(plugin, 1, (plugin.getConfig().getInt("Action_Announcements.Time") * 20));
		
	}

	@Override
	public void run() {
		
		
		String msg = plugin.getConfig().getString("Action_Announcements." + currentNum + ".Message").replace('&', '§');
		for(Player p : Bukkit.getOnlinePlayers()){
			msg = plugin.papi.getMessage(msg, p);
			bapi.sendActionBar(p, msg);
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
