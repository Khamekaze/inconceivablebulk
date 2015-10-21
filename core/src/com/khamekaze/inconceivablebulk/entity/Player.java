package com.khamekaze.inconceivablebulk.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.khamekaze.inconceivablebulk.MainGame;

public class Player extends Entity {

	public Player(int hp, int attackDamage, int attackType, int movementSpeed) {
		super(hp, attackDamage, attackType, movementSpeed);
		x = MainGame.WIDTH / 2;
		y = MainGame.HEIGHT / 2;
	}
	
	public void update(float delta) {
//		System.out.println("X: " + x + " Y: " + y);
		applyGravity(delta);
		handleInput(delta);
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
	}
	
	public void attack() {
		if(grounded) {
			
			if(isFacingLeft()) {
				System.out.println("GROUND ATTACK LEFT");
			} else if(isFacingRight()) {
				System.out.println("GROUND ATTACK RIGHT");
			}
		} else if(!grounded) {
			
			if(Gdx.input.isKeyPressed(Input.Keys.S)) {
				System.out.println("DOWNWARD ATTACK");
			}
			
			if(isFacingLeft() && !Gdx.input.isKeyPressed(Input.Keys.S)) {
				System.out.println("AIR ATTACK LEFT");
			} else if(isFacingRight() && !Gdx.input.isKeyPressed(Input.Keys.S)) {
				System.out.println("AIR ATTACK RIGHT");
			}
		}
		
	}
}
