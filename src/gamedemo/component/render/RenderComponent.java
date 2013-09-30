package gamedemo.component.render;

import gamedemo.component.Component;
import gamedemo.component.RenderableComponent;

public abstract class RenderComponent extends Component implements RenderableComponent {	
	public abstract int getWidth();
	
	public abstract int getHeight();
	
}
