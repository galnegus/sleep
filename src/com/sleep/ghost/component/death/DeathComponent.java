package com.sleep.ghost.component.death;

import com.sleep.Sleep;
import com.sleep.ghost.Message;
import com.sleep.ghost.component.Component;
import com.sleep.ghost.component.ComponentException;
import com.sleep.ghost.component.movement.MovementComponent;

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
		Sleep.world.activeLevel.removeEntity(owner);
		
		owner.sendMessage(Message.ENTITY_DEATH);
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
			
			Sleep.world.activeLevel.entityManager.remove(owner);
		}
	}

	@Override
	public void init() throws ComponentException {

	}

	@Override
	public void receiveMessage(Message message) {
		// TODO Auto-generated method stub
		
	}

}
