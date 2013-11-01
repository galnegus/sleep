package com.sleep.component.shader;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.sleep.Sleep;
import com.sleep.component.Component;
import com.sleep.component.ComponentException;

public class LightShaderComponent extends Component implements ShaderComponent {
	private Texture light;
	private Color color;
	private float initSize;
	private float size;
	private boolean lightOscillate;

	// if set to false, the light will "go out"
	public boolean destroy;
	
	/*
	 * TODO:
	 * fix destroySteps so it's framerate independent
	 */
	private final float destroySteps = 16;
	private Color destroySubtract;

	public LightShaderComponent(Texture light, Color color, float size, boolean lightOscillate) {
		this.light = light;
		this.color = color;
		this.initSize = size;
		this.lightOscillate = lightOscillate;
		this.destroy = false;
		this.destroySubtract = new Color(color.r / destroySteps, color.g / destroySteps, color.b / destroySteps,
				color.a / destroySteps);
	}

	@Override
	public void drawShader() {
		Color c = Sleep.batch.getColor();
		Sleep.batch.setColor(color);
		Sleep.batch.draw(light, owner.position.x + owner.getWidth() / 2 - size / 2, owner.position.y + owner.getWidth()
				/ 2 - size / 2, size, size);
		Sleep.batch.setColor(c);
	}

	@Override
	public void update() {
		if (lightOscillate)
			size = initSize + initSize / 2 * MathUtils.random();
		else
			size = initSize;
		
		
		if (destroy) {
			color.sub(destroySubtract);
		} else {
			color.a += 0.05 * (MathUtils.random() - 0.5);
			if (color.a <= 0.2)
				color.a = 0.25f;
			else if (color.a > 0.5)
				color.a = 0.45f;
		}

		
	}

	@Override
	public void init() throws ComponentException {
		// TODO Auto-generated method stub

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

}
