package com.sleep.component.movement;

import com.badlogic.gdx.math.Vector2;
import com.sleep.GameScreen;
import com.sleep.component.Component;
import com.sleep.component.ComponentException;

/**
 * @author thi
 *
 */
public class MovementComponent extends Component {

	protected static final float MAX_VELOCITY = 600000f;
	protected static final float GROUND_FRICTION = 1.01f;
	
	private float acceleration = calculateAcceleration(GROUND_FRICTION, MAX_VELOCITY);
	
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
	
	public void move(float x, float y) {
		destination.x = owner.position.x + x;
		destination.y = owner.position.y + y;
		if(GameScreen.level.getEntityAt(destination.x, destination.y) == null) {
			moving = true;
			GameScreen.level.moveEntityTo(destination, owner);
		} else if(GameScreen.level.getEntityAt(destination.x, destination.y).getName().equals("Box") && owner.getName().equals("Player")) {
			Vector2 pushTo = new Vector2(destination.x + x, destination.y + y);
			if(GameScreen.level.getEntityAt(pushTo) == null) {
				GameScreen.level.getEntityAt(destination.x, destination.y).getComponent(MovementComponent.class).move(x, y);
				moving = true;
				GameScreen.level.moveEntityTo(destination, owner);
			}
		}
	}
	
	private void keepMoving(float delta) {
		if(destination.x > owner.position.x && Math.abs(destination.x - owner.position.x) > 0) {
			direction.x += 1;
		} else if (destination.x < owner.position.x && Math.abs(destination.x - owner.position.x) > 0) {
			direction.x -= 1;
		} else if (destination.y < owner.position.y) {
			direction.y -= 1;
		} else if (destination.y > owner.position.y) {
			direction.y += 1;
		}
	}
	
	private void stopMoving(float delta) {
		if((direction.x == 1 && destination.x < owner.position.x) || (direction.x == -1 && destination.x > owner.position.x)) {
			moving = false;
			owner.position.x = destination.x;
			velocity.x = 0;
		} else if((direction.y == 1 && destination.y < owner.position.y) || (direction.y == -1 && destination.y > owner.position.y)) {
			moving = false;
			owner.position.y = destination.y;
			velocity.y = 0;
		}
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

	/**
	 * Should always be called first in the update method. Movement will still be functional if called last, but then
	 * other components/entities won't be able to see the owner entity's acceleration.
	 */
	protected void resetAcceleration() {
		direction.x = 0;
		direction.y = 0;
	}

	@Override
	public void update(float delta) {
		resetAcceleration();
		
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
		
		velocity.x = newVelocity(velocity.x, direction.x * acceleration, GROUND_FRICTION, delta);
		velocity.y = newVelocity(velocity.y, direction.y * acceleration, GROUND_FRICTION, delta);
		
		Vector2 position = owner.position;

		position.y = position.y + delta / 2 * (oldVelocity.y + velocity.y); //Heun's method
		position.x = position.x + delta / 2 * (oldVelocity.x + velocity.x); //Heun's method
		
		if(moving)
			stopMoving(delta);
	}

	@Override
	public void init() throws ComponentException {
		// TODO Auto-generated method stub
		
	}
}
