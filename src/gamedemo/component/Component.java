package gamedemo.component;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import gamedemo.Entity;

public abstract class Component {
	protected Entity owner;
	
	public void setOwnerEntity(Entity owner) {
		this.owner = owner;
	}
	
	public abstract void update(GameContainer gc, StateBasedGame sb, int delta);
	
	public abstract void init() throws ComponentException;
	
	public <T extends Component> T getDependency(Class<? extends Component> familyClass, Class<T> returnClass) throws ComponentException {
		Component returnObj = owner.getComponent(familyClass);
		
		if (returnObj != null && returnClass.isInstance(returnObj)) {
			return returnClass.cast(returnObj);
		} else {
			throw new ComponentException(returnClass.getSimpleName() + " missing from Entity!");
		}
	}
}
