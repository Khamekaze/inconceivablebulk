package com.khamekaze.inconceivablebulk.environment;

public class SpawnPoint {
	
	private float x, y;
	private boolean checked = false;
	
	public SpawnPoint(float x) {
		this.x = x;
		this.y = 0;
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public float getX() {
		return x;
	}

}
