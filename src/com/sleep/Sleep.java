package com.sleep;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.sleep.soko.Soko;
import com.sleep.text.IF;

public class Sleep extends Game {
	// assets
	public static AssetManager assets;

	// rendering stuff
	public static SpriteBatch batch;
	public static ShapeRenderer shapeRenderer;
	public static BitmapFont spawnerFont;
	public static Music music;

	public static FrameBuffer fboLight;
	public static FrameBuffer fboBlurA;
	public static FrameBuffer fboBlurB;

	public static TextureRegion fboRegion;

	public static OrthographicCamera viewportCamera;

	// screens
	public Soko sokoDeath;
	public IF interactiveFiction;

	// shader stuff
	public static ShaderProgram ambientShader;
	public static final float ambientIntensity = 1f;
	public static final Vector3 ambientColor = new Vector3(0f, 0f, 0f);
	public static Texture light;

	public static ShaderProgram blurShader;
	public static float blurRadius = 0f;

	// input thingy
	public static InputMultiplexer inputMultiplexer;

	/**
	 * WARNING: Game might break (NullPointerException) if objects are created
	 * in a stupid order
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
		assets.load("images/cursor2.png", Texture.class);
		assets.load("images/room_bw.png", Texture.class);
		assets.load("images/horizontal_bw.png", Texture.class);
		assets.load("images/vertical_bw.png", Texture.class);
		assets.load("images/exit1.png", Texture.class);
		assets.load("images/exit2.png", Texture.class);
		assets.load("images/exit3.png", Texture.class);
		assets.load("music/spook2.ogg", Music.class);
		assets.finishLoading();

		// initialize light shader texture
		light = assets.get("images/light.png", Texture.class);

		ShaderProgram.pedantic = false;

		// ambient light shader
		ambientShader = new ShaderProgram(Gdx.files.internal("shaders/vertexShader.vsh"),
				Gdx.files.internal("shaders/ambientPixelShader.fsh"));

		if (!ambientShader.isCompiled()) {
			System.err.println(ambientShader.getLog());
			System.exit(0);
		}
		if (ambientShader.getLog().length() != 0)
			System.out.println(ambientShader.getLog());

		ambientShader.begin();
		ambientShader.setUniformi("u_lightmap", 1); // texture binding to slot 1
		ambientShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y, ambientColor.z, ambientIntensity);
		ambientShader.end();

		// blur shader
		blurShader = new ShaderProgram(Gdx.files.internal("shaders/vertexShader.vsh"),
				Gdx.files.internal("shaders/blurPixelShader.fsh"));

		if (!blurShader.isCompiled()) {
			System.err.println(blurShader.getLog());
			System.exit(0);
		}
		if (blurShader.getLog().length() != 0)
			System.out.println(blurShader.getLog());

		blurShader.begin();
		blurShader.setUniformf("dir", 0f, 0f); // direction of blur; nil for now
		blurShader.setUniformf("resolution", Constants.WIDTH); // size of FBO
		blurShader.setUniformf("radius", 1f); // radius of blur
		blurShader.end();

		// create objects for rendering stuff
		spawnerFont = new BitmapFont(Gdx.files.internal("fonts/24pt.fnt"));
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		fboLight = new FrameBuffer(Format.RGBA8888, Constants.WIDTH, Constants.HEIGHT, false);
		fboBlurA = new FrameBuffer(Format.RGBA8888, Constants.WIDTH, Constants.HEIGHT, false);
		fboBlurB = new FrameBuffer(Format.RGBA8888, Constants.WIDTH, Constants.HEIGHT, false);

		fboRegion = new TextureRegion(fboBlurA.getColorBufferTexture());
		fboRegion.flip(false, true);

		viewportCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		viewportCamera.setToOrtho(false);

		// play music
		music = assets.get("music/spook2.ogg", Music.class);
		music.setLooping(true);
		music.setVolume(1f);
		// music.play();
		
		// create input multiplexer
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);

		// create screens
		sokoDeath = new Soko(this);
		interactiveFiction = new IF(this);

		setScreen(interactiveFiction);
	}

	@Override
	public void resize(int width, int height) {
		fboLight = new FrameBuffer(Format.RGBA8888, width, height, false);
		fboBlurA = new FrameBuffer(Format.RGBA8888, width, height, false);
		fboBlurB = new FrameBuffer(Format.RGBA8888, width, height, false);

		ambientShader.begin();
		ambientShader.setUniformf("resolution", width, height);
		ambientShader.end();

		blurShader.begin();
		blurShader.setUniformf("resolution", width);
		blurShader.begin();
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
		fboLight.dispose();
		ambientShader.dispose();
		light.dispose();
	}

}
