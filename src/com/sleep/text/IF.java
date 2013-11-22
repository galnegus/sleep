package com.sleep.text;

public class IF implements InputReader {
	private Terminal terminal;
	
	public IF() {
		terminal = new Terminal(this);
	}
	
	public void update() {
		terminal.update();
	}
	
	public void render() {
		terminal.render();
	}

	@Override
	public void receiveInput(String input) {
		// doms ething sdcool
	}
	
}
