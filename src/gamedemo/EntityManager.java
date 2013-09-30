package gamedemo;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class EntityManager {
	protected List<Entity> entityList;
	
	public EntityManager() {
		entityList = new ArrayList<Entity>();
	}
	
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
		for(int i = 0; i < entityList.size(); i++) {
			entityList.get(i).update(gc, sb, delta);
		}
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics gr)
			throws SlickException {
		for (Entity e : entityList) {
			e.render(gc, sb, gr);
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
