package com.khamekaze.inconceivablebulk.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.khamekaze.inconceivablebulk.gamestate.TestState;

public class TestScreen extends Screen {
	
	private TestState test;

	@Override
	public void create() {
		test = new TestState();
		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
		Gdx.input.setInputProcessor(inputManager);
	}

	@Override
	public void update() {
		camera.position.set(test.getPlayer().getX(), camera.viewportHeight/2, 0);
//		camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
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
