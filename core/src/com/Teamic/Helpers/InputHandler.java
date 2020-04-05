package com.Teamic.Helpers;

import com.Teamic.GameWorld.GameWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

public class InputHandler implements InputProcessor {
    private GameWorld world;

    // Ask for a reference to the Bird when InputHandler is created.
    public InputHandler(GameWorld world) {
        // myBird now represents the gameWorld's bird.
        this.world = world;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    	float clickX = ((float) screenX / Gdx.graphics.getWidth()) * world.viewWidth;
    	float clickY =  ((float) screenY / Gdx.graphics.getHeight()) * world.viewHeight;
        world.touchDown((int) clickX - world.originX, (int) clickY - world.originY, pointer, button);
        return true; // Return true to say we handled the touch.
    }

    @Override
    public boolean keyDown(int keycode) {
    	world.keyDown(keycode);
    	//System.out.print(Integer.toString(keycode));
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
    	world.keyUp(keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    	float clickX = ((float) screenX / Gdx.graphics.getWidth()) * world.viewWidth;
    	float clickY =  ((float) screenY / Gdx.graphics.getHeight()) * world.viewHeight;
    	world.touchUp((int) clickX - world.originX, (int) clickY - world.originY, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
    	float clickX = ((float) screenX / Gdx.graphics.getWidth()) * world.viewWidth;
    	float clickY =  ((float) screenY / Gdx.graphics.getHeight()) * world.viewHeight;
    	world.touchDragged((int) clickX - world.originX, (int) clickY - world.originY, pointer);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
