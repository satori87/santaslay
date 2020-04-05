package com.Teamic.GameObjects;

import com.Teamic.GameWorld.GameWorld;
import com.Teamic.Helpers.Random;

public class Santa extends Movable {

	public final int SANTACOLLISIONWIDTH = 12;
	public final int SANTACOLLISIONHEIGHT = 12;
	
	public final int SANTAWIDTH = 38;
	public final int SANTAHEIGHT = 34;
	
	public final int SANTAHP = 100;
	
	public int[] ammo;
	
	public final int CANDYCANERELOAD = 800;
	public final int ORNAMENTRELOAD = 1000;
	public final int STRANDRELOAD = 100;
	public final int GIFTRELOAD = 1200;
	
	public long reloadStamp = 0;
	
	public int firing = 0; //0 = not 1=firing, used int to expand for reload states, etc


	public int level = 0;
	public long levelStamp = 0;
	public int kills = 0;
	public int totalKills = 0;
	public int lives = 3;
	
	public int curWeapon = 0; //0 = candycane shooter 1 = ornament blaster 2 = laser 3 = gift launcher
	
	public Santa(GameWorld world, float X, float Y) {

		this.width = SANTAWIDTH;
		this.height = SANTAHEIGHT;
		this.X = X;
		this.Y = Y;
		speed = 60;
		destinationX = X;
		destinationY = Y;	
		this.world = world;
		hp = SANTAHP;
		maxhp = SANTAHP;
		ammo = new int[4];
		
		basicAmmo();
		
		collisionWidth = SANTACOLLISIONWIDTH;
		collisionHeight = SANTACOLLISIONHEIGHT;
						
	}

	public void update(float delta) {
		super.update(delta);
		
		if (world.isTouchDown && world.tick - world.touchStamp >= 200) {	
			setNewDestination(world.curTouchX, world.curTouchY);
		}		
		
		if (world.tick > reloadStamp) {firing = 0;}	

	}
	
	public void respawn() {
		X = world.SPAWNX;
		Y = world.SPAWNY;
		destinationX = X;
		destinationY = Y;
		hp = maxhp;
		lives -= 1;
		kills = 0;
		basicAmmo();
		
	}
	
	public void basicAmmo() {
		ammo[0] = 15;
		ammo[1] = 21;
		ammo[2] = 80;
		ammo[3] = 4;
	}
	
	public int getReloadTime(int weapon) {
		int t = 0;
		switch (weapon) {
		case 0:
			t = CANDYCANERELOAD;
			break;
		case 1:
			t = ORNAMENTRELOAD;
			break;
		case 2:
			t = STRANDRELOAD;
			break;
		case 3:
			t = GIFTRELOAD;
			break;
		}
		return t;
	}
	
	public void fire() {
		
		//if (X == destinationX && Y == destinationY) {//cant move and fire
		//} else {
			//stop moving so we can fire
			//destinationX = X;
			//destinationY = Y;
		//}
			if (world.tick > reloadStamp) { //have we reloaded?
				if(ammo[curWeapon] > 0) {
					reloadStamp = world.tick + getReloadTime(curWeapon);
					firing = 1;
					//First lets update our direction!
					float xDist = world.curTouchX - X;
					float yDist = world.curTouchY - Y;
					
					if (Math.abs(xDist) > Math.abs(yDist)) {
						if (xDist > 0) {dir = 3;} else {dir = 2;}
					} else {
						if (yDist > 0) {dir = 1;} else {dir = 0;}
					}
					ammo[curWeapon] -= 1;
					switch(curWeapon) {
					case 0:
						world.newBullet(1, X, Y, dir,0);
						break;
					case 1:						
						world.newBullet(2, X, Y, dir,0);
						if (ammo[curWeapon] > 0) {world.newBullet(2, X, Y, dir,200);ammo[curWeapon]-=1;}
						if (ammo[curWeapon] > 0) {world.newBullet(2, X, Y, dir,400);ammo[curWeapon]-=1;}				
						moveLock(400);
						break;
					case 2:
						int a,b;							
						//for(int i = 0; i < 3; i++) {
							a = (int) (Math.random() * 24) - 12;
							b = (int) (Math.random() * 24) - 12;			
							world.newBullet(3, X+a, Y+b, dir,0);
							//moveLock(40);
						//}
						break;
					case 3:
						world.newBullet(4, X, Y, dir,0);	
						moveLock(500);
						break;				
					}
				}
			}	
	}

	public void damage(int d) {
		
		hp -= d;		
		
		bleed(d*2,0,2);
		//bleed(d/2,2,0);
		bloodstream(3 + Random.getInt(d),7);
				
		if (hp <= 0) {
			died();
		}

	}
	
	public void died() {
		
		float spreadFactor = 4.8f;
		world.gameState = 6;
		world.diedAt = world.tick;
		bleed(600, 0, 2.5f * spreadFactor); //drops
		bleed(30 + Random.getInt(5), 1, 2.5f * spreadFactor); //big blood pile
		//bleed(3 + Random.getInt(7), 3, 5 * spreadFactor); //skin chunk 1		
		//bleed(3 + Random.getInt(7), 2, 5 * spreadFactor); //skin chunk 2
		//bleed(3 + Random.getInt(7), 6, 5 * spreadFactor); //skin chunk 3
		//if (Math.random() < .7) { //head
		//	bleed(1,5,5 * spreadFactor);
		//} else { //brain
		bleed(90 + Random.getInt(60),4,4*spreadFactor); 
		//}			
		
		//Many streams of blood
		bloodstream(100 + Random.getInt(15),15);

	}
	
	public void collidedEntity(Entity e) {
		//careful where you walk
	}
	
	public void collidedZombie(Zombie z) {
		//ewww gross
	}
	
	
	public void collidedPickup(Pickup p) {
	
		switch (p.type) {
		case 1: //cane
			ammo[0] += 8;
			if(ammo[0] > 100) {ammo[0] = 100;}
			break;
		case 2: //heart
			hp += 50;
			if(hp > maxhp) {hp = maxhp;}
			break;
		case 3: //oraments remember there are multiple ornament pickups!!
			ammo[1] += 4;
			if(ammo[1] > 200) {ammo[1] = 200;}
			break;
		case 4: //1up
			lives += 1;
			if (lives > 8) {
				lives = 8;
				hp = maxhp;
			}
			break;
		case 5: //star
			hp =maxhp;
			break;
		case 6: //strand
			ammo[2] += 50;
			if(ammo[2] > 600) {ammo[2] = 600;}
			break;
		case 7: //gifts
			ammo[3] += 7;
			if(ammo[3] > 400) {ammo[3] = 400;}
			break;
		case 8: //SNOWMAN FRIEND
			world.addCompanion(0,world.santa.X, world.santa.Y);
			
		}
		p.remove();		
		
	}

	public void tallyKill() {
		kills += 1;			
		if(kills >= (int)(Math.pow(level,1.05)*5.0f)) {
			startLevel(level + 1);
		}
	}
		
	public void startLevel(int lev) {		
		totalKills += kills;
		kills = 0;
		level = lev;		
		world.gameState = 5;
		levelStamp = world.tick + 3000;
		world.spawnModifier = 6.0f - ((float) level * 0.1f);
		if (world.spawnModifier < 1.0f) {world.spawnModifier = 1.0f;}
		world.zombieModifier += 0.1f;
		
	}
	
}
