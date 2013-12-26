package com.sleep.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.sleep.Sleep;

public class IF implements InputReceiver {
	private Terminal terminal;
	private OverWorld overWorld;
	private Sleep sleep;
	
	private BitmapFont font;
	
	public IF(Sleep sleep, OverWorld overWorld) {
		font = new BitmapFont(Gdx.files.internal("fonts/Inconsolata36pxbold.fnt"));
		font.setColor(1f, 1f, 1f, 1f);
		
		terminal = new Terminal(this, font);
		
		this.sleep = sleep;
		this.overWorld = overWorld;
	}

	public void update() {
		if (terminal.terminalIsActive)
			terminal.update();
	}

	public void render() {
		Room room = overWorld.getCurrentRoom();
		if (room.hasMonologues()) {
			terminal.terminalIsActive = false;
			Monologue monologue = room.getCurrentMonologue();
			if (monologue.isDone() && Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
				monologue.renderDone(font);
				if (monologue.isReallyDone()) {
					terminal.print(monologue.toString());
					room.nextMonologue();
				}
			} else {
				monologue.render(font);
			}
		} else {
			terminal.terminalIsActive = true;
			terminal.render();
		}
		
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
			sleep.ghostScreen.setLevel(overWorld.getCurrentRoom().level);
			sleep.setScreen(sleep.ghostScreen);
		}
	}

}
