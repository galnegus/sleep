package com.sleep.soko.component.render;

import com.badlogic.gdx.graphics.Texture;
import com.sleep.Sleep;
import com.sleep.soko.component.ComponentException;
import com.sleep.soko.component.Message;

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
		Sleep.batch.draw(image, 0, 0, getWidth() * xSize, getHeight() * ySize, 0, 0, xSize, ySize);
	}

	@Override
	public void update() {
		// DO NOTHING
	}

	@Override
	public void init() throws ComponentException {
		// DO NOTHING
	}

	@Override
	public void receiveMessage(Message message) {
		// TODO Auto-generated method stub
		
	}
}
