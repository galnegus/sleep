package com.sleep.text.object;

import java.util.HashMap;
import java.util.Map;

public class Room {
	private char id;
	private String name;
	
	private Map<String, Room> connections;

	public Room(char id, String name) {
		this.id = id;
		this.name = name;
		connections = new HashMap<String, Room>();
	}
	
	public void addConnection(String direction, Room destination) {
		connections.put(direction, destination);
	}
}
