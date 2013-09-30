package gamedemo.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public interface RenderableComponent {
	void render(GameContainer gc, StateBasedGame sb, Graphics gr);
}
