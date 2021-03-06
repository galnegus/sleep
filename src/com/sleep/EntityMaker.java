package com.sleep;

import java.util.LinkedHashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sleep.soko.SokoLevel;
import com.sleep.soko.component.LightComponent;
import com.sleep.soko.component.SpawnerComponent;
import com.sleep.soko.component.animation.AnimationActions;
import com.sleep.soko.component.death.DeathComponent;
import com.sleep.soko.component.death.PlayerDeathComponent;
import com.sleep.soko.component.movement.GhostMovementComponent;
import com.sleep.soko.component.movement.MovementComponent;
import com.sleep.soko.component.movement.PlayerMovementComponent;
import com.sleep.soko.component.movement.SpectreMovementComponent;
import com.sleep.soko.component.render.AnimationRenderComponent;
import com.sleep.soko.component.render.BackgroundRenderComponent;
import com.sleep.soko.component.render.GhostRenderComponent;
import com.sleep.soko.component.render.ImageRenderComponent;
import com.sleep.text.Room;
import com.sleep.text.component.movement.OverWorldMovementComponent;
import com.sleep.text.component.render.RoomRenderComponent;

/**
 * Creates entities!
 */
public class EntityMaker {
	private static final int EXIT_DEPTH = 3;
	private static final int BOX_DEPTH = 0;
	private static final int GHOST_DEPTH = 1;
	private static final int PLAYER_DEPTH = 2;
	private static final int SPAWNER_DEPTH = 3;
	private static final int WALL_DEPTH = 4;

	private static final int ROOM_DEPTH = 3;
	private static final int CONNECTION_DEPTH = 4;

	private static final int GRID_DEPTH = -1;

	/*
	 * Entities for soko
	 */

	public static Entity makePlayer(SokoLevel level, int x, int y) {
		Array<TextureRegion> anim = new Array<TextureRegion>();
		anim.add(new TextureRegion(Sleep.assets.get("images/player.png", Texture.class)));
		anim.add(new TextureRegion(Sleep.assets.get("images/player_bw.png", Texture.class)));

		Animation idle = new Animation(0.5f, anim, Animation.LOOP);

		LinkedHashMap<AnimationActions, Animation> playerAnimations = new LinkedHashMap<AnimationActions, Animation>(2);
		playerAnimations.put(AnimationActions.IDLE, idle);

		return level.entityManager.add(new Entity("Player", new Vector2(x, y), PLAYER_DEPTH))
				.addComponent(new PlayerMovementComponent(level)).addComponent(new AnimationRenderComponent(idle))
				.addComponent(new PlayerDeathComponent(level))
				.addComponent(new LightComponent(Sleep.light, new Color(1f, 1f, 1f, 1f), 2500, true)).initComponents();
	}

	public static Entity makeGhost(SokoLevel level, int x, int y) {
		return level.entityManager.add(new Entity("Ghost", new Vector2(x, y), GHOST_DEPTH))
				.addComponent(new GhostMovementComponent(level))
				.addComponent(new GhostRenderComponent(Sleep.assets.get("images/dark_circle.png", Texture.class)))
				.addComponent(new DeathComponent(level))
				.addComponent(new LightComponent(Sleep.light, new Color(0f, 0f, 0f, 0.1f), 100, true)).initComponents();
	}

	public static Entity makeSpectre(SokoLevel level, int x, int y) {
		return level.entityManager.add(new Entity("Spectre", new Vector2(x, y), GHOST_DEPTH))
				.addComponent(new SpectreMovementComponent(level))
				.addComponent(new GhostRenderComponent(Sleep.assets.get("images/dark_circle.png", Texture.class)))
				.addComponent(new DeathComponent(level))
				.addComponent(new LightComponent(Sleep.light, new Color(0f, 0f, 0f, 0.1f), 100, true)).initComponents();
	}

	public static Entity makeBox(SokoLevel level, int x, int y) {
		return level.entityManager.add(new Entity("Box", new Vector2(x, y), BOX_DEPTH))
				.addComponent(new ImageRenderComponent(Sleep.assets.get("images/box_bw.png", Texture.class)))
				.addComponent(new MovementComponent(level)).initComponents();
	}
	
