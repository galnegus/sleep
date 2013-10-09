package com.sleep;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
	protected List<Entity> entityList;
	
	public EntityManager() {
		entityList = new ArrayList<Entity>();
	}
	
	public void update(float delta) {
		for(int i = 0; i < entityList.size(); i++) {
			entityList.get(i).update(delta);
		}
	}

	public void render(Sleep game) {
		for (Entity e : entityList) {
			e.render(game);
		}
	}
	
	public List<Entity> getEntityList() {
		return entityList;
	}
	
	public Entity getEntity(String name) {
		for(Entity e : entityList) {
			if(e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}
	
	public Entity add(Entity e) {
		entityList.add(e);
		return e;
	}
}
