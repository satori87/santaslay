package com.Teamic.GameObjects;

import com.Teamic.GameWorld.GameWorld;
import com.Teamic.Helpers.Random;

public class Spawner extends Entity {
	
	GameWorld world;
	
	public long effectStamp = 0;
	public long stockStamp = 0;
	public final int STOCKTIME = 5000;
	public final float STOCKRANGEPERCENTAGE = 0.15f;
	
	public int spawnerType = 0; //1 = candy cane
	
	public boolean stocked = false;
	
	public Spawner(GameWorld world, int type) {
		this.world = world;
		spawnerType = type;
	}
	
	public void update(float delta) {
		super.update(delta);
		

		if (world.tick > effectStamp && stocked) {
			effectStamp = world.tick + 500;
			world.newEffect(5, X+8, Y+14, 0);
		}
		
		if(world.tick > stockStamp && !stocked) {
			stock(0);
			

		}
		
	}
	
	public void stock(int i) {
		
		int r=0,r2=0,r3 = 0;
		int t = 1;
		int w = 0, h = 0;
		
		stocked = true;
		
		switch (spawnerType) {
		case 1:
			r = Random.getInt(10);
			r2 = Random.getInt(10);
			r3 = Random.getInt(10);
	
			switch(r) {
			case 0:
				t = 1; //candy canes
				break;
			case 1:
				t = 1; //more canes
				break;
			case 2:
				t = 1; //moar canes
				break;
			case 3:
				t = 6; //LIGHTS
				break;
			case 4:
				t = 6; //LIGHTS
				break;
			case 5:
				t = 1; //CANES
				break;
			case 6:
				if(r2 > 2) {
					t = 7; //rockets
					w = 4;
					h = -2;
				} else { //snowman!
					t = 8;
				}
				break;
			case 7:
				if(r2 == 7) {
					if(r3 == 7) { //THREE SEVENS WOO JACKPOT BABY
						t = 4; //1 up
					} else {
						t = 5; //big health
					}
					
				} else {
					t = 2; //heart
				}
				break;
			case 8:
				t = 3; //ornaments
				break;
			case 9:
				t = 3; //ornaments
				break;
			}
			break;
		}

		if(i > 0) {t = i;}
		
		world.newEffect(6, X+8, Y+14, 0);
		
		if (t == 3) { //special case for ornaments
			Pickup p;
			p = new Pickup(world,this,t,X+8+w,Y+14+h,20000+Random.getInt(2500));
			world.pickups.add(p);
			p = new Pickup(world,this,t,X+8+w,Y+14+h,20000+Random.getInt(2500));
			world.pickups.add(p);
			p = new Pickup(world,this,t,X+8+w,Y+14+h,20000+Random.getInt(2500));
			world.pickups.add(p);
			p = new Pickup(world,this,t,X+8+w,Y+14+h,20000+Random.getInt(2500));
			world.pickups.add(p);
			p = new Pickup(world,this,t,X+8+w,Y+14+h,20000+Random.getInt(2500));
			world.pickups.add(p);
		} else {
			Pickup p = new Pickup(world,this,t,X+8+w,Y+14+h,20000+Random.getInt(2500));
			world.pickups.add(p);
		}
		
	}
	
	public void take() {
		
		stocked = false;
		stockStamp = world.tick + getStockTime();
	}
	
	public int getStockTime() {
		
		int stockVariance = (int) (STOCKTIME * STOCKRANGEPERCENTAGE);
		return STOCKTIME + Random.getInt(stockVariance * 2) - stockVariance;
		
	}
	
}
