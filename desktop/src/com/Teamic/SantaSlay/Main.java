package com.Teamic.SantaSlay;

import com.Teamic.SantaSlay.SSGame;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "SantaSlay";
		cfg.useGL20 = false;
		cfg.width = 900;
		cfg.height = 600;
		
		new LwjglApplication(new SSGame(), cfg);
	}
}
