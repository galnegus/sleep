package com.sleep.soko.component.death;

import com.sleep.soko.SokoLevel;

public class PlayerDeathComponent extends DeathComponent {
	public PlayerDeathComponent(SokoLevel level) {
		super(level);
	}

	public void die() {
		System.out.println("game over");
		super.die();
	}
}
