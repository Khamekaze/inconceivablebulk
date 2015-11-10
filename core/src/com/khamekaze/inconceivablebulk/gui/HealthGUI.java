package com.khamekaze.inconceivablebulk.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HealthGUI {
	
	private Sprite lifeBarContainer, lifeBar;
	
	private float x, y, lifebarWidth;
	
	public HealthGUI(float x, float y) {
		lifeBarContainer = new Sprite(new Texture(Gdx.files.internal("rawSprites/gui/lifebar.png")));
		lifeBar = new Sprite(new Texture(Gdx.files.internal("rawSprites/gui/lifebarbar.png")));
		lifebarWidth = lifeBar.getWidth();
		this.x = x;
		this.y = y;
		lifeBarContainer.setPosition(x, y);
		lifeBar.setPosition(x + 4, y + 4);
	}
	
	public void update() {
		lifeBar.setSize(lifebarWidth, 42);
		lifeBarContainer.setPosition(x, y);
		lifeBar.setPosition(x + 4, y + 4);
	}
	
	public void render(SpriteBatch sb) {
		lifeBarContainer.draw(sb);
		lifeBar.draw(sb);
	}
	
	public float getLifeBarWidth() {
		return lifebarWidth;
	}
	
	public void setLifeBarWidth(float width) {
		this.lifebarWidth = width;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

}
