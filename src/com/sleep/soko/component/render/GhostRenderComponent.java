package com.sleep.soko.component.render;

import java.util.ArrayDeque;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sleep.Sleep;
import com.sleep.soko.component.ComponentException;
import com.sleep.soko.component.Message;

/**
 * This does two things:
 * I. renders a "trace" when a ghost is moving.
 * II. renders some glitchy randomized ambience things constantly
 */
public class GhostRenderComponent extends RenderComponent {
	private Texture image;
	private ArrayList<Shadow> shadows;
	private ArrayDeque<Shadow> shadowsRemoval;
	private float timePassed;

	public boolean castShadows = true;
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}

	public GhostRenderComponent(Texture image) {
		this.image = image;
		shadows = new ArrayList<Shadow>();
		shadowsRemoval = new ArrayDeque<Shadow>();
		timePassed = 0;
	}

	@Override
	public void render() {
		for (Shadow shadow : shadows) {
			shadow.render();
		}

		float xOffset = MathUtils.random() - 0.5f;
		float yOffset = MathUtils.random() - 0.5f;
		float length = (float) Math.sqrt(xOffset * xOffset + yOffset * yOffset);
		xOffset = (xOffset * MathUtils.random() * 5f) / length;
		yOffset = (yOffset * MathUtils.random() * 5f) / length;

		Sleep.batch.draw(image, owner.position.x + xOffset, owner.position.y + yOffset);

		if (castShadows) {
			if (timePassed > 0.1) {
				timePassed -= 0.1;
				shadows.add(new Shadow(owner.position.x + xOffset, owner.position.y + yOffset, this));
			}
		}

	}

	@Override
	public void update() {
		timePassed += Gdx.graphics.getRawDeltaTime();

		for (Shadow shadow : shadows) {
			shadow.update();
		}

		Shadow shadow;
		while ((shadow = shadowsRemoval.poll()) != null) {
			shadows.remove(shadow);
		}

	}

	public void removeShadow(Shadow shadow) {
		shadowsRemoval.add(shadow);
	}

	@Override
	public void init() throws ComponentException {
		// TODO Auto-generated method stub

	}

	private class Shadow {
		private Vector2 position;
		private float timeLimit;
		private float timePassed;
		private Color color;
		private GhostRenderComponent owner;

		public Shadow(float x, float y, GhostRenderComponent owner) {
			this.position = new Vector2(x, y);
			this.timeLimit = 0.3f;
			this.timePassed = 0f;
			this.color = new Color(0f, 0f, 0f, 1f);
			this.owner = owner;
		}

		public void render() {
			Color c = Sleep.batch.getColor();
			Sleep.batch.setColor(color);
			Sleep.batch.draw(image, position.x, position.y);
			Sleep.batch.setColor(c);
		}

		public void update() {
			timePassed += Gdx.graphics.getRawDeltaTime();

			if (timePassed <= timeLimit) {
				color.a = (timeLimit - timePassed) / timeLimit;
			} else {
				color.a = 0;
				owner.removeShadow(this);
			}

		}
	}

	@Override
	public void receiveMessage(Message message) {
		if (message == Message.ENTITY_DEATH) {
			castShadows = false;
		}
	}

}
