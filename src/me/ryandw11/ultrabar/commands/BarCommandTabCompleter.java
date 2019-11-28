package me.ryandw11.ultrabar.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import me.ryandw11.ultrabar.api.parameters.BarParameter;
import me.ryandw11.ultrabar.core.UltraBar;

import org.bukkit.command.Command;

public class BarCommandTabCompleter implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
		ArrayList<String> completions = new ArrayList<>();
		completions.addAll(Arrays.asList("player:", "time:", "perm:", "msg:", "prog:", "w:", "color:", "style:", "clear:", "id:"));
		for(BarParameter bp : UltraBar.plugin.getBarParameters()) {
			completions.add(bp.tab() + ":");
		}
		return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
	}
}
