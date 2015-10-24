package com.khamekaze.inconceivablebulk.gamestate;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.khamekaze.inconceivablebulk.MainGame;
import com.khamekaze.inconceivablebulk.entity.Enemy;
import com.khamekaze.inconceivablebulk.entity.Entity;
import com.khamekaze.inconceivablebulk.entity.Player;
import com.khamekaze.inconceivablebulk.screen.ScreenManager;

public class TestState {
	
	private ShapeRenderer renderer;
	
	private Player player;
	private Enemy enemy, enemyTwo, enemyThree;
	private Array<Entity> entities;
	
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
	}
	
	public void update(float delta) {
		player.update(delta);
		for(Entity e : entities) {
			e.update(delta);
		}
		
		checkCollisions();
	}
	
	public void render(SpriteBatch sb) {
		renderer.setProjectionMatrix(ScreenManager.getCurrentScreen().camera.combined);
		renderer.begin(ShapeType.Line);
		
		renderer.setColor(Color.BLACK);
		renderer.rect(player.getHitBox().x, player.getHitBox().y, player.getHitBox().width, player.getHitBox().height);
		
		for(Entity e : entities) {
			if(e.getHp() > 0)
				renderer.rect(e.getHitBox().x, e.getHitBox().y, e.getHitBox().width, e.getHitBox().height);
		}
		
		if(player.isAttacking()) {
			renderer.setColor(Color.RED);
			renderer.rect(player.getAttackHitbox().x, player.getAttackHitbox().y, player.getAttackHitbox().width, player.getAttackHitbox().height);
		}
		renderer.end();
	}
	
	public void checkCollisions() {
		for(Entity e : entities) {
			if(player.isAttacking()) {
				if(e.checkCollision(player.getAttackHitbox())) {
					e.takeDamage(player.getAttackDamage());
					e.attacked(player.getHitBox());
				}
			}
			
			if(e.isAttacking())
				player.checkCollision(e.getAttackHitbox());
			
			if(e.getHp() < 0) {
				e = null;
			}
			
			if(!player.isAttacking())
				e.setDamageDelay(false);
		}
	}
	
	public Player getPlayer() {
		return player;
	}

}
