package com.sleep.component.movement;

import com.badlogic.gdx.math.Vector2;
import com.sleep.Constants;
import com.sleep.GameScreen;
import com.sleep.component.ComponentException;

public class GhostMovementComponent extends MovementComponent {
	private float moveTimer = 0;

	@Override
	public void update(float delta) {
		moveTimer += delta;
		if(moveTimer >= Constants.GHOST_MOVE_FREQUENCY && !isMoving()) {
			moveTimer -= Constants.GHOST_MOVE_FREQUENCY;
			
			Vector2 moveTo = GameScreen.grid.bestMove(owner.position);
			
			move(moveTo.x * Constants.GRID_CELL_SIZE, moveTo.y * Constants.GRID_CELL_SIZE);
		}
		
		super.update(delta);
	}

	@Override
	public void init() throws ComponentException {
	}

}
