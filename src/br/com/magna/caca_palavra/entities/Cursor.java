package br.com.magna.caca_palavra.entities;

public class Cursor {
	public static Integer xPosition;
	public static Integer yPosition;
	public static Integer[] pin1;
	public static Integer[] pin2;
	public static Integer pins;
	
	static {
		xPosition = 0;
		yPosition = 0;
		pin1 = new Integer[] {-1, -1};
		pin2 = new Integer[] {-1, -1};
		pins = 0;
	}
	
	public static void clearPins() {
		pin1 = new Integer[] {-1, -1};
		pin2 = new Integer[] {-1, -1};
		pins = 0;
	}
}
