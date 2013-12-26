package com.sleep.text;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class TerminalInputProcessor implements InputProcessor {
	private Terminal terminal;

	public StringBuilder currentInput;
	private List<String> inputLog;
	private int inputLogIndex = 0;
	private int inputLogMaxSize = 5;

	public TerminalInputProcessor(Terminal terminal) {
		this.terminal = terminal;
		currentInput = new StringBuilder();
		inputLog = new ArrayList<String>();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (terminal.terminalIsActive) {
			// BACKSPACE
			if (keycode == Input.Keys.BACKSPACE && terminal.cursor > 0) {
				currentInput.deleteCharAt(terminal.cursor - 1);
				terminal.cursor--;
			}
			
			// DELETE
			if (keycode == Input.Keys.FORWARD_DEL && terminal.cursor < currentInput.length())
				currentInput.deleteCharAt(terminal.cursor);

			// LEFT/RIGHT
			if (keycode == Input.Keys.LEFT && terminal.cursor > 0)
				terminal.cursor--;
			if (keycode == Input.Keys.RIGHT && terminal.cursor < currentInput.length())
				terminal.cursor++;

			// PG UP/PG DOWN
			if (keycode == Input.Keys.PAGE_UP && terminal.outputLogIndex < terminal.outputLogSize() - 1)
				terminal.outputLogIndex++;
			if (keycode == Input.Keys.PAGE_DOWN && terminal.outputLogIndex > 0)
				terminal.outputLogIndex--;

			// UP/DOWN
			if (keycode == Input.Keys.UP && !inputLog.isEmpty() && inputLogIndex < inputLog.size()) {
				inputLogIndex++;
				currentInput.setLength(0);
				currentInput.append(inputLog.get(inputLog.size() - inputLogIndex));
				terminal.cursor = inputLog.get(inputLog.size() - inputLogIndex).length();
			}
			if (keycode == Input.Keys.DOWN && inputLogIndex > 1) {
				inputLogIndex--;
				currentInput.setLength(0);
				currentInput.append(inputLog.get(inputLog.size() - inputLogIndex));
				terminal.cursor = inputLog.get(inputLog.size() - inputLogIndex).length();
			}

			//ENTER
			if (keycode == Input.Keys.ENTER && currentInput.length() > 0) {
				// add input to inputLog if the last added element isn't repeated
				if (inputLog.size() == 0 || !inputLog.get(inputLog.size() - 1).equals(currentInput.toString())) {
					inputLog.add(currentInput.toString());
				}
				inputLogIndex = 0;

				// remove oldest element if inputLog's size exceeds max size
				if (inputLog.size() > inputLogMaxSize) {
					inputLog.remove(0);
				}

				// send input to terminal, reset input, cursor and outputLogIndex
				terminal.sendInput(currentInput.toString());
				currentInput.setLength(0);
				terminal.cursor = 0;
				terminal.outputLogIndex = 0;
			}
			terminal.cursorActive();
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if (terminal.terminalIsActive) {
			if ((Character.isLetterOrDigit(character) || character == ' ' || character == '-' || character == '_')
					&& currentInput.length() < terminal.getMaxInputLength()) {
				currentInput.insert(terminal.cursor, character);
				terminal.cursor++;
			}
			terminal.cursorActive();
		}
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
