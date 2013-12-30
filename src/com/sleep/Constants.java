package com.sleep;

import com.badlogic.gdx.Input;

public class Constants {
	// App
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	// Level Symbols
	public static final char BOX = '$';
	public static final char WALL = '#';
	public static final char PLAYER = '@';
	public static final char GHOST = '.';
	public static final char SPECTRE = ',';
	public static final int GRID_CELL_SIZE = 64;

	// Player behaviour
	public static final float PLAYER_MOVE_FREQUENCY = 0.25f;

	// Enemy behaviour
	public static final float GHOST_MOVE_FREQUENCY = 0.5f;
	public static final float SPECTRE_MOVE_FREQUENCY = 0.5f;
	
	// Input
	public static final int MOVE_LEFT = Input.Keys.LEFT;
	public static final int MOVE_LEFT_ALT = Input.Keys.A;
	public static final int MOVE_RIGHT = Input.Keys.RIGHT;
	public static final int MOVE_RIGHT_ALT = Input.Keys.D;
	public static final int MOVE_UP = Input.Keys.UP;
	public static final int MOVE_UP_ALT = Input.Keys.W;
	public static final int MOVE_DOWN = Input.Keys.DOWN;
	public static final int MOVE_DOWN_ALT = Input.Keys.S;
	
	// Overworld Symbols
	public static final char VERTICAL_DOORWAY = '|';
	public static final char HORIZONTAL_DOORWAY = '-';
	
	// Default fader values
	public static final float FADER_FREQ = 0.05f;
	public static final int FADER_STEPS = 10;
}
