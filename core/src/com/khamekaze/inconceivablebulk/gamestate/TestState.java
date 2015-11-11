package com.khamekaze.inconceivablebulk.gamestate;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.khamekaze.inconceivablebulk.MainGame;
import com.khamekaze.inconceivablebulk.combo.Combo;
import com.khamekaze.inconceivablebulk.entity.Coin;
import com.khamekaze.inconceivablebulk.entity.Enemy;
import com.khamekaze.inconceivablebulk.entity.Player;
import com.khamekaze.inconceivablebulk.environment.SpawnPoint;
import com.khamekaze.inconceivablebulk.gui.CoinsGUI;
import com.khamekaze.inconceivablebulk.gui.HealthGUI;
import com.khamekaze.inconceivablebulk.screen.ScreenManager;

public class TestState {
	
	private HealthGUI healthGUI;
	private CoinsGUI coinsGUI;
	
	private ShapeRenderer renderer;
	
	private Animation hitSplashOneAnim;
	private TextureRegion[] hitSplashOneFrames;
	private TextureRegion currentFrame;
	
	private Vector3 joystickPos = new Vector3();
	private Vector3 touchPos = new Vector3();
	private Vector3 attackButtonPos = new Vector3();
	private Vector3 jumpButtonPos = new Vector3();
	
	private int movePointer = 0, jumpPointer = 0, attackPointer = 0;
	
	private float hitSplashTime = 0.0f, dialogAlpha = 0.0f;
	public float groundY = 110, levelWidth;
	private boolean showSplash = false, movingJoystick = false, hasStarted = false, pwoActive = false;
	private float spawnDelayTime = 0;
	
	private Vector2 pwoVector, playerVector;
	
	private Combo combo;
	
	private Random random = new Random();
	
	private Player player;
	private Enemy enemy, enemyTwo, enemyThree, enemyFour, enemyFive, enemySix, enemySeven, enemyEight, enemyNine, enemyTen, enemyEleven;
	private Array<Enemy> entities;
	private Array<Sprite> bgSprites;
	private Array<Coin> coins = new Array<Coin>();
	private Array<SpawnPoint> spawnPoints;
	private float[] spawnPointsX = new float[5];
	
	private boolean cameraShake = false, enemyKilled = false, slowMotion = false, cameraXSet = false, dialog = false, bossFight = false, playerInBossArea = false;
	private float cameraShakeDuration = 0.0f, pwoDuration = 0.0f, pwoCameraX = 0.0f, joyStickRingX;
	
	private Sprite bgPartZero, bgPartOne, bgPartTwo, bgPartThree, joystickRing, joystick, attackButton, jumpButton;
	private Sprite tutorialBoxOne;
	
