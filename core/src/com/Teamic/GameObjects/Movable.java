package com.Teamic.GameObjects;

import com.Teamic.GameWorld.GameWorld;
import com.Teamic.Helpers.Random;

public class Movable extends Entity {
	
	public GameWorld world;
	
	public float destinationX = 0, destinationY = 0;
	public float moveX = 0, moveY = 0;
	public float yChange = 0, xChange = 0;
	public float speed = 0;
	public int dir = 0; //0 = up, 1 = down, 2 = left, 3 = right

	public long diesAt = 0;
	public long aliveAt = 0;
	public boolean aliveYet = true;
	public boolean willDieAt = false;
	
	public boolean collidedX = false;
	public boolean collidedY = false;
	
	public float hp = 0;
	public float maxhp = 0;
	
	public long bleedStamp = 0;
	
	public int bleeding = 0; //scale 0 to 10
	
	
	public boolean moveLocked = false; //for triple shot ornament
	public long lockStamp = 0;
	
	
	public Movable() {
		
	}
	
	public Movable(GameWorld world) {
		this.world = world;
	}
	
	//these are empty and exist only to be overridden, they are not abstract to avoid santa having a collidedSanta, etc
	public void collidedBullet(Bullet b) {
		
	}
	public void collidedZombie(Zombie z) {
		
	}
	public void collidedSanta(Santa s) {
		
	}
	
	public void collidedSnowman(Snowman s) {
		
	}
	
	public void remove() {
		super.remove();
		
	}
	
	public void update(float delta) {
		super.update(delta);
			
		if(willDieAt && world.tick >= diesAt) {
			remove();
		} else if (aliveYet) {
			calcMove(delta);
			checkMove();
			if (!moveLocked) {
				move();
			} else if (world.tick > lockStamp) {
				moveLocked = false;
			}
			
			bleeding = calcBleeding();
			
			if (world.tick > bleedStamp && this.getClass() != Snowman.class) {
				//BLEED
				if (bleeding >  (Math.random() * 40)) {
					bleedStamp = world.tick + 200;
					bleed(1,0,1.5f);
					if (bleeding > 7) {
						bloodstream(0 + Random.getInt(2),4);
					}
				}
			}
			
			if (world.tick > bleedStamp && this.getClass() == Snowman.class) {
				//BLEED
				if (bleeding >  (Math.random() * 40)) {
					bleedStamp = world.tick + 200;
					bleed(1,7,1.5f);
					if (bleeding > 7) {
						bleed(1,8,1.5f);
						//bloodstream(0 + Random.getInt(2),4);
					}
				}
			}
			
			validatePosition();
		} else if (world.tick >= aliveAt) {	
			aliveYet = true;
		}	
		
		
		
	}
	
	public int calcBleeding() {
		 if (hp / maxhp < .8) { //bleeding
			return bleeding = (int)(10-(hp/maxhp)*10.0f);
		} else {
			return 0;
		}
		
	}
	
	public void moveLock(int duration) {
		
		moveLocked = true;
		lockStamp = world.tick + 100;
		
	}
	
	public void calcMove(float delta) {
		
		xChange = 0;
		yChange = 0;
		
		
		int d;
		
		double h = Math.pow(Math.pow((destinationX - X),2) + Math.pow((destinationY - Y),2),0.5);
		double theta;
		//if I remember correctly I can come back and use atan instead of asin
		//this will avoid using hypotenuse, and therefore avoid the expensive distance formula
		
		if (destinationY != Y) {			
			theta = Math.asin((destinationY - Y) / h);
			yChange = (float) Math.sin(theta) * speed * delta;		
		}
					
		if (destinationX != X) {	
			theta = Math.acos((destinationX - X) / h);
			xChange = (float) Math.cos(theta) * speed * delta;		
		}	
		
		moveX = X + xChange;
		moveY = Y + yChange;
		
		//dont change direction if moveLocked
		if (!moveLocked) {
			d = getDir(X, Y, xChange, yChange);
			if (d != -1) {dir = d;}
		}
		

		
	}
	
	public static int getDir(float X, float Y, float xChange, float yChange) {
		int dir = -1;
		//float xChange = X2 - X1;
		//float yChange = Y1 - Y2;
		
		if (Math.abs(xChange) > Math.abs(yChange))  {
			if (xChange > 0) {
				dir = 3; //right
			} else if (xChange < 0) {
				dir = 2; //left
			}
		} else if (yChange < 0) {
			dir = 0; //up
		} else if (yChange > 0) {dir = 1;} //down		
		return dir;
	}
	
