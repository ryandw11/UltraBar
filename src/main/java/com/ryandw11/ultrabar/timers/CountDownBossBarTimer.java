package com.ryandw11.ultrabar.timers;

import com.ryandw11.ultrabar.api.events.BarTerminateEvent;
import com.ryandw11.ultrabar.api.events.TerminationReason;
import com.ryandw11.ultrabar.UltraBar;
import com.ryandw11.ultrabar.api.UBossBar;
import com.ryandw11.ultrabar.api.UltraBarAPI;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class CountDownBossBarTimer extends BossBarTimer {
    private BossBar b;
    private UBossBar ub;
    private final double progress;
    private final UltraBar plugin;
    private final UltraBarAPI uba;

    public CountDownBossBarTimer(int ticks, double one) {
        this.progress = one / ticks;
        this.plugin = UltraBar.plugin;
        this.uba = new UltraBarAPI();
    }

    @Override
    public void run() {
        double prog = ub.getProgress() - progress;
        if (prog < 0) {
            if (ub == null) {
                this.cancel();
            }
            uba.deleteBar(ub);
            BarTerminateEvent bte = new BarTerminateEvent(ub, TerminationReason.BAR_TIME_OUT);
            Bukkit.getServer().getPluginManager().callEvent(bte);
        } else {
            if (b.getPlayers().size() < 1 && (!ub.isTracked() || !ub.isPublicBar())) {
                if (ub == null) {
                    this.cancel();
                }
                uba.deleteBar(ub);
                BarTerminateEvent bte = new BarTerminateEvent(ub, TerminationReason.OUT_OF_PLAYERS);
                Bukkit.getServer().getPluginManager().callEvent(bte);
                return;
            }
            b.setProgress(prog);
            ub.setProgress(prog);
            b.setColor(ub.getColor());
            b.setStyle(ub.getStyle());
            if (ub.getMessage() != null && b.getPlayers().size() > 0) {
                b.setTitle(replacePlaceholders(plugin.chatColorUtil.translateChatColor(ub.getMessage()), b.getPlayers().get(0)));
            }
        }

    }

    @Override
    public void setupTimer(UBossBar ub) {
        this.ub = ub;
        this.b = ub.getBar();
    }

    private String replacePlaceholders(String title, Player p) {
        return plugin.papiTranslate.getMessage(title, p)
                .replace("%time_left%", Math.max(0, (int) Math.floor(((ub.getProgress() - progress) * (ub.getTime() * 20)) / 20)) + "")
                .replace("%time_left_form%", getFormattedTime(Math.max(0, Math.floor(((ub.getProgress() - progress) * (ub.getTime() * 20)) / 20))));
    }

    private String getFormattedTime(double seconds) {
        double hours = seconds / 60;
        double second = Math.floor(seconds % 60);
        double minutes = Math.floor(hours % 60);
        hours = Math.floor(hours / 60);
        StringBuilder builder = new StringBuilder();
        if (hours > 0) {
            builder.append((int) hours).append("h ");
        }
        if (minutes > 0) {
            builder.append((int) minutes).append("m ");
        }
        builder.append((int) second).append("s");
        return builder.toString();
    }


}
