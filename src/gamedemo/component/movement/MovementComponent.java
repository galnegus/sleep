package gamedemo.component.movement;

import gamedemo.Constants;
import gamedemo.Demo;
import gamedemo.Entity;
import gamedemo.component.Component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * @author thi
 *
 */
public abstract class MovementComponent extends Component {

	protected static final float MAX_VELOCITY = 0.6f;
	protected static final float GROUND_FRICTION = 1.01f;
	
	protected float groundAcceleration = calculateAcceleration(GROUND_FRICTION, MAX_VELOCITY);
	protected float maxVelocity = terminalVelocity(groundAcceleration, GROUND_FRICTION);
	
	protected Vector2f velocity = new Vector2f();
	protected Vector2f direction = new Vector2f(0, 0);
	private Vector2f destination = new Vector2f();
	
	protected boolean moving = false;
	
	public Vector2f getVelocity() {
		return velocity;
	}

	public Vector2f getDirection() {
		return direction;
	}
	
	protected void moveTo(float x, float y, int delta) {
		if(Demo.level.getEntityAt(x, y) == null) {
			destination.x = x;
			destination.y = y;
			moving = true;
		} else if(Demo.level.getEntityAt(x, y).getName() == "Box") {
			Vector2f playerMovement = new Vector2f(x - owner.getPosition().x, y - owner.getPosition().y);
			Entity box = Demo.level.getEntityAt(x, y);
			Vector2f pushTo = new Vector2f(box.getPosition().x + playerMovement.x * 1, box.getPosition().y + playerMovement.y * 1);
			if(Demo.level.getEntityAt(pushTo) == null) {
				box.getPosition().x = pushTo.x;
				box.getPosition().y = pushTo.y;
				destination.x = x;
				destination.y = y;
				moving = true;
			}
		}
	}
	
	private void keepMoving(int delta) {
		Vector2f currentPos = owner.getPosition();
		if(destination.x > currentPos.x && Math.abs(destination.x - currentPos.x) > 0) {
			moveRight(delta);
		} else if (destination.x < currentPos.x && Math.abs(destination.x - currentPos.x) > 0) {
			moveLeft(delta);
		} else if (destination.y < currentPos.y) {
			moveUp(delta);
		} else if (destination.y > currentPos.y) {
			moveDown(delta);
		}
	}
	
	private void stopMoving(int delta) {
		if(moving) {
			Vector2f currentPos = owner.getPosition();
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
	

	private void moveLeft(int delta) {
		direction.x -= 1;
	}

	private void moveRight(int delta) {
		direction.x += 1;
	}

	private void moveUp(int delta) {
		direction.y -= 1;
	}

	private void moveDown(int delta) {
		direction.y += 1;
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
	protected float newVelocity(float velocity, float acceleration, float friction, int delta) {
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

	protected void updateStuff(int delta) {
		if(moving)
			keepMoving(delta);
		
		Vector2f oldVelocity = new Vector2f(velocity);
		
		/*
		double diagonal = Math.sqrt((direction.x * direction.x) + (direction.y * direction.y));
		
		if(diagonal != 0) {
			direction.x /= diagonal;
			direction.y /= diagonal;
		}
		*/
		
		velocity.x = newVelocity(velocity.x, direction.x * groundAcceleration, GROUND_FRICTION, delta);
		velocity.y = newVelocity(velocity.y, direction.y * groundAcceleration, GROUND_FRICTION, delta);
		
		

		
		Vector2f position = owner.getPosition();

		position.y = position.y + (float) delta / 2 * (oldVelocity.y + velocity.y); //Heun's method
		position.x = position.x + (float) delta / 2 * (oldVelocity.x + velocity.x); //Heun's method
		
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
	public abstract void update(GameContainer gc, StateBasedGame sb, int delta);
}
