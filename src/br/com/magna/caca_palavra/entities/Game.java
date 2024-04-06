package br.com.magna.caca_palavra.entities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JFrame;

public class Game extends JFrame implements KeyListener, Runnable{

	private static final long serialVersionUID = 1L;
	
	private Table t = new Table();
	//private Pattern regex = Pattern.compile("[a-zA-Z][0123456789]{2}");
	//private Scanner scan = new Scanner(System.in);
	public static Control keyPressed = null;

	private List<String> words = t.getWords();
	private List<String> foundWords = new ArrayList<String>();
	private Map<Character, Integer> map = new HashMap<>();

	private char[][] tableMatrix = t.getMatrix();
	private boolean isGameWon = false;

	private void configGameFrame() {
		setSize(100, 100);
		addKeyListener(this);
		setFocusable(true);
		setVisible(true);
	}
	
	public void start() {
		mapAlphabetToIndex();
		configGameFrame();
	}
	@Override
	public void run() {
		start();
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {
		move(e);
	}
	
	private void move(KeyEvent e) {
		TerminalHandler.clear();
		try {
			Control.lookup(e.getKeyCode()).action();
			
			verifyPositions(Cursor.pin1[0], Cursor.pin2[0], Cursor.pin1[1], Cursor.pin2[1]);
			
			if (verifyWinCondition()) {
				TerminalHandler.clear();
				TerminalHandler.printScreen(TerminalHandler.WIN);
				isGameWon = true;
				System.exit(0);
				printFoundWords();
			}
			
			t.printMatrix();
			printFoundWords();
		}
		catch(NullPointerException | InterruptedException ex) {
			
		}
	}
	
	private boolean verifyWinCondition() {
		return foundWords.size() == 5;
	}

	/*
	private boolean tryPosition() {
		System.out.println("\nDigite as posições que deseja (Ex: a01 a12)");
		System.out.print("> ");

		try {
			String input = scan.nextLine();

			if (regex.matcher(input).find()) {
				String formatedInput = input.toUpperCase();

				Character pos1LineChar = formatedInput.charAt(0);
				Integer pos1Column = Integer.parseInt(formatedInput.substring(1, 3));

				Character pos2LineChar = formatedInput.charAt(4);
				Integer pos2Column = Integer.parseInt(formatedInput.substring(5, 7));

				Integer pos1Line = map.get(pos1LineChar);
				Integer pos2Line = map.get(pos2LineChar);

				if (!verifyPositions(pos1Line, pos2Line, pos1Column, pos2Column)) {
					return false;
				}

				return true;
			}
		} catch (InputMismatchException e) {
			return false;
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}

		return false;
	}
	*/
	
	private boolean verifyPositions(int pos1Line, int pos2Line, int pos1Column, int pos2Column) {
		String secretWord = "";

		// check column
		if (pos1Line != pos2Line && pos1Column == pos2Column) {
			try {
				for (int line = pos1Line; line <= pos2Line; line++) {
					secretWord += tableMatrix[line][pos1Column];
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}

			if (words.contains(secretWord)) {
				for (int line = pos1Line; line <= pos2Line; line++) {
					tableMatrix[line][pos1Column] = '-';
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
					secretWord += tableMatrix[pos1Line][column];
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}

			if (words.contains(secretWord)) {
				for (int column = pos1Column; column <= pos2Column; column++) {
					tableMatrix[pos1Line][column] = '-';
				}
				Cursor.clearPins();
				foundWords.add(secretWord);
			}

			return true;
		}

		return false;
	}

	private void printFoundWords() {
		if (!isGameWon) {
			System.out.println("\nPossíveis palavras");
			for (String s : words) {
				if (foundWords.contains(s)) {
					System.out.print("{" + s + "} ");
					continue;
				}
				System.out.print(s + " ");
			}
			System.out.println();
		}
	}

	private void mapAlphabetToIndex() {
		Character character = 65;
		for (int i = 0; i < 26; i++) {
			map.put(character, i);
			character++;
		}
	}
}
