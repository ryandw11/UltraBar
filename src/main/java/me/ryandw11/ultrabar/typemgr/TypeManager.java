package me.ryandw11.ultrabar.typemgr;

import org.bukkit.entity.Player;

/**
 * Provide support for older versions.
 */
public interface TypeManager {
	void title(String message, Player p, int fadein, int time, int fadeout, String subtitle);
	
	void actionBar(Player player, String message);
	
}
