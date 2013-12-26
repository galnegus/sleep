package com.sleep.text;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public interface Monologue {
	public int getX();

	public int getY();

	public int getWrapWidth();
	
	public boolean isDone();
	
	public boolean isReallyDone();

	public String pollText();
	
	public String pollWords();

	public void render(BitmapFont font);
	
	public void renderDone(BitmapFont font);
}
