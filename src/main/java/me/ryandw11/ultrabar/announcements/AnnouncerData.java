package me.ryandw11.ultrabar.announcements;

import me.ryandw11.ultrabar.UltraBar;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds information about the announcer settings.
 */
public class AnnouncerData {
    private List<String> worldWhiteList;
    private final boolean isRandomOrder;
    private final boolean noProgress;

    public AnnouncerData(UltraBar plugin) {
        worldWhiteList = new ArrayList<>();
        if (plugin.getConfig().getBoolean("BossBarMessages.World_Whitelist_Enabled")) {
            worldWhiteList = plugin.getConfig().getStringList("BossBarMessages.World_Whitelist");
        }

        isRandomOrder = plugin.getConfig().getBoolean("BossBarMessages.Random_Order");
        noProgress = plugin.getConfig().getBoolean("BossBarMessages.No_Progress");
    }

    /**
     * Check if a world is in a whitelist.
     *
     * @param w The world.
     * @return If the world is in the whitelist.
     */
    public boolean isWorldInWhitelist(World w) {
        if (worldWhiteList.isEmpty()) return true;
        return worldWhiteList.contains(w.getName());
    }

    /**
     * Check if boss bars should be done in a random order.
     *
     * @return If the boss bars should be done if a random order.
     */
    public boolean isRandomOrder() {
        return isRandomOrder;
    }

    /**
     * Check if the announcement bar should modify the bar's progress.
     *
     * @return If the announcement bar should modify the bar's progress.
     */
    public boolean isNoProgress() {
        return noProgress;
    }
}
