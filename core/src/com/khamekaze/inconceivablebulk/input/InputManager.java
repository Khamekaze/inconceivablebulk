package com.khamekaze.inconceivablebulk.input;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.khamekaze.inconceivablebulk.screen.ScreenManager;

public class InputManager implements InputProcessor {
	
	private Rectangle mouseHitbox;
	private Vector3 input;
	
	private Map<Integer, TouchInfo> touches = new HashMap<Integer, TouchInfo>();
	
	public InputManager() {
		input = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		mouseHitbox = new Rectangle(input.x, input.y, 10, 10);
		
		for(int i = 0; i < 2; i++) {
			touches.put(i, new TouchInfo());
		}
	}
	
	public void update() {
		input.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		ScreenManager.getCurrentScreen().camera.unproject(input);
		mouseHitbox.setX(input.x - 5);
		mouseHitbox.setY(input.y - 5);
	}
	
	public boolean getIntersecting(Rectangle rect) {
		return mouseHitbox.overlaps(rect) || mouseHitbox.contains(rect);
	}
	
	public Rectangle getMouseHitbox() {
		return mouseHitbox;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(pointer < 2) {
			touches.get(pointer).touchX = screenX;
			touches.get(pointer).touchY = screenY;
			touches.get(pointer).touched = true;
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(pointer < 2) {
			touches.get(pointer).touchX = 0;
			touches.get(pointer).touchY = 0;
			touches.get(pointer).touched = false;
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Map<Integer, TouchInfo> getTouches() {
		return touches;
	}
}
