package com.sleep.text;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class MonologueInputProcessor implements InputProcessor {
	// the monologue that is being progressed
	Monologue monologue = null;
	
	// the monologue that was active when the enter key was pressed down
	Monologue monologueAtEnterDown = null;
	
	public void setActiveMonologue(Monologue activeMonologue) {
		this.monologue = activeMonologue;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.ENTER) {
			monologueAtEnterDown = monologue;		
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.ENTER) {
			if (monologue != null && monologue == monologueAtEnterDown) {
				if (!monologue.introIsDone) {
					monologue.introIsDone = true;
				} else if (!monologue.continueTriggered) {
					monologue.continueTriggered = true;
				}
			}			
		}
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
