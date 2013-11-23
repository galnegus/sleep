package com.sleep;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

public class Sleep extends Game {
	// assets
	public static AssetManager assets;

	// rendering stuff
	public static SpriteBatch batch;
	public static BitmapFont spawnerFont;
	public static Music music;
	public static FrameBuffer fbo;
	
	// screens
	private GhostScreen ghostScreen;
	private TextScreen textScreen;

	// shader stuff
	public static ShaderProgram ambientShader;
	public static ShaderProgram defaultShader;
	public static final float ambientIntensity = 1f;
	public static final Vector3 ambientColor = new Vector3(0f, 0f, 0f);
	public static Texture light;
	
	/**
	 * WARNING: Game might break (NullPointerException) if objects are created in
	 * a stupid order
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
		assets.load("images/cursor.png", Texture.class);
		assets.load("images/room_bw.png", Texture.class);
		assets.load("images/horizontal_bw.png", Texture.class);
		assets.load("images/vertical_bw.png", Texture.class);
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
		
		// create objects for rendering stuff
		spawnerFont = new BitmapFont(Gdx.files.internal("fonts/24pt.fnt"));
		batch = new SpriteBatch();
		fbo = new FrameBuffer(Format.RGBA8888, Constants.WIDTH, Constants.HEIGHT, false);

		// play music
		music = assets.get("music/spook2.ogg", Music.class);
		music.setLooping(true);
		music.setVolume(1f);
//		music.play();
		
		// create screens
		ghostScreen = new GhostScreen();
		textScreen = new TextScreen();
		
		setScreen(textScreen);
	}

	@Override
	public void resize(int width, int height) {
		fbo = new FrameBuffer(Format.RGBA8888, width, height, false);

		ambientShader.begin();
		ambientShader.setUniformf("resolution", width, height);
		ambientShader.end();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		batch.dispose();
		spawnerFont.dispose();
		music.dispose();
		fbo.dispose();
		ambientShader.dispose();
		defaultShader.dispose();
		light.dispose();
	}

}
