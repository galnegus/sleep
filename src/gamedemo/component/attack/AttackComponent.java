package gamedemo.component.attack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.StateBasedGame;

import gamedemo.Demo;
import gamedemo.Entity;
import gamedemo.component.Component;
import gamedemo.component.ComponentException;
import gamedemo.component.movement.MovementComponent;
import gamedemo.component.movement.ProjectileMovementComponent;
import gamedemo.component.particle.ParticleComponent;
import gamedemo.component.render.ImageRenderComponent;

public class AttackComponent extends Component{
	
	@Override
	public void init() throws ComponentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_S)) {
			try {
				MovementComponent m = owner.getComponent(MovementComponent.class);				
				
				Entity poo = new Entity("poo");
				
					poo.addComponent(new ImageRenderComponent(new Image("/Images/poo.png")));
				
				poo.addComponent(new ProjectileMovementComponent(m.getVelocity()));
				
				ParticleComponent particleComp = new ParticleComponent(new ParticleSystem(new Image("/Images/smoke_particle.png"), 1000));
				ConfigurableEmitter emitter = ParticleIO.loadEmitter(new File("Images/smoke.xml"));
				particleComp.addEmitter(emitter);
				poo.addComponent(particleComp);
				
				Vector2f newPos = new Vector2f();
				newPos.x = owner.getPosition().x + owner.getWidth()/2 - poo.getWidth()/2;
				newPos.y = owner.getPosition().y + owner.getHeight()/2 - poo.getHeight()/2;
				poo.setPosition(newPos);
				poo.initComponents();
				
				Demo.entityManager.add(poo);
			} catch (SlickException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
