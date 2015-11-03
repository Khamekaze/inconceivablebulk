package com.khamekaze.inconceivablebulk.entity;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.khamekaze.inconceivablebulk.MainGame;

public class Player extends Entity {
	
	private boolean groundPound = false, pwoAttack = false;
	
	private Animation standingAnim, walkingAnim, attackingAnimOne, attackingAnimTwo, dyingAnim, jumpAnim, stompAnim;
	private TextureRegion[] walkFrames, stanceFrames, attackOneFrames, attackTwoFrames, jumpFrames;
	private TextureRegion currentFrame;
	private Sprite streak;
	
	private Random attackSelector = new Random();
	private int selectedAttack = 0;
	
	private float elapsedTime = 0f, attackFrame = 0.0f, jumpFrame = 0.0f;

	public Player(int hp, int attackDamage, int attackType, float movementSpeed) {
		super(hp, attackDamage, attackType, movementSpeed);
		x = MainGame.WIDTH / 2 - 50;
		y = MainGame.HEIGHT / 2;
		walkFrames = new TextureRegion[11];
		stanceFrames = new TextureRegion[19];
		attackOneFrames = new TextureRegion[6];
		attackTwoFrames = new TextureRegion[6];
		jumpFrames = new TextureRegion[9];
		loadSprites();
		walkingAnim = new Animation(1f/22f, walkFrames);
		standingAnim = new Animation(1f/22f, stanceFrames);
		attackingAnimOne = new Animation(1f/22f, attackOneFrames);
		attackingAnimTwo = new Animation(1f/22f, attackTwoFrames);
		jumpAnim = new Animation(1f/22f, jumpFrames);
		elapsedTime = 0f;
	}
	
