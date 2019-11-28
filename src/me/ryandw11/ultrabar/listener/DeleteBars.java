package me.ryandw11.ultrabar.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.ryandw11.ultrabar.api.events.BarTerminateEvent;
import me.ryandw11.ultrabar.core.UltraBar;

public class DeleteBars implements Listener{
	
	@EventHandler
	public void onDelete(BarTerminateEvent evt) {
		UltraBar.trackedBars.remove(evt.getBar());
	}

}
