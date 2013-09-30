package gamedemo;

import gamedemo.component.render.BackgroundRenderComponent;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

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

	public Level(String path) throws SlickException {
		char[][] charBoard;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));

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
	
	public Entity getEntityAt(Vector2f position) {
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
	
	public Entity setEntityAt(Vector2f position, Entity e) {
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
	
	public void update(GameContainer gc, StateBasedGame sb, int delta)
			throws SlickException {
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
		
		int xPlayer = (int) Demo.player.getPosition().x / Constants.GRID_CELL_SIZE;
		int yPlayer = (int) Demo.player.getPosition().y / Constants.GRID_CELL_SIZE;
		Vector2f playerPos = new Vector2f(xPlayer, yPlayer);
		distanceGrid[xPlayer][yPlayer] = 0;
		
		Deque<Vector2f> queue = new ArrayDeque();
		Vector2f[] moves = new Vector2f[4];
		queue.add(playerPos);
		while(!queue.isEmpty()) {
			Vector2f currentPos = queue.pop();
			
			moves[0] = new Vector2f(currentPos.x - 1, currentPos.y);
			moves[1] = new Vector2f(currentPos.x + 1, currentPos.y);
			moves[2] = new Vector2f(currentPos.x, currentPos.y - 1);
			moves[3] = new Vector2f(currentPos.x, currentPos.y + 1);
			
			for(Vector2f move : moves) {
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
