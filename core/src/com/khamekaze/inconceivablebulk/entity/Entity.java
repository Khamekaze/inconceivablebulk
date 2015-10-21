package com.khamekaze.inconceivablebulk.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.khamekaze.inconceivablebulk.MainGame;

public class Entity {
	
	private int stopped = 0, moveLeft = 1, moveRight = 2; 
	
	private boolean facingRight = false, facingLeft = false;

	protected boolean grounded = false;
	
	private int hp, maxHp, attackDamage, attackType, movementSpeed;
	private float gravity = 0.4f;

	protected float y, x;

	private float jumpHeight = 10;
	
	private Rectangle hitBox;
	
	public Entity(int hp, int attackDamage, int attackType, int movementSpeed) {
		this.hp = hp;
		maxHp = hp;
		this.attackDamage = attackDamage;
		this.attackType = attackType;
		this.movementSpeed = movementSpeed;
		hitBox = new Rectangle(MainGame.WIDTH / 2, MainGame.HEIGHT / 2, 50, 50);
	}
	
	public void move(int direction) {
		if(direction == moveLeft) {
			x -= movementSpeed;
			facingLeft = true;
			facingRight = false;
		} else if(direction == moveRight) {
			x += movementSpeed;
			facingRight = true;
			facingLeft = false;
		}
	}
	
	public void jump(float delta) {
		if(grounded) {
			y += jumpHeight;
			hitBox.y = y;
			grounded = false;
		}
	}
	
	public void applyGravity(float delta) {
		if(grounded && jumpHeight < 10) {
			jumpHeight = 10;
		} else if(!grounded) {
			jumpHeight -= gravity;
			y += jumpHeight;
			if(y < 50) {
				y = 50;
				grounded = true;
			}
		}
		
		hitBox.x = x - 25;
		hitBox.y = y;
	}
	
	public void takeDamage(int damage) {
		hp -= damage;
		if(hp < 0) {
			hp = 0;
		}
	}
	
	public void regenerate(int regAmount) {
		hp += regAmount;
		if(hp > maxHp) {
			hp = maxHp;
		}
	}
	
	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
	}

	public int getStopped() {
		return stopped;
	}

	public void setStopped(int stopped) {
		this.stopped = stopped;
	}

	public int getMoveLeft() {
		return moveLeft;
	}

	public void setMoveLeft(int moveLeft) {
		this.moveLeft = moveLeft;
	}

	public int getMoveRight() {
		return moveRight;
	}

	public void setMoveRight(int moveRight) {
		this.moveRight = moveRight;
	}

	public boolean isFacingRight() {
		return facingRight;
	}

	public void setFacingRight(boolean facingRight) {
		this.facingRight = facingRight;
	}

	public boolean isFacingLeft() {
		return facingLeft;
	}

	public void setFacingLeft(boolean facingLeft) {
		this.facingLeft = facingLeft;
	}

	public boolean isGrounded() {
		return grounded;
	}

	public void setGrounded(boolean grounded) {
		this.grounded = grounded;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getAttackType() {
		return attackType;
	}

	public void setAttackType(int attackType) {
		this.attackType = attackType;
	}

	public int getMovementSpeed() {
		return movementSpeed;
	}

	public void setMovementSpeed(int movementSpeed) {
		this.movementSpeed = movementSpeed;
	}

	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getJumpHeight() {
		return jumpHeight;
	}

	public void setJumpHeight(float jumpHeight) {
		this.jumpHeight = jumpHeight;
	}

	public Rectangle getHitBox() {
		return hitBox;
	}

	public void setHitBox(Rectangle hitBox) {
		this.hitBox = hitBox;
	}

}
