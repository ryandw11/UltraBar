package me.ryandw11.ultrabar.announcements;

import me.ryandw11.ultrabar.UltraBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

/**
 * An internal class that is used to store information about a bar.
 */
public class BarData {

    private BarColor barColor;
    private BarStyle barStyle;
    private String title;

    private int time;

    public BarData(String title, BarColor barColor, BarStyle barStyle, int time) {
        this.title = title;
        this.barColor = barColor;
        this.barStyle = barStyle;
        this.time = time;
    }

    public BarColor getBarColor() {
        return barColor;
    }

    public BarStyle getBarStyle() {
        return barStyle;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

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
        return UltraBar.plugin.chatColorUtil.translateChatColor(UltraBar.plugin.papi.getMessage(title, p));
    }

}
