package com.Teamic.GameWorld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.Teamic.GameObjects.Blood;
import com.Teamic.GameObjects.Bullet;
import com.Teamic.GameObjects.Spawner;
import com.Teamic.GameObjects.Effect;
import com.Teamic.GameObjects.Entity;
import com.Teamic.GameObjects.Movable;
import com.Teamic.GameObjects.Pickup;
import com.Teamic.GameObjects.Santa;
import com.Teamic.GameObjects.Snowman;
import com.Teamic.GameObjects.Zombie;
import com.Teamic.Helpers.Coord;
import com.Teamic.Helpers.Random;

public class GameWorld {

	public long tick = 0;
	public long stepTick = 0;
	public int walkStep = 0;
	public int healthStep = 0; //for candy cane health bar
	public int twoStep = 0; //two Step
	public Santa santa;
	
	public final int STEPTIMER = 150;
	public long zombieTimer = 0;
	
	public boolean paused = false;
	
	public final int TILESHIGH = 10;
	public final int TILESWIDE = 15;
	
	public final int XMASTREEX = 220;
	public final int XMASTREEY = 120;
	
	public final int SPAWNX = 240;
	public final int SPAWNY = 170;
	
	public final int RESPAWNTIMER = 7000;
	
	public float viewWidth, viewHeight, gameWidth, gameHeight;

	public int originX, originY;

	public long touchStamp = 0;
	public boolean isTouchDown = false;
	public int initialTouchX = 0;
	public int initialTouchY = 0;
	public int curTouchX = 0;
	public int curTouchY = 0;
	
	public String status;
	
	public float spawnModifier = 6.0f;
	public float zombieModifier = 1.0f;
	
	public long diedAt = 0;
	
	
	public int gameState = 0; //0 = init, 1 = splash, 2 = menu, 3 = tutorial, 4 = playing
	public int splashState = 0; //0 = showing studio logo, 1 = showing game logo
	public long splashStamp = 0;
	public float splashOffset = 0; //0 to 120
	public int tutorialState = 0;
	public long tutorialStamp = 0;

	public List<Bullet> bullets;
	public List<Effect> effects;	
	public List<Entity> entities;
	public List<Zombie> zombies;
	public List<Blood> blood;
	public List<Spawner> spawners;
	public List<Pickup> pickups;
	public List<Snowman> snowmen;
	
	public Movable bear;
	public Entity honey;
	public Movable cursor;
	public int cursorHeld = 0;
	
	
	public boolean[] isKeyDown;
	
	public GameWorld() {
		
		bullets = new ArrayList<Bullet>();
		effects = new ArrayList<Effect>();
		entities = new ArrayList<Entity>();
		zombies = new ArrayList<Zombie>();
		blood = new ArrayList<Blood>();
		spawners = new ArrayList<Spawner>();
		pickups = new ArrayList<Pickup>();
		snowmen = new ArrayList<Snowman>();		
		isKeyDown = new boolean[256];
		
		tick = System.currentTimeMillis();

		startSplash();	
		//startTutorial();
		//newGame();
	}
	
	public void startSplash() {			
		gameState = 1;
		//splashOffset = 140.0f;
		splashState = 0;
		splashStamp = tick + 2000;

		honey = new Entity();
		honey.width = 40;
		honey.height = 37;
		honey.collisionWidth = 0;
		honey.collisionHeight = 0;
		honey.collidable = false;
		honey.X = 240;
		honey.Y = 150;
		
		bear = new Movable(this);
		bear.width = 64;
		bear.height = 64;
		bear.X = 0;
		bear.Y = 200;
		bear.speed = 120;
		bear.destinationX = 200;
		bear.destinationY = 200;	

	}

	
	public void newGame() {	
		clearWorld(true);
		santa = new Santa(this, SPAWNX, SPAWNY);
		addXmasTree(XMASTREEX,XMASTREEY);
		addCanes();				
		randomSnowmen();
		randomSnowmen();
		randomTrees();		
		santa.startLevel(1);		
	}
		
	public void showMenu() {
		gameState = 2;
		clearWorld(true);
		splashOffset = 135.0f;
	}
	
