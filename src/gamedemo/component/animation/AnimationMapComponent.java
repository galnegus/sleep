package gamedemo.component.animation;

import gamedemo.component.Component;
import gamedemo.component.ComponentException;
import gamedemo.component.render.AnimationRenderComponent;
import gamedemo.component.render.RenderComponent;

import java.util.LinkedHashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

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
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
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
}
