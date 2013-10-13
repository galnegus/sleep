package com.sleep.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.sleep.Constants;
import com.sleep.Entity;
import com.sleep.EntityFactory;
import com.sleep.Sleep;

public class SpawnerComponent extends Component implements RenderableComponent {
	float timer;
	float frequency;

	Vector2 fontOffset = new Vector2(0, 0);

	public SpawnerComponent(float init, float frequency) {
		this.timer = init;
		this.frequency = frequency;
	}

	@Override
	public void render() {
		String out = Integer.toString((int) timer);

		// for some reason, the font is rendered 3 pixels to the left (???)
		fontOffset.x = 3 + ((Constants.GRID_CELL_SIZE - Sleep.font.getBounds(out).width) / 2);
		fontOffset.y = -((Constants.GRID_CELL_SIZE - Sleep.font.getBounds(out).height) / 2);

		Sleep.font.setColor(Color.WHITE);
		Sleep.font.setFixedWidthGlyphs("1");
		Sleep.font.draw(Sleep.batch, out, owner.position.x + fontOffset.x, owner.position.y + Constants.GRID_CELL_SIZE
				+ fontOffset.y);
	}

	@Override
	public void update(float delta) {
		timer -= delta;
		if(timer < 0) {
			Entity ghost = EntityFactory.makeGhost((int) owner.position.x, (int) owner.position.y);
			//Sleep.grid.setEntityAt(ghost, owner.position);
			timer += frequency;
		}

	}

	@Override
	public void init() throws ComponentException {
		// TODO Auto-generated method stub

	}

}
