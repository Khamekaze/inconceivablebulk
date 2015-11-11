package com.khamekaze.inconceivablebulk.screen;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.khamekaze.inconceivablebulk.MainGame;
import com.khamekaze.inconceivablebulk.entity.Entity;
import com.khamekaze.inconceivablebulk.gamestate.TestState;

public class TestScreen extends Screen {
	
	private TestState test;
	private Random cameraShaker;
	private boolean zoomed = false, cameraRecentered = true;
	private float zoomLength = 6.0f;
	private float speed = 1.0f, slowMotionTime = 6.0f;
	private boolean slowMotion = false;
	private ShapeRenderer renderer;
	private Vector2 pwoCameraX = new Vector2();
	private float yPos = 240;

	@Override
	public void create() {
		test = new TestState();
		camera.position.set(camera.viewportWidth/2, yPos, 0);
		Gdx.input.setInputProcessor(inputManager);
		cameraShaker = new Random();
		camera.update();
		renderer = new ShapeRenderer();
	}

	@Override
	public void update() {
		if(!test.isPlayerInBossArea()) {
			if(test.getCameraShakeBool() && !test.getEnemyKilled()) {
				if(!test.getCameraXSet()) {
					cameraShakeExecute();
				} else if(test.getCameraXSet()) {
					pwoCameraShakeExecute();
				}
			} else if(!test.getEnemyKilled() && !test.getCameraXSet() && cameraRecentered) {
				camera.position.set(test.getPlayer().getX() + 50, yPos, 0);
			}  else if(!test.getEnemyKilled() && test.getCameraXSet()) {
				cameraRecentered = false;
				camera.position.set(test.getPwoCameraX(), yPos, 0);
			} else if(!cameraRecentered) {
				smoothResetCamera();
			}

			pwoCameraPan();

			if(test.getEnemyKilled()) {
				zoomCamera();
			}
		} else if(test.isBossFight() && test.isPlayerInBossArea()) {
			bossFightCamera();
		}
		
		slowMotion();
		
		inputManager.update();
		camera.update();
		test.update(speed, slowMotion);
	}

	@Override
	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		test.render(sb);
		sb.end();
		renderer.setProjectionMatrix(camera.combined);
		renderer.begin(ShapeType.Line);
		
		renderer.setColor(Color.BLACK);
		
		for(Entity e : test.getEntities()) {
			if(e.isAttacking())
				renderer.rect(e.getAttackHitbox().x, e.getAttackHitbox().y, e.getAttackHitbox().width, e.getAttackHitbox().height);
		}
		
		renderer.end();
		
	}

	@Override
	public void resize(int width, int height) {
		camera.translate(camera.viewportWidth / 2, yPos, 0);
		viewPort.update(width, height);
	}
	
	public void cameraShakeExecute() {
		if(test.getCameraShakeBool() && test.getCameraShakeDuration() > 0) {
			int xShake = cameraShaker.nextInt(4) - 2;
			camera.position.set(test.getPlayer().getX() + xShake + 50, yPos, 0);
			test.decreaseCameraShakeDuration();
		}
	}
	
	public void pwoCameraShakeExecute() {
		if(test.getCameraShakeBool() && test.getCameraShakeDuration() > 0) {
			int xShake = cameraShaker.nextInt(4) - 2;
			camera.position.set(test.getPwoCameraX() + xShake, yPos, 0);
			test.decreaseCameraShakeDuration();
		}
	}
	
	public void pwoCameraPan() {
		if(test.getCameraXSet()) {
			if(camera.position.y < 340) {
				yPos += 10;
				System.out.println("PANNING");
				if(camera.position.y >= 340) {
					yPos = 340;
					camera.position.y = yPos;
				}
			}
		} else if(!test.getCameraXSet()) {
			if(camera.position.y > 240) {
				yPos -= 10;
				camera.position.y = yPos;
				if(camera.position.y <= 240) {
					yPos = 240;
					camera.position.y = yPos;
				}
			}
		}
	}
	
	public void zoomCamera() {
		if(slowMotionTime == 6.0f) {
			slowMotion = true;
			slowMotionTime -= 0.1f;
		} else if(slowMotionTime < 6.0f) {
			slowMotionTime -= 0.1f;
			if(slowMotionTime <= 0) {
				slowMotionTime = 0;
				slowMotion = false;
				test.setEnemyKilled(false);
			}
		}
	}
	
	public void smoothResetCamera() {
		if(camera.position.x != test.getPlayer().getX() + 50) {
			if(camera.position.x > test.getPlayer().getX() + 50) {
				camera.position.x -= 5;
				if(camera.position.x < test.getPlayer().getX() + 50) {
					camera.position.x = test.getPlayer().getX() + 50;
					cameraRecentered = true;
				}
			} else if(camera.position.x < test.getPlayer().getX() + 50) {
				camera.position.x += 5;
				if(camera.position.x > test.getPlayer().getX() + 50) {
					camera.position.x = test.getPlayer().getX() + 50;
					cameraRecentered = true;
				}
			}
		}
	}
	
	public void bossFightCamera() {
		if(camera.position.x < test.levelWidth - 200) {
			camera.position.x += 4;
			if(camera.position.x > test.levelWidth - 200)
				camera.position.x = test.levelWidth - 200;
		} else if(camera.position.x > test.levelWidth - 200) {
			camera.position.x -= 4;
			if(camera.position.x < test.levelWidth - 200) {
				camera.position.x = test.levelWidth - 200;
			}
		}
	}
	
	public void slowMotion() {
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			if(slowMotion)
				slowMotion = false;
			else if(!slowMotion)
				slowMotion = true;
		}
		
		if(slowMotion) {
			slowMotionTime -= 0.1f;
			speed = 0.01f;
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
