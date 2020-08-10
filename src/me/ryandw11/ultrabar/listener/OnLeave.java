package me.ryandw11.ultrabar.listener;

import me.ryandw11.ultrabar.api.UBossBar;
import me.ryandw11.ultrabar.UltraBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Remove player's boss bars when they leave.
 */
public class OnLeave implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent evt){
        for(UBossBar ub : UltraBar.trackedBars) {
            if(ub.getPlayers().contains(evt.getPlayer()))
                ub.removePlayer(evt.getPlayer());
        }
    }
}
