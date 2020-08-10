package me.ryandw11.ultrabar.api.events;

import me.ryandw11.ultrabar.api.UBossBar;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * When the bar gets terminated for any reason.<br>
 * This event cannot be canceled.
 *
 * @author Ryandw11
 * @since 2.1.4
 */
public class BarTerminateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private UBossBar bar;
    private TerminationReason tr;

    public BarTerminateEvent(UBossBar bar, TerminationReason tr) {
        this.bar = bar;
        this.tr = tr;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Get the bar data.  <br>
     * (Note: This bar has been terminated and is dead.)
     *
     * @return The bar data.
     */
    public UBossBar getBar() {
        return bar;
    }

    /**
     * Get the reason for the bar's termination.
     *
     * @return The termination reason.
     */
    public TerminationReason getReason() {
        return tr;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
