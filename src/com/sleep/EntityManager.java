package com.sleep;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EntityManager {
	private List<Entity> entityList;
	private ArrayDeque<Entity> removeList;

	public EntityManager() {
		entityList = new ArrayList<Entity>();
		removeList = new ArrayDeque<Entity>();
	}

	public void update() {
		for (int i = 0; i < entityList.size(); i++) {
			entityList.get(i).update();
		}
		
		Entity entity;
		while ((entity = removeList.poll()) != null) {
			entityList.remove(entity);
		}
	}

	public void render() {
		sort();
		for (Entity e : entityList) {
			e.render();
		}
	}
	
	public void drawLight() {
		for (Entity e : entityList) {
			e.drawLight();
		}
	}
	
	public void bindLight(int i) {
		for (Entity e : entityList) {
			e.bindLight(i);
		}
	}

	private void sort() {
		Collections.sort(entityList, new Comparator<Entity>() {
			public int compare(Entity a, Entity b) {
				return b.getDepth() - a.getDepth();
			}
		});
	}

	public List<Entity> get(String name) {
		List<Entity> ret = new ArrayList<Entity>();
		for (Entity e : entityList) {
			if (e.getName().equals(name)) {
				ret.add(e);
			}
		}

		return ret;
	}

	public void remove(Entity e) {
		removeList.add(e);
	}

	public Entity add(Entity e) {
		entityList.add(e);
		return e;
	}
	
	public int size() {
		return entityList.size();
	}
}
