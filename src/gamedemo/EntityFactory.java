package gamedemo;

import java.util.LinkedHashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import gamedemo.component.animation.AnimationActions;
import gamedemo.component.animation.AnimationMapComponent;
import gamedemo.component.attack.AttackComponent;
import gamedemo.component.movement.PlayerMovementComponent;
import gamedemo.component.render.AnimationRenderComponent;
import gamedemo.component.render.BackgroundRenderComponent;
import gamedemo.component.render.ImageRenderComponent;

public class EntityFactory {
	public static Entity makePlayer(int x, int y) throws SlickException {
		LinkedHashMap<AnimationActions, Animation> monkeyAnimations = new LinkedHashMap<AnimationActions, Animation>(2);
		Animation idle = ResourceLibrary.playerIdleAnimation();
		monkeyAnimations.put(AnimationActions.WALK_RIGHT, idle);
		monkeyAnimations.put(AnimationActions.WALK_LEFT, idle);
		
		Entity player = Demo.entityManager.add(new Entity("Player"))
				.addComponent(new PlayerMovementComponent())
				.addComponent(new AnimationRenderComponent(idle))
				.addComponent(new AnimationMapComponent(monkeyAnimations))
				.initComponents()
				.setPosition(new Vector2f(x, y));
		
		Demo.player = player;
		
		return player;
	}
	
	public static Entity makeBox(int x, int y) throws SlickException {
		return Demo.entityManager.add(new Entity("Box"))
				.addComponent(new ImageRenderComponent(ResourceLibrary.box()))
				.initComponents()
				.setPosition(new Vector2f(x, y));
	}
	
	public static Entity makeWall(int x, int y) throws SlickException {
		return Demo.entityManager.add(new Entity("Wall"))
				.addComponent(new ImageRenderComponent(ResourceLibrary.wall()))
				.initComponents()
				.setPosition(new Vector2f(x, y));
	}
	
	public static Entity makeGrid(int xSize, int ySize) throws SlickException {
		return Demo.backgroundManager.add(new Entity("Grid"))
				.addComponent(new BackgroundRenderComponent(ResourceLibrary.grid(), xSize, ySize));
	}
}
