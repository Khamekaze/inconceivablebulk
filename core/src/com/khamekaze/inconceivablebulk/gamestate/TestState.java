package com.khamekaze.inconceivablebulk.gamestate;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.khamekaze.inconceivablebulk.MainGame;
import com.khamekaze.inconceivablebulk.combo.Combo;
import com.khamekaze.inconceivablebulk.entity.Enemy;
import com.khamekaze.inconceivablebulk.entity.Entity;
import com.khamekaze.inconceivablebulk.entity.Player;
import com.khamekaze.inconceivablebulk.screen.ScreenManager;

public class TestState {
	
	private ShapeRenderer renderer;
	
	private Vector2 pwoVector, playerVector;
	
	private Combo combo;
	
	private Random random = new Random();
	
	private Player player;
	private Enemy enemy, enemyTwo, enemyThree;
	private Array<Enemy> entities;
	
	private boolean cameraShake = false, enemyKilled = false, slowMotion = false, cameraXSet = false;
	private float cameraShakeDuration = 0.0f, pwoDuration = 0.0f, pwoCameraX = 0.0f;
	
	private Sprite bg;
	private Texture texture;
	
	
	public TestState() {
		player = new Player(10, 2, 0, 6);
		
		enemy = new Enemy(10, 3, 0, 1, MainGame.WIDTH / 2 + 100, MainGame.HEIGHT / 2);
		enemyTwo = new Enemy(10, 3, 0, 2, MainGame.WIDTH / 2 - 100, MainGame.HEIGHT / 2);
		enemyThree = new Enemy(10, 3, 0, 0.5f, MainGame.WIDTH / 2 - 200, MainGame.HEIGHT / 2);
		
		entities = new Array<Enemy>();
		
		entities.add(enemy);
		entities.add(enemyTwo);
		entities.add(enemyThree);
		
		renderer = new ShapeRenderer();
		texture = new Texture("testbg.png");
		bg = new Sprite(texture);
		bg.setPosition(0, 0);
		
		pwoVector = new Vector2();
		playerVector = new Vector2();
		
		combo = new Combo();
		
	}
	
	public void update(float delta, boolean slowmo) {
		player.update(delta, slowmo);
		for(Enemy e : entities) {
			e.update(delta, player.getHitBox());
		}
		
		combo.setX(ScreenManager.getCurrentScreen().camera.position.x + 50);
		combo.setY(ScreenManager.getCurrentScreen().camera.position.y + 150);
		combo.update(delta);
		
		pwoAttack(delta);
		
		checkCollisions(delta);
	}
	
	public void render(SpriteBatch sb) {
		bg.draw(sb);
		player.render(sb);
		combo.render(sb);
	}
	
	public void checkCollisions(float speed) {
		for(Enemy e : entities) {
			if(e.getHp() > 0) {
				if(player.isAttacking()) {
					if(e.checkCollision(player.getAttackHitbox())) {
						e.setStunned(true);
						e.setStunTime(3);
						if(!e.getDamageDelay())
							combo.setComboAmount(combo.getComboAmount() + 1);
						e.takeDamage(player.getAttackDamage());
						if(!player.getGroundPound()) {
							e.attacked(player.getHitBox(), speed);
						} else if(player.getGroundPound())
							e.stompAttacked(player, speed);
						cameraShake = true;
						cameraShakeDuration = 1f;
						combo.setComboShow(10);
						
					}
				}

				if(e.isAttacking())
					player.checkCollision(e.getAttackHitbox());

				if(e.getHp() <= 0) {
					enemyKilled = true;
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
				if(!e.isLaunched()) {
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
				playerVector.set(player.getX() + 50, player.getY() + 50);

				//X
				if(player.getX() < e.getX()) {
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
					
					player.getStreakSprite().setPosition(pwoVector.x, pwoVector.y - 50);
					player.getStreakSprite().setOrigin(0, 50);
					player.getStreakSprite().setRotation((float) Math.toDegrees(angle));
				} else if(player.getX() > e.getX()) {
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
					
					player.getStreakSprite().setPosition(pwoVector.x, pwoVector.y - 50);
					player.getStreakSprite().setOrigin(0, 50);
					player.getStreakSprite().setRotation((float) Math.toDegrees(angle));
				}
				
				player.getStreakSprite().setAlpha(1);
				
				player.setX(pwoVector.x - 50);
				player.setY(pwoVector.y - 50);
				
				combo.setComboAmount(combo.getComboAmount() + 1);
//				e.takeDamage(player.getAttackDamage());
				
				e.attacked(player.getHitBox(), delta);
				cameraShake = true;
				cameraShakeDuration = 1f;
				combo.setComboShow(10);
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

}
