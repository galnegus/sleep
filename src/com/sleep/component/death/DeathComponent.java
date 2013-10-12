package com.sleep.component.death;

import com.sleep.GameScreen;
import com.sleep.component.Component;
import com.sleep.component.ComponentException;

public class DeathComponent extends Component {
	
	public void die() {
		// oh no
		GameScreen.entityManager.remove(owner);
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void init() throws ComponentException {
		
	}

}
