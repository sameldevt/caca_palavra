package br.com.magna.caca_palavra.entities;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public enum Control {
	UP{
		@Override
		public void action() {
			--Cursor.xPosition;
		}
	},
	DOWN{
		@Override
		public void action() {
			++Cursor.xPosition;
		}
	},
	LEFT{
		@Override
		public void action() {
			--Cursor.yPosition;
		}
	},
	RIGHT{
		@Override
		public void action() {
			++Cursor.yPosition;
		}
	},
	SELECT{
		@Override
		public void action() {
			if(Cursor.pins > 1) {
				return;
			}
			
			if(Cursor.pins == 1) {
				Cursor.pin2[0] = Cursor.xPosition;
				Cursor.pin2[1] = Cursor.yPosition;
				Cursor.pins++;
				return;
			}
			
			if(Cursor.pins == 0) {
				Cursor.pin1[0] = Cursor.xPosition;
				Cursor.pin1[1] = Cursor.yPosition;
				Cursor.pins+=1;
				return;
			}
			
		}
	},
	CANCEL{
		@Override
		public void action() {
			Cursor.pin1[0] = -1;
			Cursor.pin1[1] = -1;
			Cursor.pin2[0] = -1;
			Cursor.pin2[1] = -1;
			Cursor.pins = 0;
		}
	};
	
	private static Map<Integer, Control> controls = new HashMap<Integer, Control>();
	
	static {
		controls.put(KeyEvent.VK_UP, Control.UP);
		controls.put(KeyEvent.VK_DOWN, Control.DOWN);
		controls.put(KeyEvent.VK_RIGHT, Control.RIGHT);
		controls.put(KeyEvent.VK_LEFT, Control.LEFT);
		controls.put(KeyEvent.VK_ENTER, Control.SELECT);
		controls.put(KeyEvent.VK_ESCAPE, Control.CANCEL);
	}
	
	public abstract void action() throws InterruptedException;
	
	public static Control lookup(Integer pos) {
		return controls.get(pos);
	}
}
