package com.khamekaze.inconceivablebulk.entity;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Entity {
	
	private float launchHeight = 0.0f;
	private boolean launched = false, alerted = false, excecuteAttack = false;
	private Vector2 enemyPos, playerPos;
	private Random attackDelay;
	private boolean hasAttacked = false;
	private float attackCd = 0.0f;
	private boolean stunned = false;
	private float stunTime = 0.0f;
	private float hitFrameTime = 0.0f, attackTime = 1;
	private float deathTime = 10f;
	private float alpha = 1.0f;
	private float xVel = 0, yVel = 0;
	
	private Animation hitSplashOneAnim;
	private TextureRegion[] hitSplashOneFrames;
	private TextureRegion currentFrame;
	
	private Sprite sprite, portrait;

	public Enemy(int hp, int attackDamage, int attackType, float movementSpeed, float x, float y) {
		super(hp, attackDamage, attackType, movementSpeed);
		this.x = x;
		this.y = y;
		enemyPos = new Vector2(x, y);
		playerPos = new Vector2();
		attackDelay = new Random();
		sprite = new Sprite(new Texture(Gdx.files.internal("rawSprites/enemy/chicken.png")));
	}
	
	public void update(float delta, Rectangle player) {
		if(!launched)
			applyGravity(delta);
		getHitBox().x = x;
		getHitBox().y = y;
		if(isFacingLeft()) {
			getAttackHitbox().x = x;
			getAttackHitbox().y = y + 50;
		} else if(isFacingRight()) {
			getAttackHitbox().x = x + 50;
			getAttackHitbox().y = y + 50;
		}
		enemyPos.set(x + 50, y + 50);
		playerPos.set(player.x + 50, player.y + 50);
		int direction = 0;
		if(alerted) {
			if(enemyPos.x > playerPos.x + 75)
				direction = 1;
			else if(enemyPos.x < playerPos.x - 75)
				direction = 2;
			else {
				direction = 0;
			}
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
		
		if(excecuteAttack) {
			attack();
		}
		
		if(isFacingLeft()) {
			if(!sprite.isFlipX()) {
				sprite.flip(true, false);
			}
		} else if(isFacingRight()) {
			if(sprite.isFlipX()) {
				sprite.flip(true, false);
			}
		}
		
		sprite.setPosition(x, y);
		
		if(getHp() <= 0) {
			dies(delta, player);
		}
		
	}
	
	public void render(SpriteBatch sb) {
		sprite.draw(sb);
	}
	
	public void dies(float delta, Rectangle player) {
		if(deathTime == 10) {
			if(player.getX() > x) {
				yVel = 0.5f;
				xVel = -1;
			} else if(player.getX() < x) {
				yVel = 0.5f;
				xVel = 1;
			}
			y += yVel;
			x += xVel;
			deathTime -= 0.1f * delta;
		} else if(deathTime < 10 && !isGrounded()) {
			if(y > groundY) {
				if(player.getX() > x)
					xVel = -1;
				else if(player.getX() < x)
					xVel = 1;
				yVel -= 0.1f * delta;
				xVel = xVel * delta;
			} else if(y <= groundY) {
				yVel = 0;
				y = groundY;
				xVel = 0;
				setGrounded(true);
			}
			x += xVel;
			y += yVel;
			deathTime -= 0.1f * delta;
		} else if(isGrounded()) {
			xVel = 0;
			yVel = 0;
			if(alpha > 0.02f)
				alpha -= 0.01f;
			else if(alpha <= 0.02f)
				alpha = 0;
			sprite.setAlpha(alpha);
			deathTime = 0;
		}
	}
	
	public void attack() {
		if(!getDialogBoolean()) {
			if(getHp() > 0) {
				if(!hasAttacked && attackCd == 0 && !stunned) {
					attackTime = 2;
					attackCd = (float) attackDelay.nextInt(10) + 3;
					hasAttacked = true;
					System.out.println("DELAY");
				} else if(attackCd > 0 && hasAttacked && !stunned) {
					attackCd -= 0.1f;
					if(attackCd <= 0 && hasAttacked && !stunned) {
						System.out.println("ATTACK ENDED");
						attackCd = 0;
						setIsAttacking(true);
					}
				} else if(isAttacking() && attackTime > 0) {
					attackTime -= 0.1f;
					if(attackTime <= 0) {
						attackTime = 0;
						hasAttacked = false;
						setIsAttacking(false);
						excecuteAttack = false;
					}
				}
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
	
	public void setAlerted(boolean alerted) {
		this.alerted = alerted;
	}
	
	public boolean isAlerted() {
		return alerted;
	}
	
	public boolean getHasAttacked() {
		return hasAttacked;
	}
	
	public boolean getExcecuteAttack() {
		return excecuteAttack;
	}
	
	public void setExcecuteAttack(boolean attack) {
		this.excecuteAttack = attack;
	}
}
