package br.com.magna.caca_palavra.entities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Game extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;

	private Table table = new Table();
	private Cursor gameCursor = table.getCursor();
	private char[][] matrix = table.getMatrix();
	private boolean isTutorial = true;

	private List<String> words = table.getWords();
	private List<String> foundWords = new ArrayList<String>();

	private boolean verifyWinCondition() {
		if (isTutorial) {
			return foundWords.size() == 1 ? true : false;
		}

		return foundWords.size() == 5 ? true : false;
	}

	private void configGameFrame() {
		addKeyListener(this);
		setFocusable(true);
		setVisible(true);
		setSize(100, 100);
	}

	public void start() {
		configGameFrame();
		loadTutorialWord();
		TerminalHandler.printScreen(TerminalHandler.HOW_TO_PLAY);
	}

	private void loadTutorialWord() {
		matrix[0][0] = 'A';
		matrix[0][1] = 'Z';
		matrix[0][2] = 'U';
		matrix[0][3] = 'L';
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (isTutorial) {
			tutorialAction(e);
			return;
		}

		gameAction(e);
	}

	private void tutorialAction(KeyEvent e) {
		TerminalHandler.clear();
		TerminalHandler.printScreen(TerminalHandler.TUTORIAL);
		System.out.println("Encontre uma palavra na matriz abaixo");

		try {
			int keyPressed = e.getKeyCode();
			Method action = ControlCursor.lookup(keyPressed);
			action.invoke(new ControlCursor(), gameCursor);
			table.updateCursor(gameCursor);

			int cursorPin1Line = gameCursor.getPin1Cords()[0];
			int cursorPin1Column = gameCursor.getPin1Cords()[1];
			int cursorPin2Line = gameCursor.getPin2Cords()[0];
			int cursorPin2Column = gameCursor.getPin2Cords()[1];

			verifyPositions(cursorPin1Line, cursorPin1Column, cursorPin2Line, cursorPin2Column);

			if (verifyWinCondition()) {
				TerminalHandler.clear();
				endTutorial();
				return;
			}

			table.printMatrix();
		} catch (NullPointerException e0) {
			table.printMatrix();
		} catch (IllegalAccessException e1) {
			LogHandler.error("Error accesing method from ControlCursor:" + e1.getMessage());
		} catch (InvocationTargetException e1) {
			LogHandler.error("Error invocating method from ControlCursor:" + e1.getMessage());
		}
	}

	private void gameAction(KeyEvent e) {
		TerminalHandler.clear();
		try {
			int keyPressed = e.getKeyCode();
			Method action = ControlCursor.lookup(keyPressed);

			action.invoke(new ControlCursor(), gameCursor);
			table.updateCursor(gameCursor);

			int cursorPin1Line = gameCursor.getPin1Cords()[0];
			int cursorPin1Column = gameCursor.getPin1Cords()[1];
			int cursorPin2Line = gameCursor.getPin2Cords()[0];
			int cursorPin2Column = gameCursor.getPin2Cords()[1];

			verifyPositions(cursorPin1Line, cursorPin1Column, cursorPin2Line, cursorPin2Column);

			if (verifyWinCondition()) {
				TerminalHandler.clear();
				TerminalHandler.printScreen(TerminalHandler.WIN);
				printFoundWords();
				System.exit(0);
			}

			table.printMatrix();
			printFoundWords();
		} catch (NullPointerException e0) {
			table.printMatrix();
			printFoundWords();
		} catch (IllegalAccessException e1) {
			LogHandler.error("Error accesing method from ControlCursor:" + e1.getMessage());
		} catch (InvocationTargetException e1) {
			LogHandler.error("Error invocating method from ControlCursor:" + e1.getMessage());
		}
	}

	private void endTutorial() {
		matrix = table.updateMatrix(new char[26][26]);
		foundWords.clear();
		isTutorial = false;
		gameCursor.clearPins();
		TerminalHandler.printScreen(TerminalHandler.WIN);
		System.out.println("Você finalizou o tutorial!");
		System.out.println("Pressione alguma tecla para começar o jogo");
	}

	private boolean verifyPositions(int cursorPin1Line, int cursorPin1Column, int cursorPin2Line, int cursorPin2Column) {
		if(!checkColumn(cursorPin1Line, cursorPin1Column, cursorPin2Line, cursorPin2Column)) {
			if(!checkLine(cursorPin1Line, cursorPin1Column, cursorPin2Line, cursorPin2Column)) {
				return false;
			}
		}
		
		return true;
	}

	private boolean checkColumn(int cursorPin1Line, int cursorPin1Column, int cursorPin2Line, int cursorPin2Column) {
		String secretWord = "";

		if (cursorPin1Line != cursorPin2Line && cursorPin1Column == cursorPin2Column) {
			try {
				for (int line = cursorPin1Line; line <= cursorPin2Line; line++) {
					secretWord += matrix[line][cursorPin1Column];
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}

			if (words.contains(secretWord)) {
				for (int line = cursorPin1Line; line <= cursorPin2Line; line++) {
					matrix[line][cursorPin1Column] = '-';
				}
				gameCursor.clearPins();
				foundWords.add(secretWord);
			}

			return true;
		}

		return false;
	}

	private boolean checkLine(int cursorPin1Line, int cursorPin1Column, int cursorPin2Line, int cursorPin2Column) {
		String secretWord = "";
		if (cursorPin1Line == cursorPin2Line && cursorPin1Column != cursorPin2Column) {
			try {
				for (int column = cursorPin1Column; column <= cursorPin2Column; column++) {
					secretWord += matrix[cursorPin1Line][column];
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}

			if (words.contains(secretWord)) {
				for (int column = cursorPin1Column; column <= cursorPin2Column; column++) {
					matrix[cursorPin1Line][column] = '-';
				}
				gameCursor.clearPins();
				foundWords.add(secretWord);
			}

			return true;
		}

		return false;
	}

	private void printFoundWords() {
		if (!verifyWinCondition()) {
			System.out.println("\nPossíveis palavras");
			for (String word : words) {
				if (foundWords.contains(word)) {
					System.out.print("{" + word + "} ");
					continue;
				}
				System.out.print(word + " ");
			}
			System.out.println();
		}

		System.out.println("Palavras encontradas");
		for (String word : foundWords) {
			System.out.print(word + " ");
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
