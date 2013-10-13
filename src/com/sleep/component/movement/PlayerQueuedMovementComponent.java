package com.sleep.component.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.sleep.Constants;
import com.sleep.component.ComponentException;

public class PlayerQueuedMovementComponent extends MovementComponent {
	private boolean movingUp = false;
	private boolean movingDown = false;
	private boolean movingLeft = false;
	private boolean movingRight = false;

	private Vector2 queuedMove = new Vector2(0, 0);

	@Override
	public void update(float delta) {
		// queue moves
		if (isMoving()) {
			if (Gdx.input.isKeyPressed(Constants.MOVE_LEFT) && !movingLeft) {
				queuedMove.set(-Constants.GRID_CELL_SIZE, 0);
			} else if (Gdx.input.isKeyPressed(Constants.MOVE_RIGHT) && !movingRight) {
				queuedMove.set(Constants.GRID_CELL_SIZE, 0);
			} else if (Gdx.input.isKeyPressed(Constants.MOVE_UP) && !movingUp) {
				queuedMove.set(0, Constants.GRID_CELL_SIZE);
			} else if (Gdx.input.isKeyPressed(Constants.MOVE_DOWN) && !movingDown) {
				queuedMove.set(0, -Constants.GRID_CELL_SIZE);
			}
		} else if (!isMoving()) {
			// do queued move
			if (queuedMove.x != 0 || queuedMove.y != 0) {
				if (queuedMove.x > 0) {
					movingRight = true;
//					movingLeft = false;
//					movingUp = false;
//					movingDown = false;
				} else if (queuedMove.x < 0) {
					movingLeft = true;
//					movingRight = false;
//					movingUp = false;
//					movingDown = false;
				} else if (queuedMove.y > 0) {
					movingUp = true;
//					movingDown = false;
//					movingLeft = false;
//					movingRight = false;
				} else if (queuedMove.y < 0) {
					movingDown = true;
//					movingUp = false;
//					movingLeft = false;
//					movingRight = false;
				}
				move(queuedMove.x, queuedMove.y);
				queuedMove.set(0, 0);
			} else {
				// do input move
				if (Gdx.input.isKeyPressed(Constants.MOVE_LEFT) && !movingLeft) {
					move(-Constants.GRID_CELL_SIZE, 0);
					movingLeft = true;
				} else if (!Gdx.input.isKeyPressed(Constants.MOVE_LEFT) && movingLeft) {
					movingLeft = false;
				} else if (Gdx.input.isKeyPressed(Constants.MOVE_RIGHT) && !movingRight) {
					move(Constants.GRID_CELL_SIZE, 0);
					movingRight = true;
				} else if (!Gdx.input.isKeyPressed(Constants.MOVE_RIGHT) && movingRight) {
					movingRight = false;
				} else if (Gdx.input.isKeyPressed(Constants.MOVE_UP) && !movingUp) {
					move(0, Constants.GRID_CELL_SIZE);
					movingUp = true;
				} else if (!Gdx.input.isKeyPressed(Constants.MOVE_UP) && movingUp) {
					movingUp = false;
				} else if (Gdx.input.isKeyPressed(Constants.MOVE_DOWN) && !movingDown) {
					move(0, -Constants.GRID_CELL_SIZE);
					movingDown = true;
				} else if (!Gdx.input.isKeyPressed(Constants.MOVE_DOWN) && movingDown) {
					movingDown = false;
				}
			}

		}
		System.out.println("movement: ");
		System.out.println("up: " + movingUp);
		System.out.println("down: " + movingDown);
		System.out.println("left: " + movingLeft);
		System.out.println("right: " + movingRight);
		super.update(delta);
	}

	@Override
	public void init() throws ComponentException {
		// DO NOTHING
	}
}