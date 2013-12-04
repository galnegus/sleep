package com.sleep.soko.component;

import com.badlogic.gdx.Gdx;

public enum Message {
	ENTITY_DEATH("entity death"), MOVE_LEFT("move left"), MOVE_UP("move up"), MOVE_RIGHT("move right"), MOVE_DOWN("move down");
	
	private String name;
	
	private Message(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
	
	public static Message fromString(String string) {
		if (string != null) {
			for (Message message : Message.values()) {
				if (message.toString().equals(string)) {
					return message;
				}
			}
		}
		Gdx.app.error("MessageFromStringError", "invalid message enum given");
		return null;
	}
}