	public TestState() {
		
		healthGUI = new HealthGUI(ScreenManager.getCurrentScreen().camera.position.x, ScreenManager.getCurrentScreen().camera.position.y);
		coinsGUI = new CoinsGUI(ScreenManager.getCurrentScreen().camera.position.x, ScreenManager.getCurrentScreen().camera.position.y);
		
		spawnPoints = new Array<SpawnPoint>();
		setSpawnPoints();
		
		player = new Player(25, 2, 0, 6);
		player.groundY = groundY;
		player.setPosition(3500, groundY);
		enemy = new Enemy(10, 3, 0, 1, MainGame.WIDTH / 2 + 100, groundY);
		enemy.groundY = groundY;
		
		entities = new Array<Enemy>();
		entities.add(enemy);
		
		
		renderer = new ShapeRenderer();
		bgPartZero = new Sprite(new Texture(Gdx.files.internal("rawSprites/environment/levelone/levelonepartzero.png")));
		bgPartZero.setPosition(-50, 0);
		bgPartOne = new Sprite(new Texture(Gdx.files.internal("rawSprites/environment/levelone/levelonepartone.png")));
		bgPartOne.setPosition(bgPartZero.getWidth() - 50, 0);
		bgPartTwo = new Sprite(new Texture(Gdx.files.internal("rawSprites/environment/levelone/leveloneparttwo.png")));
		bgPartTwo.setPosition(bgPartOne.getWidth() * 2 - 50, 0);
		bgPartThree = new Sprite(new Texture(Gdx.files.internal("rawSprites/environment/levelone/levelonepartthree.png")));
		bgPartThree.setPosition(bgPartOne.getWidth() * 3 - 50, 0);
		
		bgSprites = new Array<Sprite>();
		bgSprites.add(bgPartZero);
		bgSprites.add(bgPartOne);
		bgSprites.add(bgPartTwo);
		bgSprites.add(bgPartThree);
		
		for(Sprite s : bgSprites) {
			levelWidth += s.getWidth();
		}
		
		levelWidth -= 200;
		
		player.maxDistanceX = levelWidth;
		for(Enemy e : entities) {
			e.maxDistanceX = levelWidth;
		}
		
		
		pwoVector = new Vector2();
		playerVector = new Vector2();
		
		combo = new Combo();
		
		hitSplashOneFrames = new TextureRegion[4];
		loadSprites();
		hitSplashOneAnim = new Animation(1f/22f, hitSplashOneFrames);
		
		joystickRing = new Sprite(new Texture(Gdx.files.internal("rawSprites/gui/joystickring.png")));
		joystickPos.set(ScreenManager.getCurrentScreen().camera.position.x + 30, ScreenManager.getCurrentScreen().camera.position.y + 60, 0);
		joystickRing.setPosition(joystickPos.x, joystickPos.y);
		joyStickRingX = joystickRing.getX() + 50;
		joystick = new Sprite(new Texture(Gdx.files.internal("rawSprites/gui/joystick.png")));
		joystick.setPosition(joyStickRingX, joystickRing.getY());
		
		attackButton = new Sprite(new Texture(Gdx.files.internal("rawSprites/gui/attackButton.png")));
		attackButtonPos.set(ScreenManager.getCurrentScreen().camera.position.x + 300, ScreenManager.getCurrentScreen().camera.position.y + 60, 0);
		attackButton.setPosition(attackButtonPos.x, attackButtonPos.y);
		jumpButton = new Sprite(new Texture(Gdx.files.internal("rawSprites/gui/jumpButton.png")));
		jumpButtonPos.set(ScreenManager.getCurrentScreen().camera.position.x + 400, ScreenManager.getCurrentScreen().camera.position.y + 120, 0);
		jumpButton.setPosition(jumpButtonPos.x, jumpButtonPos.y);
		
		tutorialBoxOne = new Sprite(new Texture(Gdx.files.internal("rawSprites/dialog/tutorialone.png")));
		tutorialBoxOne.setPosition(enemy.getX() - 75, enemy.getY() + 125);
		
	}
	
	public void update(float delta, boolean slowmo) {
		player.update(delta, slowmo);
		player.setDialogBoolean(dialog);
		
		healthGUI.setLifeBarWidth(10 * player.getHp());
		for(Enemy e : entities) {
			e.update(delta, player.getHitBox());
			e.setDialogBoolean(dialog);
		}

		combo.setX(ScreenManager.getCurrentScreen().camera.position.x + 50);
		combo.setY(ScreenManager.getCurrentScreen().camera.position.y + 150);
		combo.update(delta);
		
		float healthGUIX = ScreenManager.getCurrentScreen().camera.position.x - ScreenManager.getCurrentScreen().camera.viewportWidth / 2 + 15;
		float healthGUIY = ScreenManager.getCurrentScreen().camera.position.y + ScreenManager.getCurrentScreen().camera.viewportHeight / 2 -
							65;
		
		healthGUI.setPosition(healthGUIX, healthGUIY);
		healthGUI.update();
		coinsGUI.setPosition(healthGUIX, healthGUIY - 10);

		if(slowmo) {
			hitSplashOneAnim.setFrameDuration(1f/2.2f);
		} else if(!slowmo) {
			hitSplashOneAnim.setFrameDuration(1f/22f);
		}

		joystickPos.set(ScreenManager.getCurrentScreen().camera.position.x - 310, ScreenManager.getCurrentScreen().camera.position.y - 180, 0);
		jumpButtonPos.set(ScreenManager.getCurrentScreen().camera.position.x + 230, ScreenManager.getCurrentScreen().camera.position.y - 180, 0);
		attackButtonPos.set(ScreenManager.getCurrentScreen().camera.position.x + 130, ScreenManager.getCurrentScreen().camera.position.y - 180, 0);
		
		if(coins.size != 0) {
			for(Coin c : coins) {
				if(!c.isPickedUp()) {
					c.checkIfPickedUp(player);
					c.update(delta);
				}
			}
		}
		
		coinsGUI.setCoins(player.getPickedUpCoins());
		
		pwoAttack(delta);

		checkCollisions(delta);
		
		if(!player.isStunned())
			handleJoystick();
		
		if(!player.isPwoAttack())
			spawnEnemies();
		
		checkIfBossBattle();
		if(bossFight)
			bossFight();
		
		winStateWhoop();
	}
	
