package gamedemo;

import org.newdawn.slick.Input;

public class Constants {
	//Level
	public static final char BOX = '$';
	public static final char WALL = '#';
	public static final char PLAYER = '@';
	public static final int GRID_CELL_SIZE = 64;
	
	//Input
	public static final int MOVE_LEFT = Input.KEY_A;
	public static final int MOVE_RIGHT = Input.KEY_D;
	public static final int MOVE_UP = Input.KEY_W;
	public static final int MOVE_DOWN = Input.KEY_S;

	public static final int ZOOM_IN = Input.KEY_ADD;
	public static final int ZOOM_OUT = Input.KEY_MINUS;
	
}
