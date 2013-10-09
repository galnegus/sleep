package com.sleep.component.movement;

import com.badlogic.gdx.math.Vector2;
import com.sleep.Entity;
import com.sleep.GameScreen;
import com.sleep.component.Component;

/**
 * @author thi
 *
 */
public abstract class MovementComponent extends Component {

	protected static final float MAX_VELOCITY = 600000f;
	protected static final float GROUND_FRICTION = 1.01f;
	
	protected float groundAcceleration = calculateAcceleration(GROUND_FRICTION, MAX_VELOCITY);
	protected float maxVelocity = terminalVelocity(groundAcceleration, GROUND_FRICTION);
	
	protected Vector2 velocity = new Vector2();
	protected Vector2 direction = new Vector2(0, 0);
	private Vector2 destination = new Vector2();
	
	protected boolean moving = false;
	
	public Vector2 getVelocity() {
		return velocity;
	}

	public Vector2 getDirection() {
		return direction;
	}
	
	protected void moveTo(float x, float y, float delta) {
		if(GameScreen.level.getEntityAt(x, y) == null) {
			destination.x = x;
			destination.y = y;
			moving = true;
		} else if(GameScreen.level.getEntityAt(x, y).getName() == "Box") {
			Vector2 playerMovement = new Vector2(x - owner.getPosition().x, y - owner.getPosition().y);
			Entity box = GameScreen.level.getEntityAt(x, y);
			Vector2 pushTo = new Vector2(box.getPosition().x + playerMovement.x * 1, box.getPosition().y + playerMovement.y * 1);
			if(GameScreen.level.getEntityAt(pushTo) == null) {
				box.getPosition().x = pushTo.x;
				box.getPosition().y = pushTo.y;
				destination.x = x;
				destination.y = y;
				moving = true;
			}
		}
	}
	
	private void keepMoving(float delta) {
		Vector2 currentPos = owner.getPosition();
		if(destination.x > currentPos.x && Math.abs(destination.x - currentPos.x) > 0) {
			moveRight(delta);
		} else if (destination.x < currentPos.x && Math.abs(destination.x - currentPos.x) > 0) {
			moveLeft(delta);
		} else if (destination.y < currentPos.y) {
			moveDown(delta);
		} else if (destination.y > currentPos.y) {
			moveUp(delta);
		}
	}
	
	private void stopMoving(float delta) {
		if(moving) {
			Vector2 currentPos = owner.getPosition();
			if(direction.x == 1 && currentPos.x > destination.x ||
					direction.x == -1 && currentPos.x < destination.x) {
				moving = false;
				owner.getPosition().x = destination.x;
				velocity.x = 0;
			} else if(direction.y == 1 && currentPos.y > destination.y ||
					direction.y == -1 && currentPos.y < destination.y) {
				moving = false;
				owner.getPosition().y = destination.y;
				velocity.y = 0;
			}
		}
	}
	

	private void moveLeft(float delta) {
		direction.x -= 1;
	}

	private void moveRight(float delta) {
		direction.x += 1;
	}

	private void moveUp(float delta) {
		direction.y += 1;
	}

	private void moveDown(float delta) {
		direction.y -= 1;
	}
	
	/**
	 * NEWVELOCITY():
	 * v(0) = v
	 * v(1) = v(0) / F + a =
	 *      = v / F + a.
	 * v(2) = v(1) / F + a =
	 *      = (v / F + a) / F + a = 
	 *      = v / F^2 + a / F + a.
	 * v(3) = v / F^3 + a / F^2 + a / F + a.
	 * ....
	 * v(delta) = v / F^(delta) + a * sum(1/F^(k)), k = 0...delta-1.
	 * 
	 * -> geometrisk summa -> profit (snabbare kod)
	 * v(delta) = v / F^(delta) + a * (((1 / F^(delta)) - 1) / ((1 / F) - 1)), where F != 1.
	 * v(delta) = v / F^(delta) + a * delta, where F == 1.
	 */
	protected float newVelocity(float velocity, float acceleration, float friction, float delta) {
		float newVelocity = (float) (velocity / Math.pow(friction, delta));
		if (friction == 1) {
			return newVelocity + acceleration * delta;
		} else {
			return (float) (newVelocity + acceleration * ((1 / Math.pow(friction, delta)) - 1) / ((1 / friction) - 1));
		}
	}
	
	/**
	 * To get the terminal velocity for given acceleration and friction the formula for newVelocity() is used.
	 * Calculate v(infinity) to get the terminal velocity.
	 * 
	 * The function can be written:
	 * v(infinity) ~ -a / ((1 / F) - 1)
	 */
	protected float terminalVelocity(float acceleration, float friction) {
		return -acceleration / ((1 / friction) - 1);
	}
	
	/**
	 * The terminal velocity is given by
	 * v(infinity) ~ -a / ((1 / F) - 1)  (see terminalVelocity()).
	 * 
	 * This function can be rewritten to get the acceleration needed to obtain a given terminal velocity.
	 * a = -v(infinity) / F + v(infinity), 
	 * where v(infinity) is the terminal velocity.
	 */
	protected float calculateAcceleration(float friction, float terminalVelocity) {
		return -terminalVelocity / friction + terminalVelocity;
	}

	protected void updateStuff(float delta) {
		if(moving)
			keepMoving(delta);
		
		Vector2 oldVelocity = new Vector2(velocity);
		
		/*
		double diagonal = Math.sqrt((direction.x * direction.x) + (direction.y * direction.y));
		
		if(diagonal != 0) {
			direction.x /= diagonal;
			direction.y /= diagonal;
		}
		*/
		
		velocity.x = newVelocity(velocity.x, direction.x * groundAcceleration, GROUND_FRICTION, delta);
		velocity.y = newVelocity(velocity.y, direction.y * groundAcceleration, GROUND_FRICTION, delta);
		
		Vector2 position = owner.getPosition();

		position.y = position.y + delta / 2 * (oldVelocity.y + velocity.y); //Heun's method
		position.x = position.x + delta / 2 * (oldVelocity.x + velocity.x); //Heun's method
		
		if(moving)
			stopMoving(delta);
	}

	/**
	 * Should always be called first in the update method. Movement will still be functional if called last, but then
	 * other components/entities won't be able to see the owner entity's acceleration.
	 */
	protected void resetAcceleration() {
		direction.x = 0;
		direction.y = 0;
	}

	@Override
	public abstract void update(float delta);
}
