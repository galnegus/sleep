package com.sleep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontFader extends Fader {
	private Color oldColor;

	public FontFader(Color c, Color target, float frequency, float steps) {
		super(c, target, frequency, steps);
	}

	public FontFader(Color c, Color target) {
		super(c, target);
	}
	
	public FontFader(Color c) {
		super(c);
	}

	public void render(BitmapFont font) {
		oldColor = new Color(font.getColor());
		font.setColor(c.r, c.g, c.b, c.a);
		

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

	public void renderDone(BitmapFont font) {
		font.setColor(oldColor);
	}
}
