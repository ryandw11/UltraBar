package me.ryandw11.ultrabar.announcements;

import org.bukkit.entity.Player;

/**
 * An interface that could allow different types of announcers in the future.
 */
public interface Announcer {
    /**
     * Start the program.
     */
    void startProgram();

    /**
     * Stop the program.
     */
    void stopProgram();

    /**
     * Add a player.
     *
     * @param p The player to add.
     */
    void addPlayer(Player p);

    /**
     * Remove a player.
     *
     * @param p The player to remove.
     */
    void removePlayer(Player p);
}
