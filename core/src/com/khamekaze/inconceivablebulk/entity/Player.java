package com.khamekaze.inconceivablebulk.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.khamekaze.inconceivablebulk.MainGame;

public class Player extends Entity {
	
	private boolean groundPound = false;

	public Player(int hp, int attackDamage, int attackType, int movementSpeed) {
		super(hp, attackDamage, attackType, movementSpeed);
		x = MainGame.WIDTH / 2;
		y = MainGame.HEIGHT / 2;
	}
	
	public void update(float delta) {
//		System.out.println("X: " + x + " Y: " + y);
		
		if(isFacingLeft()) {
			getAttackHitbox().setPosition(x - 15, y + 25);
		} else if(isFacingRight()) {
			getAttackHitbox().setPosition(x + 25, y + 25);
		}
		
		if(isAttacking() && getAttackLength() > 0) {
			setAttackLength(getAttackLength() - 0.1f);
			if(getAttackLength() < 0) {
				setAttackLength(0);
			}
		} else if(isAttacking() && getAttackLength() == 0 && grounded) {
			setIsAttacking(false);
		} else if(!isAttacking() && getAttackLength() != 1 && grounded) {
			setAttackLength(1);
		}
		
		applyGravity(delta);
		handleInput(delta);
		if(groundPound) {
			specialAttack();
			if(grounded) {
				groundPound = false;
				setIsAttacking(false);
			}
		}
	}
	
	public void handleInput(float delta) {
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isGrounded()) {
			jump(delta);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			move(getMoveLeft());
		} else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			move(getMoveRight());
		}
		
		if(Gdx.input.justTouched()) {
			attack();
		}
		
		if(grounded) {
			if(isFacingLeft()) {
				getAttackHitbox().setSize(40, 10);
				getAttackHitbox().setPosition(x - 15, y + 25);
			} else if(isFacingRight()) {
				getAttackHitbox().setSize(40, 10);
				getAttackHitbox().setPosition(x + 25, y + 25);
			}
		}
	}
	
	public void attack() {
		if(grounded) {
			setIsAttacking(true);
			if(isFacingLeft()) {
				System.out.println("GROUND ATTACK LEFT");
			} else if(isFacingRight()) {
				System.out.println("GROUND ATTACK RIGHT");
			}
		} else if(!grounded) {
			
			if(Gdx.input.isKeyPressed(Input.Keys.S)) {
				System.out.println("DOWNWARD ATTACK");
				groundPound = true;
			}
			
			if(isFacingLeft() && !Gdx.input.isKeyPressed(Input.Keys.S)) {
				setIsAttacking(true);
				System.out.println("AIR ATTACK LEFT");
			} else if(isFacingRight() && !Gdx.input.isKeyPressed(Input.Keys.S)) {
				setIsAttacking(true);
				System.out.println("AIR ATTACK RIGHT");
			}
		}
	}
	
	public void specialAttack() {
		if(!isAttacking()) {
			setIsAttacking(true);
		}
		
		setY(getY() - 35);
		getAttackHitbox().set(getX(), getY() - 25, getHitBox().width, 2);
		if(getAttackHitbox().getY() < 50) {
			getAttackHitbox().set(getX(), 50, getHitBox().width, 2);
		}
		if(getY() < 50) {
			setY(50);
			setAttackLength(0);
			grounded = true;
			if(isFacingLeft()) {
				getAttackHitbox().setSize(40, 10);
				getAttackHitbox().setPosition(x - 15, y + 25);
			} else if(isFacingRight()) {
				getAttackHitbox().setSize(40, 10);
				getAttackHitbox().setPosition(x + 25, y + 25);
			}
		}
	}
	
	@Override
	public void move(int direction) {
		if(direction == getMoveLeft()) {
			if(grounded && attacking) {
				setFacingLeft(true);
				setFacingRight(false);
			} else {
				if(!groundPound) {
					x -= getMovementSpeed();
					setFacingLeft(true);
					setFacingRight(false);
				}
			}
		} else if(direction == getMoveRight()) {
			if(grounded && attacking) {
				setFacingLeft(false);
				setFacingRight(true);
			} else {
				if(!groundPound) {
					x += getMovementSpeed();
					setFacingLeft(false);
					setFacingRight(true);
				}
			}
		}
	}
}
