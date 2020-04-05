package com.Teamic.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {

	public static Texture texture; //blank texture we use to load resources one at a time

	public static Texture tiles[];

	public static TextureRegion[] blood; //diff drops of blood for particle
	
	public static TextureRegion[] bar; //candy cane health bar
	
	public static TextureRegion[][] santa; //direction,walkstep
	
	public static TextureRegion[] tile; //presets for trees, etc
	
	public static TextureRegion[][][] weapons; //type, direction, firestate
	
	public static TextureRegion[][] projectiles; //type, direction
	
	public static TextureRegion[][] effects; //type, frame
	
	public static TextureRegion[] pickups;
	
	public static TextureRegion[][] font; //type, ascii code
	
	public static TextureRegion[][] frames; //type,piece
	
	public static TextureRegion[][][] zombies; //type,direction,walkstep
	
	public static TextureRegion[] weaponsb;
	
	public static TextureRegion[][] snowman; //direction, walkstep
	
	public static TextureRegion bear[];
	
	public static TextureRegion logo[];
	
	public static TextureRegion cursor[];
	
	
	public static boolean[][] validChar; //font, code
	
	public static boolean loaded = false;
	

	public static void load() {

		validChar = new boolean[2][256];
		

		projectiles = new TextureRegion[5][4];
		
		pickups = new TextureRegion[8];
		
		int i;
		blood = new TextureRegion[9];
		
		loadTexture("bear");
		bear = new TextureRegion[3];
		bear[0] = new TextureRegion(texture,0,0,64,64);
		bear[0].flip(false, true);
		bear[1] = new TextureRegion(texture,64,0,64,64);
		bear[1].flip(false, true);
		bear[2] = new TextureRegion(texture,0,64,40,37);
		bear[2].flip(false, true);
		
		loadTexture("logo");
		logo = new TextureRegion[6];
		logo[0] = new TextureRegion(texture,0,0,480,64);
		logo[0].flip(false, true);
		logo[1] = new TextureRegion(texture,0,64,192,64);
		logo[1].flip(false, true);
		logo[2] = new TextureRegion(texture,0,128,480,192);
		logo[2].flip(false, true);
		logo[3] = new TextureRegion(texture,0,320,40,52);
		logo[3].flip(false, true);
		logo[4] = new TextureRegion(texture,40,320,40,52);
		logo[4].flip(false, true);
		logo[5] = new TextureRegion(texture,80,320,40,52);
		logo[5].flip(false, true);
		
		
		loadTexture("santa");
		santa = new TextureRegion[4][3];
		for(int y = 0; y < 4; y++) {
			for(int x = 0; x < 3; x++) {
				santa[y][x] = new TextureRegion(texture, x * 36, y * 34, 36, 34); //santa has his own dimensions, make consts?
				santa[y][x].flip(false, true); //we have to flip every texture we load or it will be upside down. can also flip horizontally
			}
		}
		
		loadTexture("bar0");
		bar = new TextureRegion[18];	
		bar[0] = new TextureRegion(texture, 0, 0, 300, 14);
		bar[0].flip(false, true);
		bar[1] = new TextureRegion(texture, 0, 14, 300, 14);
		bar[1].flip(false, true);
		for (i = 0; i < 16; i++) {
			bar[i+2] = new TextureRegion(texture, 0, 28 + i * 14+6, 296, 6);
			bar[i+2].flip(false, true);
		}
		
		
		tiles = new Texture[3];
		tile = new TextureRegion[10];
		for(i = 0; i < 2; i++) {
			loadTexture("tiles" + Integer.toString(i+1));
			
			if(i == 1) {
				//get the blood			
				blood[0] = new TextureRegion(texture,8,115,5,4);
				blood[0].flip(false, true);
				blood[1] = new TextureRegion(texture,66,94,29,34);
				blood[1].flip(false, true);

				//94,128,31,35
			} else {
				tile[0] = new TextureRegion(texture,192,64,32,64); //xmas tree
				tile[1] = new TextureRegion(texture,224,64,32,64); //xmas tree alt frame
				tile[2] = new TextureRegion(texture,160,128,32,64); //tree 1
				tile[3] = new TextureRegion(texture,192,128,32,64); //tree 2
				tile[4] = new TextureRegion(texture,224,128,32,64); //tree 3
				tile[5] = new TextureRegion(texture,0,192,32,64); //candy cane
				tile[6] = new TextureRegion(texture,0,128,32,64); //snow man
				for(int q = 0; q < 7; q++) {
					tile[q].flip(false, true);
				}			
			}
			tiles[i] = texture;
		}
		

		loadTexture("zombiehead");
		blood[5] = new TextureRegion(texture,0,0,29,25);
		blood[5].flip(false, true);
		blood[6] = new TextureRegion(texture,29,0,4,3);
		blood[6].flip(false, true);
		
		
		zombies = new TextureRegion[7][4][3];

		for(i = 0; i < 6; i++) {
			loadTexture("zombie" + Integer.toString(i));
			if(i == 0) {
				blood[2] = new TextureRegion(texture,58,63,6,4);
				blood[2].flip(false, true);//58,63,6,4
				blood[3] = new TextureRegion(texture,13,111,5,5);
				blood[3].flip(false, true);
				blood[4] = new TextureRegion(texture,46,124,6,6);
				blood[4].flip(false, true);
			}
			for(int y = 0; y < 4; y++) {
				for(int x = 0; x < 3; x++) {
					zombies[i][y][x] = new TextureRegion(texture, x * 36, y * 36, 36, 36); 
					zombies[i][y][x].flip(false, true);
				}
			}
		}
		loadTexture("zombie6");
		for(int y = 0; y < 4; y++) {
			for(int x = 0; x < 3; x++) {
				zombies[6][y][x] = new TextureRegion(texture, x * 48, y * 48, 48, 48); 
				zombies[6][y][x].flip(false, true);
			}
		}
		
		snowman = new TextureRegion[4][3];
		loadTexture("snowman");
		for(int y = 0; y < 4; y++) {
			for(int x = 0; x < 3; x++) {
				snowman[y][x] = new TextureRegion(texture, x * 64, y * 64, 64, 64); 
				snowman[y][x].flip(false, true);
			}
		}
		blood[7] = new TextureRegion(texture,192,0,8,9);
		blood[7].flip(false, true);
		blood[8] = new TextureRegion(texture,192,64,19,19);
		blood[8].flip(false, true);
		for(int a = 0; a < 4; a++) {
			projectiles[4][a] = new TextureRegion(texture,192,0,8,9);
			projectiles[4][a].flip(false, true);
		}
		pickups[7] = new TextureRegion(texture,192,128,16,23);
		
		

		weapons = new TextureRegion[4][4][2];
		loadTexture("weapons");
		for(int a = 0; a < 4; a++) {
			for(int b = 0; b < 4; b++) {
				weapons[a][b][0] = new TextureRegion(texture,b * 32,a * 32, 32, 32);
				weapons[a][b][0].flip(false, true);
			}
		}

		loadTexture("weaponsf");
		for(int a = 0; a < 4; a++) {
			for(int b = 0; b < 4; b++) {
				weapons[a][b][1] = new TextureRegion(texture,b * 32,a * 32, 32, 32);
				weapons[a][b][1].flip(false, true);
			}
		}
		
		loadTexture("projectiles");
		for(int a = 0; a < 4; a++) {
			for(int b = 0; b < 4; b++) {
				projectiles[a][b] = new TextureRegion(texture,b * 20, a * 20, 20, 20);
				projectiles[a][b].flip(false, true);
			}
		}
		
		
		effects = new TextureRegion[8][8];
		loadTexture("effects");
		for(int a = 0; a < 8; a++) {
			for(int b = 0; b < 8; b++) {
				effects[a][b] = new TextureRegion(texture,b * 32, a * 32, 32, 32);
				effects[a][b].flip(false, true);
			}
		}
		
		int a, x,y;
		
		font = new TextureRegion[2][256];
		


		for(a = 0; a < 256; a++) {
			validChar[0][a] = false;
			validChar[1][a] = false;
			
		}
		
		for(int f = 0; f < 2; f++) {
			loadTexture("font" + Integer.toString(f));
			for(a = 64; a <= 90; a++) { // @ thru Z
				if(f == 0) {
					x = 1 + ((a-64) * 11);
					y = 4;
				} else {
					x = (a-64)*16;
					y = 0;
				}
				font[f][a] = new TextureRegion(texture, x, y, getFontWidth(f), getFontHeight(f));
				font[f][a].flip(false, true);
				validChar[f][a] = true;
				
		}
		for(a = 33; a <= 63; a++) { // ! thru ?
			if(f == 0) {
				x = 67 + ((a-33) * 11);
				y = 28;
			} else {
				x = (a-33)*16;
				y = 16;
				
			}
			font[f][a] = new TextureRegion(texture, x, y, getFontWidth(f), getFontHeight(f));
			font[f][a].flip(false, true);
			validChar[f][a] = true;
		}
		}
		
		
		frames = new TextureRegion[2][9];
		loadTexture("frames");
		for(i = 0; i < 2; i++) {
			for(int f = 0; f < 5; f++) {
				frames[i][f] = new TextureRegion(texture,f * 12, i * 12, 12, 12);
				frames[i][f].flip(false, true);
			}
			frames[i][5] =  new TextureRegion(texture,5 * 12, i * 12, 1, 12);
			frames[i][5].flip(false, true);
			frames[i][6] =  new TextureRegion(texture,6 * 12, i * 12, 1, 12);
			frames[i][6].flip(false, true);
			frames[i][7] =  new TextureRegion(texture,7 * 12, i * 12, 12, 1);
			frames[i][7].flip(false, true);
			frames[i][8] =  new TextureRegion(texture,8 * 12, i * 12, 12, 1);
			frames[i][8].flip(false, true);			
		}
		
		cursor = new TextureRegion[2];
		cursor[0] =  new TextureRegion(texture,0, 24, 23, 26);
		cursor[0].flip(false, true);
		cursor[1] =  new TextureRegion(texture,23, 24, 23, 26);
		cursor[1].flip(false, true);
		
		
		weaponsb = new TextureRegion[4];
		loadTexture("weaponsb");
		for(i = 0; i < 4; i++) {
			weaponsb[i] = new TextureRegion(texture,0,i*25,57,25);
			weaponsb[i].flip(false,true);
		}
		
		pickups[0] = new TextureRegion(texture,64,0,9,34); //cane
		pickups[1] = new TextureRegion(texture,73,0,17,17);//heart
		pickups[2] = new TextureRegion(texture,96,32,5,6);//ornament
		pickups[3] = new TextureRegion(texture,90,0,21,17);//1up
		pickups[4] = new TextureRegion(texture,111,0,17,17);//star (big health)
		pickups[5] = new TextureRegion(texture,64,36,6,32); //strand
		pickups[6] = new TextureRegion(texture,64,68,32,32);//gifts (rockets)
		
		
		for(i = 0; i < 8; i++) {
			pickups[i].flip(false, true);
		}
		
		loaded = true;
		
	}

	public static int getFontWidth(int type) {
		if (type == 0) {
			return 10;
		}
		return 16;
	}
	
	public static int getFontHeight(int type) {

		return 16;

	}
	
	public static void loadTexture(String name) {
		
		texture = new Texture(Gdx.files.internal("data/" + name + ".png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

	}
	
	public static void dispose() {
		// We must dispose of the texture when we are finished.
		texture.dispose();
	}

}