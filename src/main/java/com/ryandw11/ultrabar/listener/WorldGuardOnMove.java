package com.ryandw11.ultrabar.listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.ryandw11.ultrabar.UltraBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

/**
 * This class handles showing the region that the player is in.
 */
public class WorldGuardOnMove implements Listener {
    private final UltraBar plugin;

    public WorldGuardOnMove(UltraBar plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!plugin.getConfig().getBoolean("WorldGuardRegion.Enabled") || !plugin.worldguard) {
            return;
        }
        Player p = event.getPlayer();

        Location loc = new Location(BukkitAdapter.adapt(p.getWorld()), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(loc);
        if (set.size() > 1) {
            for (ProtectedRegion r : set) {
                String name = r.getId();
                plugin.typeManager.actionBar(p, plugin.chatColorUtil.translateChatColor(Objects.requireNonNull(plugin.getConfig().getString("WorldGuardRegion.Message2")).replace("%region%", name).replace("%number%", (set.size() - 1) + "")));
                break;
            }
        } else if (set.size() == 1) {
            for (ProtectedRegion r : set) {
                String name = r.getId();
                plugin.typeManager.actionBar(p, plugin.chatColorUtil.translateChatColor(plugin.getConfig().getString("WorldGuardRegion.Message").replace("%region%", name)));
                break;
            }
        }

    }

}
