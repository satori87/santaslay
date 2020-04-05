package com.Teamic.GameObjects;

import com.Teamic.GameWorld.GameWorld;
import com.Teamic.Helpers.Coord;
import com.Teamic.Helpers.Random;

public class Snowman extends Movable {
	
	public long considerStamp = 0;
	public boolean isInRange = false;
	public int attackState = 0;
	public long attackStamp = 0;
	
	public final boolean FUCKSHITUP = true;
	
	public boolean fuckShitUp = FUCKSHITUP; //always
	
	public Snowman(GameWorld world, float X, float Y) {
		this.world = world;
		this.X = X;
		this.Y = Y;
		this.width = 64;
		this.height = 64;
		this.speed = 20;
		this.collisionWidth = 12;
		this.collisionHeight = 12;
		this.collisionYOff = 16;
		this.destinationX = X;
		this.destinationY = Y;
		hp = 100;
		maxhp = 100;
	}
	
	public void update(float delta) {
		super.update(delta);
		

		isInRange = inRange();
		if(world.tick > considerStamp) {
			considerStamp = world.tick + 900;
			if(!isInRange && (world.tick > attackStamp + 200)) {
				if(Coord.distance(X,Y,world.santa.X,world.santa.Y) > 40) {
					setNewDestination(world.santa.X,world.santa.Y);
				} else {					
					setNewDestination(X + Random.getInt(60) - 30,Y + Random.getInt(60) - 30);
				}
			}
		}
		
		
		
		
		
	}
	
	public boolean inRange() {

		if(world.tick > attackStamp) {
	
			float d;
			int tDir;
			float lowestD = 500;
			
			float startX=0,endX=0,startY=0,endY=0;
			
			Zombie closest = new Zombie(world,0,0,0);
			for(Zombie z : world.zombies) {
				d = Coord.distance(X, Y, z.X, z.Y);
				if (d < 480) { //range of snowballz
					//ok now check if they are lined up on either axis
					if(Math.abs(X-z.X) < 17 || Math.abs(Y-z.Y) < 17) {
						tDir = getDir(X,Y,z.X-X,z.Y-Y);
						switch(tDir) {
						case 0:
							startX = X - 4;
							startY = z.Y;
							endX = X + 4;
							endY = Y;
							break;
						case 1:
							startX = X - 4;
							startY = Y;
							endX = X + 4;
							endY = z.Y;
							break;
						case 2:
							startX = z.X;
							endX = X;
							startY = Y - 4;
							endY = Y + 4;
							break;
						case 3:
							startX = X;
							endX = z.X;
							startY = Y - 4;
							endY = Y + 4;
							break;								
						}
						if(world.clearShot(startX,endX,startY,endY)) {
							if (d < lowestD) {
								lowestD = d;
								closest = z;
							}
						}
					}
				}
			}
			
			if(lowestD < 490) {
				//stop moving and shoot
				attackState = 1;
				dir = getDir(X,Y,closest.X-X,closest.Y-Y);
				setNewDestination(X,Y);
				attackStamp = world.tick + 900;
				
				world.newBullet(5, X, Y, dir,0);
				world.newBullet(5, X, Y, dir,300);
				world.newBullet(5, X, Y, dir,600);			
				moveLock(900);
				return true;
			} else {
				attackState = 0;
			}
		} else {
			attackState = 1;
		}	
		return false;		
		
	}
	
	public void collidedEntity(Entity e) {
		
	}
	
	public void collidedZombie(Zombie z) {
		
	}
	
	public void damage(Zombie z) {
		float spreadFactor = 1.0f;
		hp -= z.strength;
		bleed(4, 8, 2.5f * spreadFactor); 
		bleed(2, 7, 1.5f * spreadFactor); 
		if(hp < 1) {
			died(z);
		}
		
	}
	
	public void died(Zombie z) {
		
		float spreadFactor = 4.0f;
		
		
		//snowblood
		bleed(200, 8, 2.5f * spreadFactor); 
		bleed(20, 7, 1.5f * spreadFactor); 
		
		
		//Many streams of blood
		//bloodstream(3 + Random.getInt(9),15);
		
		remove();

	}
	
		
	
}
