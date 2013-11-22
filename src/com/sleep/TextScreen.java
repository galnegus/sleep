package com.sleep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sleep.text.IF;
import com.sleep.text.Terminal;

public class TextScreen implements Screen {
	IF interactiveFiction;
	OrthographicCamera textCamera;
	
	public TextScreen() {
		interactiveFiction = new IF();
		textCamera = new OrthographicCamera();
		textCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	public void update() {
//		Sleep.world.update();
		Sleep.camera.update(Gdx.graphics.getDeltaTime());
		interactiveFiction.update();
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
		
		Sleep.batch.setProjectionMatrix(textCamera.combined);
		Sleep.batch.setShader(Sleep.defaultShader);
		Sleep.batch.begin();
		interactiveFiction.render();
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
