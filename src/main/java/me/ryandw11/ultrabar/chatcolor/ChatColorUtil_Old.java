package me.ryandw11.ultrabar.chatcolor;

import org.bukkit.ChatColor;

public class ChatColorUtil_Old implements ChatColorUtil{

    @Override
    public String translateChatColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
