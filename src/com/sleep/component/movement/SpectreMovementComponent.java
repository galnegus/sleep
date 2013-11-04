package com.sleep.component.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sleep.Constants;
import com.sleep.Sleep;
import com.sleep.component.ComponentException;

public class SpectreMovementComponent extends MovementComponent {
	private float moveTimer;

	public SpectreMovementComponent() {
		this.moveTimer = 0;
		Velocity = 300f;
	}

	@Override
	public void update() {
		moveTimer += Gdx.graphics.getRawDeltaTime();
		if (moveTimer >= Constants.SPECTRE_MOVE_FREQUENCY && !isMoving()) {
			moveTimer -= Constants.SPECTRE_MOVE_FREQUENCY;

			Vector2 moveTo = bestMove(owner.position);

			move(moveTo.x * Constants.GRID_CELL_SIZE, moveTo.y * Constants.GRID_CELL_SIZE);
		}

		super.update();
	}

	/**
	 * looks in the distanceGrid for the move that brings the entity the closest
	 * to the player
	 * 
	 * @param mover
	 *            the entity being moved
	 * @return a vector describing the movement (ie movement.x = -1, movement.y
	 *         = 0 for a move going left)
	 */
	public Vector2 bestMove(Vector2 mover) {
		Vector2 bestMove = new Vector2(0, 0);
		Vector2 moverPos = Sleep.grid.getGridPos(mover);
		Vector2 player = Sleep.grid.getGridPos(Sleep.player.position);

		int min = Sleep.grid.getXSize() * Sleep.grid.getYSize();

		Vector2[] moves = new Vector2[4];
		moves[0] = new Vector2(moverPos.x - 1, moverPos.y - 1);
		moves[1] = new Vector2(moverPos.x + 1, moverPos.y - 1);
		moves[2] = new Vector2(moverPos.x - 1, moverPos.y + 1);
		moves[3] = new Vector2(moverPos.x + 1, moverPos.y + 1);

		for (Vector2 move : moves) {
			if (move.x >= 0 && move.x < Sleep.grid.getXSize() && move.y >= 0 && move.y < Sleep.grid.getYSize()) {
				if (Sleep.grid.getSpectreDistanceAt(move.x, move.y) < min
						&& Sleep.grid.getSpectreDistanceAt(move.x, move.y) >= 0) {
					min = Sleep.grid.getSpectreDistanceAt(move.x, move.y);
					bestMove.set(move.x, move.y);
				} else if (Sleep.grid.getSpectreDistanceAt(move.x, move.y) == min) {

					// if the difference in distance in x and y from player to
					// move is smaller than difference in x and y from player to
					// bestMove, set move to bestMove.
					if (Math.abs(Math.abs(player.x - bestMove.x) - Math.abs(player.y - bestMove.y)) > Math.abs(Math
							.abs(player.x - move.x) - Math.abs(player.y - move.y))) {
						bestMove.set(move.x, move.y);
					}

					// if move is as good as bestMove, random!
					else if (Math.abs(Math.abs(player.x - bestMove.x) - Math.abs(player.y - bestMove.y)) == Math
							.abs(Math.abs(player.x - move.x) - Math.abs(player.y - move.y))) {
						if (MathUtils.random(1) == 0) {
							bestMove.set(move.x, move.y);
						}
					}
				}
			}
		}

		// if no move was found, check manhattan distance!
		if (bestMove.x == 0 && bestMove.y == 0) {
			for (Vector2 move : moves) {
				if (move.x >= 0 && move.x < Sleep.grid.getXSize() && move.y >= 0 && move.y < Sleep.grid.getYSize()) {
					if (Sleep.grid.manhattanDistance(move.x, move.y) < min
							&& Sleep.grid.manhattanDistance(move.x, move.y) >= 0) {
						min = Sleep.grid.manhattanDistance(move.x, move.y);
						bestMove.set(move.x, move.y);
					} else if (Sleep.grid.manhattanDistance(move.x, move.y) == min) {

						// if difference in distance in x and y from player to
						// move is smaller than difference in x and y from
						// player to bestMove, set move to bestMove.
						if (Math.abs(Math.abs(player.x - bestMove.x) - Math.abs(player.y - bestMove.y)) > Math.abs(Math
								.abs(player.x - move.x) - Math.abs(player.y - move.y))) {
							bestMove.set(move.x, move.y);
						}

						// if move is as good as bestMove, random!
						else if (Math.abs(Math.abs(player.x - bestMove.x) - Math.abs(player.y - bestMove.y)) == Math
								.abs(Math.abs(player.x - move.x) - Math.abs(player.y - move.y))) {
							if (MathUtils.random(1) == 0) {
								bestMove.set(move.x, move.y);
							}
						}
					}
				}
			}
		}
		
		Vector2 movement = new Vector2(bestMove.x - moverPos.x, bestMove.y - moverPos.y);
		return movement;
	}

	@Override
	public void init() throws ComponentException {
	}

}
