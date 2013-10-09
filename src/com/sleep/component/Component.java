package com.sleep.component;

import com.sleep.Entity;

public abstract class Component {
	protected Entity owner;
	
	public void setOwnerEntity(Entity owner) {
		this.owner = owner;
	}
	
	public abstract void update(float delta);
	
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