	public void render(SpriteBatch sb) {
		
		bgPartZero.draw(sb);
		bgPartOne.draw(sb);
		bgPartTwo.draw(sb);
		bgPartThree.draw(sb);
		
		if(player.getX() >= tutorialBoxOne.getX() - 25 && player.getX() <= tutorialBoxOne.getX() + 175) {
			if(dialogAlpha < 1)
				dialogAlpha += 0.1f;
			else if(dialogAlpha >= 1)
				dialogAlpha = 1;
			
		} else {
			if(dialogAlpha > 0)
				dialogAlpha -= 0.1f;
			else if(dialogAlpha <= 0.2f)
				dialogAlpha = 0;
		}
		
		tutorialBoxOne.setAlpha(dialogAlpha);
		tutorialBoxOne.draw(sb);
		
		for(Enemy e : entities) {
			e.render(sb);
		}
		
		if(coins.size != 0) {
			for(Coin c : coins) {
				if(!c.isPickedUp())
					c.render(sb);
			}
		}
		player.render(sb);
		
		
			
		
		if(player.isAttacking() && !player.getGroundPound()) {
			hitSplashTime += Gdx.graphics.getDeltaTime();
			currentFrame = hitSplashOneAnim.getKeyFrame(hitSplashTime);
		} else if(!player.isAttacking()) {
			hitSplashTime = 0;
			showSplash = false;
		}
		
		if(player.isFacingRight()) {
			if(currentFrame.isFlipX()) {
				currentFrame.flip(true, false);
			}
		} else if(player.isFacingLeft()) {
			if(!currentFrame.isFlipX()) {
				currentFrame.flip(true, false);
			}
		}
		
		if(showSplash) {
			if(player.isFacingLeft()) {
				sb.draw(currentFrame, player.getX() - 80, player.getY() + 8);
			} else if(player.isFacingRight()) {
				sb.draw(currentFrame, player.getX() + 80, player.getY() + 8);
			}
		}
		
		joystickRing.setPosition(joystickPos.x, joystickPos.y);
		joyStickRingX = joystickRing.getX() + 50;
		
		if(!movingJoystick)
			joystick.setPosition(joyStickRingX - 50, joystickRing.getY());
		else if(movingJoystick) {
			joystick.setPosition(ScreenManager.getCurrentScreen().inputManager.getMouseHitbox().x - 50 + 5,
								 ScreenManager.getCurrentScreen().inputManager.getMouseHitbox().y - 50 + 5);
		}
		
		jumpButton.setPosition(jumpButtonPos.x, jumpButtonPos.y);
		attackButton.setPosition(attackButtonPos.x, attackButtonPos.y);
		
		joystickRing.draw(sb);
		joystick.draw(sb);
		
		attackButton.draw(sb);
		jumpButton.draw(sb);
		
		combo.render(sb);
		healthGUI.render(sb);
		coinsGUI.render(sb);
	}
	
