package com.sleep.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sleep.Sleep;

public class TyperMonologue extends Monologue {
	private float secondsPerCharacter;

	private String[] wordList;
	private StringBuilder currentTextWords;
	private int currentWordIndex = 0;
	private int currentSpaceIndex = -1;
	private char lastChar = '0';

	private float timer = 0;
	private int charCounter = 0;

	public TyperMonologue(String text, int x, int y, int wrapWidth, float secondsPerCharacter) {
		super(text, x, y, wrapWidth);
		this.secondsPerCharacter = secondsPerCharacter;

		wordList = text.split(" +");
		currentTextWords = new StringBuilder();
		currentTextWords.append(wordList[0]);
	}

	public TyperMonologue(String text) {
		this(text, 50, Gdx.graphics.getHeight() - 50, (int) (700), 0.05f);
	}

	private String pollText() {
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
			renderIsDone = true;
			if (Gdx.input.isKeyPressed(Keys.ENTER))
				continueTrigger = true;
			return text;
		}
	}

	private String pollWords() {
		return currentTextWords.toString();
	}

	@Override
	public void render(BitmapFont font) {
		String text = pollText();
		String words = pollWords();
		
		// fixes newline bug when whole words arn't yet printed
		if (text.length() > 0 && words.length() > 0
				&& font.getWrappedBounds(text, wrapWidth).height != font.getWrappedBounds(words, wrapWidth).height) {
			text = new StringBuilder(text).insert(currentSpaceIndex, "\n").toString();
		}
		font.drawWrapped(Sleep.batch, text, x, y, wrapWidth);
	}
	
	@Override
	public void postRender(BitmapFont font) {
		postRenderIsDone = true;
	}
}
