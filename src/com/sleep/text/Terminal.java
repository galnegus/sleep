package com.sleep.text;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sleep.Sleep;

public class Terminal {
	private static final int startPosX = 35;
	private static final int startPosY = 35;
	
	private BitmapFont font;
	private int maxInputLength;
	
	private TerminalInputProcessor inputProcessor;
	private InputReader inputReader;
	
	private static final int wrapWidth = 853;
	public int outputLogIndex;
	private List<String> outputLog;
	
	public int cursor;
	private Color cursorColor;
	private float cursorBlinkTimer;
	private final float cursorBlinkFrequency = 0.5f;

	public Terminal(InputReader inputReader) {
		font = new BitmapFont(Gdx.files.internal("fonts/Inconsolata36pxbold.fnt"));
		font.setColor(1f, 1f, 1f, 1f);
		maxInputLength = (int) ((float) wrapWidth / font.getSpaceWidth());

		inputProcessor = new TerminalInputProcessor(this);
		Gdx.input.setInputProcessor(inputProcessor);
		this.inputReader = inputReader;
		
		outputLogIndex = 0;
		outputLog = new ArrayList<String>();
		
		cursor = 0;
		cursorColor = new Color(1f, 1f, 1f, 1f);
	}

	public void update() {
		cursorBlinkTimer += Gdx.graphics.getRawDeltaTime();
		if (cursorBlinkTimer > cursorBlinkFrequency) {
			cursorBlinkTimer -= cursorBlinkFrequency;
			cursorColor.set((cursorColor.r + 1) % 2, (cursorColor.g + 1) % 2, (cursorColor.b + 1) % 2, 1f);
		}
	}
	
	public void print(String output) {
		outputLog.add(output);
	}
	
	public void sendInput(String input) {
		print(input);
		inputReader.receiveInput(input);
	}
	
	public void cursorActive() {
		cursorColor.set(1f, 1f, 1f, 1f);
		cursorBlinkTimer = 0;
	}
	
	public int outputLogSize() {
		return outputLog.size();
	}
	
	public int getMaxInputLength() {
		return maxInputLength;
	}

	public void render() {
		// render output
		int outputStartPosY = startPosY + (int) font.getLineHeight();
		for (int i = outputLog.size() - 1 - outputLogIndex; i >= 0; i--) {
			outputStartPosY += font.getWrappedBounds(outputLog.get(i), wrapWidth).height;
			
			if (outputStartPosY >= Gdx.graphics.getHeight() + font.getWrappedBounds(outputLog.get(i), wrapWidth).height) {
				break;
			}
			
			font.drawWrapped(Sleep.batch, outputLog.get(i), startPosX, outputStartPosY, wrapWidth);
		}
		
		// render current input
		font.draw(Sleep.batch, ">" + inputProcessor.currentInput.toString(), startPosX, startPosY + font.getLineHeight());
		
		// render cursor
		Color c = Sleep.batch.getColor();
		Sleep.batch.setColor(cursorColor);
		int blendSrc = Sleep.batch.getBlendSrcFunc();
		int blendDst = Sleep.batch.getBlendDstFunc();
		Sleep.batch.setBlendFunction(GL20.GL_ONE_MINUS_DST_COLOR, GL20.GL_ONE_MINUS_SRC_COLOR);
		Sleep.batch.draw(Sleep.assets.get("images/cursor.png", Texture.class), 3 + startPosX + font.getSpaceWidth()
				+ cursor * font.getSpaceWidth(), startPosY - font.getLineHeight() / 4);
		Sleep.batch.setBlendFunction(blendSrc, blendDst);
		Sleep.batch.setColor(c);
	}
}
