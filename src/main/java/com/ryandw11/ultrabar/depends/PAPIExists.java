package com.ryandw11.ultrabar.depends;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import com.ryandw11.ultrabar.UltraBar;

public class PAPIExists implements PlaceholderAPIDepend {
    public String getMessage(String s, Player p) {
        String product;
        try {
            product = PlaceholderAPI.setPlaceholders(p, s);
        } catch (Exception e) {
            Bukkit.getLogger().warning("Player specific placeholders do not work on server wide messages!");
            Bukkit.getLogger().warning("The plugin UltraBar will now disable it self due to this.");
            Bukkit.getPluginManager().disablePlugin(UltraBar.plugin);
            product = s;
        }
        return product;
    }
}
