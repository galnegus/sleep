package com.sleep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sleep.text.IF;
import com.sleep.text.OverWorld;

public class TextScreen implements Screen {
	private OverWorld overWorld;
	private CoolCamera overWorldCamera;
	
	private IF interactiveFiction;
	private OrthographicCamera textCamera;

	public TextScreen() {
		overWorld = new OverWorld("levels/mansion");
		overWorldCamera = new CoolCamera(Constants.WIDTH, Constants.HEIGHT);
		overWorldCamera.resize(Constants.WIDTH * 4, Constants.HEIGHT * 4,
				overWorld.player.position.x + (overWorld.player.getWidth() / 2) - Constants.WIDTH / 3,
				overWorld.player.position.y + (overWorld.player.getHeight() / 2));
		
		interactiveFiction = new IF(overWorld);
		textCamera = new OrthographicCamera();
		textCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void update() {
		overWorld.update();
		overWorldCamera.update(Gdx.graphics.getDeltaTime(), overWorld.player.position.x
				+ (overWorld.player.getWidth() / 2) - Constants.WIDTH / 3, overWorld.player.position.y
				+ (overWorld.player.getHeight() / 2));
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
		Sleep.batch.setProjectionMatrix(overWorldCamera.combined);
		Sleep.batch.setShader(Sleep.defaultShader);
		Sleep.batch.begin();
		overWorld.drawLight();
		Sleep.batch.end();
		Sleep.fbo.end();

		// draw scene
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Sleep.batch.setProjectionMatrix(overWorldCamera.combined);
		Sleep.batch.setShader(Sleep.ambientShader);
		Sleep.batch.begin();
		Sleep.fbo.getColorBufferTexture().bind(1);
		overWorld.bindLight(0);
		overWorld.render();
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
