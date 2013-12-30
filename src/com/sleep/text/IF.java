package com.sleep.text;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sleep.Constants;
import com.sleep.CoolCamera;
import com.sleep.CoolScreen;
import com.sleep.Sleep;

public class IF extends CoolScreen implements InputReceiver {
	private Terminal terminal;
	private OverWorld overWorld;
	private CoolCamera overWorldCamera;
	private Sleep sleep;

	private BitmapFont font;

	private LinkedList<Monologue> monoloQue;

	public IF(Sleep sleep) {
		super(sleep);
		
		overWorld = new OverWorld("levels/dream");
		overWorldCamera = new CoolCamera(Constants.WIDTH, Constants.HEIGHT);
		overWorldCamera.resize(Constants.WIDTH / 2, Constants.HEIGHT / 2, overWorld.player.position.x
				+ (overWorld.player.getWidth() / 2) - Constants.WIDTH / 8, overWorld.player.position.y
				+ (overWorld.player.getHeight() / 2), false);

		font = new BitmapFont(Gdx.files.internal("fonts/Inconsolata36pxbold.fnt"));
		font.setColor(1f, 1f, 1f, 1f);

		terminal = new Terminal(this, font);

		this.sleep = sleep;

		monoloQue = new LinkedList<Monologue>();
	}

	public void update() {
		overWorld.update();
		overWorldCamera.update(Gdx.graphics.getDeltaTime(), overWorld.player.position.x
				+ (overWorld.player.getWidth() / 2) - overWorldCamera.viewportWidth / 4, overWorld.player.position.y
				+ (overWorld.player.getHeight() / 2));
		if (terminal.terminalIsActive)
			terminal.update();
	}

	public void render(float delta) {
		update();

		super.drawLight(overWorldCamera, overWorld);

		// start framebuffer for blurring effect
		Sleep.fboBlurA.begin();
		
		super.drawScene(overWorldCamera, overWorld, overWorld);

		drawInteractiveFiction();
		
		// end framebuffer for blurring
		Sleep.fboBlurA.end();

		super.drawBlur();

		super.render();
	}
	
	public void drawInteractiveFiction() {
		Room room = overWorld.getCurrentRoom();
		while (room.hasMonologues()) {
			monoloQue.add(room.getCurrentMonologue());
			room.nextMonologue();
		}

		Sleep.batch.setProjectionMatrix(Sleep.viewportCamera.combined);
		Sleep.batch.setShader(null);
		Sleep.batch.begin();
		if (!monoloQue.isEmpty()) {
			terminal.fader.fadeOut();
			terminal.terminalIsActive = false;
			Monologue monologue = monoloQue.getFirst();
			if (monologue.isDone() && monologue.continueTriggered()) {
				monologue.postRender(font);
				if (monologue.isReallyDone()) {
					terminal.print(monologue.toString());
					monoloQue.removeFirst();
				}
			} else {
				monologue.render(font);
			}
		} else {
			terminal.terminalIsActive = true;
			terminal.fader.fadeIn();
			terminal.render();
		}
		Sleep.batch.end();
	}

	@Override
	public void receiveInput(String input) {
		if (input.equals("go left") || input.equals("go west"))
			overWorld.movePlayer(Direction.LEFT);
		else if (input.equals("go up") || input.equals("go north"))
			overWorld.movePlayer(Direction.UP);
		else if (input.equals("go right") || input.equals("go east"))
			overWorld.movePlayer(Direction.RIGHT);
		else if (input.equals("go down") || input.equals("go south"))
			overWorld.movePlayer(Direction.DOWN);
		else if (input.equals("clear"))
			terminal.clear();
		else if (input.equals("sleep") && overWorld.getCurrentRoom().level != null) {
			terminal.terminalIsActive = false;
			sleep.sokoDeath.setLevel(overWorld.getCurrentRoom().level);
			screenSwitcher.switchScreen(sleep.sokoDeath);
			// sleep.setScreen(sleep.sokoDeath);
		} else if (input.equals("test")) {
			monoloQue.add(new TyperMonologue("Hedge."));
		}
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
