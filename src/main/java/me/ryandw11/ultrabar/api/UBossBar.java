package me.ryandw11.ultrabar.api;

import me.ryandw11.ultrabar.api.enums.CountStyle;
import me.ryandw11.ultrabar.timers.BossBarTimer;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Any active boss bar on the server.
 *
 * @author Ryandw11
 * @since 2.1
 */
public class UBossBar {
    protected int time = -1;
    protected BossBarTimer timer;
    protected BossBar bar;
    private String message;
    private BarColor color;
    private BarStyle style;
    private double progress = -1;
    private Collection<Player> players;
    private World world;
    private boolean tracked;
    private boolean publicBar;
    private int id = -1;
    private final UUID pid;
    private String permission;
    private final CountStyle countStyle;

    private Map<String, String> parameters;
    private final Map<String, String> storedData;

    /**
     * Create a new UBossBar.
     *
     * <p>This is only meant for internal and advanced use. Please use
     * {@link BossBarBuilder} to create boss bars.</p>
     *
     * @param bbb   The instance of the builder.
     * @param bar   The instance of the Bukkit Boss Bar.
     * @param timer The time (Can be null)
     */
    public UBossBar(BossBarBuilder bbb, BossBar bar, @Nullable BossBarTimer timer) {
        this.setMessage(bbb.getMessage());
        this.setColor(bbb.getColor());
        this.setStyle(bbb.getStyle());
        this.setTime(bbb.getTime());
        this.setProgress(bbb.getProgress());
        this.setPlayers(bbb.getPlayers());
        this.setWorld(bbb.getWorld());
        this.setTracked(bbb.getTracked());
        this.setPublic(bbb.getPublicBar());
        this.timer = timer;
        this.bar = bar;
        this.id = bbb.getId();
        this.storedData = bbb.getData();
        this.pid = UUID.randomUUID();
        this.permission = bbb.getPermission();
        this.countStyle = bbb.getCountStyle();
    }

    /**
     * Get the message of a boss bar.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message of a boss bar.
     *
     * @param message The message of a boss bar.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the color of the boss bar.
     *
     * @return The color.
     */
    public BarColor getColor() {
        return color;
    }

    /**
     * Set the color of the boss bar.
     *
     * @param color The color to set.
     */
    public void setColor(BarColor color) {
        this.color = color;
    }

    /**
     * Get the style of the boss bar.
     *
     * @return The style of the boss bar.
     */
    public BarStyle getStyle() {
        return style;
    }

    /**
     * SEt the style of the boss bar.
     *
     * @param style The style of the boss bar.
     */
    public void setStyle(BarStyle style) {
        this.style = style;
    }

    /**
     * Get the time of the boss bar.
     *
     * <p>This is the time that the bar originally started with.
     * This will not present the time left.</p>
     *
     * @return The time of the boss bar. (In Seconds).
     */
    public int getTime() {
        return time;
    }

    /**
     * Set the time of the boss bar.
     *
     * @param time The time of the boss bar. (In Seconds).
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Get the progress of the boss bar.
     *
     * @return The progress of the boss bar.
     */
    public double getProgress() {
        return progress;
    }

    /**
     * Set the progress of the boss bar.
     *
     * @param progress The progress of the boss bar.
     */
    public void setProgress(double progress) {
        this.progress = progress;
    }

    /**
     * Set if the boss bar is public.
     *
     * <p>A public bar means players will be automatically added to it when they join.</p>
     *
     * @param pub If the boss bar should be public.
     */
    public void setPublic(boolean pub) {
        this.publicBar = pub;
    }

    /**
     * Set a data value.
     *
     * @param key   the key
     * @param value the data
     */
    public void setData(String key, String value) {
        this.storedData.put(key, value);
    }

    /**
     * If a key exists.
     *
     * @param key the key
     * @return if the key exists
     */
    public boolean containsKey(String key) {
        return this.storedData.containsKey(key);
    }

    /**
     * Get a piece of data.
     *
     * @param key The key
     * @return The piece of data.
     */
    public String getData(String key) {
        return this.storedData.get(key);
    }

    /**
     * Get the map of data.
     *
     * @return The map of data.
     */
    public Map<String, String> getData() {
        return this.storedData;
    }

