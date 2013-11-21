package com.sleep;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class World {
	private Map<Integer, Level> levels;
	public Level activeLevel;

	public World(String filename) {
		levels = new HashMap<Integer, Level>();
		FileHandle worldTxt = Gdx.files.internal(filename);
		BufferedReader br = worldTxt.reader(20);

		try {
			String[] line;
			while (br.ready()) {
				line = br.readLine().split(" ");
				levels.put(Integer.parseInt(line[0]), new Level("levels/" + line[1]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setLevel(2);
	}

	public void setLevel(int newLevel) {
		activeLevel = levels.get(newLevel);

		// Sleep.camera.resize(width, height);
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
