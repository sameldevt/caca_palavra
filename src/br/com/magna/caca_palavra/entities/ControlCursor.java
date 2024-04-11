package br.com.magna.caca_palavra.entities;

import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ControlCursor {

	private static Map<Integer, Method> controls = new HashMap<Integer, Method>();

	static {
		try {
			controls.put(KeyEvent.VK_UP, ControlCursor.class.getDeclaredMethod("moveUp", Cursor.class));
			controls.put(KeyEvent.VK_DOWN, ControlCursor.class.getDeclaredMethod("moveDown", Cursor.class));
			controls.put(KeyEvent.VK_RIGHT, ControlCursor.class.getDeclaredMethod("moveRight", Cursor.class));
			controls.put(KeyEvent.VK_LEFT, ControlCursor.class.getDeclaredMethod("moveLeft", Cursor.class));
			controls.put(KeyEvent.VK_ENTER, ControlCursor.class.getDeclaredMethod("selectPosition", Cursor.class));
			controls.put(KeyEvent.VK_ESCAPE, ControlCursor.class.getDeclaredMethod("removeSelections", Cursor.class));
		} catch (NoSuchMethodException e) {
			LogHandler.error("Couldn't find method " + e.getMessage());
		}
	}

	public static Method lookup(int key) {
		return controls.get(key);
	}

	public void moveRight(Cursor cursor) {
		int currentYPosition = cursor.getYPosition();
		cursor.setyPosition(currentYPosition + 1);
	}

	public void moveLeft(Cursor cursor) {
		int currentYPosition = cursor.getYPosition();
		cursor.setyPosition(currentYPosition - 1);

	}

	public void moveUp(Cursor cursor) {
		int currentXPosition = cursor.getXPosition();
		cursor.setxPosition(currentXPosition - 1);

	}

	public void moveDown(Cursor cursor) {
		int currentXPosition = cursor.getXPosition();
		cursor.setxPosition(currentXPosition + 1);
	}

	public void selectPosition(Cursor cursor) {
		int totalPins = cursor.getTotalPins();
		int cursorXPosition = cursor.getXPosition();
		int cursorYPosition = cursor.getYPosition();
		int[] cursorCords = new int[] { cursorXPosition, cursorYPosition };

		if (totalPins > 1) {
			return;
		}

		if (totalPins == 0) {
			cursor.setPin1Cords(cursorCords);
			cursor.setTotalPins(totalPins + 1);
			return;
		}

		if (totalPins == 1) {
			cursor.setPin2Cords(cursorCords);
			cursor.setTotalPins(totalPins + 1);
			return;
		}
	}

	public void removeSelections(Cursor cursor) {
		cursor.resetPositionsAndTotalPins();
	}
}
