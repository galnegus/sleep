package com.sleep;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Sleep implements ApplicationListener {
	public static EntityManager backgroundManager;
	public static EntityManager entityManager;
	public static AssetManager assets;
	public static Entity player;
	public static Grid grid;
	
	public static float timer;
	public static boolean updatesPaused;

	public static SpriteBatch batch;
	public static BitmapFont font;
	public static Music music;

	// Camera camera;
	CoolCamera camera;

	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("fonts/24pt.fnt"));
		

		assets = new AssetManager();
		assets.load("images/ghost.png", Texture.class);
		assets.load("images/spectre.png", Texture.class);
		assets.load("images/wall.png", Texture.class);
		assets.load("images/box.png", Texture.class);
		assets.load("images/grid.png", Texture.class);
		assets.load("images/player.png", Texture.class);
		assets.load("images/player_bw.png", Texture.class);
		assets.load("images/placeholder.png", Texture.class);
		assets.load("music/25_october.ogg", Music.class);
		assets.finishLoading();
		
		music = assets.get("music/25_october.ogg", Music.class);
		music.setLooping(true);
		music.setVolume(0.8f);
		music.play();

		entityManager = new EntityManager();
		backgroundManager = new EntityManager();

		camera = new CoolCamera(1280, 720);
		
		timer = 0;
		updatesPaused = false;

		grid = new Grid();
	}

	@Override
	public void resize(int width, int height) {
		
	}
	
	public void update() {
		if(!updatesPaused) {
			entityManager.update(Gdx.graphics.getDeltaTime());
			backgroundManager.update(Gdx.graphics.getDeltaTime());
			grid.update();
		}
	}

	@Override
	public void render() {
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
		update();
		
		timer += Gdx.graphics.getDeltaTime();
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
