package com.sleep.component.death;

import com.sleep.Sleep;
import com.sleep.component.Component;
import com.sleep.component.ComponentException;
import com.sleep.component.movement.MovementComponent;
import com.sleep.component.shader.LightShaderComponent;

/**
 * when die() is used, the entity is instantly removed from the grid.
 * 
 * the entity is only removed from the entityList though, when certain critera
 * have been met.
 * 
 */
public class DeathComponent extends Component {
	private boolean alive;
	private MovementComponent killedBy;
	
	public DeathComponent() {
		this.alive = true;
		killedBy = null;
	}

	public void die() {
		alive = false;
		Sleep.grid.removeEntity(owner);
		MovementComponent moveComp = owner.getComponent(MovementComponent.class);
		if (moveComp != null) {
			
			moveComp.moveable = false;
		}
		LightShaderComponent lightComp = owner.getComponent(LightShaderComponent.class);
		if (lightComp != null) {
			
			lightComp.destroy = true;
		}
	}
	
	public void die(MovementComponent killedBy) {
		die();
		
		this.killedBy = killedBy;
	}

	@Override
	public void update() {

		if (!alive) {
			MovementComponent moveComp = owner.getComponent(MovementComponent.class);
			if (moveComp != null) {
				if (moveComp.isMoving()) {
					return;
				}
			}
			if (killedBy != null) {
				if (killedBy.isMoving()) {
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
