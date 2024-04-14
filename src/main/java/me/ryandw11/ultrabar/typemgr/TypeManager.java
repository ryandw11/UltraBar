package me.ryandw11.ultrabar.typemgr;

import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

/**
 * Provide support for older versions.
 *
 * TODO: Remove this.
 */
public interface TypeManager {
    void title(String message, Player p, int fadein, int time, int fadeout, String subtitle);

    void actionBar(Player player, String message);

    /**
     * Create a boss bar.
     *
     * @param key   The NamespacedKey to use (not used on 1.12).
     * @param title The title.
     * @param color The bar color.
     * @param style The style of the boss bar.
     * @param flags The flags of the boss bar.
     */
    BossBar createBossBar(NamespacedKey key, String title, BarColor color, BarStyle style, BarFlag... flags);

}
