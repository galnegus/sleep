package com.sleep.text;

import com.badlogic.gdx.math.Vector2;

public enum Direction {
	LEFT("left", new Vector2(-1, 0)), 
	UP("up", new Vector2(0, 1)), 
	RIGHT("right", new Vector2(1, 0)), 
	DOWN("down", new Vector2(0, -1));

	private String name;
	private Vector2 movement;

	private Direction(String name, Vector2 diff) {
		this.name = name;
		this.movement = diff;
	}

	public String toString() {
		return name;
	}
	
	public Vector2 getMovement() {
		return movement;
	}
}
