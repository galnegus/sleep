package gamedemo.component.movement;

import gamedemo.Constants;
import gamedemo.Demo;
import gamedemo.component.ComponentException;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public class PlayerMovementComponent extends MovementComponent {
	boolean triggerUp = true;
	boolean triggerDown = true;
	boolean triggerLeft = true;
	boolean triggerRight = true;

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		resetAcceleration();
		Input input = gc.getInput();
		
		if(!moving) {
			if(input.isKeyDown(Constants.MOVE_LEFT) && triggerLeft) {
				moveTo(owner.getPosition().x - Constants.GRID_CELL_SIZE, owner.getPosition().y, delta);
				triggerLeft = false;
			} else if(!input.isKeyDown(Constants.MOVE_LEFT) && !triggerLeft) {
				triggerLeft = true;
			}
			
			if(input.isKeyDown(Constants.MOVE_RIGHT) && triggerRight) {
				moveTo(owner.getPosition().x + Constants.GRID_CELL_SIZE, owner.getPosition().y, delta);
				triggerRight = false;
			} else if(!input.isKeyDown(Constants.MOVE_RIGHT) && !triggerRight) {
				triggerRight = true;
			}
			
			if(input.isKeyDown(Constants.MOVE_UP) && triggerUp) {
				moveTo(owner.getPosition().x, owner.getPosition().y - Constants.GRID_CELL_SIZE, delta);
				triggerUp = false;
			} else if(!input.isKeyDown(Constants.MOVE_UP) && !triggerUp) {
				triggerUp = true;
			}
			
			if(input.isKeyDown(Constants.MOVE_DOWN) && triggerDown){
				moveTo(owner.getPosition().x, owner.getPosition().y + Constants.GRID_CELL_SIZE, delta);
				triggerDown = false;
			} else if(!input.isKeyDown(Constants.MOVE_DOWN) && !triggerDown) {
				triggerDown = true;
			}		
		}
		updateStuff(delta);
	}

	@Override
	public void init() throws ComponentException {
		// DO NOTHING
	}
}
