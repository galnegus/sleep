package com.sleep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sleep.text.IF;
import com.sleep.text.OverWorld;

public class TextScreen implements Screen {
	private OverWorld overWorld;
	private CoolCamera overWorldCamera;

	private IF interactiveFiction;
	private OrthographicCamera textCamera;

	public TextScreen(Sleep sleep) {
		overWorld = new OverWorld("levels/dream");
		overWorldCamera = new CoolCamera(Constants.WIDTH, Constants.HEIGHT);
		overWorldCamera.resize(Constants.WIDTH / 2, Constants.HEIGHT / 2, overWorld.player.position.x
				+ (overWorld.player.getWidth() / 2) - Constants.WIDTH / 8, overWorld.player.position.y
				+ (overWorld.player.getHeight() / 2), false);

		interactiveFiction = new IF(sleep);
		textCamera = new OrthographicCamera();
		textCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void update() {
		overWorld.update();
		overWorldCamera.update(Gdx.graphics.getDeltaTime(), overWorld.player.position.x
				+ (overWorld.player.getWidth() / 2) - overWorldCamera.viewportWidth / 4, overWorld.player.position.y
				+ (overWorld.player.getHeight() / 2));
		interactiveFiction.update();
	}

	@Override
	public void render(float delta) {
		// update stuff
		update();
		
		overWorld.render();

		Sleep.batch.setProjectionMatrix(textCamera.combined);
		Sleep.batch.setShader(null);
		Sleep.batch.begin();
		interactiveFiction.render(delta);
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
