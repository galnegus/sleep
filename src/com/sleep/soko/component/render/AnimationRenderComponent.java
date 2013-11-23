package com.sleep.soko.component.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.sleep.Sleep;
import com.sleep.soko.component.ComponentException;
import com.sleep.soko.component.Message;


public class AnimationRenderComponent extends RenderComponent {
	private Animation animation;
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
	public void render() {
		Sleep.batch.draw(animation.getKeyFrame(stateTime, true), owner.position.x, owner.position.y);
				
	}
	
	@Override
	public void update() {
		stateTime += Gdx.graphics.getRawDeltaTime();
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
