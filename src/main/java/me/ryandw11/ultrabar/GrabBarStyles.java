package me.ryandw11.ultrabar;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

/**
 * This class is responsible for grabbing the bar styles.
 */
public class GrabBarStyles {
    /**
     * Get the bar color from a String.
     *
     * @param color The color in String form.
     * @return The BarColor Enum. (Default is Purple).
     */
    public static BarColor barColor(String color) {
        try {
            return BarColor.valueOf(color.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return BarColor.PURPLE;
        }
    }

    /**
     * Grab the BarStyle enum from a string.
     *
     * @param style The style.
     * @return The enum. (Default Solid).
     */
    public static BarStyle barStyle(String style) {
        switch (style) {
            case "6":
                return BarStyle.SEGMENTED_6;
            case "10":
                return BarStyle.SEGMENTED_10;
            case "12":
                return BarStyle.SEGMENTED_12;
            case "20":
                return BarStyle.SEGMENTED_20;
            default:
                return BarStyle.SOLID;
        }
    }
}
