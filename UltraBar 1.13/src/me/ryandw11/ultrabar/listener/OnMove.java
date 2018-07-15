package me.ryandw11.ultrabar.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.ryandw11.ultrabar.core.UltraBar;

public class OnMove implements Listener {
	private UltraBar plugin;
	public OnMove(UltraBar plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		if(!plugin.getConfig().getBoolean("WorldGuardRegion.Enabled") && plugin.worldguard != true){
			return;
		}
		Player p = event.getPlayer();
		ApplicableRegionSet set = WGBukkit.getRegionManager(p.getWorld()).getApplicableRegions(p.getLocation());
		if(set.size() > 1){
			for (ProtectedRegion r : set) {
					String name = r.getId();
					plugin.mgr.actionBar(p, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("WorldGuardRegion.Message2").replace("%region%", name).replace("%number%", (set.size() - 1) + "")));
					break;
				}
		}else if(set.size() == 1){
			for (ProtectedRegion r : set) {
				String name = r.getId();
				plugin.mgr.actionBar(p, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("WorldGuardRegion.Message").replace("%region%", name)));
				break;
			}
		}
		
	}
	
}
