package me.ryandw11.ultrabar.commands;

import me.ryandw11.ultrabar.UltraBar;
import me.ryandw11.ultrabar.api.UBossBar;
import me.ryandw11.ultrabar.api.UltraBarAPI;
import me.ryandw11.ultrabar.api.events.BarTerminateEvent;
import me.ryandw11.ultrabar.api.events.TerminationReason;
import me.ryandw11.ultrabar.api.parameters.BarParameter;
import me.ryandw11.ultrabar.schedulers.ActionBarSched;
import me.ryandw11.ultrabar.schedulers.TitleSched;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ConcurrentModificationException;

public class UltraBarCommand implements CommandExecutor {

    private final UltraBar plugin;
    private final UltraBarAPI uba;

    public UltraBarCommand(UltraBar plugin) {
        this.plugin = plugin;
        this.uba = new UltraBarAPI();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("ultrabar.help")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7==========[&6UltraBar Help&7]=========="));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/bar &7- Commands for the boss bar."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/utitle &7- Commands for the title command."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/actionbar &7- Commands for the actionbar command."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/ultrabar reload &7- Reload the plugin."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/ultrabar toggle &7- Toggle if you will receive bar announcements."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/ultrabar hooks &7- See the active hooks."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/ultrabar cancel [id] (player) &7- Remove all active bars."));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/ultrabar clear (player) &7- Remove all active bars for that player."));
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("ultrabar.reload")) {
                if (plugin.getConfig().getBoolean("BossBarMessages.Enabled")) {
                    for (UBossBar b : UltraBar.trackedBars) {
                        uba.deleteBar(b);
                        BarTerminateEvent bte = new BarTerminateEvent(b, TerminationReason.BAR_CANCEL);
                        Bukkit.getServer().getPluginManager().callEvent(bte);
                    }
                    UltraBar.plugin.getBarAnnouncer().stopProgram();
                }

                plugin.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "The config file was reloaded!");
                plugin.getLogger().info("[UltraBar] The config files were reloaded!");
                if (plugin.getConfig().getBoolean("BossBarMessages.Enabled")) {
                    UltraBar.plugin.resetBarAnnouncer();
                }
                if (plugin.getConfig().getBoolean("Title_Announcements.Enabled")) {
                    TitleSched ts = new TitleSched();
                    ts.startProgram();
                }
                if (plugin.getConfig().getBoolean("Action_Announcements.Enabled")) {
                    ActionBarSched as = new ActionBarSched();
                    as.startProgram();
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                return true;
            }
            if (!sender.hasPermission("ultrabar.toggle")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
                return true;
            }
            if (plugin.getToggledPlayers().contains(sender)) {
                plugin.removeTogglePlayer((Player) sender);
                sender.sendMessage(ChatColor.GREEN + "Boss Bar announcements will now show!");
            } else {
                plugin.addTogglePlayer((Player) sender);
                sender.sendMessage(ChatColor.RED + "Boss Bar announcements will no longer show!");
            }
        } else if (args.length == 1 && (args[0].equalsIgnoreCase("hooks") || args[0].equalsIgnoreCase("hook"))) {
            if (!sender.hasPermission("ultrabar.hooks")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
                return true;
            }

            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

            int num = 0;
            sender.sendMessage(ChatColor.GREEN + "Active Plugin Hooks:");
            if (plugin.worldguard) {
                if (!version.equals("v1_12_R1"))
                    sender.sendMessage(ChatColor.BLUE + "- WorldGuard (1.13+)");
                num++;
            }
            if (plugin.placeholderAPI) {
                sender.sendMessage(ChatColor.BLUE + "- PlaceHolderAPI");
                num++;
            }
            for (BarParameter bp : plugin.getBarParameters()) {
                if (bp.getPluginName() != null) {
                    sender.sendMessage(ChatColor.BLUE + "- " + bp.getPluginName());
                    num++;
                }
            }
            if (num == 0)
                sender.sendMessage(ChatColor.BLUE + "There are currently no active plugin hooks!");
        } else if (args[0].equalsIgnoreCase("cancelbars") || args[0].equalsIgnoreCase("cancel")) {
            if (!sender.hasPermission("ultrabar.cancel")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission for this command");
                return true;
            }
            if (args.length == 3) {
                int id = -1;
                try {
                    id = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid number!");
                    return true;
                }

                String playerName = args[2];
                Player player = Bukkit.getPlayer(playerName);
                if(player == null) {
                    sender.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }

                UltraBarAPI uapi = new UltraBarAPI();
                for (UBossBar b : uapi.getBarsWithId(id)) {
                    b.removePlayer(player);
                    b.updatePlayers();
                }
                sender.sendMessage(ChatColor.GREEN + "Removed that player from all bars with that id!");

            }else if (args.length == 2) {
                int id = -1;
                try {
                    id = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid number!");
                    return true;
                }

                UltraBarAPI uapi = new UltraBarAPI();
                for (UBossBar b : uapi.getBarsWithId(id)) {
                    uba.deleteBar(b);
                    BarTerminateEvent bte = new BarTerminateEvent(b, TerminationReason.BAR_CANCEL);
                    Bukkit.getServer().getPluginManager().callEvent(bte);
                }
                sender.sendMessage(ChatColor.GREEN + "Removed all bossbars with that id!");

            } else {
                try {
                    for (UBossBar b : UltraBar.trackedBars) {
                        uba.deleteBar(b);
                        BarTerminateEvent bte = new BarTerminateEvent(b, TerminationReason.BAR_TIME_OUT);
                        Bukkit.getServer().getPluginManager().callEvent(bte);
                    }
                } catch (ConcurrentModificationException e) {

                }
                plugin.getLogger().info("All of the boss bars have been removed.");
                sender.sendMessage(ChatColor.GREEN + "Cleared all of the active bars!");
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("clear")) {
            if (!sender.hasPermission("ultrabar.clear")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission for this command");
                return true;
            }
            UltraBarAPI ubapi = new UltraBarAPI();
            if (Bukkit.getPlayer(args[1]) == null) {
                sender.sendMessage(ChatColor.RED + "That player does not exist!");
                return false;
            }
            if (!Bukkit.getPlayer(args[1]).isOnline()) {
                sender.sendMessage(ChatColor.RED + "That player is not online!");
                return false;
            }
            for (UBossBar ub : ubapi.getBarsForPlayer(Bukkit.getPlayer(args[1]))) {
                ub.removePlayer(Bukkit.getPlayer(args[1]));
                ub.updatePlayers();
            }
            sender.sendMessage(ChatColor.GREEN + "Cleared all bars for that player!");
        } else {
            sender.sendMessage(ChatColor.RED + "Unknown SubCommand. Type /ultrabar for help!");
        }

        return false;
    }

}
