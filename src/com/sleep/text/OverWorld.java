package com.sleep.text;

import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.sleep.Constants;
import com.sleep.Entity;
import com.sleep.EntityManager;
import com.sleep.Fader;
import com.sleep.LightSource;
import com.sleep.Renderer;
import com.sleep.Sleep;
import com.sleep.soko.component.Message;

public class OverWorld implements Renderer, LightSource {
	private EntityManager entityManager;
	
	private int columns;
	private int rows;

	private char[][] map;
	private Vector2 playerPosition;
	
	private Map<Character, Room> roomList;
	private Room currentRoom;

	public Entity player;

	public Fader fader;

	public OverWorld(String filename) {
		entityManager = new EntityManager();
		OverWorldParser parser = new OverWorldParser(filename, entityManager);
		
		columns = parser.getColumns();
		rows = parser.getRows();
		
		map = parser.getMap();
		playerPosition = parser.getPlayerPosition();
		
		roomList = parser.getRoomList();
		currentRoom = parser.getCurrentRoom();
		
		player = parser.getPlayer();
		
		fader = new Fader(new Color(Color.BLACK), new Color(Color.WHITE), Constants.FADER_FREQ, Constants.FADER_STEPS);
	}

	public int columnCount() {
		return columns;
	}

	public int rowCount() {
		return rows;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	private boolean isWithinMapBounds(Vector2 position) {
		if (position.x >= 0 && position.y >= 0 && position.x < columns && position.y < rows)
			return true;
		else
			return false;
	}

	private boolean isDoorway(Vector2 position, Direction direction) {
		if (isWithinMapBounds(position)) {
			char mapSymbol = map[(int) position.x][(int) position.y];
			if ((direction == Direction.LEFT || direction == Direction.RIGHT)
					&& mapSymbol == Constants.HORIZONTAL_DOORWAY) {
				return true;
			} else if ((direction == Direction.UP || direction == Direction.DOWN)
					&& mapSymbol == Constants.VERTICAL_DOORWAY) {
				return true;
			}
		}
		return false;
	}

	public void movePlayer(Direction direction) {
		Vector2 movement = direction.getMovement();
		if (isDoorway(new Vector2(playerPosition.x + movement.x, playerPosition.y + movement.y), direction)) {
			Message message = Message.fromString("move " + direction.toString());
			player.sendMessage(message);

			playerPosition.add(movement.x * 2, movement.y * 2);
			currentRoom = roomList.get(map[(int) playerPosition.x][(int) playerPosition.y]);
		}

	}

	public void printMap() {
		System.out.println("map: ");
		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < rows; y++) {
				System.out.print(map[x][y]);
			}
			System.out.println();
		}
	}

	public void update() {
		entityManager.update();
	}
	
	public void drawLight() {
		entityManager.drawLight();
	}
	
	public void bindLight(int i) {
		entityManager.bindLight(i);
	}

	public void render() {
		fader.render(Sleep.batch);

		entityManager.render();
		
		fader.renderDone(Sleep.batch);
	}
}