	public void loadSprites() {
		for(int i = 0; i < 11; i++) {
			Texture texture = new Texture(Gdx.files.internal("rawSprites/playerrun" + (i) + ".png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion sprite = new TextureRegion(texture);
			walkFrames[i] = sprite;
		}
		
		for(int i = 0; i < 19; i++) {
			Texture texture = new Texture(Gdx.files.internal("rawSprites/player/stance/playerstance" + (i) + ".png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion sprite = new TextureRegion(texture);
			stanceFrames[i] = sprite;
		}
		
		for(int i = 0; i < 6; i++) {
			Texture texture = new Texture(Gdx.files.internal("rawSprites/player/attack/attackOne/playerattack" + (i) + ".png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion sprite = new TextureRegion(texture);
			attackOneFrames[i] = sprite;
		}
		
		for(int i = 0; i < 6; i++) {
			Texture texture = new Texture(Gdx.files.internal("rawSprites/player/attack/attackTwo/playerattack" + (i) + ".png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion sprite = new TextureRegion(texture);
			attackTwoFrames[i] = sprite;
		}
		
		for(int i = 0; i < 9; i++) {
			Texture texture = new Texture(Gdx.files.internal("rawSprites/player/jump/playerjump" + (i) + ".png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion sprite = new TextureRegion(texture);
			jumpFrames[i] = sprite;
		}
		
		streak = new Sprite(new Texture(Gdx.files.internal("rawSprites/streak.png")));
	}
	
	public void update(float delta, boolean slowmo) {
		if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			if(pwoAttack)
				pwoAttack = false;
			else if(!pwoAttack)
				pwoAttack = true;
		}
		
		if(!pwoAttack) {
			handleInput(delta, slowmo);
			applyGravity(delta);
		}
		
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
		
		if(!getIsMoving()) {
			currentFrame = standingAnim.getKeyFrame(elapsedTime, true);
		} else if(getIsMoving() && isGrounded()) {
			currentFrame = walkingAnim.getKeyFrame(elapsedTime, true);
		} else if(!isGrounded() && !isAttacking()) {
			jumpFrame += Gdx.graphics.getDeltaTime();
			currentFrame = jumpAnim.getKeyFrame(jumpFrame, false);
		}
		
		if(isAttacking() && isGrounded()) {
			attackFrame += Gdx.graphics.getDeltaTime();
			if(selectedAttack == 0)
				currentFrame = attackingAnimOne.getKeyFrame(attackFrame, false);
			else if(selectedAttack == 1)
				currentFrame = attackingAnimTwo.getKeyFrame(attackFrame, false);
		}
		
		if(!isAttacking() && attackFrame > 0) {
			attackFrame = 0;
		}
		
		if(jumpFrame > 0 && isGrounded()) {
			jumpFrame = 0;
		}
		
		if(isFacingLeft() && !currentFrame.isFlipX()) {
			currentFrame.flip(true, false);
		} else if(isFacingRight() && currentFrame.isFlipX()) {
			currentFrame.flip(true, false);
		}
		
		sb.draw(currentFrame, x, y);
		
		if(pwoAttack) {
			streak.draw(sb);
			streak.setAlpha(0);
		}
	}
	
	public void handleInput(float delta, boolean slowmo) {
		if(isFacingLeft()) {
			getAttackHitbox().setPosition(x, y + 50);
		} else if(isFacingRight()) {
			getAttackHitbox().setPosition(x + 50, y + 50);
		}

		if(isAttacking() && getAttackLength() > 0) {
			setAttackLength(getAttackLength() - 0.1f * delta);
			if(getAttackLength() < 0) {
				setAttackLength(0);
			}
		} else if(isAttacking() && getAttackLength() == 0 && isGrounded()) {
			setIsAttacking(false);
		} else if(!isAttacking() && getAttackLength() != 1 && isGrounded()) {
			setAttackLength(1);
		}
		
		if(slowmo) {
			walkingAnim.setFrameDuration(1f/2.2f);
			standingAnim.setFrameDuration(1f/2.2f);
			attackingAnimOne.setFrameDuration(1f/2.2f);
			jumpAnim.setFrameDuration(1f/2.2f);
		} else if(!slowmo) {
			walkingAnim.setFrameDuration(1f/22f);
			standingAnim.setFrameDuration(1f/22f);
			attackingAnimOne.setFrameDuration(1f/22f);
			jumpAnim.setFrameDuration(1f/22f);
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isGrounded()) {
			jump(delta);
		}
		
		move(delta);
		
		if(Gdx.input.justTouched() && !slowmo) {
			selectedAttack = attackSelector.nextInt(2);
			attack();
		}
		
		if(grounded) {
			if(isFacingLeft()) {
				getAttackHitbox().setSize(50, 20);
				getAttackHitbox().setPosition(x, y + 50);
			} else if(isFacingRight()) {
				getAttackHitbox().setSize(50, 20);
				getAttackHitbox().setPosition(x + 50, y + 50);
			}
		}
	}
	
	public void attack() {
		if(isGrounded()) {
			if(isFacingLeft()) {
				setIsAttacking(true);
			} else if(isFacingRight()) {
				setIsAttacking(true);
			}
		} else if(!isGrounded() && !groundPound) {
			if(Gdx.input.isKeyPressed(Input.Keys.S)) {
				groundPound = true;
			}
			
			if(isFacingLeft() && !Gdx.input.isKeyPressed(Input.Keys.S)) {
				setIsAttacking(true);
			} else if(isFacingRight() && !Gdx.input.isKeyPressed(Input.Keys.S)) {
				setIsAttacking(true);
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
				getAttackHitbox().setPosition(x, y + 50);
			} else if(isFacingRight()) {
				getAttackHitbox().setSize(50, 20);
				getAttackHitbox().setPosition(x + 50, y + 50);
			}
		}
	}
	
	public void move(float speed) {
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			if(grounded && isAttacking()) {
				setFacingLeft(true);
				setFacingRight(false);
				setIsMoving(false);
			} else {
				if(!groundPound) {
					if(x > 0) {
						x -= getMovementSpeed() * speed;
						setIsMoving(true);
						setFacingLeft(true);
						setFacingRight(false);
					} else if(x < 0) {
						x = 0;
					} else if(x == 0) {
						setIsMoving(false);
						setFacingLeft(true);
						setFacingRight(false);
					}
				}
			}
		} else if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			if(grounded && isAttacking()) {
				setFacingLeft(false);
				setFacingRight(true);
				setIsMoving(false);
			} else {
				if(!groundPound) {
					x += getMovementSpeed() * speed;
					setIsMoving(true);
					setFacingLeft(false);
					setFacingRight(true);
				}
			}
		} else {
			setIsMoving(false);
		}
	}
	
	public void setGroundPound(boolean groundpound) {
		this.groundPound = groundpound;
	}
	
	public boolean getGroundPound() {
		return groundPound;
	}
	
	public void setPwoAttack(boolean attack) {
		this.pwoAttack = attack;
	}
	
	public boolean isPwoAttack() {
		return pwoAttack;
	}
	
	public Sprite getStreakSprite() {
		return streak;
	}
}
