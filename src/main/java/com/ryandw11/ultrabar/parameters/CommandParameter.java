package com.ryandw11.ultrabar.parameters;

import java.util.ArrayList;
import java.util.List;

import com.ryandw11.ultrabar.api.events.BarTerminateEvent;
import com.ryandw11.ultrabar.api.events.TerminationReason;
import com.ryandw11.ultrabar.api.parameters.BarParameter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.ryandw11.ultrabar.api.BossBarBuilder;
import com.ryandw11.ultrabar.UltraBar;

public class CommandParameter implements BarParameter, Listener {

    public CommandParameter() {
        Bukkit.getPluginManager().registerEvents(this, UltraBar.plugin);
    }

    @Override
    public String getPluginName() {
        return null;
    }

    @Override
    public List<String> aliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("command");
        aliases.add("cmd");
        return aliases;
    }

    @Override
    public void barCreation(String value, BossBarBuilder bar, CommandSender creator) {
        if (!creator.hasPermission("ultrabar.bar.command")) {
            creator.sendMessage(ChatColor.RED + "You do not have permission to set a command for the bar.");
            return;
        }
        bar.setData("ubcmd", value);
    }

    @EventHandler
    public void onBarTerminate(BarTerminateEvent evt) {
        if (!evt.getBar().containsKey("ubcmd")) return;
        if (evt.getReason() == TerminationReason.BAR_TIME_OUT) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), evt.getBar().getData("ubcmd").trim());
            UltraBar.plugin.getLogger().info("Ultra Bar has ran the command: " + evt.getBar().getData("ubcmd"));
        }
    }

    @Override
    public String tab() {
        return "cmd";
    }

}
