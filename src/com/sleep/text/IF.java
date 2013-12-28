package com.sleep.text;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sleep.Sleep;

public class IF implements InputReceiver, Screen {
	private Terminal terminal;
	private OverWorld overWorld;
	private Sleep sleep;

	private BitmapFont font;

	private LinkedList<Monologue> monoloque;

	public IF(Sleep sleep) {
		overWorld = new OverWorld("levels/dream");

		font = new BitmapFont(Gdx.files.internal("fonts/Inconsolata36pxbold.fnt"));
		font.setColor(0.8f, 0.8f, 0.8f, 1f);

		terminal = new Terminal(this, font);

		this.sleep = sleep;

		monoloque = new LinkedList<Monologue>();
	}

	public void update() {
		overWorld.update();
		if (terminal.terminalIsActive)
			terminal.update();
	}

	public void render(float delta) {
		update();

		overWorld.render();

		Sleep.batch.setProjectionMatrix(Sleep.viewportCamera.combined);
		Sleep.batch.setShader(null);
		Sleep.batch.begin();
		Room room = overWorld.getCurrentRoom();
		while (room.hasMonologues()) {
			monoloque.add(room.getCurrentMonologue());
			room.nextMonologue();
		}

		if (!monoloque.isEmpty()) {
			terminal.terminalIsActive = false;
			terminal.batchFader.goDark();
			terminal.fontFader.goDark();
			Monologue monologue = monoloque.getFirst();
			if (monologue.isDone() && Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
				monologue.renderDone(font);
				if (monologue.isReallyDone()) {
					terminal.print(monologue.toString());
					monoloque.removeFirst();
				}
			} else {
				monologue.render(font);
			}
		} else {
			terminal.terminalIsActive = true;
			terminal.batchFader.fadeIn();
			terminal.fontFader.fadeIn();
			terminal.render();
		}
		Sleep.batch.end();

	}

	@Override
	public void receiveInput(String input) {
		if (input.equals("go left"))
			overWorld.movePlayer(Direction.LEFT);
		else if (input.equals("go up"))
			overWorld.movePlayer(Direction.UP);
		else if (input.equals("go right"))
			overWorld.movePlayer(Direction.RIGHT);
		else if (input.equals("go down"))
			overWorld.movePlayer(Direction.DOWN);
		else if (input.equals("clear"))
			terminal.clear();
		else if (input.equals("kill") && overWorld.getCurrentRoom().level != null) {
			terminal.terminalIsActive = false;
			sleep.sokoDeath.setLevel(overWorld.getCurrentRoom().level);
			sleep.setScreen(sleep.sokoDeath);
		} else if (input.equals("test")) {
			monoloque.add(new DefaultMonologue("Hedge."));
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
