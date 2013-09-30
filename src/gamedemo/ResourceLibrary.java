package gamedemo;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ResourceLibrary {		
		public static Animation playerIdleAnimation() throws SlickException {
			Image[] anim = { new Image("/images/player.png") };
			return new Animation(anim, 100);
		}
		
		public static Image grid() throws SlickException {
			return new Image("/images/grid.png");
		}
		
		public static Image box() throws SlickException {
			return new Image("/images/box.png");
		}
		
		public static Image wall() throws SlickException {
			return new Image("/images/wall.png");
		}
}
