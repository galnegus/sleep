package com.sleep.component.animation;

import com.sleep.Entity;
import com.sleep.component.movement.MovementComponent;

public enum AnimationActions {
	WALK_LEFT ("left") {
		public boolean check(Entity owner) {
			MovementComponent movementComponent = owner.getComponent(MovementComponent.class);

			if (movementComponent != null && movementComponent.getDirection().x < 0)
				return true;
			else
				return false;
		}
	},
	WALK_RIGHT ("right") {
		public boolean check(Entity owner) {
			MovementComponent movementComponent = owner.getComponent(MovementComponent.class);

			if (movementComponent != null && movementComponent.getDirection().x > 0)
				return true;
			else
				return false;
		}
	};
	
	public String name;
	
	AnimationActions(String name) {
		this.name = name;
	}

	public abstract boolean check(Entity owner);
}
