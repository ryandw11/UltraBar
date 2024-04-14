package me.ryandw11.ultrabar.api.title;

import me.ryandw11.ultrabar.UltraBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class allows for Title information to be stored dynamically.
 * This class is primarily used by the TitleCommand.
 */
public class TitleBuilder {
    private String title;
    private String subTitle;
    private final UltraBar plugin;
    private List<Player> players;
    private int fadeIn;
    private int fadeOut;
    private int time;

    public TitleBuilder(UltraBar plugin) {
        this.title = "";
        this.subTitle = "";
        this.plugin = plugin;
        this.players = new ArrayList<>();
        this.fadeIn = 5;
        this.fadeOut = 5;
        this.time = 10;
    }

    public void setTitle(@NotNull String title) {
        this.title = Objects.requireNonNull(title);
    }

    public void setSubTitle(@NotNull String subTitle) {
        this.subTitle = Objects.requireNonNull(subTitle);
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public void addAllPlayers() {
        players.addAll(Bukkit.getOnlinePlayers());
    }

    public void setFadeIn(int fadeIn) {
        this.fadeIn = fadeIn;
    }

    public void setFadeOut(int fadeOut) {
        this.fadeOut = fadeOut;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean hasPlayers() {
        return !players.isEmpty();
    }

    /**
     * Send the title to the players.
     * <p>The players list is cleared to prevent memory leaks.</p>
     */
    public void send() {
        for (Player p : players) {
            plugin.typeManager.title(plugin.papiTranslate.getMessage(UltraBar.plugin.chatColorUtil.translateChatColor(this.title), p), p,
                    fadeIn, time, fadeOut, plugin.papiTranslate.getMessage(plugin.chatColorUtil.translateChatColor(this.subTitle), p));
        }
        players.clear();
    }
}
