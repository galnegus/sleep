package com.sleep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EntityManager {
	private List<Entity> entityList;
	
	public EntityManager() {
		entityList = new ArrayList<Entity>();
	}
	
	public void update(float delta) {
		for(int i = 0; i < entityList.size(); i++) {
			entityList.get(i).update(delta);
		}
	}

	public void render(Sleep game) {
		sort();
		for (Entity e : entityList) {
			e.render(game);
		}
	}
	
	private void sort() {
		Collections.sort(entityList, new Comparator<Entity> () {
			public int compare(Entity a, Entity b) {
		        return a.getDepth() - b.getDepth();
		    }
		});
	}
	
	public Entity get(String name) {
		for(Entity e : entityList) {
			if(e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}
	
	public boolean remove(Entity e) {
		return entityList.remove(e);
	}
	
	public Entity add(Entity e) {
		entityList.add(e);
		return e;
	}
}
