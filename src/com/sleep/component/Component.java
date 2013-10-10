package com.sleep.component;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.sleep.Entity;

public abstract class Component {
	protected Entity owner;
	
	public void setOwnerEntity(Entity owner) {
		this.owner = owner;
	}
	
	public abstract void update(float delta);
	
	public abstract void init() throws ComponentException;
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T getDependency(Class<? extends Component> familyClass, Class<T> returnClass) throws ComponentException {
		Component returnObj = owner.getComponent(familyClass);
		
		if (returnObj != null && ClassReflection.isInstance(returnClass, returnObj)) {
			return (T) returnObj;
		} else {
			throw new ComponentException(ClassReflection.getSimpleName(returnClass) + " missing from Entity!");
		}
	}
}
