package me.ryandw11.ultrabar;

import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

import me.ryandw11.ultrabar.core.UltraBar;

public class BossBarTimer extends BukkitRunnable{
	private BossBar b;
	private double progress;

	public BossBarTimer(BossBar bar, int ticks, double one, String title){
		this.b = bar;
		this.progress = one / ticks;
	}
	@Override
	public void run() {
		double prog = b.getProgress() - progress;
        if(prog < 0){
        	b.setVisible(false);
        	this.cancel();
        	UltraBar.bossbars.remove(b);
        }
        else{
        	b.setProgress(prog);
        }
		
	}
	

}
