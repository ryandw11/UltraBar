package me.ryandw11.ultrabar.announcements;

import org.bukkit.entity.Player;

public interface Announcer {
    void startProgram();
    void stopProgram();
    void addPlayer(Player p);
    void removePlayer(Player p);
}
