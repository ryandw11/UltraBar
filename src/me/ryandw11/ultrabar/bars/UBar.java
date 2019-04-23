package me.ryandw11.ultrabar.bars;

import org.bukkit.boss.BossBar;

import me.ryandw11.ultrabar.core.UltraBar;

public class UBar {
	
	private BossBar bb;
	private int id;
	private boolean pub;
	
	
	public UBar(BossBar bb, int id, boolean pub) {
		this.bb = bb;
		this.id = id;
		this.pub = pub;
		
		checkValid();
	}
	
	public int getId() {
		return id;
	}
	
	public BossBar getBossBar() {
		return bb;
	}
	
	public boolean isPublic() {
		return pub;
	}
	
	public void clear() {
		bb.setVisible(false);
		bb.removeAll();
	}
	
	private void checkValid() {
		for(UBar ub : UltraBar.bossbars) {
			if(ub != this && ub.getId() == this.getId()) {
				this.clear();
				UltraBar.bossbars.remove(this);
			}
		}
	}

}
