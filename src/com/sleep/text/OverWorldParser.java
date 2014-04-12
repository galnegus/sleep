package com.sleep.text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.sleep.Constants;
import com.sleep.Entity;
import com.sleep.EntityMaker;
import com.sleep.EntityManager;

public class OverWorldParser {
	private int rows, columns;
	private char[][] map;

	private Map<Character, Room> roomList;
	private Room currentRoom;

	private Entity player;
	private Vector2 playerPosition;
	
	private final char STARTING_ROOM = '0';

	public OverWorldParser(String filename, EntityManager entityManager) {
		roomList = new HashMap<Character, Room>();
		playerPosition = new Vector2();

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
						if (monologueBranch.get(j).getString("type").equals("typer")) {
							monologues.add(new TyperMonologue(monologueBranch.get(j).getString("text")));
						} else if (monologueBranch.get(j).getString("type").equals("fader")) {
							monologues.add(new FaderMonologue(monologueBranch.get(j).getString("text")));
						}
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

					if (map[x][y] == STARTING_ROOM) {
						player = EntityMaker.makeIFPlayer(entityManager, x * Constants.GRID_CELL_SIZE / 2, y
								* Constants.GRID_CELL_SIZE / 2);
						playerPosition.set(x, y);
						currentRoom = roomList.get(STARTING_ROOM);
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public char[][] getMap() {
		return map;
	}

	public Map<Character, Room> getRoomList() {
		return roomList;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public Entity getPlayer() {
		return player;
	}

	public Vector2 getPlayerPosition() {
		return playerPosition;
	}
}
