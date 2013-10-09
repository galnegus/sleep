package com.sleep.component.movement;

import com.badlogic.gdx.Gdx;
import com.sleep.Constants;
import com.sleep.component.ComponentException;

public class PlayerMovementComponent extends MovementComponent {
	boolean triggerUp = true;
	boolean triggerDown = true;
	boolean triggerLeft = true;
	boolean triggerRight = true;

	@Override
	public void update(float delta) {
		resetAcceleration();
		
		if(!moving) {
			if(Gdx.input.isKeyPressed(Constants.MOVE_LEFT) && triggerLeft) {
				moveTo(owner.position.x - Constants.GRID_CELL_SIZE, owner.position.y, delta);
				triggerLeft = false;
			} else if(!Gdx.input.isKeyPressed(Constants.MOVE_LEFT) && !triggerLeft) {
				triggerLeft = true;
			}
			
			if(Gdx.input.isKeyPressed(Constants.MOVE_RIGHT) && triggerRight) {
				moveTo(owner.position.x + Constants.GRID_CELL_SIZE, owner.position.y, delta);
				triggerRight = false;
			} else if(!Gdx.input.isKeyPressed(Constants.MOVE_RIGHT) && !triggerRight) {
				triggerRight = true;
			}
			
			if(Gdx.input.isKeyPressed(Constants.MOVE_UP) && triggerUp) {
				moveTo(owner.position.x, owner.position.y + Constants.GRID_CELL_SIZE, delta);
				triggerUp = false;
			} else if(!Gdx.input.isKeyPressed(Constants.MOVE_UP) && !triggerUp) {
				triggerUp = true;
			}
			
			if(Gdx.input.isKeyPressed(Constants.MOVE_DOWN) && triggerDown){
				moveTo(owner.position.x, owner.position.y - Constants.GRID_CELL_SIZE, delta);
				triggerDown = false;
			} else if(!Gdx.input.isKeyPressed(Constants.MOVE_DOWN) && !triggerDown) {
				triggerDown = true;
			}		
		}
		updateStuff(delta);
	}

	@Override
	public void init() throws ComponentException {
		// DO NOTHING
	}
}
