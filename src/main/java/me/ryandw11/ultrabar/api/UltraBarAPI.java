package me.ryandw11.ultrabar.api;

import me.ryandw11.ultrabar.UltraBar;
import me.ryandw11.ultrabar.api.parameters.BarParameter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This is the main api class for UltraBar.
 * Please see {@link BossBarBuilder} for the creation of boss bars.
 *
 * @author Ryandw11
 */
public final class UltraBarAPI {
    private final UltraBar plugin;

    public UltraBarAPI() {
        plugin = UltraBar.plugin;
    }

    /**
     * Send a title using the system.
     *
     * @param p        The player
     * @param title    The title
     * @param subtitle The subtitle
     * @param fadein   The time it should take to fade in
     * @param time     The amount of time the title should be on the screen.
     * @param fadeout  The time it should take to fade out.
     */
    public void sendTitle(Player p, String title, String subtitle, int fadein, int time, int fadeout) {
        plugin.typeManager.title(title, p, fadein, time, fadeout, subtitle);
    }

    /**
     * Send an action bar using the plugin. (Automatically gets the server version. NMS is handled with the plugin).
     *
     * @param p       The player to send the action bar to.
     * @param message The message.
     */
    public void sendActionBar(Player p, String message) {
        plugin.typeManager.actionBar(p, message);
    }

    /**
     * Grab the active bossbars.
     *
     * @return A list of active bars.
     * @since 2.1.3
     */
    public List<UBossBar> getTrackedBars() {
        return UltraBar.trackedBars;
    }

    /**
     * Grab the list of toggled players. (API Use)
     *
     * @return The list of toggled players.
     */
    public List<Player> getToggledPlayers() {
        return plugin.getToggledPlayers();
    }

    /**
     * Add a player to the toggled list. (API Use)
     *
     * @param p The player to add.
     */
    public void addToggledPlayer(Player p) {
        plugin.addTogglePlayer(p);
    }

    /**
     * Add a player to the toggled list. (API Use)
     *
     * @param p The player to remove.
     */
    public void removeToggledPlayer(Player p) {
        plugin.removeTogglePlayer(p);
    }

    /**
     * Find all active bars with a player.
     *
     * @param p The player you want to find bars for.
     * @return Active bars for a player.
     * @since 2.1
     */
    public List<UBossBar> getBarsForPlayer(Player p) {
        List<UBossBar> output = new ArrayList<>();
        for (UBossBar bb : UltraBar.trackedBars) {
            if (bb.getPlayers().contains(p))
                output.add(bb);
        }

        return output;
    }

    /**
     * Find all active bars with a certain id.
     *
     * @param id The int id of the bar.
     * @return Active bars for the id.
     * @since 2.1.1
     */
    public List<UBossBar> getBarsWithId(int id) {
        List<UBossBar> output = new ArrayList<>();
        for (UBossBar bb : UltraBar.trackedBars) {
            if (bb.getId() == id)
                output.add(bb);
        }
        return output;
    }

    /**
     * Register a bar parameter.
     *
     * @param bp The parameter to register.
     */
    public void registerParameter(BarParameter bp) {
        plugin.registerParameter(bp);
    }

    /**
     * Get a bar with a certain pid.
     *
     * @param pid The UUID of the bar.
     * @return The bar that was found. (Null if none).
     */
    public UBossBar getBarWithPid(UUID pid) {
        for (UBossBar bb : UltraBar.trackedBars) {
            if (bb.getPID() == pid)
                return bb;
        }
        return null;
    }

    /**
     * Delete a bar.
     *
     * @param bar Delete a bar.
     */
    public void deleteBar(UBossBar bar) {
        bar.getTimer().ifPresent(BukkitRunnable::cancel);
        bar.bar.setVisible(false);
        bar.bar = null;
        bar.timer = null;
        UltraBar.trackedBars.remove(bar);
    }
}
