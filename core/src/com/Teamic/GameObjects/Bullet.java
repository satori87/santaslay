package com.Teamic.GameObjects;

import com.Teamic.GameWorld.GameWorld;

public class Bullet extends Movable {

	public int bulletType  = 0; //0 = unused in case list bug 1 = candycane 2 = ornament 3 = xmas lights 4 = present

	public Bullet(GameWorld world, int type, float X, float Y, int dir, int delay) {
		this.bulletType = type;
		this.X = X;
		this.Y = Y;
		this.dir = dir;
		this.world = world;
		width = 20;
		height = 20;

		if(dir == 0 || dir == 1)
		{
			collisionWidth = 8;
			collisionHeight = 12;
		} else {
			collisionWidth = 12;
			collisionHeight = 8;
		}
		if(type == 5) {
			width = 8;
			height = 9;
			collisionWidth = 4;
			collisionHeight = 4;
		}	
		
		diesAt = world.tick + 2000;
		this.speed = 200;
		//if(dir == 2 || dir == 3)
		if (delay > 0) {
			aliveAt = world.tick + delay;
			aliveYet = false;
		}
		
	}

	
	public void remove() {
		super.remove();
		
		int a,b;			
		
		trigger();
		
		switch (bulletType) {
		case 1:
			Effect e = world.newEffect(bulletType, X, Y,0);
			e.dir = dir;
			break;
		case 2:
			world.newEffect(bulletType, X, Y,0);
			break;
			
		case 3:
			for(int i = 0; i < 4; i++) {
				a = (int) (Math.random() * 48) - 24;
				b = (int) (Math.random() * 48) - 24;			
				world.newEffect(bulletType, X+a, Y+b,i*120);
			}
			break;
		case 4:								
			for(int i = 0; i < 50; i++) {
				a = (int) (Math.random() * 70) - 35;
				b = (int) (Math.random() * 70) - 35;			
				world.newEffect(bulletType, X+a, Y+b,i*10);
			}
			break;
		}
	}
	
	public void trigger() {
		
		switch (bulletType) {
		case 1:
			//FINALLY! candy canes are explosive
			world.newExplosion(0,X,Y,35.0f,50);
			break;
		case 4:
			//rocket go boom
			world.newExplosion(1,X,Y,40.0f,100);
			break;		
		}
		
	}
	
	public void calcMove(float delta) {
		
		moveX = X;
		moveY = Y;
		
		switch (dir) {
		case 0:
			moveY = Y - (speed * delta);
			break;
		case 1:
			moveY = Y + (speed * delta);
			break;
		case 2:
			moveX = X - (speed * delta);
			break;
		case 3:
			moveX = X + (speed * delta);
			break;			
		}		
		
		xChange = moveX - X;
		yChange = moveY - Y;
		
		destinationX = moveX;
		destinationY = moveY;
		
		if (moveX < 0 || moveX >= world.gameWidth || moveY < 0 || moveY >= world.gameHeight) {
			//Remove without detonating
			remove = true;
		}
		
	}	
	
	public void collidedEntity(Entity e) {
		remove();
		
	}	
	
}
