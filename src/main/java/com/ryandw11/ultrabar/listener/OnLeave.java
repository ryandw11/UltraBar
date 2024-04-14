package com.ryandw11.ultrabar.listener;

import com.ryandw11.ultrabar.api.UBossBar;
import com.ryandw11.ultrabar.UltraBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Remove player's boss bars when they leave.
 */
public class OnLeave implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent evt) {
        for (UBossBar ub : UltraBar.trackedBars) {
            if (ub.getPlayers().contains(evt.getPlayer()))
                ub.removePlayer(evt.getPlayer());
        }
        // Remove the player from the list of toggled players.
        UltraBar.plugin.getToggledPlayers().remove(evt.getPlayer());

        // Remove the player from the bar announcer.
        if (UltraBar.plugin.getBarAnnouncer() != null) {
            UltraBar.plugin.getBarAnnouncer().removePlayer(evt.getPlayer());
        }
    }
}
