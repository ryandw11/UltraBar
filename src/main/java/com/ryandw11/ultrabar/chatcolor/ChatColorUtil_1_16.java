package com.ryandw11.ultrabar.chatcolor;

import net.md_5.bungee.api.ChatColor;

public class ChatColorUtil_1_16 implements ChatColorUtil {

    @Override
    public String translateChatColor(String message) {
        if (!message.contains("{"))
            return ChatColor.translateAlternateColorCodes('&', message);
        StringBuilder finalMessage = new StringBuilder();
        StringBuilder interior = new StringBuilder();
        boolean readInterior = false;
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == '{' && !readInterior) {
                readInterior = true;
            } else if (!readInterior) {
                finalMessage.append(c);
            } else if (c == '{') {
                finalMessage.append('{');
                finalMessage.append(interior);
                interior = new StringBuilder();
            } else if (c == '}' && interior.toString().contains("#")) {
                readInterior = false;
                try {
                    finalMessage.append(ChatColor.of(interior.toString()));
                } catch (IllegalArgumentException ex) {
                    finalMessage.append("{");
                    finalMessage.append(interior);
                    finalMessage.append("}");
                } finally {
                    interior = new StringBuilder();
                }

            } else if (c == '}') {
                readInterior = false;
                finalMessage.append("{");
                finalMessage.append(interior);
                finalMessage.append("}");
                interior = new StringBuilder();
            } else {
                interior.append(c);
            }
        }
        if (interior.toString().length() > 0) {
            finalMessage.append('{');
            finalMessage.append(interior);
        }
        return ChatColor.translateAlternateColorCodes('&', finalMessage.toString());
    }
}
