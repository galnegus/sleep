package com.sleep.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sleep.Sleep;

public class DefaultMonologue implements Monologue {
	private String text;
	private int x;
	private int y;
	private int wrapWidth;
	private float secondsPerCharacter;

	private String[] wordList;
	private StringBuilder currentTextWords;
	private int currentWordIndex = 0;
	private int currentSpaceIndex = -1;
	private char lastChar = '0';

	private float timer = 0;
	private int charCounter = 0;
	
	private boolean done = false;
	private boolean reallyDone = false;

	public DefaultMonologue(String text, int x, int y, int wrapWidth, float secondsPerCharacter) {
		this.text = text.trim();
		this.x = x;
		this.y = y;
		this.wrapWidth = wrapWidth;
		this.secondsPerCharacter = secondsPerCharacter;

		wordList = text.split(" +");
		currentTextWords = new StringBuilder();
		currentTextWords.append(wordList[0]);
	}

	public DefaultMonologue(String text) {
		this(text, 50, Gdx.graphics.getHeight() - 50, (int) (700), 0.05f);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWrapWidth() {
		return wrapWidth;
	}
	
	public boolean isDone() {
		return done;
	}
	
	public boolean isReallyDone() {
		return reallyDone;
	}

	public String pollText() {
		timer += Gdx.graphics.getRawDeltaTime();
		if (timer > secondsPerCharacter) {
			timer -= secondsPerCharacter;
			charCounter++;
		}
		
		if (charCounter < text.length()) {
			if (text.charAt(charCounter) == ' ' && lastChar != ' ') {
				currentWordIndex++;
				currentSpaceIndex = charCounter;
				currentTextWords.append(" ").append(wordList[currentWordIndex]);
			}
			lastChar = text.charAt(charCounter);
			return text.substring(0, charCounter);
		} else {
			done = true;
			return text;
		}
	}

	public String pollWords() {
		return currentTextWords.toString();
	}

	public String toString() {
		return text;
	}

	@Override
	public void render(BitmapFont font) {
		String text = pollText();
		String words = pollWords();
		if (text.length() > 0 && words.length() > 0
				&& font.getWrappedBounds(text, wrapWidth).height != font.getWrappedBounds(words, wrapWidth).height) {
			text = new StringBuilder(text).insert(currentSpaceIndex, "\n").toString();
		}
		font.drawWrapped(Sleep.batch, text, x, y, wrapWidth);
	}
	
	public void renderDone(BitmapFont font) {
		reallyDone = true;
	}
}
