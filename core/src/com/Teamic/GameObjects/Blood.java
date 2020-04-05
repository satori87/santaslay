package com.Teamic.GameObjects;

import com.Teamic.GameWorld.GameWorld;

public class Blood extends Movable {
	
	public final float ACCELERATION = 400f;
	
	public int bloodType = 0; //hehe

	public float XVelocity = 0;
	public float YVelocity = 0;
	
	public boolean foreground = true; //becomes background once it hits the ground
	
	public float initialY = 0;
	public float fallDistance = 0;
	
	public Blood(GameWorld world, int type,float X, float Y, int delay, int TTL, float initialXVelocity, float initialYVelocity, float spread) {
		this.X = X;
		this.Y = Y;
		initialY = Y;
		rotation = (float) Math.random() * 360;
		this.world = world;	
		this.collidable = false;
		diesAt = world.tick + TTL;
		willDieAt = true;
		this.XVelocity = initialXVelocity;
		this.YVelocity = initialYVelocity*1.5f;
		if (this.YVelocity == 0) {this.YVelocity = 1.0f;}
		fallDistance = (float) Math.random() * (spread / 10.0f);
		if (initialYVelocity < 0) {
			fallDistance = -fallDistance;
		} 
		//fallDistance = (float) (-(spread/20.0f) + Math.random() * (spread / 10.0f));
		fallDistance += 18;
		if (delay > 0) {
			aliveAt = world.tick + delay;
			aliveYet = false;
		}
		bloodType = type;
		this.width = 5;
		this.height = 5;
		switch (type) {
		case 0:
			this.width = 5;
			this.height = 4;
			break;
		case 1:
			this.width = 29;
			this.height = 34;
			rotation = 0;
			break;
		case 2:
			this.width = 6;
			this.height = 4;
			break;
		case 3:
			this.width = 5;
			this.height = 5;
			break;
		case 4:
			this.width = 6;
			this.height = 6;
			break;
		case 5:
			this.width = 29;
			this.height = 25;
			break;
		case 6:
			this.width = 4;
			this.height = 3;
			break;
		case 7:
			this.width = 8;
			this.height = 9;
			break;
		case 8:
			this.width = 19;
			this.height = 19;
			break;
		}
		
	}

	
	public void calcMove(float delta) {
		
		YVelocity += (ACCELERATION * delta);
		xChange = XVelocity * delta;
		yChange = YVelocity * delta;

	}
	
	public void checkMove() {
		
		collidedX = false;
		collidedY = false;
		
	}
	
	public void move() {
		
		if (foreground) {
			X += xChange;
			Y += yChange;
		}
		
	}
	
	public void validatePosition() {
		
		if(YVelocity > 0) {
			if (Y - initialY > fallDistance) {
				Y = initialY + fallDistance;
				foreground = false;
				XVelocity = 0;
				YVelocity = 0;
			}
		} 
	}
	
	public void collidedEntity(Entity e) {
		//perhaps we could have blood stick to certain objects,
		//but this will require more precise data than our bounding box collision provides
	}
	
}
