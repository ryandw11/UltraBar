package me.ryandw11.ultrabar.typemgr;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

/**
 * The modern implementation of TypeManager.
 */
public class ModernTypeManager implements TypeManager {

    @Override
    public void title(String message, Player p, int fadein, int time, int fadeout, String subtitle) {
        p.sendTitle(message, subtitle, fadein, time, fadeout);
    }

    @Override
    public void actionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    @Override
    public BossBar createBossBar(NamespacedKey key, String title, BarColor color, BarStyle style, BarFlag... flags) {
        return Bukkit.createBossBar(key, title, color, style, flags);
    }
}
