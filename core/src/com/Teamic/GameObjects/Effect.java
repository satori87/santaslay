package com.Teamic.GameObjects;

import com.Teamic.GameWorld.GameWorld;

public class Effect extends Entity {

	public int frame = 0;
	public long frameStamp = 0;
	public int maxFrames = 8;
	
	public int speed = 60;
	public int dir = 0;
	public int type = 0; //parallels bullet type and is also +1 from weapon type
	public boolean delayed = false;

	GameWorld world;
	
	public Effect(GameWorld world, int type, float X, float Y, int delay) {
		
		this.world = world;
		this.X = X;
		this.Y = Y;
		this.type = type;
		this.width = 32;
		this.height = 32;
		this.collidable = false;
		
		if (delay > 0) {
			aliveAt = world.tick + delay;
			delayed = true;
		}
		frameStamp = world.tick + speed;
		
		
	}
	
	public void update(float delta) {
		
		if(!delayed) {
			if (world.tick > frameStamp) {
				frame++;
				if(frame < maxFrames) {
					frameStamp = world.tick + speed;
				} else {
					remove();
				}
			}
		} else if (world.tick > aliveAt) {
			delayed = false;
		}
		
	}
	
}
