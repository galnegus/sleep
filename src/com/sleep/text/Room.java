package com.sleep.text;

import java.util.HashMap;
import java.util.Map;

public class Room {
	private char id;
	private String name;
	
	private Map<Direction, Doorway> doorways;

	public Room(char id, String name) {
		this.id = id;
		this.name = name;
		doorways = new HashMap<Direction, Doorway>();
	}
	
	public void addDoorway(Direction direction, Doorway doorway) {
//		doorways.put(direction, destination);
	}
	
	public boolean doorway(Direction direction) {
		return false;
	}
	
	public String toString() {
		return "[id: " + id + ", name: " + name + "]";
	}
}
