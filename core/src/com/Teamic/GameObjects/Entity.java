package com.Teamic.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Entity {

	public float X = 0, Y = 0;
	public int width = 0, height = 0;
	public int collisionWidth = 0;
	public int collisionHeight = 0;
	public int collisionXOff = 0;
	public int collisionYOff = 0;
	
	public long diesAt = 0;
	public long aliveAt = 0;
	public boolean aliveYet = false;
	
	public boolean collidable = true;
	
	public boolean remove = false;
	
	public float considerYOff = 0;
	public boolean considerY = false;
	
	public int tile = 0;
	
	public float rotation = 0.0f;
	
	public boolean isSanta = false; //special flag only used for drawList for Santa
	
	public TextureRegion tex;
	
	public void remove() {
		
		remove = true;
		
	}
	
	public void update(float delta) {

	}
	

}