	public void checkMove() {
				
		collidedX = false;
		collidedY = false;

		if(collidable) {
			for (Entity e : world.entities) {
				if (checkCollision(e)) {
					collidedEntity(e);
				}
			}
			
			if(this.getClass() == Santa.class) {
				for (Pickup p : world.pickups) {
					if (checkCollision(p)) {
						world.santa.collidedPickup(p);
					}
				}
			} else if(this.getClass() != Bullet.class) { //only zombies collide with snowmen
				for (Snowman s : world.snowmen) {
					if (checkCollision(s)) {
						collidedSnowman(s);
						//world.santa.collided(s);
					}
				}
			}
			
			if(this.getClass() != Bullet.class) {
				
				if(this.getClass() != Santa.class) {
					for (Bullet b : world.bullets) {
						if (checkCollision(b)) {
							collidedBullet(b);
						}
					}	
				}
			
				for (Zombie z : world.zombies) {
					if (checkCollision(z)) {
						collidedZombie(z);
					}
				}
			
				if(world.santa != null && this != world.santa) {
					if (checkCollision(world.santa)) {
						collidedSanta(world.santa);
					}
				}
			} else {
				
			}
		}	
		
			
	}
	
	public boolean checkCollision(Entity e) {
		
		boolean collided = false;
		
		if(!e.collidable || this == e || e.remove) {return false;}
				
		int combWidth = collisionWidth + e.collisionWidth;
		int combHeight = collisionHeight + e.collisionHeight;
		float eX = e.X + e.collisionXOff;
		float eY = e.Y + e.collisionYOff;
		
		float mX = moveX + collisionXOff;
		float mY = moveY + collisionYOff;
	
		
		float cX = X + collisionXOff;
		float cY = Y + collisionYOff;
		
		if (Math.abs(mX - eX) < combWidth && Math.abs(cY - eY) < combHeight) {
			collidedX = true;
			collided = true;
			if (Math.abs(cX - eX) < combWidth && Math.abs(mY - eY) < combHeight) {
				collidedY = true;
			}
		} else if (Math.abs(mX - eX) < combWidth && Math.abs(mY - eY) < combHeight) {
			collidedY = true;
			collided = true;
		}
		return collided;
	}
	
	public void move() {

		if (!collidedX) {X += xChange;}
		if (!collidedY) {Y += yChange;}
		
	}
	
	public void validatePosition() {
		
		if(xChange > 0 && X > destinationX) {X = destinationX;}
		if(xChange < 0 && X < destinationX) {X = destinationX;}
		
		if(yChange > 0 && Y > destinationY) {Y = destinationY;}
		if(yChange < 0 && Y < destinationY) {Y = destinationY;}
		
	}
			
	public void setNewDestination(float X, float Y) {
		
		destinationX = X;
		destinationY = Y;
		
	}	
	
	public void bloodstream(int t, int l) {
		Blood b;
			float spread = 60;
			float fallDistance;
			float p,q,c;
			float XVelocity;
			float YVelocity;
			for(int n = 0; n < t; n++) {
				XVelocity = (float) (-(spread) + Math.random() * spread * 2);
				YVelocity = (float) (-(spread*3) + Math.random() * spread * 6);
				p = (float) (Math.random() * 16) - 8;
				q = (float) (Math.random() * 8) - 4;
				
				fallDistance = (float) (4 + (Math.random() * 24));
				
				for(int i = 0; i < l; i++) {
					c = (float) (10 * i);
					b = world.newBlood(0,X+p,Y+q,c,30000+c+(float)Math.random()*8000,XVelocity,YVelocity,spread);
					b.fallDistance = fallDistance - 3 + Random.getInt(3);
				}
			}
			
	}
	
	public void bleed(int t, int type, float spread) {
		float p,q,c;

		float XVelocity;
		float YVelocity;
		
		//spread *= 1.1;

		for(int i = 0; i < t; i++) {
			p = (float) (Math.random() * 16) - 8;
			q = (float) (Math.random() * 8) - 4;
			c = (float) Math.random() * 100;
			XVelocity = (float) ((spread * -40) + (Math.random() * spread * 80));
			YVelocity = (float) ((spread * -10) + (Math.random() * spread * 20));
			
			world.newBlood(type,X+p,Y+q,c,30000+c+(float)Math.random()*8000,XVelocity, YVelocity,spread * 100);
		}
		
	}
	

	public void collidedEntity(Entity e) {
		
	}
	
	
	
}
