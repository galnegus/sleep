package com.sleep.soko;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.sleep.Constants;
import com.sleep.Entity;
import com.sleep.EntityMaker;
import com.sleep.EntityManager;

public class SokoLevel {
	private Entity[][] grid;
	public int[][] ghostPathGrid;
	public int[][] spectrePathGrid;
	private int columns, rows;

	public EntityManager entityManager;
	public EntityManager backgroundManager;
	public Entity player;

	public int columnCount() {
		return columns;
	}

	public int rowCount() {
		return rows;
	}

	public SokoLevel(String filename) {
		entityManager = new EntityManager();
		backgroundManager = new EntityManager();

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

			// rotate (x=y, y=x) and flip the board (y=ySize-y)
			// [[1,2,3],[4,5,6]] -> [[5,6],[3,4],[1,2]]
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
						grid[x][y] = EntityMaker.makeBox(this, x * Constants.GRID_CELL_SIZE, y
								* Constants.GRID_CELL_SIZE);
					} else if (charBoard[x][y] == Constants.PLAYER) {
						player = EntityMaker.makePlayer(this, x * Constants.GRID_CELL_SIZE, y
								* Constants.GRID_CELL_SIZE);
						grid[x][y] = player;
					} else if (charBoard[x][y] == Constants.WALL) {
						grid[x][y] = EntityMaker.makeWall(entityManager, x * Constants.GRID_CELL_SIZE, y
								* Constants.GRID_CELL_SIZE);
					} else if (charBoard[x][y] == Constants.GHOST) {
						grid[x][y] = EntityMaker.makeGhost(this, x * Constants.GRID_CELL_SIZE, y
								* Constants.GRID_CELL_SIZE);
					} else if (charBoard[x][y] == Constants.SPECTRE) {
						grid[x][y] = EntityMaker.makeSpectre(this, x * Constants.GRID_CELL_SIZE, y
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

						grid[x][y] = EntityMaker.makeSpawner(this, x * Constants.GRID_CELL_SIZE, y
								* Constants.GRID_CELL_SIZE, spawnerType.get(charBoard[x][y]),
								spawnerInit.get(charBoard[x][y]), spawnerFreq.get(charBoard[x][y]));
					}
				}
			}

			EntityMaker.makeGrid(backgroundManager, 0, 0, columns, rows);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Vector2 getGridPos(float x, float y) {
		int gridX = (int) x / Constants.GRID_CELL_SIZE;
		int gridY = (int) y / Constants.GRID_CELL_SIZE;

		return new Vector2(gridX, gridY);
	}

	public Entity getEntityAt(float x, float y) {
		Vector2 floorGridPos = getGridPos(x, y);

		if (floorGridPos.x < 0 || floorGridPos.y < 0 || floorGridPos.x >= columns || floorGridPos.y >= rows) {
			return null;
		} else {
			return grid[(int) floorGridPos.x][(int) floorGridPos.y];
		}

	}

	/**
	 * puts an entity on the grid at the given render position
	 */
	public Entity setEntityAt(Entity e, float x, float y) {
		Vector2 gridPos = getGridPos(x, y);

		if (gridPos.x < 0 || gridPos.y < 0 || gridPos.x >= columns || gridPos.y >= rows)
			return null;
		else
			return grid[(int) gridPos.x][(int) gridPos.y] = e;

	}

	public void removeEntity(Entity e) {
		Vector2 gridPos = getGridPos(e.position.x, e.position.y);
		if (grid[(int) gridPos.x][(int) gridPos.y] == e) {
			grid[(int) gridPos.x][(int) gridPos.y] = null;
		}
	}

	public Entity moveEntityTo(Entity e, float x, float y) {
		removeEntity(e);
		return setEntityAt(e, x, y);
	}

	public void update() {
		updateGhostPathGrid();
		updateSpectrePathGrid();

		entityManager.update();
	}

	public void render() {
		backgroundManager.render();
		entityManager.render();
	}

	public void drawLight() {
		entityManager.drawLight();
	}

	public void bindLight(int i) {
		entityManager.bindLight(i);
	}

	/**
	 * BFS search through the grid orthogonally, marking the distance in moves
	 * from any position to the player's position.
	 * 
	 * -2 == not visited
	 * -1 == visited but unreachable
	 * >= 0 == visited and reachable
	 */
	private void updateGhostPathGrid() {
		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < rows; y++) {
				ghostPathGrid[x][y] = -2;
			}
		}

		int xPlayer = (int) player.position.x / Constants.GRID_CELL_SIZE;
		int yPlayer = (int) player.position.y / Constants.GRID_CELL_SIZE;
		Vector2 playerPos = new Vector2(xPlayer, yPlayer);
		ghostPathGrid[xPlayer][yPlayer] = 0;

		LinkedList<Vector2> queue = new LinkedList<Vector2>();
		Vector2[] moves = new Vector2[4];
		queue.add(playerPos);
		while (!queue.isEmpty()) {
			Vector2 currentPos = queue.poll();

			moves[0] = new Vector2(currentPos.x - 1, currentPos.y);
			moves[1] = new Vector2(currentPos.x + 1, currentPos.y);
			moves[2] = new Vector2(currentPos.x, currentPos.y - 1);
			moves[3] = new Vector2(currentPos.x, currentPos.y + 1);

			for (Vector2 move : moves) {
				if (move.x >= 0 && move.x < columns && move.y >= 0 && move.y < rows) {
					if (ghostPathGrid[(int) move.x][(int) move.y] == -2) {
						if (grid[(int) move.x][(int) move.y] == null) {
							ghostPathGrid[(int) move.x][(int) move.y] = ghostPathGrid[(int) currentPos.x][(int) currentPos.y] + 1;
							queue.add(move);
						} else {
							ghostPathGrid[(int) move.x][(int) move.y] = -1;
						}

					}
				}

			}
		}
	}

	/**
	 * Works similarily to updateGhostPathGrid(), but searches through the grid
	 * diagonally rather than horizontally/vertically.
	 * 
	 * The BFS searches from up to 5 initial positions: the player's position,
	 * and any adjacent available (unoccupied) orthogonal position.
	 */
	private void updateSpectrePathGrid() {
		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < rows; y++) {
				spectrePathGrid[x][y] = -2;
			}
		}

		int xPlayer = (int) player.position.x / Constants.GRID_CELL_SIZE;
		int yPlayer = (int) player.position.y / Constants.GRID_CELL_SIZE;

		List<Vector2> startingPositions = new ArrayList<Vector2>();
		startingPositions.add(new Vector2(xPlayer, yPlayer));
		spectrePathGrid[xPlayer][yPlayer] = 0;
		if (grid[xPlayer - 1][yPlayer] == null) {
			startingPositions.add(new Vector2(xPlayer - 1, yPlayer));
			spectrePathGrid[xPlayer - 1][yPlayer] = 1;
		}
		if (grid[xPlayer + 1][yPlayer] == null) {
			startingPositions.add(new Vector2(xPlayer + 1, yPlayer));
			spectrePathGrid[xPlayer + 1][yPlayer] = 1;
		}
		if (grid[xPlayer][yPlayer - 1] == null) {
			startingPositions.add(new Vector2(xPlayer, yPlayer - 1));
			spectrePathGrid[xPlayer][yPlayer - 1] = 1;
		}
		if (grid[xPlayer][yPlayer + 1] == null) {
			startingPositions.add(new Vector2(xPlayer, yPlayer + 1));
			spectrePathGrid[xPlayer][yPlayer + 1] = 1;
		}

		for (Vector2 startingPos : startingPositions) {
			LinkedList<Vector2> queue = new LinkedList<Vector2>();
			Vector2[] moves = new Vector2[4];
			queue.add(startingPos);
			while (!queue.isEmpty()) {
				Vector2 currentPos = queue.poll();

				moves[0] = new Vector2(currentPos.x - 1, currentPos.y - 1);
				moves[1] = new Vector2(currentPos.x - 1, currentPos.y + 1);
				moves[2] = new Vector2(currentPos.x + 1, currentPos.y - 1);
				moves[3] = new Vector2(currentPos.x + 1, currentPos.y + 1);

				for (Vector2 move : moves) {
					if (move.x >= 0 && move.x < columns && move.y >= 0 && move.y < rows) {
						if (spectrePathGrid[(int) move.x][(int) move.y] == -2
								|| spectrePathGrid[(int) move.x][(int) move.y] > spectrePathGrid[(int) currentPos.x][(int) currentPos.y] + 2) {
							if (grid[(int) move.x][(int) move.y] == null) {
								spectrePathGrid[(int) move.x][(int) move.y] = spectrePathGrid[(int) currentPos.x][(int) currentPos.y] + 2;
								queue.add(move);
							} else {
								spectrePathGrid[(int) move.x][(int) move.y] = -1;
							}

						}
					}

				}
			}
		}

	}

	public int getPathDistance(int[][] pathGrid, int x, int y) {
		return pathGrid[x][y];
	}

	/**
	 * calculates manhattan distance from position to player
	 * 
	 * if position is occupied by an entity, return a number smaller than 0
	 * (-2).
	 */
	public int manhattanDistance(int x, int y) {
		if (grid[x][y] != null)
			return -2;

		Vector2 playerGridPos = getGridPos(player.position.x, player.position.y);

		return Math.abs((int) playerGridPos.x - x) + Math.abs((int) playerGridPos.y - y);
	}

	public void printGrid(int[][] distanceGrid) {
		System.out.println("distance grid: ");
		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < rows; y++) {
				System.out.print(distanceGrid[x][y] + "\t");
			}
			System.out.println();
		}
	}

	public void printGrid(char[][] distanceGrid) {
		System.out.println("distance grid: ");
		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < rows; y++) {
				System.out.print(distanceGrid[x][y]);
			}
			System.out.println();
		}
	}

	public void printGrid() {
		for (int i = 0; i < 5; i++) {
			System.out.println();
		}
		System.out.println("grid: ");
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < columns; x++) {
				if (grid[x][y] == null) {
					System.out.print(" ");
				} else if (grid[x][y].getName().equals("Player")) {
					System.out.print(Constants.PLAYER);
				} else if (grid[x][y].getName().equals("Wall")) {
					System.out.print(Constants.WALL);
				} else if (grid[x][y].getName().equals("Box")) {
					System.out.print(Constants.BOX);
				} else if (grid[x][y].getName().equals("Ghost")) {
					System.out.print(Constants.GHOST);
				} else if (grid[x][y].getName().equals("Spectre")) {
					System.out.print(Constants.SPECTRE);
				}

				// System.out.print("\t");
			}
			System.out.println();
		}
	}
}
