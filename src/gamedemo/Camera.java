package gamedemo;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class Camera {
	protected Vector2f position;
	protected Vector2f scale;
	protected Vector2f centerPoint;
	protected float rotation;
	protected final float CAMERA_SCROLL_ACCELERATION = .33f;

	public Camera(GameContainer gc) {
		position = new Vector2f();
		scale = new Vector2f(1, 1);
		centerPoint = new Vector2f(gc.getWidth()/2, gc.getHeight()/2);
		this.position.x = 0;
		this.position.y = 0;
		this.rotation = 0;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
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
	public void update(GameContainer gc, int delta) {
		Input input = gc.getInput();
		float increment = 0.05f;
		if (input.isKeyDown(Constants.ZOOM_IN)) {
			scale.x += increment;
			scale.y += increment;
		} else if (input.isKeyDown(Constants.ZOOM_OUT)) {
			scale.x -= increment;
			scale.y -= increment;
		}
		
		float playerX = Demo.player.getPosition().x - (gc.getWidth() - Demo.player.getWidth())/ (2);
		float playerY = Demo.player.getPosition().y - (gc.getHeight() - Demo.player.getHeight())/ (2);
		
		position.x = position.x + delta * (playerX - position.x) / (gc.getWidth() * CAMERA_SCROLL_ACCELERATION);
		position.y = position.y + delta * (playerY - position.y) / (gc.getHeight() * CAMERA_SCROLL_ACCELERATION);
		
		if (position.x < 0) {
			position.x = 0;
		} else if (position.x > Demo.level.getXSize() * Constants.GRID_CELL_SIZE - gc.getWidth()) {
			position.x = Demo.level.getXSize() * Constants.GRID_CELL_SIZE - gc.getWidth();
		}
		if (position.y < 0) {
			position.y = 0;
		} else if (position.y > Demo.level.getYSize() * Constants.GRID_CELL_SIZE - gc.getHeight() / scale.y) {
			position.y = Demo.level.getYSize() * Constants.GRID_CELL_SIZE - gc.getHeight() / scale.y;
		}
	}

	/**
	 * This method should be called in the main render method before rendering
	 * anything else. All coordinates used in subsequent rendering methods will
	 * be rendered relative to the coordinates entered as arguments in
	 * Graphics.translate().
	 * 
	 * @param gr
	 */
	public void render(Graphics gr) {
		gr.translate(centerPoint.x, centerPoint.y);
		gr.scale(scale.x, scale.y);
		gr.rotate(0, 0, rotation);
		gr.translate(-centerPoint.x, -centerPoint.y);
		gr.translate(-position.x, -position.y);
		
		
	}

	/**
	 * This method should be called in the main render method after all other
	 * rendering is done.
	 * 
	 * @param gr
	 */
	public void renderDone(Graphics gr) {
		gr.resetTransform();
	}
}
