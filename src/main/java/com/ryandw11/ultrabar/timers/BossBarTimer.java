package com.ryandw11.ultrabar.timers;

import com.ryandw11.ultrabar.api.UBossBar;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The timer responsible for handling the progress of a boss bar.
 */
public abstract class BossBarTimer extends BukkitRunnable {
    public abstract void setupTimer(UBossBar ub);
}
