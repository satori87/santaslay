package com.Teamic.GameObjects;

import com.Teamic.GameWorld.GameWorld;
import com.Teamic.Helpers.Coord;
import com.Teamic.Helpers.Random;

public class Zombie extends Movable {

	public final int ZOMBIECOLLISIONWIDTH = 12;
	public final int ZOMBIECOLLISIONHEIGHT = 12;
	
	public final int ZOMBIEWIDTH = 36;
	public final int ZOMBIEHEIGHT = 36;
	
	public int attackTime = 900;
	public long attackStamp = 0;
	
	public int task = 0; //0 = idle, 1 = move towards santa
	
	public int zombieType = 0;
	

	
	public float oldSpeed = 0;

	public boolean inRange = false;
	
	public float attackX = 0;
	public float attackY = 0;
	public int attackDir = 0;
	public int attackLength = 8; //how far the tackle teaches
	public int attackState = 0; //0 = not attacking, 1 = moving back to half the length, 2 = moving to attack length
	
	
	public int strength = 0;
	
	public int target = 0; //0 = santa 1 = a snowman
	public float targetX = 0;
	public float targetY = 0;
	public Snowman targetSnowman;
	
	
	public Zombie(GameWorld world, int type, float X, float Y) {
		
		this.world = world;
		
		this.zombieType = type;		
		

		
		this.X = X;
		this.Y = Y;
		
		this.width = ZOMBIEWIDTH;
		this.height = ZOMBIEHEIGHT;
		collisionWidth = ZOMBIECOLLISIONWIDTH;
		collisionHeight = ZOMBIECOLLISIONHEIGHT;
		
		if(type == 6) { //big dude
			width = 48;
			height = 48;
			collisionWidth = 16;
			collisionHeight = 16;
		}
		
		speed = 5 + (int) (Math.random() * 15);
		maxhp = (int) ((15 + Math.random() * 10) * world.zombieModifier);
		strength = (int) ((5 + Math.random() * 8) * world.zombieModifier);
		hp = maxhp;
		
		task = 1;
		target();

								
	}
	
	public void target() {
		
		float dSanta = Coord.distance(X,Y, world.santa.X, world.santa.Y);
		float dSnowman = 800;
		float d;
		targetSnowman = null;
		for (Snowman s : world.snowmen) {
			d = Coord.distance(X,Y, s.X, s.Y);
			if (d < dSnowman && d < dSanta) {
				targetSnowman = s;
				dSnowman = d;
			}
		}
		
		if(targetSnowman == null) {
			destinationX = world.santa.X;
			destinationY = world.santa.Y;
			target = 0;
		} else {
			destinationX = targetSnowman.X;
			destinationY = targetSnowman.Y;
			target = 1;
		}
		targetX = destinationX;
		targetY = destinationY;
		
	}
	
	public void remove() {
		
		if(!willDieAt) {
			willDieAt = true;
			diesAt = world.tick + 200;
		} else {
			super.remove();
		}		
		
	}
	
	public void checkMove() {
		
		if(attackState == 0) {
			super.checkMove();
		} else {
			collidedX = false;
			collidedY = false;
		}
		
	}
	
	public void update(float delta) {
		

		super.update(delta);
		
		if(attackState == 0 ){ 
			target();
		}
		
		if(inRange && !willDieAt && attackState == 0) { //are we in range and not dead
			//get that fat gift-givin sonnabitch (or his damn snowman thing)
			attack();
		}
	
		inRange = false; //for next tick, make sure we are still in range
		
		
		
		
	}
	public void move() {
		
		if(attackState == 0) {
			super.move();
		} else {
			attackX += xChange;
			attackY += yChange;
		}
		
		if (Coord.distance(X,Y,targetX,targetY) < 40) {
			inRange = true;
		}
		
		
		
	}
	
