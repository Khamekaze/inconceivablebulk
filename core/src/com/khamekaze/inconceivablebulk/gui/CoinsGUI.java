package com.khamekaze.inconceivablebulk.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CoinsGUI {
	
	private float x, y;
	private BitmapFont font;
	private String text;
	private int coins;
	
	public CoinsGUI(float x, float y) {
		this.x = x;
		this.y = y;
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		coins = 0;
		text = "COINS: " + coins;
	}
	
	public void render(SpriteBatch sb) {
		font.draw(sb, text, x, y);
	}
	
	public void addCoin() {
		coins++;
	}
	
	public void addCoins(int amount) {
		coins += amount;
	}
	
	public void removeCoins(int amount) {
		coins -= amount;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setCoins(int amount) {
		this.coins = amount;
		text = "COINS: " + coins;
	}

}
