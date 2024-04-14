package com.ryandw11.ultrabar.listener;

import com.ryandw11.ultrabar.api.UBossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.ryandw11.ultrabar.UltraBar;

public class OnChangeWorld implements Listener {

    @EventHandler
    public void onChange(PlayerChangedWorldEvent e) {
		/*
			Remove players from bars that are not in that world.
			(This will also add them).
		 */
        for (UBossBar ub : UltraBar.trackedBars) {
            if (!ub.getPlayers().contains(e.getPlayer())) continue;
            if (ub.getParameters() != null && ub.getParameters().containsKey("clear")) {
                if (ub.getParameters().get("clear").equalsIgnoreCase("world"))
                    ub.getBar().removePlayer(e.getPlayer());
            }
        }
    }

}
