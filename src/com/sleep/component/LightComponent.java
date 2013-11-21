package com.sleep.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.sleep.Message;
import com.sleep.Sleep;

public class LightComponent extends Component {
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

	public LightComponent(Texture light, Color color, float size, boolean lightOscillate) {
		this.light = light;
		this.color = color;
		this.initSize = size;
		this.lightOscillate = lightOscillate;
		this.destroy = false;
		this.destroySubtract = new Color(color.r / destroySteps, color.g / destroySteps, color.b / destroySteps,
				color.a / destroySteps);
	}

	public void drawLight() {
		Color c = Sleep.batch.getColor();
		Sleep.batch.setColor(color);
		Sleep.batch.draw(light, owner.position.x + owner.getWidth() / 2 - size / 2, owner.position.y + owner.getWidth()
				/ 2 - size / 2, size, size);
		Sleep.batch.setColor(c);
	}
	
	public void bindLight(int i) {
		light.bind(i);
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
			else if (color.a > 0.45)
				color.a = 0.4f;
		}

		
	}

	@Override
	public void init() throws ComponentException {
		// TODO Auto-generated method stub

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveMessage(Message message) {
		if (message == Message.ENTITY_DEATH) {
			destroy = true;
		}
	}
}
