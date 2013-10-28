package com.sleep.component.render;

import com.badlogic.gdx.graphics.Texture;
import com.sleep.Sleep;
import com.sleep.component.ComponentException;

public class BackgroundRenderComponent extends RenderComponent {
	int xSize, ySize;
	Texture image;

	public BackgroundRenderComponent(Texture image, int xSize, int ySize) {
		this.image = image;
		this.xSize = xSize;
		this.ySize = ySize;
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}

	@Override
	public void render() {
		Sleep.batch.draw(image, 0, 0, getWidth() * ySize, getHeight() * xSize, 0, 0, ySize, xSize);
	}

	@Override
	public void update() {
		// DO NOTHING
	}

	@Override
	public void init() throws ComponentException {
		// DO NOTHING
	}
}
