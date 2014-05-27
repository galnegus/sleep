package com.sleep.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sleep.Sleep;

public class TyperMonologue extends Monologue {
	private float secondsPerCharacter;

	private String[] wordList;
	private StringBuilder currentWords;
	private int currentWordIndex = 0;
	private int currentSpaceIndex = -1;
	private char lastChar = '0';

	private float timer = 0;
	private int charCounter = 0;

	public TyperMonologue(String text, int x, int y, int wrapWidth, float secondsPerCharacter) {
		super(text, x, y, wrapWidth);
		this.secondsPerCharacter = secondsPerCharacter;

		wordList = text.split(" +");
		currentWords = new StringBuilder();
		currentWords.append(wordList[0]);
	}

	public TyperMonologue(String text) {
		this(text, 50, Gdx.graphics.getHeight() - 50, (int) (700), 0.1f);
	}

	private String pollText() {
		timer += Gdx.graphics.getRawDeltaTime();
		if (timer > secondsPerCharacter) {
			timer -= secondsPerCharacter;
			charCounter++;
		}
		
		if (!introIsDone && charCounter < text.length()) {
			// determine what word is currently being printed.
			if (text.charAt(charCounter) == ' ' && lastChar != ' ') {
				currentWordIndex++;
				currentSpaceIndex = charCounter;
				currentWords.append(" ").append(wordList[currentWordIndex]);
			}
			
			// remember last char printed (needed for determining next potential word)
			lastChar = text.charAt(charCounter);
			return text.substring(0, charCounter);
		} else {
			introIsDone = true;
			return text;
		}
	}

	private String pollWords() {
		return currentWords.toString();
	}

	@Override
	public void renderIntro(BitmapFont font) {
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
	public void renderOutro(BitmapFont font) {
		outroIsDone = true;
	}
}
