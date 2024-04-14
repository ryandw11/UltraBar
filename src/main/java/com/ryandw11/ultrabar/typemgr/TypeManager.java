package com.ryandw11.ultrabar.typemgr;

import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

/**
 * Provide support for older versions.
 *
 * <p>As of version 2.4.0, UltraBar no longer offers support for older versions of Spigot without
 * built-in Title and ActionBar support. There is only one implementation of this interface.</p>
 */
public interface TypeManager {
    void title(String message, Player p, int fadein, int time, int fadeout, String subtitle);

    void actionBar(Player player, String message);

    /**
     * Create a boss bar.
     *
     * @param key   The NamespacedKey to use
     * @param title The title.
     * @param color The bar color.
     * @param style The style of the boss bar.
     * @param flags The flags of the boss bar.
     */
    BossBar createBossBar(NamespacedKey key, String title, BarColor color, BarStyle style, BarFlag... flags);

}
