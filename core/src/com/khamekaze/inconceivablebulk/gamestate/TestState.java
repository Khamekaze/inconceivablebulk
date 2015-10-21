package com.khamekaze.inconceivablebulk.gamestate;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.khamekaze.inconceivablebulk.entity.Player;
import com.khamekaze.inconceivablebulk.screen.ScreenManager;

public class TestState {
	
	private ShapeRenderer renderer;
	
	private Player player;
	
	public TestState() {
		player = new Player(10, 5, 0, 8);
		renderer = new ShapeRenderer();
	}
	
	public void update(float delta) {
		player.update(delta);
	}
	
	public void render(SpriteBatch sb) {
		renderer.setProjectionMatrix(ScreenManager.getCurrentScreen().camera.combined);
		renderer.begin(ShapeType.Line);
		
		renderer.setColor(Color.BLACK);
		renderer.rect(player.getHitBox().x, player.getHitBox().y, player.getHitBox().width, player.getHitBox().height);
		
		if(player.isAttacking()) {
			renderer.setColor(Color.RED);
			renderer.rect(player.getAttackHitbox().x, player.getAttackHitbox().y, player.getAttackHitbox().width, player.getAttackHitbox().height);
		}
		renderer.end();
	}
	
	public Player getPlayer() {
		return player;
	}

}
