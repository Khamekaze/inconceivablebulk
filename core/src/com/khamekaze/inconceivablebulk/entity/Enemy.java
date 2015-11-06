package com.khamekaze.inconceivablebulk.entity;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Entity {
	
	private float launchHeight = 0.0f;
	private boolean launched = false;
	private Vector2 enemyPos, playerPos;
	private Random attackDelay;
	private boolean hasAttacked = false;
	private float attackCd = 0.0f;
	private boolean stunned = false;
	private float stunTime = 0.0f;
	private float hitFrameTime = 0.0f;
	
	private Animation hitSplashOneAnim;
	private TextureRegion[] hitSplashOneFrames;
	private TextureRegion currentFrame;

	public Enemy(int hp, int attackDamage, int attackType, float movementSpeed, float x, float y) {
		super(hp, attackDamage, attackType, movementSpeed);
		this.x = x;
		this.y = y;
		enemyPos = new Vector2(x, y);
		playerPos = new Vector2();
		attackDelay = new Random();
		
	}
	
	public void update(float delta, Rectangle player) {
		if(!launched)
			applyGravity(delta);
		getHitBox().x = x;
		getHitBox().y = y;
		enemyPos.set(x + 50, y + 50);
		playerPos.set(player.x + 50, player.y + 50);
		int direction;
		if(enemyPos.x > playerPos.x + 75)
			direction = 1;
		else if(enemyPos.x < playerPos.x - 75)
			direction = 2;
		else {
			direction = 0;
			attack();
		}
		
		if(!getDamageDelay() && !launched && !stunned)
			move(direction, delta);
		
		if(stunned && grounded) {
			stunTime -= 0.1f;
			if(stunTime <0) {
				stunTime = 0;
				stunned = false;
			}
		}
	}
	
	public void render(SpriteBatch sb) {
		
		
	}
	
	public void attack() {
		if(!hasAttacked) {
			attackCd = (float) attackDelay.nextInt(3) + 3;
			hasAttacked = true;
		} else if(hasAttacked) {
			attackCd -= 0.03f;
			if(attackCd < 0) {
				attackCd = 0;
				hasAttacked = false;
			}
		}
	}
	
	public void setLaunchHeight(float height) {
		this.launchHeight = height;
	}
	
	public float getLaunchHeight() {
		return launchHeight;
	}
	
	public void setLaunched(boolean launched) {
		this.launched = launched;
	}
	
	public boolean isLaunched() {
		return launched;
	}
	
	public void setStunned(boolean stunned) {
		this.stunned = stunned;
	}
	
	public boolean isStunned() {
		return stunned;
	}
	
	public void setStunTime(float time) {
		this.stunTime = time;
	}
	
	public float getStunTime() {
		return stunTime;
	}
	
}
