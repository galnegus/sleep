package com.sleep.soko;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.sleep.Constants;
import com.sleep.Entity;
import com.sleep.EntityMaker;

public class SokoLevelParser {
	private Entity[][] grid;
	public int[][] ghostPathGrid;
	public int[][] spectrePathGrid;
	private int columns, rows;

	public Entity player;

	public SokoLevelParser(String filename, SokoLevel level) {
		try {
			FileHandle levelTxt = Gdx.files.internal(filename);
			BufferedReader br = levelTxt.reader(200);

			columns = Integer.parseInt(br.readLine());
			rows = Integer.parseInt(br.readLine());

			grid = new Entity[columns][rows];
			ghostPathGrid = new int[columns][rows];
			spectrePathGrid = new int[columns][rows];

			char[][] charBoard = new char[columns][rows];
			char[][] input = new char[rows][columns];

			for (int i = 0; i < rows; i++) {
				input[i] = br.readLine().toCharArray();
			}

			// rotate (x=y, y=x) and flip the board vertically (y=ySize-y)
			// so that the matrix indices matches the opengl coordinates
			//
			// [1,2,3] -> [5,6]
			// [4,5,6] -> [3,4]
			// XXXXXXX -> [1,2]

			for (int x = 0; x < columns; x++) {
				for (int y = 0; y < rows; y++) {
					charBoard[x][rows - 1 - y] = input[y][x];
				}
			}

			// parse spawner timings
			String[] line;
			Map<Character, String> spawnerType = new HashMap<Character, String>();
			Map<Character, Float> spawnerInit = new HashMap<Character, Float>();
			Map<Character, Float> spawnerFreq = new HashMap<Character, Float>();
			while (br.ready()) {
				line = br.readLine().split(" ");
				spawnerType.put(line[0].charAt(0), line[1]);
				spawnerInit.put(line[0].charAt(0), Float.parseFloat(line[2]));
				spawnerFreq.put(line[0].charAt(0), Float.parseFloat(line[3]));
			}

			br.close();

			// create entities
			for (int x = 0; x < columns; x++) {
				for (int y = 0; y < rows; y++) {
					if (charBoard[x][y] == ' ') {
						// do nothing
					} else if (charBoard[x][y] == Constants.BOX) {
						grid[x][y] = EntityMaker.makeBox(level, x * Constants.GRID_CELL_SIZE, y
								* Constants.GRID_CELL_SIZE);
					} else if (charBoard[x][y] == Constants.PLAYER) {
						player = EntityMaker.makePlayer(level, x * Constants.GRID_CELL_SIZE, y
								* Constants.GRID_CELL_SIZE);
						grid[x][y] = player;
					} else if (charBoard[x][y] == Constants.WALL) {
						grid[x][y] = EntityMaker.makeWall(level.entityManager, x * Constants.GRID_CELL_SIZE, y
								* Constants.GRID_CELL_SIZE);
					} else if (charBoard[x][y] == Constants.GHOST) {
						grid[x][y] = EntityMaker.makeGhost(level, x * Constants.GRID_CELL_SIZE, y
								* Constants.GRID_CELL_SIZE);
					} else if (charBoard[x][y] == Constants.SPECTRE) {
						grid[x][y] = EntityMaker.makeSpectre(level, x * Constants.GRID_CELL_SIZE, y
								* Constants.GRID_CELL_SIZE);
					} else if (Character.isLetterOrDigit(charBoard[x][y])) {
						if (!spawnerInit.containsKey(charBoard[x][y])) {
							Gdx.app.error("LevelFormattingError", "spawnerInit value for key '" + charBoard[x][y]
									+ "' missing");
						}
						if (!spawnerFreq.containsKey(charBoard[x][y])) {
							Gdx.app.error("LevelFormattingError", "spawnerFreq value for key '" + charBoard[x][y]
									+ "' missing");
						}

						grid[x][y] = EntityMaker.makeSpawner(level, x * Constants.GRID_CELL_SIZE, y
								* Constants.GRID_CELL_SIZE, spawnerType.get(charBoard[x][y]),
								spawnerInit.get(charBoard[x][y]), spawnerFreq.get(charBoard[x][y]));
					}
				}
			}

			EntityMaker.makeGrid(level, 0, 0, columns, rows);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Entity[][] getGrid() {
		return grid;
	}

	public int[][] getGhostPathGrid() {
		return ghostPathGrid;
	}

	public int[][] getSpectrePathGrid() {
		return spectrePathGrid;
	}

	public int getColumns() {
		return columns;
	}

	public int getRows() {
		return rows;
	}

	public Entity getPlayer() {
		return player;
	}

}