	public void startTutorial() {
		
		clearWorld(true);
		addCane(160,110,0);
		gameState = 3;
		tutorialState = 1;
		tutorialStamp = tick + 5000;
		
		cursor = new Movable(this);
		cursor.X = 300;
		cursor.Y = 220;
		cursor.collidable = false;
		cursor.width = 23;
		cursor.height = 26;
		cursor.speed = 160;
		cursor.destinationX = SPAWNX;
		cursor.destinationY = SPAWNY;
		
		
		
		santa = new Santa(this, SPAWNX, SPAWNY);
		
	}
	
	public void updateSplash(float delta) {
		if(splashState == 1) {
			if (tick > splashStamp) {
				splashState = 2;
			}
		} else if (splashState == 2) {
			if(splashOffset < 135) {
				splashOffset += (delta * 200.0f);
			} else {
				showMenu();
				
			}
		} else if (splashState == 0) {
			bear.update(delta);
			if (bear.X >= 520) {
				splashState = 1;
				splashStamp = tick + 2000;
			} else if (bear.X >= 280) {
				bear.destinationX = 520;
				bear.speed = 400;
			} else if (bear.X >= 240) {
				honey.X = 520;
				bear.destinationY = 200;
				bear.destinationX = 280;
				bear.speed = 300;
			} else if(bear.X >= 200) {
				bear.destinationX = 240;
				bear.destinationY = 150;
				bear.speed = 300;
			}
		}
	}
	
	public void updateTutorial(float delta) {
		
		if(tutorialState > 0) 
		{
			boolean ticked = false;
			if(tick > tutorialStamp) {
				tutorialState += 1;
				tutorialStamp = tick + 5000;
				ticked = true;
			}
			
			cursor.update(delta);
			
			
			switch (tutorialState) {
			case 1:
				break;
			case 2:
				if (ticked) {//we just transitioned to 2
					cursor.destinationX = 340;
					cursor.destinationY = 100;
				} else if (cursor.X == 340 && cursor.Y == 100) {
					cursorHeld = 1;
					santa.setNewDestination(340, 100);
				}
				break;
			case 3:
				if (ticked) {
					cursor.setNewDestination(100, 280);
				} else {
					santa.setNewDestination(cursor.X, cursor.Y);
					if(cursor.X < 200) {
						cursor.setNewDestination(300, 260);
					} else if (santa.X == 300) {
						cursor.setNewDestination(SPAWNX, SPAWNY);
					}
				}
				break;
			case 4:
				santa.setNewDestination(santa.X,santa.Y);
				cursorHeld = 0;
				cursor.X = 160;
				cursor.Y = 160;
				cursor.setNewDestination(160,160);
				curTouchX = 160;
				curTouchY = 160;
				santa.fire();
				break;
			case 5:
				cursor.setNewDestination(20, 20);
				break;
			case 6:
				cursor.setNewDestination(50, 300);				
				break;
			case 7:
				santa.setNewDestination(160, 110);
				break;
			case 8:
				break;
			case 9:
				showMenu();
				break;
			}
		}
	}
	
	public void update(float delta) {
						
		if (!paused) {
			
			tick = System.currentTimeMillis();
			
			updateSteps();
			
			switch(gameState) {
			case 0:
				//do nothing
				break;
			case 1: //splash
				updateSplash(delta);
				break;
			case 2: //menu
				break;
			case 3: //tutorial mode
				santa.update(delta);
				updateTutorial(delta);
				break;
			case 4: //normal play
				santa.update(delta);					
							
				//if(Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT) || Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
				//	santa.fire();
				//}	
				addZombie();
				break;
			case 5: //level beginning
				if(tick > santa.levelStamp) {
					gameState = 4;
				}
				break;
			case 6: //dead
				if (tick > diedAt + RESPAWNTIMER) {
					if (santa.lives > 0) {
						santa.respawn();
						clearWorld(false);
						santa.startLevel(santa.level);
					} else {
						//game over bro
						wompWomp();
					}
				}
			case 7: //credits
				break;
			}
			
			
			if(gameState != 5) {iterateEntities(delta);}
		}		
	}	
	
	
	public void updateSteps() {
		
		if (tick > stepTick) {
			stepTick = tick + STEPTIMER;
			walkStep += 1;
			healthStep +=1;
			if (walkStep > 2) {walkStep = 0;}
			if (healthStep > 14) {healthStep = 0;}
			twoStep = 1 - twoStep;
		}
		
	}
	
