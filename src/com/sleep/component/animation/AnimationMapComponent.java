package com.sleep.component.animation;

import java.util.LinkedHashMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.sleep.Message;
import com.sleep.component.Component;
import com.sleep.component.ComponentException;
import com.sleep.component.render.AnimationRenderComponent;
import com.sleep.component.render.RenderComponent;

public class AnimationMapComponent extends Component {
	private LinkedHashMap<AnimationActions, Animation> animations;
	private Animation currentAnimation;

	private AnimationRenderComponent renderComponent;

	/**
	 * Basic konstruktor
	 * 
	 * @param animations
	 *            Contains all animation for the owner entity. The "priority" of
	 *            the animations is decided by the order of which the mappings
	 *            are inserted.
	 */
	public AnimationMapComponent(LinkedHashMap<AnimationActions, Animation> animations) {
		this.animations = animations;
	}

	public Animation getAnimation(String s) {
		return animations.get(s);
	}

	public Animation getCurrentAnimation() {
		return currentAnimation;
	}

	public void setCurrentAnimation(AnimationActions a) {
		currentAnimation = animations.get(a);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This method decides which animation is being used for the current state
	 * of the owner entity.
	 */
	@Override
	public void update() {
		for (AnimationActions action : animations.keySet()) {
			if (action.check(owner)) {
				setCurrentAnimation(action);
				break;
			}
		}

		if (currentAnimation != null && renderComponent.getAnimation() != currentAnimation) {
			renderComponent.setAnimation(currentAnimation);
		}
	}

	@Override
	public void init() throws ComponentException {
		renderComponent = getDependency(RenderComponent.class, AnimationRenderComponent.class);
	}

	@Override
	public void receiveMessage(Message message) {
		// TODO Auto-generated method stub
		
	}
}
