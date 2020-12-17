package me.ryandw11.ultrabar.typemgr;

import org.bukkit.entity.Player;

public interface Typemgr {
	public void title(String message, Player p, int fadein, int time, int fadeout, String subtitle);
	
	public void actionBar(Player player, String message);
	
}
