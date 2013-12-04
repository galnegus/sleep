package com.sleep.soko.component.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.sleep.Entity;
import com.sleep.soko.Level;
import com.sleep.soko.component.Component;
import com.sleep.soko.component.ComponentException;
import com.sleep.soko.component.Message;
import com.sleep.soko.component.death.DeathComponent;

/**
 * @author thi
 * 
 */
public class MovementComponent extends Component {
	protected Level level;
	
	public float Velocity = 600f;
	
	protected Vector2 direction = new Vector2(0, 0);
	private Vector2 destination = new Vector2();

	private boolean moving = false;
	public boolean movable = true;
	
	public MovementComponent(Level level) {
		this.level = level;
	}

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
		if (movable) {
			destination.x = owner.position.x + x;
			destination.y = owner.position.y + y;
			Entity entityAtDest = level.getEntityAt(destination.x, destination.y);

			if (entityAtDest == null) {
				moving = true;
				level.moveEntityTo(owner, destination.x, destination.y);

				// push box
			} else if (entityAtDest.getName().equals("Box")
					&& (owner.getName().equals("Player") || owner.getName().equals("Box"))) {

				// recursively check that several boxes can be moved
				if (entityAtDest.getComponent(MovementComponent.class).move(x, y)) {
					moving = true;
					level.moveEntityTo(owner, destination.x, destination.y);
				}

				// no cannibalism!
			} else if (entityAtDest.getComponent(DeathComponent.class) != null && !owner.getName().equals("Player")
					&& (entityAtDest.getName().equals("Player") || owner.getName().equals("Box"))) {
				entityAtDest.getComponent(DeathComponent.class).die(this);
				moving = true;
				level.moveEntityTo(owner, destination.x, destination.y);
			}

			return moving;
		}
		return false;
	}

	private void keepMoving() {
		if (destination.x > owner.position.x && Math.abs(destination.x - owner.position.x) > 0) {
			direction.x = 1;
		} else if (destination.x < owner.position.x && Math.abs(destination.x - owner.position.x) > 0) {
			direction.x = -1;
		}

		if (destination.y < owner.position.y) {
			direction.y = -1;
		} else if (destination.y > owner.position.y) {
			direction.y = 1;
		}
	}

	private void stopMoving() {
		if ((direction.x == 1 && destination.x < owner.position.x)
				|| (direction.x == -1 && destination.x > owner.position.x)) {
			owner.position.x = destination.x;
		}

		if ((direction.y == 1 && destination.y < owner.position.y)
				|| (direction.y == -1 && destination.y > owner.position.y)) {
			owner.position.y = destination.y;
		}

		if (owner.position.x == destination.x && owner.position.y == destination.y) {
			moving = false;
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
	public void update() {
		resetAcceleration();

		if (moving)
			keepMoving();

		Vector2 position = owner.position;

		position.y += Gdx.graphics.getDeltaTime() * Velocity * direction.y;
		position.x += Gdx.graphics.getDeltaTime() * Velocity * direction.x;

		if (moving)
			stopMoving();
	}

	@Override
	public void init() throws ComponentException {
	}

	@Override
	public void receiveMessage(Message message) {
		if (message == Message.ENTITY_DEATH) {
			movable = false;
		}
	}
}
