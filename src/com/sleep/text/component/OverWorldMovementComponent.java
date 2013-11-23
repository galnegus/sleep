package com.sleep.text.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.sleep.Constants;
import com.sleep.soko.component.Component;
import com.sleep.soko.component.ComponentException;
import com.sleep.soko.component.Message;

public class OverWorldMovementComponent extends Component {
	public float Velocity = 100f;

	protected Vector2 direction = new Vector2(0, 0);
	private Vector2 destination = new Vector2();

	private boolean moving = false;
	public boolean movable = true;

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
			if (moving) {
				destination.x += x;
				destination.y += y;
			} else {
				destination.x = owner.position.x + x;
				destination.y = owner.position.y + y;
			}
			moving = true;

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
		if (message == Message.MOVE_LEFT) {
			move(-Constants.GRID_CELL_SIZE, 0);
		} else if (message == Message.MOVE_UP) {
			move(0, Constants.GRID_CELL_SIZE);
		} else if (message == Message.MOVE_RIGHT){
			System.out.println("hej");
			move(Constants.GRID_CELL_SIZE, 0);
		} else if (message == Message.MOVE_DOWN){
			move(0, -Constants.GRID_CELL_SIZE);
		}
	}
}