	public static Entity makeExit(SokoLevel level, int x, int y) {
		Array<TextureRegion> anim = new Array<TextureRegion>();
		anim.add(new TextureRegion(Sleep.assets.get("images/exit1.png", Texture.class)));
		anim.add(new TextureRegion(Sleep.assets.get("images/exit2.png", Texture.class)));
		anim.add(new TextureRegion(Sleep.assets.get("images/exit3.png", Texture.class)));

		Animation portal = new Animation(0.25f, anim, Animation.LOOP_PINGPONG);

		LinkedHashMap<AnimationActions, Animation> exitAnimations = new LinkedHashMap<AnimationActions, Animation>(2);
		exitAnimations.put(AnimationActions.IDLE, portal);

		return level.entityManager.add(new Entity("Exit", new Vector2(x, y), EXIT_DEPTH))
				.addComponent(new AnimationRenderComponent(portal))
				.addComponent(new LightComponent(Sleep.light, new Color(0.3125f, 0.8125f, 0.8125f, 1f), 64, true)).initComponents();
	}

	public static Entity makeGrid(SokoLevel level, int x, int y, int xSize, int ySize) {
		Texture tex = Sleep.assets.get("images/grid_bw.png", Texture.class);
		tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		return level.backgroundManager.add(new Entity("Grid", new Vector2(x, y), GRID_DEPTH))
				.addComponent(new BackgroundRenderComponent(tex, xSize, ySize)).initComponents();
	}

	public static Entity makeSpawner(SokoLevel level, int x, int y, String type, float init, float freq) {
		level.entityManager.add(new Entity("Spawner", new Vector2(x, y), SPAWNER_DEPTH))
				.addComponent(new SpawnerComponent(level, type, init, freq)).initComponents();

		return makeWall(level.entityManager, x, y);
	}

	/*
	 * Entities for both soko/IF
	 */

	public static Entity makeWall(EntityManager entityManager, int x, int y) {
		return entityManager.add(new Entity("Wall", new Vector2(x, y), WALL_DEPTH))
				.addComponent(new ImageRenderComponent(Sleep.assets.get("images/wall_bw.png", Texture.class)))
				.initComponents();
	}

	/*
	 * Entities for IF
	 */

	public static Entity makeRoom(EntityManager entityManager, int x, int y, Room room) {
		return entityManager.add(new Entity("Room", new Vector2(x, y), ROOM_DEPTH))
				.addComponent(new RoomRenderComponent(Sleep.assets.get("images/room_bw.png", Texture.class), room)).initComponents();
	}

	public static Entity makeHorizontalDoorway(EntityManager entityManager, int x, int y) {
		return entityManager.add(new Entity("Horizontal Doorway", new Vector2(x, y), CONNECTION_DEPTH))
				.addComponent(new ImageRenderComponent(Sleep.assets.get("images/horizontal_bw.png", Texture.class)))
				.initComponents();
	}

	public static Entity makeVerticalDoorway(EntityManager entityManager, int x, int y) {
		return entityManager.add(new Entity("Vertical Doorway", new Vector2(x, y), CONNECTION_DEPTH))
				.addComponent(new ImageRenderComponent(Sleep.assets.get("images/vertical_bw.png", Texture.class)))
				.initComponents();
	}

	public static Entity makeIFPlayer(EntityManager entityManager, int x, int y) {
		Array<TextureRegion> anim = new Array<TextureRegion>();
		anim.add(new TextureRegion(Sleep.assets.get("images/player.png", Texture.class)));
		anim.add(new TextureRegion(Sleep.assets.get("images/player_bw.png", Texture.class)));

		Animation idle = new Animation(0.5f, anim, Animation.LOOP);

		LinkedHashMap<AnimationActions, Animation> monkeyAnimations = new LinkedHashMap<AnimationActions, Animation>(2);
		monkeyAnimations.put(AnimationActions.WALK_RIGHT, idle);
		monkeyAnimations.put(AnimationActions.WALK_LEFT, idle);

		return entityManager.add(new Entity("Player", new Vector2(x, y), PLAYER_DEPTH))
				.addComponent(new OverWorldMovementComponent()).addComponent(new AnimationRenderComponent(idle))
				.addComponent(new LightComponent(Sleep.light, new Color(1f, 1f, 1f, 0.25f), 250, true))
				.initComponents();
	}
}
