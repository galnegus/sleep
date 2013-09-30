package gamedemo.component.render;

import gamedemo.component.ComponentException;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;


public class AnimationRenderComponent extends RenderComponent {
	Animation animation;

	public AnimationRenderComponent(Animation animation) {
		this.animation = animation;
		// TODO Auto-generated constructor stub
	}
	
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}
	
	public Animation getAnimation() {
		return animation;
	}
	
	@Override
	public int getHeight() {
		return animation.getHeight();
	}

	@Override
	public int getWidth() {
		return animation.getWidth();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		animation.draw(owner.getPosition().getX(), owner.getPosition().getY());
				
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		// TODO Auto-generated method stub
	}

	@Override
	public void init() throws ComponentException {
		// DO NOTHING		
	}	

}
