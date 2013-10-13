package com.sleep.component.animation;

import com.sleep.Entity;
import com.sleep.component.movement.MovementComponent;

public enum AnimationActions {
	WALK_LEFT {
		public boolean check(Entity owner) {
			MovementComponent movementComponent = owner.getComponent(MovementComponent.class);

			if (movementComponent != null && movementComponent.getDirection().x < 0)
				return true;
			else
				return false;
		}
	},
	WALK_RIGHT {
		public boolean check(Entity owner) {
			MovementComponent movementComponent = owner.getComponent(MovementComponent.class);

			if (movementComponent != null && movementComponent.getDirection().x > 0)
				return true;
			else
				return false;
		}
	};

	public abstract boolean check(Entity owner);
}
