package com.sleep.text;

import com.sleep.soko.component.Message;

public class IF implements InputReader {
	private Terminal terminal;
	private OverWorld overWorld;
	
	public IF(OverWorld overWorld) {
		terminal = new Terminal(this);
		this.overWorld = overWorld;
	}
	
	public void update() {
		terminal.update();
	}
	
	public void render() {
		terminal.render();
	}

	@Override
	public void receiveInput(String input) {
		if (input.equals("go left")) {
			overWorld.player.sendMessage(Message.MOVE_LEFT);
		} else if (input.equals("go up")) {
			overWorld.player.sendMessage(Message.MOVE_UP);
		} else if (input.equals("go right")) {
			overWorld.player.sendMessage(Message.MOVE_RIGHT);
		} else if (input.equals("go down")) {
			overWorld.player.sendMessage(Message.MOVE_DOWN);
		}
	}
	
}
