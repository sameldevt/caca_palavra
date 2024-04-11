package br.com.magna.caca_palavra.entities;

public class Cursor {
	private int xPosition;
	private int yPosition;
	private int[] pin1Cords;
	private int[] pin2Cords;
	private int totalPins;
	
	public Cursor() {
		xPosition = 0;
		yPosition = 0;
		pin1Cords = new int[] {-1, -1};
		pin2Cords = new int[] {-1, -1};
		totalPins = 0;
	}
	
	public void clearPins() {
		pin1Cords = new int[] {-1, -1};
		pin2Cords = new int[] {-1, -1};
		totalPins = 0;
	}
	
	public void resetPositionsAndTotalPins() {
		pin1Cords[0] = -1;
		pin1Cords[1] = -1;
		pin2Cords[0] = -1;
		pin2Cords[1] = -1;
		totalPins = 0;
	}

	public int getXPosition() {
		return xPosition;
	}

	public int getYPosition() {
		return yPosition;
	}

	public int[] getPin1Cords() {
		return pin1Cords;
	}

	public int[] getPin2Cords() {
		return pin2Cords;
	}

	public int getTotalPins() {
		return totalPins;
	}

	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}

	public void setPin1Cords(int[] pin1Cords) {
		this.pin1Cords = pin1Cords;
	}

	public void setPin2Cords(int[] pin2Cords) {
		this.pin2Cords = pin2Cords;
	}

	public void setTotalPins(int totalPins) {
		this.totalPins = totalPins;
	}
}
