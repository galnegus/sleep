package com.sleep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class CoolScreenSwitcher {
	private Sleep sleep;
	private boolean switchFrom = false;
	private boolean switchTo = false;
	private CoolScreen screen;
	private float timer = 0f;
	private float freq = 0.1f;
	private int steps = 10;
	private float value = 0f;

	private final float blurRadiusMultiplier = 5f;
	private float oldBlurRadius = -1f;

	public CoolScreenSwitcher(Sleep sleep) {
		this.sleep = sleep;
	}

	public CoolScreenSwitcher(Sleep sleep, float freq, int steps) {
		this.sleep = sleep;
		this.freq = freq;
		this.steps = steps;
	}

	/**
	 * Tells the game to switch to another screen, initiating the
	 * fading/blurring process.
	 */
	public void switchScreen(CoolScreen screen) {
		this.screen = screen;
		oldBlurRadius = Sleep.blurRadius;
		switchFrom = true;
		screen.screenSwitcher.switchToScreen();
	}

	/**
	 * This method is called when this screen is switched to.
	 */
	public void switchToScreen() {
		switchTo = true;
		value = 1f;
		oldBlurRadius = Sleep.blurRadius;
	}

	/**
	 * Renders the screenSwitching effect, does nothing if switchTo and
	 * switchFrom are false.
	 */
	public void render() {
		if (switchFrom || switchTo) {
			// might be needed at some point?

			Sleep.blurRadius = oldBlurRadius + value * blurRadiusMultiplier;

			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Color c = new Color(Sleep.shapeRenderer.getColor());
			Sleep.shapeRenderer.begin(ShapeType.Filled);
			Sleep.shapeRenderer.setColor(0f, 0f, 0f, value);
			Sleep.shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			Sleep.shapeRenderer.setColor(c);
			Sleep.shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}

		if (switchFrom) {
			timer += Gdx.graphics.getRawDeltaTime();
			if (timer > freq) {
				if (value == 1f) {
					timer = 0f;
					switchFrom = false;
					sleep.setScreen(screen);
					Sleep.blurRadius = oldBlurRadius;
				}

				timer -= freq;
				value += 1f / (float) steps;
				if (value > 1)
					value = 1f;
			}
		} else if (switchTo) {
			timer += Gdx.graphics.getRawDeltaTime();
			if (timer > freq) {
				if (value == 0f) {
					timer = 0f;
					switchTo = false;
					Sleep.blurRadius = oldBlurRadius;
				}

				timer -= freq;
				value -= 1f / (float) steps;
				if (value < 0)
					value = 0f;
			}
		}
	}
}
