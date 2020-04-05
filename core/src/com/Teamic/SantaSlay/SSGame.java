package com.Teamic.SantaSlay;

import com.Teamic.GameWorld.GameScreen;
import com.Teamic.Helpers.AssetLoader;
import com.badlogic.gdx.Game;


public class SSGame extends Game {

    @Override
    public void create() {
        System.out.println("SSGame Created!");
        AssetLoader.load();
        setScreen(new GameScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }

}