package com.khamekaze.inconceivablebulk.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.khamekaze.inconceivablebulk.MainGame;

public class Entity {
	
	private int stopped = 0, moveLeft = 1, moveRight = 2; 
	
	private boolean facingRight = true, facingLeft = false, moving = false;

	protected boolean grounded = false, attacking = false;
	
	private int hp, maxHp, attackDamage, attackType;
	private float movementSpeed;
	private float gravity = 0.4f, attackLength = 1;
	
	public float groundY = 50;
	
	private boolean damageDelay = false;

	protected float y, x;

	private float jumpHeight = 0;
	
	private Rectangle hitBox, attackHitbox;
	
	public Entity(int hp, int attackDamage, int attackType, float movementSpeed) {
		this.hp = hp;
		maxHp = hp;
		this.attackDamage = attackDamage;
		this.attackType = attackType;
		this.movementSpeed = movementSpeed;
		hitBox = new Rectangle(MainGame.WIDTH / 2, MainGame.HEIGHT / 2, 100, 100);
		attackHitbox = new Rectangle(MainGame.WIDTH / 2, MainGame.HEIGHT / 2, 50, 20);
	}
	
	public void update(float delta) {
		applyGravity(delta);
	}
	
	public void move(int direction, float delta) {
		moving = false;
		if(direction == moveLeft) {
			if(grounded && attacking) {
				facingLeft = true;
				facingRight = false;
				moving = false;
			} else {
				x -= movementSpeed * delta;
				facingLeft = true;
				facingRight = false;
				moving = true;
			}
		} else if(direction == moveRight) {
			if(grounded && attacking) {
				facingLeft = false;
				facingRight = true;
				moving = false;
			} else {
				x += movementSpeed * delta;
				facingLeft = false;
				facingRight = true;
				moving = true;
			}
		} else if(direction == stopped) {
			moving = false;
		}
	}
	
	public void applyGravity(float delta) {
		if(y > groundY)
			grounded = false;
		if(grounded && jumpHeight < 10) {
			jumpHeight = 10;
			gravity = 0.4f;
			gravity = gravity * delta;
		} else if(!grounded) {
			gravity = 0.4f;
			gravity = gravity * delta;
			
			jumpHeight -= gravity;
			
			y += jumpHeight * delta;
			if(y < groundY) {
				y = groundY;
			}
		}
		
		if(y == groundY) {
			grounded = true;
		}
		
		hitBox.x = x;
		hitBox.y = y;
	}
	
	public void attacked(Rectangle attacker, float speed) {
		if(attacker.getX() > x) {
			x -= 1f * speed;
		} else if(attacker.getX() < x) {
			x += 1f * speed;
		}
	}
	
	public void stompAttacked(Player attacker, float speed) {
		if(attacker.getX() > x) {
			x -= 10f * speed;
		} else if(attacker.getX() < x) {
			x += 10f * speed;
		}
	}
	
	public void takeDamage(int damage) {
		if(!damageDelay) {
			hp -= damage;
			if(hp < 0) {
				hp = 0;
			}
			damageDelay = true;
		}
	}
	
	public void regenerate(int regAmount) {
		hp += regAmount;
		if(hp > maxHp) {
			hp = maxHp;
		}
	}
	
	public boolean checkCollision(Rectangle rect) {
		return rect.overlaps(hitBox);
	}
	
	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
		hitBox.x = x;
		hitBox.y = y;
	}
	
	public boolean getDamageDelay() {
		return damageDelay;
	}
	
	public void setDamageDelay(boolean damageDelay) {
		this.damageDelay = damageDelay;
	}
	
	public float getAttackLength() {
		return attackLength;
	}
	
	public void setAttackLength(float attackLength) {
		this.attackLength = attackLength;
	}
	
	public boolean isAttacking() {
		return attacking;
	}
	
	public void setIsAttacking(boolean attacking) {
		this.attacking = attacking;
	}
	
	public Rectangle getAttackHitbox() {
		return attackHitbox;
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

	public float getMovementSpeed() {
		return movementSpeed;
	}

	public void setMovementSpeed(float movementSpeed) {
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
		hitBox.x = this.x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
		hitBox.y = this.y;
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
	
	public boolean getIsMoving() {
		return moving;
	}
	
	public void setIsMoving(boolean moving) {
		this.moving = moving;
	}

}
