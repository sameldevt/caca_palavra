package br.com.magna.caca_palavra.entities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import br.com.magna.caca_palavra.services.LogHandler;

public class Table {
	
	private char[][] matrix = new char[26][26];
	private List<String> words = new ArrayList<String>();
	
	public Table() {
		loadContent();
	}
	
	public char[][] getMatrix(){
		return matrix;
	}
	
	public List<String> getWords(){
		return words;
	}
	

	public char[][] updateMatrix(char[][] newMatrix) {
		matrix = newMatrix;
		loadMatrix();
		loadRandomWords();
		
		return matrix;
	}
	
	public void printMatrix() {
		char lineCharacter = 65;
		
		System.out.print("   ");
		for(int column = 0; column < matrix.length; column++) {
			if(column > 9) {
				System.out.print(column + " ");
				continue;
			}
			System.out.print("0" + column + " ");
		}
		System.out.println();
		
		for (int line = 0; line < matrix.length; line++) {
			System.out.print(lineCharacter + "  ");
			for (int column = 0; column < matrix[line].length; column++) {
				System.out.print(" " + matrix[line][column] + " ");
			}
			lineCharacter++;
			System.out.println();
		}
	}
	
	private void loadContent() {
		matrix = new char[5][5];
		loadWordList();
		loadMatrix();
		loadRandomWords();
	}

	
	private void loadWordList() {
		try(Scanner scan = new Scanner(new File("util/words.txt"))){
			while(scan.hasNext()) {
				String line = scan.nextLine();
				words.add(line);
			}
		}
		catch(IOException e) {
			LogHandler.error("Error reading archive 'util/words.txt'");
		}
	}
	
	private void loadMatrix() {
		Random randomNumber = new Random();

		for (int line = 0; line < matrix.length; line++) {
			for (int column = 0; column < matrix[line].length; column++) {
				matrix[line][column] = ' ';
				//matrix[line][column] = (char) randomNumber.nextInt(65, 90);
			}
		}
	}
	
	private void loadRandomWords() {
		loadHorizontalWords();
		loadVerticalWords();
	}

	private void loadHorizontalWords() {
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
	}

	private void loadVerticalWords() {
		Random radom = new Random();
		int randomWordId = 0;

		int wordCount = 0;
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
