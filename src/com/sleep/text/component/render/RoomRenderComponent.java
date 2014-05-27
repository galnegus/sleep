package com.sleep.text.component.render;

import com.badlogic.gdx.graphics.Texture;
import com.sleep.Sleep;
import com.sleep.soko.component.ComponentException;
import com.sleep.soko.component.Message;
import com.sleep.soko.component.render.RenderComponent;
import com.sleep.text.Room;

public class RoomRenderComponent extends RenderComponent {
	protected Texture image;
	private Room room;
	
	public RoomRenderComponent(Texture image, Room room) {
		this.image = image;
		this.room = room;
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	@Override
	public void render() {
//		Color c = Sleep.batch.getColor();
//		
//		if (!room.isCompleted()) {
//			Sleep.batch.setColor(Color.GREEN);
//		}
		
		Sleep.batch.draw(image, owner.position.x, owner.position.y);
		
//		Sleep.batch.setColor(c);
	}
	
	@Override
	public void update() {
	}

	@Override
	public void init() throws ComponentException {
	}

	@Override
	public void receiveMessage(Message message) {
		// TODO Auto-generated method stub
		
	}
}