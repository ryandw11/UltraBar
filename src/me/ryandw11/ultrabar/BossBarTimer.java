package me.ryandw11.ultrabar;

import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

import me.ryandw11.ultrabar.api.bars.UBossBar;
import me.ryandw11.ultrabar.core.UltraBar;

public class BossBarTimer extends BukkitRunnable{
	private BossBar b;
	private UBossBar ub;
	private double progress;
	private UltraBar plugin;

	public BossBarTimer(int ticks, double one){
		this.progress = one / ticks;
		this.plugin = UltraBar.plugin;
	}
	@Override
	public void run() {
		double prog = ub.getProgress() - progress;
        if(prog < 0){
        	if(ub == null) {
        		this.cancel();
        	}
        	ub.delete();
        }
        else{
        	if(b.getPlayers().size() < 1) {
        		this.cancel();
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
