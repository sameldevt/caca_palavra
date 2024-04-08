package br.com.magna.caca_palavra.entities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Game extends JFrame implements KeyListener{

	private static final long serialVersionUID = 1L;
	
	private Table table = new Table();
	private char[][] matrix = table.getMatrix();
	private boolean isTutorial = true;
	
	public static Control keyPressed = null;
	private List<String> words = table.getWords();
	private List<String> foundWords = new ArrayList<String>();

	private boolean verifyWinCondition() {
		if(isTutorial) {
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
		if(isTutorial) {
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
			Control key = Control.lookup(e.getKeyCode());
			
			key.action();

			verifyPositions(Cursor.pin1[0], Cursor.pin2[0], Cursor.pin1[1], Cursor.pin2[1]);
			
			if (verifyWinCondition()) {
				TerminalHandler.clear();
				endTutorial();
				return;
			}
			
			table.printMatrix();
		}
		catch(NullPointerException | InterruptedException ex) {
			table.printMatrix();
		}
	}
	
	private void gameAction(KeyEvent e) {
		TerminalHandler.clear();
		try {
			Control key = Control.lookup(e.getKeyCode());
			
			key.action();

			verifyPositions(Cursor.pin1[0], Cursor.pin2[0], Cursor.pin1[1], Cursor.pin2[1]);
			
			if (verifyWinCondition()) {
				TerminalHandler.clear();
				TerminalHandler.printScreen(TerminalHandler.WIN);
				printFoundWords();
				System.exit(0);
			}
			
			table.printMatrix();
			printFoundWords();	
		}
		catch(NullPointerException | InterruptedException ex) {
			table.printMatrix();
			printFoundWords();	
		}
	}
	
	private void endTutorial() {
		matrix = table.updateMatrix(new char[26][26]);
		foundWords.clear();
		isTutorial = false;
		Cursor.clearPins();
		TerminalHandler.printScreen(TerminalHandler.WIN);
		System.out.println("Você finalizou o tutorial!");
		System.out.println("Pressione alguma tecla para começar o jogo");
	}
	
	private boolean verifyPositions(int pos1Line, int pos2Line, int pos1Column, int pos2Column) {
		String secretWord = "";

		// check column
		if (pos1Line != pos2Line && pos1Column == pos2Column) {
			try {
				for (int line = pos1Line; line <= pos2Line; line++) {
					secretWord += matrix[line][pos1Column];
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}

			if (words.contains(secretWord)) {
				for (int line = pos1Line; line <= pos2Line; line++) {
					matrix[line][pos1Column] = '-';
				}
				Cursor.clearPins();
				foundWords.add(secretWord);
			}

			return true;
		}
		// check line
		else if (pos1Line == pos2Line && pos1Column != pos2Column) {
			try {
				for (int column = pos1Column; column <= pos2Column; column++) {
					secretWord += matrix[pos1Line][column];
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}

			if (words.contains(secretWord)) {
				for (int column = pos1Column; column <= pos2Column; column++) {
					matrix[pos1Line][column] = '-';
				}
				Cursor.clearPins();
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
		for(String word : foundWords) {
			System.out.print(word + " ");
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
}
