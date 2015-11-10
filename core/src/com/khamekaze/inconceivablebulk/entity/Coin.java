package com.khamekaze.inconceivablebulk.entity;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Coin {
	
	private float x, y, width, height, yVel, xVel, originalXVel, originalYVel, groundY, gravity, timeUntilDespawn = 30f, alpha = 1f;
	private Sprite sprite;
	private Rectangle hitBox;
	private boolean pickedUp = false, grounded = false;
	private Random rand = new Random();
	
	public Coin(float x, float y, float groundY) {
		this.x = x;
		this.y = y;
		this.groundY = groundY;
		width = 40;
		height = 40;
		sprite = new Sprite(new Texture(Gdx.files.internal("rawSprites/powerups/coin.png")));
		sprite.setPosition(x, y);
		hitBox = new Rectangle(x, y, width, height);
		xVel = (float) rand.nextInt(8) - 4;
		yVel = (float) rand.nextInt(5) + 5;
		originalXVel = xVel;
		originalYVel = yVel;
		gravity = 0.4f;
	}
	
	public void update(float delta) {
		hitBox.setPosition(x, y);
		sprite.setPosition(x, y);
		gravity(delta);
		if(timeUntilDespawn > 0) {
			timeUntilDespawn -= (0.1f * delta);
		} else if(timeUntilDespawn <= 0) {
			alpha -= 0.02f;
			if(alpha <= 0) {
				alpha = 0;
				pickedUp = true;
			}
			sprite.setAlpha(alpha);
		}
	}
	
	public void render(SpriteBatch sb) {
		
		sprite.draw(sb);
	}
	
	public void gravity(float delta) {
		if(y > groundY && !grounded) {
			gravity = 0.4f;
			gravity = gravity * delta;
			xVel = originalXVel;
			xVel = xVel * delta;
			
			yVel = originalYVel;
			yVel -= gravity;
			originalYVel -= gravity;
			yVel = yVel * delta;
			x += xVel;
			y += yVel;
			if(y <= groundY) {
				y = groundY;
				grounded = true;
			}
		}
	}
	
	public void checkIfPickedUp(Player player) {
		if(player.getHitBox().overlaps(hitBox) && !pickedUp) {
			pickedUp = true;
			player.setPickedUpCoins(player.getPickedUpCoins() + 1);
			System.out.println(player.getPickedUpCoins());
		}
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		hitBox.x = x;
		hitBox.y = y;
	}
	
	public boolean isPickedUp() {
		return pickedUp;
	}

}
