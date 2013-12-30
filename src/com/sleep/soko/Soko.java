package com.sleep.soko;

import com.badlogic.gdx.Gdx;
import com.sleep.Constants;
import com.sleep.CoolCamera;
import com.sleep.CoolScreen;
import com.sleep.Sleep;

public class Soko extends CoolScreen {
	public static boolean updatesPaused = false;
	private SokoLevel level;
	private CoolCamera camera;

	public Soko(Sleep sleep) {
		super(sleep);

		camera = new CoolCamera(Constants.WIDTH, Constants.HEIGHT);
	}

	public void update() {
		if (!updatesPaused) {
			level.update();

			camera.update(Gdx.graphics.getDeltaTime(), level.player.position.x + (level.player.getWidth() / 2),
					level.player.position.y + (level.player.getHeight() / 2), level.columnCount()
							* Constants.GRID_CELL_SIZE, level.rowCount() * Constants.GRID_CELL_SIZE);
		}
	}

	public void setLevel(SokoLevel level) {
		this.level = level;

		int width = level.columnCount() * Constants.GRID_CELL_SIZE;
		int height = level.rowCount() * Constants.GRID_CELL_SIZE;

		camera.resize(width, height, level.player.position.x + (level.player.getWidth() / 2), level.player.position.y
				+ (level.player.getHeight()) / 2, true);
	}

	@Override
	public void render(float delta) {
		// update stuff
		update();

		super.drawLight(camera, level);

		Sleep.fboBlurA.begin();
		super.drawScene(camera, level, level);
		Sleep.fboBlurA.end();

		super.drawBlur();

		// SWITCHES SCREEN IF TRIGGERED, OTHERWISE DOES NOTHING
		super.render();

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
