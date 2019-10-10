package me.ryandw11.ultrabar.listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import me.ryandw11.ultrabar.api.bars.UBossBar;
import me.ryandw11.ultrabar.core.UltraBar;

public class OnChangeWorld implements Listener {
	
	@EventHandler
	public void onChange(PlayerChangedWorldEvent e) {
		for(UBossBar ub : UltraBar.trackedBars) {
			if(!ub.getPlayers().contains(e.getPlayer())) continue;
			if(ub.getParameters() != null && ub.getParameters().containsKey("clear")) {
				if(ub.getParameters().get("clear").equalsIgnoreCase("world"))
					ub.getBar().removePlayer(e.getPlayer());
			}
		}
	}

}
