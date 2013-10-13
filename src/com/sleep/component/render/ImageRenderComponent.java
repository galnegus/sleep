package com.sleep.component.render;

import com.badlogic.gdx.graphics.Texture;
import com.sleep.Sleep;
import com.sleep.component.ComponentException;

public class ImageRenderComponent extends RenderComponent {	
	Texture image;
	public ImageRenderComponent(Texture image) {
		this.image = image;
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	public void setImage(Texture image) {
		this.image = image;
	}
	
	public Texture getImage() {
		return image;
	}
	
	@Override
	public void render() {
		Sleep.batch.draw(image, owner.position.x, owner.position.y);
	}
	
	@Override
	public void update(float delta) {
	}

	@Override
	public void init() throws ComponentException {
	}
}