    /**
     * Delete data from the stored data.
     *
     * @param key The key to delete.
     */
    public void deleteData(String key) {
        this.storedData.remove(key);
    }

    /**
     * Get the id.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the players.
     * <p><b>Note:</b> If you want to remove players use {@link #removePlayer(Player)}</p>
     *
     * @return A collection of players.
     */
    public Collection<Player> getPlayers() {
        return players;
    }

    /**
     * Set the collection of players.
     *
     * <p>Use {@link #updatePlayers()} to sync the player list.</p>
     *
     * @param players The collection of players to set.
     */
    public void setPlayers(Collection<Player> players) {
        this.players = players;
    }

    /**
     * A way to remove players as #remove does not work on Collections.
     *
     * <p>Use {@link #updatePlayers()} to sync the player list.</p>
     *
     * @param p The player.
     */
    public void removePlayer(Player p) {
        List<Player> temp = new LinkedList<>(players);
        temp.remove(p);
        players = temp;
    }

    /**
     * Add a player to the player Collection
     *
     * <p>Use {@link #updatePlayers()} to sync the player list.</p>
     *
     * @param p the player
     */
    public void addPlayer(Player p) {
        List<Player> temp = new LinkedList<>(players);
        temp.add(p);
        players = temp;
    }

    /**
     * Get the world that this boss bar is assigned to.
     *
     * @return The world that this boss bar is assigned to. (Can be null).
     */
    @Nullable
    public World getWorld() {
        return world;
    }

    /**
     * Set the world that this boss bar is assigned to.
     *
     * @param world The world that this boss bar is assigned to.
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * If this boss bar is being tracked.
     *
     * @return If this boss bar is being tacked.
     */
    public boolean isTracked() {
        return tracked;
    }

    /**
     * Set if this boss bar should be tracked.
     *
     * @param tracked If this boss bar should be tracked.
     */
    public void setTracked(boolean tracked) {
        this.tracked = tracked;
    }

    /**
     * Get the timer associated with the bar.
     *
     * @return The BossBarTimer.
     */
    public Optional<BossBarTimer> getTimer() {
        return Optional.ofNullable(timer);
    }

    /**
     * Get the boss bar associated with this bar.
     *
     * @return The boss bar associated with this bar.
     */
    public BossBar getBar() {
        return bar;
    }

    /**
     * Set the boss bar associated with this bar.
     *
     * @param bar The boss bar that should be associated with this bar.
     */
    public void setBar(BossBar bar) {
        this.bar = bar;
    }

    /**
     * Is null if the command was not use to form this bar.
     *
     * @return Parameters or null.
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

    /**
     * Set the map of parameters.
     *
     * @param param The map of parameters.
     */
    public void setParameters(Map<String, String> param) {
        this.parameters = param;
    }

    /**
     * Update the list of players on the live bar.
     *
     * <p>This must be called if you want players to be added / removed from the
     * actual boss bar.</p>
     */
    public void updatePlayers() {
        bar.removeAll();
        for (Player p : players) {
            bar.addPlayer(p);
        }
    }

    /**
     * Get the UUID of the boss bar.
     *
     * @return The UUID of the boss bar.
     */
    public UUID getPID() {
        return this.pid;
    }

    /**
     * Set the permission for the bar.
     *
     * @param permission The permission. (Null for none).
     */
    public void setPermission(@Nullable String permission) {
        this.permission = permission;
    }

    /**
     * Get the permission for the bar.
     *
     * @return The optional of a string.
     */
    public Optional<String> getPermission() {
        return Optional.ofNullable(permission);
    }

    /**
     * Get the count style.
     *
     * @return The count style.
     */
    public CountStyle getCountStyle() {
        return countStyle;
    }

    /**
     * Check to see if a player meets the conditions to be added to this bar.
     *
     * @param p The player to check.
     * @return If the player meets the bar's conditions.
     */
    public boolean checkPlayerConditions(Player p) {
        if (permission != null && !p.hasPermission(permission))
            return false;
        return world == null || p.getWorld() == world;
    }

    /**
     * If the bar is a public bar.
     *
     * @return true or false.
     */
    public boolean isPublicBar() {
        return publicBar;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UBossBar)) return false;
        UBossBar ub = (UBossBar) o;
        return this.pid == ub.pid;
    }

    @Override
    public int hashCode() {
        return this.pid.hashCode();
    }

}
