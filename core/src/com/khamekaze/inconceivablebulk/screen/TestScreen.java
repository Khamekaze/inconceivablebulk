package com.khamekaze.inconceivablebulk.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.khamekaze.inconceivablebulk.gamestate.TestState;

public class TestScreen extends Screen {
	
	private TestState test;

	@Override
	public void create() {
		test = new TestState();
	}

	@Override
	public void update() {
		test.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void render(SpriteBatch sb) {
		test.render(sb);
	}

	@Override
	public void resize(int width, int height) {
		
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