	public void loadSprites() {
		for(int i = 0; i < 4; i++) {
			Texture texture = new Texture(Gdx.files.internal("rawSprites/player/attack/attacksplash/splashOne/attacksplash" + (i) + ".png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			TextureRegion sprite = new TextureRegion(texture);
			hitSplashOneFrames[i] = sprite;
		}
		
		currentFrame = hitSplashOneFrames[0];
	}
	
	public void setSpawnPoints() {
		spawnPointsX[0] = 800;
		spawnPointsX[1] = 2000;
		spawnPointsX[2] = 3000;
		spawnPointsX[3] = 3800;
		spawnPointsX[4] = 4100;
		
		SpawnPoint spawn;
		for(int i = 0; i < 5; i++) {
			spawn = new SpawnPoint(spawnPointsX[i]);
			spawnPoints.add(spawn);
		}
	}
	
	public void spawnCoin() {
		Coin coin = new Coin(player.getX() + 50, player.getY() + 100, groundY);
		coins.add(coin);
	}
	
	public void checkIfBossBattle() {
		int spawnCount = 0;
		int enemyCount = 0;
		for(SpawnPoint s : spawnPoints) {
			if(!s.isChecked())
				spawnCount++;
		}
		
		for(Enemy e : entities) {
			if(e.getHp() > 0) {
				enemyCount++;
			}
		}
		
		if(enemyCount > 0 || spawnCount > 0) {
			bossFight = false;
		} else {
			bossFight = true;
		}
		
		if(bossFight && player.getX() > levelWidth - 450) {
			playerInBossArea = true;
			
			if(player.getX() > levelWidth - 450) {
				player.setMoveLeft(true);
				player.setMoveRight(false);
				player.move(1);
				if(player.getX() <= levelWidth - 450) {
					dialog = true;
					player.setX(levelWidth - 450); 
					player.setMoveLeft(false);
					player.setIsMoving(false);
					player.setFacingRight(true);
					player.setFacingLeft(false);
					player.setIsMoving(false);
				}
			}
		}
	}
	
	public void winStateWhoop() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.X)) {
			for(Enemy e : entities) {
				e.setHp(0);
			}
			
			for(SpawnPoint p : spawnPoints) {
				p.setChecked(true);
			}
		}
	}
	
	public void bossFight() {
		
	}
	
	public void spawnEnemies() {
		for(SpawnPoint s : spawnPoints) {
			if(player.getX() > s.getX() - 800 && !s.isChecked() && spawnDelayTime == 0) {
				Enemy generated;
				for(int i = 0; i < 10; i++) {
					float movement = random.nextInt(4) + 2;
					float rX = random.nextInt(2);
					if(rX == 0) {
						float newX = random.nextInt(100);
						rX = player.getX() + 700 + newX;
					} else if(rX == 1) {
						float newX = random.nextInt(100);
						rX = player.getX() - 700 - newX;
					}
					if(rX < 0) {
						float newX = random.nextInt(100);
						rX = 800 + newX;
					} else if(player.getX() < 100) {
						float newX = random.nextInt(200);
						rX = 1300 + newX;
					}
					
					if(rX > levelWidth) {
						int newX = random.nextInt(100);
						rX = levelWidth - newX;
					}
					generated = new Enemy(10, 3, 0, movement, rX, groundY);
					generated.groundY = groundY;
					if(hasStarted)
						generated.setAlerted(true);
					else 
						generated.setAlerted(false);
					entities.add(generated);
					
				}
				
				spawnDelayTime = 60;
			} else if(player.getX() >= s.getX() && !s.isChecked()) {
				s.setChecked(true);
			}
		}
		
		if(spawnDelayTime > 0) {
			spawnDelayTime -= 0.1f;
			if(spawnDelayTime < 0)
				spawnDelayTime = 0;
		}
	}
	
	public void handleJoystick() {
		for(int i = 0; i < 2; i++) {
			if(ScreenManager.getCurrentScreen().inputManager.getTouches().get(i).touched) {
				touchPos.set(ScreenManager.getCurrentScreen().inputManager.getTouches().get(i).touchX, ScreenManager.getCurrentScreen().inputManager.getTouches().get(i).touchY, 0);
				ScreenManager.getCurrentScreen().camera.unproject(touchPos);
				if(touchPos.x > joystickPos.x && touchPos.x < joystickPos.x + 100 &&
						touchPos.y > joystickPos.y && touchPos.y < joystickPos.y + 100) {
					movePointer = i;
					movingJoystick = true;
				} else if(touchPos.x > jumpButtonPos.x && touchPos.x < jumpButtonPos.x + 75 &&
						touchPos.y > jumpButtonPos.y && touchPos.y < jumpButtonPos.y + 75) {
					jumpPointer = i;
					if(!player.getJumpBool()) {
						player.setJumpBool(true);
					}
					
				} else if(touchPos.x > attackButtonPos.x && touchPos.x < attackButtonPos.x + 75 &&
						touchPos.y > attackButtonPos.y && touchPos.y < attackButtonPos.y + 75) {
					if(!player.isAttacking() && player.getAttackLength() != 0 && !player.getJustAttacked()) {
							attackPointer = i;
							if(joystick.getY() < joystickPos.y - 50) {
								player.setExcecuteGP(true);
								player.setJustAttacked(true);
							} else {
								player.setJustAttacked(true);
								player.attack();
							}
					}
				}
			}
			if(movePointer != -1) { 
				if(!ScreenManager.getCurrentScreen().inputManager.getTouches().get(movePointer).touched) {
					movingJoystick = false;
					movePointer = -1;
				}
			}
			if(attackPointer != -1) {
				if(!ScreenManager.getCurrentScreen().inputManager.getTouches().get(attackPointer).touched) {
					player.setJustAttacked(false);
					attackPointer = -1;
					if(movePointer > 0)
						movePointer = 0;
				}
			}
			
			if(jumpPointer != -1) {
				if(!ScreenManager.getCurrentScreen().inputManager.getTouches().get(jumpPointer).touched) {
					player.setJumpBool(false);
					jumpPointer = -1;
					if(movePointer > 0)
						movePointer = 0;
				}
			}
		}
		
		if(!player.isStunned()) {
			if(joystick.getX() + 50 > joyStickRingX + 10) {
				player.setMoveLeft(false);
				player.setMoveRight(true);
			} else if(joystick.getX() + 50 < joyStickRingX - 10) {
				player.setMoveLeft(true);
				player.setMoveRight(false);
			} else {
				player.setMoveLeft(false);
				player.setMoveRight(false);
			}
		}
	}
	
	public void checkCollisions(float speed) {
		for(Enemy e : entities) {
			if(e.getHp() > 0) {
				if(player.isAttacking()) {
					if(e.checkCollision(player.getAttackHitbox())) {
						if(!hasStarted)
							hasStarted = true;
						showSplash = true;
						e.setStunned(true);
						e.setStunTime(3);
						if(!e.getDamageDelay()) {
							combo.setComboAmount(combo.getComboAmount() + 1);
							int amount = combo.getComboAmount() % 6;
							if(amount == 0) {
								spawnCoin();
								amount = -1;
							}
						}
						e.takeDamage(player.getAttackDamage());
						
						if(!player.getGroundPound()) {
							e.attacked(player.getHitBox(), speed);
						} else if(player.getGroundPound())
							e.stompAttacked(player, speed);
						cameraShake = true;
						cameraShakeDuration = 1f;
						combo.setComboShow(10);
						
					}
					if(cameraShake) {
						if(e.getX() > player.getX() - 450 && e.getX() < player.getX() + 550)
							e.setAlerted(true);
					}
				}

				if(player.checkCollision(e.getAttackHitbox()) && !e.isStunned() && e.isAlerted()) {
					e.setExcecuteAttack(true);
					if(!player.getDamageDelay() && e.isAttacking() && !player.isAttacking()) {
						player.takeDamage(e.getAttackDamage());
						player.setStunned(true);
						player.setStunTime(3);
						player.attacked(e.getHitBox(), speed);
						System.out.println(player.getHp());
					}
				}

				if(e.getHp() <= 0) {
					enemyKilled = true;
					e.setIsAttacking(false);
					e.setExcecuteAttack(false);
				}

				if(!player.isAttacking())
					e.setDamageDelay(false);
			}
		}
	}
	
	public void pwoAttack(float delta) {
		if(player.isPwoAttack()) {
			if(!cameraXSet) {
				pwoCameraX = player.getX() + 50;
				cameraXSet = true;
			}
			for(Enemy e : entities) {
				if(!e.isLaunched() && e.getX() > player.getX() - 350 && e.getX() < player.getX() + 450) {
					float height = (float) random.nextInt(150) + 200;
					if(height <= 50)
						height = 350;
					e.setLaunchHeight(height);
					e.setGrounded(false);
					e.setLaunched(true);
					e.setJumpHeight(0);
				} else if(e.isLaunched()) {
					if(e.getY() < e.getLaunchHeight()) {
						float vel = (e.getLaunchHeight() / 50) * 4;
						e.setY(e.getY() + vel);
					} else if(e.getY() >= e.getLaunchHeight()) {
						e.setY(e.getY() + 0.05f);
					}
				}
			}
			handlePwoInput(delta);
		} else if(!player.isPwoAttack()) {
			cameraXSet = false;
			for(Enemy e : entities) {
				if(e.isLaunched()) {
					e.setLaunchHeight(0);
					e.setLaunched(false);
					cameraShake = false;
					player.setJumpHeight(0);
				}
			}
		}
	}
	
	public void handlePwoInput(float delta) {
		for(Enemy e : entities) {
			e.setStunned(true);
			e.setStunTime(3);
			if(Gdx.input.justTouched() && ScreenManager.getCurrentScreen().inputManager.getIntersecting(e.getHitBox())) {
				if(e.getHp() > 0) {
					System.out.println(player.getY());
					player.setPwoFrameNumber(random.nextInt(3));
					playerVector.set(player.getX() + 50, player.getY() + 50);

					//X
					if(player.getX() < e.getX()) {
						player.setFacingLeft(false);
						player.setFacingRight(true);
						//Y
						if(player.getY() < e.getY()) {
							float dist = (float) random.nextInt(75);
							pwoVector.y = e.getY() + dist + 50;
						} else if(player.getY() > e.getY()) {
							float dist = (float) random.nextInt(75);
							pwoVector.y = e.getY() - dist + 50;
						}
						float dist = (float) random.nextInt(75);

						player.getStreakSprite().setRotation(0);
						pwoVector.x = e.getX() + dist + 50;
						float distance = (float) Math.sqrt((playerVector.x - pwoVector.x) * (playerVector.x - pwoVector.x) +
								(playerVector.y - pwoVector.y) * (playerVector.y - pwoVector.y));
						double angle = Math.atan2(playerVector.y - pwoVector.y, playerVector.x - pwoVector.x);
						player.getStreakSprite().setSize(distance, 100);

						player.setRotation((float) Math.toDegrees(angle) + 180);

						player.getStreakSprite().setPosition(pwoVector.x, pwoVector.y - 50);
						player.getStreakSprite().setOrigin(0, 50);
						player.getStreakSprite().setRotation((float) Math.toDegrees(angle));
					} else if(player.getX() > e.getX()) {

						player.setFacingLeft(true);
						player.setFacingRight(false);

						//Y
						if(player.getY() < e.getY()) {
							float dist = (float) random.nextInt(75);
							pwoVector.y = e.getY() + dist + 50;
						} else if(player.getY() > e.getY()) {
							float dist = (float) random.nextInt(75);
							pwoVector.y = e.getY() - dist + 50;
						}
						float dist = (float) random.nextInt(75);

						player.getStreakSprite().setRotation(0);
						pwoVector.x = e.getX() - dist + 50;
						float distance = (float) Math.sqrt((playerVector.x - pwoVector.x) * (playerVector.x - pwoVector.x) +
								(playerVector.y - pwoVector.y) * (playerVector.y - pwoVector.y));
						double angle = Math.atan2(playerVector.y - pwoVector.y, playerVector.x - pwoVector.x);
						player.getStreakSprite().setSize(distance, 100);
						player.setRotation((float) Math.toDegrees(angle));

						player.getStreakSprite().setPosition(pwoVector.x, pwoVector.y - 50);
						player.getStreakSprite().setOrigin(0, 50);
						player.getStreakSprite().setRotation((float) Math.toDegrees(angle));
					}

					player.getStreakSprite().setAlpha(1);

					player.setX(pwoVector.x - 50);
					player.setY(pwoVector.y - 50);

					combo.setComboAmount(combo.getComboAmount() + 1);
					e.takeDamage(player.getAttackDamage());

					e.attacked(player.getHitBox(), delta);
					cameraShake = true;
					cameraShakeDuration = 1f;
					combo.setComboShow(10);
				}
			}
		}
		
		if(pwoDuration < 10) {
			pwoDuration += Gdx.graphics.getDeltaTime();
			if(pwoDuration > 10)
				pwoDuration = 10;
		}

		if(pwoDuration == 10) {
			pwoDuration = 0;
			player.setPwoAttack(false);
			cameraShake = false;
			player.setJumpHeight(0);
		}
	}
	
	public Vector2 getPlayerVect() {
		return playerVector;
	}
	
	public Vector2 getPwoVect() {
		return pwoVector;
	}
	
	public boolean getCameraShakeBool() {
		return cameraShake;
	}
	
	public void setCameraShakeBool(boolean shake) {
		cameraShake = shake;
	}
	
	public float getCameraShakeDuration() {
		return cameraShakeDuration;
	}
	
	public void decreaseCameraShakeDuration() {
		if(cameraShakeDuration > 0) {
			cameraShakeDuration -= 0.1f;
			if(cameraShakeDuration < 0) {
				cameraShakeDuration = 0;
			}
		}
		
		if(cameraShakeDuration == 0) {
			cameraShake = false;
		}
	}
	
	public boolean getEnemyKilled() {
		return enemyKilled;
	}
	
	public void setEnemyKilled(boolean killed) {
		enemyKilled = killed;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Array<Enemy> getEntities() {
		return entities;
	}
	
	public void setPwoCameraX(float x) {
		this.pwoCameraX = x;
	}
	
	public float getPwoCameraX() {
		return pwoCameraX;
	}
	
	public boolean getCameraXSet() {
		return cameraXSet;
	}
	
	public boolean isBossFight() {
		return bossFight;
	}
	
	public boolean isPlayerInBossArea() {
		return playerInBossArea;
	}

}
