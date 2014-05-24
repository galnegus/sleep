package com.sleep.text;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.sleep.soko.SokoLevel;

public class Room {
	private char id;
	private String name;
	public SokoLevel level = null;
	
	ArrayList<Monologue> monologues;
	private int currentMonologueIndex = 0;
	private boolean anyMonologuesLeft = true;

	public Room(char id, String name, boolean soko, ArrayList<Monologue> monologues) {
		this.id = id;
		this.name = name;
		this.monologues = monologues;
		if (soko)
			level = new SokoLevel("levels/" + id + ".level");
		if (monologues == null)
			anyMonologuesLeft = false;
		
	}
	
	public char getId() {
		return id;
	}
	
	public boolean isCompleted() {
		if (level != null) {
			return level.isCompleted();
		} else {
			Gdx.app.error("Room.java", "Room " + name + "(" + id + ") does not have a level, cannot be completed");
			return false;
		}
	}
	
	public Monologue getCurrentMonologue() {
		return monologues.get(currentMonologueIndex);
	}
	
	public boolean hasMonologues() {
		return anyMonologuesLeft;
	}
	
	public void nextMonologue() {
		currentMonologueIndex++;
		if (monologues == null || currentMonologueIndex >= monologues.size())
			anyMonologuesLeft = false;
	}
	
	public String toString() {
		return "[id: " + id + ", name: " + name + "]";
	}
}
