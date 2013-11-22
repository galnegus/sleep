package com.sleep.ghost.component.render;

import com.sleep.ghost.component.Component;
import com.sleep.ghost.component.RenderableComponent;

public abstract class RenderComponent extends Component implements RenderableComponent {	
	public abstract int getWidth();
	
	public abstract int getHeight();
	
}
