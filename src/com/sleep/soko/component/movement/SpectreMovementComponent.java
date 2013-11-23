package com.sleep.soko.component.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.sleep.Constants;
import com.sleep.soko.Level;
import com.sleep.soko.component.ComponentException;

public class SpectreMovementComponent extends PathfindingMovementComponent {
	private float moveTimer;

	public SpectreMovementComponent(Level level) {
		super(level);
		this.moveTimer = 0;
		Velocity = 300f;
	}

	@Override
	public void update() {
		moveTimer += Gdx.graphics.getRawDeltaTime();
		if (moveTimer >= Constants.SPECTRE_MOVE_FREQUENCY && !isMoving()) {
			moveTimer -= Constants.SPECTRE_MOVE_FREQUENCY;
			
			Vector2[] moves = new Vector2[4];
			moves[0] = new Vector2(-1, -1);
			moves[1] = new Vector2(-1, 1);
			moves[2] = new Vector2(1, -1);
			moves[3] = new Vector2(1, 1);

			Vector2 moveTo = bestMove(level.spectrePathGrid, moves);

			move(moveTo.x * Constants.GRID_CELL_SIZE, moveTo.y * Constants.GRID_CELL_SIZE);
		}

		super.update();
	}

	@Override
	public void init() throws ComponentException {
	}

}
