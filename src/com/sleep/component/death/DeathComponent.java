package com.sleep.component.death;

import com.sleep.Sleep;
import com.sleep.component.Component;
import com.sleep.component.ComponentException;
import com.sleep.component.movement.MovementComponent;

/**
 * when die() is used, the entity is instantly removed from the grid.
 * 
 * the entity is only removed from the entityList though, when certain critera
 * have been met.
 * 
 */
public class DeathComponent extends Component {
	private boolean alive = true;

	public void die() {
		alive = false;
		Sleep.grid.removeEntity(owner);
		MovementComponent moveComp = owner.getComponent(MovementComponent.class);
		if (moveComp != null) {
			moveComp.moveable = false;
		}
	}

	@Override
	public void update(float delta) {

		if (!alive) {
			MovementComponent moveComp = owner.getComponent(MovementComponent.class);
			if (moveComp != null) {
				if (moveComp.isMoving()) {
					return;
				}
			}
			Sleep.entityManager.remove(owner);
		}
	}

	@Override
	public void init() throws ComponentException {

	}

}
