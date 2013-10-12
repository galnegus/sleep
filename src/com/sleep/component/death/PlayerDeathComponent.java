package com.sleep.component.death;

import com.sleep.GameScreen;

public class PlayerDeathComponent extends DeathComponent{
	public void die() {
		System.out.println("game over");
		super.die();
	}
}
