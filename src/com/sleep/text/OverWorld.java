package com.sleep.text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.sleep.Constants;
import com.sleep.Entity;
import com.sleep.EntityMaker;
import com.sleep.EntityManager;
import com.sleep.Fader;
import com.sleep.LightSource;
import com.sleep.Renderer;
import com.sleep.Sleep;
import com.sleep.soko.component.Message;

public class OverWorld implements Renderer, LightSource {
	private int columns;
	private int rows;

	private char[][] map;
	private Vector2 playerPosition;
	private Room currentRoom;

	private EntityManager entityManager;
	private Map<Character, Room> roomList;

	public Entity player;

	public Fader fader;

	public OverWorld(String filename) {
		entityManager = new EntityManager();
		roomList = new HashMap<Character, Room>();
		playerPosition = new Vector2();
		fader = new Fader(new Color(Color.BLACK), new Color(Color.WHITE), Constants.FADER_FREQ, Constants.FADER_STEPS);

		try {
			JsonReader json = new JsonReader();
			JsonValue root = json.parse(Gdx.files.internal(filename + ".json"));
			JsonValue rooms = root.get("rooms");

			JsonValue room;
			char id;
			String name;
			boolean soko;
			for (int i = 0; i < rooms.size; i++) {
				room = rooms.get(i);

				if (room.getString("id") == null)
					Gdx.app.error("WorldXmlError", "no id element found in room element " + i);
				if (room.getString("name") == null)
					Gdx.app.error("WorldXmlError", "no name element found in room element " + i);
				if (room.getString("id").length() > 1)
					Gdx.app.error("WorldXmlError", "bad id supplied in xml element " + i
							+ ", id must be a single character");

				id = room.getString("id").charAt(0);
				name = room.getString("name");
				soko = room.getBoolean("soko");
				JsonValue monologueBranch = room.get("monologue");
				ArrayList<Monologue> monologues = null;
				if (monologueBranch != null) {
					monologues = new ArrayList<Monologue>();
					for (int j = 0; j < monologueBranch.size; j++) {
//						monologues.add(new TyperMonologue(monologueBranch.get(j).getString("text")));
						monologues.add(new FaderMonologue(monologueBranch.get(j).getString("text")));
					}
				}
				roomList.put(id, new Room(id, name, soko, monologues));
			}

			FileHandle levelTxt = Gdx.files.internal(filename + ".world");
			BufferedReader br = levelTxt.reader(200);

			columns = Integer.parseInt(br.readLine());
			rows = Integer.parseInt(br.readLine());

			map = new char[columns][rows];
			char[][] input = new char[rows][columns];

			for (int i = 0; i < rows; i++) {
				input[i] = br.readLine().toCharArray();
			}

			/**
			 * rotate (x=y, y=x) and flip the board (y=ySize-y)
			 * [[1,2,3],[4,5,6]] -> [[5,6],[3,4],[1,2]]
			 * 
			 * also flip any doorways
			 * - -> |, | -> -
			 */
			for (int x = 0; x < columns; x++) {
				for (int y = 0; y < rows; y++) {
					map[x][rows - 1 - y] = input[y][x];
				}
			}

			br.close();

			// First create doorways
			for (int x = 0; x < columns; x++) {
				for (int y = 0; y < rows; y++) {
					if (map[x][y] == Constants.HORIZONTAL_DOORWAY) {
						// HORIZONTAL DOORWAY
						EntityMaker.makeHorizontalDoorway(entityManager, x * Constants.GRID_CELL_SIZE / 2, y
								* Constants.GRID_CELL_SIZE / 2);
					} else if (map[x][y] == Constants.VERTICAL_DOORWAY) {
						// VERTICAL DOORWAY
						EntityMaker.makeVerticalDoorway(entityManager, x * Constants.GRID_CELL_SIZE / 2, y
								* Constants.GRID_CELL_SIZE / 2);
					}
				}
			}

			// Then create rooms
			for (int x = 0; x < columns; x++) {
				for (int y = 0; y < rows; y++) {
					if (Character.isLetterOrDigit(map[x][y])) {
						// ROOM
						EntityMaker.makeRoom(entityManager, x * Constants.GRID_CELL_SIZE / 2, y
								* Constants.GRID_CELL_SIZE / 2, roomList.get(map[x][y]));
					} else if (map[x][y] == '#') {
						// WALL
						EntityMaker.makeWall(entityManager, x * Constants.GRID_CELL_SIZE / 2, y
								* Constants.GRID_CELL_SIZE / 2);
					}

					if (map[x][y] == '0') {
						player = EntityMaker.makeIFPlayer(entityManager, x * Constants.GRID_CELL_SIZE / 2, y
								* Constants.GRID_CELL_SIZE / 2);
						playerPosition.set(x, y);
						setCurrentRoom('0');
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	private void setCurrentRoom(char roomID) {
		currentRoom = roomList.get(roomID);
	}

	public void movePlayer(Direction direction) {
		Vector2 movement = direction.getMovement();
		if (isDoorway(new Vector2(playerPosition.x + movement.x, playerPosition.y + movement.y), direction)) {
			Message message = Message.fromString("move " + direction.toString());
			player.sendMessage(message);

			playerPosition.add(movement.x * 2, movement.y * 2);
			setCurrentRoom(map[(int) playerPosition.x][(int) playerPosition.y]);
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
