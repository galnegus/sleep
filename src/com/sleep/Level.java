package com.sleep;

import java.io.*;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

public class Level {
	private Entity[][] grid;
	private int[][] distanceGrid;
	private int xSize, ySize;
	
	public int getXSize() {
		return xSize;
	}

	public int getYSize() {
		return ySize;
	}

	public Level() {
		char[][] charBoard;
		try {
			FileHandle levelTxt = Gdx.files.internal("levels/test.txt");
			BufferedReader br = levelTxt.reader(200);

			xSize = Integer.parseInt(br.readLine());
			ySize = Integer.parseInt(br.readLine());
			
			charBoard = new char[xSize][ySize];
			grid = new Entity[xSize][ySize];
			distanceGrid = new int[xSize][ySize];
			
			int i = 0;
			while (br.ready()) {
				charBoard[i++] = br.readLine().toCharArray();
			}
			
			br.close();
		
			for(int x = 0; x < charBoard.length; x++) {
				for(int y = 0; y < charBoard[x].length; y++) {
					if(charBoard[x][y] == Constants.BOX){
						grid[x][y] = EntityFactory.makeBox(x * Constants.GRID_CELL_SIZE, y * Constants.GRID_CELL_SIZE);
					} else if(charBoard[x][y] == Constants.PLAYER) {
						grid[x][y] = EntityFactory.makePlayer(x * Constants.GRID_CELL_SIZE, y * Constants.GRID_CELL_SIZE);
					} else if(charBoard[x][y] == Constants.WALL){
						grid[x][y] = EntityFactory.makeWall(x * Constants.GRID_CELL_SIZE, y * Constants.GRID_CELL_SIZE);
					} else if(charBoard[x][y] == Constants.GHOST){
						grid[x][y] = EntityFactory.makeGhost(x * Constants.GRID_CELL_SIZE, y * Constants.GRID_CELL_SIZE);
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
	
	public Vector2 getGridPos(Vector2 position) {
		return getGridPos(position.x, position.y);
	}
	
	public Vector2 getGridPos(float x, float y) {
		int gridX = (int) x / Constants.GRID_CELL_SIZE;
		int gridY = (int) y / Constants.GRID_CELL_SIZE;
		
		return new Vector2(gridX, gridY);
	}
	
	/**
	 * retrieves an entity at the given render position.
	 * 
	 * as there can be 2 possible grid positions for a given render positions (technically 4, but only 2 are ever used), 
	 * both grid positions are checked for any entity.
	 * if no entity is found at the floor, the entity (or lack of) at the ceiling is returned.
	 */
	public Entity getEntityAt(Vector2 position) {
		return getEntityAt(position.x, position.y);
	}
	public Entity getEntityAt(float x, float y) {
		Vector2 floorGridPos = getGridPos(x, y);
		Vector2 ceilGridPos = new Vector2((float) Math.ceil(x / Constants.GRID_CELL_SIZE), (float) Math.ceil(y / Constants.GRID_CELL_SIZE));
		
		if(floorGridPos.x < 0 || floorGridPos.y < 0 || floorGridPos.x >= xSize || floorGridPos.y >= ySize) {
			return null;
		} else {
			Entity floorEntity = grid[(int) floorGridPos.x][(int) floorGridPos.y];
			Entity ceilEntity = grid[(int) ceilGridPos.x][(int) ceilGridPos.y];
			
			return floorEntity == null ? ceilEntity : floorEntity;
		}
			
	}
	
	/**
	 * puts an entity on the grid at the given render position
	 */
	public Entity setEntityAt(Vector2 position, Entity e) {
		return setEntityAt(position.x, position.y, e);
	}
	
	public Entity setEntityAt(float x, float y, Entity e) {
		int gridX = (int) x / Constants.GRID_CELL_SIZE;
		int gridY = (int) y / Constants.GRID_CELL_SIZE;
		
		if(gridY < 0 || gridY < 0 || gridX >= xSize || gridY >= ySize)
			return null;
		else 
			return grid[gridX][gridY] = e;
			
	}
	
	public Entity moveEntityTo(Vector2 position, Entity e) {
		return moveEntityTo(position.x, position.y, e);
	}
	
	public Entity moveEntityTo(float x, float y, Entity e) {
		Vector2 eGridPos = getGridPos(e.position);
		grid[(int) eGridPos.x][(int) eGridPos.y] = null;
		return setEntityAt(x, y, e);
	}
	
	public void update(float delta) {
		updateDistanceMatrix();
	}
	
	/**
	 * -2	== not visited
	 * -1	== visited but unreachable
	 * >= 0	== visited and reachable
	 */
	private void updateDistanceMatrix() {
		for(int x = 0; x < xSize; x++) {
			for(int y = 0; y < ySize; y++) {
				distanceGrid[x][y] = -2;
			}
		}
		
		int xPlayer = (int) GameScreen.player.position.x / Constants.GRID_CELL_SIZE;
		int yPlayer = (int) GameScreen.player.position.y / Constants.GRID_CELL_SIZE;
		Vector2 playerPos = new Vector2(xPlayer, yPlayer);
		distanceGrid[xPlayer][yPlayer] = 0;
		
		LinkedList<Vector2> queue = new LinkedList<Vector2>();
		Vector2[] moves = new Vector2[4];
		queue.add(playerPos);
		while(!queue.isEmpty()) {
			Vector2 currentPos = queue.poll();
			
			moves[0] = new Vector2(currentPos.x - 1, currentPos.y);
			moves[1] = new Vector2(currentPos.x + 1, currentPos.y);
			moves[2] = new Vector2(currentPos.x, currentPos.y - 1);
			moves[3] = new Vector2(currentPos.x, currentPos.y + 1);
			
			for(Vector2 move : moves) {
				if(move.x >= 0 && move.x < xSize && move.y >= 0 && move.y < ySize) {
					if(distanceGrid[(int) move.x][(int) move.y] == -2) {
						if(grid[(int) move.x][(int) move.y] == null) {
							distanceGrid[(int) move.x][(int) move.y] = distanceGrid[(int) currentPos.x][(int) currentPos.y] + 1;
							queue.add(move);
						} else {
							distanceGrid[(int) move.x][(int) move.y] = -1;
						}
						
					}
				}
				
			}
		}
	}
	
	/**
	 * looks in the distanceGrid for the move that brings the entity the closest to the player
	 * 
	 * @param mover the entity being moved
	 * @return a vector describing the movement (ie movement.x = -1, movement.y = 0 for a move going left)
	 */
	public Vector2 bestMove(Vector2 mover) {
		Vector2 movement = new Vector2(0, 0);
		Vector2 gridPos = getGridPos(mover);
		Vector2 player = getGridPos(GameScreen.player.position);
		
		int min = xSize * ySize;
		
		Vector2[] moves = new Vector2[4];
		moves[0] = new Vector2(gridPos.x - 1, gridPos.y);
		moves[1] = new Vector2(gridPos.x + 1, gridPos.y);
		moves[2] = new Vector2(gridPos.x, gridPos.y - 1);
		moves[3] = new Vector2(gridPos.x, gridPos.y + 1);
		
		for(Vector2 move : moves) {
			if(distanceGrid[(int) move.x][(int) move.y] < min && distanceGrid[(int) move.x][(int) move.y] >= 0) {
				min = distanceGrid[(int) move.x][(int) move.y];
				movement.x = move.x - gridPos.x;
				movement.y = move.y - gridPos.y;
			} else if(distanceGrid[(int) move.x][(int) move.y] == min) {
				if(Math.abs(Math.abs(player.x - gridPos.x) - Math.abs(player.y - gridPos.y)) 
						> Math.abs(Math.abs(player.x - move.x) - Math.abs(player.y - move.y))) {
					movement.x = move.x - gridPos.x;
					movement.y = move.y - gridPos.y;
				}
			}
		}
		
		return movement;
	}
	
	public void printDistanceGrid() {
		System.out.println("distance grid: ");
		for(int x = 0; x < xSize; x++) {
			for(int y = 0; y < ySize; y++) {
				System.out.print(distanceGrid[x][y] + "\t");
			}
			System.out.println();
		}
	}
	
	public void printGrid() {
		System.out.println("grid: ");
		for(int x = 0; x < xSize; x++) {
			for(int y = 0; y < ySize; y++) {
				if(grid[x][y] == null) {
					System.out.print(" ");
				} else if(grid[x][y].getName().equals("Player")) {
					System.out.print(Constants.PLAYER);
				} else if(grid[x][y].getName().equals("Wall")) {
					System.out.print(Constants.WALL);
				} else if(grid[x][y].getName().equals("Box")) {
					System.out.print(Constants.BOX);
				} else if(grid[x][y].getName().equals("Ghost")) {
					System.out.print(Constants.GHOST);
				}
				
				//System.out.print("\t");
			}
			System.out.println();
		}
	}
}
