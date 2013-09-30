package gamedemo.component.sound;

import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;

import gamedemo.component.Component;
import gamedemo.component.ComponentException;

public class SoundComponent extends Component{
	HashMap<String, Sound> soundMap = new HashMap<String, Sound>();

	@Override
	public void init() throws ComponentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		// TODO Auto-generated method stub
		
	}

}
