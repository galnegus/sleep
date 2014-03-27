package com.sleep.soko;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Tobias
 *
 * Checks if player is positioned on specified "exit coordinates"
 */
public class WinExit implements Win {
	private Vector2 position;
	
	public WinExit() {
		// do nothing
	}
	
	public void setPosition(float x, float y) {
		if (this.position == null)
			this.position = new Vector2(x, y);
		else
			Gdx.app.error("WinExit", "winCondition.position already set!");
	}
	
	public float getX() {
		return position.x;
	}
	
	public float getY() {
		return position.y;
	}
	
	@Override
	public boolean winConditionMet() {
		// TODO Auto-generated method stub
		return false;
	}

}
