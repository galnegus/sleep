package gamedemo;

import gamedemo.component.Component;
import gamedemo.component.ComponentException;
import gamedemo.component.RenderableComponent;
import gamedemo.component.render.RenderComponent;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

public class Entity{

	private String name;

	private Vector2f position;

	private Map<Class<? extends Component>, Component> components = null;

	public Entity(String id) {
		this.name = id;

		components = new HashMap<Class<? extends Component>, Component>();

		position = new Vector2f();
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
	 * rather another component which does extend Component), then the key
	 * will be defined as the class type of the superclass. Ie if you add a
	 * component of class {@code<ImageRenderComponent extends RenderComponent
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
		Class<? extends Component>compFamilyClass = getComponentFamilyClass(component);
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
	public <T extends Component> T getComponent(Class<T> type) {
		return type.cast(components.get(type));
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public Entity setPosition(Vector2f position) {
		this.position = position;
		return this;
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

	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		for (Component comp : components.values()) {
			comp.update(gc, sb, delta);
		}
	}

	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		for(Component comp : components.values()) {
			if(comp instanceof RenderableComponent) {
				((RenderableComponent) comp).render(gc, sb, gr);
			}
		}
	}

	public Entity initComponents() {
		for (Component comp : components.values()) {
			try {
				comp.init();
			} catch (ComponentException e) {
				System.err.println("initComponents() failed in\n entity: " + name + "\n component: " + comp.getClass().getName() + ".");
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