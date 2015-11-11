package com.khamekaze.inconceivablebulk.combo;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.khamekaze.inconceivablebulk.MainGame;
import com.khamekaze.inconceivablebulk.screen.ScreenManager;

public class Combo {
	
	private int comboAmount = 0;
	private Sprite comboSprite, comboOne, comboTwo, comboThree, comboFour, comboFive, comboSix, comboSeven, comboEight, comboNine, comboZero;
	
	private Array<Sprite> comboNumbers;
	
	private float comboShow = 0f;
	private boolean showComboBool = false;
	
	private Random rand;
	
	private float x, y;
	
	public Combo() {
		loadTextures();
		x = MainGame.WIDTH / 2;
		y = MainGame.HEIGHT - 100;
	}
	
	public void update(float speed) {
		comboSprite.setX(x);
		comboSprite.setY(y);
		showCombo(speed);
	}
	
	public void render(SpriteBatch sb) {
		if(showComboBool) {
			comboSprite.draw(sb);
			comboUp(sb);
		}
	}
	
	public void comboUp(SpriteBatch sb) {
		if(comboAmount < 10) {
			comboNumbers.get(comboAmount - 1).setX(comboSprite.getX() + comboSprite.getWidth() + 5);
			comboNumbers.get(comboAmount - 1).setY(y);
			comboNumbers.get(comboAmount - 1).draw(sb);
		} else if(comboAmount >= 10 && comboAmount < 100) {
			System.out.println(comboAmount);
			int secondNumber = comboAmount % 10;
			
			if(secondNumber == 0) {
				
				secondNumber = 10;
			}
			int firstNumber = comboAmount / 10;
			comboNumbers.get(firstNumber - 1).setX(comboSprite.getX() + comboSprite.getWidth() + 5);
			comboNumbers.get(firstNumber - 1).setY(y);
			comboNumbers.get(firstNumber - 1).draw(sb);
			comboNumbers.get(secondNumber - 1).setX(comboSprite.getX() + comboSprite.getWidth() + 5 + 30);
			comboNumbers.get(secondNumber - 1).setY(y);
			comboNumbers.get(secondNumber - 1).draw(sb);
		} else if(comboAmount >= 100) {
			int thirdNumber = comboAmount % 10;
			int newAmount = comboAmount - thirdNumber;
			newAmount /= 10;
			int secondNumber = newAmount % 10;
			newAmount = newAmount - secondNumber;
			int firstNumber = newAmount / 10;
			if(firstNumber <= 0)
				firstNumber = 1;
			if(secondNumber == 0)
				secondNumber = 10;
			if(thirdNumber == 0)
				thirdNumber = 10;
			comboNumbers.get(firstNumber - 1).setX(comboSprite.getX() + comboSprite.getWidth() + 5);
			comboNumbers.get(firstNumber - 1).setY(y);
			comboNumbers.get(firstNumber - 1).draw(sb);
			comboNumbers.get(secondNumber - 1).setX(comboSprite.getX() + comboSprite.getWidth() + 5 + 30);
			comboNumbers.get(secondNumber - 1).setY(y);
			comboNumbers.get(secondNumber - 1).draw(sb);
			comboNumbers.get(thirdNumber - 1).setX(comboSprite.getX() + comboSprite.getWidth() + 5 + 60);
			comboNumbers.get(thirdNumber - 1).setY(y);
			comboNumbers.get(thirdNumber - 1).draw(sb);
		}
	}
	
	public void showCombo(float speed) {
		if(comboShow > 0) {
			comboShow -= 0.1f * speed;
			if(comboShow <= 0) {
				comboShow = 0;
				showComboBool = false;
				comboAmount = 0;
			}
		}
		
		if(comboShow != 0) {
			showComboBool = true;
		}
	}
	
	public void loadTextures() {
		comboSprite = new Sprite(new Texture("combo.png"));
		comboOne = new Sprite(new Texture("comboone.png"));
		comboTwo = new Sprite(new Texture("combotwo.png"));
		comboThree = new Sprite(new Texture("combothree.png"));
		comboFour = new Sprite(new Texture("combofour.png"));
		comboFive = new Sprite(new Texture("combofive.png"));
		comboSix = new Sprite(new Texture("combosix.png"));
		comboSeven = new Sprite(new Texture("comboseven.png"));
		comboEight = new Sprite(new Texture("comboeight.png"));
		comboNine = new Sprite(new Texture("combonine.png"));
		comboZero = new Sprite(new Texture("combozero.png"));
		comboNumbers = new Array<Sprite>();
		comboNumbers.add(comboOne);
		comboNumbers.add(comboTwo);
		comboNumbers.add(comboThree);
		comboNumbers.add(comboFour);
		comboNumbers.add(comboFive);
		comboNumbers.add(comboSix);
		comboNumbers.add(comboSeven);
		comboNumbers.add(comboEight);
		comboNumbers.add(comboNine);
		comboNumbers.add(comboZero);
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getX() {
		return x;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public float getY() {
		return y;
	}
	 
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getComboShow() {
		return comboShow;
	}
	
	public void setComboShow(float show) {
		this.comboShow = show;
	}
	
	public boolean isShowComboBool() {
		return showComboBool;
	}
	
	public void setShowComboBool(boolean show) {
	 this.showComboBool = show;
	}
	
	public void setComboAmount(int amount) {
		this.comboAmount = amount;
	}
	
	public int getComboAmount() {
		return comboAmount;
	}
	
}
