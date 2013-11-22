package com.sleep.ghost.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.sleep.Constants;
import com.sleep.Sleep;
import com.sleep.ghost.EntityMaker;
import com.sleep.ghost.Message;

public class SpawnerComponent extends Component implements RenderableComponent {
	String type;
	float timer;
	float frequency;
	
	Color red;

	Vector2 fontOffset = new Vector2(0, 0);

	public SpawnerComponent(String type, float init, float frequency) {
		this.type = type;
		this.timer = init;
		this.frequency = frequency;
		
		this.red = new Color(1f, 64f/255f, 108f/255f, 1f);
	}

	@Override
	public void render() {
		String out = Integer.toString((int) timer);

		// for some reason, the font is rendered 3 pixels to the left (???)
		fontOffset.x = 3 + ((Constants.GRID_CELL_SIZE - Sleep.spawnerFont.getBounds(out).width) / 2);
		fontOffset.y = -((Constants.GRID_CELL_SIZE - Sleep.spawnerFont.getBounds(out).height) / 2);

		if (timer < 4) {
			Sleep.spawnerFont.setColor(red);
		} else {
			Sleep.spawnerFont.setColor(Color.WHITE);
		}
		
		Sleep.spawnerFont.setFixedWidthGlyphs("1");
		Sleep.spawnerFont.draw(Sleep.batch, out, owner.position.x + fontOffset.x, owner.position.y + Constants.GRID_CELL_SIZE
				+ fontOffset.y);
	}

	@Override
	public void update() {
		timer -= Gdx.graphics.getRawDeltaTime();
		if(timer < 0) {
			if(type.equals("Ghost")) {
				EntityMaker.makeGhost(Sleep.world.activeLevel.entityManager, (int) owner.position.x, (int) owner.position.y);
			} else if(type.equals("Spectre")) {
				EntityMaker.makeSpectre(Sleep.world.activeLevel.entityManager, (int) owner.position.x, (int) owner.position.y);
			}
			timer += frequency;
		}

	}

	@Override
	public void init() throws ComponentException {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveMessage(Message message) {
		// TODO Auto-generated method stub
		
	}
}
