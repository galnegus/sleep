package gamedemo.component.particle;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.StateBasedGame;

import gamedemo.component.Component;
import gamedemo.component.ComponentException;
import gamedemo.component.RenderableComponent;
import gamedemo.component.movement.MovementComponent;

public class ParticleComponent extends Component implements RenderableComponent {
	private ParticleSystem system;
	private ParticleEmitter activeEmitter;
	private MovementComponent movementComponent;
	
	public ParticleComponent (ParticleSystem particleSystem) {
		this.system = particleSystem;
		//system.setBlendingMode(ParticleSystem.BLEND_ADDITIVE);
	}

	public void addEmitter(ParticleEmitter emitter) {
		system.addEmitter(emitter);
		activeEmitter = emitter;
	}
	
	public ParticleSystem getParticleSystem() {
		return system;
	}

	@Override
	public void init() throws ComponentException {
		movementComponent = getDependency(MovementComponent.class, MovementComponent.class);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		system.update(delta);
		system.setPosition(owner.getPosition().getX(), owner.getPosition().getY());
		system.moveAll(activeEmitter, -movementComponent.getVelocity().x * delta, 0f);
	}
	
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		system.render();
	}

}
