package com.sleep;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Sleep implements ApplicationListener {
	public static EntityManager backgroundManager;
	public static EntityManager entityManager;
	public static AssetManager assets;
	public static Entity player;
	public static Grid grid;

	public static SpriteBatch batch;

	// Camera camera;
	CoolCamera camera;

	@Override
	public void create() {
		batch = new SpriteBatch();

		assets = new AssetManager();
		assets.load("images/ghost.png", Texture.class);
		assets.load("images/wall.png", Texture.class);
		assets.load("images/box.png", Texture.class);
		assets.load("images/grid.png", Texture.class);
		assets.load("images/player.png", Texture.class);
		assets.load("images/player_bw.png", Texture.class);
		assets.load("images/placeholder.png", Texture.class);
		assets.finishLoading();

		entityManager = new EntityManager();
		backgroundManager = new EntityManager();

		// camera = new Camera(gc);
		camera = new CoolCamera(1280, 720);

		grid = new Grid();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		assets.update();

		// clear screen
		Gdx.gl.glClearColor(1, 0.98f, 0.96f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// update camera matrices
		camera.update(Gdx.graphics.getDeltaTime());

		// draw batch
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		backgroundManager.render();
		entityManager.render();
		batch.end();

		// update stuff
		entityManager.update(Gdx.graphics.getDeltaTime());
		backgroundManager.update(Gdx.graphics.getDeltaTime());
		grid.update();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
