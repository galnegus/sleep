package com.sleep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.sleep.soko.SokoLevel;

public class GhostScreen implements Screen {
	public static boolean updatesPaused = false;
	private SokoLevel level;
	private CoolCamera camera;

	public GhostScreen() {
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

		// clear screen color
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);

		// draw light to FBO
		Sleep.fbo.begin();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Sleep.batch.setProjectionMatrix(camera.combined);
		Sleep.batch.setShader(Sleep.defaultShader);
		Sleep.batch.begin();
		level.drawLight();
		Sleep.batch.end();
		Sleep.fbo.end();

		// draw scene
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Sleep.batch.setProjectionMatrix(camera.combined);
		Sleep.batch.setShader(Sleep.ambientShader);
		Sleep.batch.begin();
		Sleep.fbo.getColorBufferTexture().bind(1);
		level.bindLight(0);
		level.render();
		Sleep.batch.end();
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
