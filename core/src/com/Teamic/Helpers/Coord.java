package com.Teamic.Helpers;

public class Coord {

	public float X = 0, Y = 0;	
	
	public Coord(float X, float Y) {
		this.X = X;
		this.Y = Y;
	}
	
	public static float distance(float X1, float Y1, float X2, float Y2) {
		return (float) Math.pow(Math.pow((X1 - X2),2) + Math.pow((Y1 - Y2),2),0.5);
	}
	
	public static String St(float X, float Y) {
		return "(" + Integer.toString((int)X) + ", " + Integer.toString((int)Y) + ")";
	}
	
}
