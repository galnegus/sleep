package gamedemo.component.render;

import gamedemo.Camera;
import gamedemo.component.ComponentException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;


public class BackgroundRenderComponent extends RenderComponent {
	int xSize, ySize;
	Image image;
	Camera cam;
	
	public BackgroundRenderComponent(Image image, int xSize, int ySize) {
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
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		gr.fillRect(0, 0, getWidth() * ySize, getHeight() * xSize, image, 0, 0);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		// DO NOTHING
	}

	@Override
	public void init() throws ComponentException {
		// DO NOTHING		
	}
}
