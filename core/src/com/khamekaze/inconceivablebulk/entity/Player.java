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
import com.badlogic.gdx.math.Rectangle;
import com.khamekaze.inconceivablebulk.MainGame;
import com.khamekaze.inconceivablebulk.screen.ScreenManager;

public class Player extends Entity {
	
	private boolean groundPound = false, pwoAttack = false, jump = false, justAttacked = false, excecuteGP = false;
	
	private int pickedUpCoins = 0;
	
	private Animation 		standingAnim, walkingAnim, attackingAnimOne, attackingAnimTwo, dyingAnim,
					  		jumpAnim, airAttackAnim;
	private TextureRegion[] walkFrames, stanceFrames, attackOneFrames, attackTwoFrames,
							jumpFrames, airAttackFrames, pwoFrames;
	private TextureRegion currentFrame;
	private Sprite streak, playerStomp;
	private Sprite portrait;
	private float rotation = 0;
	private int pwoFrameNumber = 0;
	private float damageDelayTime = 0.0f, stunTime = 0.0f;
	
	private boolean moveLeft = false, moveRight = false, stunned = false;
	
	private Random attackSelector = new Random();
	private int selectedAttack = 0;
	
	private float elapsedTime = 0f, attackFrame = 0.0f, jumpFrame = 0.0f;

