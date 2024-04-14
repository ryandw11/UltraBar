package me.ryandw11.ultrabar.announcements;

import me.ryandw11.ultrabar.GrabBarStyles;
import me.ryandw11.ultrabar.UltraBar;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class is responsible for boss bar announcements.
 */
public class IndividualBossBarAnnouncer extends BukkitRunnable implements Announcer {

    private final UltraBar plugin;

    private final Map<Player, BossBar> barMap;
    private final List<ConfigurationSection> barConfigurations;
    private int currentBar;

    private final AnnouncerData announcerData;
    private BarData barData;

    private long previousTime;
    private float timeLeft;

    private final NamespacedKey key;

    public IndividualBossBarAnnouncer() {
        this.plugin = UltraBar.plugin;
        this.barMap = new HashMap<>();
        this.barConfigurations = new ArrayList<>();
        this.key = new NamespacedKey(plugin, "Announcement");
        this.announcerData = new AnnouncerData(plugin);

        if (!plugin.getConfig().contains("BossBarMessages.Messages")) {
            throw new IllegalStateException("BoosBarMessages does not exist in the config.");
        }

        for (String sectionName : Objects.requireNonNull(plugin.getConfig().getConfigurationSection("BossBarMessages.Messages")).getKeys(false)) {
            ConfigurationSection section = plugin.getConfig().getConfigurationSection("BossBarMessages.Messages." + sectionName);
            barConfigurations.add(section);
        }

        if (barConfigurations.isEmpty()) {
            throw new IllegalStateException("No BossBarMessages found! Please add some or disable the announcement feature!");
        }

        currentBar = 0;
        createBarData(currentBar);
        timeLeft = barData.getTime();

        for (Player p : Bukkit.getOnlinePlayers()) {
            barMap.put(p, createBossBar(barData, p));
        }
    }

    @Override
    public void run() {
        if (currentBar > barConfigurations.size()) currentBar = 0;

        float deltaTime = (System.currentTimeMillis() - previousTime) / 1000f;
        previousTime = System.currentTimeMillis();

        timeLeft -= deltaTime;
        if (timeLeft < 0) {
            // Pick the next bar.
            if (!announcerData.isRandomOrder())
                currentBar++;
            else
                currentBar = ThreadLocalRandom.current().nextInt(0, barConfigurations.size());
            if (currentBar >= barConfigurations.size()) currentBar = 0;
            createBarData(currentBar);

            timeLeft = barData.getTime();

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!barMap.containsKey(p) && shouldPlayerSeeBar(p))
                    barMap.put(p, createBossBar(barData, p));
                if (barMap.containsKey(p))
                    setBossBar(barData, p);
            }
        } else {
            List<Player> playersToRemove = new ArrayList<>();
            for (Map.Entry<Player, BossBar> bossBarEntry : barMap.entrySet()) {
                if (!bossBarEntry.getKey().isOnline() || !shouldPlayerSeeBar(bossBarEntry.getKey())) {
                    playersToRemove.add(bossBarEntry.getKey());
                    bossBarEntry.getValue().removeAll();
                    continue;
                }
                bossBarEntry.getValue().setTitle(barData.getTranslatedString(bossBarEntry.getKey()));
                // Calculate the current progress.
                if (!announcerData.isNoProgress())
                    bossBarEntry.getValue().setProgress(1 / (barData.getTime() / timeLeft));
            }

            // Remove offline players from the map.
            playersToRemove.forEach(barMap::remove);
        }
    }

    private void createBarData(int current) {
        ConfigurationSection section = this.barConfigurations.get(current);
        String title = section.contains("Message") ? section.getString("Message") : "Default Message...";
        BarColor barColor = section.contains("Color") ? GrabBarStyles.barColor(Objects.requireNonNull(section.getString("Color"))) : BarColor.PURPLE;
        BarStyle barStyle = section.contains("Style") ? GrabBarStyles.barStyle(Objects.requireNonNull(section.getString("Style"))) : BarStyle.SOLID;
        int time = barConfigurations.get(0).getInt("Time");
        this.barData = new BarData(title, barColor, barStyle, time);
    }

    private void setBossBar(BarData data, Player player) {
        BossBar bar = this.barMap.get(player);
        bar.setProgress(1);
        bar.setTitle(data.getTranslatedString(player));
        bar.setStyle(data.getBarStyle());
        bar.setColor(data.getBarColor());
    }

    private BossBar createBossBar(BarData data, Player player) {
        BossBar bar = plugin.typeManager.createBossBar(key, data.getTranslatedString(player), data.getBarColor(), data.getBarStyle());
        bar.addPlayer(player);
        return bar;
    }

    private void clearAllPlayers() {
        for (Map.Entry<Player, BossBar> bossBarEntry : barMap.entrySet()) {
            bossBarEntry.getValue().removeAll();
        }
        barMap.clear();
    }

    private boolean shouldPlayerSeeBar(Player player) {
        if (!announcerData.isWorldInWhitelist(player.getWorld())) return false;

        return !plugin.getToggledPlayers().contains(player);
    }

    @Override
    public void startProgram() {
        this.runTaskTimer(plugin, 5L, 1L);
    }

    @Override
    public void stopProgram() {
        clearAllPlayers();
        this.cancel();
    }

    @Override
    public void addPlayer(Player p) {
        if (!this.barMap.containsKey(p) && shouldPlayerSeeBar(p))
            this.barMap.put(p, createBossBar(barData, p));
    }

    @Override
    public void removePlayer(Player p) {
        if (this.barMap.containsKey(p)) {
            this.barMap.get(p).removeAll();
        }
        this.barMap.remove(p);
    }
}
