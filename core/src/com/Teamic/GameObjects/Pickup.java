package com.Teamic.GameObjects;

import com.Teamic.GameWorld.GameWorld;
import com.Teamic.Helpers.Random;

public class Pickup extends Entity {

	GameWorld world;
	
	Spawner owner;
	
	public boolean dies = true;
	
	public int type = 0;
	
	public float floatY = 0;
	public float floatX = 0;
	
	public boolean floatingUp = true;
	public long floatStamp = 0;
	public int floatRange = 2;
	public int floatTime = 200;
	
	public Pickup(GameWorld world, Spawner owner, int type, float X, float Y, int TTL) {
		
		this.world = world;
		this.owner = owner;
		this.type = type;
		diesAt = world.tick + TTL;
		this.X = X;
		this.Y = Y;

		this.collisionWidth = 4;
		this.collisionHeight = 4;
		
		switch (type) {
		case 1:
			width = 9;
			height = 34;
			break;
		case 2:
			width = 17;
			height = 17;
			break;
		case 3:
			width = 5;
			height = 6;
			floatRange = 8;
			floatX = Random.getInt(11) - 5;
			floatY = Random.getInt(15) - 7;
			if(Random.getInt(2) == 1) {floatingUp = false;}
			floatTime = Random.getInt(100) + 100;
			
			break;
		case 4:
			width = 21;
			height = 17;
			break;
		case 5:
			width = 17;
			height = 17;
			break;
		case 6:
			width = 6;
			height = 32;
			break;
		case 7:
			width = 32;
			height = 32;
			break;	
		case 8:
			width = 16;
			height = 23;
			break;
		}

		
	}
	
	public void update(float delta) {
		
		if(world.tick > floatStamp) {
			floatStamp = world.tick + floatTime;
			if(floatingUp) {
				floatY -= 1;
				if(floatY == -floatRange) {floatingUp = false;}
			} else {
				floatY += 1;
				if(floatY == floatRange) {floatingUp = true;}
			}
		}
		
		if(dies && world.tick >= diesAt) {
			remove();
		}
		
	}
	
	public void remove() {
		super.remove();
		
		owner.take();
		
	}
	
}
