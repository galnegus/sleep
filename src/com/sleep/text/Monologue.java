package com.sleep.text;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public abstract class Monologue {
	protected String text;
	protected int x;
	protected int y;
	protected int wrapWidth;

	protected boolean introIsDone = false;
	protected boolean continueTriggered = false;
	protected boolean outroIsDone = false;

	public Monologue(String text, int x, int y, int wrapWidth) {
		this.text = text.trim();
		this.x = x;
		this.y = y;
		this.wrapWidth = wrapWidth;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String toString() {
		return text;
	}

	public abstract void render(BitmapFont font);

	public abstract void postRender(BitmapFont font);
}
