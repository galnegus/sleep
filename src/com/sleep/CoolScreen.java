package com.sleep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

public abstract class CoolScreen implements Screen {
	public CoolScreenSwitcher screenSwitcher;

	public CoolScreen(Sleep sleep) {
		screenSwitcher = new CoolScreenSwitcher(sleep);
	}

	/**
	 * Renders a lightmap in the fboLight framebuffer by calling
	 * LightSource.drawLight().
	 * 
	 * Must be called before calling drawScene(), or there will only be
	 * darkness (no lightmap).
	 */
	protected void drawLight(OrthographicCamera camera, LightSource lightSource) {
		// draw light to FBO
		Sleep.fboLight.begin();
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Sleep.batch.setProjectionMatrix(camera.combined);
		Sleep.batch.setShader(null);
		Sleep.batch.begin();
		lightSource.drawLight();
		Sleep.batch.end();
		Sleep.fboLight.end();
	}

	/**
	 * Renders any object in the renderer and applies the lightmap previously
	 * rendered in the fboLight framebuffer by drawLight() through some shader
	 * stuff.
	 */
	protected void drawScene(OrthographicCamera camera, LightSource lightSource, Renderer renderer) {
		// draw scene
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Sleep.batch.setProjectionMatrix(camera.combined);
		Sleep.batch.setShader(Sleep.ambientShader);
		Sleep.batch.begin();
		Sleep.fboLight.getColorBufferTexture().bind(1);
		lightSource.bindLight(0);
		renderer.render();
		Sleep.batch.end();

	}

	/**
	 * Before calling drawBlur(), drawScene() must be called following a call to
	 * Sleep.fboBlurA.begin(), followed by a call to Sleep.fboBlurA().end()
	 * 
	 * * I.e, the thing that is being blurred needs to be inside the fboBlurA
	 * framebuffer.
	 * 
	 * IMPORTANT: Before beginning the fboBlurA framebuffer, drawLight() must be
	 * called.
	 * 
	 * To summarize:
	 * 1. drawLight().
	 * 2. fboBlurA.begin().
	 * 3. drawScene().
	 * 4. render some other stuff if you feel like it.
	 * 5. fboBlurA.end().
	 * 6. drawBlur().
	 */
	protected void drawBlur() {
		Sleep.fboBlurB.begin();
		Sleep.batch.setProjectionMatrix(Sleep.viewportCamera.combined);
		Sleep.batch.begin();
		Sleep.batch.setShader(Sleep.blurShader);
		Sleep.blurShader.setUniformf("dir", 0f, 1f);
		Sleep.blurShader.setUniformf("radius", Sleep.blurRadius);
		Sleep.fboRegion.setTexture(Sleep.fboBlurA.getColorBufferTexture());
		Sleep.batch.draw(Sleep.fboRegion, 0, 0);
		Sleep.batch.flush();
		Sleep.fboBlurB.end();

		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Sleep.blurShader.setUniformf("dir", 1f, 0f);
		Sleep.blurShader.setUniformf("radius", Sleep.blurRadius);
		Sleep.fboRegion.setTexture(Sleep.fboBlurB.getColorBufferTexture());
		Sleep.batch.draw(Sleep.fboRegion, 0, 0);
		Sleep.batch.flush();
		Sleep.batch.end();
		Sleep.batch.setShader(null);
	}
	
	public void render() {
		screenSwitcher.render();
	}

}
