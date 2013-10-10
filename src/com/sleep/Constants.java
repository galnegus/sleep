package com.sleep;

import com.badlogic.gdx.Input;

public class Constants {
	//Level
	public static final char BOX = '$';
	public static final char WALL = '#';
	public static final char PLAYER = '@';
	public static final char GHOST = 'x';
	public static final int GRID_CELL_SIZE = 64;
	
	//Player behaviour
	public static final float PLAYER_MOVE_FREQUENCY = 0.25f;
	
	//Ghost behaviour
	public static final float GHOST_MOVE_FREQUENCY = 0.8f;
	
	//Input
	public static final int MOVE_LEFT = Input.Keys.A;
	public static final int MOVE_RIGHT = Input.Keys.D;
	public static final int MOVE_UP = Input.Keys.W;
	public static final int MOVE_DOWN = Input.Keys.S;

	public static final int ZOOM_IN = Input.Keys.PLUS;
	public static final int ZOOM_OUT = Input.Keys.MINUS;
	
	public static final int ROTATE_LEFT = Input.Keys.Q;
	public static final int ROTATE_RIGHT = Input.Keys.E;
	
}