package com.sleep.component.render;

import com.sleep.component.Component;
import com.sleep.component.RenderableComponent;

public abstract class RenderComponent extends Component implements RenderableComponent {	
	public abstract int getWidth();
	
	public abstract int getHeight();
	
}
