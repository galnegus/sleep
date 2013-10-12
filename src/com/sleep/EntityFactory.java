package com.sleep;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
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
	private static final int BOX_DEPTH = 0;
	private static final int GHOST_DEPTH = 1;
	private static final int PLAYER_DEPTH = 2;
	private static final int SPAWNER_DEPTH = 3;
	private static final int WALL_DEPTH = 4;
	
	private static final int GRID_DEPTH = -1;
	
	public static Entity makePlayer(int x, int y) {
		Array<TextureRegion> anim = new Array<TextureRegion>();
		anim.add(new TextureRegion(GameScreen.assets.get("images/player.png", Texture.class)));
		anim.add(new TextureRegion(GameScreen.assets.get("images/player_bw.png", Texture.class)));

		Animation idle = new Animation(0.5f, anim, Animation.LOOP);

		LinkedHashMap<AnimationActions, Animation> monkeyAnimations = new LinkedHashMap<AnimationActions, Animation>(2);
		monkeyAnimations.put(AnimationActions.WALK_RIGHT, idle);
		monkeyAnimations.put(AnimationActions.WALK_LEFT, idle);

		Entity player = GameScreen.entityManager.add(new Entity("Player", new Vector2(x, y), PLAYER_DEPTH))
				.addComponent(new PlayerMovementComponent()).addComponent(new AnimationRenderComponent(idle))
				.addComponent(new PlayerDeathComponent()).initComponents();

		GameScreen.player = player;

		return player;
	}

	public static Entity makeGhost(int x, int y) {
		return GameScreen.entityManager.add(new Entity("Ghost", new Vector2(x, y), GHOST_DEPTH))
				.addComponent(new GhostMovementComponent())
				.addComponent(new ImageRenderComponent(GameScreen.assets.get("images/ghost.png", Texture.class)))
				.addComponent(new DeathComponent()).initComponents();
	}

	public static Entity makeBox(int x, int y) {
		return GameScreen.entityManager.add(new Entity("Box", new Vector2(x, y), BOX_DEPTH))
				.addComponent(new ImageRenderComponent(GameScreen.assets.get("images/box.png", Texture.class)))
				.addComponent(new MovementComponent()).initComponents();
	}

	public static Entity makeWall(int x, int y) {
		return GameScreen.entityManager.add(new Entity("Wall", new Vector2(x, y), WALL_DEPTH))
				.addComponent(new ImageRenderComponent(GameScreen.assets.get("images/wall.png", Texture.class)))
				.initComponents();
	}

	public static Entity makeGrid(int xSize, int ySize) {
		Texture tex = GameScreen.assets.get("images/grid.png", Texture.class);
		tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		return GameScreen.backgroundManager.add(new Entity("Grid", new Vector2(0, 0), GRID_DEPTH)).addComponent(
				new BackgroundRenderComponent(tex, xSize, ySize));
	}
}
