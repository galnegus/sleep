package com.sleep;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class CoolCamera extends OrthographicCamera {
	protected Vector2 movement;
	protected final float CAMERA_SCROLL_ACCELERATION = 1500f;

	public CoolCamera(int width, int height) {
		super(width, height);
		this.movement = new Vector2(0, 0);
		this.setToOrtho(false, width, height);
	}

	/**
	 * Changes the size of camera so that no "black bars" are visible. Also
	 * positions the camera to focus on the player entity position.
	 * 
	 * @param boardWidth
	 *            the width of the board in pixels (e.g. level.xSize *
	 *            Constants.GRID_CELL_SIZE)
	 * @param boardHeight
	 *            the height of the board in pixels (e.g. level.ySize *
	 *            Constants.GRID_CELL_SIZE)
	 * @param x
	 *            x position to position camera at (centered)
	 * @param y
	 *            y position to position camera at (centered)
	 */

	public void resize(int boardWidth, int boardHeight, float x, float y, boolean respectBoundaries) {
		Vector2 widthSize = newSizeByWidth(boardWidth);
		Vector2 heightSize = newSizeByHeight(boardHeight);

		if (widthSize.len() < heightSize.len()) {
			viewportWidth = (int) widthSize.x;
			viewportHeight = (int) widthSize.y;
		} else {
			viewportWidth = (int) heightSize.x;
			viewportHeight = (int) heightSize.y;
		}

		if (viewportWidth > Constants.WIDTH || viewportHeight > Constants.HEIGHT) {
			viewportWidth = Constants.WIDTH;
			viewportHeight = Constants.HEIGHT;
		}

		setToOrtho(false, viewportWidth, viewportHeight);

		position.set(x, y, position.z);

		if (respectBoundaries)
			adjustPositionByBounds(boardWidth, boardHeight);
	}

	/**
	 * Updates the camera, focusing on the coordinates given.
	 * 
	 * @param delta
	 *            Gdx.graphics.getDeltaTime()
	 * @param x
	 *            x-coordinate of position to focus camera on
	 * @param y
	 *            y-coordinate of position to focus camera on
	 */
	public void update(float delta, float x, float y) {
		updatePosition(delta, x, y);
		super.update();
	}

	/**
	 * Updates camera position (see above), adjusts position so that nothing
	 * outside of the given board size (black bars) is inside of the camera's
	 * viewport.
	 * 
	 * @param boardWidth
	 *            the width of the board
	 * @param boardHeight
	 *            the height of the board
	 */
	public void update(float delta, float x, float y, int boardWidth, int boardHeight) {
		updatePosition(delta, x, y);
		adjustPositionByBounds(boardWidth, boardHeight);
		super.update();
	}

	/**
	 * Updates position smoothly, focusing on (x, y).
	 */
	private void updatePosition(float delta, float x, float y) {
		movement.x = delta * ((x - position.x) * CAMERA_SCROLL_ACCELERATION) / viewportWidth;
		movement.y = delta * ((y - position.y) * CAMERA_SCROLL_ACCELERATION) / viewportHeight;
		position.set(position.x + movement.x, position.y + movement.y, position.z);
	}

	/**
	 * Adjusts position so that only the board, given by the parameters is
	 * visible.
	 */
	private void adjustPositionByBounds(int boardWidth, int boardHeight) {
		if (position.x <= viewportWidth / 2) {
			position.set(viewportWidth / 2, position.y, position.z);
		} else if (position.x >= boardWidth - viewportWidth / 2) {
			position.set(boardWidth - viewportWidth / 2, position.y, position.z);
		}
		if (position.y <= viewportHeight / 2) {
			position.set(position.x, viewportHeight / 2, position.z);
		} else if (position.y >= boardHeight - viewportHeight / 2) {
			position.set(position.x, boardHeight - viewportHeight / 2, position.z);
		}
	}

	/**
	 * Caclulates appropriate size that fits aspect ratio given by
	 * Constants.WIDTH and Constants.HEIGHT, given a width.
	 */
	public Vector2 newSizeByWidth(int width) {
		return new Vector2(width, width * ((float) Constants.HEIGHT / (float) Constants.WIDTH));
	}

	/**
	 * Caclulates appropriate size that fits aspect ratio given by
	 * Constants.WIDTH and Constants.HEIGHT, given a height.
	 */
	public Vector2 newSizeByHeight(int height) {
		return new Vector2(height * ((float) Constants.WIDTH / (float) Constants.HEIGHT), height);
	}
}