package com.khamekaze.inconceivablebulk.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.khamekaze.inconceivablebulk.MainGame;

public class Player extends Entity {
	
	private boolean groundPound = false;
	
	private Animation standingAnim, walkingAnim, attackingAnim, dyingAnim, jumpAnim, stompAnim;
	private Texture walkSheet;
	private TextureRegion[] walkFrames;
	private TextureRegion currentFrame;
	
	private float elapsedTime = 0f;

	public Player(int hp, int attackDamage, int attackType, int movementSpeed) {
		super(hp, attackDamage, attackType, movementSpeed);
		x = MainGame.WIDTH / 2 - 50;
		y = MainGame.HEIGHT / 2;
		walkFrames = new TextureRegion[11];
		loadSprites();
		walkingAnim = new Animation(1f/22f, walkFrames);
		elapsedTime = 0f;
	}
	
	public void loadSprites() {
		for(int i = 0; i < 11; i++) {
			Texture texture = new Texture(Gdx.files.internal("rawSprites/playerrun" + (i + 1) + ".png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion sprite = new TextureRegion(texture);
			walkFrames[i] = sprite;
		}
	}
	
	public void update(float delta, boolean slowmo) {
		
		if(isFacingLeft()) {
			getAttackHitbox().setPosition(x, y + 25);
		} else if(isFacingRight()) {
			getAttackHitbox().setPosition(x + 50, y + 25);
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
		
		if(slowmo) {
			walkingAnim.setFrameDuration(1f/2.2f);
		} else if(!slowmo) {
			walkingAnim.setFrameDuration(1f/22f);
		}
		
		handleInput(delta, slowmo);
		
		applyGravity(delta);
		
		if(groundPound) {
			specialAttack(delta);
			if(grounded) {
				groundPound = false;
				setIsAttacking(false);
			}
		}
	}
	
	public void render(SpriteBatch sb) {
		elapsedTime += Gdx.graphics.getDeltaTime();
		currentFrame = walkingAnim.getKeyFrame(elapsedTime, true);
		if(isFacingLeft() && !currentFrame.isFlipX()) {
			currentFrame.flip(true, false);
		} else if(isFacingRight() && currentFrame.isFlipX()) {
			currentFrame.flip(true, false);
		}
		
		sb.draw(currentFrame, x, y);
	}
	
	public void handleInput(float delta, boolean slowmo) {
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isGrounded()) {
			jump(delta);
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			if(x > 0) {
				move(getMoveLeft(), delta);
				if(x <= 0) {
					x = 0;
				}
			}
		} else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			move(getMoveRight(), delta);
		}
		
		if(Gdx.input.justTouched() && !slowmo) {
			attack();
		}
		
		if(grounded) {
			if(isFacingLeft()) {
				getAttackHitbox().setSize(50, 20);
				getAttackHitbox().setPosition(x, y + 25);
			} else if(isFacingRight()) {
				getAttackHitbox().setSize(50, 20);
				getAttackHitbox().setPosition(x + 50, y + 25);
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
	
	public void specialAttack(float speed) {
		if(!isAttacking()) {
			setIsAttacking(true);
		}
		
		setY(getY() - 35 * speed);
		getAttackHitbox().set(getX(), getY(), getHitBox().width, 2);
		if(getAttackHitbox().getY() < 50) {
			getAttackHitbox().set(getX(), 50, getHitBox().width, 2);
		}
		if(getY() < 50) {
			setY(50);
			setAttackLength(0);
			grounded = true;
			if(isFacingLeft()) {
				getAttackHitbox().setSize(50, 20);
				getAttackHitbox().setPosition(x, y + 25);
			} else if(isFacingRight()) {
				getAttackHitbox().setSize(50, 20);
				getAttackHitbox().setPosition(x + 50, y + 25);
			}
		}
	}
	
	public void move(int direction, float speed) {
		if(direction == getMoveLeft()) {
			if(grounded && attacking) {
				setFacingLeft(true);
				setFacingRight(false);
			} else {
				if(!groundPound) {
					x -= getMovementSpeed() * speed;
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
					x += getMovementSpeed() * speed;
					setFacingLeft(false);
					setFacingRight(true);
				}
			}
		}
	}
	
	public void setGroundPound(boolean groundpound) {
		this.groundPound = groundpound;
	}
	
	public boolean getGroundPound() {
		return groundPound;
	}
}
