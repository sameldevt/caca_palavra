package br.com.magna.caca_palavra.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import br.com.magna.caca_palavra.services.LogHandler;

public class Game {

	private Table table = new Table();
	private Pattern regex = Pattern.compile("[a-zA-Z][0123456789]{2}");
	private Scanner scan = new Scanner(System.in);

	private List<String> words = table.getWords();
	private List<String> foundWords = new ArrayList<String>();
	private Map<Character, Integer> charsToIndex = new HashMap<>();

	private char[][] matrix = table.getMatrix();
	private boolean isTutorial = true;
	private boolean isGameWon = false;

	public void start() {
		mapAlphabetToIndex();
		loadTutorialWord();
		tutorialLoop();
	}

	private void loadTutorialWord() {
		matrix[0][0] = 'A';
		matrix[0][1] = 'Z';
		matrix[0][2] = 'U';
		matrix[0][3] = 'L';
	}

	private void tutorialLoop() {
		TerminalHandler.printScreen(TerminalHandler.HOW_TO_PLAY);
		scan.nextLine();

		while (isTutorial) {
			TerminalHandler.clear();
			TerminalHandler.printScreen(TerminalHandler.TUTORIAL);
			System.out.println("Encontre uma palavra na matriz abaixo\n");

			table.printMatrix();

			if (!tryPosition()) {
				System.out.println("Nenhuma palavra por aqui!");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e2) {
					LogHandler.error("Error handling thread: " + e2.getCause());
				}
			}

			if (verifyWinCondition()) {
				TerminalHandler.clear();
				endTutorial();
				break;
			}
		}

		gameLoop();
	}

	private void gameLoop() {
		while (!isGameWon) {
			TerminalHandler.clear();
			table.printMatrix();
			printFoundWords();

			if (!tryPosition()) {
				System.out.println("Nenhuma palavra por aqui!");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e2) {
					LogHandler.error("Error handling thread: " + e2.getCause());
				}
			}

			if (verifyWinCondition()) {
				isGameWon = true;
			}
		}

		TerminalHandler.clear();
		TerminalHandler.printScreen(TerminalHandler.WIN);
		printFoundWords();
		System.exit(0);
	}

	private void endTutorial() {
		matrix = table.updateMatrix(new char[26][26]);
		foundWords.clear();
		isTutorial = false;
		TerminalHandler.printScreen(TerminalHandler.WIN);
		System.out.println("Você finalizou o tutorial!");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			LogHandler.error("Error handling thread: " + e.getCause());
		}
	}

	private boolean verifyWinCondition() {
		if (isTutorial) {
			return foundWords.size() == 1 ? true : false;
		}

		return foundWords.size() == 5 ? true : false;
	}

	private boolean tryPosition() {
		System.out.println("\nDigite as coordenadas que deseja (Ex: a01 a12)");
		System.out.print("> ");

		try {
			String input = scan.nextLine();

			if (regex.matcher(input).find()) {
				String formatedInput = input.toUpperCase();

				Character pos1LineChar = formatedInput.charAt(0);
				Character pos2LineChar = formatedInput.charAt(4);

				Integer cordinate1Column = Integer.parseInt(formatedInput.substring(1, 3));
				Integer cordinate2Column = Integer.parseInt(formatedInput.substring(5, 7));
				Integer cordinate1Line = charsToIndex.get(pos1LineChar);
				Integer cordinate2Line = charsToIndex.get(pos2LineChar);

				if (!verifyPositions(cordinate1Line, cordinate1Column, cordinate2Line, cordinate2Column)) {
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

	private boolean verifyPositions(int cordinate1Row, int cordinate1Column, int cordinate2Row, int cordinate2Column) {
		if (!checkColumn(cordinate1Row, cordinate1Column, cordinate2Row, cordinate2Column)) {
			if (!checkLine(cordinate1Row, cordinate1Column, cordinate2Row, cordinate2Column)) {
				return false;
			}
		}

		return true;
	}

	private boolean checkColumn(int cordinate1Row, int cordinate1Column, int cordinate2Row, int cordinate2Column) {
		String secretWord = "";

		if (cordinate1Row != cordinate2Row && cordinate1Column == cordinate2Column) {
			try {
				for (int line = cordinate1Row; line <= cordinate2Row; line++) {
					secretWord += matrix[line][cordinate1Column];
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}

			if (words.contains(secretWord)) {
				for (int line = cordinate1Row; line <= cordinate2Row; line++) {
					matrix[line][cordinate1Column] = '-';
				}

				System.out.println("Você encontrou a palavra " + secretWord);
				try {
					Thread.sleep(750);
				} catch (InterruptedException e) {
					LogHandler.error("Error handling thread: " + e.getCause());
				}
				foundWords.add(secretWord);
				return true;
			}

		}

		return false;
	}

	private boolean checkLine(int cordinate1Row, int cordinate1Column, int cordinate2Row, int cordinate2Column) {
		String secretWord = "";
		if (cordinate1Row == cordinate2Row && cordinate1Column != cordinate2Column) {
			try {
				for (int column = cordinate1Column; column <= cordinate2Column; column++) {
					secretWord += matrix[cordinate1Row][column];
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}

			if (words.contains(secretWord)) {
				for (int column = cordinate1Column; column <= cordinate2Column; column++) {
					matrix[cordinate1Row][column] = '-';
				}

				System.out.println("Você encontrou a palavra " + secretWord);
				try {
					Thread.sleep(750);
				} catch (InterruptedException e) {
					LogHandler.error("Error handling thread: " + e.getCause());
				}
				foundWords.add(secretWord);
				return true;
			}
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

	private void mapAlphabetToIndex() {
		Character character = 65;
		for (int i = 0; i < 26; i++) {
			charsToIndex.put(character, i);
			character++;
		}
	}
}
