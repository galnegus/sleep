package com.sleep.soko;

import com.badlogic.gdx.Gdx;
import com.sleep.Entity;
import com.sleep.EntityManager;
import com.sleep.LightSource;
import com.sleep.Renderer;
import com.sleep.Sleep;

public class SokoLevel implements LightSource, Renderer {
	private Sleep sleep = null;
	
	public EntityManager entityManager;
	public EntityManager backgroundManager;
	
	public CollisionGrid collisionGrid;
	
	public Entity player;
	
	private boolean update = true;
	private boolean complete = false;

	public int columnCount() {
		return collisionGrid.columnCount();
	}

	public int rowCount() {
		return collisionGrid.rowCount();
	}

	public SokoLevel(String filename) {
		
		entityManager = new EntityManager();
		backgroundManager = new EntityManager();

		SokoLevelParser parser = new SokoLevelParser(filename, this);
		collisionGrid = new CollisionGrid(parser);
		player = parser.getPlayer();
	}
	
	public void levelComplete() {
		if (sleep == null) {
			Gdx.app.error(this.getClass().getName(), "Sleep has not been set, cannot switch screen!");
		} else {
			sleep.sokoDeath.switchScreen(sleep.interactiveFiction);
			complete = true;
		}
	}
	
	public void setSleep(Sleep sleep) {
		if (this.sleep == null) {
			this.sleep = sleep;
		}
	}
	
	public boolean isCompleted() {
		return complete;
	}

	public void update() {
		if (update) {
			collisionGrid.updateGhostPathGrid();
			collisionGrid.updateSpectrePathGrid();

			entityManager.update();
		}
	}

	public void render() {
		backgroundManager.render();
		entityManager.render();
	}

	public void drawLight() {
		entityManager.drawLight();
	}

	public void bindLight(int i) {
		entityManager.bindLight(i);
	}
}
