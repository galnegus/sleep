package com.sleep;

import java.util.LinkedHashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sleep.component.SpawnerComponent;
import com.sleep.component.animation.AnimationActions;
import com.sleep.component.death.DeathComponent;
import com.sleep.component.death.PlayerDeathComponent;
import com.sleep.component.movement.*;
import com.sleep.component.render.AnimationRenderComponent;
import com.sleep.component.render.BackgroundRenderComponent;
import com.sleep.component.render.GhostRenderComponent;
import com.sleep.component.render.ImageRenderComponent;
import com.sleep.component.shader.LightShaderComponent;

public class EntityFactory {
	private static final int BOX_DEPTH = 0;
	private static final int GHOST_DEPTH = 1;
	private static final int PLAYER_DEPTH = 2;
	private static final int SPAWNER_DEPTH = 3;
	private static final int WALL_DEPTH = 4;

	private static final int GRID_DEPTH = -1;

	public static Entity makePlayer(int x, int y) {
		Array<TextureRegion> anim = new Array<TextureRegion>();
		anim.add(new TextureRegion(Sleep.assets.get("images/player.png", Texture.class)));
		anim.add(new TextureRegion(Sleep.assets.get("images/player_bw.png", Texture.class)));

		Animation idle = new Animation(0.5f, anim, Animation.LOOP);

		LinkedHashMap<AnimationActions, Animation> monkeyAnimations = new LinkedHashMap<AnimationActions, Animation>(2);
		monkeyAnimations.put(AnimationActions.WALK_RIGHT, idle);
		monkeyAnimations.put(AnimationActions.WALK_LEFT, idle);

		Entity player = Sleep.entityManager.add(new Entity("Player", new Vector2(x, y), PLAYER_DEPTH))
				.addComponent(new PlayerMovementComponent()).addComponent(new AnimationRenderComponent(idle))
				.addComponent(new PlayerDeathComponent())
				.addComponent(new LightShaderComponent(Sleep.light, new Color(1f, 1f, 1f, 0.25f), 2500, true))
				.initComponents();

		Sleep.player = player;

		return player;
	}

	public static Entity makeGhost(int x, int y) {
		return Sleep.entityManager.add(new Entity("Ghost", new Vector2(x, y), GHOST_DEPTH))
				.addComponent(new GhostMovementComponent())
				.addComponent(new GhostRenderComponent(Sleep.assets.get("images/dark_circle.png", Texture.class)))
				.addComponent(new DeathComponent())
				.addComponent(new LightShaderComponent(Sleep.light, new Color(0f, 0f, 0f, 0.1f), 100, true))
				.initComponents();
	}

	public static Entity makeSpectre(int x, int y) {
		return Sleep.entityManager.add(new Entity("Spectre", new Vector2(x, y), GHOST_DEPTH))
				.addComponent(new SpectreMovementComponent())
				.addComponent(new GhostRenderComponent(Sleep.assets.get("images/dark_circle.png", Texture.class)))
				.addComponent(new DeathComponent())
				.addComponent(new LightShaderComponent(Sleep.light, new Color(0f, 0f, 0f, 0.1f), 100, true))
				.initComponents();
	}

	public static Entity makeBox(int x, int y) {
		return Sleep.entityManager.add(new Entity("Box", new Vector2(x, y), BOX_DEPTH))
				.addComponent(new ImageRenderComponent(Sleep.assets.get("images/box_bw.png", Texture.class)))
				.addComponent(new MovementComponent()).initComponents();
	}

	public static Entity makeWall(int x, int y) {
		return Sleep.entityManager.add(new Entity("Wall", new Vector2(x, y), WALL_DEPTH))
				.addComponent(new ImageRenderComponent(Sleep.assets.get("images/wall_bw.png", Texture.class)))
				.initComponents();
	}

	public static Entity makeGrid(int xSize, int ySize) {
		Texture tex = Sleep.assets.get("images/grid_bw.png", Texture.class);
		tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		return Sleep.backgroundManager.add(new Entity("Grid", new Vector2(0, 0), GRID_DEPTH)).addComponent(
				new BackgroundRenderComponent(tex, xSize, ySize));
	}

	public static Entity makeSpawner(int x, int y, String type, float init, float freq) {
		Sleep.entityManager.add(new Entity("Spawner", new Vector2(x, y), SPAWNER_DEPTH))
				.addComponent(new SpawnerComponent(type, init, freq)).initComponents();

		return makeWall(x, y);
	}
}
