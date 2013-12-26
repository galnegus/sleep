package com.sleep.soko.component.movement;

import com.badlogic.gdx.Gdx;
import com.sleep.Constants;
import com.sleep.soko.SokoLevel;
import com.sleep.soko.component.ComponentException;

public class PlayerMovementComponent extends MovementComponent {
	private boolean movingUp = false;
	private boolean movingDown = false;
	private boolean movingLeft = false;
	private boolean movingRight = false;

	public PlayerMovementComponent(SokoLevel level) {
		super(level);
	}

	@Override
	public void update() {
		// queue moves
		if (!isMoving()) {
			if (!Gdx.input.isKeyPressed(Constants.MOVE_LEFT) && !Gdx.input.isKeyPressed(Constants.MOVE_LEFT_ALT)) {
				movingLeft = false;
			}
			if (!Gdx.input.isKeyPressed(Constants.MOVE_RIGHT) && !Gdx.input.isKeyPressed(Constants.MOVE_RIGHT_ALT)) {
				movingRight = false;
			}
			if (!Gdx.input.isKeyPressed(Constants.MOVE_UP) && !Gdx.input.isKeyPressed(Constants.MOVE_UP_ALT)) {
				movingUp = false;
			}
			if (!Gdx.input.isKeyPressed(Constants.MOVE_DOWN) && !Gdx.input.isKeyPressed(Constants.MOVE_DOWN_ALT)) {
				movingDown = false;
			}

			if ((Gdx.input.isKeyPressed(Constants.MOVE_LEFT) || Gdx.input.isKeyPressed(Constants.MOVE_LEFT_ALT))
					&& !movingLeft) {
				move(-Constants.GRID_CELL_SIZE, 0);
				movingLeft = true;
			} else if ((Gdx.input.isKeyPressed(Constants.MOVE_RIGHT) || Gdx.input
					.isKeyPressed(Constants.MOVE_RIGHT_ALT)) && !movingRight) {
				move(Constants.GRID_CELL_SIZE, 0);
				movingRight = true;
			} else if ((Gdx.input.isKeyPressed(Constants.MOVE_UP) || Gdx.input.isKeyPressed(Constants.MOVE_UP_ALT))
					&& !movingUp) {
				move(0, Constants.GRID_CELL_SIZE);
				movingUp = true;
			} else if ((Gdx.input.isKeyPressed(Constants.MOVE_DOWN) || Gdx.input.isKeyPressed(Constants.MOVE_DOWN_ALT))
					&& !movingDown) {
				move(0, -Constants.GRID_CELL_SIZE);
				movingDown = true;
			}
		}

		super.update();
	}

	@Override
	public void init() throws ComponentException {
		// DO NOTHING
	}
}