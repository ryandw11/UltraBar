package me.ryandw11.ultrabar.depends;

import org.bukkit.entity.Player;

public class PAPINotFound implements PlaceholderAPIDepend {
    public String getMessage(String s, Player p) {
        return s;
    }
}
