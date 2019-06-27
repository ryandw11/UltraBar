package me.ryandw11.ultrabar.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.bukkit.command.Command;

public class BarCommandTabCompleter implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
		ArrayList<String> completions = new ArrayList<>();
		completions = new ArrayList<>( Arrays.asList("player:", "time:", "perm:", "msg:", "prog:", "w:", "color:", "style:"));
		return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
	}
}
