package com.sleep.component.render;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.sleep.Sleep;
import com.sleep.component.ComponentException;


public class AnimationRenderComponent extends RenderComponent {
	Animation animation;
	float stateTime;

	public AnimationRenderComponent(Animation animation) {
		this.animation = animation;
		this.stateTime = 0;
		// TODO Auto-generated constructor stub
	}
	
	public void setAnimation(Animation animation) {
		this.animation = animation;
		this.stateTime = 0;
	}
	
	public Animation getAnimation() {
		return animation;
	}
	
	@Override
	public int getHeight() {
		return animation.getKeyFrame(stateTime).getRegionHeight();
	}

	@Override
	public int getWidth() {
		return animation.getKeyFrame(stateTime).getRegionWidth();
	}

	@Override
	public void render(Sleep game) {
		game.batch.draw(animation.getKeyFrame(stateTime, true), owner.position.x, owner.position.y);
				
	}
	
	@Override
	public void update(float delta) {
		stateTime += delta;
	}

	@Override
	public void init() throws ComponentException {
		// DO NOTHING		
	}	

}
