package com.sleep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * This class will create an effect which transitions any spriteBatch/BitmapFont
 * drawing done between Fader.render() and Fader.renderDone() from one given
 * color to another given color.
 */
public class Fader {
	private Color c;
	private Color target;
	private float frequency;
	private float steps;

	private float incrementR;
	private float incrementG;
	private float incrementB;
	private float incrementA;

	private float timer = 0;

	private Color oldColor;

	public Fader(Color c, Color target) {
		this(c, target, Constants.FADER_FREQ, Constants.FADER_STEPS);
	}

	public Fader(Color c, Color target, float frequency, float steps) {
		this.c = new Color(c);
		this.target = new Color(target);
		this.frequency = frequency;
		this.steps = steps;
		calcIncrements();
	}

	private void calcIncrements() {
		incrementR = (target.r - c.r) / steps;
		incrementG = (target.g - c.g) / steps;
		incrementB = (target.b - c.b) / steps;
		incrementA = (target.a - c.a) / steps;
	}

	protected float colorSum(Color c) {
		return c.r + c.g + c.b + c.a;
	}

	protected float incrementSum() {
		return incrementR + incrementG + incrementB + incrementA;
	}

	public void render(SpriteBatch batch) {
		render(batch, null);
	}

	public void render(BitmapFont font) {
		render(null, font);
	}

	public void render(SpriteBatch batch, BitmapFont font) {
		if (!done()) {
			timer += Gdx.graphics.getRawDeltaTime();
			if (timer > frequency) {
				timer -= frequency;
				if (Math.abs(colorSum(c) - colorSum(target)) > Math.abs(incrementSum())) {
					c.r += incrementR;
					c.g += incrementG;
					c.b += incrementB;
					c.a += incrementA;
				} else if (Math.abs(colorSum(c) - colorSum(target)) <= Math.abs(incrementSum())) {
					c.r = target.r;
					c.g = target.g;
					c.b = target.b;
					c.a = target.a;
				}
			}
		}
		if (batch != null) {
			oldColor = new Color(batch.getColor());
			batch.setColor(c.r, c.g, c.b, c.a);
		}

		if (font != null) {
			oldColor = new Color(font.getColor());
			font.setColor(c.r, c.g, c.b, c.a);
		}
	}

	public void renderDone(SpriteBatch batch) {
		renderDone(batch, null);
	}

	public void renderDone(BitmapFont font) {
		renderDone(null, font);
	}

	public void renderDone(SpriteBatch batch, BitmapFont font) {
		if (batch != null)
			batch.setColor(oldColor);
		if (font != null)
			font.setColor(oldColor);
	}

	public void fadeIn() {
		target.set(Color.WHITE);
		calcIncrements();
	}

	public void fadeOut() {
		target.set(Color.BLACK);
		calcIncrements();
	}

	public void fadeTo(Color to) {
		target.set(to);
		calcIncrements();
	}

	public void goDark() {
		c.set(Color.BLACK);
		target.set(Color.BLACK);
		calcIncrements();
	}

	public void goLight() {
		c.set(Color.WHITE);
		target.set(Color.WHITE);
		calcIncrements();
	}

	public void goTo(Color to) {
		c.set(to);
		target.set(to);
		calcIncrements();
	}

	public boolean done() {
		// System.out.println("color: " + c.toString() + ", target: " +
		// target.toString());
		return c.equals(target);
	}
	
	@Override
	public String toString() {
		return "Color = " + c.toString();
	}
}
