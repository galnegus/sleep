package com.sleep;

import java.util.LinkedHashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sleep.component.animation.AnimationActions;
import com.sleep.component.death.DeathComponent;
import com.sleep.component.death.PlayerDeathComponent;
import com.sleep.component.movement.GhostMovementComponent;
import com.sleep.component.movement.MovementComponent;
import com.sleep.component.movement.PlayerMovementComponent;
import com.sleep.component.render.AnimationRenderComponent;
import com.sleep.component.render.BackgroundRenderComponent;
import com.sleep.component.render.ImageRenderComponent;

public class EntityFactory {
	public static Entity makePlayer(int x, int y) {
		Array<TextureRegion> anim = new Array<TextureRegion>();
		anim.add(new TextureRegion(GameScreen.assets.get("images/player.png", Texture.class)));
		anim.add(new TextureRegion(GameScreen.assets.get("images/player_bw.png", Texture.class)));
		
		Animation idle = new Animation(1f, anim, Animation.LOOP);
		
		LinkedHashMap<AnimationActions, Animation> monkeyAnimations = new LinkedHashMap<AnimationActions, Animation>(2);
		monkeyAnimations.put(AnimationActions.WALK_RIGHT, idle);
		monkeyAnimations.put(AnimationActions.WALK_LEFT, idle);
		
		Entity player = GameScreen.entityManager.add(new Entity("Player"))
				.addComponent(new PlayerMovementComponent())
				.addComponent(new AnimationRenderComponent(idle))
				.addComponent(new PlayerDeathComponent())
				.initComponents()
				.setPosition(new Vector2(x, y));
		
		GameScreen.player = player;
		
		return player;
	}
	
	public static Entity makeGhost(int x, int y) {
		return GameScreen.entityManager.add(new Entity("Ghost"))
				.addComponent(new GhostMovementComponent())
				.addComponent(new ImageRenderComponent(GameScreen.assets.get("images/ghost.png", Texture.class)))
				.addComponent(new DeathComponent())
				.initComponents()
				.setPosition(new Vector2(x, y));
	}
	
	public static Entity makeBox(int x, int y) {
		return GameScreen.entityManager.add(new Entity("Box"))
				.addComponent(new ImageRenderComponent(GameScreen.assets.get("images/box.png", Texture.class)))
				.addComponent(new MovementComponent())
				.initComponents()
				.setPosition(new Vector2(x, y));
	}
	
	public static Entity makeWall(int x, int y) {
		return GameScreen.entityManager.add(new Entity("Wall"))
				.addComponent(new ImageRenderComponent(GameScreen.assets.get("images/wall.png", Texture.class)))
				.initComponents()
				.setPosition(new Vector2(x, y));
	}
	
	public static Entity makeEnemy(int x, int y) {
		return GameScreen.entityManager.add(new Entity("Enemy"))
				.addComponent(new ImageRenderComponent(GameScreen.assets.get("images/enemy.png", Texture.class)))
				.initComponents()
				.setPosition(new Vector2(x, y));
	}
	
	public static Entity makeGrid(int xSize, int ySize) {
		Texture tex = GameScreen.assets.get("images/grid.png", Texture.class);
		tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		return GameScreen.backgroundManager.add(new Entity("Grid"))
				.addComponent(new BackgroundRenderComponent(tex, xSize, ySize));
	}
}