	public void addZombie() {
		Coord c;
		int d;
		//Add a zombie every 3 seconds
		if (tick > zombieTimer) {
			c = new Coord(-1,-1);
			d = Random.getInt(4);
			switch (d) {
			case 0:
				c = getFreeCoord(0,480,0,20,16,16);
				break;
			case 1:
				c = getFreeCoord(0,480,300,320,16,16);
				break;
			case 2:
				c = getFreeCoord(0,40,0,320,16,16);
				break;
			case 3:
				c = getFreeCoord(460,480,0,320,16,16);
				break;				
			}
			if (c.X >= 0 && ((c.X <= 20 || c.X >= 460) || (c.Y <= 20 || c.Y >= 300)) && c.Y >= 0) {
				Zombie z = new Zombie(this, Random.getInt(7), c.X, c.Y);
				zombies.add(z);
				zombieTimer = tick + (int)(1000.0f * spawnModifier);
			}
		}
	}
		
	public void iterateEntities(float delta) {

		Bullet b;
		Iterator<Bullet> bulletsItr = bullets.iterator();		 
		while(bulletsItr.hasNext()) {			
			b = bulletsItr.next();
			b.update(delta);
			if(b.remove) {bulletsItr.remove();}			
		}

		Iterator<Effect> effectsItr = effects.iterator();
		Effect e;
		while(effectsItr.hasNext()) {
			e = effectsItr.next();
			e.update(delta);
			if(e.remove) {effectsItr.remove();}
		}	
		
		Iterator<Entity> entitiesItr = entities.iterator();
		Entity n;
		while(entitiesItr.hasNext()) {
			n = entitiesItr.next();
			n.update(delta);
			if(n.remove) {entitiesItr.remove();}
		}
		
		Iterator<Zombie> zombiesItr = zombies.iterator();
		Zombie z;
		while(zombiesItr.hasNext()) {
			z = zombiesItr.next();
			z.update(delta);
			if(z.remove) {zombiesItr.remove();}
		}
		
		Iterator<Blood> bloodItr = blood.iterator();
		Blood p;
		while(bloodItr.hasNext()) {
			p = bloodItr.next();
			p.update(delta);
			if(p.remove) {bloodItr.remove();}
		}
		
		Iterator<Spawner> caneItr = spawners.iterator();
		Spawner cc;
		while(caneItr.hasNext()) {
			cc = caneItr.next();
			cc.update(delta);
			if(cc.remove) {caneItr.remove();}
		}
		
		Iterator<Pickup> pickupItr = pickups.iterator();
		Pickup pu;
		while(pickupItr.hasNext()) {
			pu = pickupItr.next();
			pu.update(delta);
			if(pu.remove) {pickupItr.remove();}
		}
		
		Iterator<Snowman> snowmenItr = snowmen.iterator();
		Snowman s;
		while(snowmenItr.hasNext()) {
			s = snowmenItr.next();
			s.update(delta);
			if(s.remove) {snowmenItr.remove();}
		}	
	}
	
	public void clearWorld(boolean mapToo) {
		
		pickups.clear();
		blood.clear();
		zombies.clear();
		effects.clear();
		bullets.clear();
		snowmen.clear();
		if(mapToo) {
			entities.clear();
			spawners.clear();
		} else {
			for(Spawner s : spawners) {
				s.take();		
			}
		}
	}
	
	public void wompWomp() {
		//this is the game over function. womp womp
		clearWorld(true);
		newGame();
	}
			
	public Coord getFreeCoord(float startX, float endX, float startY, float endY, int colWidth, int colHeight) {
		
		float x = 0, y = 0;
		Coord c;
		Zombie m;
		
		boolean found = false;
		int out = 0;
		while (!found && out < 10000) {
			x = (int) (Math.random() * (endX - startX))+startX;
			y = (int) (Math.random() * (endY - startY))+startY;
			m = new Zombie(this,0,x,y);
			m.collisionWidth = colWidth;
			m.collisionHeight = colHeight;
			m.collidable = true;
			m.moveX = x;
			m.moveY = y;
			m.checkMove();
			if(!(m.collidedX) && !(m.collidedY) && Coord.distance(x, y, XMASTREEX, XMASTREEY) > 70) {
				found = true;
			}
			out++;
		}
		
		if (out > 9999) {
			x = -1;
			y = -1;
		}
		c = new Coord(x,y);
		return c;
		
	}
	
