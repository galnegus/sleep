package com.sleep.text;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.sleep.Constants;
import com.sleep.Fader;
import com.sleep.Sleep;

public class Terminal {
	private static final int startPosX = 35;
	private static final int startPosY = 35;

	private BitmapFont font;

	private TerminalInputProcessor inputProcessor;
	private InputReceiver inputReceiver;

	private static final int wrapWidth = (int) (Gdx.graphics.getWidth() * (2f / 3f));
	public int outputLogIndex;
	private List<String> outputLog;

	public int cursor;
	private Color cursorColor;
	private float cursorBlinkTimer;
	private final float cursorBlinkFrequency = 0.5f;
	
	public Fader fader;
	
	// needs to be set to false when not rendering the terminal
	public boolean isOutputtingMonologue = false;
	public boolean isInSokoDeath = false;

	public Terminal(InputReceiver inputReader, BitmapFont font) {
		this.font = font;

		inputProcessor = new TerminalInputProcessor(this);
		Sleep.inputMultiplexer.addProcessor(inputProcessor);
		this.inputReceiver = inputReader;

		outputLogIndex = 0;
		outputLog = new ArrayList<String>();

		cursor = 0;
		cursorColor = new Color(1f, 1f, 1f, 1f);
		
		fader = new Fader(new Color(Color.BLACK), new Color(Color.WHITE), Constants.FADER_FREQ, Constants.FADER_STEPS);
		
	}

	public void update() {
		cursorBlinkTimer += Gdx.graphics.getRawDeltaTime();
		if (cursorBlinkTimer > cursorBlinkFrequency) {
			cursorBlinkTimer -= cursorBlinkFrequency;
			cursorColor.set((cursorColor.r + 1) % 2, (cursorColor.g + 1) % 2, (cursorColor.b + 1) % 2, (cursorColor.a + 1) % 2);
		}
	}
	
	public boolean isActive() {
		if (!isOutputtingMonologue && !isInSokoDeath) {
			return true;
		}
		return false;
	}

	public void print(String output) {
		outputLog.add(output);
	}

	public void sendInput(String input) {
		print(input);
		inputReceiver.receiveInput(input);
	}

	public void clear() {
		outputLog.clear();
	}

	public void cursorActive() {
		cursorColor.set(1f, 1f, 1f, 1f);
		cursorBlinkTimer = 0;
	}

	public int outputLogSize() {
		return outputLog.size();
	}

	public int getMaxInputLength() {
		return (int) ((float) wrapWidth / font.getSpaceWidth());
	}

	public void render() {
		fader.render(Sleep.batch, font);
		
		// render output
		int outputStartPosY = startPosY + (int) font.getLineHeight();
		TextBounds wrappedBounds;
		for (int i = outputLog.size() - 1 - outputLogIndex; i >= 0; i--) {
			wrappedBounds = font.getWrappedBounds(outputLog.get(i), wrapWidth);
			outputStartPosY += wrappedBounds.height + font.getWrappedBounds(" ", wrapWidth).height / 4;

			if (outputStartPosY >= Gdx.graphics.getHeight() + wrappedBounds.height) {
				break;
			}

			font.drawWrapped(Sleep.batch, outputLog.get(i), startPosX, outputStartPosY, wrapWidth);
		}

		// render current input
		font.draw(Sleep.batch, ">" + inputProcessor.currentInput.toString(), startPosX,
				startPosY + font.getLineHeight());

		// render cursor
		Color c = new Color(Sleep.batch.getColor());
		Sleep.batch.setColor(cursorColor.r * c.r, cursorColor.g * c.g, cursorColor.b * c.b, cursorColor.a * c.a);
//		int blendSrc = Sleep.batch.getBlendSrcFunc();
//		int blendDst = Sleep.batch.getBlendDstFunc();
//		Sleep.batch.setBlendFunction(GL20.GL_ONE_MINUS_DST_COLOR, GL20.GL_ONE_MINUS_SRC_COLOR);
		Sleep.batch.draw(Sleep.assets.get("images/cursor2.png", Texture.class), 3 + startPosX + font.getSpaceWidth()
				+ cursor * font.getSpaceWidth(), startPosY - font.getLineHeight() / 4);
//		Sleep.batch.setBlendFunction(blendSrc, blendDst);
		Sleep.batch.setColor(c);
		
		fader.renderDone(Sleep.batch, font);
	}
}
