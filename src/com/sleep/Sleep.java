package com.sleep;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

public class Sleep implements ApplicationListener {
	// object/entity stuff
	public static AssetManager assets;
	public static World world;

	public static boolean updatesPaused = false;

	// rendering stuff
	public static SpriteBatch batch;
	public static BitmapFont font;
	public static Music music;
	public static FrameBuffer fbo;
	public static CoolCamera camera;

	// shader stuff
	public static ShaderProgram ambientShader;
	public static ShaderProgram defaultShader;
	public static final float ambientIntensity = 1f;
	public static final Vector3 ambientColor = new Vector3(0.1f, 0.1f, 0.1f);
	public static Texture light;

	/**
	 * WARNING: Game will break (NullPointerException) if objects are created in
	 * a stupid order
	 * 
	 * This approximate order needs to be used:
	 * load assets -> shaders -> entities/grid -> camera
	 **/
	@Override
	public void create() {
		// load assets
		assets = new AssetManager();
		assets.load("images/light.png", Texture.class);
		assets.load("images/dark_circle.png", Texture.class);
		assets.load("images/wall_bw.png", Texture.class);
		assets.load("images/box_bw.png", Texture.class);
		assets.load("images/grid_bw.png", Texture.class);
		assets.load("images/player.png", Texture.class);
		assets.load("images/player_bw.png", Texture.class);
		assets.load("images/placeholder.png", Texture.class);
		assets.load("music/spook2.ogg", Music.class);
		assets.finishLoading();

		// initialize light shader texture
		light = assets.get("images/light.png", Texture.class);

		ShaderProgram.pedantic = false;
		ambientShader = new ShaderProgram(Gdx.files.internal("shaders/vertexShader.glsl"),
				Gdx.files.internal("shaders/ambientPixelShader.glsl"));
		defaultShader = new ShaderProgram(Gdx.files.internal("shaders/vertexShader.glsl"),
				Gdx.files.internal("shaders/defaultPixelShader.glsl"));
		ambientShader.begin();
		ambientShader.setUniformi("u_lightmap", 1); // texture binding to slot 1
		ambientShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y, ambientColor.z, ambientIntensity);
		ambientShader.end();

		// create entities/grid
		world = new World("levels/world");

		// create objects for rendering stuff
		camera = new CoolCamera(Constants.WIDTH, Constants.HEIGHT);
		font = new BitmapFont(Gdx.files.internal("fonts/24pt.fnt"));
		batch = new SpriteBatch();
		fbo = new FrameBuffer(Format.RGBA8888, Constants.WIDTH, Constants.HEIGHT, false);

		// play music
		music = assets.get("music/spook2.ogg", Music.class);
		music.setLooping(true);
		music.setVolume(1f);
		music.play();
	}

	@Override
	public void resize(int width, int height) {
		fbo = new FrameBuffer(Format.RGBA8888, width, height, false);

		ambientShader.begin();
		ambientShader.setUniformf("resolution", width, height);
		ambientShader.end();
		
		camera.resize(width, height);
	}

	public void update() {
		if (!updatesPaused) {
			world.update();
			camera.update(Gdx.graphics.getDeltaTime());
		}
	}

	@Override
	public void render() {
		// update stuff
		update();

		// clear screen color
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);

		// draw light to FBO
		fbo.begin();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.setShader(defaultShader);
		batch.begin();
		world.drawLight();
		batch.end();
		fbo.end();

		// draw scene
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.setShader(ambientShader);
		batch.begin();
		fbo.getColorBufferTexture().bind(1);
		world.bindLight(0);
		world.render();
		batch.end();
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
