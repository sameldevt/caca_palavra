package br.com.magna.caca_palavra.entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Table {

	private char[][] matrix;
	private List<String> words = new ArrayList<String>();

	public Table() {
		loadContent();
	}

	public char[][] updateMatrix(char[][] newMatrix) {
		matrix = newMatrix;
		loadGameMatrix();
		loadRandomWords();
		
		return matrix;
	}
	
	private void loadContent() {
		matrix = new char[5][5];
		loadWordList();
		loadGameMatrix();
		loadRandomWords();
	}
	
	public char[][] getMatrix() {
		return matrix;
	}

	public List<String> getWords() {
		return words;
	}

	public void printMatrix() {
		char lineCharacter = 65;
		
		System.out.print("    ");
		for(int column = 0; column < matrix.length; column++) {
			if(column >= 9) {
				System.out.print(column + " ");
				continue;
			}
			System.out.print(column + "  ");
		}
		System.out.println();
		
		for (int line = 0; line < matrix.length; line++) {
			System.out.print(lineCharacter + "  ");
			for (int column = 0; column < matrix[line].length; column++) {

				if (Cursor.pin1[0] == line && Cursor.pin1[1] == column) {
					System.out.print("{" + matrix[line][column] + "}");
					continue;
				}

				if (Cursor.pin2[0] == line && Cursor.pin2[1] == column) {
					System.out.print("{" + matrix[line][column] + "}");
					continue;
				}

				if (Cursor.xPosition == line && Cursor.yPosition == column) {
					System.out.print("{" + matrix[line][column] + "}");
					continue;
				}

				System.out.print(" " + matrix[line][column] + " ");
			}
			lineCharacter++;
			System.out.println();
		}
	}

	private void loadGameMatrix() {
		Random randomNumber = new Random();

		for (int line = 0; line < matrix.length; line++) {
			for (int column = 0; column < matrix[line].length; column++) {
				//matrix[line][column] = ' ';
				matrix[line][column] = (char) randomNumber.nextInt(65, 90);
			}
		}
	}

	private void loadWordList() {
		try (Scanner scan = new Scanner(new File("util/words.txt"))) {
			while (scan.hasNext()) {
				String line = scan.nextLine();
				words.add(line);
			}
		} catch (IOException e) {
			throw new RuntimeException("Error reading archive 'util/words.txt'");
		}
	}
	
	private void loadRandomWords() {
		Random radom = new Random();
		int randomWordId = 0;
		int wordCount = 0;

		while (wordCount < 10) {
			randomWordId = radom.nextInt(0, words.size() - 1);
			String randomWord = words.get(randomWordId).toUpperCase();

			int randomLine = radom.nextInt(0, matrix.length);
			int randomColumn = radom.nextInt(0, matrix.length);

			if (matrix.length - randomColumn > randomWord.length()) {
				for (int column = randomColumn, charPos = 0; charPos < randomWord.length(); column++, charPos++) {
					matrix[randomLine][column] = randomWord.charAt(charPos);
				}
			}
			wordCount++;
		}

		wordCount = 0;
		while (wordCount < 10) {
			randomWordId = radom.nextInt(0, words.size() - 1);
			String randomWord = words.get(randomWordId).toUpperCase();

			int randomLine = radom.nextInt(0, matrix.length);
			int randomColumn = radom.nextInt(0, matrix.length);

			if (matrix.length - randomLine > randomWord.length()) {
				for (int line = randomLine, charPos = 0; charPos < randomWord.length(); line++, charPos++) {
					matrix[line][randomColumn] = randomWord.charAt(charPos);
				}
			}
			wordCount++;
		}
	}
}
