package com.sleep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class GhostScreen implements Screen {
	public static boolean updatesPaused = false;

	public void update() {
		if (!updatesPaused) {
			Sleep.world.update();
			Sleep.camera.update(Gdx.graphics.getDeltaTime());
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
		Sleep.batch.setProjectionMatrix(Sleep.camera.combined);
		Sleep.batch.setShader(Sleep.defaultShader);
		Sleep.batch.begin();
		Sleep.world.drawLight();
		Sleep.batch.end();
		Sleep.fbo.end();

		// draw scene
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Sleep.batch.setProjectionMatrix(Sleep.camera.combined);
		Sleep.batch.setShader(Sleep.ambientShader);
		Sleep.batch.begin();
		Sleep.fbo.getColorBufferTexture().bind(1);
		Sleep.world.bindLight(0);
		Sleep.world.render();
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