	public Player(int hp, int attackDamage, int attackType, float movementSpeed) {
		super(hp, attackDamage, attackType, movementSpeed);
		x = MainGame.WIDTH / 2 - 50;
		y = groundY;
		walkFrames = new TextureRegion[11];
		stanceFrames = new TextureRegion[19];
		attackOneFrames = new TextureRegion[6];
		attackTwoFrames = new TextureRegion[6];
		airAttackFrames = new TextureRegion[5];
		pwoFrames = new TextureRegion[3];
		jumpFrames = new TextureRegion[9];
		loadSprites();
		walkingAnim = new Animation(1f/22f, walkFrames);
		standingAnim = new Animation(1f/22f, stanceFrames);
		attackingAnimOne = new Animation(1f/22f, attackOneFrames);
		attackingAnimTwo = new Animation(1f/22f, attackTwoFrames);
		airAttackAnim = new Animation(1f/22f, airAttackFrames);
		jumpAnim = new Animation(1f/22f, jumpFrames);
		playerStomp = new Sprite(new Texture(Gdx.files.internal("rawSprites/player/stomp/playerstomp.png")));
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
		
		for(int i = 0; i < 5; i++) {
			Texture texture = new Texture(Gdx.files.internal("rawSprites/player/airattack/playerairattack" + (i) + ".png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion sprite = new TextureRegion(texture);
			airAttackFrames[i] = sprite;
		}
		
		for(int i = 0; i < 3; i++) {
			Texture texture = new Texture(Gdx.files.internal("rawSprites/player/pwo/playerpwo" + (i) + ".png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion sprite = new TextureRegion(texture);
			pwoFrames[i] = sprite;
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
			if(!getDialogBoolean()) {
				if(!stunned)
					handleInput(delta, slowmo);
				applyGravity(delta);
			}
			rotation = 0;
		}
		

		if(!getDialogBoolean()) {
			if(excecuteGP) {
				jump = false;
				groundPound = true;
			}

			if(groundPound) {
				specialAttack(delta);
				if(grounded) {
					groundPound = false;
				}
			}
		}
		
		if(y > groundY)
			setGrounded(false);
		
		if(getDamageDelayTime() > 0) {
			setDamageDelayTime(getDamageDelayTime() - 0.1f);
			if(getDamageDelayTime() <= 0) {
				setDamageDelayTime(0);
				setDamageDelay(false);
			}
		}
		
		if(stunned && isGrounded()) {
			stunTime -= 0.1f;
			if(stunTime <0) {
				stunTime = 0;
				stunned = false;
			}
		}
	}
	
	public void render(SpriteBatch sb) {
		if(!getDialogBoolean()) {
			elapsedTime += Gdx.graphics.getDeltaTime();

			if(!getIsMoving()) {
				currentFrame = standingAnim.getKeyFrame(elapsedTime, true);
			} else if(getIsMoving() && isGrounded()) {
				currentFrame = walkingAnim.getKeyFrame(elapsedTime, true);
			}
			if(!isGrounded() && !isAttacking()) {
				jumpFrame += Gdx.graphics.getDeltaTime();
				currentFrame = jumpAnim.getKeyFrame(jumpFrame, false);
			} else if(!isGrounded() && !isAttacking() && !getIsMoving()) {
				jumpFrame += Gdx.graphics.getDeltaTime();
				currentFrame = jumpAnim.getKeyFrame(jumpFrame, false);
			}

			if(isAttacking() && isGrounded()) {
				attackFrame += Gdx.graphics.getDeltaTime();
				if(selectedAttack == 0)
					currentFrame = attackingAnimOne.getKeyFrame(attackFrame, false);
				else if(selectedAttack == 1)
					currentFrame = attackingAnimTwo.getKeyFrame(attackFrame, false);
			} else if(isAttacking() && !isGrounded()) {
				attackFrame += Gdx.graphics.getDeltaTime();
				currentFrame = airAttackAnim.getKeyFrame(attackFrame, false);
			}

			if(groundPound) {
				currentFrame = playerStomp;
			}

			if(pwoAttack && !isGrounded()) {
				currentFrame = pwoFrames[pwoFrameNumber];
			}

			if(!isAttacking() && attackFrame > 0) {
				attackFrame = 0;
			}

			if(jumpFrame > 0 && isGrounded()) {
				jumpFrame = 0;
			}

			
		}
		if(isFacingLeft() && !currentFrame.isFlipX()) {
			currentFrame.flip(true, false);
		} else if(isFacingRight() && currentFrame.isFlipX()) {
			currentFrame.flip(true, false);
		}
		
		sb.draw(currentFrame, x, y, 50, 50, 100, 100, 1f, 1f, (float) rotation);
		
		if(pwoAttack) {
			streak.draw(sb);
			streak.setAlpha(0);
		}
	}
	
	public void attacked(Rectangle attacker, float speed) {
		if(attacker.getX() > x) {
			x -= 1f * speed;
		} else if(attacker.getX() < x) {
			x += 1f * speed;
		}
	}
	
	public void handleInput(float delta, boolean slowmo) {
		
		if(!isAttacking()) {
			if(isFacingLeft()) {
				getAttackHitbox().setPosition(x, y + 50);
			} else if(isFacingRight()) {
				getAttackHitbox().setPosition(x + 50, y + 50);
			}
		}

		if(isAttacking() && getAttackLength() > 0) {
			setAttackLength(getAttackLength() - 0.1f * delta);
			if(getAttackLength() <= 0) {
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
			attackingAnimTwo.setFrameDuration(1f/2.2f);
			jumpAnim.setFrameDuration(1f/2.2f);
			airAttackAnim.setFrameDuration(1f/2.2f);
		} else if(!slowmo) {
			walkingAnim.setFrameDuration(1f/22f);
			standingAnim.setFrameDuration(1f/22f);
			attackingAnimOne.setFrameDuration(1f/22f);
			attackingAnimTwo.setFrameDuration(1f/22f);
			jumpAnim.setFrameDuration(1f/22f);
			airAttackAnim.setFrameDuration(1f/22f);
		}
		
		if(jump && isGrounded() && !isAttacking()) {
			jump(delta);
		}
		
		move(delta);
		
		if(isGrounded()) {
			jump = false;
			if(isFacingLeft()) {
				getAttackHitbox().setSize(50, 20);
				getAttackHitbox().setPosition(x, y + 50);
			} else if(isFacingRight()) {
				getAttackHitbox().setSize(50, 20);
				getAttackHitbox().setPosition(x + 50, y + 50);
			}
		} else if(!isGrounded()) {
			if(isFacingLeft()) {
				getAttackHitbox().setSize(50, 20);
				getAttackHitbox().setPosition(x, y + 50);
			} else if(isFacingRight()) {
				getAttackHitbox().setSize(50, 20);
				getAttackHitbox().setPosition(x + 50, y + 50);
			}
		}
	}
	
	public void jump(float delta) {
		y += getJumpHeight() * delta;
		getHitBox().y = y;
		getHitBox().x = x;
		grounded = false;
	}
	
	public void attack() {
		if(!getDialogBoolean()) {
			selectedAttack = attackSelector.nextInt(2);
			if(isGrounded()) {
				if(isFacingLeft()) {
					setIsAttacking(true);
				} else if(isFacingRight()) {
					setIsAttacking(true);
				}
			} else if(!isGrounded() && !groundPound) {


				if(isFacingLeft() && !Gdx.input.isKeyPressed(Input.Keys.S)) {

					setIsAttacking(true);

				} else if(isFacingRight() && !Gdx.input.isKeyPressed(Input.Keys.S)) {
					setIsAttacking(true);
				}
			}
		}
	}
	
	public void specialAttack(float speed) {
		if(!isAttacking()) {
			setIsAttacking(true);
		}
		setY(getY() - 35 * speed);
		getAttackHitbox().set(getX(), getY(), getHitBox().width, 2);
		if(getAttackHitbox().getY() < groundY) {
			getAttackHitbox().set(getX(), groundY, getHitBox().width, 2);
		}
		if(getY() <= groundY) {
			setY(groundY);
			setAttackLength(1);
			grounded = true;
			setIsAttacking(false);
			if(isFacingLeft()) {
				getAttackHitbox().setSize(50, 20);
				getAttackHitbox().setPosition(x, y + 50);
			} else if(isFacingRight()) {
				getAttackHitbox().setSize(50, 20);
				getAttackHitbox().setPosition(x + 50, y + 50);
			}
			
			groundPound = false;
			excecuteGP = false;
		}
	}
	
	public void move(float speed) {
		if(!getDialogBoolean()) {
			if(Gdx.input.isKeyPressed(Input.Keys.A) || moveLeft) {
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
			} else if(Gdx.input.isKeyPressed(Input.Keys.D) || moveRight) {
				if(grounded && isAttacking()) {
					setFacingLeft(false);
					setFacingRight(true);
					setIsMoving(false);
				} else {
					if(!groundPound) {
						if(x < maxDistanceX) {
							x += getMovementSpeed() * speed;
							setIsMoving(true);
							setFacingLeft(false);
							setFacingRight(true);
						} else if(x >= maxDistanceX) {
							x = maxDistanceX;
							setIsMoving(false);
						}
					}
				}
			} else {
				setIsMoving(false);
			}
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
	
	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public int getPwoFrameNumber() {
		return pwoFrameNumber;
	}
	
	public void setPwoFrameNumber(int number) {
		this.pwoFrameNumber = number;
	}
	
	public void setMoveLeft(boolean move) {
		this.moveLeft = move;
	}
	
	public void setMoveRight(boolean move) {
		this.moveRight = move;
	}
	
	public void setJumpBool(boolean jump) {
		this.jump = jump;
	}
	
	public boolean getJumpBool() {
		return jump;
	}
	
	public boolean getJustAttacked() {
		return justAttacked;
	}
	
	public void setJustAttacked(boolean attacked) {
		this.justAttacked = attacked;
	}
	
	public void setExcecuteGP(boolean gp) {
		this.excecuteGP = gp;
	}
	
	public void setPickedUpCoins(int coins) {
		this.pickedUpCoins = coins;
	}
	
	public int getPickedUpCoins() {
		return pickedUpCoins;
	}
	
	public float getStunTime() {
		return stunTime;
	}
	
	public void setStunTime(float time) {
		this.stunTime = time;
	}
	
	public void setStunned(boolean stunned) {
		this.stunned = stunned;
	}
	
	public boolean isStunned() {
		return stunned;
	}
} 
