package com.sleep.soko.component.render;

import com.badlogic.gdx.graphics.Texture;
import com.sleep.Sleep;
import com.sleep.soko.component.ComponentException;
import com.sleep.soko.component.Message;

public class ImageRenderComponent extends RenderComponent {	
	protected Texture image;
	public ImageRenderComponent(Texture image) {
		this.image = image;
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	@Override
	public void render() {
		Sleep.batch.draw(image, owner.position.x, owner.position.y);
	}
	
	@Override
	public void update() {
	}

	@Override
	public void init() throws ComponentException {
	}

	@Override
	public void receiveMessage(Message message) {
		// TODO Auto-generated method stub
		
	}
}
