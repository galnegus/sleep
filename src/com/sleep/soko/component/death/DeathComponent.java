package com.sleep.soko.component.death;

import com.sleep.soko.SokoLevel;
import com.sleep.soko.component.Component;
import com.sleep.soko.component.ComponentException;
import com.sleep.soko.component.Message;
import com.sleep.soko.component.movement.MovementComponent;

/**
 * when die() is used, the entity is instantly removed from the grid.
 * 
 * the entity is only removed from the entityList though, when certain critera
 * have been met.
 * 
 */
public class DeathComponent extends Component {
	private SokoLevel level;
	
	private boolean alive;
	private MovementComponent killedBy;

	public DeathComponent(SokoLevel level) {
		this.level = level;
		this.alive = true;
		killedBy = null;
	}

	public void die() {
		alive = false;
		level.collisionGrid.removeEntity(owner);
		
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
			
			level.entityManager.remove(owner);
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
