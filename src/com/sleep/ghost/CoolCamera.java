package com.sleep.ghost;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.sleep.Constants;
import com.sleep.Sleep;

public class CoolCamera extends OrthographicCamera {
	protected Vector2 size;
	protected Vector2 movement;
	protected Vector2 scale;
	protected Vector2 centerPoint;
	protected float rotation;
	protected final float CAMERA_SCROLL_ACCELERATION = 1500f;

	public CoolCamera(int width, int height) {
		this.size = new Vector2(width, height);
		this.movement = new Vector2(0, 0);
		this.scale = new Vector2(1, 1);
		this.centerPoint = new Vector2(width / 2, height / 2);
		this.rotation = 0;

		this.setToOrtho(false, width, height);
	}

	/**
	 * Changes the size of camera so that no "black bars" are visible. Also
	 * positions the camera to focus on the player entity position.
	 * 
	 * @param width
	 *            the width of the board in pixels (e.g. level.xSize *
	 *            Constants.GRID_CELL_SIZE)
	 * @param height
	 *            the height of the board in pixels (e.g. level.ySize *
	 *            Constants.GRID_CELL_SIZE)
	 * @param player
	 *            the player entity on which the camera focuses
	 */
	public void resize(int width, int height, Entity player) {
		Vector2 widthSize = newSizeByWidth(width);
		Vector2 heightSize = newSizeByHeight(height);

		if (widthSize.len() < heightSize.len()) {
			width = (int) widthSize.x;
			height = (int) widthSize.y;
		} else {
			width = (int) heightSize.x;
			height = (int) heightSize.y;
		}
		
		if (width > Constants.WIDTH || height > Constants.HEIGHT) {
			width = Constants.WIDTH;
			height = Constants.HEIGHT;
		}

		viewportWidth = width;
		viewportHeight = height;

		size.set(width, height);
		centerPoint.set(width / 2, height / 2);
		setToOrtho(false, width, height);

		position.set(player.position.x + (player.getWidth()) / (2), player.position.y + (player.getHeight()) / (2),
				position.z);
	}

	/**
	 * Sets the camera to be centered on the position of an entity (such as the
	 * player Entity). Sets a minimum X position if the resulting X position
	 * from centering is less than 0.
	 * 
	 * @param monkey
	 *            The entity to center the camera to.
	 * @param gc
	 *            GameContainer
	 */
	public void update(float delta) {
		Level activeLevel = Sleep.world.activeLevel;
		Entity player = activeLevel.player;
		
		float playerX = player.position.x + (player.getWidth()) / (2);
		float playerY = player.position.y + (player.getHeight()) / (2);

		movement.x = delta * ((playerX - position.x) * CAMERA_SCROLL_ACCELERATION) / size.x;
		movement.y = delta * ((playerY - position.y) * CAMERA_SCROLL_ACCELERATION) / size.y;

		position.set(position.x + movement.x, position.y + movement.y, position.z);

		if (position.x <= size.x / 2 && movement.x < 0) {
			position.set(size.x / 2, position.y, position.z);
		} else if (position.x >= activeLevel.getXSize() * Constants.GRID_CELL_SIZE - size.x / 2 && movement.x > 0) {
			position.set(activeLevel.getXSize() * Constants.GRID_CELL_SIZE - size.x / 2, position.y, position.z);
		}
		if (position.y <= size.y / 2 && movement.y < 0) {
			position.set(position.x, size.y / 2, position.z);
		} else if (position.y >= activeLevel.getYSize() * Constants.GRID_CELL_SIZE - size.y / 2 && movement.y > 0) {
			position.set(position.x, activeLevel.getYSize() * Constants.GRID_CELL_SIZE - size.y / 2, position.z);
		}

		super.update();
	}

	public Vector2 newSizeByWidth(int width) {
		return new Vector2(width, width * ((float) Constants.HEIGHT / (float) Constants.WIDTH));
	}

	public Vector2 newSizeByHeight(int height) {
		return new Vector2(height * ((float) Constants.WIDTH / (float) Constants.HEIGHT), height);
	}
}