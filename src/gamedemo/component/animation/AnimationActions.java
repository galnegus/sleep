package gamedemo.component.animation;

import gamedemo.Entity;
import gamedemo.component.movement.MovementComponent;

public enum AnimationActions {
	IDLE_RIGHT {
		public boolean check(Entity owner) {
			MovementComponent movementComponent = owner.getComponent(MovementComponent.class);

			if (movementComponent != null && (movementComponent.getVelocity().x >= 0 && movementComponent.getDirection().x == 0))
				return true;
			else
				return false;
		}
	},
	IDLE_LEFT {
		public boolean check(Entity owner) {
			MovementComponent movementComponent = owner.getComponent(MovementComponent.class);

			if (movementComponent != null && (movementComponent.getVelocity().x < 0 && movementComponent.getDirection().x == 0))
				return true;
			else
				return false;
		}
	},
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
