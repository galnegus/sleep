package com.sleep.text;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class TerminalInputProcessor implements InputProcessor {
	private Terminal terminal;

	public StringBuilder currentInput;

	public TerminalInputProcessor(Terminal terminal) {
		this.terminal = terminal;
		currentInput = new StringBuilder();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.BACKSPACE && terminal.cursor > 0) {
			currentInput.deleteCharAt(terminal.cursor - 1);
			terminal.cursor--;
		}
		if (keycode == Input.Keys.FORWARD_DEL && terminal.cursor < currentInput.length()) {
			currentInput.deleteCharAt(terminal.cursor);
		}
		if (keycode == Input.Keys.LEFT && terminal.cursor > 0)
			terminal.cursor--;
		if (keycode == Input.Keys.RIGHT && terminal.cursor < currentInput.length())
			terminal.cursor++;
		if (keycode == Input.Keys.ENTER && currentInput.length() > 0) {
			terminal.sendInput(currentInput.toString());
			currentInput.setLength(0);
			terminal.cursor = 0;
		}
		terminal.cursorActive();
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if ((Character.isLetterOrDigit(character) || character == ' ' || character == '-' || character == '_')
				&& currentInput.length() < terminal.getMaxInputLength()) {
			currentInput.insert(terminal.cursor, character);
			terminal.cursor++;
		}
		terminal.cursorActive();
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