	public void keyDown(int keycode) {
		isKeyDown[keycode] = true;
	}
	
	public void keyUp(int keycode) {
		isKeyDown[keycode] = false;
	}
	
	public void touchDown(int X, int Y, int pointer, int button) {
		
		if (X < 0) {X = 0;}
		if (Y < 0) {Y = 0;}
		if (X > gameWidth) {X = (int) gameWidth;}
		if (Y > gameHeight) {Y = (int) gameHeight;}
		
		debug(X,Y);
		switch(gameState) {
		case 2:
			if (X > 130 && X < 350) {
				for(int b = 0; b < 3; b++) {			
					if (Y > 177+(b*45) && Y < 213+(b*45)) {
						switch (b) {
						case 0: //new game
							newGame();
							break;
						case 1: //tutorial
							startTutorial();
							break;
						case 2: //credits
							gameState = 7;
							break;
						
						}
					}
				}
			}
			break;
		case 4:
			if (X <= 70 && Y <= 32) {		
				santa.curWeapon += 1;
				if (santa.curWeapon > 3) {santa.curWeapon = 0;}
			} else {
				if (isTouchDown == false) {
					touchStamp = tick; //mark the time the touch began
					isTouchDown = true;
					initialTouchX = X;
					initialTouchY = Y;
					curTouchX = X;
					curTouchY = Y;
				} 
			}
			break;	
		case 7:
			if (X > 130 && X < 350) {
				if (Y > 177+(2*45) && Y < 213+(2*45)) {
					showMenu();
					
				}
			}
			break;
		}
		
		
	}
	
	public void touchDragged(int X, int Y, int pointer) {
		
		if (X < 0) {X = 0;}
		if (Y < 0) {Y = 0;}
		if (X > gameWidth) {X = (int) gameWidth;}
		if (Y > gameHeight) {Y = (int) gameHeight;}
		
		curTouchX = X;
		curTouchY = Y;
		
	}
		
	public void touchUp(int X, int Y, int pointer, int button) {		
		if (X < 0) {X = 0;}
		if (Y < 0) {Y = 0;}
		if (X > gameWidth) {X = (int) gameWidth;}
		if (Y > gameHeight) {Y = (int) gameHeight;}
		if (isTouchDown) {
			switch(gameState) {
			case 4:				
				if(tick - touchStamp < 200) {	
					//santa.dir = Movable.getDir(santa.X, santa.Y, X - santa.X, Y - santa.Y);
					santa.fire();
				}
				santa.setNewDestination(santa.X,santa.Y);
				break;
			}
		}				
		if (gameState != 3) {isTouchDown = false;}
	}
	
	public void randomTrees() {
		
		float x,y;
		Coord c;
		for(int i = 0; i < 7; i++) {
			c = getFreeCoord(40,440,40,280,50,50);
			x = c.X;
			y = c.Y;
			addTree(x,y);
		}
		
	}
	
	public void addCanes() {
	/*
		float x,y;
		Coord c;
		for(int i = 0; i < 4; i++) {
			c = getFreeCoord(100,380,100,220,32,32);
			x = c.X;
			y = c.Y;
			addCane(x,y);
		}
		*/
		addCane(160,110,0);
		addCane(160,190,0);
		addCane(320,110,0);
		addCane(320,190,8);
		
	}
	
	public void randomSnowmen() {
		
		float x,y;
		Coord c;
		c = getFreeCoord(40,440,40,280,50,50);
		x = c.X;
		y = c.Y;
		addSnowman(x,y);
		
	}
	
	public void addTree(float x, float y) {

		Entity e = new Entity();
		e.tile = Random.getInt(3) + 2;
		e.width = 32;
		e.height = 64;
		e.collisionWidth = 8;
		e.collisionHeight = 8;
		e.collisionYOff = 16;
		e.X = x;
		e.Y = y;
		
		entities.add(e);

	}
	
