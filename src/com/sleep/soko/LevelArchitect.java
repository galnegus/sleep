package com.sleep.soko;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.sleep.Constants;
import com.sleep.CoolCamera;

public class LevelArchitect {
	private Map<Integer, Level> levels;
	public Level activeLevel;
	private CoolCamera camera;

	public LevelArchitect(String filename, CoolCamera camera) {
		this.camera = camera;

		levels = new HashMap<Integer, Level>();
		FileHandle worldTxt = Gdx.files.internal(filename);
		BufferedReader br = worldTxt.reader(20);

		try {
			String line;
			while (br.ready()) {
				line = br.readLine();
				levels.put(Integer.parseInt(line), new Level("levels/" + line + ".level"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		setLevel(2);
	}

	public void setLevel(int newLevel) {
		activeLevel = levels.get(newLevel);

		int width = activeLevel.columnCount() * Constants.GRID_CELL_SIZE;
		int height = activeLevel.rowCount() * Constants.GRID_CELL_SIZE;

		camera.resize(width, height, activeLevel.player.position.x + (activeLevel.player.getWidth() / 2),
				activeLevel.player.position.y + (activeLevel.player.getHeight()) / 2);
	}

	public void update() {
		activeLevel.update();
	}

	public void render() {
		activeLevel.render();
	}

	public void drawLight() {
		activeLevel.drawLight();
	}

	public void bindLight(int i) {
		activeLevel.bindLight(i);
	}
}
