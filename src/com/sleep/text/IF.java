package com.sleep.text;

public class IF implements InputReceiver {
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
	}

}
