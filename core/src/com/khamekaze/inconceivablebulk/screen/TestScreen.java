package com.khamekaze.inconceivablebulk.screen;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.khamekaze.inconceivablebulk.MainGame;
import com.khamekaze.inconceivablebulk.gamestate.TestState;

public class TestScreen extends Screen {
	
	private TestState test;
	private Random cameraShaker;
	private boolean zoomed = false;
	private float zoomLength = 6.0f;

	@Override
	public void create() {
		test = new TestState();
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
		Gdx.input.setInputProcessor(inputManager);
		cameraShaker = new Random();
		camera.update();
		System.out.println(camera.zoom);
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
		camera.update();
		test.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		test.render(sb);
		sb.end();
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
			camera.position.set(test.getPlayer().getX() + 25, MainGame.HEIGHT / 2 + test.getPlayer().getY() + 190 * camera.zoom - camera.viewportHeight / 2, 0);
			camera.zoom -= 0.08f;
			if(camera.zoom <= 0.6f) {
				camera.zoom = 0.6f;
				camera.position.set(test.getPlayer().getX() + 25, MainGame.HEIGHT / 2 + test.getPlayer().getY() + 190 * camera.zoom - camera.viewportHeight / 2, 0);
				zoomed = true;
			}
		}
		
		if(zoomed) {
			camera.position.set(test.getPlayer().getX() + 25, MainGame.HEIGHT / 2 + test.getPlayer().getY() + 190 * camera.zoom - camera.viewportHeight / 2, 0);
			if(zoomLength > 0) {
				zoomLength -= 0.1f;
				if(zoomLength < 0)
					zoomLength = 0;
			}
			if(zoomLength == 0) {
				camera.zoom += 0.08f;
				
				if(camera.zoom >= 1) {
					camera.zoom = 1;
					zoomLength = 6;
					zoomed = false;
					test.setEnemyKilled(false);
				}
			}
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
