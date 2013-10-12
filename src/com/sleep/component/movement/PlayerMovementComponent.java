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
		if(!isMoving()) {
			if(Gdx.input.isKeyPressed(Constants.MOVE_LEFT) && triggerLeft) {
				move(-Constants.GRID_CELL_SIZE, 0);
				triggerLeft = false;
			} else if(!Gdx.input.isKeyPressed(Constants.MOVE_LEFT) && !triggerLeft) {
				triggerLeft = true;
			}
			
			if(Gdx.input.isKeyPressed(Constants.MOVE_RIGHT) && triggerRight) {
				move(Constants.GRID_CELL_SIZE, 0);
				triggerRight = false;
			} else if(!Gdx.input.isKeyPressed(Constants.MOVE_RIGHT) && !triggerRight) {
				triggerRight = true;
			}
			
			if(Gdx.input.isKeyPressed(Constants.MOVE_UP) && triggerUp) {
				move(0, Constants.GRID_CELL_SIZE);
				triggerUp = false;
			} else if(!Gdx.input.isKeyPressed(Constants.MOVE_UP) && !triggerUp) {
				triggerUp = true;
			}
			
			if(Gdx.input.isKeyPressed(Constants.MOVE_DOWN) && triggerDown){
				move(0, -Constants.GRID_CELL_SIZE);
				triggerDown = false;
			} else if(!Gdx.input.isKeyPressed(Constants.MOVE_DOWN) && !triggerDown) {
				triggerDown = true;
			}		
		}
		super.update(delta);
	}

	@Override
	public void init() throws ComponentException {
		// DO NOTHING
	}
}