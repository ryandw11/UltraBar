package me.ryandw11.ultrabar;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

import me.ryandw11.ultrabar.api.UBossBar;
import me.ryandw11.ultrabar.api.UltraBarAPI;
import me.ryandw11.ultrabar.api.events.BarTerminateEvent;
import me.ryandw11.ultrabar.api.events.TerminationReason;
import me.ryandw11.ultrabar.core.UltraBar;

public class BossBarTimer extends BukkitRunnable{
	private BossBar b;
	private UBossBar ub;
	private double progress;
	private UltraBar plugin;
	private UltraBarAPI uba;

	public BossBarTimer(int ticks, double one){
		this.progress = one / ticks;
		this.plugin = UltraBar.plugin;
		this.uba = new UltraBarAPI();
	}
	@Override
	public void run() {
		double prog = ub.getProgress() - progress;
        if(prog < 0){
        	if(ub == null) {
        		this.cancel();
        	}
        	uba.deleteBar(ub);
        	BarTerminateEvent bte = new BarTerminateEvent(ub, TerminationReason.BAR_TIME_OUT);
        	Bukkit.getServer().getPluginManager().callEvent(bte);
        }
        else{
        	if(b.getPlayers().size() < 1) {
        		if(ub == null) {
            		this.cancel();
            	}
        		uba.deleteBar(ub);
            	BarTerminateEvent bte = new BarTerminateEvent(ub, TerminationReason.OUT_OF_PLAYERS);
            	Bukkit.getServer().getPluginManager().callEvent(bte);
        		return;
        	}
        	b.setProgress(prog);
        	ub.setProgress(prog);
        	b.setColor(ub.getColor());
        	b.setStyle(ub.getStyle());
        	if(ub.getMessage() != null && b.getPlayers().get(0) != null) {
        		b.setTitle(plugin.papi.getMessage(ub.getMessage(), b.getPlayers().get(0)));
        	}
        }
		
	}
	
	public void setupTimer(UBossBar ub) {
		this.ub = ub;
		this.b = ub.getBar();
	}
	

}
