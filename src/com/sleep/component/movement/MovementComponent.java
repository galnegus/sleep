package com.sleep.component.movement;

import com.badlogic.gdx.math.Vector2;
import com.sleep.Constants;
import com.sleep.Entity;
import com.sleep.Sleep;
import com.sleep.component.Component;
import com.sleep.component.ComponentException;
import com.sleep.component.death.DeathComponent;

/**
 * @author thi
 * 
 */
public class MovementComponent extends Component {
	protected Vector2 direction = new Vector2(0, 0);
	private Vector2 destination = new Vector2();

	private boolean moving = false;
	public boolean moveable = true;

	public Vector2 getDirection() {
		return direction;
	}

	/**
	 * moves the entity by a given amount
	 * 
	 * this method only triggers a "movement",
	 * the movement is done when the moving boolean is set to false
	 * 
	 * @param x
	 * @param y
	 */
	public boolean move(float x, float y) {
		if(moveable) {
			destination.x = owner.position.x + x;
			destination.y = owner.position.y + y;
			Entity entityAtDest = Sleep.grid.getEntityAt(destination.x, destination.y);

			if (entityAtDest == null) {
				moving = true;
				Sleep.grid.moveEntityTo(owner, destination);
			} else if (entityAtDest.getName().equals("Box")
					&& (owner.getName().equals("Player") || owner.getName().equals("Box"))) {
				if (entityAtDest.getComponent(MovementComponent.class).move(x, y)) {
					moving = true;
					Sleep.grid.moveEntityTo(owner, destination);
				}
			} else if ((owner.getName().equals("Box") && entityAtDest.getName().equals("Ghost"))
					|| (owner.getName().equals("Ghost") && entityAtDest.getName().equals("Player"))) {
				entityAtDest.getComponent(DeathComponent.class).die(this);
				moving = true;
				Sleep.grid.moveEntityTo(owner, destination);
			}

			return moving;
		}
		return false;
	}

	private void keepMoving() {
		if (destination.x > owner.position.x && Math.abs(destination.x - owner.position.x) > 0) {
			direction.x += 1;
		} else if (destination.x < owner.position.x && Math.abs(destination.x - owner.position.x) > 0) {
			direction.x -= 1;
		} else if (destination.y < owner.position.y) {
			direction.y -= 1;
		} else if (destination.y > owner.position.y) {
			direction.y += 1;
		}
	}

	private void stopMoving() {
		if ((direction.x == 1 && destination.x < owner.position.x)
				|| (direction.x == -1 && destination.x > owner.position.x)) {
			moving = false;
			owner.position.x = destination.x;
		} else if ((direction.y == 1 && destination.y < owner.position.y)
				|| (direction.y == -1 && destination.y > owner.position.y)) {
			moving = false;
			owner.position.y = destination.y;
		}
	}
	
	public boolean isMoving() {
		return moving;
	}

	/**
	 * Should always be called first in the update method. Movement will still
	 * be functional if called last, but then
	 * other components/entities won't be able to see the owner entity's
	 * acceleration.
	 */
	private void resetAcceleration() {
		direction.x = 0;
		direction.y = 0;
	}

	@Override
	public void update(float delta) {
		resetAcceleration();

		if (moving)
			keepMoving();

		Vector2 position = owner.position;

		position.y += delta * Constants.VELOCITY * direction.y;
		position.x += delta * Constants.VELOCITY * direction.x;

		if (moving)
			stopMoving();
	}

	@Override
	public void init() throws ComponentException {
	}
}
