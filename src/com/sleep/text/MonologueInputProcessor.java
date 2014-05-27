package com.sleep.text;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class MonologueInputProcessor implements InputProcessor {
	Monologue activeMonologue = null;
	
	public void setActiveMonologue(Monologue activeMonologue) {
		this.activeMonologue = activeMonologue;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.ENTER) {
			if (activeMonologue != null) {
				if (!activeMonologue.introIsDone) {
					activeMonologue.introIsDone = true;
				} else if (!activeMonologue.continueTriggered) {
					activeMonologue.continueTriggered = true;
				}/* else if (!activeMonologue.outroIsDone) {
					activeMonologue.outroIsDone = true;
				}*/ // maybe, maybe not
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
