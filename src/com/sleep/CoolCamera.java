package com.sleep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

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
		this.centerPoint = new Vector2(width/2, height/2);
		this.rotation = 0;
		
		this.setToOrtho(false, width, height);
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
		float increment = 0.05f;
		if (Gdx.input.isKeyPressed(Constants.ZOOM_IN)) {
			scale.x += increment;
			scale.y += increment;
		} else if (Gdx.input.isKeyPressed(Constants.ZOOM_OUT)) {
			scale.x -= increment;
			scale.y -= increment;
		}
		
		float playerX = GameScreen.player.getPosition().x + (GameScreen.player.getWidth())/ (2);
		float playerY = GameScreen.player.getPosition().y + (GameScreen.player.getHeight())/ (2);
		
		movement.x = delta * ((playerX - position.x) * CAMERA_SCROLL_ACCELERATION) / size.x ;
		movement.y = delta * ((playerY - position.y) * CAMERA_SCROLL_ACCELERATION) / size.y;
		
		
		if (position.x <= size.x / 2 && movement.x < 0) {
			movement.x = 0;
		} else if (position.x >= GameScreen.level.getXSize() * Constants.GRID_CELL_SIZE - size.x / 2 && movement.x > 0) {
			movement.x = 0;
			//movement.x = GameScreen.level.getXSize() * Constants.GRID_CELL_SIZE - size.y;
		}
		if (position.y <= size.y / 2 && movement.y < 0) {
			movement.y = 0;
		} else if (position.y >= GameScreen.level.getYSize() * Constants.GRID_CELL_SIZE - size.y / 2 && movement.y > 0) {
			//movement.y = GameScreen.level.getYSize() * Constants.GRID_CELL_SIZE - size.y / scale.y;
			movement.y = 0;
		}
		
		
		this.translate(movement);
		super.update();
	}
}