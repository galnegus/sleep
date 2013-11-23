package com.sleep.soko.component.death;

import com.sleep.soko.Level;

public class PlayerDeathComponent extends DeathComponent {
	public PlayerDeathComponent(Level level) {
		super(level);
	}

	public void die() {
		System.out.println("game over");
		super.die();
	}
}
