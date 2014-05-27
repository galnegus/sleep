package com.sleep.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sleep.Fader;
import com.sleep.Sleep;

public class FaderMonologue extends Monologue {
	private Fader fader;

	public FaderMonologue(String text, int x, int y, int wrapWidth) {
		super(text, x, y, wrapWidth);

		this.fader = new Fader(new Color(Color.BLACK), new Color(Color.WHITE));
	}

	public FaderMonologue(String text, int x, int y, int wrapWidth, float frequency, int steps) {
		super(text, x, y, wrapWidth);

		this.fader = new Fader(new Color(Color.BLACK), new Color(Color.WHITE), frequency, steps);
	}

	public FaderMonologue(String text) {
		this(text, 50, Gdx.graphics.getHeight() - 50, (int) (700));
	}

	@Override
	public void render(BitmapFont font) {
		if (!introIsDone)
			fader.render(font);
		
		font.drawWrapped(Sleep.batch, text, x, y, wrapWidth);
		
		if (!introIsDone)
			fader.renderDone(font);
		
		if (introIsDone || fader.done()) {
			introIsDone = true;
			fader.goLight();
			fader.fadeOut();
		}
	}

	@Override
	public void postRender(BitmapFont font) {
		if (!outroIsDone)
			fader.render(font);
		
		font.drawWrapped(Sleep.batch, text, x, y, wrapWidth);
		
		if (!outroIsDone)
			fader.renderDone(font);
		
		if (fader.done())
			outroIsDone = true;
	}

}
