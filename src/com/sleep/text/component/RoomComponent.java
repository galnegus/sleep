package com.sleep.text.component;

import com.sleep.soko.component.Component;
import com.sleep.soko.component.ComponentException;
import com.sleep.soko.component.Message;
import com.sleep.text.Room;

public class RoomComponent extends Component {
	private Room room;
	
	public RoomComponent(Room room) {
		this.room = room;
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
