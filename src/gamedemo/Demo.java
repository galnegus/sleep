package gamedemo;

import gamedemo.component.render.BackgroundRenderComponent;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class Demo extends BasicGame {

	public static EntityManager backgroundManager;
	public static EntityManager entityManager;
	public static Music musicPlayer;
	public static Entity player;
	public static Level level;

	Camera camera;

	Demo() {
		super("Monkey Vengeance");
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		entityManager = new EntityManager();
		backgroundManager = new EntityManager();
		camera = new Camera(gc);
		
		//musicPlayer = new Music("/music/sleep.ogg");
		//musicPlayer.play();
		
		level = new Level("./levels/test.txt");
		
		gc.setTargetFrameRate(60);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		entityManager.update(gc, null, delta);
		backgroundManager.update(gc, null, delta);
		camera.update(gc, delta);
		level.update(gc, null, delta);
	}

	@Override
	public void render(GameContainer gc, Graphics gr) throws SlickException {
		camera.render(gr);
		backgroundManager.render(gc, null, gr);

		entityManager.render(gc, null, gr);
		
		camera.renderDone(gr);
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Demo());

		app.setDisplayMode(1280, 720, false);
		app.start();
	}

}
