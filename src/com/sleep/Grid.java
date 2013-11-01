package com.sleep;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

public class Grid {
	private Entity[][] grid;
	private int[][] ghostPathGrid;
	private int[][] spectrePathGrid;
	private int xSize, ySize;

	public int getXSize() {
		return xSize;
	}

	public int getYSize() {
		return ySize;
	}

	public Grid() {
		char[][] charBoard;
		try {
			FileHandle levelTxt = Gdx.files.internal("levels/level1");
			BufferedReader br = levelTxt.reader(200);

			xSize = Integer.parseInt(br.readLine());
			ySize = Integer.parseInt(br.readLine());

			charBoard = new char[xSize][ySize];
			grid = new Entity[xSize][ySize];
			ghostPathGrid = new int[xSize][ySize];
			spectrePathGrid = new int[xSize][ySize];

			for (int i = 0; i < xSize; i++) {
				charBoard[i] = br.readLine().toCharArray();
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
			for (int x = 0; x < charBoard.length; x++) {
				for (int y = 0; y < charBoard[x].length; y++) {
					if (charBoard[x][y] == ' ') {
						// do nothing
					} else if (charBoard[x][y] == Constants.BOX) {
						grid[x][y] = EntityFactory.makeBox(x * Constants.GRID_CELL_SIZE, y * Constants.GRID_CELL_SIZE);
					} else if (charBoard[x][y] == Constants.PLAYER) {
						grid[x][y] = EntityFactory.makePlayer(x * Constants.GRID_CELL_SIZE, y
								* Constants.GRID_CELL_SIZE);
					} else if (charBoard[x][y] == Constants.WALL) {
						grid[x][y] = EntityFactory.makeWall(x * Constants.GRID_CELL_SIZE, y * Constants.GRID_CELL_SIZE);
					} else if (charBoard[x][y] == Constants.GHOST) {
						grid[x][y] = EntityFactory
								.makeGhost(x * Constants.GRID_CELL_SIZE, y * Constants.GRID_CELL_SIZE);
					} else if (charBoard[x][y] == Constants.SPECTRE) {
						grid[x][y] = EntityFactory.makeSpectre(x * Constants.GRID_CELL_SIZE, y
								* Constants.GRID_CELL_SIZE);
					} else if (Character.isLetterOrDigit(charBoard[x][y])) {
						if (!spawnerInit.containsKey(charBoard[x][y])) {
							Gdx.app.error("GridError", "spawnerInit value for key '" + charBoard[x][y] + "' missing");
						}
						if (!spawnerFreq.containsKey(charBoard[x][y])) {
							Gdx.app.error("GridError", "spawnerFreq value for key '" + charBoard[x][y] + "' missing");
						}

						grid[x][y] = EntityFactory.makeSpawner(x * Constants.GRID_CELL_SIZE, y
								* Constants.GRID_CELL_SIZE, spawnerType.get(charBoard[x][y]),
								spawnerInit.get(charBoard[x][y]), spawnerFreq.get(charBoard[x][y]));
					}
				}
			}

			EntityFactory.makeGrid(ySize, xSize);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Vector2 getGridPos(Entity e) {
		return getGridPos(e.position.x, e.position.y);
	}

	public Vector2 getGridPos(Vector2 position) {
		return getGridPos(position.x, position.y);
	}

	public Vector2 getGridPos(float x, float y) {
		int gridX = (int) x / Constants.GRID_CELL_SIZE;
		int gridY = (int) y / Constants.GRID_CELL_SIZE;

		return new Vector2(gridX, gridY);
	}

	public Entity getEntityAt(Vector2 position) {
		return getEntityAt(position.x, position.y);
	}

	public Entity getEntityAt(float x, float y) {
		Vector2 floorGridPos = getGridPos(x, y);

		if (floorGridPos.x < 0 || floorGridPos.y < 0 || floorGridPos.x >= xSize || floorGridPos.y >= ySize) {
			return null;
		} else {
			return grid[(int) floorGridPos.x][(int) floorGridPos.y];
		}

	}

	public int getGhostDistanceAt(Vector2 position) {
		return getGhostDistanceAt(position.x, position.y);
	}

	public int getGhostDistanceAt(float x, float y) {
		return ghostPathGrid[(int) x][(int) y];
	}

	public int getSpectreDistanceAt(Vector2 position) {
		return getSpectreDistanceAt(position.x, position.y);
	}

	public int getSpectreDistanceAt(float x, float y) {
		return spectrePathGrid[(int) x][(int) y];
	}

	/**
	 * puts an entity on the grid at the given render position
	 */
	public Entity setEntityAt(Entity e, Vector2 position) {
		return setEntityAt(e, position.x, position.y);
	}

	public Entity setEntityAt(Entity e, float x, float y) {
		int gridX = (int) x / Constants.GRID_CELL_SIZE;
		int gridY = (int) y / Constants.GRID_CELL_SIZE;

		if (gridX < 0 || gridY < 0 || gridX >= xSize || gridY >= ySize)
			return null;
		else
			return grid[gridX][gridY] = e;

	}

	public void removeEntity(Entity e) {
		Vector2 gridPos = getGridPos(e.position);
		grid[(int) gridPos.x][(int) gridPos.y] = null;
	}

	public Entity moveEntityTo(Entity e, Vector2 position) {
		return moveEntityTo(e, position.x, position.y);
	}

	public Entity moveEntityTo(Entity e, float x, float y) {
		Vector2 gridPos = getGridPos(e.position);
		if (grid[(int) gridPos.x][(int) gridPos.y] == e) {
			grid[(int) gridPos.x][(int) gridPos.y] = null;
		}
		return setEntityAt(e, x, y);
	}

	public void update() {
		updateGhostPathGrid();
		updateSpectrePathGrid();

		// printGrid(spectrePathGrid);
	}

	/**
	 * -2 == not visited
	 * -1 == visited but unreachable
	 * >= 0 == visited and reachable
	 */
	private void updateGhostPathGrid() {
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				ghostPathGrid[x][y] = -2;
			}
		}

		int xPlayer = (int) Sleep.player.position.x / Constants.GRID_CELL_SIZE;
		int yPlayer = (int) Sleep.player.position.y / Constants.GRID_CELL_SIZE;
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
				if (move.x >= 0 && move.x < xSize && move.y >= 0 && move.y < ySize) {
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
	 * and any adjacent position horizontally or vertically that doesn't contain
	 * any box or wall.
	 */
	private void updateSpectrePathGrid() {
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				spectrePathGrid[x][y] = -2;
			}
		}

		int xPlayer = (int) Sleep.player.position.x / Constants.GRID_CELL_SIZE;
		int yPlayer = (int) Sleep.player.position.y / Constants.GRID_CELL_SIZE;

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
					if (move.x >= 0 && move.x < xSize && move.y >= 0 && move.y < ySize) {
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

	public void printGrid(int[][] distanceGrid) {
		System.out.println("distance grid: ");
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				System.out.print(distanceGrid[x][y] + "\t");
			}
			System.out.println();
		}
	}

	public void printGrid() {
		for (int i = 0; i < 5; i++) {
			System.out.println();
		}
		System.out.println("grid: ");
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
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
				}

				// System.out.print("\t");
			}
			System.out.println();
		}
	}
}
