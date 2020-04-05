package com.Teamic.GameWorld;

import com.Teamic.Helpers.InputHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class GameScreen implements Screen {

	private GameWorld world;
	private GameRenderer renderer;
	private float runTime;

	//These are the constants for logic resolution. THEY MUST BE 3:2
	private final float gameWidth = 480;
	private final float gameHeight = 320;
	
	private float viewWidth, viewHeight;
	
	private int oX;
	private int oY;
	
	private float screenWidth;
	private float screenHeight;
	
	// This is the constructor, not the class declaration
	public GameScreen() {		
				
		world = new GameWorld();
		
		initEngine();		
		
		Gdx.input.setInputProcessor(new InputHandler(world));

	}

	public void initEngine() {
		
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
				
		float r = (float) screenWidth / (float) screenHeight;
		
		if (r == 1.5) { //3:2
			oX = 0;
			oY = 0;
			viewWidth = gameWidth;
			viewHeight = gameHeight;
		} else if (r > 1.5) { //16:10, 17:10, 16:9
			viewWidth = gameHeight * r;
			viewHeight = gameHeight;
			oX = (int) ((viewWidth - gameWidth) / 2.0f);
			oY = 0;
		} else if (r < 1.5) { //4:3
			viewWidth = gameWidth;
			viewHeight = gameWidth / r;
			oX = 0;
			oY = (int) ((viewHeight - gameHeight) / 2.0f);
		}
		
		world.originX = oX;
		world.originY = oY;
		world.viewWidth = viewWidth;
		world.viewHeight = viewHeight;
		world.gameWidth = gameWidth;
		world.gameHeight = gameHeight;
		renderer = new GameRenderer(world, gameWidth, gameHeight, oX, oY, viewWidth, viewHeight);
		
	}
	
	@Override
	public void render(float delta) {
		runTime += delta;
		world.update(delta);
		renderer.render(runTime);
	}

	@Override
	public void resize(int width, int height) {
		System.out.println("GameScreen - resizing");
		initEngine();
	}

	@Override
	public void show() {
		System.out.println("GameScreen - show called");
	}

	@Override
	public void hide() {
		System.out.println("GameScreen - hide called");
	}

	@Override
	public void pause() {
		if(world != null) {
			world.paused = true;
		}
		System.out.println("GameScreen - pause called");
	}

	@Override
	public void resume() {
		if(world != null) {
			world.paused = false;
		}
		System.out.println("GameScreen - resume called");
	}

	@Override
	public void dispose() {
		// Leave blank
	}

}
