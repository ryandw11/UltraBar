package me.ryandw11.ultrabar.commands;

import me.ryandw11.ultrabar.UltraBar;
import me.ryandw11.ultrabar.api.parameters.BarParameter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BarCommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        ArrayList<String> completions = new ArrayList<>(Arrays.asList("player:", "time:", "perm:", "persist:", "msg:", "prog:", "w:", "color:", "style:", "clear:", "id:", "countStyle:"));
        for (BarParameter bp : UltraBar.plugin.getBarParameters()) {
            completions.add(bp.tab() + ":");
        }
        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
    }
}
