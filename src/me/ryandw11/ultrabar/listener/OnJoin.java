package me.ryandw11.ultrabar.listener;

import me.ryandw11.ultrabar.GrabBarStyles;
import me.ryandw11.ultrabar.UltraBar;
import me.ryandw11.ultrabar.api.BossBarBuilder;
import me.ryandw11.ultrabar.api.UBossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoin implements Listener {

    private UltraBar plugin;

    public OnJoin(UltraBar plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        if (plugin.getConfig().getBoolean("OnJoin.BossBar.Enabled")) {
            BarColor color = GrabBarStyles.barColor(plugin.getConfig().getString("OnJoin.BossBar.Color"));
            BarStyle style = GrabBarStyles.barStyle(plugin.getConfig().getString("OnJoin.BossBar.Style"));
            String message = plugin.papi.getMessage(plugin.chatColorUtil.translateChatColor(plugin.getConfig().getString("OnJoin.BossBar.Message").replace("{player}", p.getName())), p);
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
            bb.getTimer().setupTimer(bb);
        }
        if (plugin.getConfig().getBoolean("OnJoin.Title.Enabled")) {
            String message = plugin.papi.getMessage(plugin.getConfig().getString("OnJoin.Title.Message"), p);
            String submessage = plugin.getConfig().getString("OnJoin.Title.SubTitle");
            int fadein = plugin.getConfig().getInt("OnJoin.Title.fadein");
            int fadeout = plugin.getConfig().getInt("OnJoin.Title.fadeout");
            int time = plugin.getConfig().getInt("OnJoin.Title.time");

            plugin.mgr.title(plugin.chatColorUtil.translateChatColor(message.replace("{player}", p.getName())), p, fadein, time, fadeout, plugin.chatColorUtil.translateChatColor(submessage));

        }
        if (plugin.getConfig().getBoolean("OnJoin.ActionBar.Enabled")) {
            String message = plugin.papi.getMessage(plugin.getConfig().getString("OnJoin.ActionBar.Message"), p);
            plugin.mgr.actionBar(p, plugin.chatColorUtil.translateChatColor(message.replace("{player}", p.getName())));
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
        if (!plugin.getConfig().getBoolean("BossBarMessages.World_Whitelist_Enabled") && plugin.getConfig().getBoolean("BossBarMessages.Enabled") && UltraBar.barMessage != null)
            UltraBar.barMessage.addPlayer(p);
        else if (plugin.getConfig().getBoolean("BossBarMessages.World_Whitelist_Enabled") && plugin.getConfig().getBoolean("BossBarMessages.Enabled") && UltraBar.barMessage != null) {
            if (plugin.getConfig().getStringList("BossBarMessages.World_Whitelist").contains(p.getWorld().getName())) {
                UltraBar.barMessage.addPlayer(p);
            }
        }

    }

}