	public void calcMove(float delta) {
		
		if(attackState == 0) {
			super.calcMove(delta);
		} else {
			
			xChange = 0;
			yChange = 0;

			double h = Math.pow(Math.pow((destinationX - attackX),2) + Math.pow((destinationY - attackY),2),0.5);
			double theta;
			//if I remember correctly I can come back and use atan instead of asin
			//this will avoid using hypotenuse, and therefore avoid the expensive distance formula
			
			if (destinationY != attackY) {			
				theta = Math.asin((destinationY - attackY) / h);
				yChange = (float) Math.sin(theta) * speed * delta;		
			}
						
			if (destinationX != attackX) {	
				theta = Math.acos((destinationX - attackX) / h);
				xChange = (float) Math.cos(theta) * speed * delta;		
			}		
			
		}
		
	}
	
	public void validatePosition() {
		
		if(attackState == 0) {
			super.validatePosition();
		} else {
			if(Math.abs(destinationX - attackX) < 1.0 && Math.abs(destinationY - attackY) < 1.0) {
				if(attackState == 1) { //we finished initial backing up
					attackState = 2; //go for enemy
					destinationX = X - ((X - targetX)/4);
					destinationY = Y - ((Y - targetY)/4);
					speed = 80.0f;
				} else if (attackState == 2) { //we hit enemy
					if(target == 0) {
						world.santa.damage(strength);
					} else if (targetSnowman != null){
						targetSnowman.damage(this);
					}
					attackState = 3; //return to our spot
					destinationX = X;
					destinationY = Y;
					speed = 50;
				} else if (attackState == 3) { //we returned to our spot
					attackState = 0;
					speed = oldSpeed;
					target();
				}
			}
		}
		
	}
	
	public void attack() {
		if (world.tick > attackStamp) {
			attackState = 1;
			attackX = X;
			attackY = Y;

			attackDir = getAttackDir();
			oldSpeed = speed;
			speed = 50;
			destinationX = X - ((targetX - X)/8);
			destinationY = Y - ((targetY - Y)/8);
			attackStamp = world.tick + attackTime;
		}
		
	}
	
	public int getAttackDir() {
		int dir = 0;
		
		float xChange = X - targetX;
		float yChange = Y - targetY;
		
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
	
	public void collidedEntity(Entity e) {
		//you're probably stuck on it too
	}
	
	public void collidedZombie(Zombie z) {
		//we already know this is a different zombie
	}
	
	public void collidedBullet(Bullet b) {
		//TAKE THAT
		b.remove();

		int d = 0;
		
		//stubby stub
		switch (b.bulletType) {
		case 1:
			d = 0;
			break;
		case 2:
			d = 7;
			break;
		case 3:
			d = 4;
			break;
		case 4:
			d = 0;
			break;
		case 5:
			d = 3;
			break;
		}
		if (d > 0) {damage(d,b.bulletType);}

	}
	
	public void damage(int d, int damageType) {
		
		if(hp > 0) {
			if (!remove) {
				hp -= d;
				
				bleed(d,0,2);
				bleed(d/2,2,0);
				bloodstream(1 + Random.getInt(d/2),7);
						
				if (hp <= 0) {
					world.santa.tallyKill();
					died(damageType);
				}
			}
		}
		
	}
	
	public void died(int damageType) {
		
		float spreadFactor = 1.0f;
		
		switch (damageType) {
		case 1:
			spreadFactor = 0.8f;
			break;
		case 2:
			spreadFactor = 1.2f;
			break;
		case 3:
			spreadFactor = 0.7f;
			break;
		case 4:
			spreadFactor = 2.2f;
			break;
		
		}

		bleed(30, 0, 2.5f * spreadFactor); //drops
		bleed(1 + Random.getInt(2), 1, 2.5f * spreadFactor); //big blood pile
		bleed(3 + Random.getInt(7), 3, 5 * spreadFactor); //skin chunk 1		
		bleed(3 + Random.getInt(7), 2, 5 * spreadFactor); //skin chunk 2
		bleed(3 + Random.getInt(7), 6, 5 * spreadFactor); //skin chunk 3
		if (Math.random() < .7) { //head
			bleed(1,5,5 * spreadFactor);
		} else { //brain
			bleed(1 + Random.getInt(6),4,4*spreadFactor); 
		}			
		
		//Many streams of blood
		bloodstream(3 + Random.getInt(9),15);
		
		remove();

	}
	
	public void collidedSanta(Santa s) {
		
	}
	
}
