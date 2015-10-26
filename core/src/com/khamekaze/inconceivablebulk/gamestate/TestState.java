package com.khamekaze.inconceivablebulk.gamestate;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.khamekaze.inconceivablebulk.MainGame;
import com.khamekaze.inconceivablebulk.combo.Combo;
import com.khamekaze.inconceivablebulk.entity.Enemy;
import com.khamekaze.inconceivablebulk.entity.Entity;
import com.khamekaze.inconceivablebulk.entity.Player;
import com.khamekaze.inconceivablebulk.screen.ScreenManager;

public class TestState {
	
	private ShapeRenderer renderer;
	
	private Combo combo;
	
	private Player player;
	private Enemy enemy, enemyTwo, enemyThree;
	private Array<Entity> entities;
	
	private boolean cameraShake = false, enemyKilled = false, slowMotion = false;
	private float cameraShakeDuration = 0.0f;
	
	private Sprite bg;
	private Texture texture;
	
	
	public TestState() {
		player = new Player(10, 2, 0, 6);
		
		enemy = new Enemy(10, 3, 0, 4, MainGame.WIDTH / 2 + 100, MainGame.HEIGHT / 2);
		enemyTwo = new Enemy(10, 3, 0, 4, MainGame.WIDTH / 2 - 100, MainGame.HEIGHT / 2);
		enemyThree = new Enemy(10, 3, 0, 4, MainGame.WIDTH / 2 - 200, MainGame.HEIGHT / 2);
		
		entities = new Array<Entity>();
		
		entities.add(enemy);
		entities.add(enemyTwo);
		entities.add(enemyThree);
		
		renderer = new ShapeRenderer();
		texture = new Texture("testbg.png");
		bg = new Sprite(texture);
		bg.setPosition(0, 0);
		
		combo = new Combo();
		
	}
	
	public void update(float delta, boolean slowmo) {
		player.update(delta, slowmo);
		for(Entity e : entities) {
			e.update(delta);
		}
		
		if(player.getIsMoving()) {
			bg.translateX(player.getMovementSpeed());
		}
		
		combo.setX(ScreenManager.getCurrentScreen().camera.position.x + 100);
		combo.update(delta);
		
		checkCollisions();
	}
	
	public void render(SpriteBatch sb) {
		bg.draw(sb);
		combo.render(sb);
	}
	
	public void checkCollisions() {
		for(Entity e : entities) {
			if(e.getHp() > 0) {
				if(player.isAttacking()) {
					if(e.checkCollision(player.getAttackHitbox())) {
						if(!e.getDamageDelay())
							combo.setComboAmount(combo.getComboAmount() + 1);
						e.takeDamage(player.getAttackDamage());
						e.attacked(player.getHitBox());
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
	
	public Array<Entity> getEntities() {
		return entities;
	}

}
