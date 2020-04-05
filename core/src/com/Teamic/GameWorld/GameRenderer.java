package com.Teamic.GameWorld;

import java.util.ArrayList;

import com.Teamic.GameObjects.Blood;
import com.Teamic.GameObjects.Bullet;
import com.Teamic.GameObjects.Effect;
import com.Teamic.GameObjects.Entity;
import com.Teamic.GameObjects.Pickup;
import com.Teamic.GameObjects.Santa;
import com.Teamic.GameObjects.Snowman;
import com.Teamic.GameObjects.Zombie;
import com.Teamic.Helpers.AssetLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameRenderer {

	private GameWorld world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;

	private SpriteBatch batcher;

	public ArrayList<Entity>[] drawList;

	
	@SuppressWarnings("unused")
	private float gameHeight, gameWidth, viewWidth, viewHeight;
	private float originX;
	private float originY;

	BitmapFont font = new BitmapFont(true);
	
	@SuppressWarnings("unchecked")
	public GameRenderer(GameWorld world, float gameWidth, float gameHeight, float originX, float originY, float viewWidth, float viewHeight) {
		
		this.world = world;
		
		this.gameHeight = gameHeight;
		this.gameWidth = gameWidth;
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;
		this.originX = originX;
		this.originY = originY;
		
		drawList = new ArrayList[(int)gameHeight];
		
		for(int y = 0; y < gameHeight; y++) {
			drawList[y] =  new ArrayList<Entity>();
		}
		
		//Set up our camera, which handles the screen scaling, use viewWidth to include letterbox area
		cam = new OrthographicCamera();
		cam.setToOrtho(true, viewWidth, viewHeight);

		//Create our sprite batcher and shape renderer from the camera
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		
	}

	public void render(float runTime) {

		if(AssetLoader.loaded) {
			// Fill the entire screen with black, to prevent potential flickering.
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			
			batcher.begin(); // Begin SpriteBatch
	
			//Clear the drawList
			for(int i = 0; i < gameHeight; i++) {
				drawList[i].clear();
			}
			
			if(world.paused) {				
				//draw ONLY this if paused
				drawFontCentered(1,240,160,"PAUSED",true);				
			} else {				
				//Draw background and blood before the Y-sorted drawList to save resources
				drawBackground();
				drawBlood(false);				
				//In no particular order, add drawables to the drawList				
				drawEntities();			
				drawBlood(true);				
				//Now, draw our Y-sorted drawList
				drawSorted();				
				//Now draw everything foreground like dialog boxes, etc
				drawOverlay();
			}
			
			batcher.end(); // End SpriteBatch
					
			drawBars(); //letterbox, uses shapeRenderer
		}

	}
	
	public void drawEntities() {
		
		int t = 0;
		float X,Y;
		float r = 0;
		
		//insert a special case into drawList so Santa can be drawn directly at that time (for paper doll to work)
		if(world.santa != null && (world.gameState == 3 || world.gameState == 4 || world.gameState == 5)) {
			Entity e = new Entity();
			e.isSanta = true;
			int y = (int)(world.santa.Y + (world.santa.height/2));
			if (y < 0) {y = 0;}
			if (y >= 320) {y = 319;}
			drawList[y].add(e);
		}
		
		for (Entity e : world.entities) {
			if (e.tile == 0) {t = world.twoStep;} else {t = e.tile;}
			drawTexture(AssetLoader.tile[t],e.X,e.Y,e.width,e.height,true,0,e.considerYOff);	
		}		
		for (Pickup e : world.pickups) {
			drawTexture(AssetLoader.pickups[e.type-1],e.X+e.floatX,e.Y+e.floatY,e.width,e.height,true,0,e.considerYOff);	
		}	
		
		for (Snowman s : world.snowmen) {
			drawTexture(AssetLoader.snowman[s.dir][world.twoStep],s.X,s.Y,s.width,s.height,true,0,0);
		}
		
		for (Zombie z : world.zombies) {
			if(z.attackState == 0) {
				X = z.X; Y = z.Y;
			} else {X = z.attackX; Y = z.attackY;}
			drawTexture(AssetLoader.zombies[z.zombieType][z.dir][world.walkStep],X,Y,z.width,z.height,true,0,0);
		}
		
		for (Effect b : world.effects) {	
			if(b.type > 0 && !b.delayed) {
				if(b.dir == 2) {r = 270;}
				if(b.dir == 3) {r = 90;}
				if(b.dir == 1) {r = 180;}				
				drawTexture(AssetLoader.effects[b.type-1][b.frame],b.X,b.Y,b.width,b.height,true,r,0);
			}
		}
		
		for (Bullet b : world.bullets) {
		    drawTexture(AssetLoader.projectiles[b.bulletType-1][b.dir],b.X,b.Y,b.width,b.height, true,0,0);
		}
		
		if(world.gameState == 1) {
			
			if(world.splashState == 0) {
				drawTexture(AssetLoader.logo[0],0,50,480,64,false,0,0);
				//draw bear and honey
				drawTexture(AssetLoader.bear[2],world.honey.X,world.honey.Y,world.honey.width,world.honey.height,true,0,0);
				drawTexture(AssetLoader.bear[world.twoStep],world.bear.X,world.bear.Y,world.bear.width,world.bear.height,true,0,0);
			} else {
				
				//draw game logo
				if(world.splashState == 1) {
					drawTexture(AssetLoader.logo[0],0,50,480,64,false,0,0);
					drawTexture(AssetLoader.logo[1],240,138,96,32,true,0,0);
				}
				
				
				drawTexture(AssetLoader.logo[2],0,140-world.splashOffset,480,192,false,0,0);
				t = world.walkStep + 3;			
				if(t == 4) {
					t = 5;
				} else {
					if (t == 5) {
						t = 4;
					}
				}
				drawTexture(AssetLoader.logo[t],40,220-world.splashOffset,40,52,false,0,0);
			}
		} else if (world.gameState == 2) {
			drawTexture(AssetLoader.logo[2],0,140-world.splashOffset,480,192,false,0,0);
			t = world.walkStep + 3;			
			if(t == 4) {
				t = 5;
			} else {
				if (t == 5) {
					t = 4;
				}
			}
			drawTexture(AssetLoader.logo[t],40,220-world.splashOffset,40,52,false,0,0);
			
			//draw our MENU 
			float a,b;
			t = world.twoStep;
			a = 28;
			b = -10;
			drawFrame(t,144,200+b,192,12);
			drawFont(1,143+a,198+b,"NEW GAME");
			a = 0;
			b = 35;			
			drawFrame(1-t,144,200+b,192,12);
			drawFont(1,143+a,198+b,"HOW TO PLAY");
			a = 34;
			b = 80;			
			drawFrame(t,144,200+b,192,12);
			drawFont(1,143+a,198+b,"CREDITS");
			
		}
	
		
	}
	
	public void drawBlood(boolean foreground) {
		for (Blood z : world.blood) {
			if (z.foreground == foreground) {
				if(foreground) {
					drawTexture(AssetLoader.blood[z.bloodType],z.X,z.Y,z.width,z.height,true, z.rotation,0);
				} else {
					drawTextureDirectly(AssetLoader.blood[z.bloodType],z.X,z.Y,z.width,z.height,true, z.rotation);
				}
			}
		}
	}
	
	public void drawOverlay() {
		
		float a,b;
		String s = "";
		String s2 = "";
		switch(world.gameState) {
		case 1:
			//oops we did this somewhere else
			break;
		case 2:
			//oops we did this somewhere else
			break;
		case 3:
			drawHUD();
			//tutorial text here
			
			switch(world.tutorialState) {
			case 1:
				s = "You are Santa";
				break;
			case 2:
				s = "Click and hold on the screen";
				s2 = "and Santa will follow";
				break;
			case 3:
				s = "Drag around to maneuver";
				break;
			case 4:
				s = "Tap to shoot in 4 directions";
				break;
			case 5:
				s = "Tap the ammo indicator";
				s2 = "to change weapons";
				break;
			case 6:
				s = "If you run out of health, you die";
				s2 = "If you run out of lives, game over";
				break;
			case 7:
				s = "Collect ammo, health, and";
				s2 = "snowmen from candy canes";				
				break;
			case 8:
				s = "Defend against the horde";
				s2 = "as long as you can";
				break;				
			}
			drawFontCentered(0,240,200,s,false);
			drawFontCentered(0,240,220,s2,false);
			drawTextureDirectly(AssetLoader.cursor[world.cursorHeld],world.cursor.X,world.cursor.Y,23,26,false,0);
			break;
		case 4:
			drawHUD();
			break;
		case 5:
			drawHUD();
			drawFontCentered(1,240,160,"LEVEL " + Integer.toString(world.santa.level),true);
			break;
		case 6:
			drawHUD();
			if(world.santa.lives > 0) {
				drawFontCentered(1,240,160,"SEASONS BLEEDINGS",true);
			} else {
				drawFontCentered(1,240,160,"CHRISTMAS IS CANCELLED",true);
			}
			break;
		case 7:
			drawFontCentered(1,240,20,"Graphics",true);
			
			drawFontCentered(0,240,50,"Master484, StormtrooperJon, ZiNGOT",false);
			drawFontCentered(0,240,70,"Daniel Matuszewski, Curt, Amir027",false);
			drawFontCentered(0,240,90,"http://opengameart.org/users/buch",false);
			drawFontCentered(0,240,110,"Pentagonbuddy, Jalen, Gecky",false);
			
			drawFontCentered(1,240,140,"Sound / Music",true);
			
			drawFontCentered(1,240,200,"Created by", true);
			drawFontCentered(1,240,220,"Michael Whitlock", true);
			drawFontCentered(1,240,240,"(Big Bear)", true);
			
			a = 36;
			b = 80;			
			drawFrame(world.twoStep,144,200+b,192,12);
			drawFont(1,143+a,198+b,"GO BACK");
			
			break;
		}		
	}
	
	public void drawHUD() {
		
		if(world.santa == null) {return;}
		int a = 1;
		if(world.santa.reloadStamp-100 > world.tick || world.santa.ammo[world.santa.curWeapon] == 0) {a = 0;}
		drawFrame(a,12,12,48,12);

		drawTextureDirectly(AssetLoader.weaponsb[world.santa.curWeapon],6,5,57,25,false,0);
		drawFont(0,4,20,"x" + Integer.toString(world.santa.ammo[world.santa.curWeapon]));
		
		
		batcher.draw(AssetLoader.bar[0], originX,originY + 304,309,14);
		batcher.draw(AssetLoader.bar[2+world.healthStep], originX-265 + (((float)world.santa.hp / world.santa.maxhp) * 261.0f),originY+307,309,6);
		
		batcher.draw(AssetLoader.bar[1], originX,originY + 304,309,14);		
		drawFont(0,2,294,"HEALTH");
		
		int k = world.santa.totalKills + world.santa.kills;
		if (k > 9999) {k=9999;}
		String sk = Integer.toString(k);
		
		String s = "";
		if (k < 10) {
			s = "000" + sk;
		} else if (k < 100) {
			s = "00" + sk;
		} else if (k < 1000) {
			s = "0" + sk;
		}
		drawFont(0,366,4,s);
		drawFont(0,416,4,"KILLS");
		
		if(world.santa.lives > 0) {
			for(int i = 0; i < world.santa.lives; i++) {
				drawTextureDirectly(AssetLoader.pickups[3],316+(i*25),303,21,17,false,0);
			}
		}
		
	}
	
	public void drawFont(int type, float x, float y, String St) {

		char ch;
		int c;

		for (int i = 0; i < St.length(); i++) {			
			ch = St.charAt(i);
			ch = Character.toUpperCase(ch);
			c = (int) ch;
			if (AssetLoader.validChar[type][c]) {
				drawTextureDirectly(AssetLoader.font[type][c],x,y,AssetLoader.getFontWidth(type),AssetLoader.getFontHeight(type),false,0);
			}
			x += AssetLoader.getFontWidth(type) + 2;		
		}
		
	}
	
	public void drawFontCentered(int type, float x, float y, String St, boolean shadow) {

		char ch;
		int c;
		float nx = x - ((St.length() * (AssetLoader.getFontWidth(type) + 2)) / 2);
		float ny = y - (AssetLoader.getFontHeight(type) / 2);
		
		
		for (int i = 0; i < St.length(); i++) {			
			ch = St.charAt(i);
			ch = Character.toUpperCase(ch);
			c = (int) ch;
			if (AssetLoader.validChar[type][c]) {
				if (shadow) {drawTextureDirectly(AssetLoader.font[type][c],nx+1,ny+1,AssetLoader.getFontWidth(type),AssetLoader.getFontHeight(type),false,0);}
				drawTextureDirectly(AssetLoader.font[type][c],nx,ny,AssetLoader.getFontWidth(type),AssetLoader.getFontHeight(type),false,0);
			}
			nx += AssetLoader.getFontWidth(type) + 2;			
		}
		
	}
	
	public void drawText(float x, float y, String St, boolean shadow) {
		
		font.draw(batcher, St, originX + x, originY + y);
		
	}
	
	public void drawBars() {
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0,0,0,1);
		if (originY > 0) {
			shapeRenderer.rect(0, 0, viewWidth, originY); //Top bar
			shapeRenderer.rect(0, viewHeight - originY, viewWidth, originY); //Bottom bar
		} else if (originX > 0) {
			shapeRenderer.rect(0, 0, originX, viewHeight); //Left bar			
			shapeRenderer.rect(viewWidth - originX, 0, originX, viewHeight); //Right bar			
		}
		shapeRenderer.end();
		
	}
	
	public void drawTexture(TextureRegion region, float X, float Y, float width, float height, boolean centered, float rotation, float considerY)
	{
		if(region == null) {return;}
		int bucketY = (int) Y;
		//use originX and originY to account for letterbox
		
		if (Y >= 0 && Y < gameHeight) {
			Entity e = new Entity();
			e.tex = region;
			e.X = X;
			e.Y = Y;
			if (centered) {
				e.X -= (width / 2);
				e.Y -= (height /2);
			}
			e.width = (int) width;
			e.height = (int) height;
			e.rotation = rotation;
			
			bucketY += (int) (height / 2);
			bucketY += considerY;
			if(bucketY >= gameHeight) {bucketY = (int)gameHeight-1;}
			drawList[bucketY].add(e);
			
		}
		
	}

	public void drawTextureDirectly(TextureRegion region, float X, float Y, float width, float height, boolean centered, float rotation)
	{
		if(region == null) {return;}
		//use originX and originY to account for letterbox
		
		float eX, eY;
		eX = X + originX;
		eY = Y + originY;
		if (centered) {
			eX -= (width / 2);
			eY -= (height /2);
		}
		
		batcher.draw(region, eX, eY, width / 2, height / 2, width, height, 1, 1, rotation);
		
	}
			
	public void drawSorted() {
		
		for(int y = 0; y < gameHeight; y++) {
			for(Entity e : drawList[y]) {
				if(e.isSanta) {
					drawSanta();
				} else {
					if(e.tex != null) {
						drawTextureDirectly(e.tex,e.X,e.Y,e.width,e.height,false,e.rotation);
					}
				}
			}
		}
		
	}
	
	public void drawSanta() {

		int d, t;
		d = world.santa.dir;
		t = world.santa.curWeapon;
		Santa s = world.santa;
		
		float wX = s.X - (s.width / 2);
		float wY = s.Y - (s.height / 2);
		
		switch (s.dir) { 
		//these modifiers just help align the paperdoll weapons to santa
		//the walkstep variable is used to move the gun in time to santas movements
		case 0: //up
			wX += world.walkStep;
			wY -=  9;
			break;
		case 1: //down
			wX += world.walkStep;
			wY += 11;
			break;
		case 2: //left
			wX -= 10;
			wY += 5 + world.walkStep;
			break;
		case 3: //right
			wX += 12;
			wY += 5 + world.walkStep;
			break;
		}
		
		TextureRegion tex;
		
		if(t == 0) {
			tex = AssetLoader.weapons[t][d][1-s.firing];
		} else {			
			tex = AssetLoader.weapons[t][d][s.firing];
		}
		
		//draw weapon layer above/below santa based on direction
		//we dont use centered draw here because we calculated X/Y based on santa's height/width, not the weapon texture's
		if (world.santa.dir == 0 || world.santa.dir == 2) {drawTextureDirectly(tex, wX, wY,32,32,false,0);}
		drawTextureDirectly(AssetLoader.santa[world.santa.dir][world.walkStep], world.santa.X, world.santa.Y, world.santa.width, world.santa.height, true,0);
		if (world.santa.dir == 1 || world.santa.dir == 3) {drawTextureDirectly(tex, wX, wY,32,32,false,0);}
		
	}

	public void drawBackground() {
		if(world.gameState < 3 || world.gameState > 5) {return;}
		for(int y = 0; y < world.TILESHIGH; y++) {
			for (int x = 0; x < world.TILESWIDE; x++) {				
				drawTextureFromTex(AssetLoader.tiles[0], x*32, y*32, 32, 32, 0, 0, 32, 32, false, true);
			}
		}
		
	}
		
	public void drawTextureFromTex(Texture tex, float dX, float dY, float width, float height, int sX, int sY, int sWidth, int sHeight, boolean flipX, boolean flipY) {
		
		batcher.draw(tex, dX+originX, dY+originY, width, height, sX, sY, sWidth, sHeight, flipX, flipY);
				
	}
	
	public void drawFrame(int type, float X, float Y, float width, float height) {
		
		int x,y;
		
		//top left piece
		drawTextureDirectly(AssetLoader.frames[type][0],X-12,Y-12,12,12,false,0);
		
		//top left piece
		drawTextureDirectly(AssetLoader.frames[type][1],X+width,Y-12,12,12,false,0);
				
		//bottom left piece
		drawTextureDirectly(AssetLoader.frames[type][2],X-12,Y+height,12,12,false,0);
				
		//bottom right piece
		drawTextureDirectly(AssetLoader.frames[type][3],X+width,Y+height,12,12,false,0);
		
		//top piece
		for(x = (int)X; x < X + width; x++) {
			drawTextureDirectly(AssetLoader.frames[type][5],x,Y-12,1,12,false,0);
		}
		
		//bottom piece
		for(x = (int)X; x < X + width; x++) {
			drawTextureDirectly(AssetLoader.frames[type][6],x,Y+height,1,12,false,0);
		}
				
		//draw left side
		for(y = (int)Y; y < Y + height; y++) {
			drawTextureDirectly(AssetLoader.frames[type][7],X-12,y,12,1,false,0);
		}
		
		//draw right side
		for(y = (int)Y; y < Y + height; y++) {
			drawTextureDirectly(AssetLoader.frames[type][8],X+width,y,12,1,false,0);
		}
		
		//fill in jawn
		for(x = (int)X; x < X + width; x++) {
			for(y = (int)Y; y < Y + height; y++) {
				drawTextureDirectly(AssetLoader.frames[type][4],x,y,1,1,false,0);
			}
		}
		
	}
}
