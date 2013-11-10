package com.sleep.component.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sleep.Constants;
import com.sleep.Sleep;
import com.sleep.component.ComponentException;

public abstract class PathfindingMovementComponent extends MovementComponent {

	@Override
	public void update() {
		super.update();
	}

	/**
	 * looks in given pathGrid for the move that brings the entity the closest
	 * to the player
	 * 
	 * @param pathGrid
	 *            a given pathGrid (preferably from Sleep.grid)
	 * @param moves
	 *            a vector of moves describing movement in gridpos (eg (-1, 0)
	 *            for move going left, or (1, 1) for move going diagonally
	 *            up-left).
	 * @return a vector describing the movement (eg (-1, 0) for a move going
	 *         left)
	 */
	public Vector2 bestMove(int[][] pathGrid, Vector2[] moves) {
		Vector2 bestMove = new Vector2(0, 0);
		Vector2 moverPos = Sleep.grid.getGridPos(owner.position);
		Vector2 player = Sleep.grid.getGridPos(Sleep.player.position);

		// apply actual gridpos to each move
		for (Vector2 move : moves) {
			move.x += moverPos.x;
			move.y += moverPos.y;
		}

		int min = Sleep.grid.getXSize() * Sleep.grid.getYSize();

		for (Vector2 move : moves) {
			if (move.x >= 0 && move.x < Sleep.grid.getXSize() && move.y >= 0 && move.y < Sleep.grid.getYSize()) {
				if (Sleep.grid.getPathDistance(pathGrid, move) < min && Sleep.grid.getPathDistance(pathGrid, move) >= 0) {
					min = Sleep.grid.getPathDistance(pathGrid, move);
					bestMove.set(move.x, move.y);
				} else if (Sleep.grid.getPathDistance(pathGrid, move) == min) {

					// if the difference in distance in x and y from player to
					// move is smaller than difference in x and y from player to
					// bestMove, set bestMove to move.
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
						// player to bestMove, set bestMove to move.
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