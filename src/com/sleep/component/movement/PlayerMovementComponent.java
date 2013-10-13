package com.sleep.component.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.sleep.Constants;
import com.sleep.component.ComponentException;

public class PlayerMovementComponent extends MovementComponent {
	private boolean movingUp = false;
	private boolean movingDown = false;
	private boolean movingLeft = false;
	private boolean movingRight = false;

	private Vector2 queuedMove = new Vector2(0, 0);

	@Override
	public void update(float delta) {
		// queue moves
		if (!isMoving()) {
			if (!Gdx.input.isKeyPressed(Constants.MOVE_LEFT)) {
				movingLeft = false;
			} 
			if (!Gdx.input.isKeyPressed(Constants.MOVE_RIGHT)) {
				movingRight = false;
			} 
			if (!Gdx.input.isKeyPressed(Constants.MOVE_UP)) {
				movingUp = false;
			} 
			if (!Gdx.input.isKeyPressed(Constants.MOVE_DOWN)) {
				movingDown = false;
			}
			
			if (Gdx.input.isKeyPressed(Constants.MOVE_LEFT) && !movingLeft) {
				move(-Constants.GRID_CELL_SIZE, 0);
				movingLeft = true;
			} else if (Gdx.input.isKeyPressed(Constants.MOVE_RIGHT) && !movingRight) {
				move(Constants.GRID_CELL_SIZE, 0);
				movingRight = true;
			} else if (Gdx.input.isKeyPressed(Constants.MOVE_UP) && !movingUp) {
				move(0, Constants.GRID_CELL_SIZE);
				movingUp = true;
			} else if (Gdx.input.isKeyPressed(Constants.MOVE_DOWN) && !movingDown) {
				move(0, -Constants.GRID_CELL_SIZE);
				movingDown = true;
			} 
		}

		super.update(delta);
	}

	@Override
	public void init() throws ComponentException {
		// DO NOTHING
	}
}