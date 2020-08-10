package me.ryandw11.ultrabar.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import me.ryandw11.ultrabar.UltraBar;

public class OnMove_1_13_R1 implements Listener {
	private UltraBar plugin;
	public OnMove_1_13_R1(UltraBar plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		if(!plugin.getConfig().getBoolean("WorldGuardRegion.Enabled") && !plugin.worldguard){
			return;
		}
		Player p = event.getPlayer();
		Location loc = new Location((Extent) p.getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionQuery query = container.createQuery();
		ApplicableRegionSet set = query.getApplicableRegions(loc);
		if(set.size() > 1){
			for (ProtectedRegion r : set) {
					String name = r.getId();
					plugin.mgr.actionBar(p, plugin.chatColorUtil.translateChatColor( plugin.getConfig().getString("WorldGuardRegion.Message2").replace("%region%", name).replace("%number%", (set.size() - 1) + "")));
					break;
				}
		}else if(set.size() == 1){
			for (ProtectedRegion r : set) {
				String name = r.getId();
				plugin.mgr.actionBar(p, plugin.chatColorUtil.translateChatColor( plugin.getConfig().getString("WorldGuardRegion.Message").replace("%region%", name)));
				break;
			}
		}
		
	}
	
}
