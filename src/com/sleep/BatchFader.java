package com.sleep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BatchFader extends Fader {
	private Color oldColor;

	public BatchFader(Color c, Color target, float frequency, float steps) {
		super(c, target, frequency, steps);
	}

	public BatchFader(Color c, Color target) {
		super(c, target);
	}
	
	public BatchFader(Color c) {
		super(c);
	}

	public void render(SpriteBatch batch) {
		oldColor = new Color(batch.getColor());
		batch.setColor(c.r, c.g, c.b, c.a);

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

	public void renderDone(SpriteBatch batch) {
		batch.setColor(oldColor);
	}
}
