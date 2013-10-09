package com.sleep;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;

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
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(levelTxt.file())));

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
	
	public Entity getEntityAt(Vector2 position) {
		return getEntityAt(position.x, position.y);
	}
	
	public Entity getEntityAt(float x, float y) {
		int gridX = (int) x / Constants.GRID_CELL_SIZE;
		int gridY = (int) y / Constants.GRID_CELL_SIZE;
		
		if(gridY < 0 || gridY < 0 || gridX >= xSize || gridY >= ySize)
			return null;
		else
			return grid[gridX][gridY];
	}
	
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
	
	public void update(float delta) {
		Entity temp;
		for(int x = 0; x < xSize; x++) {
			for(int y = 0; y < ySize; y++) {
				if(grid[x][y] != null) {
					if(grid[x][y] != getEntityAt(grid[x][y].getPosition())) {
						temp = grid[x][y];
						grid[x][y] = null;
						setEntityAt((temp.getPosition()), temp);
					}
				}
			}
		}
		
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
		
		int xPlayer = (int) GameScreen.player.getPosition().x / Constants.GRID_CELL_SIZE;
		int yPlayer = (int) GameScreen.player.getPosition().y / Constants.GRID_CELL_SIZE;
		Vector2 playerPos = new Vector2(xPlayer, yPlayer);
		distanceGrid[xPlayer][yPlayer] = 0;
		
		Deque<Vector2> queue = new ArrayDeque<Vector2>();
		Vector2[] moves = new Vector2[4];
		queue.add(playerPos);
		while(!queue.isEmpty()) {
			Vector2 currentPos = queue.pop();
			
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
	
	public void printDistanceGrid() {
		System.out.println("grid: ");
		for(int x = 0; x < xSize; x++) {
			for(int y = 0; y < ySize; y++) {
				System.out.print(distanceGrid[x][y] + "\t");
			}
			System.out.println();
		}
	}
}
