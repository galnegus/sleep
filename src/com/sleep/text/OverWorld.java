package com.sleep.text;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.sleep.Constants;
import com.sleep.soko.Entity;
import com.sleep.soko.EntityMaker;
import com.sleep.soko.EntityManager;
import com.sleep.text.object.Room;

public class OverWorld {
	private int columns;
	private int rows;

	private EntityManager entityManager;
	private EntityManager backgroundManager;
	private Map<Character, Room> roomList;

	public Entity player;

	public OverWorld(String filename) {
		entityManager = new EntityManager();
		backgroundManager = new EntityManager();
		roomList = new HashMap<Character, Room>();

		try {
			XmlReader xml = new XmlReader();

			Element root = xml.parse(Gdx.files.internal(filename + ".xml"));
			Element rooms = root.getChildByName("rooms");
			
			Element room;
			char id;
			String name;
			for (int i = 0; i < rooms.getChildCount(); i++) {
				room = rooms.getChild(i);

				if (room.getChildByName("id") == null)
					Gdx.app.error("WorldXmlError", "no id element found in room element " + i);
				if (room.getChildByName("name") == null)
					Gdx.app.error("WorldXmlError", "no name element found in room element " + i);
				if (room.getChildByName("id").getText().length() > 1) 
					Gdx.app.error("WorldXmlError", "bad id supplied in xml element " + i + ", id must be a single character");

				id = room.getChildByName("id").getText().charAt(0);
				name = room.getChildByName("name").getText();
				roomList.put(id, new Room(id, name));
			}
			
			FileHandle levelTxt = Gdx.files.internal(filename + ".world");
			BufferedReader br = levelTxt.reader(200);

			columns = Integer.parseInt(br.readLine());
			rows = Integer.parseInt(br.readLine());

			char[][] charBoard = new char[columns][rows];
			char[][] input = new char[rows][columns];

			for (int i = 0; i < rows; i++) {
				input[i] = br.readLine().toCharArray();
			}

			// rotate (x=y, y=x) and flip the board (y=ySize-y)
			// [[1,2,3],[4,5,6]] -> [[5,6],[3,4],[1,2]]
			for (int x = 0; x < columns; x++) {
				for (int y = 0; y < rows; y++) {
					charBoard[x][rows - 1 - y] = input[y][x];
				}
			}

			br.close();

			// create entities
			for (int x = 0; x < columns; x++) {
				for (int y = 0; y < rows; y++) {
					if (Character.isLetterOrDigit(charBoard[x][y])) {
						EntityMaker.makeRoom(entityManager, x * Constants.GRID_CELL_SIZE / 2, y
								* Constants.GRID_CELL_SIZE / 2);
					} else if (charBoard[x][y] == '#') {
						// make wall
						EntityMaker.makeWall(entityManager, x * Constants.GRID_CELL_SIZE / 2, y
								* Constants.GRID_CELL_SIZE / 2);
					} else if (charBoard[x][y] == '-') {
						EntityMaker.makeHorizontalConnection(entityManager, x * Constants.GRID_CELL_SIZE / 2, y
								* Constants.GRID_CELL_SIZE / 2);
					} else if (charBoard[x][y] == '|') {
						EntityMaker.makeVerticalConnection(entityManager, x * Constants.GRID_CELL_SIZE / 2, y
								* Constants.GRID_CELL_SIZE / 2);
					}
					
					if(charBoard[x][y] == '1') {
						player = EntityMaker.makeIFPlayer(entityManager, x * Constants.GRID_CELL_SIZE / 2, y
								* Constants.GRID_CELL_SIZE / 2);
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
		backgroundManager.render();
		entityManager.render();
	}
}