	public void addSnowman(float x, float y) {

		Entity e = new Entity();
		e.tile = 6;
		e.width = 32;
		e.height = 64;
		e.collisionWidth = 8;
		e.collisionHeight = 4;
		e.collisionYOff = 8;
		e.X = x;
		e.Y = y;
		
		entities.add(e);

	}
	
	public void addXmasTree(float x, float y) {

		Entity e = new Entity();
		e.tile = 0;
		e.width = 32;
		e.height = 64;
		e.collisionWidth = 8;
		e.collisionHeight = 8;
		e.collisionYOff = 16;
		e.X = x;
		e.Y = y;
		
		entities.add(e);

	}
	
	public void addCane(float x, float y, int p) {

		Entity e = new Entity();
		e.tile = 5;
		e.width = 32;
		e.height = 64;
		e.collisionWidth = 2;
		e.collisionHeight = 2;
		e.collisionYOff = 16;
		e.X = x;
		e.Y = y;
		
		entities.add(e);
		
		Spawner c = new Spawner(this,1);
		c.X = x;
		c.Y = y;
		if(p > 0) {c.stock(8);}
		spawners.add(c);
		
	}
	
	public Effect newEffect(int type, float X, float Y, int delay) {
		
		Effect e = new Effect(this, type, X, Y, delay);
		effects.add(e);
		return e;
		
	}
	
	public void newBullet(int type, float X, float Y, int dir, int delay) {
		
		Bullet b = new Bullet(this, type, X, Y, dir, delay);
		bullets.add(b);
		
	}
	
	public Blood newBlood(int type, float X, float Y, float delay, float TTL, float XVelocity, float YVelocity, float spread) {
		
		
		Blood b = new Blood(this,type, X,Y,(int) delay, (int) TTL,XVelocity,YVelocity,spread);
		blood.add(b);
		return b;
	}
	
	public void newExplosion(int type, float X, float Y, float intensity, float range) {
		
		float d;
		float dam;
		
		for(Zombie z : zombies) {			
			d = Coord.distance(X, Y, z.X, z.Y);			
			if (d < range) {
				
				//lets use an even distribution from center to edge
				dam = (int) (intensity * ((range-d)/range));
				z.damage((int)dam,4);
				debug(dam);
				
			}			
		}
		
	}
	
	public boolean clearShot(float startX, float endX, float startY, float endY) {
		for(Entity e : entities) {
			if(e.X+e.collisionWidth >= startX) {
				if (e.X-e.collisionWidth <= endX) {
					if (e.Y+e.collisionHeight+4+e.collisionYOff >= startY) {
						if (e.Y-e.collisionHeight-4+e.collisionYOff <= endY) {
							//debug(,e.Y-e.collisionHeight-e.collisionYOff);
							return false;
						}
							
					}
				}
			}
		}
		return true;
	}

	public void addCompanion(int type, float X, float Y) {
		boolean found = false;
		for(int a = -50; a < 50; a++) {
			for(int b = -50; b < 50; b++) {
				if(!found) {
					Snowman s = new Snowman(this,X+a,Y+b);
					s.collidable = true;
					s.moveX = X+a;
					s.moveY = Y+b;
					s.checkMove();
					if(!(s.collidedX) && !(s.collidedY)) {
						snowmen.add(s);
						for(int x = 0; x < s.X + 16; x+=16) {
							newEffect(8,x,s.Y,x/2);
							newEffect(8,x,s.Y+8,x/2);
							newEffect(8,x,s.Y+16,x/2);
							newEffect(8,x,s.Y+24,x/2);			
						}
						for(int i = 0; i < 60; i++) {
							newEffect(7,s.X + Random.getInt(32) - 8,s.Y + Random.getInt(32),i*4);
						}
						found = true;
					}
				}
			}
		}
	}
	
	
	public void debug(String s) {
		System.out.println(s);
	}
	public void debug(float a) {
		debug(Float.toString(a));
	}
	public void debug(float a, float b) {		
		debug(Float.toString(a) + "," + Float.toString(b));
	}
	public void debug(float a, float b, float c) {		
		debug(Float.toString(a) + "," + Float.toString(b) + "," + Float.toString(c));
	}
	
}
