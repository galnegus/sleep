package gamedemo.component.render;

import gamedemo.component.ComponentException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;


public class ImageRenderComponent extends RenderComponent {	
	Image image;
	public ImageRenderComponent(Image image) {
		this.image = image;
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public Image getImage() {
		return image;
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		image.draw(owner.getPosition().getX(), owner.getPosition().getY());
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		//DO SOMETHING
	}

	@Override
	public void init() throws ComponentException {
		// DO NOTHING		
	}
}
