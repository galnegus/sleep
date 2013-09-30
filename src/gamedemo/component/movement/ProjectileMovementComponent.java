package gamedemo.component.movement;

import gamedemo.component.ComponentException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;


public class ProjectileMovementComponent extends MovementComponent {
	public ProjectileMovementComponent(Vector2f velocity) {
		if(velocity.x >= 0) {
			direction.x = 1;
		} else {
			direction.x = -1;
		}
		
		this.velocity.x = maxVelocity * direction.x;
	}

	
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		updateStuff(delta);
	}

	@Override
	public void init() throws ComponentException {
		// DO NOTHING
	}
}
