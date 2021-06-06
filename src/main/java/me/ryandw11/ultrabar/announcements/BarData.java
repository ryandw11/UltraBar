package me.ryandw11.ultrabar.announcements;

import me.ryandw11.ultrabar.UltraBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

/**
 * An internal class that is used to store information about a bar.
 */
public class BarData {

    private final BarColor barColor;
    private final BarStyle barStyle;
    private final String title;

    private int time;

    /**
     * Construct the bar data.
     *
     * @param title    The title.
     * @param barColor The bar color.
     * @param barStyle The bar style.
     * @param time     The time.
     */
    public BarData(String title, BarColor barColor, BarStyle barStyle, int time) {
        this.title = title;
        this.barColor = barColor;
        this.barStyle = barStyle;
        this.time = time;
    }

    /**
     * Get the bar color.
     *
     * @return The bar color.
     */
    public BarColor getBarColor() {
        return barColor;
    }

    /**
     * Get the bar style.
     *
     * @return The bar style.
     */
    public BarStyle getBarStyle() {
        return barStyle;
    }

    /**
     * Get the time for the bar.
     *
     * @return The bar time.
     */
    public int getTime() {
        return time;
    }

    /**
     * Set the time data for the bar.
     *
     * @param time The time to set.
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Change the time.
     *
     * @param deltaTime The delta time to change by.
     */
    public void changeTime(int deltaTime) {
        this.time += deltaTime;
    }


    /**
     * Get the String with chat color and placeholders already replaced.
     *
     * @param p The player.
     * @return The String with chat color and placeholders already replaced.
     */
    public String getTranslatedString(Player p) {
        return UltraBar.plugin.chatColorUtil.translateChatColor(UltraBar.plugin.papiTranslate.getMessage(title, p));
    }

}
