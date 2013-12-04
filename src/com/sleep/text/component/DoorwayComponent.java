package com.sleep.text.component;

import java.util.HashMap;
import java.util.Map;

import com.sleep.soko.component.Component;
import com.sleep.soko.component.ComponentException;
import com.sleep.soko.component.Message;
import com.sleep.text.Room;

public class DoorwayComponent extends Component {
	private Map<String, Room> rooms;
	
	public DoorwayComponent(HashMap<String, Room> rooms) {
		this.rooms = rooms;
	}

	@Override
	public void update() {
	}

	@Override
	public void init() throws ComponentException {
	}

	@Override
	public void receiveMessage(Message message) {
	}
}
