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
		if(moveTimer >= Constants.GHOST_MOVE_FREQUENCY) {
			moveTimer = 0;
			
			Vector2 move = GameScreen.level.bestMove(owner.position);
			
			teleport(move.x, move.y);
		}
		
		updateStuff(delta);
	}

	@Override
	public void init() throws ComponentException {
	}

}
