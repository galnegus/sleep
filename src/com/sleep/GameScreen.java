package com.sleep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;

public class GameScreen implements Screen {
	final Sleep game;
	
	public static EntityManager backgroundManager;
	public static EntityManager entityManager;
	public static AssetManager assets;
	public static Entity player;
	public static Level level;

	//Camera camera;
	CoolCamera camera;

	public GameScreen(final Sleep game) {
		this.game = game;
		
		assets = new AssetManager();
		assets.load("images/enemy.png", Texture.class);
		assets.load("images/wall.png", Texture.class);
		assets.load("images/box.png", Texture.class);
		assets.load("images/grid.png", Texture.class);
		assets.load("images/player.png", Texture.class);
		assets.load("images/player_bw.png", Texture.class);
		assets.finishLoading();
		
		entityManager = new EntityManager();
		backgroundManager = new EntityManager();
		
		//camera = new Camera(gc);
		camera = new CoolCamera(1280, 720);
		
		level = new Level();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render(float delta) {
		assets.update();
		
		//clear screen
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//update camera matrices
		camera.update(delta);
		
		//draw batch
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		
		backgroundManager.render(game);
		entityManager.render(game);
		
		game.batch.end();
		
		//update stuff
		entityManager.update(delta);
		backgroundManager.update(delta);
		level.update(delta);

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
