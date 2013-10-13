package com.sleep.component.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.sleep.Constants;
import com.sleep.component.ComponentException;

public class PlayerContinousMovementComponent extends MovementComponent {
	float moveTimer = 0;

	@Override
	public void update(float delta) {
		moveTimer += delta;

		
		if(!isMoving() && moveTimer >= Constants.PLAYER_MOVE_FREQUENCY) {
			if(Gdx.input.isKeyPressed(Constants.MOVE_LEFT)) {
				move(-Constants.GRID_CELL_SIZE, 0);
				moveTimer = 0;
			} 
			if(Gdx.input.isKeyPressed(Constants.MOVE_RIGHT)) {
				move(Constants.GRID_CELL_SIZE, 0);
				moveTimer = 0;
			}
			if(Gdx.input.isKeyPressed(Constants.MOVE_UP)) {
				move(0, Constants.GRID_CELL_SIZE);
				moveTimer = 0;
			}
			if(Gdx.input.isKeyPressed(Constants.MOVE_DOWN)){
				move(0, -Constants.GRID_CELL_SIZE);
				moveTimer = 0;
			}
		}
		super.update(delta);
	}

	@Override
	public void init() throws ComponentException {
		// DO NOTHING
	}
}
