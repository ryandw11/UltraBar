package me.ryandw11.ultrabar.typemgr;


import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;

import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Typemgr_1_11_R1 implements Typemgr{

	

	
	public Typemgr_1_11_R1(){
	}
	
	/*Bar Style method created.
	 * 
	 * # of Notched lines.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void title(String message, Player p, int fadein, int time, int fadeout, String subtitle){
		p.sendTitle(message, subtitle); //fixes error in 1.11
	}
	@Override
	public void actionBar(Player player, String message){
		CraftPlayer p = (CraftPlayer) player;
		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
	}
	
	
	
	
}
