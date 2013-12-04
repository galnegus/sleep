package com.sleep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.sleep.soko.LevelArchitect;

public class GhostScreen implements Screen {
	public static boolean updatesPaused = false;
	private LevelArchitect levelArchitect;
	private CoolCamera camera;

	public GhostScreen() {
		camera = new CoolCamera(Constants.WIDTH, Constants.HEIGHT);

		// create levels
		levelArchitect = new LevelArchitect("levels/levels", camera);
	}

	public void update() {
		if (!updatesPaused) {
			levelArchitect.update();

			camera.update(Gdx.graphics.getDeltaTime(), levelArchitect.activeLevel.player.position.x
					+ (levelArchitect.activeLevel.player.getWidth() / 2), levelArchitect.activeLevel.player.position.y
					+ (levelArchitect.activeLevel.player.getHeight() / 2), levelArchitect.activeLevel.columnCount()
					* Constants.GRID_CELL_SIZE, levelArchitect.activeLevel.rowCount() * Constants.GRID_CELL_SIZE);
		}
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
		levelArchitect.drawLight();
		Sleep.batch.end();
		Sleep.fbo.end();

		// draw scene
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Sleep.batch.setProjectionMatrix(camera.combined);
		Sleep.batch.setShader(Sleep.ambientShader);
		Sleep.batch.begin();
		Sleep.fbo.getColorBufferTexture().bind(1);
		levelArchitect.bindLight(0);
		levelArchitect.render();
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
