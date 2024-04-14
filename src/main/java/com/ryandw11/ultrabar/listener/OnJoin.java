package com.ryandw11.ultrabar.listener;

import com.ryandw11.ultrabar.api.UBossBar;
import com.ryandw11.ultrabar.GrabBarStyles;
import com.ryandw11.ultrabar.UltraBar;
import com.ryandw11.ultrabar.api.BossBarBuilder;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class OnJoin implements Listener {

    private final UltraBar plugin;

    public OnJoin(UltraBar plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        // Add the boss bar announcement first.
        if (plugin.getConfig().getBoolean("BossBarMessages.Enabled")) {
            plugin.getBarAnnouncer().addPlayer(p);
        }

        if (plugin.getConfig().getBoolean("OnJoin.BossBar.Enabled")) {
            BarColor color = GrabBarStyles.barColor(Objects.requireNonNull(plugin.getConfig().getString("OnJoin.BossBar.Color")));
            BarStyle style = GrabBarStyles.barStyle(Objects.requireNonNull(plugin.getConfig().getString("OnJoin.BossBar.Style")));
            String message = plugin.papiTranslate.getMessage(plugin.chatColorUtil.translateChatColor(plugin.getConfig().getString("OnJoin.BossBar.Message").replace("{player}", p.getName())), p);
            int time = plugin.getConfig().getInt("OnJoin.BossBar.Time");
            double progress = plugin.getConfig().getDouble("OnJoin.BossBar.Health");

            /*
             * Update BossBarBuilder
             */
            BossBarBuilder bbb = new BossBarBuilder(false)
                    .setSinglePlayer(p)
                    .setMessage(message)
                    .setColor(color)
                    .setStyle(style)
                    .setTime(time)
                    .setProgress(progress);
            UBossBar bb = bbb.build();
            bb.getTimer().ifPresent(timer -> timer.setupTimer(bb));
        }
        if (plugin.getConfig().getBoolean("OnJoin.Title.Enabled")) {
            String message = plugin.papiTranslate.getMessage(plugin.getConfig().getString("OnJoin.Title.Message"), p);
            String submessage = plugin.getConfig().getString("OnJoin.Title.SubTitle");
            int fadein = plugin.getConfig().getInt("OnJoin.Title.fadein");
            int fadeout = plugin.getConfig().getInt("OnJoin.Title.fadeout");
            int time = plugin.getConfig().getInt("OnJoin.Title.time");

            plugin.typeManager.title(plugin.chatColorUtil.translateChatColor(message.replace("{player}", p.getName())), p, fadein, time, fadeout, plugin.chatColorUtil.translateChatColor(submessage));

        }
        if (plugin.getConfig().getBoolean("OnJoin.ActionBar.Enabled")) {
            String message = plugin.papiTranslate.getMessage(plugin.getConfig().getString("OnJoin.ActionBar.Message"), p);
            plugin.typeManager.actionBar(p, plugin.chatColorUtil.translateChatColor(message.replace("{player}", p.getName())));
        }

        /*
         * Give the player all of the boss bars.
         */
        for (UBossBar b : UltraBar.trackedBars) {
            if (b.isPublicBar() && b.checkPlayerConditions(p)) {
                b.addPlayer(p);
                b.updatePlayers();
            }
        }

    }

}
