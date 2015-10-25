package com.khamekaze.inconceivablebulk.screen;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.khamekaze.inconceivablebulk.MainGame;
import com.khamekaze.inconceivablebulk.entity.Entity;
import com.khamekaze.inconceivablebulk.gamestate.TestState;

public class TestScreen extends Screen {
	
	private TestState test;
	private Random cameraShaker;
	private boolean zoomed = false;
	private float zoomLength = 6.0f;
	private float speed = 1.0f, slowMotionTime = 6.0f;
	private boolean slowMotion = false;
	private ShapeRenderer renderer;

	@Override
	public void create() {
		test = new TestState();
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
		Gdx.input.setInputProcessor(inputManager);
		cameraShaker = new Random();
		camera.update();
		System.out.println(camera.zoom);
		renderer = new ShapeRenderer();
	}

	@Override
	public void update() {
		if(test.getCameraShakeBool() && !test.getEnemyKilled()) {
			cameraShakeExecute();
		} else if(!test.getEnemyKilled()) {
			camera.position.set(test.getPlayer().getX() + 25, camera.viewportHeight/2, 0);
		} 
		
		
		
		if(test.getEnemyKilled()) {
			zoomCamera();
		}
		
		slowMotion();
		
		camera.update();
		test.update(speed);
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		test.render(sb);
		sb.end();
		renderer.setProjectionMatrix(ScreenManager.getCurrentScreen().camera.combined);
		renderer.begin(ShapeType.Line);
		
		renderer.setColor(Color.BLACK);
		renderer.rect(test.getPlayer().getHitBox().x, test.getPlayer().getHitBox().y, test.getPlayer().getHitBox().width, test.getPlayer().getHitBox().height);
		
		for(Entity e : test.getEntities()) {
			if(e.getHp() > 0)
				renderer.rect(e.getHitBox().x, e.getHitBox().y, e.getHitBox().width, e.getHitBox().height);
		}
		
		if(test.getPlayer().isAttacking()) {
			renderer.setColor(Color.RED);
			renderer.rect(test.getPlayer().getAttackHitbox().x, test.getPlayer().getAttackHitbox().y, test.getPlayer().getAttackHitbox().width, test.getPlayer().getAttackHitbox().height);
		}
		renderer.end();
		
	}

	@Override
	public void resize(int width, int height) {
		camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
	}
	
	public void cameraShakeExecute() {
		if(test.getCameraShakeBool() && test.getCameraShakeDuration() > 0) {
			int xShake = cameraShaker.nextInt(4) - 2;
			camera.position.set(test.getPlayer().getX() + xShake + 25, camera.viewportHeight / 2, 0);
			test.decreaseCameraShakeDuration();
		}
	}
	
	public void zoomCamera() {
		if(!zoomed && camera.zoom > 0.6f) {
			slowMotion = true;
			camera.position.set(test.getPlayer().getX() + 25, 240 * camera.zoom, 0);
			camera.zoom -= 0.08f;
			if(camera.zoom <= 0.6f) {
				camera.zoom = 0.6f;
				camera.position.set(test.getPlayer().getX() + 25, 240 * camera.zoom, 0);
				zoomed = true;
			}
		}
		
		if(zoomed) {
			camera.position.set(test.getPlayer().getX() + 25, 240 * camera.zoom, 0);
			if(zoomLength > 0) {
				zoomLength -= 0.1f;
				if(zoomLength < 0)
					zoomLength = 0;
			}
			if(zoomLength == 0) {
				camera.zoom += 0.08f;
				camera.position.set(test.getPlayer().getX() + 25, 240 * camera.zoom, 0);
				if(camera.zoom >= 1) {
					camera.zoom = 1;
					zoomLength = 6;
					zoomed = false;
					test.setEnemyKilled(false);
				}
			}
		}
	}
	
	public void slowMotion() {
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			if(slowMotion)
				slowMotion = false;
			else if(!slowMotion)
				slowMotion = true;
			
			System.out.println(slowMotion);
		}
		
		if(slowMotion) {
			slowMotionTime -= 0.1f;
			speed = 0.1f;
			if(slowMotionTime <= 0) {
				slowMotionTime = 0;
				slowMotion = false;
			}
		} else if(!slowMotion) {
			speed = 1.0f;
			slowMotionTime = 6.0f;
		}
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

}
