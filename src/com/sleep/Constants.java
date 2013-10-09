package com.sleep;

import com.badlogic.gdx.Input;

public class Constants {
	//Level
	public static final char BOX = '$';
	public static final char WALL = '#';
	public static final char PLAYER = '@';
	public static final int GRID_CELL_SIZE = 64;
	
	//Input
	public static final int MOVE_LEFT = Input.Keys.A;
	public static final int MOVE_RIGHT = Input.Keys.D;
	public static final int MOVE_UP = Input.Keys.W;
	public static final int MOVE_DOWN = Input.Keys.S;

	public static final int ZOOM_IN = Input.Keys.PLUS;
	public static final int ZOOM_OUT = Input.Keys.MINUS;
	
}
