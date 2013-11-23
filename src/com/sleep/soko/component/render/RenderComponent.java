package com.sleep.soko.component.render;

import com.sleep.soko.component.Component;
import com.sleep.soko.component.RenderableComponent;

public abstract class RenderComponent extends Component implements RenderableComponent {	
	public abstract int getWidth();
	
	public abstract int getHeight();
	
}
