package com.khamekaze.inconceivablebulk.entity;

public class Enemy extends Entity {

	public Enemy(int hp, int attackDamage, int attackType, int movementSpeed, float x, float y) {
		super(hp, attackDamage, attackType, movementSpeed);
		this.x = x;
		this.y = y;
	}
	
	public void update(float delta) {
		applyGravity(delta);
	}
	
}
