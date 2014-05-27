package com.sleep;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.sleep.soko.component.Component;
import com.sleep.soko.component.ComponentException;
import com.sleep.soko.component.LightComponent;
import com.sleep.soko.component.Message;
import com.sleep.soko.component.RenderableComponent;
import com.sleep.soko.component.render.RenderComponent;

public class Entity {

	private String name;
	public Vector2 position;
	private final int depth;
	
	private Map<Class<? extends Component>, Component> components = null;

	public Entity(String id, Vector2 position, int depth) {
		this.name = id;
		this.position = position;
		this.depth = depth;

		components = new HashMap<Class<? extends Component>, Component>();
	}

	/**
	 * Adds a component to the components HashMap. The key of the mapping will
	 * be decided by the inheritance of the component being added.
	 * <p>
	 * If the component only extends the Component class, the key will be
	 * defined as the class type of the component being added. Ie if you add
	 * component of class {@code<HackingComponent extends Component>} then the
	 * key of the added mapping will be HackingComponent.class.
	 * <p>
	 * If the components extends another component that isn't Component (but
	 * rather another component which does extend Component), then the key will
	 * be defined as the class type of the superclass. Ie if you add a component
	 * of class {@code<ImageRenderComponent extends RenderComponent
	 * extends Component>} then the key of the added mapping will be
	 * RenderComponent.class.
	 * <p>
	 * This means that the components HashMap may only contain one component
	 * extending "RenderComponent" (or any other Component superclass), but it
	 * may contain an unlimited number of components extending Component.
	 * 
	 * @param component
	 *            The component being added.
	 */
	public Entity addComponent(Component component) {
		Class<? extends Component> compFamilyClass = getComponentFamilyClass(component);
		components.put(compFamilyClass, component);

		component.setOwnerEntity(this);

		return this;
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Component> getComponentFamilyClass(Component component) {
		Class<? extends Component> current = component.getClass();
		while (true) {
			if (current.getSuperclass().equals(Component.class)) {
				return current;
			} else {
				current = (Class<? extends Component>) current.getSuperclass();
			}
		}
	}

	/**
	 * Retrieves a component from the components HashMap and casts it to the
	 * correct type.
	 * <p>
	 * For instance {@code <MovementComponent extends Component>} can be
	 * retrieved by calling <br>
	 * {@code MovementComponent m = getComponent(MovementComponent.class);}
	 * 
	 * @param <T>
	 *            A class which extends Component
	 * @param type
	 *            The class type of the component that is being retrieved
	 *            <b>or</b> the class type of a component that extends it;
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<T> type) {
		return (T) (components.get(type));
	}

	public int getWidth() {
		RenderComponent renderComponent = getComponent(RenderComponent.class);
		if (renderComponent != null)
			return renderComponent.getWidth();
		else
			return 0;
	}

	public int getHeight() {
		RenderComponent renderComponent = getComponent(RenderComponent.class);
		if (renderComponent != null)
			return renderComponent.getHeight();
		else
			return 0;
	}

	public int getDepth() {
		return depth;
	}

	public void update() {
		for (Component comp : components.values()) {
			comp.update();
		}
	}
	
	public void drawLight() {
		for (Component comp : components.values()) {
			if (comp instanceof LightComponent) {
				((LightComponent) comp).drawLight();
			}
		}
	}
	
	public void bindLight(int i) {
		for (Component comp : components.values()) {
			if (comp instanceof LightComponent) {
				((LightComponent) comp).bindLight(i);
			}
		}
	}

	public void render() {
		for (Component comp : components.values()) {
			if (comp instanceof RenderableComponent) {
				((RenderableComponent) comp).render();
			}
		}
	}
	
	public void sendMessage(Message message) {
		for (Component comp: components.values()) {
			comp.receiveMessage(message);
		}
	}

	public Entity initComponents() {
		for (Component comp : components.values()) {
			try {
				comp.init();
			} catch (ComponentException e) {
				System.err.println("initComponents() failed in\n entity: " + name + "\n component: "
						+ comp.getClass().getName() + ".");
				e.printStackTrace();
				System.exit(0);
			}
		}

		return this;
	}
	
	public String getName() {
		return name;
	}
}